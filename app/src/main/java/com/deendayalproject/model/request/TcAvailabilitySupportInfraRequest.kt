package com.deendayalproject.model.request

data class TcAvailabilitySupportInfraRequest(

val loginId: String,
val imeiNo: String,
val appVersion: String,
val tcId: String,
val sanctionOrder: String,
val drinkingWater: String,
val drinkingWaterImage: String,
val fireFightingEquipment: String,
val fireFightingEquipmentImage: String,
val firstAidKit: String,
val firstAidKitImage: String

)

