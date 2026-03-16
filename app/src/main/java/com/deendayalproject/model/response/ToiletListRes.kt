package com.deendayalproject.model.response

data class ToiletListRes(
    val wrappedList: List<ToiletItem>,
    val errorsMap: Map<String, String>,
    val responseCode: Int,
    val responseDesc: String
)

data class ToiletItem(
    val rfToiletId: Int,
    val type: String
)

