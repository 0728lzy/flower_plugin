import java.util.*
import kotlin.random.Random
import com.github.megatronking.stringfog.plugin.StringFogExtension
import io.github.goldfish07.reschiper.plugin.Extension
import java.io.FileOutputStream
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("android-junk-code")
    id("com.flower.code")
}
apply(plugin = "stringfog")
apply(plugin = "io.github.goldfish07.reschiper")

val reschiperUtil by configurations.creating

fun loadResChiperWhiteList(configFile: File): Set<String> {
    if (!configFile.exists()) return emptySet()

    val document = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(configFile)
    val nodes = document.getElementsByTagName("path")

    return (0 until nodes.length)
        .mapNotNull { nodes.item(it).textContent?.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

val resChiperConfigFile = rootProject.file("tools/reschiper-config.xml")
val resChiperOutputBundleName = "app-release-obfuscated.aab"

fun envFlag(name: String, defaultValue: Boolean = false): Boolean {
    return when (System.getenv(name)?.trim()?.lowercase(Locale.ROOT)) {
        null, "" -> defaultValue
        "1", "true", "yes", "y", "on" -> true
        "0", "false", "no", "n", "off" -> false
        else -> defaultValue
    }
}

extensions.configure<Extension>("resChiper") {
    enableObfuscation = true
    obfuscationMode = "default"
    mergeDuplicateResources = true
    enableFileFiltering = false
    enableFilterStrings = false
    obfuscatedBundleName = resChiperOutputBundleName
    whiteList = loadResChiperWhiteList(resChiperConfigFile)
}

flowerCode {
    enabled = envFlag("FLOWER_CODE_ENABLE", true)
    protectAllProjectClasses = true

    targetClasses = mutableListOf()

    minTemplatesPerMethod = 2
    maxTemplatesPerMethod = 3

    excludeMethods = mutableSetOf("<init>", "<clinit>", "toString", "hashCode", "equals")
    excludeClassRegexes = mutableListOf(
        "com/catcsyun/liantadog/[a-z]+\\d+(/.*)?"
    )
    injectAtMethodStart = true
    injectAtMethodEnd = true
    injectBeforeReturn = true
}

extensions.configure<StringFogExtension>("stringfog") {
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    enable = true
    debug = true
}

val l_app_channel = "OPPO"   //CSJ HUAWEI BAIDU OPPO XIAOMI VIVO HONOR YYB                                𤓖
val l_version_code = 101
val l_version_name = "1.0.1.2-O"
val l_app_name="全能猫狗宠物翻译器"
val sign_name = "csliantacatdog"

tasks.register("generateObfuscationDict") {
    doLast {
        val firstChars = charArrayOf('l','I','O','o','S','s','Z','z','B','b')
        val otherChars = charArrayOf(
            'l','I','1',
            'O','0','o',
            'S','5','s',
            'Z','2','z',
            'B','8','b'
        )
        val size = 20000
        val minLen = 8
        val maxLen = 12
        val set = HashSet<String>(size)
        while (set.size < size) {
            val len = Random.nextInt(minLen, maxLen + 1)
            val sb = StringBuilder(len)
            sb.append(firstChars.random())
            repeat(len - 1) {
                sb.append(otherChars.random())
            }
            set.add(sb.toString())
        }
        val file = File(rootProject.projectDir, "./app/obf-dict.txt")
        file.bufferedWriter().use { writer ->
            set.forEach {
                writer.appendLine(it)
            }
        }    }}

tasks.named("preBuild") {
    dependsOn("generateObfuscationDict")
}


tasks.register("resDJApkGenerate") {
    group = "obfuscation"
    description = "Build release AAB, obfuscate resources with ReSChiper, convert to APK, then protect Dex with dpt-shell."
    dependsOn("resChiperRelease")
    val buildDir = layout.buildDirectory
    val appId = android.defaultConfig.applicationId
    val versionName = android.defaultConfig.versionName
    val channel = System.getenv("APP_CHANNEL") ?: l_app_channel
    val date = System.currentTimeMillis()
    val dynamicName = "${appId}_release_${date}_${channel}_${versionName}_obfuscated.apk"
    val inputAab = buildDir.file("outputs/bundle/release/app-release.aab").get().asFile
    val obfuscatedAab = buildDir.file("outputs/bundle/release/$resChiperOutputBundleName").get().asFile
    val outputApks = buildDir.file("outputs/apk/release/app-release-obfuscated.apks").get().asFile
    val finalApk = buildDir.file("outputs/apk/release/$dynamicName").get().asFile
    val dptOutputDir = buildDir.dir("outputs/apk/release/dpt").get().asFile
    val dptProtectedApk = buildDir.file("outputs/apk/release/${dynamicName.removeSuffix(".apk")}_dpt.apk").get().asFile
    val releaseOutputDir = file("release")
    val releaseOutputApk = file("release/${dynamicName.removeSuffix(".apk")}_o.apk")
    val bundletoolJar = file("${rootProject.projectDir}/tools/bundletool.jar")
    val dptJar = file("${rootProject.projectDir}/tools/dpt.jar")
    val signingConfig = android.signingConfigs.getByName("myConfig")
    doLast {
        if (!resChiperConfigFile.exists()) throw GradleException("Missing: ${resChiperConfigFile.absolutePath}")
        if (!inputAab.exists()) throw GradleException("Missing release bundle: ${inputAab.absolutePath}")
        if (!obfuscatedAab.exists()) throw GradleException("Missing ReSChiper output bundle: ${obfuscatedAab.absolutePath}")
        if (!bundletoolJar.exists()) throw GradleException("Missing: ${bundletoolJar.absolutePath}")
        outputApks.parentFile.mkdirs()
        releaseOutputDir.mkdirs()
        releaseOutputDir.listFiles()?.forEach { file ->
            if (file.isFile && file.extension.equals("apk", ignoreCase = true)) {
                file.delete()
            }
        }

        exec {
            commandLine(
                "${System.getProperty("java.home")}${File.separator}bin${File.separator}java",
                "-jar",
                bundletoolJar.absolutePath,
                "build-apks",
                "--bundle=${obfuscatedAab.absolutePath}",
                "--output=${outputApks.absolutePath}",
                "--mode=universal",
                "--overwrite",
                "--ks=${signingConfig.storeFile?.absolutePath}",
                "--ks-pass=pass:${signingConfig.storePassword}",
                "--ks-key-alias=${signingConfig.keyAlias}",
                "--key-pass=pass:${signingConfig.keyPassword}"
            )
        }

        if (outputApks.exists()) {
            ZipFile(outputApks).use { zip ->
                val entry = zip.getEntry("universal.apk") ?: throw GradleException("universal.apk not found in apks")
                zip.getInputStream(entry).use { input ->
                    FileOutputStream(finalApk).use { output ->
                        input.copyTo(output)
                    }
                }
            }
            outputApks.delete()
            println("APK Generated: ${finalApk.absolutePath}")

            var releaseCandidate = finalApk
            if (envFlag("DPT_ENABLE", true)) {
                if (!dptJar.exists()) throw GradleException("Missing: ${dptJar.absolutePath}")

                dptOutputDir.mkdirs()
                dptOutputDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.extension.equals("apk", ignoreCase = true)) {
                        file.delete()
                    }
                }

                val dptArgs = mutableListOf(
                    "${System.getProperty("java.home")}${File.separator}bin${File.separator}java",
                    "-jar",
                    dptJar.absolutePath,
                    "-f",
                    finalApk.absolutePath,
                    "-o",
                    dptOutputDir.absolutePath
                )

                if (envFlag("DPT_NO_SIGN")) dptArgs.add("-x")
                if (envFlag("DPT_DEBUG")) dptArgs.add("--debug")
                if (envFlag("DPT_DISABLE_ACF")) dptArgs.add("--disable-acf")
                if (envFlag("DPT_DUMP_CODE")) dptArgs.add("--dump-code")
                if (envFlag("DPT_NOISY_LOG")) dptArgs.add("--noisy-log")
                if (envFlag("DPT_KEEP_CLASSES")) dptArgs.add("-K")
                if (envFlag("DPT_SMALLER")) dptArgs.add("-S")
                if (envFlag("DPT_VERIFY_SIGN")) dptArgs.add("-vs")
                val dptExcludeAbi = System.getenv("DPT_EXCLUDE_ABI")
                    ?.takeIf { it.isNotBlank() }
                    ?: "x86,x86_64"
                dptArgs.addAll(listOf("-e", dptExcludeAbi))
                System.getenv("DPT_RULES_FILE")?.takeIf { it.isNotBlank() }?.let {
                    dptArgs.addAll(listOf("-r", file(it).absolutePath))
                }
                System.getenv("DPT_PROTECT_CONFIG")?.takeIf { it.isNotBlank() }?.let {
                    dptArgs.addAll(listOf("-c", file(it).absolutePath))
                }

                exec {
                    commandLine(dptArgs)
                }

                val generatedDptApk = dptOutputDir
                    .listFiles { file -> file.isFile && file.extension.equals("apk", ignoreCase = true) }
                    ?.maxByOrNull { it.lastModified() }
                    ?: throw GradleException("dpt-shell did not generate an APK in ${dptOutputDir.absolutePath}")

                generatedDptApk.copyTo(dptProtectedApk, overwrite = true)
                println("DPT APK Generated: ${dptProtectedApk.absolutePath}")
                releaseCandidate = dptProtectedApk
            }

            releaseCandidate.copyTo(releaseOutputApk, overwrite = true)
            println("Release APK Generated: ${releaseOutputApk.absolutePath}")
        }
    }
}

android {
//    namespace = "com.ruite.app.pet.translator"
    namespace = "com.catcsyun.liantadog"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cslianta.catdog"
        minSdk = 21
        targetSdk = 34
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() ?: l_version_code
        versionName = System.getenv("VERSION_NAME") ?: l_version_name
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue("string", "app_name", System.getenv("APP_NAME_PARAM") ?: l_app_name)
//        resValue("string", "app_channel", System.getenv("APP_CHANNEL") ?: l_app_channel)
        buildConfigField("String", "APP_CHANNEL", "\"${System.getenv("APP_CHANNEL") ?: l_app_channel}\"")
        buildConfigField("String", "URL_USER_AGREEMENT", "\"${System.getenv("URL_USER_AGREEMENT") ?: ""}\"")
        buildConfigField("String", "URL_PRIVACY_POLICY", "\"${System.getenv("URL_PRIVACY_POLICY") ?: ""}\"")

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a") // 只保留需要的架构
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // room配置
        javaCompileOptions {
            annotationProcessorOptions {
                argument("room.incremental", "true")
                argument("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    signingConfigs {
        create("myConfig") {
            storeFile = file("../sign/${sign_name}.jks")
            keyAlias = sign_name
            keyPassword = "${sign_name}123"
            storePassword = "${sign_name}123"
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }

    androidJunkCode {
        variantConfig {
            register("release"){
                //注意：这里的release是变体名称，如果没有设置productFlavors就是buildType名称，如果有设置productFlavors就是flavor+buildType，例如（freeRelease、proRelease）
                packageBase = "com.catcsyun.liantadog"  //生成java类根包名
                packageCount = System.getenv("JUNK_PACKAGE_COUNT")?.toIntOrNull() ?: 60 //生成包数量
                activityCountPerPackage = System.getenv("JUNK_ACTIVITY_COUNT")?.toIntOrNull() ?: 60//每个包下生成Activity类数量
                excludeActivityJavaFile = false
                //是否排除生成Activity的Java文件,默认false(layout和写入AndroidManifest.xml还会执行)，主要用于处理类似神策全埋点编译过慢问题
                otherCountPerPackage = System.getenv("JUNK_OTHER_PER_COUNT")?.toIntOrNull() ?: 60 //每个包下生成其它类的数量
                methodCountPerClass =  System.getenv("JUNK_OTHER_PER_COUNT")?.toIntOrNull() ?: 60   //每个类下生成方法数量
                resPrefix = "lteg_"  //生成的layout、drawable、string等资源名前缀
                drawableCount = System.getenv("JUNK_DRAWABLE_COUNT")?.toIntOrNull() ?: 600  //生成drawable资源数量
                stringCount = System.getenv("JUNK_DRAWABLE_COUNT")?.toIntOrNull() ?: 600 //生成string数量
            }
        }
    }

    buildTypes {
        getByName("release") {
            isZipAlignEnabled = false
            isMinifyEnabled = true
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("myConfig")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("myConfig")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures.viewBinding = true
    buildFeatures.buildConfig = true

    packagingOptions {
        jniLibs {
            // 从 AGP 4.2.0 开始，extractNativeLibs 清单属性已被 DSL 选项 useLegacyPackaging 取代
            // true 开启'.so'压缩 false 关闭
            useLegacyPackaging = true
        }
    }

    // 打包改名
    android.applicationVariants.all {
        val variant = this
        val buildType = variant.buildType.name
        val flavorName = variant.flavorName
        val applicationId=variant.applicationId
        val date = System.currentTimeMillis()
        val app_channel=System.getenv("APP_CHANNEL") ?: l_app_channel
//        val applicationId = this@all.applicationId

        variant.outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "${applicationId}_${flavorName}_${buildType}_${date}_${app_channel}_${variant.versionName}.apk"
            }
        }
    }
    buildFeatures {
        buildConfig = true
    }
    lint{
        abortOnError = false
    }
}

dependencies {
    implementation("com.github.megatronking.stringfog:xor:5.0.0")
    reschiperUtil("io.github.goldfish07.reschiper:plugin:0.1.0-rc4")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")


    implementation("com.airbnb.android:lottie:6.2.0")
    implementation("com.github.lygttpod:SuperTextView:2.4.6")
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.2")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.github.liangjingkanji:Net:3.6.4")


    implementation("com.hyman:flowlayout-lib:1.1.2")

    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
    implementation("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")

    implementation("com.github.getActivity:XXPermissions:18.63")//权限管理   已混淆

    implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:2.1.0")    //经典刷新头
    implementation("io.github.scwang90:refresh-footer-classics:2.1.0")    //经典加载


    implementation("com.github.jrfeng.snow:player:1.2")
    // qm-ui
    implementation("com.qmuiteam:qmui:2.1.0")
    implementation("com.qmuiteam:arch:2.1.0")
    implementation("com.qmuiteam:arch-compiler:2.1.0")

    implementation("com.github.xiaohaibin:XMarqueeView:2.0.6") //将latestVersion替换成上面 jitpack 后面的版本号
    implementation("com.github.Jay-Goo:RangeSeekBar:3.0.0")
    implementation("io.github.jeremyliao:live-event-bus-x:1.8.0")
    implementation("com.github.liangjingkanji:BRV:1.5.8")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.blankj:utilcodex:1.31.1")
    implementation("com.kongzue.dialogx:DialogX:0.0.49")
    implementation("com.tbuonomo:dotsindicator:5.0")

    implementation("com.github.jenly1314:zxing-lite:3.0.1")

    implementation("com.timqi.sectorprogressview:library:2.0.1")
    implementation("com.zjun:rule-view:0.0.1")
    implementation("com.github.superSp:RulerView:v1.5")
    implementation("com.contrarywind:Android-PickerView:4.1.9")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.android.exoplayer:exoplayer-core:2.10.5")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.10.5")

    implementation ("androidx.camera:camera-core:1.1.0-alpha10")
    implementation ("androidx.camera:camera-camera2:1.1.0-alpha10")
    implementation ("androidx.camera:camera-lifecycle:1.1.0-alpha10")
    implementation ("androidx.camera:camera-view:1.0.0-alpha23")
    implementation ("com.google.guava:guava:29.0-android")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("org.greenrobot:eventbus:3.3.1")//eventbus  已混淆



    //网络框架-------------------------------------------------start
    //导入RxJava 和 RxAndroid
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.3")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //导入retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    //转换器，请求结果转换成Model
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    //网络框架---------------------------------------------------end

//GroMore new begin
    val csjVersion = "7.4.2.1"

    val adnGdtVersion = "4.662.1532"
    val adnGdtVersionFix = ".0"

    val adnKsVersion = "4.11.20.1"
    val adnKsVersionFix = ".0"

    val adnBaiduVersion = "9.423"
    val adnBaiduVersionFix = ".3"

    val adnAdmobVersion = "17.2.0"
    val adnAdmobVersionFix = ".72"
//GroMore new end

    //dj----------------------------------------------------------------start
    implementation("com.github.li-xiaojun:XPopup:2.9.19") {
        exclude( group="com.github.bumptech.glide")
        exclude (group="com.davemorrissey.labs", module= "subsampling-scale-image-view-androidx")
    }
    implementation("com.umeng.umsdk:common:+")// (必选)版本号
    implementation("com.umeng.umsdk:asms:+") // asms包依赖(必选)
    implementation("com.umeng.umsdk:apm:+")// U-APM产品包依赖(必选)
    //判断是否虚拟机
    implementation("io.github.happylishang:antifake:1.5.0")
    // qm-ui
    implementation("com.github.jrfeng.snow:player:1.2")//不加这个qm-ui会报错重复依赖
    implementation("com.qmuiteam:qmui:2.1.0")
    implementation("com.qmuiteam:arch:2.1.0")
    implementation("com.qmuiteam:arch-compiler:2.1.0")
    // 吐司框架：https://github.com/getActivity/ToastUtils
    implementation("com.github.getActivity:ToastUtils:10.3")
    //dj----------------------------------------------------------------end


    //广告---------------------------------------------------------start

    // GroMore new begin

    implementation("androidx.annotation:annotation:1.1.0")
    implementation("com.pangle.cn:mediation-sdk:${csjVersion}")
//    implementation("com.pangle.cn:mediation-ks-adapter:${adnKsVersion}${adnKsVersionFix}")
    implementation(files("libs/mediation_ks_adapter_${adnKsVersion}${adnKsVersionFix}.aar"))
    implementation(files("libs/kssdk-ad-${adnKsVersion}.aar"))


    implementation("com.pangle.cn:mediation-baidu-adapter:${adnBaiduVersion}${adnBaiduVersionFix}")
//    implementation(name: "mediation_baidu_adapter_9.28.0", ext: 'aar')
    implementation(files("libs/Baidu_MobAds_SDK_v${adnBaiduVersion}.aar"))
//    implementation("com.pangle.cn:mediation-gdt-adapter:${adnGdtVersion}${adnGdtVersionFix}")
    implementation(files("libs/mediation_gdt_adapter_${adnGdtVersion}${adnGdtVersionFix}.aar"))
    implementation(files("libs/GDTSDK.unionNormal.${adnGdtVersion}.aar"))



    implementation("com.google.android.gms:play-services-ads:${adnAdmobVersion}") {
        exclude(group = "com.android.support")
    }
    implementation("com.pangle.cn:mediation-admob-adapter:${adnAdmobVersion}${adnAdmobVersionFix}")

    // GroMore new end
    implementation(files("libs/oaid_sdk_dj_1.0.25.aar"))
    implementation(files("libs/library-yl-utils-1.0.12.aar"))
    implementation("com.tencent.mm.opensdk:wechat-sdk-android:+")
    implementation("me.weishu:free_reflection:2.2.0")
    implementation("net.grandcentrix.tray:tray:0.12.0")
    implementation("com.github.JessYanCoding:AndroidAutoSize:v1.2.1")
    //广告---------------------------------------------------------end

    implementation("com.huawei.hms:ads-identifier:3.4.62.300")
    implementation("com.huawei.hms:ads-installreferrer:3.4.62.300")

    implementation("org.litepal.guolindev:core:3.2.3")

    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.bytedance.ads:AppConvert:2.0.0")
    implementation(files("libs/customer-1.0.0.5.aar"))


}
