package com.deendayalproject.model.response

data class GetSavePrevInsQueRes(

    val wrappedList: List<PrevInspectionItem>,
    val responseCode: Int,
    val responseDesc: String

)

data class PrevInspectionItem(
    val questionId: Int,
    val previousInspectionRemark: String,
    val previousInspectionBy: String
)