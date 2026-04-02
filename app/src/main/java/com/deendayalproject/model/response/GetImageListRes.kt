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
    val disablityCertPath: String?,
    val isRationCard: String?,
    val isRsby: String?,
    val isPmayg: String?,
    val isMinority: String?,
    val isPip: String?,
    val isCastCategory: String?,
    val isSHG: String?,
    val isDisablity: String?,
    val isNrega: String?
)