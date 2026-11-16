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
        versionCode = 9
        versionName = "0.7.2" // navigation, profile rails, app manager and apps home customisation
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}
