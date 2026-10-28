package com.juanmediahubph.ooptutorial.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("SELECT * FROM modules ORDER BY position") fun modules(): Flow<List<ModuleEntity>>
    @Query("SELECT * FROM modules ORDER BY position") suspend fun modulesOnce(): List<ModuleEntity>
    @Query("SELECT * FROM lessons ORDER BY moduleId, id") fun lessons(): Flow<List<LessonEntity>>
    @Query("SELECT * FROM lessons WHERE id = :id") suspend fun lesson(id: String): LessonEntity?
    @Query("SELECT * FROM lessons WHERE moduleId = :moduleId ORDER BY id") suspend fun lessonsForModule(moduleId: String): List<LessonEntity>
    @Query("SELECT * FROM questions ORDER BY id") suspend fun questions(): List<QuestionEntity>
    @Query("SELECT * FROM questions WHERE moduleId IN (:moduleIds)") suspend fun questionsIn(moduleIds: List<String>): List<QuestionEntity>
    @Query("SELECT * FROM question_options WHERE questionId IN (:questionIds) ORDER BY position") suspend fun optionsFor(questionIds: List<String>): List<QuestionOptionEntity>
    @Upsert suspend fun putModules(rows: List<ModuleEntity>)
    @Upsert suspend fun putLessons(rows: List<LessonEntity>)
    @Upsert suspend fun putQuestions(rows: List<QuestionEntity>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun putOptions(rows: List<QuestionOptionEntity>)
    @Query("DELETE FROM question_options WHERE questionId IN (:ids)") suspend fun deleteOptions(ids: List<String>)
    @Query("SELECT * FROM modules WHERE id = :id") suspend fun module(id: String): ModuleEntity?
}

@Dao
interface LearningDao {
    @Query("SELECT * FROM lesson_progress") fun progress(): Flow<List<LessonProgressEntity>>
    @Query("SELECT * FROM bookmarks") fun bookmarks(): Flow<List<BookmarkEntity>>
    @Query("SELECT * FROM lesson_progress WHERE lessonId = :id") suspend fun progressFor(id: String): LessonProgressEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun setProgress(row: LessonProgressEntity)
    @Query("DELETE FROM lesson_progress WHERE lessonId = :id") suspend fun clearProgress(id: String)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun bookmark(row: BookmarkEntity)
    @Query("DELETE FROM bookmarks WHERE lessonId = :id") suspend fun removeBookmark(id: String)
    @Query("SELECT * FROM bookmarks WHERE lessonId = :id") suspend fun bookmarkFor(id: String): BookmarkEntity?
    @Query("DELETE FROM lesson_progress") suspend fun clearProgress()
    @Query("DELETE FROM bookmarks") suspend fun clearBookmarks()
}

@Dao
interface AttemptDao {
    @Query("SELECT * FROM attempts WHERE status = 'IN_PROGRESS' ORDER BY startedAt DESC LIMIT 1") fun activeAttempt(): Flow<AttemptEntity?>
    @Query("SELECT * FROM attempts WHERE status = 'IN_PROGRESS' ORDER BY startedAt DESC LIMIT 1") suspend fun activeAttemptOnce(): AttemptEntity?
    @Query("SELECT * FROM attempts WHERE id = :id") suspend fun attempt(id: String): AttemptEntity?
    @Query("SELECT * FROM attempts WHERE status NOT IN ('IN_PROGRESS', 'ABANDONED') ORDER BY completedAt DESC") fun history(): Flow<List<AttemptEntity>>
    @Query("SELECT * FROM attempt_questions WHERE attemptId = :id ORDER BY position") suspend fun attemptQuestions(id: String): List<AttemptQuestionEntity>
    @Upsert suspend fun insertAttempt(row: AttemptEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertAttemptQuestions(rows: List<AttemptQuestionEntity>)
    @Query("UPDATE attempt_questions SET selectedOptionId = :optionId WHERE attemptId = :attemptId AND position = :position") suspend fun answer(attemptId: String, position: Int, optionId: String?)
    @Query("UPDATE attempt_questions SET flagged = :flagged WHERE attemptId = :attemptId AND position = :position") suspend fun flag(attemptId: String, position: Int, flagged: Boolean)
    @Query("DELETE FROM attempts WHERE id = :id") suspend fun deleteAttempt(id: String)
    @Query("DELETE FROM attempts WHERE status != 'IN_PROGRESS'") suspend fun clearHistory()
    @Query("DELETE FROM attempts") suspend fun clearAllAttempts()
    @Transaction suspend fun abandon(id: String, completedAt: Long) {
        val current = attempt(id) ?: return
        if (current.status == "IN_PROGRESS") insertAttempt(current.copy(status = "ABANDONED", completedAt = completedAt))
    }
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings WHERE key = :key") suspend fun get(key: String): AppSettingEntity?
    @Query("SELECT * FROM app_settings") fun all(): Flow<List<AppSettingEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun put(row: AppSettingEntity)
    @Query("DELETE FROM app_settings") suspend fun clear()
}

@Database(
    entities = [
        ModuleEntity::class, LessonEntity::class, QuestionEntity::class, QuestionOptionEntity::class,
        LessonProgressEntity::class, BookmarkEntity::class, AttemptEntity::class,
        AttemptQuestionEntity::class, AppSettingEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class OopDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
    abstract fun learningDao(): LearningDao
    abstract fun attemptDao(): AttemptDao
    abstract fun settingsDao(): SettingsDao
}
