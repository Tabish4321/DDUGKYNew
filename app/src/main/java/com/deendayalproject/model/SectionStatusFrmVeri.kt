package com.deendayalproject.model

import com.google.gson.annotations.SerializedName

data class SectionStatusFrmVeri(
    @SerializedName("section")
    val section: String? = null,
    @SerializedName("completed")
    val completed: Boolean = false,
)
