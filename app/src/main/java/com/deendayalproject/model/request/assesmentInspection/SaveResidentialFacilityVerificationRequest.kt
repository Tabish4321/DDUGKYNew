package com.deendayalproject.model.request.assesmentInspection

/**
 * Created by Rishi Porwal
 */


data class SaveResidentialFacilityVerificationRequest(

    val appVersion: String,
    val candidateId: String,
    val batchId: Int,
    val inspectionId: Int,

    val separateHostelsQid: String,
    val separateHostels: String,
    val separateHostelsRemark: String?,

    val hostelNameBoardAvailableQid: String,
    val hostelNameBoardAvailable: String,
    val hostelNameBoardAvailableRemark: String?,

    val contactDetailsBoardQid: Int,
    val contactDetailsBoard: String,
    val contactDetailsBoardRemark: String?,

    val entitlementResponsibilitiesBoardQid: Int,
    val entitlementResponsibilitiesBoard: String,
    val entitlementResponsibilitiesBoardRemark: String?,

    val basicInformationBoardQid: Int,
    val basicInformationBoard: String,
    val basicInformationBoardRemark: String?,

    val biometricAttendanceQid: Int,
    val biometricAttendance: String,
    val biometricAttendanceRemark: String?,

    val pickupDropFacilityQid: Int,
    val pickupDropFacility: String,
    val pickupDropFacilityRemark: String?,

    val grievanceRegisterQid: Int,
    val grievanceRegister: String,
    val grievanceRegisterRemark: String?,

    val individualBedQid: Int,
    val individualBed: String,
    val individualBedRemark: String?,

    val kitchenDiningHygienicQid: Int,
    val kitchenDiningHygienic: String,
    val kitchenDiningHygienicRemark: String?,

    val diningRecreationSpaceQid: Int,
    val diningRecreationSpace: String,
    val diningRecreationSpaceRemark: String?,

    val numberOfWashbasinsQid: Int,
    val numberOfWashbasins: String,
    val numberOfWashbasinsRemark: String?,

    val toiletSignageQid: Int,
    val toiletSignage: String,
    val toiletSignageRemark: String?,

    val foodQualityHygieneQid: Int,
    val foodQualityHygiene: String,
    val foodQualityHygieneRemark: String?,

    val foodCommitteeFormedQid: Int,
    val foodCommitteeFormed: String,
    val foodCommitteeFormedRemark: String?,

    val foodAsPerMenuQid: Int,
    val foodAsPerMenu: String,
    val foodAsPerMenuRemark: String?,

    val drinkingWaterAvailableQid: Int,
    val drinkingWaterAvailable: String,
    val drinkingWaterAvailableRemark: String?,

    val toiletHygieneMaintainedQid: Int,
    val toiletHygieneMaintained: String,
    val toiletHygieneMaintainedRemark: String?,

    val overheadTankCleanedQid: Int,
    val overheadTankCleaned: String,
    val overheadTankCleanedRemark: String?,

    val quarterlyHealthCheckupQid: Int,
    val quarterlyHealthCheckup: String,
    val quarterlyHealthCheckupRemark: String?,

    val firstAidKitQid: Int,
    val firstAidKit: String,
    val firstAidKitRemark: String?,

    val doctorOnCallQid: Int,
    val doctorOnCall: String,
    val doctorOnCallRemark: String?,

    val securityWardenPresentQid: Int,
    val securityWardenPresent: String,
    val securityWardenPresentRemark: String?,

    val gensetPowerCutQid: Int,
    val gensetPowerCut: String,
    val gensetPowerCutRemark: String?,

    val tvCableAvailableQid: Int,
    val tvCableAvailable: String,
    val tvCableAvailableRemark: String?,

    val indoorGamesEquipmentQid: Int,
    val indoorGamesEquipment: String,
    val indoorGamesEquipmentRemark: String?,

    val wardenPoliceVerificationQid: Int,
    val wardenPoliceVerification: String,
    val wardenPoliceVerificationRemark: String?
)