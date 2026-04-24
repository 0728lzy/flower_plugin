import java.util.*
import kotlin.random.Random
import com.github.megatronking.stringfog.plugin.StringFogExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("android-junk-code")
    id("com.flower.protect-suite")
}
apply(plugin = "stringfog")

fun envFlag(name: String, defaultValue: Boolean = false): Boolean {
    return when (System.getenv(name)?.trim()?.lowercase(Locale.ROOT)) {
        null, "" -> defaultValue
        "1", "true", "yes", "y", "on" -> true
        "0", "false", "no", "n", "off" -> false
        else -> defaultValue
    }
}

protectSuite {
    flowerEnabled = envFlag("FLOWER_CODE_ENABLE", true)
    flowerEnableInDebug = false
    flowerEnableInRelease = true
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

    resChiperEnabled = true
    resChiperConfigFile = "tools/reschiper-config.xml"
    resChiperOutputBundleName = "app-release-obfuscated.aab"

    outputDir = "release"
    signingConfigName = "myConfig"
    dptEnabled = envFlag("DPT_ENABLE", true)
    dptJar = "tools/dpt.jar"
    dptExcludeAbi = System.getenv("DPT_EXCLUDE_ABI")?.takeIf { it.isNotBlank() } ?: "x86,x86_64"
    dptDebug = envFlag("DPT_DEBUG")
    dptDisableAcf = envFlag("DPT_DISABLE_ACF")
    dptDumpCode = envFlag("DPT_DUMP_CODE")
    dptNoisyLog = envFlag("DPT_NOISY_LOG")
    dptKeepClasses = envFlag("DPT_KEEP_CLASSES")
    dptSmaller = envFlag("DPT_SMALLER")
    dptVerifySign = envFlag("DPT_VERIFY_SIGN")
    dptRulesFile = System.getenv("DPT_RULES_FILE")?.takeIf { it.isNotBlank() }
    dptProtectConfig = System.getenv("DPT_PROTECT_CONFIG")?.takeIf { it.isNotBlank() }
}

extensions.configure<StringFogExtension>("stringfog") {
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    enable = true
    debug = false
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

tasks.configureEach {
    if (name == "preReleaseBuild") {
        dependsOn("generateObfuscationDict")
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
                packageCount = System.getenv("JUNK_PACKAGE_COUNT")?.toIntOrNull() ?: 45 //生成包数量
                activityCountPerPackage = System.getenv("JUNK_ACTIVITY_COUNT")?.toIntOrNull() ?: 45//每个包下生成Activity类数量
                excludeActivityJavaFile = false
                //是否排除生成Activity的Java文件,默认false(layout和写入AndroidManifest.xml还会执行)，主要用于处理类似神策全埋点编译过慢问题
                otherCountPerPackage = System.getenv("JUNK_OTHER_PER_COUNT")?.toIntOrNull() ?: 45 //每个包下生成其它类的数量
                methodCountPerClass =  System.getenv("JUNK_OTHER_PER_COUNT")?.toIntOrNull() ?: 45   //每个类下生成方法数量
                resPrefix = "lteg_"  //生成的layout、drawable、string等资源名前缀
                drawableCount = System.getenv("JUNK_DRAWABLE_COUNT")?.toIntOrNull() ?: 450  //生成drawable资源数量
                stringCount = System.getenv("JUNK_DRAWABLE_COUNT")?.toIntOrNull() ?: 450 //生成string数量
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
