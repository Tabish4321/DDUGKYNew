package com.deendayalproject.model.request

data class OngoingBatchReq(

    val appVersion: String,
    val trainingCenterId: String,
    val sanctionOrder: String

)
