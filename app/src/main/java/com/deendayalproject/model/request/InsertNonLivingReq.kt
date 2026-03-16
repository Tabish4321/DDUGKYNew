package com.deendayalproject.model.request

data class InsertNonLivingReq(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val facilityId: Int,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val preparedFood: String,
    val kitchenLength: Double,
    val kitchenWidth: Double,
    val kitchenArea: Double,
    val separateAreas: String,
    val noOfSeats: Int,
    val washArea: String,
    val tvAvailable: String,
    val diningLength: Double,
    val diningWidth: Double,
    val diningArea: Double,
    val recreationLength: Double,
    val recreationWidth: Double,
    val recreationArea: Double,
    val receptionArea: String,
    val preparedFoodFile: String,
    val receptionAreaFile: String,


    val diningRecreationLength: Double,
    val diningRecreationWidth: Double,
    val diningRecreationArea: Double,

    val diningRecreationAreaFile: String,
    val diningAreaFile: String,
    val recreationAreaFile: String



)
