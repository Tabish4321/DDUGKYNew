package com.deendayalproject.model.response


data class TrainingCenterInfoRes(
    val wrappedList: MutableList<DataItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class DataItem(
    val inchargeMailId: String,
    val tcAddress: String,
    val tcType: String,
    val distanceFromBusStand: String,
    val tcEmailID: String,
    val addressType: String,
    val latitude: String,
    val parliamentaryConstituency: String,
    val schemeName: String,
    val tcNature: String,
    val inchargeMobileNo: String,
    val projectState: String,
    val specialArea: String,
    val tcLandline: String,
    val tcMobileNo: String,
    val assemblyConstituency: String,
    val centerIncharge: String,
    val sanctionOrderNo: String,
    val distanceFromAutoStand: String,
    val centerName: String,
    val longitude: String
)
