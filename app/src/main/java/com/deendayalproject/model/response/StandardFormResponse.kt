package com.deendayalproject.model.response


data class StandardFormResponse(
    val wrappedList: List<WrappedItem>,
    val responseCode: Int,
    val responseDesc: String
)

data class WrappedItem(
    val letterToMobilizationPlan: String?,
    val candidateAppForm: String?,
    val indexInvdcandidateDossier: String?,
    val aclp: String?,
    val parentsConsentForm: String?,
    val aptitudeTest: String?,
    val prfEvelPlanCandidate: String?,
    val candidateAfterBatchFreeze: String?,
    val trainingPlan: String?,
    val dailyFailureItemReport: String?,
    val letterFromMobilizationPlan: String?,
    val trainingCompCertDisbRecord: String?,
    val tadaCalcRecord: String?,
    val candidateChecklistItem: String?,
    val enrolledCandidateList: String?,
    val days15Summery: String?,
    val candidateCertificateAsmt: String?,
    val trainerProfile: String?,
    val evaluationAssessmentSumm: String?,
    val trainersAttendRegBoi: String?,
    val tradeCounselling: String?,
    val candidateIdTemp: String?,
    val batchJobTrainingPlan: String?,
    val ipEnabledCamera: String?,
    val studentEntitlement: String?,
    val deployedStaffSumm: String?,
    val dulySignedformProofApplicable: String?,
    val equipmentList: String?,
    val tcInspection: String?,
    val prfEvelPlanTrainers: String?,
    val candidateOnFieldReg: String?,
    val dulySignedformProof: String?,
    val tafEquipment: String?,
    val candidateAttendRegBio: String?,
    val trainingCertificate: String?,
    val tabletsDistribution: String?
)

