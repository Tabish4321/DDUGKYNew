package com.deendayalproject.model.request

data class SectionReq(val appVersion: String,
                      val loginId: String,
                      val imeiNo: String,
                      val sanctionOrder: String,
                      val tcId: String,
                      val facilityId: String
    )
