package com.example.esop.AswersOptionSubmit

data class SubmitResponse(

    val responseCode: Int,

    val responseDesc: String,

    val wrappedLista: List<WrappedListaItem>
)
