package com.deendayalproject.model.request

import com.deendayalproject.model.request.assesmentInspection.InspectionStandardQuestionRequest

data class SaveInspectionStandardFormRequest(

    val appVersion: String,

    val inspectionId: Int,

    val questionsDetails: List<InspectionStandardQuestionRequest>
)