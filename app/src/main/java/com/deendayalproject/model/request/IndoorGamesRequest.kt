package com.deendayalproject.model.request

data class IndoorGamesRequest(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val facilityId: Int,
    val finalArray: List<IndoorGameItem>
)

data class IndoorGameItem(
    val indoreGameName: String,
    val indoreGameFile: String
)
