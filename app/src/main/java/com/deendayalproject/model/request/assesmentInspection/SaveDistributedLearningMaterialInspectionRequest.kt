package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */
data class SaveDistributedLearningMaterialInspectionRequest(

    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val inspectionId: Int,

    val domainCurriculumQid: Int,
    val domainCurriculum: String,
    val domainCurriculumRemark: String,
    val domainCurriculumAttachment: String?,

    val bilingualTlmItSkillsQid: Int,
    val bilingualTlmItSkills: String,
    val bilingualTlmItSkillsRemark: String,
    val bilingualTlmItSkillsAttachment: String?,

    val bilingualTlmSoftSkillsQid: Int,
    val bilingualTlmSoftSkills: String,
    val bilingualTlmSoftSkillsRemark: String,
    val bilingualTlmSoftSkillsAttachment: String?,

    val bilingualTlmEnglishSkillsQid: Int,
    val bilingualTlmEnglishSkills: String,
    val bilingualTlmEnglishSkillsRemark: String,
    val bilingualTlmEnglishSkillsAttachment: String?,

    val trainingKitQid: Int,
    val trainingKit: String,
    val trainingKitRemark: String,
    val trainingKitAttachment: String?,

    val bilingualIDCardQid: Int,
    val bilingualIDCard: String,
    val bilingualIDCardRemark: String,
    val bilingualIDCardAttachment: String?,

    val practicalLearningProvidedQid: Int,
    val practicalLearningProvided: String,
    val practicalLearningProvidedRemark: String,
    val practicalLearningProvidedAttachment: String?,

    val usageLabEquipmentQid: Int,
    val usageLabEquipment: String,
    val usageLabEquipmentRemark: String,
    val usageLabEquipmentAttachment: String?,

    val tabletsUploadedQid: Int,
    val tabletsUploaded: String,
    val tabletsUploadedRemark: String,
    val tabletsUploadedAttachment: String?,

    val ipEnabledCameraFootageAssessmentsQid: Int,
    val ipEnabledCameraFootageAssessments: String,
    val ipEnabledCameraFootageAssessmentsRemark: String,
    val ipEnabledCameraFootageAssessmentsAttachment: String?
)