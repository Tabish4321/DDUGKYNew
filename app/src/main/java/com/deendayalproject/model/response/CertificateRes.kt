package com.deendayalproject.model.response

data class CertificateRes(
    val wrappedList: List<Certificate>,
    val responseCode: Int,
    val responseDesc: String,
) {

    data class Certificate(
        val CertificateNumber: String,
        val CertificateExpire: String?
    )

}