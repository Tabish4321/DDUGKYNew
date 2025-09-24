package com.deendayalproject.model.response


data class GeneralDetails(
    val wrappedList: MutableList<SafetyItem>,
    val errorsMap: Map<String, String>?,
    val responseCode: Int,
    val responseDesc: String
)

data class SafetyItem(
    val stairsProtection: String,
    val signLeakageImage: String,
    val stairsProtectionImage: String,
    val ddugkyConfrence: String,
    val signLeakage: String,
    val centerSafty: String
)
