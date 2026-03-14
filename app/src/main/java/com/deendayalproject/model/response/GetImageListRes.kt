package com.deendayalproject.model.response

data class GetImageListRes(
    val wrappedList: List<CandidateProofItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)

data class CandidateProofItem(
    val pmaygAttachment: String?,
    val shgImage: String?,
    val rsbyCardPath: String?,
    val categoryCertPath: String?,
    val minorityCertPath: String?,
    val rationCardPath: String?,
    val naregaCardPath: String?,
    val pipCert: String?,
    val disablityCertPath: String?
)