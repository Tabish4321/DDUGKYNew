package com.deendayalproject.model.response

data class InfrastructureDetailsandCompliancesRFQT(   val wrappedList: MutableList<DetailsAndCompliancesRFQT>,
                                                      val errorsMap: Map<String, String>?,
                                                      val responseCode: Int,
                                                      val responseDesc: String
)

data class DetailsAndCompliancesRFQT(
    val corridor: String?,
    val foodSpecificationBoard: String?,
    val buildingPlanFile: String?,
    val hostelNameBoardProofFile: String?,
    val wallPhotosFile: String?,
    val protectionStairsProofFile: String?,
    val buildingArea: String?,
    val foodSpecificationBoardFile: String?,
    val contactDetailImportantPeople: String?,
    val circulatingArea: String?,
    val hostelNameBoard: String?,
    val studentEntitlementBoard: String?,
    val securingWiresDoneProofFile: String?,
    val basicInformationBoardproofFile: String?,
    val openSpaceArea: String?,
    val securingWiresDone: String?,
    val corridorProofFile: String?,
    val circulatingAreaProofFile: String?,
    val buildingPhotosFile: String?,
    val leakagesProofFile: String?,
    val roof: String?,
    val basicInformationBoard: String?,
    val resFacilityId: String?,
    val conformanceDduProofFile: String?,
    val switchBoardsPanelBoardsProofFile: String?,
    val conformanceDdu: String?,
    val selfDeclaration: String?,
    val protectionStairs: String?,
    val ownership: String?,
    val contactDetailImportantPeopleproofFile: String?,
    val plastring: String?,
    val studentEntitlementBoardProofFile: String?,
    val switchBoardsPanelBoards: String?,
    val leakage: String?
)

