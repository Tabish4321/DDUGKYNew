package com.deendayalproject.model.response

data class PlacementDetailsResponse(
    val yearWisePlacementDetails: List<YearlyPlacementDetails>?,
    val commitment: PlacementCommitmentResponse?
)

data class YearlyPlacementDetails(
    val year: String?,
    val candidatePlaced: Int?,
    val sanctionOrderId: String?,
    val esicNumber: String?,
    val epfoNumber: String?,
    var proofDocument: String? = null
)

data class PlacementCommitmentResponse(
    val commitmentMoreSixMonths: String?,
    val commitmentSixMonths: String?,
    val commitmentLessSixMonths: String?
)