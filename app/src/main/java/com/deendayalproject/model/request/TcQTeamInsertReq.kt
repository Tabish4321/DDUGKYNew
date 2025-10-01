package com.deendayalproject.model.request


data class TcQTeamInsertReq(
    val appVersion: String,
    val loginId: String,
    val imeiNo: String,
    val tcId: Int,
    val sanctionOrder: String,
    val tcInfoStatus: String,
    val tcInfoRemark: String,
    val tcInfraStatus: String,
    val tcInfraRemark: String,
    val tcAcademicStatus: String,
    val tcAcademicRemark: String,
    val tcToiletStatus: String,
    val tcToiletRemark: String,
    val tcDescOtherAreaStatus: String,
    val tcDescOtherAreaRemark: String,
    val tcLearningMaterialStatus: String,
    val tcLearningMaterialRemark: String,
    val tcGdStatus: String,
    val tcGdRemark: String,
    val tcEcWiringStatus: String,
    val tcEcWiringRemark: String,
    val tcSignageInfoStatus: String,
    val tcSignageInfoRemark: String,
    val tcIpEnableStatus: String,
    val tcIpEnableRemark: String,
    val tcCommonEquipmentStatus: String,
    val tcCommonEquipmentRemark: String,
    val tcSupportInfraStatus: String,
    val tcSupportInfraRemark: String,
    val tcStandardFormStatus: String,
    val tcStandardFormRemark: String
)

