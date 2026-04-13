package com.deendayalproject.network


import android.app.DownloadManager
import android.content.BroadcastReceiver
import androidx.core.content.ContextCompat
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import java.io.File
import androidx.core.net.toUri

object AppUpdateManager {

    fun checkAndUpdate(context: Context) {
        showUpdateDialog(context)
    }

    private fun showUpdateDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("New Update Available")
            .setMessage("Please update to continue.")
            .setCancelable(false)
            .setPositiveButton("Update") { _, _ ->
                openPlayStore(context)
            }
            .show()
    }

    private fun openPlayStore(context: Context) {
        val packageName = context.packageName

        try {
            // Open Play Store app
            val intent = Intent(
                Intent.ACTION_VIEW,
                "market://details?id=$packageName".toUri()
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        } catch (e: Exception) {
            // Fallback to browser
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}