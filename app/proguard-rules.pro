# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**

-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}


-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

#2.默认保留区
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}


-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

#2.第三方包

#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.** { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#-dontwarn dalvik.**
#-dontwarn com.tencent.smtt.**
#
#-keep class com.tencent.smtt.** {
#    *;
#}
#
#-keep class com.tencent.tbs.** {
#    *;
#}

##  穿山甲原有的 开始
# pangle穿山甲 原有的
-keepclassmembers class * {
    *** getContext(...);
    *** getActivity(...);
    *** getResources(...);
    *** startActivity(...);
    *** startActivityForResult(...);
    *** registerReceiver(...);
    *** unregisterReceiver(...);
    *** query(...);
    *** getType(...);
    *** insert(...);
    *** delete(...);
    *** update(...);
    *** call(...);
    *** setResult(...);
    *** startService(...);
    *** stopService(...);
    *** bindService(...);
    *** unbindService(...);
    *** requestPermissions(...);
    *** getIdentifier(...);
   }

-keep class com.bytedance.pangle.** {*;}
-keep class com.bytedance.sdk.openadsdk.** { *; }
-keep class com.bytedance.frameworks.** { *; }

-keep class ms.bd.c.Pgl.**{*;}
-keep class com.bytedance.mobsec.metasec.ml.**{*;}

-keep class com.ss.android.**{*;}

-keep class com.bytedance.embedapplog.** {*;}
-keep class com.bytedance.embed_dr.** {*;}

-keep class com.bykv.vk.** {*;}


# applog混淆避免外部冲突
-keep public interface com.uodis.** { *; }

-keep public class com.bytedance.sdk.api.*.**{ *; }
-keep public class com.bytedance.sdk.api.**{ *; }
-keep public class com.bytedance.sdk.adapter.*.**{ *; }



# Mintegral 非AndroidX的混淆配置 15.7.27
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }

# baidu sdk
-ignorewarnings
-dontwarn com.baidu.mobads.sdk.api.**
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.baidu.mobads.** { *; }
-keep class com.style.widget.** {*;}
-keep class com.component.** {*;}
-keep class com.baidu.ad.magic.flute.** {*;}
-keep class com.baidu.mobstat.forbes.** {*;}

# 测试工具混淆
-keep class com.bytedance.mtesttools.api.** {
 public *;
}

# MSDK混淆
-keep class bykvm*.** { *; }
-keep class com.bytedance.msdk.adapter.**{ public *; }
-keep class com.bytedance.msdk.api.** {
 public *;
}

-keep class com.bytedance.msdk.base.TTBaseAd{*;}
-keep class com.bytedance.msdk.adapter.TTAbsAdLoaderAdapter{
    public *;
    protected <fields>;
}

 #ks 快手
-keep class org.chromium.** {*;}
-keep class org.chromium.** { *; }
-keep class aegon.chrome.** { *; }
-keep class com.kwai.**{ *; }
-dontwarn com.kwai.**
-dontwarn com.kwad.**
-dontwarn com.ksad.**
-dontwarn aegon.chrome.**


# Admob
-keep class com.google.android.gms.ads.MobileAds {
 public *;
}

#sigmob
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

-keep class sun.misc.Unsafe { *; }
-dontwarn com.sigmob.**
-keep class com.sigmob.**.**{*;}




#oaid 不同的版本混淆代码不太一致，你注意你接入的oaid版本
-dontwarn com.bun.**
-keep class com.bun.** {*;}
-keep class a.**{*;}
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class com.zui.opendeviceidlibrary.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}

#oaid
-keep class XI.CA.XI.**{*;}
-keep class XI.K0.XI.**{*;}
-keep class XI.XI.K0.**{*;}
-keep class XI.vs.K0.**{*;}
-keep class XI.xo.XI.XI.**{*;}
-keep class com.asus.msa.SupplementaryDID.**{*;}
-keep class com.asus.msa.sdid.**{*;}
-keep class com.bun.lib.**{*;}
-keep class com.bun.miitmdid.**{*;}
-keep class com.huawei.hms.ads.identifier.**{*;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class org.json.**{*;}
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}


#klevin 游可赢
-keep class com.tencent.tgpa.**{*;}
-keep class com.tencent.klevin.**{*;}


##  穿山甲原有的 结束

#Mintegral 不接入Mintegral sdk，可以不引入
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mbridge.** {*; }
-keep interface com.mbridge.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mbridge.**
-keep class **.R$* { public static final int mbridge*; }


#友盟
-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.cslt.maogoufanyi.bean.dj.NoProguard

-keep class com.cslt.maogoufanyi.bean.** { *; }

 #retrofit2  混淆
 -dontwarn javax.annotation.**
 -dontwarn javax.inject.**
 # OkHttp3
 -dontwarn okhttp3.logging.**
 -keep class okhttp3.internal.**{*;}
 -dontwarn okio.**
 # Retrofit
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions
 # RxJava RxAndroid
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {

     long producerIndex;
     long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {

     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {

     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 #----------- gson ----------------
 -keep class com.google.gson.** {*;}
 -keep class com.google.**{*;}
 -keep class sun.misc.Unsafe { *; }
 -keep class com.google.gson.stream.** { *; }
 -keep class com.google.gson.examples.android.model.** { *; }
 -keep class com.qiancheng.carsmangersystem.**{*;}
 # Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类
 -keep class com.powerqu.ryfree.bean.** { *; }

 ###热云针对移动智能终端补充设备标识体系统一调用SDK###
 -dontwarn org.bouncycastle.**
 -keep class org.bouncycastle.** {*;}
 -keep class XI.CA.XI.**{*;}
 -keep class XI.K0.XI.**{*;}
 -keep class XI.XI.K0.**{*;}
 -keep class XI.xo.XI.XI.**{*;}
 -keep class com.asus.msa.SupplementaryDID.**{*;}
 -keep class com.asus.msa.sdid.**{*;}
 -keep class com.bun.lib.**{*;}
 -keep class com.bun.miitmdid.**{*;}
 -keep class com.huawei.hms.ads.identifier.**{*;}
 -keep class com.samsung.android.deviceidservice.**{*;}
 -keep class com.zui.opendeviceidlibrary.**{*;}
 -keep class org.json.**{*;}
 -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
 -dontwarn com.reyun.tracking.**
 -keep class com.reyun.tracking.** {*;}

#   Dialogx
-keep class com.kongzue.dialogx.** { *; }
-dontwarn com.kongzue.dialogx.**

# 额外的，建议将 android.view 也列入 keep 范围：
-keep class android.view.** { *; }

# 若启用模糊效果，请增加如下配置：
-dontwarn androidx.renderscript.**
-keep public class androidx.renderscript.** { *; }


-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class net.grandcentrix.tray.TrayPreferences.**{*;}


#友盟
-keep class com.umeng.** {*;}
-keepclassmembers class *{
     public<init>(org.json.JSONObject);
}
-keepclassmembers enum *{
      publicstatic**[] values();
      publicstatic** valueOf(java.lang.String);
}



#优量汇竞价
-keep class com.gdt.bidding.adapter.**{*;}



#banner
-keep class * extends androidx.fragment.app.Fragment {
    public void setUserVisibleHint(boolean);
    public void onHiddenChanged(boolean);
    public void onResume();
    public void onPause();
}
-keep public class * extends android.app.Activity
-keep class com.example.zhpan.banner.bean.** { *; }
-keep class com.example.zhpan.banner.net.** { *; }
-keep class com.zhpan.idea.** { *; }
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


# For BannerViewPager
-keep class androidx.recyclerview.widget.**{*;}
-keep class androidx.viewpager2.widget.**{*;}

-printmapping ..//outputs/mapping.txt

-obfuscationdictionary bt-proguard.txt
-classobfuscationdictionary bt-proguard.txt
-packageobfuscationdictionary bt-proguard.txt



#微信混淆
-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}

#鲸鸿动能
-keep class com.huawei.hms.ads.** {*; }
-keep interface com.huawei.hms.ads.** {*; }

 #解决Missing type parameter. 问题
-dontobfuscate
-dontoptimize
-keep class org.json.** {*;}
-dontshrink
#融合归因
-keep class com.bytedance.ads.convert.broadcast.common.EncryptionTools {*;}































