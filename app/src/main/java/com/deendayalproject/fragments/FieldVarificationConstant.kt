package com.deendayalproject.fragments

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.RippleDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.deendayalproject.R
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.model.response.AttachmentItem
import com.deendayalproject.util.AppUtil
import com.google.gson.GsonBuilder
import java.text.NumberFormat
import java.util.Locale

// ─────────────────────────────────────────────────────────────
//  Domain Model
// ─────────────────────────────────────────────────────────────

data class FieldVerificationItem(
    val id: String = "",
    val requirement: String,
    val verificationDoc: String,
    val documents: List<String> = emptyList(),
    val uploadEnabled: Boolean = false,
    val imageUri: String? = null,
    val allowRemark: Boolean = false,
    var remarkText: String? = null,
    var attachments: MutableList<AttachmentItem> = mutableListOf(),
    val isAttachmentMandatory: Boolean = true,
    val sectionType: String = "",
    val verificationStatus: String = "Pending",
    val allowMultiUpload: Boolean = false,
    val allowedFileTypes: List<String> = listOf("pdf", "png", "jpg", "jpeg"),
)

// ─────────────────────────────────────────────────────────────
//  Internal helpers
// ─────────────────────────────────────────────────────────────

 data class DialogAction(val label: String, val onClick: () -> Unit)

 object SectionTag {
    const val ORG = "org"
    const val FIN = "fin"
    const val TRAINING = "training"
    const val TRAINING_INFRA = "trainingInfra"
    const val RESIDENTIAL = "residentialFacility"
    const val CERT = "cert"
    const val PLACEMENT = "placement"
    const val FIELD = "field"
    const val OFFICER_SELFIE = "Officer Selfie"
}

 object AttachmentLabel {
    // Organisation
    const val ORG_EXISTENCE = "uploadIndustryExistenceSelfDeclaration"
    const val ORG_REGISTRATION = "uploadIndustryRegistrationSelfDeclaration"
    const val ORG_EPFO = "uploadEpfoChallansSelfDeclaration"
    const val ORG_TAX = "uploadTaxDetailsSelfDeclaration"
    const val ORG_BANK = "uploadBankDetailsSelfDeclaration"
    const val ORG_MANPOWER = "uploadManpowerAgencyCheckSelfDeclaration"

    // Finance
    const val FIN_TURNOVER = "uploadAnnualTurnoverSelfDeclaration"
    const val FIN_NETWORTH = "uploadNetWorthSelfDeclaration"

    // Training
    const val TRAIN_CRITERIA = "uploadTrainingCriteriaSelfDeclaration"
    const val TRAIN_HOURS = "uploadTotalTrainingHoursSelfDeclaration"
    const val TRAIN_NSQF = "uploadRepetitionClubbingNsqfSelfDeclaration"
    const val TRAIN_BASIC = "uploadBasicTrainingSelfDeclaration"
    const val TRAIN_COMMITMENT = "uploadCommitmentSelfDeclaration"
    const val TRAIN_PLACEMENT = "uploadTrainingPlacementSelfDeclaration"
    const val TRAIN_DOMAIN = "uploadDomainSpecificTrainingSelfDeclaration"

    // Training Infra
    const val INFRA_DECLARATION = "uploadTrainingInfraSelfDeclaration"
    const val INFRA_CENTRE = "uploadTrainingCentrePhoto"
    const val INFRA_CLASSROOM = "uploadClassroomPhoto"
    const val INFRA_TOILET = "uploadToiletPhoto"
    const val INFRA_BUILDING = "uploadBuildingPhoto"
    const val INFRA_TABLES = "uploadTablesAndChairsPhoto"
    const val INFRA_LIGHTING = "uploadLightingAndSafetyPhoto"

    // Residential Facility
    const val RES_DECLARATION = "uploadResidentialFacilitySelfDeclaration"
    const val RES_BUILDING = "uploadResidentialBuildingPhoto"
    const val RES_SAFETY = "uploadResidentialSafetyMeasuresPhoto"
    const val RES_CANTEEN = "uploadResidentialCanteenPhoto"
    const val RES_BED_WATER = "uploadResidentialBedAndDrinkingWaterPhoto"

    // Certification
    const val CERT_DECLARATION = "uploadCertificationSelfDeclaration"

    // Placement
    const val PLACEMENT_DETAILS = "uploadPlacementDetailsSelfDeclaration"
    const val PLACEMENT_COMMITMENT = "uploadCommitmentSelfDeclaration"

    // Field Visit
    const val FIELD_SELFIE = "uploadOfficerSelfieWithGeoTag"
    const val FIELD_GEO_INITIATION = "verificationInitiationGeoValidation"
    const val FIELD_GEO_FINAL = "finalSubmissionGeoValidation"
    const val FIELD_TC_LAT = "trainingCentreLatitude"
    const val FIELD_TC_LNG = "trainingCentreLongitude"
    const val FIELD_RF_LAT = "residentialFacilityLatitude"
    const val FIELD_RF_LNG = "residentialFacilityLongitude"
    const val FIELD_DISTANCE = "distanceBetweenTcAndRf"
}

 object Requirement {
    const val ORG = "ORG_DETAILS"
    const val FIN = "FINANCE_DETAILS"
    const val TRAINING = "TRAINING_DETAILS"
    const val INFRA = "TRAINING_INFRA_DETAILS"
    const val RESIDENTIAL = "RESIDENTIAL_FACILITY_DETAILS"
    const val CERT = "ASSESSMENT_CERTIFICATION_DETAILS"
    const val PLACEMENT = "PLACEMENT_DETAILS"
    const val FIELD = "FIELD_VISIT_DETAILS"
}


// ─────────────────────────────────────────────────────────
//  Formatting utilities
// ─────────────────────────────────────────────────────────

fun formatAmount(value: Double?): String {
    if (value == null) return "₹ -"
    return "₹ ${
        NumberFormat.getInstance(Locale.getDefault()).apply { maximumFractionDigits = 2 }
            .format(value)
    }"
}

 fun formatNumber(value: Double?): String {
    if (value == null) return "-"
    return NumberFormat.getInstance(Locale.getDefault()).apply { maximumFractionDigits = 2 }
        .format(value)
}

 fun boldLabel(label: String, value: String): SpannableString {
    val full = "$label $value"
    return SpannableString(full).apply {
        setSpan(StyleSpan(Typeface.BOLD), 0, label.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}


 fun buildButtonBackground(dp: Float, strokeColor: Int, rippleColor: Int): Drawable {
    val cornerRadius = 10 * dp
    val strokeWidth = (1.8f * dp).toInt()
    val inset = (1.5f * dp).toInt()

    val rounded = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        this.cornerRadius = cornerRadius
        setColor(Color.TRANSPARENT)
        setStroke(strokeWidth, strokeColor)
    }
    val mask = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        this.cornerRadius = cornerRadius
        setColor(Color.WHITE)
    }
    return RippleDrawable(
        ColorStateList.valueOf(rippleColor),
        InsetDrawable(rounded, inset, inset, inset, inset),
        mask
    )
}

 fun logOrgAttachments(orgItems:MutableList<FieldVerificationItem>) {
    orgItems.forEachIndexed { index, item ->
        Log.d(
            "ORG_FINAL_CHECK",
            "Index:$index  Requirement:${item.requirement}  Attachments:${item.attachments.size}  ImageUri:${item.imageUri}  Remark:${item.remarkText}"
        )
        item.attachments.forEach {
            Log.d(
                "ORG_ATTACHMENT_DATA",
                "Label:${it.label}  Values:${it.value.size}  Remark:${it.remark}"
            )
        }
    }
}

 fun <T> logJson(tag: String, data: T) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    Log.d(tag, gson.toJson(data))
}

// ─────────────────────────────────────────────────────────
//  Item list builders
// ─────────────────────────────────────────────────────────






// ── Item factory helpers ──────────────────────────────────

 fun viewItem(requirement: String, verificationDoc: String, documents: List<String>) =
    FieldVerificationItem(
        id = "",
        requirement = requirement,
        verificationDoc = verificationDoc,
        documents = documents,
        uploadEnabled = false,
        allowRemark = false
    )

 fun uploadItem(
    id: String,
    requirement: String,
    verificationDoc: String,
    docLabel: String,
    sectionType: String
) = FieldVerificationItem(
    id = id,
    requirement = requirement,
    verificationDoc = verificationDoc,
    documents = listOf(docLabel),
    uploadEnabled = true,
    allowRemark = true,
     isAttachmentMandatory = true,
    sectionType = sectionType,
     attachments = mutableListOf(),
     remarkText = ""
)