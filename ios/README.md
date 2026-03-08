# Trace iOS Sample

A SwiftUI app demonstrating the Trace SDK with deep link routing and deferred deep links.

## Prerequisites

- Xcode 15+
- iOS 16+ deployment target

## Run

Open `TraceSample/TraceSample.xcodeproj` in Xcode → select a simulator → Run.

SPM will automatically resolve the TraceSDK dependency from [trace-sdk-ios](https://github.com/bmcreations/trace-sdk-ios).

## What It Demonstrates

- **SDK initialization** with `.test()` (simulated attribution, no API key needed)
- **TraceProvider** wrapping the root view
- **TraceRouteMapper** for declarative deep link → route mapping
- **Auth-gated navigation** — deep links park until the user authenticates
- **Deferred deep links** — simulated install with a `/product/demo-123` deep link

## Using Real Credentials

Edit `SampleApp.swift` — swap `.test(...)` for `TraceClientConfig(apiKey: "...", hashSalt: "...")`.

## SDK Version

Update the minimum version in the Xcode project's Swift Package dependency settings.
