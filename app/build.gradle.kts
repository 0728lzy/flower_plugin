import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.ruite.app.pet.translator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ruite.app.pet.translator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        ndk {
            abiFilters += listOf("arm64-v8a")
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
        register("config") {
            keyAlias = "gjonemap"
            keyPassword = "123456"
            storePassword = "123456"
            storeFile = file("../gjonemap.jks")
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("config")
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("config")
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
    implementation("com.github.getActivity:XXPermissions:18.5")


    implementation("io.github.scwang90:refresh-layout-kernel:2.1.0")      //核心必须依赖
    implementation("io.github.scwang90:refresh-header-classics:2.1.0")    //经典刷新头
    // implementation  ("io.github.scwang90:refresh-header-radar:2.1.0")       //雷达刷新头
    // implementation  ("io.github.scwang90:refresh-header-falsify:2.1.0")     //虚拟刷新头
    // implementation  ("io.github.scwang90:refresh-header-material:2.1.0")    //谷歌刷新头
    // implementation  ("io.github.scwang90:refresh-header-two-level:2.1.0")   //二级刷新头
    // implementation  ("io.github.scwang90:refresh-footer-ball:2.1.0")        //球脉冲加载
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
}