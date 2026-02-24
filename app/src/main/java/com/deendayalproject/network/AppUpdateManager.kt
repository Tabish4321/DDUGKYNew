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

object AppUpdateManager {

    private var downloadId: Long = -1
    private var apkFile: File? = null

    fun checkAndUpdate(context: Context, downloadUrl: String) {
        showUpdateDialog(context, downloadUrl)
    }

    private fun showUpdateDialog(context: Context, downloadUrl: String) {
        AlertDialog.Builder(context)
            .setTitle("New Update Available")
            .setMessage("Please update to continue.")
            .setCancelable(false)
            .setPositiveButton("DownLoad") { _, _ ->
                if (canInstall(context)) {
                    downloadApk(context, downloadUrl)
                } else {
                    requestInstallPermission(context)
                }
            }
            .show()
    }

    private fun downloadApk(context: Context, url: String) {

        apkFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            "update.apk"
        )

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Update")
            .setDescription("Please wait...")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE
            )
            .setDestinationUri(Uri.fromFile(apkFile))

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadId = manager.enqueue(request)

        registerReceiver(context)
    }

    private fun registerReceiver(context: Context) {

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {

                val id = intent?.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID,
                    -1
                )

                if (downloadId == id) {
                    context.unregisterReceiver(this)
                    installApk(context)
                }
            }
        }

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }


    private fun installApk(context: Context) {

        apkFile?.let { file ->

            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    uri,
                    "application/vnd.android.package-archive"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }

    fun canInstall(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }

    private fun requestInstallPermission(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val intent = Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${context.packageName}")
            )

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}