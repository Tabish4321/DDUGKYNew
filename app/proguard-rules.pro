# ---------- Attributes ----------
-keepattributes *Annotation*, Signature, Exceptions, InnerClasses, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations


# ---------- Kotlin ----------
-keep class kotlin.** { *; }
-dontwarn kotlin.**
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**


# ---------- ViewModel ----------
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}


# ---------- Hilt / Dagger ----------
-keep class dagger.hilt.internal.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep @dagger.Module class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keepclassmembers class * {
    @javax.inject.Inject <fields>;
    @dagger.Provides <methods>;
}

# ---------- Retrofit / OkHttp ----------
-keep class retrofit2.** { *; }
-keep interface retrofit2.http.* { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ---------- Gson / Moshi ----------
-keep class com.google.gson.** { *; }
-keep class com.squareup.moshi.** { *; }

# ---------- Simple XML ----------
-keep class org.simpleframework.xml.** { *; }

# ---------- Models ----------
-keep class com.deendayalproject.model.** { *; }

# ---------- Parcelable ----------
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

# ---------- Navigation ----------
-keep class * extends androidx.navigation.NavArgs { *; }
-keep class * extends androidx.navigation.NavDirections { *; }

# ---------- ViewBinding ----------
-keep class * implements androidx.viewbinding.ViewBinding { *; }

# ---------- Resources ----------
-keepclassmembers class **.R$* {
    public static <fields>;
}

# ---------- Debug Info ----------
-keepattributes SourceFile,LineNumberTable
