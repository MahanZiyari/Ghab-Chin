// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    //Ksp
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    //Hilt
    id ("com.google.dagger.hilt.android") version "2.57" apply false
    // Safe Args
    id ("androidx.navigation.safeargs") version "2.9.3" apply false
}