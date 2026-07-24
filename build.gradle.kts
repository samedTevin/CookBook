// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("androidx.navigation.safeargs") version "2.9.8" apply false
    id("com.google.devtools.ksp") version "2.3.4" apply false
}