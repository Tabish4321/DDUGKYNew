package com.deendayalproject.model.response

import com.deendayalproject.model.request.TrainingCenter

data class TrainingCenterResponse(  val wrappedList: List<TrainingCenter>?,
                                    val responseCode: Int,
                                    val responseDesc: String)
