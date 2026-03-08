# Trace Android Sample

A Jetpack Compose app demonstrating the Trace SDK with AndroidX Navigation3 deep link routing.

## Prerequisites

- Android Studio (2024.3+)
- JDK 21
- Android SDK 36

## Run

```bash
./gradlew installDebug
```

Or open this folder directly in Android Studio and run.

## What It Demonstrates

- **SDK initialization** with `TraceConfig.test()` (simulated attribution, no API key needed)
- **Navigation3 integration** via `rememberTraceEntryDecorator` and `rememberDeepLinkMapper`
- **Auth-gated deep links** — deep links park until the user completes login
- **Route mapping** — `/product/{id}`, `/invite/{code}`, `/checkout?promo=X`

## Test Deep Links

```bash
# Via adb
adb shell am start -a android.intent.action.VIEW -d "tracesample://open/product/42"
```

## Using Real Credentials

Edit `SampleApp.kt` — swap `TraceConfig.test()` for `TraceConfig(apiKey = "...", hashSalt = "...")`.

## SDK Version

Update the `trace` version in `gradle/libs.versions.toml`.
