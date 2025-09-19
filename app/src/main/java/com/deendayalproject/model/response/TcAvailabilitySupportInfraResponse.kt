package com.deendayalproject.model.response

data class TcAvailabilitySupportInfraResponse(

val responseCode: Int,
val responseDesc: String,
val requestDate: Long? = null,
val url: String? = null,
val errorMsg: String? = null,
val errors: Any? = null

// you can replace Any? with a specific error type if you have it

)


