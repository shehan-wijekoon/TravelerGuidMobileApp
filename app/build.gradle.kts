plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.travelapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.travelapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Compose Compiler is bundled with Kotlin 2.0.21, so NO need to specify version
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // ----------------------------------------------------
    // ✅ Core Dependencies for Travel Post Feature
    // ----------------------------------------------------

    // Compose BOM (Always use the platform() dependency for Compose versions)
    implementation(platform(libs.androidx.compose.bom))

    // Compose Core + Material3
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Material 3 UI components

    // Activity Compose (required to set up Compose content in the Activity)
    implementation(libs.androidx.activity.compose)

    // Lifecycle ViewModel integration for Compose (required to use viewModel() function)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // Extended Icons (required for Icons.Filled.Add, Icons.Filled.Star, etc.)
    implementation("androidx.compose.material:material-icons-extended")

    // Coil for image loading (essential for loading image URLs)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // ----------------------------------------------------
    // Existing Dependencies
    // ----------------------------------------------------

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}