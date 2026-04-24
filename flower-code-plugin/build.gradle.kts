plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

group = "com.flower"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.1.0")
    implementation("io.github.goldfish07.reschiper:plugin:0.1.0-rc4")
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
        create("protectSuite") {
            id = "com.flower.protect-suite"
            implementationClass = "com.flower.ProtectSuitePlugin"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/0728lzy/flower_plugin")

            credentials {
                username = project.findProperty("gpr.user") as String?
                    ?: project.findProperty("gprUser") as String?
                    ?: System.getenv("GPR_USER")
                    ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String?
                    ?: project.findProperty("gprKey") as String?
                    ?: System.getenv("GPR_KEY")
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
