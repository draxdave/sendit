# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/milan1/Documents/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
# google support compression algorithm
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

-dontwarn java.util.concurrent.**
-dontwarn com.google.android.gms.**
-dontwarn org.apache.**
-dontwarn com.squareup.okhttp.**
-dontwarn sun.misc.Unsafe
-dontwarn okio.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn retrofit2.**
-dontwarn com.google.ads.conversiontracking.**
-dontwarn org.mockito.**
-dontwarn org.junit.internal.**
-dontwarn org.junit.rules.**
-dontwarn android.test.**
-dontwarn javax.annotation.**

-keep public class com.google.android.gms.* { public *; }

-keep class com.google.android.gms.ads.identifier.** { *; }

-keepattributes SourceFile,LineNumberTable

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Add any classes the interact with gson
-keep class com.drax.sendit.data.model.** {*; }
-keep class com.drax.sendit.data.db.model.** {*; }
-keep class com.drax.sendit.data.service.models.** {*; }
-keep class com.drax.sendit.domain.network.model.** {*; }

-keep class com.google.android.gms.** { *; }
-keep class kotlinx.serialization.**{*;}

-dontwarn javax.**

# Branch
-dontwarn com.crashlytics.android.answers.shim.**
-dontwarn com.google.firebase.appindexing.**

# Retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keep class **.R
-keep class **.R$* {
    <fields>;
}

-keepclassmembers class * {
    @fully.qualified.package.AnnotationType *;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

#To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Remove all logs
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
    public static *** wtf(...);
}

# Proguard rules that are applied to your test apk/code.
-ignorewarnings
-keepattributes *Annotation*
-dontnote junit.framework.**
-dontnote junit.runner.**

#For FireBase
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.


# Keep the classes/members we need for client functionality.
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <methods>;
}

# Keep the classes/members we need for client functionality.
-keep @interface com.google.android.gms.common.annotation.KeepForSdk
-keep @com.google.android.gms.common.annotation.KeepForSdk class *
-keepclasseswithmembers class * {
  @com.google.android.gms.common.annotation.KeepForSdk <fields>;
}
-keepclasseswithmembers class * {
  @com.google.android.gms.common.annotation.KeepForSdk <methods>;
}

# Keep the public API
-keep @interface com.google.firebase.annotations.PublicApi
-keep @com.google.firebase.annotations.PublicApi class *
-keepclasseswithmembers class * {
  @com.google.firebase.annotations.PublicApi <fields>;
}
-keepclasseswithmembers class * {
  @com.google.firebase.annotations.PublicApi <methods>;
}

# Keep Enum members implicitly
-keepclassmembers @androidx.annotation.Keep public class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers @com.google.android.gms.common.annotation.KeepForSdk class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers @com.google.firebase.annotations.PublicApi class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Implicitly keep methods inside annotations
-keepclassmembers @interface * {
    public <methods>;
}

# Keep firebase
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# Keep the classes/members we need for client functionality.
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <methods>;
}

# Keep the classes/members we need for client functionality.
-keep @interface com.google.android.gms.common.annotation.KeepForSdk
-keep @com.google.android.gms.common.annotation.KeepForSdk class *
-keepclasseswithmembers class * {
  @com.google.android.gms.common.annotation.KeepForSdk <fields>;
}
-keepclasseswithmembers class * {
  @com.google.android.gms.common.annotation.KeepForSdk <methods>;
}

# Keep the public API
-keep @interface com.google.firebase.annotations.PublicApi
-keep @com.google.firebase.annotations.PublicApi class *
-keepclasseswithmembers class * {
  @com.google.firebase.annotations.PublicApi <fields>;
}
-keepclasseswithmembers class * {
  @com.google.firebase.annotations.PublicApi <methods>;
}

# Keep Enum members implicitly
-keepclassmembers @androidx.annotation.Keep public class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers @com.google.android.gms.common.annotation.KeepForSdk class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers @com.google.firebase.annotations.PublicApi class * extends java.lang.Enum {
    public <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Implicitly keep methods inside annotations
-keepclassmembers @interface * {
    public <methods>;
}

# Using names starting with "zz"
-classobfuscationdictionary obfuscate_dict.txt
-obfuscationdictionary obfuscate_dict.txt

# LeakCanary https://github.com/square/leakcanary/issues/2286
-keep class androidx.appcompat.view.WindowCallbackWrapper { *; }
-keep class android.support.v7.view.WindowCallbackWrapper { *; }

-keep class dagger.hilt.android.internal.managers.ApplicationComponentManager { *; }


-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE