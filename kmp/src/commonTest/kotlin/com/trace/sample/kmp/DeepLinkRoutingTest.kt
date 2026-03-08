package com.trace.sample.kmp

import com.trace.sdk.DeepLink
import com.trace.sdk.deeplink.DeepLinkMapperBuilder
import com.trace.sdk.deeplink.DeepLinkResult
import kotlin.test.*

class DeepLinkRoutingTest {

    /** Mirrors the rememberDeepLinkMapper block from AppNavigation.kt */
    private fun buildSampleMapper(): (DeepLink) -> DeepLinkResult<AppRoute>? =
        DeepLinkMapperBuilder<AppRoute>().apply {
            route("/product/{id}") { ProductRoute(id = it.require("id")) }
            route("/checkout") { CheckoutRoute(promo = it["promo"]) }
            action("/claim/promo/{amount}") { /* side-effect */ }
        }.build()

    private val mapper = buildSampleMapper()

    private fun DeepLinkResult<AppRoute>?.assertNavigatesTo(expected: AppRoute) {
        assertNotNull(this)
        assertIs<DeepLinkResult.Navigate<AppRoute>>(this)
        assertEquals(expected, route)
    }

    @Test
    fun productRouteMatches() {
        mapper(DeepLink(path = "/product/abc"))
            .assertNavigatesTo(ProductRoute("abc"))
    }

    @Test
    fun productRouteWithNumericId() {
        mapper(DeepLink(path = "/product/12345"))
            .assertNavigatesTo(ProductRoute("12345"))
    }

    @Test
    fun checkoutWithPromoParam() {
        mapper(DeepLink(path = "/checkout", params = mapOf("promo" to "SAVE20")))
            .assertNavigatesTo(CheckoutRoute(promo = "SAVE20"))
    }

    @Test
    fun checkoutWithoutPromo() {
        mapper(DeepLink(path = "/checkout"))
            .assertNavigatesTo(CheckoutRoute(promo = null))
    }

    @Test
    fun unknownPathReturnsNull() {
        assertNull(mapper(DeepLink(path = "/unknown/route")))
    }

    @Test
    fun actionRouteExecutesSideEffect() {
        val result = mapper(DeepLink(path = "/claim/promo/500"))
        assertNotNull(result)
        assertIs<DeepLinkResult.Action>(result)
        // Action should execute without throwing
        result.execute()
    }

    @Test
    fun deferredLinkStillRoutes() {
        mapper(DeepLink(path = "/product/xyz", isDeferred = true))
            .assertNavigatesTo(ProductRoute("xyz"))
    }

    @Test
    fun deferredCheckoutWithParams() {
        mapper(DeepLink(
            path = "/checkout",
            params = mapOf("promo" to "WELCOME10"),
            isDeferred = true,
        )).assertNavigatesTo(CheckoutRoute(promo = "WELCOME10"))
    }
}
