package com.deendayalproject.model.response

import com.deendayalproject.model.request.TrainingCenter
import com.google.gson.annotations.SerializedName

data class FieldVerificationDetailResponse(

    @SerializedName("wrappedList")
    val wrappedList: List<FieldVerificationDetailItem> = emptyList(),

    @SerializedName("errorsMap")
    val errorsMap: Map<String, Any>? = null,

    @SerializedName("responseCode")
    val responseCode: Int = 0,

    @SerializedName("responseDesc")
    val responseDesc: String? = null
)

data class FieldVerificationDetailItem(
    @SerializedName("organizationDetails")
    val organizationDetails: OrganizationDetails? = null,
    val financialDetails: FinancialDetailsResponse? = null,
    val trainingDetails: TrainingDetailsResponse? = null,
    val trainingInfrastrutureDetails: TrainingInfraDetailsResponse? = null,
    val assessmentCertificationDetails: AssessmentCertificationDetailsResponse? = null,
    val placementDetails: PlacementDetailsResponse? = null
)

data class AssessmentCertificationDetailsResponse(
    val commitmentLetterDetails: CertificationDetails?
)
data class CertificationDetails(
    val awardBodyCommit: String?,
    val seventyPctCommit: String?
)

data class TrainingInfraDetailsResponse(
    val residentialFacilityDetails: ResidentialFacilityDetails?
)
data class ResidentialFacilityDetails(
    val residentialFacilityAvailable: String?,
    val residentialFacilityDocument: String?
)

/** Root group under organizationDetails */
data class OrganizationDetails(
    @SerializedName("proofOfIndustryExistence")
    val proofOfIndustryExistence: ProofOfIndustryExistence? = null,

    @SerializedName("industryRegistration")
    val industryRegistration: IndustryRegistration? = null,

    @SerializedName("epfoChallans")
    val epfoChallans: EpfoChallans? = null,

    @SerializedName("taxDetails")
    val taxDetails: TaxDetails? = null,

    @SerializedName("bankDetails")
    val bankDetails: BankDetails? = null,

    @SerializedName("manpowerAgencyCheck")
    val manpowerAgencyCheck: ManpowerAgencyCheck? = null
)

/** proofOfIndustryExistence */
data class ProofOfIndustryExistence(
    @SerializedName("dateOfIncorporation")
    val dateOfIncorporation: String? = null // e.g., "2017-06-30T18:30:00.000+00:00"
)

/** industryRegistration */
data class IndustryRegistration(
    @SerializedName("epfoNumber")
    val epfoNumber: String? = null,

    @SerializedName("epfoAttachment")
    val epfoAttachment: String? = null,

    @SerializedName("esicNumber")
    val esicNumber: String? = null,

    @SerializedName("esicAttachment")
    val esicAttachment: String? = null,

    @SerializedName("factoryRegistrationNumber")
    val factoryRegistrationNumber: String? = null,

    @SerializedName("factoryRegistrationAttachment")
    val factoryRegistrationAttachment: String? = null
)

/** epfoChallans */
data class EpfoChallans(
    @SerializedName("existingStaffRegisteredInEpfo")
    val existingStaffRegisteredInEpfo: String? = null, // comes as string in your sample

    @SerializedName("epfoDocument")
    var epfoDocument: String? = null
)

/** taxDetails */
data class TaxDetails(
    @SerializedName("gstNumber")
    val gstNumber: String? = null,

    @SerializedName("tanNumber")
    val tanNumber: String? = null,

    @SerializedName("tanAttachment")
    var tanAttachment: String? = null
)

/** bankDetails */
data class BankDetails(
    @SerializedName("bankAccountNumber")
    val bankAccountNumber: String? = null,

    @SerializedName("bankName")
    val bankName: String? = null,

    @SerializedName("ifscCode")
    val ifscCode: String? = null,

    @SerializedName("bankLetterDocument")
    var bankLetterDocument: String? = null,

    @SerializedName("aadhaarLinked")
    val aadhaarLinked: Boolean? = null,

    @SerializedName("panLinked")
    val panLinked: Boolean? = null,

    @SerializedName("selfDeclarationDocument")
    var selfDeclarationDocument: String? = null
)

/** manpowerAgencyCheck */
data class ManpowerAgencyCheck(
    @SerializedName("engagedInManpowerSupply")
    val engagedInManpowerSupply: Boolean? = null,

    @SerializedName("remarks")
    val remarks: String? = null
)

// a single remark item (one row)
data class RemarkItem(
    val section: String,     // e.g. "Organization", "Finance", "Training"
    val requirement: String, // the FieldVerificationItem.requirement text (or an id if you have one)
    val remark: String
)

// optional wrapper if backend expects grouped remarks
data class SectionRemarks(
    val section: String,
    val remarks: List<RemarkItem>
)