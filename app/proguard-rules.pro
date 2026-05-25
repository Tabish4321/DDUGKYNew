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


# BouncyCastle core classes
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**
-dontwarn org.xmlpull.**
-dontwarn org.xmlpull.v1.**
-dontwarn org.kxml2.io.**
-dontwarn android.content.res.**
-dontwarn org.slf4j.impl.StaticLoggerBinder


-keep class org.xmlpull.** { *; }
-keepclassmembers class org.xmlpull.** { *; }

-dontwarn aQute.**
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn com.ctc.wstx.**
-dontwarn org.codehaus.stax2.**
-dontwarn aQute.bnd.annotation.spi.**

# Keep StAX & Woodstox service classes
-keep class com.fasterxml.** { *; }
-keep class org.codehaus.stax2.** { *; }
-keep class com.ctc.wstx.** { *; }
-dontwarn javax.xml.stream.**

-keep class com.fasterxml.jackson.** { *; }
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.* <fields>;
    @com.fasterxml.jackson.annotation.* <methods>;
}
-dontwarn java.beans.**
##########################################
# Keep Jackson core/databind classes
##########################################
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.**

##########################################
# Keep XML module (jackson-dataformat-xml)
##########################################
-keep class com.fasterxml.jackson.dataformat.xml.** { *; }
-dontwarn com.fasterxml.jackson.dataformat.xml.**

##########################################
# Keep annotations for Jackson
##########################################
-keepattributes Annotation

##########################################
# Keep Java Beans used by Jackson
##########################################
-keep class java.beans.** { *; }
-dontwarn java.beans.**



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
-keep class com.deendayalproject.uidai.** { *; }
-keep class com.deendayalproject.base.BaseResponse { *; }




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



#-------------Xstream------------
-dontwarn java.awt.**
-dontwarn javax.swing.**
-dontwarn com.thoughtworks.xstream.converters.extended.**
-dontwarn aQute.bnd.annotation.spi.**

# Keep XStream classes
-keep class com.thoughtworks.xstream.** { *; }
-dontwarn com.thoughtworks.xstream.**

# Keep Properties (needed for .properties file loading)
-keep class java.util.Properties { *; }
# Keep Java Security / Certificates (.cer, .p12, keystore)
-keep class java.security.** { *; }
-keep class javax.security.** { *; }
-keep class javax.crypto.** { *; }
-keep class java.security.cert.** { *; }
-keep class java.security.KeyStore { *; }


# Some security providers use reflection
-keep class sun.security.** { *; }

# If Apache XML Security / BouncyCastle are used
-keep class org.apache.xml.security.** { *; }
-dontwarn org.apache.xml.security.**

# --- Apache XML Digital Signature (JSR 105 RI) ---
-keep class org.apache.jcp.xml.dsig.internal.dom.** { *; }
-dontwarn org.apache.jcp.xml.dsig.internal.dom.**

# --- Javax XML Crypto (used via reflection) ---
-keep class javax.xml.crypto.** { *; }
-dontwarn javax.xml.crypto.**

# --- Keep KeyStore / crypto r eflection ---
-keep class java.security.** { *; }
-keepclassmembers class java.security.** { *; }
-dontwarn java.security.**

# --- Keep DOM / Transformer (used in signXML) ---
-keep class org.w3c.dom.** { *; }
-keep class javax.xml.parsers.** { *; }
-keep class javax.xml.transform.** { *; }

# ---------- Debug Info ----------
-keepattributes SourceFile,LineNumberTable
