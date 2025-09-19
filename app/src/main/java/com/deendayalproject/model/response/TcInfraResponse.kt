package com.deendayalproject.model.response

data class TcInfraResponse(
    val wrappedList: List<Building>,
    val responseCode: Int,
    val responseDesc: String
)

data class Building(
    val buildingPlan: String,
    val buildingOwner: String,
    val painting: String,
    val buildingRoof: String,
    val buildingWallImage: String,
    val buildingArea: String,
    val roofCeilingPhoto: String,
    val selfDeclaration: String
)
