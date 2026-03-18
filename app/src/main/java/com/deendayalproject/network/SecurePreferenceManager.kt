package com.deendayalproject.network

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePreferenceManager {

    private const val PREF_NAME = "app_preferences"
    private const val TOKEN_KEY = "token"

    private fun getEncryptedPrefs(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun saveToken(context: Context, token: String) {
        getEncryptedPrefs(context)
            .edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getToken(context: Context): String {
        return getEncryptedPrefs(context)
            .getString(TOKEN_KEY, "") ?: ""
    }

    fun clearToken(context: Context) {
        getEncryptedPrefs(context)
            .edit()
            .remove(TOKEN_KEY)
            .apply()
    }
}