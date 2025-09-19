package com.deendayalproject.model.request

data class TcSignagesInfoBoardRequest(

    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val tcNameBoard: String,
    val tcNameBoardImage: String,
    val activityAchievmentBoard: String,
    val activityAchievmentBoardImage: String,
    val studentEntitlementResponsibilityBoard: String,
    val studentEntitlementResponsibilityBoardImage: String,
    val contactDetailImpPeople: String,
    val contactDetailImpPeopleImage: String,
    val basicInfoBoard: String,
    val basicInfoBoardImage: String,
    val codeConductBoard: String,
    val codeConductBoardImage: String,
    val studentAttendanceEntitlementBoard: String,
    val studentAttendanceEntitlementBoardImage: String
)

