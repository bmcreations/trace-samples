package com.trace.sample

import android.app.Application
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig

// Test mode — no API key needed, simulated attribution + deep links.
// To use real credentials:
//   val traceConfig = TraceConfig(
//       apiKey   = "tr_live_your_key",
//       hashSalt = "your_64_char_hex",
//       region   = Region.US,
//   )
val traceConfig = TraceConfig.test()

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Trace.initialize(
            context = this,
            config = traceConfig,
        )
    }
}
