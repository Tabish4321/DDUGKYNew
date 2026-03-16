package com.deendayalproject.model.request

data class ToiletCountListReq(val appVersion: String,
                              val facilityId: String,
                              val tcId: Int,
                              val sanctionOrder: String)

