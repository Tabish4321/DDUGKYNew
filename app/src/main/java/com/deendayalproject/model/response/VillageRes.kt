package com.deendayalproject.model.response




data class VillageRes(
    val wrappedList: List<VillageModel>,
    val errorsMap: Map<String, Any>,
    val responseCode: Int,
    val responseDesc: String
)

data class VillageModel(
    val villageName: String,
    val villageCode: String
)
