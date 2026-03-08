package com.trace.sample.kmp

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey

@Serializable
data object SplashRoute : AppRoute

@Serializable
data object LoginRoute : AppRoute

@Serializable
data object HomeRoute : AppRoute

@Serializable
data class ProductRoute(val id: String) : AppRoute

@Serializable
data class CheckoutRoute(val promo: String? = null) : AppRoute
