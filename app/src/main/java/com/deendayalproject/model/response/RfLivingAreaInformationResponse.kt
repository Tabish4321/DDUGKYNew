package com.deendayalproject.model.response

data class RfLivingAreaInformationResponse(  val wrappedList: MutableList<LivingAreaInformation>,
                                             val errorsMap: Map<String, String>?,
                                             val responseCode: Int,
                                             val responseDesc: String
)

data class LivingAreaInformation(
    val mattress: String?,
    val roomNumber: String?,
    val fanPdf: String?,
    val cot: String?,
    val storage: String?,
    val cotPdf: String?,
    val falseCeiling: String?,
    val airCondtion: String?,
    val trainingCenterId: String?,
    val airConditionPdf: String?,
    val ceilingHeightPdf: String?,
    val infoBoard: String?,
    val roofTypePdf: String?,
    val ceilingHeight: String?,
    val falseCeilingPdf: String?,
    val bedSheetPdf: String?,
    val lights: String?,
    val windowAreaPdf: String?,
    val area: String?,
    val mattressPdf: String?,
    val lightPdf: String?,
    val resFacilityId: String?,
    val areaPdf: String?,
    val roofType: String?,
    val windowArea: String?,
    val length: String?,
    val fans: String?,
    val bedSheet: String?,
    val width: String?,
    val sanctionOrder: String?,
    val storagePdf: String?
)

