package com.deendayalproject.model.response

data class GetPrevDueQueListRes(
    val wrappedList: List<DueDiligenceQueListItem>,
    val responseCode: Int,
    val responseDesc: String
)


data class DueDiligenceQueListItem(
    val questionId: Int,
    val dueDiligenceRemark: String,
    val dueDiligenceBy: String
)