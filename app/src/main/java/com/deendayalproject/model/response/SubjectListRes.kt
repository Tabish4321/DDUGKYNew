package com.deendayalproject.model.response


data class SubjectListRes(
    val wrappedList: List<SubjectListData>,
    val responseCode: Int,
    val responseDesc: String,
    val responseMsg: String?,
    val appCode: String?
)

data class SubjectListData(
    val subjectId: String,
    val subject: String
)
