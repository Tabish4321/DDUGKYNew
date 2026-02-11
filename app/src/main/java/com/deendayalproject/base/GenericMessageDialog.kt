package com.deendayalproject.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import com.deendayalproject.R
import com.deendayalproject.databinding.ErrorDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Created by Rishi Porwal
 */
object GenericMessageDialog {


    fun show(
        context: Context,
        title: String = "Message",
        message: String
    ) {
        if (context is Activity && context.isFinishing) return

        val binding =
            ErrorDialogBinding.inflate(LayoutInflater.from(context))

        val dialog = MaterialAlertDialogBuilder(
            context,
            R.style.FullWidthMaterialDialog
        )
            .setView(binding.root)
            .setCancelable(true)
            .create()

        binding.tvTitle.text = title
        binding.tvMessage.text = message

        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

