package com.deendayalproject.security

import android.util.Log

/**
 * Created by Rishi Porwal
 */

object SecurityUtils {

    // Key names constants
    object Keys {
        const val ENCRYPT_IV_KEY = "encrypt_iv_key"
    }

    fun getEncryptIvKey(): String = SecureConfig.encryptIvKey


    fun keyLogsTest() {
//        val TAG="Secure KEY Testing"
//
//        try {
//            Log.d(TAG, "=== SecureConfig Test - BEGIN ===")
//            Log.d(TAG, "ENCRYPT_IV_KEY (property): ${getEncryptIvKey()}")
//            Log.d(TAG, "ENCRYPT_KEY     (property): ${getEncryptKey()}")
//            Log.d(TAG, "CRYPT_LIB_AES   (property): ${getCryptLibAes()}")
//            Log.d(TAG, "CRYPT_ID        (property): ${getCryptId()}")
//            Log.d(TAG, "CRYPT_IV        (property): ${getCryptIv()}")
//
//            // Example using getSecureValue(keyName)
//            Log.d(TAG, "getSecureValue(ENCRYPT_IV_KEY): ${getSecureValue(Keys.ENCRYPT_IV_KEY)}")
//
//            Log.d(TAG, "=== SecureConfig Test - END ===")
//
//            println("SecureConfigTest logged to Logcat (tag=$TAG)")
//        } catch (t: Throwable) {
//            Log.e(TAG, "Error while logging secure values", t)
//        }
    }


}