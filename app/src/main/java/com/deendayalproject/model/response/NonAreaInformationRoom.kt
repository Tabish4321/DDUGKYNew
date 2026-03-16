package com.deendayalproject.model.response


data class NonAreaInformationRoom(
    val wrappedList: List<NonArea>,
    val responseCode: Int,
    val responseDesc: String
)

data class  NonArea(
    val receptionArea: String,
    val recreationLength: String,
    val kitchenLength: String,
    val kitchenWidth: String,
    val tvAvailable: String,
    val diningWidth: String,
    val kitchenArea: String,
    val recreationArea: String,
    val receptionAreaPdf: String,
    val washArea: String,
    val noOfSeats: String,
    val preparedFood: String,
    val separateAreas: String,
    val diningLength: String,
    val diningArea: String,
    val recreationWidth: String,
    val preprationFoodPdf: String,
    val diningRecreationArea: String,
    val diningRecreationLengh: String,
    val diningRecreationWidth: String,
    val diningRecreationAreaFile: String,
    val diningAreaFile: String
)
