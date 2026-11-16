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
        versionCode = 7
        versionName = "0.7.0" // universal sidebar branding and navigation
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
