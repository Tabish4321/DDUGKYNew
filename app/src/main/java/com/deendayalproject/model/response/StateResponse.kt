package com.deendayalproject.model.response

data class StateResponse(val wrappedList: List<StateModel>?,
                         val responseCode: Int,
                         val responseDesc: String)
