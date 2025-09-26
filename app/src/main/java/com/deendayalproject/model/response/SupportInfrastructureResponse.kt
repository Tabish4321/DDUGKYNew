package com.deendayalproject.model.response

data class SupportInfrastructureResponse(
    val wrappedList: List<DrinkingWaterItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class DrinkingWaterItem(
    val drinkingWaterImage: String?,
    val fireFighterEquipImage: String?,
    val drinkingWater: String,
    val firstAidKit: String,
    val firstAidKitImage: String?,
    val fireFighterEquip: String
)
