package com.deendayalproject.model.request

import com.deendayalproject.model.response.RemarkItem

data class FieldVerificationDetailRequest(

    val loginId: String,
    val appVersion: String,
    val captiveEmpanelmentId: String,
    val prnNo: String
)


//    data class FieldVerificationFinalSubmit(
//        val appVersion: String,
//        val loginId: String,
//        val captiveEmpanelmentId: String,
//        val prnNo: String,
//        val remarks:Map<String, List<RemarkItem>> //List<RemarkItem> // All remarks flattened
//    )

data class FieldVerificationFinalSubmit(
    val appVersion: String,
    val loginId: String,
    val captiveEmpanelmentId: String,
    val prnNo: String,
    val Organization: List<RemarkItem>,
    val Finance: List<RemarkItem>,
    val Training: List<RemarkItem>,
    val TrainingInfra: List<RemarkItem>,
    val Certification: List<RemarkItem>,
    val Placement: List<RemarkItem>,
    val FieldVisit: List<RemarkItem>,
    val ResidentialFacility : List<RemarkItem>
)