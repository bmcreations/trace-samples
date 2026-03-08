import SwiftUI
import SampleKmp

@main
struct iOSApp: App {
    init() {
        MainViewControllerKt.initializeTrace()
    }

    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(.all)
        }
    }
}
