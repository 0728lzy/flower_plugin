pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/google")
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven("https://maven.aliyun.com/nexus/content/repositories/central")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/google")
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven("https://maven.aliyun.com/nexus/content/repositories/central")
    }
}

rootProject.name = "translator"
include(":app")
 