package com.deendayalproject.security

import android.util.Log

/**
 * Created by Rishi Porwal
 */
object SecureConfig {


    init {
        try{
        System.loadLibrary("secure-keys")
        Log.d("SecureConfig", "✅ Native library loaded")
    } catch (e: Exception) {
        Log.e("SecureConfig", " Failed to load native library", e)
    }
    }

    // external JNI methods
    private external fun getEncryptIvKeyNative(): String



    // Kotlin getters

    val encryptIvKey: String by lazy { getEncryptIvKeyNative() }

}
