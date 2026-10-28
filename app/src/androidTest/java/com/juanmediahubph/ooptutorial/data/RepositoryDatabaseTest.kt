package com.juanmediahubph.ooptutorial.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juanmediahubph.ooptutorial.data.local.OopDatabase
import com.juanmediahubph.ooptutorial.data.repository.AttemptRequest
import com.juanmediahubph.ooptutorial.data.repository.OopRepository
import com.juanmediahubph.ooptutorial.data.seed.SeedContent
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryDatabaseTest {
    private lateinit var database: OopDatabase
    private lateinit var repository: OopRepository

    @Before fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, OopDatabase::class.java).build()
        repository = OopRepository(database)
    }

    @After fun tearDown() = runBlocking { database.close() }

    @Test fun seedImportIsIdempotentAndPreservesProgressAndBookmarks() = runBlocking {
        SeedContent.import(database)
        repository.setCompleted("m01-l01", true)
        repository.toggleBookmark("m01-l01")
        SeedContent.import(database)
        assertEquals(12, database.contentDao().modulesOnce().size)
        assertNotNull(database.learningDao().progressFor("m01-l01"))
        assertNotNull(database.learningDao().bookmarkFor("m01-l01"))
        assertEquals(144, database.contentDao().questions().size)
    }

    @Test fun answersAndQuestionSnapshotsSurviveSubmissionAndDuplicateSubmission() = runBlocking {
        SeedContent.import(database)
        val id = repository.newAttempt(AttemptRequest("PRACTICE", setOf("m01"), questionCount = 5, passThreshold = 70))
        val questions = repository.getAttemptQuestions(id)
        assertEquals(5, questions.size)
        assertEquals(5, questions.map { it.questionId }.distinct().size)
        val orderedOptions = OopRepository.decodeOptions(questions.first().optionsSnapshot)
        val option = orderedOptions.first()
        repository.saveAnswer(id, 0, option.id)
        val result = repository.submit(id)!!
        assertNotNull(result.completedAt)
        val restored = repository.getAttemptQuestions(id).first()
        assertNotNull(restored.selectedOptionId)
        assertEquals(orderedOptions, OopRepository.decodeOptions(restored.optionsSnapshot))
        assertEquals(option.id, restored.selectedOptionId)
        assertEquals(result, repository.submit(id))
        assertNull(repository.getAttempt("missing"))
    }

    @Test fun oneActiveAttemptIsEnforcedAndExpiredDeadlineFinalizes() = runBlocking {
        SeedContent.import(database)
        val id = repository.newAttempt(AttemptRequest("EXAM", setOf("m01"), questionCount = 10, durationMinutes = 15, passThreshold = 70))
        val rejected = runCatching {
            repository.newAttempt(AttemptRequest("PRACTICE", setOf("m02"), questionCount = 5, passThreshold = 70))
        }
        assertTrue(rejected.isFailure)
        val expiredDeadline = System.currentTimeMillis() - 1
        val startedAt = repository.getAttempt(id)!!.startedAt
        database.openHelper.writableDatabase.execSQL(
            "UPDATE attempts SET deadlineAt = ? WHERE id = ?",
            arrayOf(expiredDeadline, id)
        )
        repository.recoverExpiredAttempt()
        assertEquals("TIMED_OUT", repository.getAttempt(id)?.status)
        assertEquals(expiredDeadline - startedAt, repository.getAttempt(id)?.elapsedMillis)
    }

    @Test fun historicalQuestionSnapshotDoesNotChangeWhenBundledQuestionIsEdited() = runBlocking {
        SeedContent.import(database)
        val id = repository.newAttempt(AttemptRequest("PRACTICE", setOf("m01"), questionCount = 5, passThreshold = 70))
        val snapshot = repository.getAttemptQuestions(id).first()
        val source = database.contentDao().questions().first { it.id == snapshot.questionId }
        database.contentDao().putQuestions(listOf(source.copy(prompt = "Updated bundled wording")))
        assertEquals(snapshot.questionTextSnapshot, repository.getAttemptQuestions(id).first().questionTextSnapshot)
        assertTrue(snapshot.questionTextSnapshot != "Updated bundled wording")
    }

    @Test fun activeAttemptAndSavedAnswerCanBeRecoveredByANewRepository() = runBlocking {
        SeedContent.import(database)
        val id = repository.newAttempt(AttemptRequest("EXAM", setOf("m02"), questionCount = 10, durationMinutes = 15, passThreshold = 70))
        val optionId = OopRepository.decodeOptions(repository.getAttemptQuestions(id).first().optionsSnapshot).last().id
        repository.saveAnswer(id, 0, optionId)
        val reopenedRepository = OopRepository(database)
        assertEquals(id, reopenedRepository.getAttempt(id)?.id)
        assertEquals(id, database.attemptDao().activeAttemptOnce()?.id)
        assertEquals(optionId, reopenedRepository.getAttemptQuestions(id).first().selectedOptionId)
    }

    @Test fun resetProgressRetainsBookmarksAndHistoryButResetAllClearsThem() = runBlocking {
        SeedContent.import(database)
        repository.setCompleted("m01-l01", true)
        repository.toggleBookmark("m01-l01")
        val id = repository.newAttempt(AttemptRequest("PRACTICE", setOf("m01"), questionCount = 5, passThreshold = 70))
        repository.submit(id)
        repository.updateSetting("theme", "DARK")
        repository.resetProgress()
        assertNull(database.learningDao().progressFor("m01-l01"))
        assertNotNull(database.learningDao().bookmarkFor("m01-l01"))
        repository.resetAll()
        assertNull(database.learningDao().bookmarkFor("m01-l01"))
        assertNull(repository.getAttempt(id))
        assertEquals(12, database.contentDao().modulesOnce().size)
        assertEquals("SYSTEM", repository.setting("theme", "DARK"))
    }
}
