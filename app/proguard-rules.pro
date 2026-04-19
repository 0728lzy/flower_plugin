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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-printmapping ..//outputs/mapping.txt

-obfuscationdictionary obf-dict.txt
-classobfuscationdictionary obf-dict.txt
-packageobfuscationdictionary obf-dict.txt

# 自己代码允许混淆
-keep,allowobfuscation class com.cslianta.catdog.** { *; }

# 特殊不混淆类
-keep class com.cslianta.catdog.utils.lzy.ScreenUtils
-keep class com.cslianta.catdog.csj.UISimpleUtils
-keep class com.cslianta.catdog.bean.** {*;}
-keep class com.cslianta.catdog.event.** {*;}
-keep class com.cslianta.catdog.network.** {*;}
-keep class com.cslianta.catdog.entity.** {*;}
-keep class com.cslianta.catdog.model.** {*;}

-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.huawei.hms.ads.** {*; }
-keep interface com.huawei.hms.ads.** {*; }

-keep class com.zym.customer.bean.** {*;}

-keep class **.*Binding {*;}
-keep class **.*BindingImpl {*;}
# 保留第三方库
-keep class !com.cslianta.catdog.** { *; }
# 忽略警告
-dontwarn **

-overloadaggressively
-flattenpackagehierarchy a
-repackageclasses a