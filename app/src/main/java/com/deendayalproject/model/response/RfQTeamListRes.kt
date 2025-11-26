package com.deendayalproject.model.response

import com.deendayalproject.model.request.TrainingCenter

data class RfQTeamListRes(val wrappedList: List<RfCenter>?,
                          val responseCode: Int,
                          val responseDesc: String)




data class RfCenter(
    val trainingCenterId: Int,
    val senctionOrder: String,
    val trainingCenterAddress: String,
    val trainingCenterName: String,
    val districtName: String,
    val status: String,
    val facilityId: Int,
    val finalRfCapacity:String
)
