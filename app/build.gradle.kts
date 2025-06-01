import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android) // hilt
    alias(libs.plugins.kotlin.kapt) // kapt
    alias(libs.plugins.kotlin.serialization) // serialization
    alias(libs.plugins.kotlin.parcelize) // parcelize
    alias(libs.plugins.devtools.ksp) // ksp
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "project.fix.skripsi"
    compileSdk = 35

    defaultConfig {
        applicationId = "project.fix.skripsi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val baseUrl = localProperties.getProperty("BASE_URL")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        val endpoint = localProperties.getProperty("ENDPOINT")
        buildConfigField("String", "ENDPOINT", "\"$endpoint\"")

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
        buildConfig = true
    }
}

dependencies {

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

    /**
     * Domain Dependency
     */

    // coroutines core
    implementation(libs.kotlinx.coroutines.core)

    /**
     * Data Dependency
     */

    // retrofit
    implementation (libs.retrofit)
    implementation (libs.retrofit2.converter.gson)
    implementation(libs.logging.interceptor)

    // coroutines
    implementation (libs.kotlinx.coroutines.android)

    /**
     * App Dependency
     */

    // hilt for DI
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // coil
    implementation(libs.coil3.coil.compose)
    implementation(libs.coil.network.okhttp)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    // more icons
    implementation(libs.androidx.material.icons.extended)

    // shimmer
    implementation(libs.compose.shimmer)

    // accompanist permission
    implementation (libs.accompanist.permissions)

    // room database
    implementation(libs.androidx.room.runtime)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp (libs.androidx.room.compiler)

    // gson converter
    implementation (libs.gson)

    // paging3
    implementation (libs.paging.runtime)
    implementation(libs.androidx.paging.compose)
}