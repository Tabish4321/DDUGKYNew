package com.deendayalproject.model.response

data class DistrictResponse(val wrappedList: List<DistrictModel>?,
                            val responseCode: Int,
                            val responseDesc: String)
