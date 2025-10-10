package com.deendayalproject.model.request

data class ReceptionAreaRoomDetailsRequest(
    var loginId: String = "",
    var imeiNo: String = "",
    var appVersion: String = "",
    var tcId: String = "",
    var sanctionOrder: String = "",
    var roomType: String = "",
    var roomLength:String = "",
    var roomWidth:String = "",
    var roomArea: String = "",
    var roomPhotograph: String = "",
    var roomPhotographAttachment: String = "",

)

