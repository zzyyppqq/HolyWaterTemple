plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.greenrobot.greendao")
}


android {

    greendao {
        schemaVersion(3)
    }

    namespace = "com.holywatertemple"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.holywatertemple"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file("holy_release")
            keyAlias = "HolyWaterTemple"
            storePassword = "HolyWaterTemple"
            keyPassword = "HolyWaterTemple"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")

            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        debug {
            signingConfig = signingConfigs.getByName("debug")

            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false

            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.okhttp3:okhttp:4.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.0")
    //stetho
//    implementation("ccom.facebook.stetho:stetho:1.4.1")
//    implementation("ccom.facebook.stetho:stetho-okhttp3:1.4.1")
    //leakcanary
//    debugImplementation("com.squareup.leakcanary:leakcanary-android:1.5.4")
//    releaseImplementation("com.squareup.leakcanary:leakcanary-android-no-op:1.5.4")

    implementation(project(":share_lib"))
    implementation(project(":java_lib"))

    implementation("com.google.code.gson:gson:2.7")
    implementation("org.greenrobot:greendao:3.2.2")
//    implementation("net.zetetic:android-database-sqlcipher:3.5.9")
    implementation("io.reactivex:rxjava:1.2.0")
    implementation("io.reactivex:rxandroid:1.2.1")
//    implementation("com.nbsp:library:1.8")
//    implementation("com.github.nbsp-team:MaterialFilePicker:+")

//    implementation("com.droidninja:filepicker:2.2.5")
    implementation("com.yanzhenjie.recyclerview:x:1.3.2")

}