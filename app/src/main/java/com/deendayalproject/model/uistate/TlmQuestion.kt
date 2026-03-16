package com.deendayalproject.model.uistate

import android.graphics.Bitmap

data class TlmQuestion(

    val question: String,
    val answer: String? = null,
    val remarks: String = "",
    val imageBitmap: Bitmap? = null,
    val imageBase64: String? = null
)