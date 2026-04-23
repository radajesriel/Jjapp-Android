# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build the project
./gradlew assembleDebug

# Run all unit tests
./gradlew clean test

# Run a single test class
./gradlew testDebugUnitTest --tests "com.jescoding.pixel.jjappandroid.core.domain.use_cases.GetDashboardItemsTest"

# Run instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest

# Lint check
./gradlew lint
```

JDK 17 (Temurin) is required. Kotlin targets JVM 11.

## Architecture

Single-module Android app using **Clean Architecture + MVVM** with Jetpack Compose.

### Layer Structure

- **Domain** (`core/domain/`): Repository interfaces, use cases, domain models, provider abstractions (DispatcherProvider, ResourceProvider, NetworkMonitor), sync interfaces (SyncManager, ImageSyncManager), auth interface (AuthManager)
- **Data** (`core/data/`): Repository implementations, Room database (DAO, entities, mappers), provider implementations, Hilt DI modules, sync engine (`core/data/sync/`), remote layer (`core/data/remote/`) with Supabase DTOs, data sources, auth, and storage
- **Presentation** (inside each feature): Compose screens, ViewModels with `StateFlow`/`SharedFlow` for UDF state management

### Feature Organization

Features live under `features/inventory/screens/` with each screen having its own `presentation/` package. The `add_edit_product` screen also has its own `data/`, `di/`, and `domain/` packages for feature-specific logic (e.g., `ProductImageRepository`, `SaveProduct` use case).

### Navigation

Type-safe routing via sealed class `Screen` in `navigation/Screen.kt`. Routes: Dashboard (start), Item/{itemSku}, AddProduct(?itemSku=). Nav graph is in `RootNavGraph.kt`.

### Offline-First Sync

Room is the single source of truth. Supabase provides cloud backend. Sync is invisible to the UI layer.

- **Sync tracking**: `DashboardItemEntity` has `syncStatus` (SYNCED/PENDING_CREATE/PENDING_UPDATE/PENDING_DELETE) and `updatedAt` columns
- **Sync trigger points**: On every local write (via `DashboardRepositoryImpl`), periodic (WorkManager every 15min), and on network recovery
- **Sync algorithm**: Push-first (push local changes, then pull remote). Last-write-wins conflict resolution using `updatedAt` timestamps
- **Key interception point**: `DashboardRepositoryImpl` — sets sync status on writes and triggers immediate sync. No changes needed in domain layer, use cases, or ViewModels
- **Auth**: Anonymous Supabase auth on first launch (`App.kt`). Supabase RLS enforces per-user data isolation
- **Image sync**: `ImageSyncManager` uploads/downloads product images to Supabase Storage bucket `product-images`
- **Config**: `SUPABASE_URL` and `SUPABASE_ANON_KEY` are set in `local.properties` and read via `BuildConfig`

### Dependency Injection

Hilt with modules in `core/data/di/` (DatabaseModule, AppModule, DomainModule, NetworkModule, SyncModule) and feature-level modules in `add_edit_product/di/`.

### Shared UI

Reusable composables in `shared/components/` (ImagePicker, LoadingScreen, SharedTopAppbar). Theme in `shared/theme/`.

## Testing Conventions

- **Framework**: JUnit 4 + MockK for mocking + Turbine for Flow testing + Google Truth for assertions
- **Base class**: `DefaultTestClass` (`core/DefaultTestClass.kt`) provides a mocked `DispatcherProvider` and fake `ResourceProvider` — extend this for ViewModel and use case tests
- **Dispatcher rule**: `MainDispatcherRule` (`util/MainDispatcherRule.kt`) swaps `Dispatchers.Main` with `UnconfinedTestDispatcher`
- **Test doubles**: Fake implementations exist under `core/data/local/fake/` (FakeDashboardRepositoryImpl) and `test/` source set (FakeDashboardData, FakeResourceProviderImpl)
- **Test mirrors source**: Test files follow the same package structure as production code

## Key Dependencies

Managed via version catalog (`gradle/libs.versions.toml`): Room 2.6.1, Hilt 2.57.2, Navigation Compose 2.9.6, Coil 2.7.0, Accompanist Permissions 0.37.3, Coroutines 1.10.2, Supabase-kt 3.1.0 (Postgrest, Auth, Storage), Ktor 3.1.1, WorkManager 2.10.1, kotlinx-serialization 1.7.3, kotlinx-datetime 0.6.1.

## CI

GitHub Actions runs `./gradlew clean test` on PRs to master and pushes to master. Test reports are uploaded as artifacts.
