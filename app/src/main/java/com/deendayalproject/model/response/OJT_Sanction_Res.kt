package com.deendayalproject.model.response

data class OJT_Sanction_Res(

    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<OJTSanctionOrderNumber>
)

data class OJTSanctionOrderNumber(
    val sanctionOrder: String,
)

