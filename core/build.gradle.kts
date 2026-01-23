plugins {
    kotlin("jvm")
}

val gdxVersion: String by rootProject.extra

dependencies {
    implementation(kotlin("stdlib"))
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)
}
