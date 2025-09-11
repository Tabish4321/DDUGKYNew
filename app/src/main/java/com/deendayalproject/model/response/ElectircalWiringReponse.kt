package com.deendayalproject.model.response

data class ElectircalWiringReponse(

    val responseCode: Int,
    val responseDesc: String,
    val requestDate: Long? = null,// nullable for responses where it doesn't exist
    val url: String? = null,
    val errorMsg: String? = null

)
