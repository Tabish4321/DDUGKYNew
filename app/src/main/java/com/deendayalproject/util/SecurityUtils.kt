package com.deendayalproject.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private fun isDeviceRooted(): Boolean {
    val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su"
    )
    return paths.any { java.io.File(it).exists() }
}

private fun isEmulator(): Boolean {
    return (
            Build.FINGERPRINT.startsWith("generic")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.lowercase().contains("emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT
            )
}


private fun isDeveloperOptionsEnabled(context: Context): Boolean {
    return try {
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
            0
        ) == 1
    } catch (e: Exception) {
        false
    }
}

private fun isUsbDebuggingEnabled(context: Context): Boolean {
    return try {
        Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.ADB_ENABLED,
            0
        ) == 1
    } catch (e: Exception) {
        false
    }
}




 fun validateDeviceSecurity(context: Context): Boolean {

    if (isEmulator()) {
        showSecurityDialog(
            "Login is not allowed on Emulator",
            context = context
        ) {
            (context as Activity).finishAffinity()
        }
        return false
    }

    if (isDeviceRooted()) {
        showSecurityDialog(
            "Login is not allowed on Rooted Device",
            context = context
        ) {
            (context as Activity).finishAffinity()
        }
        return false
    }

    if (isDeveloperOptionsEnabled(context)) {
        showSecurityDialog(
            "Disable Developer Options to continue",
            context = context
        ) {
            (context as Activity).finishAffinity()
        }
        return false
    }

    if (isUsbDebuggingEnabled(context)) {
        showSecurityDialog(
            "USB Debugging must be disabled",
            context = context
        ) {
            (context as Activity).finishAffinity()
        }
        return false
    }

    return true
}

private fun showSecurityDialog(
    message: String,
    titleText: String = "Alert",
    context: Context,
    onClick: () -> Unit
) {

    val styledTitle = SpannableString(titleText).apply {
        setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    val styledMsg = SpannableString(message).apply {
        setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    MaterialAlertDialogBuilder(
        context,
        com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog
    )
        .setTitle(styledTitle)
        .setMessage(styledMsg)
        .setCancelable(false)
        .setPositiveButton("OK") { _, _ -> onClick() }
        .show()
}

