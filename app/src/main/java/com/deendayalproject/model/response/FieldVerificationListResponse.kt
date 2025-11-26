package com.deendayalproject.model.response

data class FieldVerificationItem(
    val piaName: String?,
    val prnNo: String?,
    val address: String?,
    val districtName: String?,
    val stateName: String?,
    val captiveApplicationId: String?,
    val remarks: String?,
    val captiveEmpanelmentId: Int?
)

// Full response wrapper
data class FieldVerificationListResponse(
    val wrappedList: List<FieldVerificationItem> = emptyList(),
    val errorsMap: Map<String, Any>? = null,
    val responseCode: Int = 0,
    val responseDesc: String? = null
)