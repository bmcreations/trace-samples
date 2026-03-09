package com.trace.sample.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig

fun MainViewController() =
    ComposeUIViewController {
        SampleTheme {
            AppNavigation()
        }
    }

fun initializeTrace() {
    // Test mode — no API key needed, simulated attribution + deep links.
    // To use real credentials:
    //   config = TraceConfig(
    //       apiKey   = "tr_live_your_key",
    //       hashSalt = "your_64_char_hex",
    //       region   = Region.US,
    //   ),
    Trace.initialize(
        config = TraceConfig.test(),
    )
}
