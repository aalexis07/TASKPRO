plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)

}

android {

    namespace = "com.example.taskpro"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.taskpro"

        minSdk = 30
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(

                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),

                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Supabase
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.storage)

    // Ktor
    implementation(libs.ktor.client.android)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Biometría
    implementation(libs.biometric)

    // Coil
    implementation(libs.coil)

    // Google Maps
    implementation(libs.google.maps)
    implementation(libs.google.location)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.1.4")

    // Coroutines
    implementation(libs.kotlinx.coroutines.play)

    // TEST
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(
        libs.androidx.espresso.core
    )
}