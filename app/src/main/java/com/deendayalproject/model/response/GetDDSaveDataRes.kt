package com.deendayalproject.model.response

data class GetDDSaveDataRes(
    val wrappedList: List<GetPreDDSaveItem> = emptyList(),
    val responseCode: Int = 0,
    val responseDesc: String = ""
)


data class GetPreDDSaveItem(
    val questionId: Int = 0,
    val inspectorAnswer: String = "",
    val inspectorRemark: String = ""
)