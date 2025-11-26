package com.deendayalproject.model.request

data class FieldVerificationListRequest(val appVersion: String,
                                 val loginId: String,
                                 val imeiNo: String)