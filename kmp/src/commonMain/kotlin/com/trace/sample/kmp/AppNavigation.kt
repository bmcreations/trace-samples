package com.trace.sample.kmp

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.trace.sdk.AttributionResult
import com.trace.sdk.compose.LocalTrace
import com.trace.sdk.compose.TraceProvider
import com.trace.sdk.compose.rememberDeepLinkMapper
import com.trace.sdk.nav3.navigateReplacing
import com.trace.sdk.nav3.rememberTraceEntryDecorator
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val navSerializersModule = SerializersModule {
    polymorphic(NavKey::class) {
        subclass(SplashRoute::class)
        subclass(LoginRoute::class)
        subclass(HomeRoute::class)
        subclass(ProductRoute::class)
        subclass(CheckoutRoute::class)
    }
}

@Composable
fun AppNavigation() {
    val authState = remember { AuthState() }
    val backStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = navSerializersModule
        },
        SplashRoute,
    )
    val isLoggedIn by authState.isLoggedIn.collectAsState()

    val mapper = rememberDeepLinkMapper {
        route("/product/{id}") { ProductRoute(id = it.require("id")) }
        route("/checkout") { CheckoutRoute(promo = it["promo"]) }
        action("/claim/promo/{amount}") { /* side-effect */ }
    }

    TraceProvider {
        val attribution by LocalTrace.current.attribution.collectAsState()
        val attributionMethod = (attribution as? AttributionResult.Attributed)?.method

        NavDisplay(
            modifier = Modifier.systemBarsPadding(),
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberTraceEntryDecorator(
                    backStack = backStack,
                    routeMapper = mapper,
                    authGate = { isLoggedIn }
                ),
            ),
            entryProvider = entryProvider {
                entry<SplashRoute> {
                    SplashScreen(
                        onReady = {
                            if (isLoggedIn) backStack.navigateReplacing(HomeRoute)
                            else backStack.navigateReplacing(LoginRoute)
                        }
                    )
                }

                entry<LoginRoute> {
                    LoginScreen(
                        onLogin = {
                            authState.login()
                            backStack.navigateReplacing(HomeRoute)
                        }
                    )
                }

                entry<HomeRoute> {
                    HomeScreen(
                        attributionMethod = attributionMethod,
                        onProductClick = { id -> backStack.add(ProductRoute(id)) },
                        onCheckout = { backStack.add(CheckoutRoute()) },
                        onLogout = {
                            authState.logout()
                            backStack.navigateReplacing(LoginRoute)
                        }
                    )
                }

                entry<ProductRoute> { route ->
                    ProductScreen(
                        id = route.id,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                entry<CheckoutRoute> { route ->
                    CheckoutScreen(
                        promo = route.promo,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}
