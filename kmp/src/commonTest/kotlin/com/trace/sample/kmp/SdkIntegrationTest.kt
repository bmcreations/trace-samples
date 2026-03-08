package com.trace.sample.kmp

import com.trace.sdk.AttributionResult
import com.trace.sdk.DeepLink
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

class SdkIntegrationTest {

    @BeforeTest
    fun setUp() {
        Trace.resetForTesting()
    }

    @Test
    fun testModeDeliversDeferredDeepLink() = runTest {
        val received = CompletableDeferred<DeepLink>()

        Trace.setDeepLinkListener { link -> received.complete(link) }
        Trace.initialize(config = TraceConfig.test())

        val link = withTimeout(5.seconds) { received.await() }
        assertEquals("/test/welcome", link.path)
        assertTrue(link.isDeferred)
        assertEquals("test_mode", link.params["source"])
        assertEquals("sandbox", link.params["campaign"])
    }

    @Test
    fun testModeDeliversAttributionResult() = runTest {
        val received = CompletableDeferred<AttributionResult>()

        Trace.setAttributionListener { result -> received.complete(result) }
        Trace.initialize(config = TraceConfig.test())

        val result = withTimeout(5.seconds) { received.await() }
        assertIs<AttributionResult.Attributed>(result)
        assertEquals("TEST", result.method)
        assertEquals("test_campaign", result.campaignId)
    }

    @Test
    fun customSimulatedDeepLinkDelivered() = runTest {
        val custom = DeepLink(
            path = "/product/999",
            params = mapOf("ref" to "email_campaign"),
            isDeferred = true,
        )
        val received = CompletableDeferred<DeepLink>()

        Trace.setDeepLinkListener { link -> received.complete(link) }
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = custom))

        val link = withTimeout(5.seconds) { received.await() }
        assertEquals("/product/999", link.path)
        assertEquals("email_campaign", link.params["ref"])
        assertTrue(link.isDeferred)
    }

    @Test
    fun nullSimulatedDeepLinkSkipsDeepLinkListener() = runTest {
        var deepLinkCalled = false
        val attributionReceived = CompletableDeferred<AttributionResult>()

        Trace.setDeepLinkListener { deepLinkCalled = true }
        Trace.setAttributionListener { result -> attributionReceived.complete(result) }
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = null))

        // Attribution should still fire
        val result = withTimeout(5.seconds) { attributionReceived.await() }
        assertIs<AttributionResult.Attributed>(result)

        // Deep link listener should NOT have been called
        assertFalse(deepLinkCalled)
    }

    @Test
    fun resetForTestingAllowsReattribution() = runTest {
        // First attribution
        val first = CompletableDeferred<AttributionResult>()
        Trace.setAttributionListener { result -> first.complete(result) }
        Trace.initialize(config = TraceConfig.test())
        withTimeout(5.seconds) { first.await() }

        // Reset and re-initialize
        Trace.resetForTesting()

        val second = CompletableDeferred<AttributionResult>()
        Trace.setAttributionListener { result -> second.complete(result) }
        Trace.initialize(config = TraceConfig.test())
        val result = withTimeout(5.seconds) { second.await() }
        assertIs<AttributionResult.Attributed>(result)
    }
}
