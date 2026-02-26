package com.deendayalproject.model.response

data class InspectionPreviousBatchRes(
    val wrappedList: List<PrevBatchItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)

data class PrevBatchItem(
    val batchId: Int?,
    val batchRegNo: String?,
    val batchName: String?
)