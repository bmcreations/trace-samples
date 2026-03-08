# Trace KMP Sample

A Kotlin Multiplatform app with Compose Multiplatform UI running on both Android and iOS, demonstrating shared deep link routing with the Trace SDK.

## Prerequisites

- Android Studio (2024.3+) with KMP plugin
- JDK 21
- Xcode 15+ (for iOS target)

## Run — Android

```bash
./gradlew installDebug
```

Or open this folder in Android Studio and run the Android target.

## Run — iOS

```bash
open iosApp/iosApp.xcodeproj
```

Select a simulator in Xcode → Run. The Gradle build runs automatically via the Xcode build phase to produce the `SampleKmp` framework.

## What It Demonstrates

- **Shared SDK initialization** — `TraceConfig.test()` in both Android and iOS entry points
- **Cross-platform Navigation3** via JetBrains Compose Multiplatform `navigation3-ui`
- **Shared deep link routing** — `rememberDeepLinkMapper` and `rememberTraceEntryDecorator` in `commonMain`
- **Auth-gated navigation** — deep links park until login completes
- **Route mapping** — `/product/{id}`, `/checkout?promo=X`, `/claim/promo/{amount}`

## Project Structure

```
src/
  commonMain/    Shared UI, navigation, routes (runs on both platforms)
  androidMain/   Android Application + Activity
  iosMain/       ComposeUIViewController + initializeTrace()
  commonTest/    SDK integration tests (TraceConfig.test())
iosApp/          Xcode project wrapping the KMP framework
```

## Test Deep Links

```bash
# Android
adb shell am start -a android.intent.action.VIEW -d "tracekmp://open/product/42"
```

## Using Real Credentials

- **Android**: Edit `SampleApp.kt` in `androidMain/`
- **iOS**: Edit `MainViewController.kt` in `iosMain/`

Swap `TraceConfig.test()` for `TraceConfig(apiKey = "...", hashSalt = "...", region = Region.US)`.

## SDK Version

Update the `trace` version in `gradle/libs.versions.toml`.
