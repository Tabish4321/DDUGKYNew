package com.deendayalproject.model.response

data class OngoingBatchRes(
    val wrappedList: List<OngoingBatchItem>?,
    val responseCode: Int?,
    val responseDesc: String?
)

data class OngoingBatchItem(
    val batchId: Int?,
    val batchRegNo: String?,
    val batchName: String?
)