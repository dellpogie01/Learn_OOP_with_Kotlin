package com.juanmediahubph.ooptutorial.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.juanmediahubph.ooptutorial.OopApplication
import com.juanmediahubph.ooptutorial.data.local.AttemptEntity
import com.juanmediahubph.ooptutorial.data.local.AttemptQuestionEntity
import com.juanmediahubph.ooptutorial.data.local.BookmarkEntity
import com.juanmediahubph.ooptutorial.data.local.LessonEntity
import com.juanmediahubph.ooptutorial.data.local.LessonProgressEntity
import com.juanmediahubph.ooptutorial.data.local.ModuleEntity
import com.juanmediahubph.ooptutorial.data.repository.AttemptRequest
import com.juanmediahubph.ooptutorial.data.repository.OopRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class AppUiState(
    val modules: List<ModuleEntity> = emptyList(),
    val lessons: List<LessonEntity> = emptyList(),
    val progress: List<LessonProgressEntity> = emptyList(),
    val bookmarks: List<BookmarkEntity> = emptyList(),
    val attempts: List<AttemptEntity> = emptyList(),
    val settings: Map<String, String> = emptyMap(),
    val activeAttempt: AttemptEntity? = null
)

data class AttemptUiState(val attempt: AttemptEntity? = null, val questions: List<AttemptQuestionEntity> = emptyList())
private data class LearningStreams(
    val modules: List<ModuleEntity>,
    val lessons: List<LessonEntity>,
    val progress: List<LessonProgressEntity>,
    val bookmarks: List<BookmarkEntity>
)
private data class AttemptStreams(
    val attempts: List<AttemptEntity>,
    val settings: List<com.juanmediahubph.ooptutorial.data.local.AppSettingEntity>,
    val active: AttemptEntity?
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    val repository: OopRepository = (application as OopApplication).repository
    val error = MutableStateFlow<String?>(null)
    private val attemptWrites = Mutex()
    val state: StateFlow<AppUiState> = combine(
        combine(repository.modules, repository.lessons, repository.progress, repository.bookmarks) { modules, lessons, progress, bookmarks ->
            LearningStreams(modules, lessons, progress, bookmarks)
        },
        combine(repository.attempts, repository.settings, repository.activeAttempt) { attempts, settings, active ->
            AttemptStreams(attempts, settings, active)
        }
    ) { learning, attempts ->
        AppUiState(
            modules = learning.modules,
            lessons = learning.lessons,
            progress = learning.progress,
            bookmarks = learning.bookmarks,
            attempts = attempts.attempts,
            settings = attempts.settings.associate { it.key to it.value },
            activeAttempt = attempts.active
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppUiState())

    init {
        viewModelScope.launch {
            runCatching { repository.loadContent(); repository.recoverExpiredAttempt() }
                .onFailure { error.value = it.message ?: "Unable to load local learning content." }
        }
    }

    fun setCompleted(id: String, complete: Boolean) = launch { repository.setCompleted(id, complete) }
    fun toggleBookmark(id: String) = launch { repository.toggleBookmark(id) }
    fun saveLastLesson(id: String) = launch { repository.saveLastLesson(id) }
    fun updateSetting(key: String, value: String) = launch { repository.updateSetting(key, value) }
    fun startAttempt(request: AttemptRequest, onCreated: (String) -> Unit) {
        launch { runCatching { attemptWrites.withLock { repository.newAttempt(request) } }.onSuccess(onCreated).onFailure { error.value = it.message } }
    }
    fun answer(id: String, position: Int, optionId: String?) = launch { attemptWrites.withLock { repository.saveAnswer(id, position, optionId) } }
    fun flag(id: String, position: Int, flagged: Boolean) = launch { attemptWrites.withLock { repository.toggleFlag(id, position, flagged) } }
    fun submit(id: String, timeout: Boolean = false, onDone: () -> Unit = {}) {
        launch { attemptWrites.withLock { repository.submit(id, timeout) }; onDone() }
    }
    fun abandon(id: String, onDone: () -> Unit = {}) = launch { attemptWrites.withLock { repository.abandon(id) }; onDone() }
    fun loadAttempt(id: String, callback: (AttemptUiState) -> Unit) {
        launch { callback(AttemptUiState(repository.getAttempt(id), repository.getAttemptQuestions(id))) }
    }
    fun deleteResult(id: String) = launch { repository.deleteResult(id) }
    fun clearHistory() = launch { repository.clearHistory() }
    fun resetProgress() = launch { repository.resetProgress() }
    fun resetAll() = launch { repository.resetAll() }

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch { runCatching { block() }.onFailure { error.value = it.message ?: "Something went wrong." } }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AppViewModel::class.java)) return AppViewModel(application) as T
            error("Unknown ViewModel class")
        }
    }
}
