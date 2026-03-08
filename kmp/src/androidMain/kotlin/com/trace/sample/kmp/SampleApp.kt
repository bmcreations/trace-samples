package com.trace.sample.kmp

import android.app.Application
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Test mode — no API key needed, simulated attribution + deep links.
        // To use real credentials:
        //   config = TraceConfig(
        //       apiKey   = "tr_live_your_key",
        //       hashSalt = "your_64_char_hex",
        //       region   = Region.US,
        //   ),
        Trace.initialize(
            context = this,
            config = TraceConfig.test(),
        )
    }
}
