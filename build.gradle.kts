plugins {
    // Other plugin aliases...
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript{
    dependencies{
        classpath("com.google.gms:google-services:4.4.2")

    }}

