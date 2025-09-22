package com.deendayalproject.model.response


data class ToiletResponse(
    val wrappedList: List<WashroomItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class WashroomItem(
    val maleWashBasinImage: String,
    val femaleToiletImage: String,
    val maleToiletImage: String,
    val overheadTanks: String,
    val maleUrinalImage: String,
    val femaleWashBasin: Int,
    val femaleWashBasinImage: String,
    val femaleToiletSignageImage: String,
    val maleUrinal: Int,
    val flooringTypeImage: String,
    val flooringType: String,
    val maleToilet: Int,
    val femaleToilet: Int,
    val maleWashBasin: Int,
    val maleToiletSignageImage: String,
    val overheadTankImage: String
)

