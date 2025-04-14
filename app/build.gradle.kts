plugins {
    alias(libs.plugins.android.application);
    alias(libs.plugins.kotlin.android);
    alias(libs.plugins.kotlin.compose);
    id ("kotlin-kapt".toString());
}

android {
    namespace = "com.fwrt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fwrt"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        vectorDrawables.useSupportLibrary = true
    }
}

dependencies {
    // ZXing 条形码扫描库
    implementation (libs.zxing.android.embedded);

    // AndroidX 基础库
    implementation (libs.androidx.core.ktx.v1120);
    implementation (libs.androidx.appcompat.v161);
    implementation (libs.material);
    implementation (libs.androidx.constraintlayout);
    implementation (libs.androidx.recyclerview.v132);

    // Room 数据库
    implementation (libs.androidx.room.runtime)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.androidx.material3.android);
    kapt ("androidx.room:room-compiler:2.7.0");
    implementation (libs.androidx.room.ktx);

    // Lifecycle 协程支持
    implementation (libs.androidx.lifecycle.runtime.ktx.v262);
    implementation (libs.androidx.lifecycle.viewmodel.ktx);

    // 协程
    implementation (libs.kotlinx.coroutines.android);

    // 单元测试
    testImplementation (libs.junit);
    androidTestImplementation (libs.androidx.junit.v115);
    androidTestImplementation (libs.androidx.espresso.core.v351);
}
