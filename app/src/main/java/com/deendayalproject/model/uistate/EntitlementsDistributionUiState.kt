package com.deendayalproject.model.uistate

/**
 * Created by Rishi Porwal
 */
data class EntitlementsDistributionUiState(

    val trainingFree: String? = null,
    val trainingFreeRemark: String = "",

    val bankAccountOpened: String? = null,
    val bankAccountOpenedRemark: String = "",

    val entitlementsPaid: String? = null,
    val entitlementsPaidRemark: String = "",

    val receivedFreeTrainingMaterial: String? = null,
    val receivedFreeTrainingMaterialRemark: String = "",

    val uniformProvidedinFirstMonth: String? = null,
    val uniformProvidedinFirstMonthRemark: String = "",

    val padsMasksProvided: String? = null,
    val padsMasksProvidedRemark: String = "",

    val medicineProvided: String? = null,
    val medicineProvidedRemark: String = "",

    val insuranceBenefitsProvided: String? = null,
    val insuranceBenefitsProvidedRemark: String = "",

    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)