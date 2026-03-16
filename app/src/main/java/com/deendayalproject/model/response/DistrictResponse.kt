package com.deendayalproject.model.response

data class DistrictResponse(val wrappedList: ArrayList<DistrictModel>,
                            val responseCode: Int,
                            val responseDesc: String)
