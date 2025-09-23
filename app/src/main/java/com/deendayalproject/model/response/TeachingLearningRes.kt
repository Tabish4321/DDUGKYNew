package com.deendayalproject.model.response


data class TeachingLearningRes(
    val wrappedList: MutableList<TrainingPlanItem>,
    val errorsMap: Map<String, String>?,
    val responseCode: Int,
    val responseDesc: String
)

data class TrainingPlanItem(
    val welcomeKit: String?,
    val trainingPlan: String?,
    val certifingAgencyName: String?,
    val trade: String?,
    val availableACLP: String?,
    val welcomeKitPdf: String,
    val domainCurriculum: String?,
    val trainingNature: String?,
    val assessmentMaterial: String?,
    val tradesAvailable: String?
)
