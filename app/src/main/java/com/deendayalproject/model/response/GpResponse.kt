package com.deendayalproject.model.response

data class GpResponse(val wrappedList: List<GpModel>?,
                      val responseCode: Int,
                      val responseDesc: String)
