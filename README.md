# Trace SDK Samples

Reference apps demonstrating the [Trace](https://clicktrace.io) install attribution and deep links SDK.

All samples default to **test mode** (`TraceConfig.test()`) — no API key, no server, no setup required. Clone and run.

| Sample | Platform | Key Features |
|--------|----------|-------------|
| [`android/`](android/) | Android (Jetpack Compose) | Navigation3, deep link routing, auth-gated navigation |
| [`ios/`](ios/) | iOS (SwiftUI) | TraceProvider, route mapping, deferred deep links |
| [`kmp/`](kmp/) | Kotlin Multiplatform (Compose) | Shared UI across Android & iOS, Navigation3, cross-platform deep links |

## Quick Start

### Android

```bash
cd android
./gradlew installDebug
```

### iOS

Open `ios/TraceSample/TraceSample.xcodeproj` in Xcode → Run.

### KMP (Compose Multiplatform)

```bash
# Android target
cd kmp
./gradlew installDebug

# iOS target
open kmp/iosApp/iosApp.xcodeproj
# Select a simulator → Run
```

## Using Real Credentials

Each sample includes commented-out code showing how to switch from test mode to real credentials. Search for `"To use real credentials"` in the entry point files:

- `android/` → `SampleApp.kt`
- `ios/` → `SampleApp.swift`
- `kmp/` → `SampleApp.kt` (Android) / `MainViewController.kt` (iOS)

## Documentation

- [Quickstart](https://clicktrace.io/docs/quickstart)
- [Android SDK](https://clicktrace.io/docs/sdk/android)
- [iOS SDK](https://clicktrace.io/docs/sdk/ios)
- [Deep Links](https://clicktrace.io/docs/sdk/deep-links)
