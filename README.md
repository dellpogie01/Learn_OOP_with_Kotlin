# OOP Tutorial

A native, single-activity Android learning app for Kotlin object-oriented programming. Lessons and the complete 144-question bank are bundled with the APK; progress, bookmarks, settings, active attempts, and results are stored locally in Room/SQLite. Runtime features do not require a network, account, paid API, analytics, ads, or `INTERNET` permission.

## Requirements and build

- Android Studio with JDK 17 and Android SDK Platform 35 (or a compatible newer SDK).
- Gradle wrapper uses Gradle 8.14.3. The first build needs internet only to download Gradle and the free AndroidX/Kotlin build dependencies.

From Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
.\gradlew.bat connectedDebugAndroidTest
```

The debug APK is written to `app\build\outputs\apk\debug\app-debug.apk`. Instrumentation tests need an emulator or connected Android device.

## Architecture

- Kotlin, Jetpack Compose, Material 3, Navigation Compose, one Activity.
- MVVM: Compose observes a lifecycle-friendly `StateFlow`; the ViewModel dispatches actions to a repository.
- Manual dependency injection constructs the Room database and repository in `OopApplication`.
- Room is the abstraction over an on-device SQLite database. DAO work runs off the UI thread; seed import and multi-row finalization/reset operations are transactional.
- `domain\QuizLogic.kt` contains pure grading and without-replacement/balanced question selection. Attempt records hold shuffled option order and immutable review snapshots.
- Content is authored in `data\seed\SeedContent.kt`. It includes 12 ordered modules, 24 complete lessons, and 144 original questions (12 per module). The seed validator checks stable IDs, references, four distinct options, and valid correct-option indices before import.
- Room schema JSON is exported under `app\schemas`. The current database schema is version 1. Do not enable destructive migration fallback. For a schema change, increment the Room version, add and test a forward `Migration`, preserve foreign-key relationships and snapshots, and commit the newly exported schema. Content has its own `SeedContent.VERSION`; increase it when bundled lessons/questions change. Import is transactional and uses upserts so progress and bookmarks survive content updates. Historical attempt questions/options/explanations/module titles are snapshots, so content edits do not rewrite old results.

## Editing content

Edit the module and lesson entries and the per-question calls in `SeedContent.kt`. Keep IDs stable after release. Each question needs four distinct options; `correctIndex` identifies the correct option in seed source order. The importer assigns stable option IDs, then each new attempt saves its randomized option order and correct option ID. Run unit tests after edits; `SeedContent.validate()` is also called before every import. Avoid deleting referenced lesson/module IDs in a content update; if removal is necessary, plan an explicit non-destructive migration and retain historical snapshots.

Lesson snippets are display-only and can be copied; the app never executes them. Update the lesson's objective, explanation, relatable example, code explanation, common mistakes, takeaway, and questions together when revising a concept. Dates use the device locale.

## Offline, privacy, and timing

All learning data stays on this device. There is no cloud synchronization or account. Uninstalling or clearing app data removes the local database. Android cloud backup and device-transfer backup are disabled in the manifest and backup rules. The app does not request internet access.

Exam deadlines are persisted as device-clock timestamps and checked on resume; a live screen uses a monotonic elapsed clock for its countdown. The app does not use exact alarms or a background service. Fully offline timing cannot prevent deliberate device-clock manipulation across process/device restarts.

Scores are for self-study, not an accredited certification. The pass threshold is saved on each attempt; later settings changes apply only to new attempts.

## Dependencies and licenses

| Dependency | Purpose | License |
|---|---|---|
| Kotlin / Kotlin Coroutines | Language and asynchronous work | Apache License 2.0 |
| AndroidX Activity, Lifecycle, Navigation | Activity and lifecycle-aware navigation/state | Apache License 2.0 |
| Jetpack Compose, Material 3, Material icons | UI and bundled vector icons | Apache License 2.0 |
| AndroidX Room | Local SQLite persistence | Apache License 2.0 |
| Gradle / Android Gradle Plugin / KSP | Build and annotation processing | Apache License 2.0 |
| JUnit 4 | Unit tests | Eclipse Public License 1.0 |

Dependency artifacts and full license texts are distributed under their respective upstream terms. There are no bundled third-party image assets.
