package com.trace.sample

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.trace.sdk.compose.TraceProvider
import com.trace.sdk.compose.rememberDeepLinkMapper
import com.trace.sdk.nav3.navigateReplacing
import com.trace.sdk.nav3.rememberTraceEntryDecorator

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val backStack = rememberNavBackStack(SplashRoute)
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val mapper = rememberDeepLinkMapper {
        route("/product/{id}") { ProductRoute(id = it.require("id")) }
        route("/invite/{code}") { InviteRoute(code = it.require("code")) }
        route("/checkout") { CheckoutRoute(promo = it["promo"]) }
        // Action deep link — no navigation, just a side-effect
        action("/claim/promo/{amount}") { /* claimReward(it.require("amount")) */ }
    }

    TraceProvider {
        NavDisplay(
            modifier = Modifier.systemBarsPadding(),
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryDecorators = listOf(
                // Manage saved state
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
                            authViewModel.login()
                            backStack.navigateReplacing(HomeRoute)
                        }
                    )
                }

                entry<HomeRoute> {
                    HomeScreen(
                        onProductClick = { id -> backStack.add(ProductRoute(id)) },
                        onCheckout = { backStack.add(CheckoutRoute()) },
                        onLogout = {
                            authViewModel.logout()
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

                entry<InviteRoute> { route ->
                    InviteScreen(
                        code = route.code,
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
