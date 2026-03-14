package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */
data class EntitlementsDistributionInspectionResponse(

    val inspectionId: Int?,
    val batchId: Int?,
    val candidateId: String?,

    val trainingFreeQid: Int?,
    val trainingFree: String?,
    val trainingFreeRemark: String?,

    val bankAccountOpenedQid: Int?,
    val bankAccountOpened: String?,
    val bankAccountOpenedRemark: String?,

    val entitlementsPaidQid: Int?,
    val entitlementsPaid: String?,
    val entitlementsPaidRemark: String?,

    val receivedFreeTrainingMaterialQid: Int?,
    val receivedFreeTrainingMaterial: String?,
    val receivedFreeTrainingMaterialRemark: String?,

    val uniformProvidedinFirstMonthQid: Int?,
    val uniformProvidedinFirstMonth: String?,
    val uniformProvidedinFirstMonthRemark: String?,

    val padsMasksProvidedQid: Int?,
    val padsMasksProvided: String?,
    val padsMasksProvidedRemark: String?,

    val medicineProvidedQid: Int?,
    val medicineProvided: String?,
    val medicineProvidedRemark: String?,

    val insuranceBenefitsProvidedQid: Int?,
    val insuranceBenefitsProvided: String?,
    val insuranceBenefitsProvidedRemark: String?
)