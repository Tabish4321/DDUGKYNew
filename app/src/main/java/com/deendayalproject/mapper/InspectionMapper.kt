package com.deendayalproject.mapper


import com.deendayalproject.model.response.*
import com.deendayalproject.model.uistate.CommonInspectionItem

fun mapToCommonList(data: InspectionFullDetails): List<CommonInspectionItem> {

    val list = mutableListOf<CommonInspectionItem>()

    data.DocumentsStandardFormsAvailabilitySaction?.forEach {
        list.add(
            CommonInspectionItem(
                sectionName = "DocumentsStandardFormsAvailability",
                question = it.question ?: "",
                questionId = it.questionId ?: 0,
                inspectionId = it.inspectionId ?: 0,
                previousRemark = it.remark
            )
        )
    }

    data.TrainingQualitySection?.forEach {
        list.add(
            CommonInspectionItem(
                sectionName = "TrainingQuality",
                subject = it.subjectType,
                question = it.question ?: "",
                questionId = it.questionId ?: 0,
                inspectionId = it.inspectionId ?: 0,
                previousRemark = it.remark
            )
        )
    }

    data.ValidateTrainerAttendanceSaction?.forEach {
        list.add(
            CommonInspectionItem(
                sectionName = "ValidateTrainerAttendance",
                trainerName = it.trainerName,
                trainerCode = it.trainerCode,
                question = it.question ?: "",
                questionId = it.questionId ?: 0,
                inspectionId = it.inspectionId ?: 0,
                previousRemark = it.remark
            )
        )
    }

    data.PreviousBatchDataVerificationSaction?.forEach {
        list.add(
            CommonInspectionItem(
                sectionName = "PreviousBatchDataVerification",
                candidateName = it.candidateName,
                candidateId = it.candidateId,
                batchId = it.batchId,
                question = it.question ?: "",
                questionId = it.questionId ?: 0,
                inspectionId = it.inspectionId ?: 0,
                previousRemark = it.remark
            )
        )
    }

    data.OngoingBatchCandidateSection?.forEach {
        list.add(
            CommonInspectionItem(
                sectionName = "OngoingBatchCandidateVerification",
                sectionType = it.sactionType,
                candidateName = it.candidateName,
                candidateId = it.candidateId,
                batchId = it.batchId,
                question = it.question ?: "",
                questionId = it.questionId ?: 0,
                inspectionId = it.inspectionId ?: 0,
                previousRemark = it.remark,
                baseImage = it.baseImageUrl
            )
        )
    }

    return list
}