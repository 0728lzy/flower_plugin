plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.0")
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")
    implementation("org.ow2.asm:asm-util:9.6")
}

gradlePlugin {
    plugins {
        create("flowerCode") {
            id = "com.flower.code"
            implementationClass = "com.flower.FlowerCodePlugin"
        }
    }
}
