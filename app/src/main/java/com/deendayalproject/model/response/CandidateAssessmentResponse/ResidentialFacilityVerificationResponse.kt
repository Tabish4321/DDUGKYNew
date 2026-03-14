package com.deendayalproject.model.response.CandidateAssessmentResponse

/**
 * Created by Rishi Porwal
 */

data class ResidentialFacilityVerificationResponse(

    val inspectionId: Int?,
    val batchId: Int?,
    val candidateId: String?,

    val separateHostels: String?,
    val separateHostelsRemark: String?,

    val hostelNameBoardAvailable: String?,
    val hostelNameBoardAvailableRemark: String?,

    val contactDetailsBoard: String?,
    val contactDetailsBoardRemark: String?,

    val entitlementResponsibilitiesBoard: String?,
    val entitlementResponsibilitiesBoardRemark: String?,

    val basicInformationBoard: String?,
    val basicInformationBoardRemark: String?,

    val biometricAttendance: String?,
    val biometricAttendanceRemark: String?,

    val pickupDropFacility: String?,
    val pickupDropFacilityRemark: String?,

    val grievanceRegister: String?,
    val grievanceRegisterRemark: String?,

    val individualBed: String?,
    val individualBedRemark: String?,

    val kitchenDiningHygienic: String?,
    val kitchenDiningHygienicRemark: String?,

    val diningRecreationSpace: String?,
    val diningRecreationSpaceRemark: String?,

    val numberOfWashbasins: String?,
    val numberOfWashbasinsRemark: String?,

    val toiletSignage: String?,
    val toiletSignageRemark: String?,

    val foodQualityHygiene: String?,
    val foodQualityHygieneRemark: String?,

    val foodCommitteeFormed: String?,
    val foodCommitteeFormedRemark: String?,

    val foodAsPerMenu: String?,
    val foodAsPerMenuRemark: String?,

    val drinkingWaterAvailable: String?,
    val drinkingWaterAvailableRemark: String?,

    val toiletHygieneMaintained: String?,
    val toiletHygieneMaintainedRemark: String?,

    val overheadTankCleaned: String?,
    val overheadTankCleanedRemark: String?,

    val quarterlyHealthCheckup: String?,
    val quarterlyHealthCheckupRemark: String?,

    val firstAidKit: String?,
    val firstAidKitRemark: String?,

    val doctorOnCall: String?,
    val doctorOnCallRemark: String?,

    val securityWardenPresent: String?,
    val securityWardenPresentRemark: String?,

    val gensetPowerCut: String?,
    val gensetPowerCutRemark: String?,

    val tvCableAvailable: String?,
    val tvCableAvailableRemark: String?,

    val indoorGamesEquipment: String?,
    val indoorGamesEquipmentRemark: String?,

    val wardenPoliceVerification: String?,
    val wardenPoliceVerificationRemark: String?
)