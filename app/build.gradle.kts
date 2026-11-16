plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.regimtv.launcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.regimtv.launcher"
        minSdk = 23
        targetSdk = 28
        versionCode = 4
        versionName = "0.4.0" // app cards, ambient colour and remote navigation
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
