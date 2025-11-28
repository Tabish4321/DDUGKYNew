// ModernProgressDialog.kt
package com.deendayalproject.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.view.isVisible
import com.deendayalproject.R
import com.deendayalproject.databinding.DialogModernProgressBinding

object ModernProgressDialog {

    fun create(context: Context, message: String? = null, cancelable: Boolean = false): Dialog {
        return ModernProgressDialogImpl(context, message, cancelable)
    }

    private class ModernProgressDialogImpl(
        context: Context,
        private val message: String?,
        cancelable: Boolean
    ) : Dialog(context, R.style.Base_Theme_Ddugky) {

        private lateinit var binding: DialogModernProgressBinding

        init {
            setCancelable(cancelable)
            setCanceledOnTouchOutside(cancelable)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            binding = DialogModernProgressBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupDialog()
        }

        private fun setupDialog() {
            // Set message if provided
            message?.let {
                binding.tvMessage.text = it
                binding.tvMessage.isVisible = true
            } ?: run {
                binding.tvMessage.isVisible = false
            }

            // Setup window properties for blur effect
            window?.apply {
                setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                setBackgroundDrawableResource(android.R.color.transparent)

                // Add dim/blur effect
                setDimAmount(0.6f)
            }
        }

        override fun dismiss() {
            super.dismiss()
        }
    }
}