package com.deendayalproject.util



import android.app.ProgressDialog
import android.content.Context

object ProgressDialogUtil {
    private var progressDialog: ProgressDialog? = null

    /**
     * Show Progress Dialog
     */
    fun showProgressDialog(context: Context, message: String = "Please wait...") {
        // Dismiss existing dialog if already showing
        dismissProgressDialog()

        progressDialog = ProgressDialog(context).apply {
            setMessage(message)
            setCancelable(false)
            show()
        }
    }

    /**
     * Dismiss Progress Dialog
     */
    fun dismissProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        progressDialog = null
    }
}
