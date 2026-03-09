package com.deendayalproject.model.response

data class TrainerListInspectionRes(

    val trainerId: String = "",
    val name: String = "",
    val contactNumber: String = "",
    val designation: String = "",
    val status: String? = null,
    val isLoading: Boolean = false
)
