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

- **Domain** (`core/domain/`): Repository interfaces, use cases, domain models, provider abstractions (DispatcherProvider, ResourceProvider)
- **Data** (`core/data/`): Repository implementations, Room database (DAO, entities, mappers), provider implementations, Hilt DI modules
- **Presentation** (inside each feature): Compose screens, ViewModels with `StateFlow`/`SharedFlow` for UDF state management

### Feature Organization

Features live under `features/inventory/screens/` with each screen having its own `presentation/` package. The `add_edit_product` screen also has its own `data/`, `di/`, and `domain/` packages for feature-specific logic (e.g., `ProductImageRepository`, `SaveProduct` use case).

### Navigation

Type-safe routing via sealed class `Screen` in `navigation/Screen.kt`. Routes: Dashboard (start), Item/{itemSku}, AddProduct(?itemSku=). Nav graph is in `RootNavGraph.kt`.

### Dependency Injection

Hilt with modules in `core/data/di/` (DatabaseModule, AppModule, DomainModule) and feature-level modules in `add_edit_product/di/`.

### Shared UI

Reusable composables in `shared/components/` (ImagePicker, LoadingScreen, SharedTopAppbar). Theme in `shared/theme/`.

## Testing Conventions

- **Framework**: JUnit 4 + MockK for mocking + Turbine for Flow testing + Google Truth for assertions
- **Base class**: `DefaultTestClass` (`core/DefaultTestClass.kt`) provides a mocked `DispatcherProvider` and fake `ResourceProvider` — extend this for ViewModel and use case tests
- **Dispatcher rule**: `MainDispatcherRule` (`util/MainDispatcherRule.kt`) swaps `Dispatchers.Main` with `UnconfinedTestDispatcher`
- **Test doubles**: Fake implementations exist under `core/data/local/fake/` (FakeDashboardRepositoryImpl) and `test/` source set (FakeDashboardData, FakeResourceProviderImpl)
- **Test mirrors source**: Test files follow the same package structure as production code

## Key Dependencies

Managed via version catalog (`gradle/libs.versions.toml`): Room 2.6.1, Hilt 2.57.2, Navigation Compose 2.9.6, Coil 2.7.0, Accompanist Permissions 0.37.3, Coroutines 1.10.2.

## CI

GitHub Actions runs `./gradlew clean test` on PRs to master and pushes to master. Test reports are uploaded as artifacts.
