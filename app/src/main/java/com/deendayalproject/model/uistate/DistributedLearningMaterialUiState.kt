package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class DistributedLearningMaterialUiState(

    val inspectionId: Int? = null,
    val batchId: Int? = null,
    val candidateId: String? = null,

    val domainCurriculum: String? = null,
    val domainCurriculumRemark: String = "",
    val domainCurriculumImage: String? = null,

    val bilingualTlmItSkills: String? = null,
    val bilingualTlmItSkillsRemark: String = "",
    val bilingualTlmItSkillsImage: String? = null,

    val bilingualTlmSoftSkills: String? = null,
    val bilingualTlmSoftSkillsRemark: String = "",
    val bilingualTlmSoftSkillsImage: String? = null,

    val bilingualTlmEnglishSkills: String? = null,
    val bilingualTlmEnglishSkillsRemark: String = "",
    val bilingualTlmEnglishSkillsImage: String? = null,

    val trainingKit: String? = null,
    val trainingKitRemark: String = "",
    val trainingKitImage: String? = null,

    val bilingualIDCard: String? = null,
    val bilingualIDCardRemark: String = "",
    val bilingualIDCardImage: String? = null,

    val practicalLearningProvided: String? = null,
    val practicalLearningProvidedRemark: String = "",
    val practicalLearningProvidedImage: String? = null,

    val usageLabEquipment: String? = null,
    val usageLabEquipmentRemark: String = "",
    val usageLabEquipmentImage: String? = null,

    val tabletsUploaded: String? = null,
    val tabletsUploadedRemark: String = "",
    val tabletsUploadedImage: String? = null,

    val ipEnabledCameraFootageAssessments: String? = null,
    val ipEnabledCameraFootageAssessmentsRemark: String = "",
    val ipEnabledCameraFootageAssessmentsImage: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)