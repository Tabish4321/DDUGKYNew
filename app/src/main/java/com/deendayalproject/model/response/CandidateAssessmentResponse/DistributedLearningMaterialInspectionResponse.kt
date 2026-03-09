package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */
data class DistributedLearningMaterialInspectionResponse(

    val inspectionId: Int?,
    val batchId: Int?,
    val candidateId: String?,

    val domainCurriculumQid: Int?,
    val domainCurriculum: String?,
    val domainCurriculumRemark: String?,

    val bilingualTlmItSkillsQid: Int?,
    val bilingualTlmItSkills: String?,
    val bilingualTlmItSkillsRemark: String?,

    val bilingualTlmSoftSkillsQid: Int?,
    val bilingualTlmSoftSkills: String?,
    val bilingualTlmSoftSkillsRemark: String?,

    val bilingualTlmEnglishSkillsQid: Int?,
    val bilingualTlmEnglishSkills: String?,
    val bilingualTlmEnglishSkillsRemark: String?,

    val trainingKitQid: Int?,
    val trainingKit: String?,
    val trainingKitRemark: String?,

    val bilingualIDCardQid: Int?,
    val bilingualIDCard: String?,
    val bilingualIDCardRemark: String?,

    val practicalLearningProvidedQid: Int?,
    val practicalLearningProvided: String?,
    val practicalLearningProvidedRemark: String?,

    val usageLabEquipmentQid: Int?,
    val usageLabEquipment: String?,
    val usageLabEquipmentRemark: String?,

    val tabletsUploadedQid: Int?,
    val tabletsUploaded: String?,
    val tabletsUploadedRemark: String?,

    val ipEnabledCameraFootageAssessmentsQid: Int?,
    val ipEnabledCameraFootageAssessments: String?,
    val ipEnabledCameraFootageAssessmentsRemark: String?,

    val domainCurriculumImage: String?,
    val bilingualTlmItSkillsImage: String?,
    val bilingualTlmSoftSkillsImage: String?,
    val bilingualTlmEnglishSkillsImage: String?,
    val trainingKitImage: String?,
    val bilingualIDCardImage: String?,
    val practicalLearningProvidedImage: String?,
    val usageLabEquipmentImage: String?,
    val tabletsUploadedImage: String?,
    val ipEnabledCameraFootageAssessmentsImage: String?
)