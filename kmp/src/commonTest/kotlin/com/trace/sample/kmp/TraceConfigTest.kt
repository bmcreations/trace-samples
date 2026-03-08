package com.trace.sample.kmp

import com.trace.sdk.DeepLink
import com.trace.sdk.Region
import com.trace.sdk.TraceConfig
import kotlin.test.*

class TraceConfigTest {

    @Test
    fun testFactoryHasDebugEnabled() {
        assertTrue(TraceConfig.test().debug)
    }

    @Test
    fun testFactoryHasTestMode() {
        assertNotNull(TraceConfig.test().testMode)
    }

    @Test
    fun testFactoryDefaultSimulatedLink() {
        val testMode = TraceConfig.test().testMode!!
        val link = testMode.simulatedDeepLink!!
        assertEquals("/test/welcome", link.path)
        assertTrue(link.isDeferred)
        assertEquals("test_mode", link.params["source"])
        assertEquals("sandbox", link.params["campaign"])
    }

    @Test
    fun customSimulatedDeepLink() {
        val custom = DeepLink(path = "/product/42", params = mapOf("ref" to "ad"), isDeferred = false)
        val config = TraceConfig.test(simulatedDeepLink = custom)
        assertEquals(custom, config.testMode!!.simulatedDeepLink)
    }

    @Test
    fun nullSimulatedDeepLink() {
        val config = TraceConfig.test(simulatedDeepLink = null)
        assertNull(config.testMode!!.simulatedDeepLink)
    }

    @Test
    fun defaultRegionIsUS() {
        val config = TraceConfig(apiKey = "key", hashSalt = "salt")
        assertEquals(Region.US, config.region)
    }

    @Test
    fun nullTestModeByDefault() {
        val config = TraceConfig(apiKey = "key", hashSalt = "salt")
        assertNull(config.testMode)
    }

    @Test
    fun enabledByDefault() {
        val config = TraceConfig(apiKey = "key", hashSalt = "salt")
        assertTrue(config.enabled)
    }

    @Test
    fun disabledConfig() {
        val config = TraceConfig(apiKey = "key", hashSalt = "salt", enabled = false)
        assertFalse(config.enabled)
    }

    @Test
    fun testFactoryDefaultCampaignId() {
        assertEquals("test_campaign", TraceConfig.test().testMode!!.simulatedCampaignId)
    }

    @Test
    fun testFactoryDefaultMethod() {
        assertEquals("TEST", TraceConfig.test().testMode!!.simulatedMethod)
    }
}
