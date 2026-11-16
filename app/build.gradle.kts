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
        versionCode = 6
        versionName = "0.6.0" // focus-driven areas, settings cards and app favourites
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
