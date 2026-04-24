pluginManagement {
    includeBuild("flower-code-plugin")
    repositories {
        //涓彴 闇€瑕侀厤缃?鍥藉唴闀滃儚
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
        //涓彴 闇€瑕侀厤缃?鍥藉唴闀滃儚
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

        //GroMore SDK渚濊禆
        maven ("https://artifact.bytedance.com/repository/pangle" )
        //applog
        maven ("https://artifact.bytedance.com/repository/Volcengine/" )
        //mintegral sdk渚濊禆   寮曞叆mintegral sdk闇€瑕佹坊鍔犳maven
        maven ("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_support/" )
        maven("https://developer.huawei.com/repo/")
        maven("https://artifact.bytedance.com/repository/Volcengine/")
    }
}

rootProject.name = "translator"
include(":app")
 
