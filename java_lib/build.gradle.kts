plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}


dependencies {
    //    implementation fileTree(dir: 'libs', include: ['*.jar'])
//    implementation files('libs/jxl.jar')
    implementation("com.hynnet:jxl:2.6.12.1")
}
