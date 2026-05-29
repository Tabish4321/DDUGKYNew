package com.deendayalproject.model.response

data class OjtListChildSRLMRes(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<ChildSRLM>
)

data class ChildSRLM(
    val candidateId: String,
    val ojtStartDate: String,
    val ojtEndDate: String,
    val batchStartDate: String,
    val batchEndDate: String,
    val rollNo: Int,
    val trainingCenterName: String,
    val piaName: String,
    val fatherName: String,
    val districtName: String,
    val status: String,
    val workplaceName: String,
    val employeersName: String,
    val candidateName: String,
    val trainingCenterId: Int,
    val piaCode: String,
    val workplaceId: String,
    val employeersId: String,
    val districtCode: String,
    val ojtPlanId: String,
    val sanctionOrder: String,
    val latitude: String,
    val longitude: String,
    val radius: String,
    val stipend: String,
    val lodgingFacility: String,
    val foodFacility: String,
    val mobileNo: String,
    val ojtPlanVerificationId: String,
    val candidateImage: String,
)


