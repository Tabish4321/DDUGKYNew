package com.deendayalproject.model.request

data class CompliancesRFQTReq(val appVersion: String,
                              val loginId: String,
                              val imeiNo: String,
                              val facilityId: String,
                              val tcId: String,
                              val sanctionOrder: String)
