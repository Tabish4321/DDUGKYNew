package com.deendayalproject.model.response

import com.deendayalproject.model.SectionStatusFrmVeri

data class TrainingDetailsResponse(
    val trainingCriteria: List<TrainingCriteriaItem>?,
    //val totalTrainingHoursRemarks: String?,
    val totalTrainingHoursRemarks: List<TotalTrainingHoursRemark>?,
    val repetitionClubbingRemarks: String?,
    val basicTraining: BasicTrainingResponse?,
    val commitment: CommitmentResponse?,
    val repetitionClubbingIfraNsqf : String?="",
    val trainingPlacement: TrainingPlacementResponse?,
    val domainSpecificTraining: DomainSpecificResponse?,

)



data class TrainingCriteriaItem(
    val year: String?,
    val target_allocated: Int?,
    val target_achieved: Int?,
    var completion_cert: String? = null,
    var completion_certificate : String?=null
)

data class BasicTrainingResponse(
    val selfDeclarationTrainingDoc: String?
)

data class CommitmentResponse(
    val form1: String?,
    val form2: String?
)

data class TrainingPlacementResponse(
    val tailorTrainingDoc: String?
)

data class DomainSpecificResponse(
    val form1: String?,
    val form2: String?
)

data class TotalTrainingHoursRemark(

    val year: String? = null,

    val trade_name: String? = null,

    val trade_duration: Int? = null,

    val commencement_certificate: String? = null
)

// --- wrapper for training rows ---
data class YearlyTrainingItem(
    val year: String?,
    val targetAllocated: Double?,
    val targetAchieved: Double?,
    val attachmentBase64: String?
)

// --- Mapper: default (your current TrainingCriteriaItem has no attachment field) ---
fun TrainingCriteriaItem.toYearlyTrainingItem(): YearlyTrainingItem {
    return YearlyTrainingItem(
        year = this.year,
        // convert Int? to Double? safely
        targetAllocated = this.target_allocated?.toDouble(),
        targetAchieved = this.target_achieved?.toDouble(),
        // Your current TrainingCriteriaItem doesn't include per-year attachment,
        // so we set null. If backend adds a field like `attachment`, replace here:
        // attachmentBase64 = this.attachment
        attachmentBase64 = null
    )
}