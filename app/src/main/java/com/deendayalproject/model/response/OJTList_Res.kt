package com.deendayalproject.model.response

data class OJTList_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OJTList>
)
data class OJTList(
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
    val mobileNo: String,
    val candidateName: String,
    val trainingCenterId: Int,
    val piaCode: String,
    val workplaceId: String,
    val employeersId: Int,
    val districtCode: String,
    val ojtPlanId: Int,
    val radius: Int,
    val sanctionOrder: String,
    val candidateImage: String,
    val lodgingFacility: String,
    val foodFacility: String,
    val stipend: String,
    val latitude: Double,
    val longitude: Double
)







