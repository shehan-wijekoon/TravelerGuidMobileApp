plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.travelerguidemobileapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.travelerguidemobileapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- Navigation ---
    implementation("androidx.navigation:navigation-compose:2.7.7")
    // --- ViewModel and LiveData/Flow Integration ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    // --- Compose Material 3 Extended Icons ---
    implementation("androidx.compose.material:material-icons-extended")
    // Firebase BOM (Critical for version management)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    // Firebase Firestore SDK (Contains @DocumentId)
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Coroutines Play Services (For using suspend functions with Firebase)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")
    // Coil - Image loading library for Compose
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}