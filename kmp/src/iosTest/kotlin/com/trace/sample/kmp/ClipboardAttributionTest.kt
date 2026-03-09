package com.trace.sample.kmp

import com.trace.sdk.AttributionResult
import com.trace.sdk.DeepLink
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig
import com.trace.sdk.TraceIOS
import com.trace.sdk.test.TestModeConfig
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import platform.Foundation.NSURL
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

/**
 * iOS-specific tests for clipboard attribution flow and TraceIOS helpers.
 *
 * Tests simulated CLIPBOARD attribution via TestModeConfig, and verifies
 * TraceIOS universal/custom-scheme link handling works correctly.
 */
class ClipboardAttributionTest {

    @BeforeTest
    fun setUp() {
        Trace.resetForTesting()
    }

    // ─── Simulated clipboard attribution ────────────────────────────────────

    @Test
    fun simulatedClipboardAttributionDelivered() = runTest {
        val received = CompletableDeferred<AttributionResult>()

        Trace.setAttributionListener { result -> received.complete(result) }
        Trace.initialize(config = TraceConfig(
            apiKey = "test_key",
            hashSalt = buildString { repeat(64) { append("0") } },
            debug = true,
            testMode = TestModeConfig(
                simulatedMethod = "CLIPBOARD",
                simulatedCampaignId = "ios_clipboard_test",
            ),
        ))

        val result = withTimeout(5.seconds) { received.await() }
        assertIs<AttributionResult.Attributed>(result)
        assertEquals("CLIPBOARD", result.method)
        assertEquals("ios_clipboard_test", result.campaignId)
    }

    @Test
    fun simulatedClipboardAttributionWithDeepLink() = runTest {
        val deepLinkReceived = CompletableDeferred<DeepLink>()
        val attributionReceived = CompletableDeferred<AttributionResult>()

        Trace.setDeepLinkListener { link -> deepLinkReceived.complete(link) }
        Trace.setAttributionListener { result -> attributionReceived.complete(result) }

        val simulatedLink = DeepLink(
            path = "/product/123",
            params = mapOf("ref" to "clipboard_test"),
            isDeferred = true,
        )

        Trace.initialize(config = TraceConfig(
            apiKey = "test_key",
            hashSalt = buildString { repeat(64) { append("0") } },
            debug = true,
            testMode = TestModeConfig(
                simulatedDeepLink = simulatedLink,
                simulatedMethod = "CLIPBOARD",
            ),
        ))

        // Both attribution and deep link should fire
        val result = withTimeout(5.seconds) { attributionReceived.await() }
        assertIs<AttributionResult.Attributed>(result)
        assertEquals("CLIPBOARD", result.method)

        val link = withTimeout(5.seconds) { deepLinkReceived.await() }
        assertEquals("/product/123", link.path)
        assertEquals("clipboard_test", link.params["ref"])
        assertTrue(link.isDeferred)
    }

    // ─── TraceIOS link handling ─────────────────────────────────────────────

    @Test
    fun handleUniversalLinkWithValidUrl() {
        // Initialize in test mode first
        Trace.initialize(config = TraceConfig.test())

        val url = NSURL(string = "https://myapp.traceclick.io/product/42?trace_click_id=abc123")
        val handled = TraceIOS.handleUniversalLink(url)
        assertTrue(handled, "Universal link with valid path should return true")
    }

    @Test
    fun handleCustomSchemeWithClickId() {
        Trace.initialize(config = TraceConfig.test())

        val url = NSURL(string = "myapp://open?trace_click_id=xyz789")
        val handled = TraceIOS.handleCustomScheme(url)
        assertTrue(handled, "Custom scheme with click ID should return true")
    }

    @Test
    fun handleCustomSchemeWithoutClickId() {
        Trace.initialize(config = TraceConfig.test())

        val url = NSURL(string = "myapp://open/product/42")
        val handled = TraceIOS.handleCustomScheme(url)
        assertTrue(handled, "Custom scheme without click ID should still return true")
    }
}
