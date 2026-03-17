package com.deendayalproject.model.response

data class PreviousInsQueRes(
    val wrappedList: List<PrevInspectionQueItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class PrevInspectionQueItem(
    val questionId: Int,
    val previousInspectionRemark: String,
    val previousInspectionBy: String
)