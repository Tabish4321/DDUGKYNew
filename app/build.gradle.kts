import java.util.Properties
import kotlin.apply

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.deendayalproject"
    compileSdk = 35

    val  keystorePropertiesFile = rootProject.file("keystore.properties")
    val  projectProperties=readProperties(keystorePropertiesFile)


    defaultConfig {
        applicationId = "com.deendayalproject"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.1.3" //1.1.2 //1.2.9//1.3.2
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            applicationIdSuffix = ""
            versionNameSuffix = ""
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    defaultConfig {
        buildConfigField("String", "CRYPT_ID", projectProperties["CRYPT_ID"] as String)
        buildConfigField("String", "CRYPT_IV", projectProperties["CRYPT_IV"] as String)
        buildConfigField("String", "CRYPLIBAES", projectProperties["CRYPLIBAES"] as String)
        buildConfigField("String", "WADH_KEY", projectProperties["WADH_KEY"] as String)
        buildConfigField("String", "FACE_AUTH_UIADI", projectProperties["FACE_AUTH_UIADI"] as String)
        buildConfigField("String", "CAPTURE_INTENT", projectProperties["CAPTURE_INTENT"] as String)
        buildConfigField("String", "USER_NAME_FOR_APP", projectProperties["USER_NAME_FOR_APP"] as String)
    }

    flavorDimensions += "environment"
    productFlavors {
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", projectProperties["BASE_URL_LIVE"] as String)
        } //BASE_URL_LIVE

        create("demo") {
            dimension = "environment"
            applicationIdSuffix = ""
            versionNameSuffix = ""
            buildConfigField("String", "BASE_URL", projectProperties["BASE_URL_DEMO"] as String)
        }//BASE_URL_DEMO
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
    }
}

fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}


dependencies {
    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Material Design
    implementation("com.google.android.material:material:1.10.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    implementation(libs.androidx.datastore.core.android)
    kapt("com.google.dagger:hilt-compiler:2.51")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Location Services
    implementation("com.google.android.gms:play-services-location:21.3.0")

//    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
//    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    // Firebase
//    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
//    implementation("com.google.firebase:firebase-crashlytics-ktx")
//    implementation("com.google.firebase:firebase-analytics-ktx")


    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("org.bouncycastle:bcprov-jdk16:1.46")
    implementation("javax.xml.crypto:jsr105-api:1.0.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")


    implementation("javax.xml.stream:stax-api:1.0-2")
    implementation("com.fasterxml.woodstox:woodstox-core:6.5.1")

    implementation("org.apache.santuario:xmlsec:2.0.3") {
        exclude(group = "org.codehaus.woodstox")
    }

    implementation("com.thoughtworks.xstream:xstream:1.4.7") {
        exclude(group = "xmlpull", module = "xmlpull")
        exclude(group="xpp3", module="xpp3_min")

        implementation("com.github.bumptech.glide:glide:4.16.0")
        kapt("com.github.bumptech.glide:compiler:4.16.0")

    }
}

kapt {
    correctErrorTypes = false
}