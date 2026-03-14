package com.deendayalproject.model.response

data class LivingRoomListViewRes(
    val wrappedList: MutableList<LivingAreaInformationRes>,
    val responseCode: Int,
    val responseDesc: String
)


//data class LivingAreaInformationRes(
//    val area: String,
//    val livingRoomId: String,
//    val roomNo: String,
//    val length: String,
//    val width: String
//)

//data class LivingAreaInformationRes(
//    val area: String,
//    val livingRoomId: String,
//    val roomNo: String,
//    val length: String,
//    val width: String,
//    val cotPdf: String,
//    val falseCeiling: String,
//    val airCondtion: String,
//    val trainingCenterId: String,
//    val airConditionPdf: String,
//    val ceilingHeightPdf: String,
//    val infoBoard: String,
//    val roofTypePdf: String,
//    val infoBoardPdf: String,
//    val ceilingHeight: String,
//    val falseCeilingPdf: String,
//    val bedSheetPdf: String,
//    val lights: String,
//    val windowAreaPdf: String,
//    val area: String,
//    val mattressPdf: String,
//    val lightPdf: String,
//    val resFacilityId: String,
//    val areaPdf: String,
//    val roofType: String,
//    val windowArea: String,
//    val length: String,
//    val fans: String,
//    val bedSheet: String,
//    val width: String,
//    val sanctionOrder: String,
//    val storagePdf: String
//)
