package com.trace.sample.kmp

import com.trace.sdk.DeepLink
import com.trace.sdk.Trace
import com.trace.sdk.TraceConfig
import com.trace.sdk.deeplink.DeepLinkMapperBuilder
import com.trace.sdk.deeplink.DeepLinkResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds

class DeferredDeepLinkFlowTest {

    private fun buildSampleMapper(): (DeepLink) -> DeepLinkResult<AppRoute>? =
        DeepLinkMapperBuilder<AppRoute>().apply {
            route("/product/{id}") { ProductRoute(id = it.require("id")) }
            route("/checkout") { CheckoutRoute(promo = it["promo"]) }
        }.build()

    @BeforeTest
    fun setUp() {
        Trace.resetForTesting()
    }

    @Test
    fun deferredDeepLinkMapsToProductRoute() = runTest {
        val simulated = DeepLink(path = "/product/42", isDeferred = true)
        val received = CompletableDeferred<DeepLink>()

        Trace.setDeepLinkListener { link -> received.complete(link) }
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = simulated))

        val link = withTimeout(5.seconds) { received.await() }
        val mapper = buildSampleMapper()
        val result = mapper(link)

        assertNotNull(result)
        assertIs<DeepLinkResult.Navigate<AppRoute>>(result)
        assertEquals(ProductRoute("42"), result.route)
    }

    @Test
    fun deferredDeepLinkWithQueryParams() = runTest {
        val simulated = DeepLink(
            path = "/checkout",
            params = mapOf("promo" to "WELCOME"),
            isDeferred = true,
        )
        val received = CompletableDeferred<DeepLink>()

        Trace.setDeepLinkListener { link -> received.complete(link) }
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = simulated))

        val link = withTimeout(5.seconds) { received.await() }
        val mapper = buildSampleMapper()
        val result = mapper(link)

        assertNotNull(result)
        assertIs<DeepLinkResult.Navigate<AppRoute>>(result)
        assertEquals(CheckoutRoute(promo = "WELCOME"), result.route)
    }

    @Test
    fun listenerReplayOnLateRegistration() = runTest {
        // Initialize FIRST (deep link will be parked since no listener yet)
        val simulated = DeepLink(path = "/product/late", isDeferred = true)
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = simulated))

        // Wait a bit for test mode attribution to complete
        kotlinx.coroutines.delay(1000)

        // Register listener AFTER — the pending link should be replayed
        val received = CompletableDeferred<DeepLink>()
        Trace.setDeepLinkListener { link -> received.complete(link) }

        val link = withTimeout(5.seconds) { received.await() }
        assertEquals("/product/late", link.path)
        assertTrue(link.isDeferred)
    }

    @Test
    fun unmappedDeferredLinkReturnsNull() = runTest {
        val simulated = DeepLink(path = "/unknown/route", isDeferred = true)
        val received = CompletableDeferred<DeepLink>()

        Trace.setDeepLinkListener { link -> received.complete(link) }
        Trace.initialize(config = TraceConfig.test(simulatedDeepLink = simulated))

        val link = withTimeout(5.seconds) { received.await() }
        val mapper = buildSampleMapper()
        val result = mapper(link)

        // Mapper returns null for unknown routes — app should handle gracefully
        assertNull(result)
    }
}
