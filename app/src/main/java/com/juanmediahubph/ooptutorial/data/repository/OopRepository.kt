package com.juanmediahubph.ooptutorial.data.repository

import androidx.room.withTransaction
import com.juanmediahubph.ooptutorial.data.local.AppSettingEntity
import com.juanmediahubph.ooptutorial.data.local.AttemptEntity
import com.juanmediahubph.ooptutorial.data.local.AttemptQuestionEntity
import com.juanmediahubph.ooptutorial.data.local.BookmarkEntity
import com.juanmediahubph.ooptutorial.data.local.LessonProgressEntity
import com.juanmediahubph.ooptutorial.data.local.OopDatabase
import com.juanmediahubph.ooptutorial.data.seed.SeedContent
import com.juanmediahubph.ooptutorial.domain.QuizLogic
import com.juanmediahubph.ooptutorial.domain.QuestionCandidate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

data class AttemptRequest(
    val mode: String,
    val moduleIds: Set<String>,
    val lessonId: String? = null,
    val difficulty: String? = null,
    val questionCount: Int,
    val durationMinutes: Int? = null,
    val passThreshold: Int
)

data class OptionSnapshot(val id: String, val text: String)

class OopRepository(private val db: OopDatabase) {
    val modules = db.contentDao().modules()
    val lessons = db.contentDao().lessons()
    val progress = db.learningDao().progress()
    val bookmarks = db.learningDao().bookmarks()
    val attempts = db.attemptDao().history()
    val activeAttempt = db.attemptDao().activeAttempt()
    val settings = db.settingsDao().all()

    suspend fun loadContent() = SeedContent.import(db)

    suspend fun setCompleted(lessonId: String, complete: Boolean) = withContext(Dispatchers.IO) {
        if (complete) db.learningDao().setProgress(LessonProgressEntity(lessonId, System.currentTimeMillis()))
        else db.learningDao().clearProgress(lessonId)
    }

    suspend fun toggleBookmark(lessonId: String) = withContext(Dispatchers.IO) {
        val dao = db.learningDao()
        if (dao.bookmarkFor(lessonId) == null) dao.bookmark(BookmarkEntity(lessonId, System.currentTimeMillis()))
        else dao.removeBookmark(lessonId)
    }

    suspend fun updateSetting(key: String, value: String) = withContext(Dispatchers.IO) {
        db.settingsDao().put(AppSettingEntity(key, value))
    }

    suspend fun setting(key: String, fallback: String) = db.settingsDao().get(key)?.value ?: fallback

    suspend fun newAttempt(request: AttemptRequest): String = withContext(Dispatchers.IO) {
        db.withTransaction {
            check(db.attemptDao().activeAttemptOnce() == null) { "An attempt is already in progress" }
            val all = db.contentDao().questionsIn(request.moduleIds.toList()).filter {
                (request.lessonId == null || it.lessonId == request.lessonId) &&
                    (request.difficulty == null || it.difficulty.equals(request.difficulty, true))
            }
            val candidates = all.map { QuestionCandidate(it.id, it.moduleId, it.difficulty) }
            val chosen = QuizLogic.choose(candidates, request.moduleIds, request.difficulty, request.questionCount, balanced = true)
            val questions = chosen.mapNotNull { item -> all.firstOrNull { it.id == item.id } }
            val optionRows = db.contentDao().optionsFor(questions.map { it.id }).groupBy { it.questionId }
            val modules = db.contentDao().modulesOnce().associateBy { it.id }
            val now = System.currentTimeMillis()
            val attemptId = UUID.randomUUID().toString()
            val deadline = request.durationMinutes?.let { now + it * 60_000L }
            db.attemptDao().insertAttempt(
                AttemptEntity(
                    id = attemptId, mode = request.mode, moduleIds = request.moduleIds.sorted().joinToString(","),
                    lessonId = request.lessonId, difficulty = request.difficulty ?: "ALL", questionCount = questions.size,
                    durationMinutes = request.durationMinutes, startedAt = now, deadlineAt = deadline,
                    completedAt = null, elapsedMillis = null, status = "IN_PROGRESS",
                    passThreshold = request.passThreshold, correctCount = null, incorrectCount = null,
                    unansweredCount = null, percentage = null, passed = null
                )
            )
            db.attemptDao().insertAttemptQuestions(questions.mapIndexed { position, question ->
                val options = optionRows[question.id].orEmpty().shuffled()
                val correct = optionRows[question.id].orEmpty().single { it.isCorrect }.id
                AttemptQuestionEntity(
                    attemptId, position, question.id, question.moduleId, question.lessonId,
                    modules[question.moduleId]?.title ?: question.moduleId, question.prompt,
                    question.code, encodeOptions(options.map { OptionSnapshot(it.id, it.text) }), correct,
                    question.explanation, null, false
                )
            })
            attemptId
        }
    }

    suspend fun saveAnswer(attemptId: String, position: Int, optionId: String?) = withContext(Dispatchers.IO) {
        db.attemptDao().answer(attemptId, position, optionId)
    }

    suspend fun toggleFlag(attemptId: String, position: Int, flagged: Boolean) = withContext(Dispatchers.IO) {
        db.attemptDao().flag(attemptId, position, flagged)
    }

    suspend fun getAttempt(id: String): AttemptEntity? = db.attemptDao().attempt(id)
    suspend fun getAttemptQuestions(id: String): List<AttemptQuestionEntity> = db.attemptDao().attemptQuestions(id)
    suspend fun openQuestions(moduleIds: List<String> = emptyList()): List<com.juanmediahubph.ooptutorial.data.local.QuestionEntity> =
        if (moduleIds.isEmpty()) db.contentDao().questions() else db.contentDao().questionsIn(moduleIds)

    suspend fun submit(attemptId: String, timedOut: Boolean = false): AttemptEntity? = withContext(Dispatchers.IO) {
        db.withTransaction {
            val dao = db.attemptDao()
            val current = dao.attempt(attemptId) ?: return@withTransaction null
            if (current.status != "IN_PROGRESS") return@withTransaction current
            val rows = dao.attemptQuestions(attemptId)
            val grade = QuizLogic.grade(rows.map { it.correctOptionId }, rows.map { it.selectedOptionId }, current.passThreshold)
            val finished = System.currentTimeMillis()
            val cap = current.deadlineAt?.minus(current.startedAt)
            val elapsed = if (timedOut && cap != null) cap else (finished - current.startedAt).coerceAtLeast(0).let { if (cap == null) it else minOf(it, cap) }
            val updated = current.copy(
                status = if (timedOut) "TIMED_OUT" else "SUBMITTED", completedAt = finished, elapsedMillis = elapsed,
                correctCount = grade.correct, incorrectCount = grade.incorrect, unansweredCount = grade.unanswered,
                percentage = grade.percentage, passed = grade.passed
            )
            dao.insertAttempt(updated)
            updated
        }
    }

    suspend fun abandon(attemptId: String) = withContext(Dispatchers.IO) {
        db.attemptDao().abandon(attemptId, System.currentTimeMillis())
    }

    suspend fun recoverExpiredAttempt() = withContext(Dispatchers.IO) {
        val active = db.attemptDao().activeAttemptOnce() ?: return@withContext
        if (active.deadlineAt != null && System.currentTimeMillis() >= active.deadlineAt) submit(active.id, true)
    }

    suspend fun deleteResult(id: String) = withContext(Dispatchers.IO) { db.attemptDao().deleteAttempt(id) }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        db.withTransaction { db.attemptDao().clearHistory() }
    }

    suspend fun resetProgress() = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.learningDao().clearProgress()
            db.settingsDao().put(AppSettingEntity("last_lesson", ""))
        }
    }

    suspend fun resetAll() = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.attemptDao().clearAllAttempts()
            db.learningDao().clearProgress()
            db.learningDao().clearBookmarks()
            db.settingsDao().clear()
            db.settingsDao().put(AppSettingEntity("seed_version", SeedContent.VERSION))
            db.settingsDao().put(AppSettingEntity("pass_threshold", "70"))
            db.settingsDao().put(AppSettingEntity("theme", "SYSTEM"))
            db.settingsDao().put(AppSettingEntity("text_scale", "1.0"))
        }
    }

    suspend fun saveLastLesson(id: String) = updateSetting("last_lesson", id)

    companion object {
        fun encodeOptions(options: List<OptionSnapshot>): String {
            val array = JSONArray()
            options.forEach { array.put(JSONObject().put("id", it.id).put("text", it.text)) }
            return array.toString()
        }

        fun decodeOptions(value: String): List<OptionSnapshot> {
            val array = JSONArray(value)
            return (0 until array.length()).map { index ->
                val item = array.getJSONObject(index)
                OptionSnapshot(item.getString("id"), item.getString("text"))
            }
        }
    }
}
