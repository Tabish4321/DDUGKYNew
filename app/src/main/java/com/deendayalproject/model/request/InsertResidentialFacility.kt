package com.deendayalproject.model.request


data class InsertResidentialFacility(
    val loginId: String,
    val appVersion: String,
    val imeiNo: String,
    val trainingCentre: Int,
    val sanctionOrder: String,
    val hostelsSeparated: String,
    val wardenCaretakerMale: String,
    val wardenCaretakerFemale: String,
    val securityGuards: String,
    val maleDoctor: String,
    val femaleDoctor: String,
    val hostelsSeparatedFile: String,
    val wardenCaretakerMaleFile: String,
    val wardenCaretakerFemaleFile: String,
    val securityGuardsFile: String,
    val maleDoctorFile: String,
    val femaleDoctorFile: String,
    val facilityId: String
)

