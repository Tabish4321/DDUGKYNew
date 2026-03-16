package com.deendayalproject.model.response

data class SectionResponse(
    val wrappedList: List<SectionRFData>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String
)

data class SectionRFData(
    val livingAreaInfoSection: Int,
    val toiletSection: Int,
    val toiletAdditionalSection: Int,
    val basiInfoSection: Int,
    val infraDtlComplianceSection: Int,
    val rfAvailableSection: Int,
    val nonLivingAreaSection: Int,
    val indoorGameDtlSection: Int,
    val supportFacltySection: Int
)
