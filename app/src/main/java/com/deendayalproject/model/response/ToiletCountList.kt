package com.deendayalproject.model.response

data class ToiletCountList(  val wrappedList: MutableList<CountList>,
                                      val responseCode: Int,
                                      val responseDesc: String
)

data class CountList(
    val toiletWashroomCount: String?,
    val toiletCount: String?,
    val washroomCount: String?,
)
