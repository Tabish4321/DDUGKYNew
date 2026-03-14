package com.deendayalproject.model.response

data class TrainerListRes(
    val wrappedList: List<TrainerData>,
    val responseCode: Int,
    val responseDesc: String
)

data class TrainerData(
    val trainerCode: Int,
    val trainerName: String,
    val trainerId: String,
    val trainerDesignation: String,
    val contactNumber: String
)