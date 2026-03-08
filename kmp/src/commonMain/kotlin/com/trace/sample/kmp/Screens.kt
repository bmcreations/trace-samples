package com.trace.sample.kmp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ─── Splash ──────────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(onReady: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
    LaunchedEffect(Unit) { onReady() }
}

// ─── Login ───────────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Trace KMP Sample", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            "Sign in to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(48.dp))
        Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Continue")
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Deep links will resume automatically after login",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ─── Home ────────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onCheckout: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            "KMP sample using JetBrains Compose Multiplatform Navigation3 + Trace SDK.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        Button(onClick = { onProductClick("abc123") }, Modifier.fillMaxWidth()) {
            Text("Open product abc123")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { onProductClick("xyz789") }, Modifier.fillMaxWidth()) {
            Text("Open product xyz789")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onCheckout, Modifier.fillMaxWidth()) {
            Text("Checkout (no promo)")
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onLogout, Modifier.fillMaxWidth()) {
            Text("Log out")
        }
    }
}

// ─── Product ─────────────────────────────────────────────────────────────────

@Composable
fun ProductScreen(id: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text("\u2190 Back") }
        Spacer(Modifier.height(16.dp))
        Text("Product", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("ID", style = MaterialTheme.typography.labelMedium)
                Text(id, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// ─── Checkout ────────────────────────────────────────────────────────────────

@Composable
fun CheckoutScreen(promo: String?, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        TextButton(onClick = onBack) { Text("\u2190 Back") }
        Spacer(Modifier.height(16.dp))
        Text("Checkout", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        if (promo != null) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Promo", style = MaterialTheme.typography.labelMedium)
                    Text(promo, style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            Text(
                "No promo code",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
