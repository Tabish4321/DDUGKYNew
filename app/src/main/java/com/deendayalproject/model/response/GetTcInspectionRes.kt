package com.deendayalproject.model.response



data class GetTcInspectionRes(
    val wrappedList: List<InspectionTcDetails>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String,
    val facilityId: Int,
    val resultImage: String?
)


data class  InspectionTcDetails(
    val trainingCenterId: Int,
    val trainingCenterName: String,
    val prnRegistrationNo: String,
    val piaName: String,
    val sanctionOrder: String,
    val inspectionType: String,
    val inspectionId: String
)


