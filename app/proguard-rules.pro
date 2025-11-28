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

-keepattributes *Annotation*

-keep class org.simpleframework.xml.** { *; }
-dontwarn org.simpleframework.xml.**


-keep class com.deendayalproject.model.request.**{*;}
-keep class com.deendayalproject.model.response.**{*;}
-keep class com.deendayalproject.model.IndoorGame
-keepclassmembers class com.deendayalproject.model.IndoorGame {
    *;
}
-keep class com.deendayalproject.model.LoginErrorResponse
-keepclassmembers class com.deendayalproject.model.LoginErrorResponse {
    *;
}
-keep class com.deendayalproject.model.SectionHandler
-keepclassmembers class com.deendayalproject.model.SectionHandler {
    *;
}
-keep class com.deendayalproject.model.TrainingCentre
-keepclassmembers class com.deendayalproject.model.TrainingCentre {
    *;
}



-keepclassmembers class * {
    @org.simpleframework.xml.* <fields>;
    @org.simpleframework.xml.* <methods>;
}


# Keep hilt generated code
-keep class dagger.hilt.** { *; }
-keep interface dagger.hilt.** { *; }
-keep class dagger.** { *; }
-keep interface dagger.** { *; }
-keep class javax.inject.** { *; }
-keep interface javax.inject.** { *; }
-dontwarn dagger.hilt.**
-dontwarn javax.inject.**

# Keep Dagger modules (like AppModule)
-keep @dagger.Module class * { *; }
-keepclasseswithmembers class * {
    @dagger.Provides <methods>;
}

# Retrofit
-keepattributes Signature, Exceptions, InnerClasses, *Annotation*
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keep interface retrofit2.http.* { *; }

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

#Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

#Moshi
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

-keepclassmembers class com.deendayalproject.network.TokenInterceptor {
    public <methods>;
}

-keep class ** extends android.app.Application { *; }