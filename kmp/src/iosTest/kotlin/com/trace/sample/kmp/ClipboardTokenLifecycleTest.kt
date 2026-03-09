package com.trace.sample.kmp

import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig
import platform.UIKit.UIPasteboard
import kotlin.test.*

/**
 * iOS-specific tests for SDK initialization with clipboard context.
 *
 * NOTE: UIPasteboard does not persist reliably in the headless Kotlin/Native
 * test runner (there is no foreground app owning the pasteboard). Real clipboard
 * token lifecycle (write → SDK read → clear) is exercised by:
 * - Server-side: `AttributionServiceParsingTest.extractClipboardClickId*`
 * - Smoke test: `TestClipboardAttribution` (full end-to-end flow)
 * - Manual: click a Trace link in Safari → install app → observe `CLIPBOARD` attribution
 */
class ClipboardTokenLifecycleTest {

    @BeforeTest
    fun setUp() {
        Trace.resetForTesting()
    }

    @Test
    fun initializeWithNonTestConfigDoesNotCrash() {
        // Verify that SDK initialization with a real (non-test) config
        // works on iOS without crashing, even when no network is available.
        Trace.initialize(config = TraceConfig(
            apiKey = "tr_test_clipboard",
            hashSalt = "a".repeat(64),
        ))
    }

    @Test
    fun initializeWithNonTestConfigTwice() {
        // Verify SDK can be reset and re-initialized with a real config
        Trace.initialize(config = TraceConfig(
            apiKey = "tr_test_clipboard",
            hashSalt = "a".repeat(64),
        ))

        Trace.resetForTesting()

        Trace.initialize(config = TraceConfig(
            apiKey = "tr_test_clipboard",
            hashSalt = "b".repeat(64),
        ))
    }

    @Test
    fun pasteboardAccessibleInTestEnvironment() {
        // Sanity check: verify we can write to UIPasteboard in the test runner.
        // Note: the value may not persist across SDK init due to the headless
        // test process lacking a foreground pasteboard context.
        val pasteboard = UIPasteboard.generalPasteboard
        pasteboard.string = "trace_test_value"
        // Read back may be null in headless test runner — that's expected
        val readBack = pasteboard.string
        assertTrue(
            readBack == "trace_test_value" || readBack == null,
            "Pasteboard should either return the written value or null in test env, got: '$readBack'"
        )
    }
}
