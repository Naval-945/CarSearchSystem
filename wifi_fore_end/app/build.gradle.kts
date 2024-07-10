plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.dingwei.wifi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dingwei.wifi"
        minSdk = 30
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 移除不必要的重复依赖项
    // implementation (libs.appcompat.v7) // 如果已经有 `implementation(libs.appcompat)`
    // implementation(libs.constraint.layout) // 如果已经有 `implementation(libs.constraintlayout)`

    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
}
