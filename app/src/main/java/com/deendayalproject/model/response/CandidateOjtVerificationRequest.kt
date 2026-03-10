package com.deendayalproject.model.response

import com.google.gson.annotations.SerializedName

data class CandidateOjtVerificationRequest(

    @SerializedName("appVersion")
    val appVersion: String,

    @SerializedName("ojtPlanId")
    val ojtPlanId: String,

    @SerializedName("sanctionOrder")
    val sanctionOrder: String,

    @SerializedName("trainingCenterId")
    val trainingCenterId: String,

    @SerializedName("batchId")
    val batchId: String,

    @SerializedName("candidateId")
    val candidateId: String,

    @SerializedName("piaCode")
    val piaCode: String,

    @SerializedName("employeersId")
    val employeersId: String,

    @SerializedName("month")
    val month: String,

    @SerializedName("fatherName")
    val fatherName: String,

    @SerializedName("districtCode")
    val districtCode: String,

    @SerializedName("trainingStartDate")
    val trainingStartDate: String,

    @SerializedName("trainingEndDate")
    val trainingEndDate: String,

    @SerializedName("ojtStartDate")
    val ojtStartDate: String,

    @SerializedName("ojtEndDate")
    val ojtEndDate: String,

    @SerializedName("verificationDate")
    val verificationDate: String,

    @SerializedName("verificationTime")
    val verificationTime: String,

    @SerializedName("candidateAvailable")
    val candidateAvailable: String,

    @SerializedName("workPlaceId")
    val workPlaceId: String,

    @SerializedName("reason")
    val reason: String,

    @SerializedName("ojtStartByCandidate")
    val ojtStartByCandidate: String,

    @SerializedName("todayActivity")
    val todayActivity: String,

    @SerializedName("previousActivity")
    val previousActivity: String,

    @SerializedName("isFieldLevelSupervisorNominated")
    val isFieldLevelSupervisorNominated: String,

    @SerializedName("supervisorInteractionTimeCount")
    val supervisorInteractionTimeCount: String,

    @SerializedName("areYouGivenSufficientInstument")
    val areYouGivenSufficientInstument: String,

    @SerializedName("areYouGivenEnoughMaterials")
    val areYouGivenEnoughMaterials: String,

    @SerializedName("eligibleStipend")
    val eligibleStipend: String,

    @SerializedName("stipendGetting")
    val stipendGetting: String,

    @SerializedName("bordingAndLoadingFacilities")
    val bordingAndLoadingFacilities: String,

    @SerializedName("candidateRollNo")
    val candidateRollNo: String,

    @SerializedName("latitude")
    val latitude: Double?,

    @SerializedName("longitude")
    val longitude: Double?,

    @SerializedName("verificationImage")
    val verificationImage: String,

//    @SerializedName("verificationVideo")
//    val verificationVideo: String


)
