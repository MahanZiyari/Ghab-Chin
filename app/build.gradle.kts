plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //Ksp
    id("com.google.devtools.ksp")
    //Hilt
    id("com.google.dagger.hilt.android")
    // Safe Args
    id("androidx.navigation.safeargs")
}

android {
    namespace = "ir.mahan.ghabchin"
    compileSdk = 36

    defaultConfig {
        applicationId = "ir.mahan.ghabchin"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt-Dagger
    implementation("com.google.dagger:hilt-android:2.57")
    ksp("com.google.dagger:hilt-compiler:2.57")
    ksp("androidx.hilt:hilt-compiler:1.2.0")
    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // Lifecycle
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.8")
    // Navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.9.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.3")
    // Paging3
    implementation("androidx.paging:paging-runtime:3.3.6")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // Gson
    implementation("com.google.code.gson:gson:2.13.1")
    //OkHTTP client
    implementation ("com.squareup.okhttp3:okhttp:5.1.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.1.0")
    //Image Loading
    implementation ("io.coil-kt:coil:2.7.0")
    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")
    // Calligraphy
    implementation ("io.github.inflationx:calligraphy3:3.1.1")
    implementation ("io.github.inflationx:viewpump:2.0.3")
    // Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.github.omtodkar:ShimmerRecyclerView:v0.4.1")
    // Nouri
    implementation ("com.github.MrNouri:DynamicSizes:1.0")
    implementation ("com.github.MrNouri:RotateView:1.0.0")
    // Other
    implementation("io.writeopia:loading-button:3.0.0")
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.flaviofaria:kenburnsview:1.0.7")
    implementation("com.robertlevonyan.components:PermissionsFlow:1.2.8")
}