package com.deendayalproject.model.response

data class SignageInfo(
    val wrappedList: List<CenterInfoItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class CenterInfoItem(
    val tcNameImage: String?,
    val contactDetailsImage: String?,
    val codeConductImage: String?,
    val studentsAttendanceImage: String?,
    val studentEntitlement: String?,
    val contactDetails: String?,
    val basicInfo: String?,
    val basicInfoImage: String?,
    val tcName: String?,
    val studentEntitlementImage: String?,
    val studentsAttendance: String?,
    val codeConduct: String?,
    val activityAchivement: String?,
    val activityAchivementImage: String?
)

