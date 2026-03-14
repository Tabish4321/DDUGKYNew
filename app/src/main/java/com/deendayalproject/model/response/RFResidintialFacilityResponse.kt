package com.deendayalproject.model.response

data class RFResidintialFacilityResponse(
    val wrappedList: MutableList<RFResidintialFacilityResponseDetails>,
    val responseCode: Int,
    val responseDesc: String
)
data class RFResidintialFacilityResponseDetails(
    val wardenCaretakerFemale: String,
    val maleDoctor: String,
    val wardenCaretakerFemalePdf: String,
    val femaleDoctorPdf: String,
    val wardenCaretakerMale: String,
    val femaleDoctor: String,
    val wardenCaretakerMalePdf: String,
    val hostelsSeparatedPdf: String,
    val securityGuardsPdf: String,
    val securityGuards: String,
    val maleDoctorPdf: String,
    val hostelsSeparated: String,
)