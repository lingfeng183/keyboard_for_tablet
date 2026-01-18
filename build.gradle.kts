buildscript {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
    }
}

plugins {
    kotlin("jvm") version "1.9.21" apply false
    kotlin("android") version "1.9.21" apply false
}

allprojects {
    repositories {
        maven { url = uri("https://maven.google.com") }
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { url = uri("https://jitpack.io") }
    }
}

// libGDX version
extra["gdxVersion"] = "1.11.0"
extra["kotlinVersion"] = "1.9.21"
