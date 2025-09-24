package com.deendayalproject.model.request

data class ToiletDetailsRequest(
    val loginId: String,
    val imeiNo: String,
    val appVersion: String,
    val tcId: String,
    val sanctionOrder: String,
    val maleToilet: Int,
    val maleToiletProof: String,
    val maleToiletSignageProof: String,
    val femaleToilet: Int,
    val femaleToiletProof: String,
    val femaleToiletSignageProof: String,
    val maleUrinals: Int,
    val maleUrinalsImage: String,
    val maleWashBasin: Int,
    val maleWashBasinImage: String,
    val femaleWashBasin: Int,
    val femaleWashBasinImage: String,
    val overheadTanks: String,
    val overheadTanksImage: String,
    val flooringType: String,
    val flooringTypeImage: String
)

