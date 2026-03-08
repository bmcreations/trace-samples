package com.trace.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TraceProvider handles intent forwarding automatically for Compose apps.
        // Non-Compose apps should call TraceAndroid.handleIntent(intent) manually.

        enableEdgeToEdge()

        setContent {
            SampleTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}
