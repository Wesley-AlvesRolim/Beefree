import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val releaseKeystorePath = providers.environmentVariable("BEEFREE_KEYSTORE_PATH").orNull
val releaseKeystorePassword = providers.environmentVariable("BEEFREE_KEYSTORE_PASSWORD").orNull
val releaseKeyAlias = providers.environmentVariable("BEEFREE_KEY_ALIAS").orNull
val releaseKeyPassword = providers.environmentVariable("BEEFREE_KEY_PASSWORD").orNull
val releaseKeystoreFile =
    releaseKeystorePath?.let { path ->
        file(path).takeIf { it.exists() } ?: rootProject.file(path).takeIf { it.exists() }
    }

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("org.jlleitschuh.gradle.ktlint").version("14.0.1")
}

android {
    namespace = "com.wesley.beefree"
    compileSdk = 36

    signingConfigs {
        create("release") {
            storeFile = releaseKeystoreFile
            storePassword = releaseKeystorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    }

    defaultConfig {
        applicationId = "com.wesley.beefree"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/build/**")
        exclude("**/test/**")
        exclude("**/androidTest/**")
    }
}

tasks.getByPath("preBuild").dependsOn("ktlintFormat")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation(libs.androidx.material3)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.room.compiler)
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.vico.views)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.core.ktx)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
