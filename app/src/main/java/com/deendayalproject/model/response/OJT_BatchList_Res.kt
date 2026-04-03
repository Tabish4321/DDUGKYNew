package com.deendayalproject.model.response

data class OJT_BatchList_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OJTBatchList>
)

data class OJTBatchList(
    val batchId: Int,
    val batchName: String,
    val batchRegNo: String,
    val ojt: Int
)

