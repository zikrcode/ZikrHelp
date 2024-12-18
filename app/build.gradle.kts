/*
 * Copyright 2024 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //KotlinSymbolProcessing
    id("com.google.devtools.ksp")

    //Hilt
    id("dagger.hilt.android.plugin")

    //Serialization
    id("org.jetbrains.kotlin.plugin.serialization")
}

//Secret
val secretPropertiesFile = project.rootProject.file("secret.properties")
val secretProperties = Properties()
secretProperties.load(secretPropertiesFile.inputStream())

android {
    namespace = "com.zikrcode.zikrhelp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zikrcode.zikrhelp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        //Secret
        buildConfigField(
            "String",
            "OPEN_AI_API_KEY",
            "\"${secretProperties["OPEN_AI_API_KEY"]}\""
        )
    }

    signingConfigs {
        create("release") {
            storeFile = file(secretProperties["storeFile"] as String)
            storePassword = secretProperties["storePassword"] as String
            keyPassword = secretProperties["keyPassword"] as String
            keyAlias = secretProperties["keyAlias"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //LifecycleUtilitiesCompose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    //Navigation
    val navigation_version = "2.7.6"
    implementation("androidx.navigation:navigation-compose:$navigation_version")
    implementation("androidx.navigation:navigation-runtime-ktx:$navigation_version")

    //Hilt
    val dagger_hilt_version = "2.47"
    implementation("com.google.dagger:hilt-android:$dagger_hilt_version")
    ksp("com.google.dagger:hilt-android-compiler:$dagger_hilt_version")

    val android_hilt_version = "1.0.0"
    implementation ("androidx.hilt:hilt-navigation-compose:$android_hilt_version")
    ksp("androidx.hilt:hilt-compiler:$android_hilt_version")

    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    //MLKitTextRecognition
    implementation("com.google.mlkit:text-recognition:16.0.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.10.0")

    //SerializationJson
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    //RetrofitConverter
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.10.0")

    //OkHttpLoggingInterceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}