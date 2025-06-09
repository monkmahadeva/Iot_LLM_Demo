plugins {
    kotlin("jvm")
    id("kotlin-kapt")
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.register<JacocoReport>("jacocoTestReportDomain") {
    doNotTrackState("Adjusted for correct Kotlin class output directory.")

    dependsOn("test")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val classFiles = fileTree("${buildDir}/classes/kotlin/main") {
        exclude(
            "**/BuildConfig.*", "**/R.class", "**/Manifest*.*",
            "**/*Module*.*", "**/*Hilt*.*", "**/*_Impl*.*"
        )
    }

    val sourceFiles = files("$projectDir/src/main/kotlin")

    classDirectories.setFrom(classFiles)
    sourceDirectories.setFrom(sourceFiles)
    executionData.setFrom(fileTree(layout.buildDirectory).include("jacoco/test.exec"))

    doLast {
        println("✅ Jacoco report generated at: file://${layout.buildDirectory}/reports/jacoco/jacocoTestReportDomain/html/index.html")
    }
}

tasks.named("build") {
    dependsOn("jacocoTestReportDomain")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.kotlinx.coroutines.core)
}
