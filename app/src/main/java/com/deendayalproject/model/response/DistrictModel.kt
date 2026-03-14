package com.deendayalproject.model.response

import com.google.gson.annotations.SerializedName

data class DistrictModel(val districtName: String, val districtCode: String,
                         @SerializedName("lgdDistrictCode;")
                         val lgdDistrictCode: String)
