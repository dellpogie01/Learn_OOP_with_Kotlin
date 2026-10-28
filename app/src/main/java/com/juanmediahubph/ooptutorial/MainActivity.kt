@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package com.juanmediahubph.ooptutorial

import android.os.Bundle
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.foundation.layout.Box
import com.juanmediahubph.ooptutorial.ui.components.FittingSingleLineText
import com.juanmediahubph.ooptutorial.ui.components.StartupSplash
import androidx.compose.foundation.background
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Snackbar
import androidx.compose.material.icons.rounded.Search
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.key
import com.juanmediahubph.ooptutorial.ui.components.AppCard
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juanmediahubph.ooptutorial.data.local.AttemptEntity
import com.juanmediahubph.ooptutorial.data.local.AttemptQuestionEntity
import com.juanmediahubph.ooptutorial.data.local.LessonEntity
import com.juanmediahubph.ooptutorial.data.local.ModuleEntity
import com.juanmediahubph.ooptutorial.data.repository.AttemptRequest
import com.juanmediahubph.ooptutorial.data.repository.OopRepository
import com.juanmediahubph.ooptutorial.ui.AppUiState
import com.juanmediahubph.ooptutorial.ui.AppViewModel
import com.juanmediahubph.ooptutorial.ui.AttemptUiState
import com.juanmediahubph.ooptutorial.ui.theme.AppTheme
import com.juanmediahubph.ooptutorial.ui.theme.codeStyle
import com.juanmediahubph.ooptutorial.ui.theme.scaledReading
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.util.Date
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: AppViewModel = viewModel(factory = AppViewModel.Factory(application))
            val state by vm.state.collectAsState()
            val useDark = when (state.settings["theme"]) {
                "LIGHT" -> false
                "DARK" -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            AppTheme(useDark) {
                StartupSplash { OopApp(vm, state) }
            }
        }
    }
}

private data class Tab(val route: String, val labelId: Int, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
private fun OopApp(vm: AppViewModel, state: AppUiState) {
    val nav = rememberNavController()
    val current by nav.currentBackStackEntryAsState()
    val route = current?.destination?.route ?: "home"
    val tabs = remember { listOf(
        Tab("home", R.string.home, Icons.Rounded.Home),
        Tab("modules", R.string.lessons, Icons.AutoMirrored.Rounded.MenuBook),
        Tab("bookmarks", R.string.bookmarks, Icons.Rounded.Bookmark),
        Tab("practice", R.string.practice, Icons.Rounded.Edit),
        Tab("exams", R.string.exams, Icons.Rounded.Quiz)
    ) }
    var pendingRequest by remember { mutableStateOf<AttemptRequest?>(null) }
    var showActiveDialog by rememberSaveable { mutableStateOf(false) }
    val begin: (AttemptRequest) -> Unit = { request ->
        if (state.activeAttempt != null) {
            pendingRequest = request
            showActiveDialog = true
        } else vm.startAttempt(request) { nav.navigate("attempt/$it") }
    }
    if (showActiveDialog) {
        AlertDialog(
            onDismissRequest = { showActiveDialog = false },
            title = { Text(stringResource(R.string.active_attempt_title)) },
            text = { Text(stringResource(R.string.active_attempt_body), modifier = Modifier.verticalScroll(rememberScrollState())) },
            confirmButton = {
                TextButton(onClick = {
                    showActiveDialog = false
                    state.activeAttempt?.let { nav.navigate("attempt/${it.id}") }
                }) { Text(stringResource(R.string.resume_attempt)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showActiveDialog = false
                    val request = pendingRequest
                    val id = state.activeAttempt?.id
                    if (request != null && id != null) vm.abandon(id) { vm.startAttempt(request) { nav.navigate("attempt/$it") } }
                    pendingRequest = null
                }) { Text(stringResource(R.string.abandon_attempt)) }
            }
        )
    }
    val error by vm.error.collectAsState()
    val titleId = when (route.substringBefore('/')) {
        "home" -> R.string.home_title
        "modules", "lesson" -> R.string.lessons
        "bookmarks" -> R.string.bookmarks
        "practice" -> R.string.practice
        "exams" -> R.string.exams
        "history" -> R.string.history
        "settings" -> R.string.settings
        "about" -> R.string.about
        "result" -> R.string.result_review
        else -> R.string.app_name
    }
    // Keep the content viewport fixed while scrolling, including the floating Home bar.
    val toolbarScrollBehavior = key(route) { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        modifier = Modifier.nestedScroll(toolbarScrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AppTopBar(
                title = stringResource(titleId),
                showBack = route !in tabs.map { it.route },
                onBack = { nav.popBackStack() },
                scrollBehavior = toolbarScrollBehavior,
                floating = route == "home",
                actions = {
                    if (route == "home") {
                        IconButton(onClick = { nav.navigate("settings") { launchSingleTop = true } }) {
                            Icon(Icons.Rounded.Settings, contentDescription = stringResource(R.string.settings))
                        }
                    }
                    if (route.startsWith("lesson/")) {
                        val id = current?.arguments?.getString("id").orEmpty()
                        if (state.lessons.any { it.id == id }) {
                            val bookmarked = state.bookmarks.any { it.lessonId == id }
                            IconButton(onClick = { vm.toggleBookmark(id) }) {
                                Icon(Icons.Rounded.Bookmark,
                                    contentDescription = stringResource(if (bookmarked) R.string.remove_bookmark else R.string.bookmark),
                                    tint = if (bookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (route in tabs.map { it.route }) {
                NavigationBar {
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = { nav.navigate(tab.route) { launchSingleTop = true } },
                            icon = { Icon(tab.icon, contentDescription = stringResource(tab.labelId)) },
                            label = { Text(stringResource(tab.labelId), maxLines = 1, overflow = TextOverflow.Ellipsis) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding).imePadding()) {
            NavHost(
                navController = nav, startDestination = "home", modifier = Modifier.weight(1f),
                enterTransition = { fadeIn(tween(200)) + slideInHorizontally(tween(200)) { it / 24 } },
                exitTransition = { fadeOut(tween(150)) },
                popEnterTransition = { fadeIn(tween(200)) },
                popExitTransition = { fadeOut(tween(150)) + slideOutHorizontally(tween(150)) { it / 24 } }
            ) {
                composable("home") { HomeScreen(state, vm, onNavigate = { nav.navigate(it) }, onBegin = begin) }
                composable("modules") { ModuleListScreen(state) { nav.navigate("lesson/$it") } }
                composable("bookmarks") { BookmarkScreen(state, vm) { nav.navigate("lesson/$it") } }
                composable("practice") { PracticeSetupScreen(state, begin) }
                composable("exams") { ExamSetupScreen(state, begin) }
                composable("history") { HistoryScreen(state, vm) { nav.navigate("result/$it") } }
                composable("settings") { SettingsScreen(state, vm) { nav.navigate("about") } }
                composable("about") { AboutScreen() }
                composable("lesson/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) {
                    LessonScreen(it.arguments?.getString("id").orEmpty(), state, vm, nav, begin)
                }
                composable("attempt/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) {
                    AttemptScreen(it.arguments?.getString("id").orEmpty(), vm, nav)
                }
                composable("result/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) {
                    ResultScreen(it.arguments?.getString("id").orEmpty(), state, vm, nav, begin)
                }
            }
            AnimatedVisibility(visible = error != null, enter = fadeIn(tween(180)), exit = fadeOut(tween(150))) {
                Snackbar(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().heightIn(max = 180.dp),
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    actionOnNewLine = true,
                    action = { TextButton(onClick = { vm.error.value = null }) { Text(stringResource(R.string.dismiss)) } }
                ) {
                    Text(error.orEmpty(), modifier = Modifier.verticalScroll(rememberScrollState()))
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(state: AppUiState, vm: AppViewModel, onNavigate: (String) -> Unit, onBegin: (AttemptRequest) -> Unit) {
    val completed = state.progress.count { it.completedAt != null }
    val total = state.lessons.size
    val lastId by remember(state.settings) { mutableStateOf(state.settings["last_lesson"].orEmpty()) }
    val lastLesson = state.lessons.firstOrNull { it.id == lastId } ?: state.lessons.firstOrNull { it.id !in state.progress.map { row -> row.lessonId } }
    val latest = state.attempts.firstOrNull()
    ScreenColumn {
        FittingSingleLineText(stringResource(R.string.home_subtitle), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        AppCard(
            onClick = { lastLesson?.let { vm.saveLastLesson(it.id); onNavigate("lesson/${it.id}") } },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Column(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.tertiaryContainer
            ))).padding(24.dp)) {
                Text(stringResource(R.string.continue_learning), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(lastLesson?.title ?: stringResource(R.string.all_lessons_complete))
                Text(lastLesson?.let { state.modules.firstOrNull { m -> m.id == it.moduleId }?.title } ?: "$completed / $total lessons complete",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                LinearProgressIndicator(progress = { if (total == 0) 0f else completed.toFloat() / total }, Modifier.fillMaxWidth())
                Text(stringResource(R.string.completed_count, stringResource(R.string.completed_lessons), completed, total), modifier = Modifier.padding(top = 8.dp))
            }
        }
        Text(stringResource(R.string.overall_progress), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp))
        val progress = if (total == 0) 0f else completed.toFloat() / total
        Text(stringResource(R.string.percent_value, (progress * 100).roundToInt()))
        val quick = listOf(
            R.string.lessons to "modules", R.string.practice to "practice", R.string.exams to "exams",
            R.string.history to "history", R.string.bookmarks to "bookmarks"
        )
        quick.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { (label, destination) ->
                    OutlinedButton(onClick = { onNavigate(destination) }, modifier = Modifier.weight(1f)) { Text(stringResource(label)) }
                }
            }
        }
        state.activeAttempt?.let { active ->
            AppCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text(stringResource(R.string.attempt_in_progress), fontWeight = FontWeight.SemiBold)
                        Text(pluralStringResource(R.plurals.attempt_summary, active.questionCount, stringResource(if (active.mode == "EXAM") R.string.exam_mode else R.string.practice_mode), active.questionCount))
                    }
                    Button(onClick = { onNavigate("attempt/${active.id}") }) { Text(stringResource(R.string.resume_attempt)) }
                }
            }
        }
        Text(stringResource(R.string.recent_result), style = MaterialTheme.typography.titleMedium)
        if (latest == null) Text(stringResource(R.string.first_result_empty), color = MaterialTheme.colorScheme.onSurfaceVariant)
        else AppCard(onClick = { onNavigate("result/${latest.id}") }, modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Star, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.recent_result_score, stringResource(if (latest.mode == "EXAM") R.string.exam_mode else R.string.practice_mode), latest.correctCount ?: 0, latest.questionCount))
                    Text(stringResource(R.string.recent_result_date, latest.percentage?.roundToInt() ?: 0, formatDate(latest.completedAt)))
                }
            }
        }
        if (state.lessons.isEmpty()) Text(stringResource(R.string.lesson_loading))
    }
}

@Composable
private fun ModuleListScreen(state: AppUiState, onLesson: (String) -> Unit) {
    var search by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("All") }
    ScreenColumn {
        OutlinedTextField(search, { search = it }, modifier = Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.search_modules_lessons)) }, leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) }, singleLine = true)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(R.string.all to "All", R.string.complete to "Complete", R.string.incomplete to "Incomplete").forEach { (labelId, option) ->
                FilterChip(filter == option, { filter = option }, modifier = Modifier.heightIn(min = 48.dp), label = { Text(stringResource(labelId)) })
            }
        }
        val completeIds = state.progress.map { it.lessonId }.toSet()
        val visible = state.modules.filter { module ->
            val moduleLessons = state.lessons.filter { it.moduleId == module.id }
            val matches = search.isBlank() || module.title.contains(search, true) ||
                moduleLessons.any { it.title.contains(search, true) }
            val statusMatches = filter == "All" ||
                (filter == "Complete" && moduleLessons.isNotEmpty() && moduleLessons.all { it.id in completeIds }) ||
                (filter == "Incomplete" && moduleLessons.any { it.id !in completeIds })
            matches && statusMatches
        }
        visible.forEach { module ->
            val moduleLessons = state.lessons.filter { it.moduleId == module.id }
            AppCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("${module.position}. ${module.title}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(module.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(
                        R.string.module_difficulty,
                        module.difficulty,
                        pluralStringResource(
                            R.plurals.module_completion,
                            moduleLessons.count { it.id in completeIds },
                            moduleLessons.count { it.id in completeIds },
                            moduleLessons.size
                        )
                    ))
                    moduleLessons.filter { search.isBlank() || it.title.contains(search, true) || module.title.contains(search, true) }.forEach { lesson ->
                        TextButton(onClick = { onLesson(lesson.id) }, modifier = Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Icon(if (lesson.id in completeIds) Icons.Rounded.CheckCircle else Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(lesson.title, modifier = Modifier.weight(1f), maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = stringResource(R.string.open_lesson))
                            }
                        }
                    }
                }
            }
        }
        if (visible.isEmpty()) EmptyCard(stringResource(R.string.no_search_results))
    }
}

@Composable
private fun LessonScreen(id: String, state: AppUiState, vm: AppViewModel, nav: androidx.navigation.NavHostController, onBegin: (AttemptRequest) -> Unit) {
    val lesson = state.lessons.firstOrNull { it.id == id }
    if (lesson == null) {
        ScreenColumn { LoadingState() }
        return
    }
    LaunchedEffect(id) { vm.saveLastLesson(id) }
    val index = state.lessons.indexOfFirst { it.id == id }
    val module = state.modules.firstOrNull { it.id == lesson.moduleId }
    val completed = state.progress.any { it.lessonId == id }
    val textScale = (state.settings["text_scale"]?.toFloatOrNull() ?: 1f).coerceIn(0.85f, 1.3f)
    val clipboard = LocalClipboardManager.current
    val appContext = LocalContext.current
    ScreenColumn {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(module?.title ?: "Lesson", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(lesson.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Text(stringResource(R.string.lesson_read_time, lesson.difficulty, lesson.readingMinutes))
        LessonSection(stringResource(R.string.objectives), lesson.objectives, textScale)
        LessonSection(stringResource(R.string.explanation), lesson.explanation, textScale)
        LessonSection(stringResource(R.string.example), lesson.example, textScale)
        Text(stringResource(R.string.code_example), style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        AppCard {
            Column(Modifier.padding(12.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        clipboard.setText(AnnotatedString(lesson.code))
                        android.widget.Toast.makeText(appContext, appContext.getString(R.string.code_copied), android.widget.Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Rounded.ContentCopy, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.copy_code))
                    }
                }
                Text(lesson.code, style = MaterialTheme.typography.bodySmall.codeStyle(textScale),
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(8.dp))
            }
        }
        LessonSection(stringResource(R.string.explanation), lesson.codeExplanation, textScale)
        LessonSection(stringResource(R.string.common_mistakes), lesson.commonMistakes, textScale)
        LessonSection(stringResource(R.string.takeaway), lesson.takeaway, textScale)
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { if (index > 0) nav.navigate("lesson/${state.lessons[index - 1].id}") }, enabled = index > 0, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.previous))
            }
            Button(onClick = { vm.setCompleted(id, !completed) }, modifier = Modifier.fillMaxWidth()) {
                Icon(if (completed) Icons.Rounded.CheckCircle else Icons.Rounded.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(if (completed) R.string.mark_incomplete else R.string.mark_complete))
            }
            OutlinedButton(onClick = { if (index < state.lessons.lastIndex) nav.navigate("lesson/${state.lessons[index + 1].id}") }, enabled = index < state.lessons.lastIndex, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.next)); Spacer(Modifier.width(8.dp)); Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
            }
        }
        Button(onClick = {
            onBegin(AttemptRequest("PRACTICE", setOf(lesson.moduleId), lessonId = lesson.id, questionCount = 5, passThreshold = state.settings["pass_threshold"]?.toIntOrNull() ?: 70))
        }, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Rounded.Quiz, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.start_quiz)) }
    }
}

@Composable
private fun LessonSection(title: String, body: String, scale: Float) {
    Column(Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(body, style = MaterialTheme.typography.bodyLarge.scaledReading(scale))
    }
}

@Composable
private fun BookmarkScreen(state: AppUiState, vm: AppViewModel, onLesson: (String) -> Unit) {
    ScreenColumn {
        val saved = state.bookmarks.mapNotNull { mark -> state.lessons.firstOrNull { it.id == mark.lessonId } }
        if (saved.isEmpty()) EmptyCard(stringResource(R.string.empty_bookmarks))
        saved.forEach { lesson ->
            val module = state.modules.firstOrNull { it.id == lesson.moduleId }
            AppCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        TextButton(onClick = { onLesson(lesson.id) }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.fillMaxWidth()) {
                                Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                                Text(module?.title ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    IconButton(onClick = { vm.toggleBookmark(lesson.id) }) {
                        Icon(Icons.Rounded.Delete, contentDescription = stringResource(R.string.remove_bookmark))
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeSetupScreen(state: AppUiState, onBegin: (AttemptRequest) -> Unit) {
    var selected by rememberSaveable { mutableStateOf(emptySet<String>()) }
    var initialized by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(state.modules) {
        if (!initialized && state.modules.isNotEmpty()) {
            selected = state.modules.map { it.id }.toSet()
            initialized = true
        }
    }
    var difficulty by rememberSaveable { mutableStateOf("ALL") }
    var count by rememberSaveable { mutableIntStateOf(5) }
    val filtered = remember(state.modules, selected, difficulty) {
        com.juanmediahubph.ooptutorial.data.seed.SeedContent.questions.count {
            it.moduleId in selected && (difficulty == "ALL" || it.difficulty.equals(difficulty, true))
        }
    }
    ScreenColumn {
        Text(stringResource(R.string.practice_intro))
        Text(stringResource(R.string.choose_modules), style = MaterialTheme.typography.titleMedium)
        state.modules.forEach { module ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = module.id in selected, onCheckedChange = { checked ->
                    selected = if (checked) selected + module.id else selected - module.id
                })
                Text(module.title)
            }
        }
        Text(stringResource(R.string.difficulty), style = MaterialTheme.typography.titleMedium)
        ChoiceChips(listOf("ALL", "Beginner", "Intermediate", "Advanced"), difficulty) { difficulty = it }
        Text(stringResource(R.string.available_questions, filtered))
        Text(stringResource(R.string.question_count), style = MaterialTheme.typography.titleMedium)
        ChoiceChips(listOf(5, 10, 20).filter { it <= filtered }.ifEmpty { listOf(filtered).filter { it > 0 } }.map(Int::toString), count.toString()) { count = it.toInt() }
        Button(onClick = {
            onBegin(AttemptRequest(
                mode = "PRACTICE",
                moduleIds = selected,
                difficulty = if (difficulty == "ALL") null else difficulty,
                questionCount = count,
                passThreshold = state.settings["pass_threshold"]?.toIntOrNull() ?: 70
            ))
        }, enabled = selected.isNotEmpty() && filtered >= count, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_practice)) }
    }
}

@Composable
private fun ExamSetupScreen(state: AppUiState, onBegin: (AttemptRequest) -> Unit) {
    var mode by rememberSaveable { mutableStateOf("MODULE") }
    var oneModule by rememberSaveable { mutableStateOf("") }
    var selected by rememberSaveable { mutableStateOf(emptySet<String>()) }
    var initialized by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(state.modules) {
        if (!initialized && state.modules.isNotEmpty()) {
            oneModule = state.modules.first().id
            selected = state.modules.map { it.id }.toSet()
            initialized = true
        }
    }
    var count by rememberSaveable { mutableIntStateOf(10) }
    var duration by rememberSaveable { mutableIntStateOf(15) }
    val modules = when (mode) {
        "MODULE" -> setOf(oneModule).filter { it.isNotBlank() }.toSet()
        "FULL" -> state.modules.map { it.id }.toSet()
        else -> selected
    }
    val available = com.juanmediahubph.ooptutorial.data.seed.SeedContent.questions.count { it.moduleId in modules }
    val fixedCount = when (mode) { "MODULE" -> 10; "FULL" -> 30; else -> count }
    val fixedDuration = when (mode) { "MODULE" -> 15; "FULL" -> 45; else -> duration }
    ScreenColumn {
        Text(stringResource(R.string.exam_intro))
        ChoiceChips(listOf("MODULE", "FULL", "CUSTOM"), mode) { mode = it }
        when (mode) {
            "MODULE" -> {
                Text(stringResource(R.string.module_label), style = MaterialTheme.typography.titleMedium)
                state.modules.forEach { module ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = oneModule == module.id, onClick = { oneModule = module.id })
                        Text(module.title)
                    }
                }
            }
            "CUSTOM" -> {
                Text(stringResource(R.string.choose_modules), style = MaterialTheme.typography.titleMedium)
                state.modules.forEach { module ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = module.id in selected, onCheckedChange = { checked ->
                            selected = if (checked) selected + module.id else selected - module.id
                        })
                        Text(module.title)
                    }
                }
                Text(stringResource(R.string.question_count), style = MaterialTheme.typography.titleMedium)
                ChoiceChips(listOf(10, 20, 30).filter { it <= available }.ifEmpty { listOf(available).filter { it > 0 } }.map(Int::toString), count.toString()) { count = it.toInt() }
                Text(stringResource(R.string.time_limit))
                ChoiceChips(listOf(15, 30, 45).map(Int::toString), duration.toString()) { duration = it.toInt() }
            }
            else -> Unit
        }
        Text(stringResource(R.string.available_questions, available))
        if (available < fixedCount) Text(pluralStringResource(R.plurals.insufficient_exam_questions, available, available), color = MaterialTheme.colorScheme.error)
        Text(pluralStringResource(R.plurals.exam_question_time, fixedCount, fixedCount, fixedDuration, stringResource(R.string.minutes)))
        Button(onClick = {
            onBegin(AttemptRequest("EXAM", modules, questionCount = fixedCount, durationMinutes = fixedDuration, passThreshold = state.settings["pass_threshold"]?.toIntOrNull() ?: 70))
        }, enabled = modules.isNotEmpty() && available >= fixedCount, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Rounded.PlayArrow, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.start_exam))
        }
    }
}

@Composable
private fun AttemptScreen(id: String, vm: AppViewModel, nav: androidx.navigation.NavHostController) {
    val context = LocalContext.current
    var data by remember(id) { mutableStateOf(AttemptUiState()) }
    var current by rememberSaveable(id) { mutableIntStateOf(0) }
    var confirmSubmit by rememberSaveable(id) { mutableStateOf(false) }
    var remaining by remember(id) { mutableLongStateOf(0L) }
    LaunchedEffect(id) { vm.loadAttempt(id) { data = it } }
    val attempt = data.attempt
    val rows = data.questions
    LaunchedEffect(attempt?.deadlineAt) {
        val deadline = attempt?.deadlineAt ?: return@LaunchedEffect
        val initialRemaining = (deadline - System.currentTimeMillis()).coerceAtLeast(0)
        val monotonicStart = android.os.SystemClock.elapsedRealtime()
        while (true) {
            remaining = (initialRemaining - (android.os.SystemClock.elapsedRealtime() - monotonicStart)).coerceAtLeast(0)
            if (remaining <= 0L && attempt.status == "IN_PROGRESS") {
                vm.submit(id, true) { nav.navigate("result/$id") { popUpTo("attempt/$id") { inclusive = true } } }
                break
            }
            delay(1000)
        }
    }
    if (attempt == null || rows.isEmpty()) {
        ScreenColumn { LoadingState() }
        return
    }
    if (attempt.status != "IN_PROGRESS") {
        LaunchedEffect(attempt.status) { nav.navigate("result/$id") { popUpTo("attempt/$id") { inclusive = true } } }
        return
    }
    val item = rows.getOrNull(current.coerceIn(0, rows.lastIndex))
    ScreenColumn {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("${stringResource(if (attempt.mode == "EXAM") R.string.exam_mode else R.string.practice_mode)} · ${stringResource(R.string.question_progress, current + 1, rows.size)}", style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.answered_count, rows.count { it.selectedOptionId != null }, rows.size), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (attempt.deadlineAt != null) {
                Icon(Icons.Rounded.Timer, contentDescription = stringResource(R.string.time_remaining))
                Text(formatDuration(remaining))
            }
        }
        LinearProgressIndicator(progress = { (current + 1).toFloat() / rows.size }, modifier = Modifier.fillMaxWidth())
        Text(item?.moduleNameSnapshot.orEmpty(), color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 8.dp))
        Text(item?.questionTextSnapshot.orEmpty(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        item?.codeSnapshot?.let { CodeCard(it) }
        val options = remember(item?.optionsSnapshot) { item?.let { OopRepository.decodeOptions(it.optionsSnapshot) }.orEmpty() }
        options.forEach { option ->
            val selected = item?.selectedOptionId == option.id
            val correct = item?.correctOptionId == option.id
            val showFeedback = attempt.mode == "PRACTICE" && item?.selectedOptionId != null
            val optionColor by animateColorAsState(
                targetValue = when {
                    showFeedback && correct -> MaterialTheme.colorScheme.secondaryContainer
                    showFeedback && selected -> MaterialTheme.colorScheme.errorContainer
                    selected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceContainerLow
                },
                animationSpec = tween(180), label = "answerColor"
            )
            AppCard(
                onClick = {
                    vm.answer(id, current, option.id)
                    data = data.copy(questions = rows.mapIndexed { index, row -> if (index == current) row.copy(selectedOptionId = option.id) else row })
                },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = optionColor,
                    contentColor = when {
                        showFeedback && correct -> MaterialTheme.colorScheme.onSecondaryContainer
                        showFeedback && selected -> MaterialTheme.colorScheme.onErrorContainer
                        selected -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
            ) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected, onClick = {
                        vm.answer(id, current, option.id)
                        data = data.copy(questions = rows.mapIndexed { index, row -> if (index == current) row.copy(selectedOptionId = option.id) else row })
                    })
                    Text(option.text, modifier = Modifier.weight(1f))
                    if (showFeedback && correct) Icon(Icons.Rounded.CheckCircle, contentDescription = stringResource(R.string.correct))
                    if (showFeedback && selected && !correct) Text(stringResource(R.string.incorrect))
                }
            }
        }
        if (attempt.mode == "PRACTICE" && item?.selectedOptionId != null) {
            val chosen = options.firstOrNull { it.id == item.selectedOptionId }
            Text(if (item.selectedOptionId == item.correctOptionId) stringResource(R.string.correct) else stringResource(R.string.incorrect),
                fontWeight = FontWeight.Bold,
                color = if (item.selectedOptionId == item.correctOptionId) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            Text(stringResource(R.string.correct_answer_label, options.firstOrNull { it.id == item.correctOptionId }?.text.orEmpty()))
            Text(item.explanationSnapshot)
            if (chosen == null) Text(stringResource(R.string.unanswered))
        }
        if (attempt.mode == "EXAM") {
            TextButton(onClick = {
                vm.flag(id, current, !item!!.flagged)
                data = data.copy(questions = rows.mapIndexed { index, row -> if (index == current) row.copy(flagged = !row.flagged) else row })
            }) {
                Icon(Icons.Rounded.Flag, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(if (item?.flagged == true) R.string.unflag_for_review else R.string.flag_for_review))
            }
        }
        FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { current = (current - 1).coerceAtLeast(0) }, enabled = current > 0) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.previous))
            }
            if (current < rows.lastIndex) {
                Button(onClick = { current++ }) { Text(stringResource(R.string.next)); Spacer(Modifier.width(8.dp)); Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null) }
            } else {
                Button(onClick = { confirmSubmit = true }) { Text(stringResource(if (attempt.mode == "EXAM") R.string.submit_exam else R.string.finish_practice)) }
            }
        }
        Text(stringResource(R.string.question_navigator), style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEachIndexed { index, question ->
                val stateDescription = when {
                    question.flagged -> stringResource(R.string.flagged_state)
                    question.selectedOptionId != null -> stringResource(R.string.answered_state)
                    else -> stringResource(R.string.unanswered_state)
                }
                FilterChip(
                    selected = current == index,
                    onClick = { current = index },
                    modifier = Modifier.heightIn(min = 48.dp).semantics {
                        contentDescription = context.getString(R.string.question_number_accessibility, index + 1, stateDescription)
                    },
                    label = { Text("${index + 1}") },
                    leadingIcon = {
                        if (question.flagged) Icon(Icons.Rounded.Flag, contentDescription = null)
                        else if (question.selectedOptionId != null) Icon(Icons.Rounded.Check, contentDescription = null)
                    }
                )
            }
        }
    }
    if (confirmSubmit) {
        val unanswered = rows.count { it.selectedOptionId == null }
        AlertDialog(
            onDismissRequest = { confirmSubmit = false },
            title = { Text(stringResource(R.string.confirm_submission)) },
            text = { Text(stringResource(R.string.unanswered_count, unanswered), modifier = Modifier.verticalScroll(rememberScrollState())) },
            confirmButton = {
                TextButton(onClick = {
                    confirmSubmit = false
                    vm.submit(id) { nav.navigate("result/$id") { popUpTo("attempt/$id") { inclusive = true } } }
                }) { Text(stringResource(R.string.submit_attempt)) }
            },
            dismissButton = { TextButton(onClick = { confirmSubmit = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }
}

@Composable
private fun ResultScreen(id: String, state: AppUiState, vm: AppViewModel, nav: androidx.navigation.NavHostController, onBegin: (AttemptRequest) -> Unit) {
    var data by remember(id) { mutableStateOf(AttemptUiState()) }
    var filter by rememberSaveable(id) { mutableStateOf("ALL") }
    var deleteConfirm by rememberSaveable(id) { mutableStateOf(false) }
    LaunchedEffect(id) { vm.loadAttempt(id) { data = it } }
    val attempt = data.attempt
    if (attempt == null) {
        ScreenColumn { LoadingState() }
        return
    }
    val rows = data.questions
    val moduleSummary = rows.groupBy { it.moduleNameSnapshot }.mapValues { (_, questions) ->
        questions.count { it.selectedOptionId == it.correctOptionId } to questions.size
    }
    ScreenColumn {
        Text(if (attempt.passed == true) stringResource(R.string.passed) else stringResource(R.string.not_passed),
            color = if (attempt.passed == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleLarge)
        Text(stringResource(R.string.score_line, stringResource(R.string.score), attempt.correctCount ?: 0, attempt.questionCount, attempt.percentage?.roundToInt() ?: 0))
        Text(stringResource(
            R.string.result_counts,
            stringResource(R.string.correct), attempt.correctCount ?: 0,
            stringResource(R.string.incorrect), attempt.incorrectCount ?: 0,
            stringResource(R.string.unanswered), attempt.unansweredCount ?: 0
        ))
        Text(stringResource(R.string.elapsed_submission, formatDuration(attempt.elapsedMillis ?: 0), formatDate(attempt.completedAt)))
        Text(stringResource(R.string.saved_threshold, attempt.passThreshold))
        Text(stringResource(R.string.module_performance), style = MaterialTheme.typography.titleMedium)
        moduleSummary.forEach { (name, scores) -> Text(stringResource(R.string.module_score, name, scores.first, scores.second)) }
        Text(stringResource(R.string.review_answers), style = MaterialTheme.typography.titleLarge)
        ChoiceChips(listOf("ALL", "WRONG"), filter) { filter = it }
        rows.forEachIndexed { index, question ->
            val options = OopRepository.decodeOptions(question.optionsSnapshot)
            val isCorrect = question.selectedOptionId == question.correctOptionId
            if (filter != "WRONG" || !isCorrect) {
                AppCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${index + 1}. ${question.questionTextSnapshot}", fontWeight = FontWeight.SemiBold)
                        question.codeSnapshot?.let { CodeCard(it) }
                        Text(stringResource(R.string.your_answer, options.firstOrNull { it.id == question.selectedOptionId }?.text ?: stringResource(R.string.unanswered)))
                        Text(stringResource(R.string.correct_answer, options.firstOrNull { it.id == question.correctOptionId }?.text.orEmpty()))
                        Text(question.explanationSnapshot)
                        if (!isCorrect) {
                            val lesson = data.questions.firstOrNull { it.lessonId == question.lessonId }
                            TextButton(onClick = { nav.navigate("lesson/${lesson?.lessonId ?: question.lessonId}") }) {
                                Text(stringResource(R.string.related_lesson_label, stringResource(R.string.related_lessons), question.lessonId))
                            }
                        }
                    }
                }
            }
        }
        Button(onClick = {
            onBegin(AttemptRequest(
                mode = attempt.mode, moduleIds = attempt.moduleIds.split(",").filter { it.isNotBlank() }.toSet(),
                lessonId = attempt.lessonId,
                difficulty = attempt.difficulty.takeUnless { it == "ALL" }, questionCount = attempt.questionCount,
                durationMinutes = attempt.durationMinutes, passThreshold = state.settings["pass_threshold"]?.toIntOrNull() ?: 70
            ))
        }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.start_again)) }
        OutlinedButton(onClick = { deleteConfirm = true }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.delete_result)) }
    }
    if (deleteConfirm) {
        ConfirmDialog(stringResource(R.string.delete_result_confirm_title), stringResource(R.string.delete_result_confirm_body), onCancel = { deleteConfirm = false }) {
            vm.deleteResult(id)
            deleteConfirm = false
            nav.popBackStack()
        }
    }
}

@Composable
private fun HistoryScreen(state: AppUiState, vm: AppViewModel, onResult: (String) -> Unit) {
    var mode by rememberSaveable { mutableStateOf("ALL") }
    var confirmClear by rememberSaveable { mutableStateOf(false) }
    ScreenColumn {
        Text(stringResource(R.string.mode_filter))
        ChoiceChips(listOf("ALL", "PRACTICE", "EXAM"), mode) { mode = it }
        if (state.attempts.isNotEmpty()) TextButton(onClick = { confirmClear = true }) { Icon(Icons.Rounded.Delete, contentDescription = null); Spacer(Modifier.width(8.dp)); Text(stringResource(R.string.clear_history)) }
        val filtered = state.attempts.filter { mode == "ALL" || it.mode == mode }
        if (filtered.isEmpty()) EmptyCard(stringResource(R.string.empty_history))
        filtered.forEach { attempt ->
            AppCard(onClick = { onResult(attempt.id) }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (attempt.mode == "EXAM") Icons.Rounded.Timer else Icons.Rounded.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(pluralStringResource(R.plurals.attempt_summary, attempt.questionCount, stringResource(if (attempt.mode == "EXAM") R.string.exam_mode else R.string.practice_mode), attempt.questionCount))
                        Text(stringResource(R.string.attempt_result_summary, attempt.correctCount ?: 0, attempt.questionCount, attempt.percentage?.roundToInt() ?: 0, formatDate(attempt.completedAt)))
                    }
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = stringResource(R.string.open_result))
                }
            }
        }
    }
    if (confirmClear) ConfirmDialog(stringResource(R.string.clear_history_confirm_title), stringResource(R.string.clear_history_confirm_body), { confirmClear = false }) {
        vm.clearHistory(); confirmClear = false
    }
}

@Composable
private fun SettingsScreen(state: AppUiState, vm: AppViewModel, onAbout: () -> Unit) {
    var confirmReset by rememberSaveable { mutableStateOf(false) }
    var confirmAll by rememberSaveable { mutableStateOf(false) }
    val scale = state.settings["text_scale"]?.toFloatOrNull() ?: 1f
    val threshold = state.settings["pass_threshold"]?.toIntOrNull() ?: 70
    ScreenColumn {
        Text(stringResource(R.string.system_theme), style = MaterialTheme.typography.titleMedium)
        ChoiceChips(listOf("SYSTEM", "LIGHT", "DARK"), state.settings["theme"] ?: "SYSTEM") { vm.updateSetting("theme", it) }
        Text(stringResource(R.string.reading_text_size), style = MaterialTheme.typography.titleMedium)
        Slider(value = scale, onValueChange = { vm.updateSetting("text_scale", "%.2f".format(java.util.Locale.US, it)) }, valueRange = 0.85f..1.3f)
        Text("${(scale * 100).roundToInt()}%")
        Text(stringResource(R.string.pass_threshold, threshold), style = MaterialTheme.typography.titleMedium)
        Slider(value = threshold.toFloat(), onValueChange = { vm.updateSetting("pass_threshold", ((it / 5).roundToInt() * 5).coerceIn(50, 100).toString()) }, valueRange = 50f..100f, steps = 9)
        AppCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(R.string.privacy_title), fontWeight = FontWeight.SemiBold)
                Text(stringResource(R.string.privacy_body))
            }
        }
        HorizontalDivider()
        Text(stringResource(R.string.data_management), style = MaterialTheme.typography.titleMedium)
        OutlinedButton(onClick = { confirmReset = true }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.reset_progress)) }
        OutlinedButton(onClick = { confirmAll = true }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.reset_all_data)) }
        Text(stringResource(R.string.data_reset_explanation), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    if (confirmReset) ConfirmDialog(stringResource(R.string.reset_progress_title), stringResource(R.string.reset_progress_body), { confirmReset = false }) {
        vm.resetProgress(); confirmReset = false
    }
    if (confirmAll) ConfirmDialog(stringResource(R.string.reset_all_title), stringResource(R.string.reset_all_body), { confirmAll = false }) {
        vm.resetAll(); confirmAll = false
    }
}

@Composable
private fun AboutScreen() {
    ScreenColumn {
        Text(stringResource(R.string.about_body))
        Text(stringResource(R.string.privacy_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.privacy_body))
        Text(stringResource(R.string.app_version), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.license_summary))
        Text(stringResource(R.string.offline_timing_note))
    }
}

@Composable
private fun ChoiceChips(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        options.forEach { option ->
            val label = when (option) {
                "ALL" -> stringResource(R.string.all)
                "WRONG" -> stringResource(R.string.wrong_answers_only)
                "MODULE" -> stringResource(R.string.module_exam)
                "FULL" -> stringResource(R.string.full_exam)
                "CUSTOM" -> stringResource(R.string.custom_exam)
                "SYSTEM" -> stringResource(R.string.system_theme)
                "LIGHT" -> stringResource(R.string.light_theme)
                "DARK" -> stringResource(R.string.dark_theme)
                "PRACTICE" -> stringResource(R.string.practice_mode)
                "EXAM" -> stringResource(R.string.exam_mode)
                "Beginner" -> stringResource(R.string.beginner)
                "Intermediate" -> stringResource(R.string.intermediate)
                "Advanced" -> stringResource(R.string.advanced)
                else -> option
            }
            FilterChip(selected == option, onClick = { onSelect(option) }, modifier = Modifier.heightIn(min = 48.dp), label = { Text(label) })
        }
    }
}

@Composable
private fun CodeCard(code: String) {
    AppCard(Modifier.fillMaxWidth()) {
        Text(code, style = MaterialTheme.typography.bodySmall.codeStyle(),
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(12.dp))
    }
}

@Composable
private fun ScreenColumn(content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).verticalScroll(rememberScrollState()).padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

@Composable
private fun EmptyCard(text: String) {
    AppCard(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.secondaryContainer) {
                Icon(Icons.AutoMirrored.Rounded.MenuBook, contentDescription = null,
                    modifier = Modifier.padding(16.dp).size(32.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Text(text, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ConfirmDialog(title: String, message: String, onCancel: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text(title) },
        text = { Text(message, modifier = Modifier.verticalScroll(rememberScrollState())) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.confirm)) } },
        dismissButton = { TextButton(onClick = onCancel) { Text(stringResource(R.string.cancel)) } }
    )
}

private fun formatDate(time: Long?): String = time?.let { DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(it)) } ?: "—"
private fun formatDuration(millis: Long): String {
    val seconds = (millis / 1000).coerceAtLeast(0)
    return "%02d:%02d".format(seconds / 60, seconds % 60)
}

@Preview(name = "About light", showBackground = true, widthDp = 320, fontScale = 1.3f)
@Preview(name = "About dark landscape", showBackground = true, widthDp = 640, heightDp = 360, fontScale = 1.3f, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AboutPreview() {
    AppTheme(dark = androidx.compose.foundation.isSystemInDarkTheme()) {
        PreviewScreen(R.string.about) { AboutScreen() }
    }
}

@Preview(name = "Practice light", showBackground = true, widthDp = 320, fontScale = 1.3f)
@Preview(name = "Practice dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PracticePreview() {
    AppTheme(dark = androidx.compose.foundation.isSystemInDarkTheme()) {
        PreviewScreen(R.string.practice) { PracticeSetupScreen(previewState(), {}) }
    }
}

@Composable
private fun AppTopBar(
    title: String,
    showBack: Boolean,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    actions: @Composable RowScope.() -> Unit = {},
    floating: Boolean = false
) {
    val bar: @Composable () -> Unit = {
        TopAppBar(
            title = {
                if (floating) FittingSingleLineText(title, style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface)
                else Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            navigationIcon = {
                if (showBack) IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            actions = actions,
            windowInsets = if (floating) WindowInsets(0, 0, 0, 0) else TopAppBarDefaults.windowInsets,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = if (floating) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surface,
                scrolledContainerColor = if (floating) MaterialTheme.colorScheme.surfaceContainerLow else MaterialTheme.colorScheme.surface
            ),
            scrollBehavior = scrollBehavior
        )
    }
    if (floating) {
        Box(Modifier.windowInsetsPadding(WindowInsets.statusBars).padding(horizontal = 16.dp, vertical = 8.dp)) {
            Surface(shape = MaterialTheme.shapes.extraLarge, tonalElevation = 3.dp, shadowElevation = 3.dp) { bar() }
        }
    } else bar()
}

@Composable
private fun LoadingState() {
    Surface(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow) {
        Column(Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CircularProgressIndicator()
            Text(stringResource(R.string.loading), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun PreviewScreen(titleId: Int, content: @Composable () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppTopBar(stringResource(titleId), false, {}, scrollBehavior) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) { content() }
    }
}

private fun previewState() = AppUiState(
    modules = com.juanmediahubph.ooptutorial.data.seed.SeedContent.modules,
    lessons = com.juanmediahubph.ooptutorial.data.seed.SeedContent.lessons.map {
        LessonEntity(it.id, it.moduleId, it.title, it.difficulty, it.objectives, it.readingMinutes,
            it.explanation, it.example, it.code, it.codeExplanation, it.commonMistakes, it.takeaway)
    }
)

@Preview(name = "Lessons light", showBackground = true, widthDp = 320, fontScale = 1.3f)
@Preview(name = "Lessons dark landscape", showBackground = true, widthDp = 640, heightDp = 360, fontScale = 1.3f, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LessonsPreview() {
    AppTheme { PreviewScreen(R.string.lessons) { ModuleListScreen(previewState(), {}) } }
}

@Preview(name = "Exams light", showBackground = true, widthDp = 320, fontScale = 1.3f)
@Preview(name = "Exams dark", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ExamsPreview() {
    AppTheme { PreviewScreen(R.string.exams) { ExamSetupScreen(previewState(), {}) } }
}
