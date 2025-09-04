pluginManagement {
    repositories {
        //中台 需要配置 国内镜像
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/google")

        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        //中台 需要配置 国内镜像
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/google")


        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven(url="https://s01.oss.sonatype.org/content/groups/public")
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/google")
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        maven("https://maven.aliyun.com/nexus/content/repositories/central")

        //GroMore SDK依赖
        maven ("https://artifact.bytedance.com/repository/pangle" )
        //applog
        maven ("https://artifact.bytedance.com/repository/Volcengine/" )
        //mintegral sdk依赖   引入mintegral sdk需要添加此maven
        maven ("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_support/" )
        maven("https://developer.huawei.com/repo/")
    }
}

rootProject.name = "translator"
include(":app")
 