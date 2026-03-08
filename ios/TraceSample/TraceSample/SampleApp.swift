import SwiftUI
import TraceSDK

@main
struct SampleApp: App {
    @StateObject private var auth = AuthViewModel()

    init() {
        // Test mode — no API key needed, simulated attribution + deep links.
        // To use real credentials:
        //   let config = TraceClientConfig(
        //       apiKey:  "tr_live_your_key",
        //       hashSalt: "your_64_char_hex"
        //   )
        let config: TraceClientConfig = .test(
            simulatedDeepLink: TraceDeepLink(
                path: "/product/demo-123",
                params: ["source": "sample"],
                isDeferred: true
            )
        )
        TraceClient.initialize(config: config)
    }

    var body: some Scene {
        WindowGroup {
            TraceProvider {
                AppRootView()
                    .environmentObject(auth)
            }
        }
    }
}
