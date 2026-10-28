package com.juanmediahubph.ooptutorial.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val position: Int
)

@Entity(
    tableName = "lessons",
    foreignKeys = [ForeignKey(entity = ModuleEntity::class, parentColumns = ["id"], childColumns = ["moduleId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("moduleId")]
)
data class LessonEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val title: String,
    val difficulty: String,
    val objectives: String,
    val readingMinutes: Int,
    val explanation: String,
    val example: String,
    val code: String,
    val codeExplanation: String,
    val commonMistakes: String,
    val takeaway: String
)

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(entity = ModuleEntity::class, parentColumns = ["id"], childColumns = ["moduleId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = LessonEntity::class, parentColumns = ["id"], childColumns = ["lessonId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("moduleId"), Index("lessonId")]
)
data class QuestionEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val lessonId: String,
    val difficulty: String,
    val prompt: String,
    val code: String?,
    val explanation: String,
    val misconceptionTag: String?
)

@Entity(
    tableName = "question_options",
    primaryKeys = ["id"],
    foreignKeys = [ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["questionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId")]
)
data class QuestionOptionEntity(
    val id: String,
    val questionId: String,
    val text: String,
    val isCorrect: Boolean,
    val position: Int
)

@Entity(
    tableName = "lesson_progress",
    foreignKeys = [ForeignKey(entity = LessonEntity::class, parentColumns = ["id"], childColumns = ["lessonId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("lessonId")]
)
data class LessonProgressEntity(@PrimaryKey val lessonId: String, val completedAt: Long?)

@Entity(
    tableName = "bookmarks",
    foreignKeys = [ForeignKey(entity = LessonEntity::class, parentColumns = ["id"], childColumns = ["lessonId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("lessonId")]
)
data class BookmarkEntity(@PrimaryKey val lessonId: String, val createdAt: Long)

@Entity(tableName = "attempts", indices = [Index("status"), Index("completedAt")])
data class AttemptEntity(
    @PrimaryKey val id: String,
    val mode: String,
    val moduleIds: String,
    val lessonId: String?,
    val difficulty: String,
    val questionCount: Int,
    val durationMinutes: Int?,
    val startedAt: Long,
    val deadlineAt: Long?,
    val completedAt: Long?,
    val elapsedMillis: Long?,
    val status: String,
    val passThreshold: Int,
    val correctCount: Int?,
    val incorrectCount: Int?,
    val unansweredCount: Int?,
    val percentage: Double?,
    val passed: Boolean?
)

@Entity(
    tableName = "attempt_questions",
    primaryKeys = ["attemptId", "position"],
    foreignKeys = [ForeignKey(entity = AttemptEntity::class, parentColumns = ["id"], childColumns = ["attemptId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("questionId"), Index("moduleId")]
)
data class AttemptQuestionEntity(
    val attemptId: String,
    val position: Int,
    val questionId: String,
    val moduleId: String,
    val lessonId: String,
    val moduleNameSnapshot: String,
    val questionTextSnapshot: String,
    val codeSnapshot: String?,
    val optionsSnapshot: String,
    val correctOptionId: String,
    val explanationSnapshot: String,
    val selectedOptionId: String?,
    val flagged: Boolean
)

@Entity(tableName = "app_settings")
data class AppSettingEntity(@PrimaryKey val key: String, val value: String)
