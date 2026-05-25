package com.deendayalproject.model.response



data class OjtSRLMRes(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<SRLMRes>
)

data class SRLMRes(
    val sanctionOrder: String,
    val prnNumber: String,
    val piaName: String
)

