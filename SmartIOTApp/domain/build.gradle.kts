plugins {
    kotlin("jvm")
    id("kotlin-kapt")
    id("jacoco")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    testImplementation(libs.junit)
}
