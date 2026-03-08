plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace   = "com.trace.sample"
    compileSdk  = 36

    defaultConfig {
        applicationId = "com.trace.sample"
        minSdk        = 24
        targetSdk     = 36
        versionCode   = 1
        versionName   = "1.0"
    }

    buildTypes {
        debug   { isDebuggable = true }
        release { isMinifyEnabled = false }
    }

    buildFeatures {
        compose      = true
        buildConfig  = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    // Trace SDK — published on Maven Central
    implementation(libs.trace.sdk)
    implementation(libs.trace.sdk.nav3)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(libs.compose.ui.tooling)
}
