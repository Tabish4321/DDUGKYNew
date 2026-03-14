package com.deendayalproject.model.response


data class ModifyRFRes(
    val wrappedList: List<RFModifyListItem>?,
    val errorsMap: Map<String, String>?,
    val responseCode: Int,
    val responseDesc: String
)
data class RFModifyListItem(
    val trainingCenterId: Int?,
    val senctionOrder: String?,
    val facilityId: Int?,
    val schemeName: String?,
    val trainingCenterName: String?,
    val remarks: String?,
    val status: String?
): java.io.Serializable
