package com.trace.sample.kmp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Simple auth state holder (KMP-compatible, no ViewModel dependency). */
class AuthState {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login()  { _isLoggedIn.value = true }
    fun logout() { _isLoggedIn.value = false }
}
