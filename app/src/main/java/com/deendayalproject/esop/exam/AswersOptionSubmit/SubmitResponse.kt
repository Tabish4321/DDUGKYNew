package com.example.esop.AswersOptionSubmit

import com.example.esop.AswersOptionSubmit.WrappedListaItem

data class SubmitResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedLista: List<WrappedListaItem>
)
