import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("android-junk-code")
}

android {
//    namespace = "com.ruite.app.pet.translator"
    namespace = "com.ruiteapp.pettranslator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ruite.app.pet.translator.zz"
        minSdk = 24
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0.2"

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
        register("myConfig") {
            keyAlias = "pettranslatorzz"
            keyPassword = "pettranslatorzz123"
            storePassword = "pettranslatorzz123"
            storeFile = file("../sign/pettranslatorzz.jks")
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
                packageBase = "com.ruiteapp.pettranslator"  //生成java类根包名
                packageCount = 70 //生成包数量
                activityCountPerPackage = 50//每个包下生成Activity类数量
                excludeActivityJavaFile = false
                //是否排除生成Activity的Java文件,默认false(layout和写入AndroidManifest.xml还会执行)，主要用于处理类似神策全埋点编译过慢问题
                otherCountPerPackage = 90  //每个包下生成其它类的数量
                methodCountPerClass = 80  //每个类下生成方法数量
                resPrefix = "zz_"  //生成的layout、drawable、string等资源名前缀
                drawableCount = 650  //生成drawable资源数量
                stringCount = 650  //生成string数量
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
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val time = SimpleDateFormat("YYYYMMddHHmm").format(Date())
                val appName = "translator"
                val filename = "${appName}-${time}.apk"
                output.outputFileName = filename
            }
    }
}

dependencies {

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

    val csjVersion = "6.5.0.2"


    val adnGdtVersion = "4.603.1473"
    val adnGdtVersionFix = ".1"


    val adnKsVersion = "3.3.69"
    val adnKsVersionFix = ".1"

    val adnBaiduVersion = "9.37"
    val adnBaiduVersionFix = ".1"

    val adnAdmobVersion = "17.2.0"
    val adnAdmobVersionFix = ".63"
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
    implementation(files("libs/mediation_ks_adapter_${adnKsVersion}${adnKsVersionFix}.aar"))
    implementation(files("libs/kssdk-ad-${adnKsVersion}.aar"))
    implementation("com.pangle.cn:mediation-baidu-adapter:${adnBaiduVersion}${adnBaiduVersionFix}")
    implementation(files("libs/Baidu_MobAds_SDK_v${adnBaiduVersion}.aar"))
    implementation(files("libs/mediation_gdt_adapter_${adnGdtVersion}${adnGdtVersionFix}.aar"))
    implementation(files("libs/GDTSDK.unionNormal.${adnGdtVersion}.aar"))
    implementation("com.google.android.gms:play-services-ads:${adnAdmobVersion}") {
        exclude(group = "com.android.support")
    }
    implementation("com.pangle.cn:mediation-admob-adapter:${adnAdmobVersion}${adnAdmobVersionFix}")
    // GroMore new end

    implementation(files("libs/library-yl-utils-1.0.6.aar"))
    implementation("com.tencent.mm.opensdk:wechat-sdk-android:+")
    implementation("me.weishu:free_reflection:2.2.0")
    implementation("net.grandcentrix.tray:tray:0.12.0")
    implementation("com.github.JessYanCoding:AndroidAutoSize:v1.2.1")
    //广告---------------------------------------------------------end

    implementation("com.huawei.hms:ads-identifier:3.4.62.300")
    implementation("com.huawei.hms:ads-installreferrer:3.4.62.300")
}