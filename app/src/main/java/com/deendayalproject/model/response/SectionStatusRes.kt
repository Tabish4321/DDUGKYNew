package com.deendayalproject.model.response

data class SectionStatusRes(
    val responseCode: Int,
    val responseDesc: String,
    val wrappedList: List<SectionStatus>? = null,
    val requestDate: Long? = null,
    val url: String? = null,
    val errorMsg: String? = null
)

data class SectionStatus(
    val academicSection: Int,
    val commonEquipSection: Int,
    val supportInfraSection: Int,
    val wiringSection: Int,
    val generalDetailsSection: Int,
    val descOtherAreaSection: Int,
    val careraSection: Int,
    val toiletWashBasinSection: Int,
    val signageSection: Int,
    val infoSection: Int
)

