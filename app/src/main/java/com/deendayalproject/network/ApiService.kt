package com.deendayalproject.network

import PreviousInspectionItemResponse
import com.deendayalproject.base.BaseResponse
import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.request.AddNewRFReq
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.AttendanceBatchListReq
import com.deendayalproject.model.request.AttendanceCandidateListReq
import com.deendayalproject.model.request.AttendanceCheckReq
import com.deendayalproject.model.request.AttendanceInsertReq
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.DLRequest
import com.deendayalproject.model.request.DeleteLivingRoomList
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.GetUrinalWashReq
import com.deendayalproject.model.request.FieldVerificationDetailRequest
import com.deendayalproject.model.request.FieldVerificationFinalSubmit
import com.deendayalproject.model.request.FieldVerificationListRequest
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.GetDDSaveDataReq
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.model.request.GetPrevDueQueList
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
import com.deendayalproject.model.request.ITLabDetailsRequest
import com.deendayalproject.model.request.IndoorGamesRequest
import com.deendayalproject.model.request.InsertFacultyAttendance
import com.deendayalproject.model.request.InsertLivingAreaReq
import com.deendayalproject.model.request.InsertNonLivingReq
import com.deendayalproject.model.request.InsertResidentialFacility
import com.deendayalproject.model.request.InsertRfInfraDetaiReq
import com.deendayalproject.model.request.InsertSupportFacilitiesReq
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.InsertToiletDataReq
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.request.LivingRoomReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModifyRfList
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.OfficeRoomDetailsRequest
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.RFQteamVerificationRequest
import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.request.RfCommonReq
import com.deendayalproject.model.request.RfFinalSubmitReq
import com.deendayalproject.model.request.RfLivingAreaInformationRQ
import com.deendayalproject.model.request.SaltRequest
import com.deendayalproject.model.request.SaveBatchVerificationRequest
import com.deendayalproject.model.request.SaveInspectionStandardFormRequest
import com.deendayalproject.model.request.SavePreDDQueReq
import com.deendayalproject.model.request.SectionReq
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.SubjectDeleteReq
import com.deendayalproject.model.request.SubjectReq
import com.deendayalproject.model.request.SubmitOfficeCumCounsellingRoomDetailsRequest
import com.deendayalproject.model.request.TCDLRequest
import com.deendayalproject.model.request.TCITLDomainLabDetailsRequest
import com.deendayalproject.model.request.TCRRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.ToiletCountListReq
import com.deendayalproject.model.request.ToiletDeleteList
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.request.ToiletRoomInformationReq
import com.deendayalproject.model.request.ToiletRoomReq
import com.deendayalproject.model.request.TrainerListReq
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.ULBReq
import com.deendayalproject.model.request.UrinalWashbasinReq
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.request.WardReq
import com.deendayalproject.model.request.assesmentInspection.AssessmentStatusInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateRecordsVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.GetDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionSectionStatusRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionStandardFormRequest
import com.deendayalproject.model.request.assesmentInspection.GetResidentialFacilityVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.GetTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveCandidateAttendanceInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveResidentialFacilityVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.SaveTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.insertRfBasicInfoReq
import com.deendayalproject.model.request.saveTrainerClassObservationInspectionReq
import com.deendayalproject.model.response.AcademicNonAcademicResponse
import com.deendayalproject.model.response.AddNewRFRes
import com.deendayalproject.model.response.AllRoomDetailResponse
import com.deendayalproject.model.response.AttendanceBatchRes
import com.deendayalproject.model.response.AttendanceCandidateRes
import com.deendayalproject.model.response.AttendanceCheckRes
import com.deendayalproject.model.response.AttendanceInsertRes
import com.deendayalproject.model.response.BlockResponse
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.AssessmentStatusResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateAssessmentInspectionDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateAttendanceInspectionResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateRecordsVerificationDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.DistributedLearningMaterialInspectionResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.EntitlementsDistributionInspectionResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.InspectionSectionStatusResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.InspectionStandardFormDto
import com.deendayalproject.model.response.CandidateAssessmentResponse.ResidentialFacilityVerificationResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.SaveInspectionStandardFormResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.TrainerAttendanceInspectionResponse
import com.deendayalproject.model.response.CandidateInspectionDetails
import com.deendayalproject.model.response.CandidateInspectionDetailsResponse
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.CommonEquipmentRes
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.DistrictResponse
import com.deendayalproject.model.response.DueDiligenceItemResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.ElectricalWireRes
import com.deendayalproject.model.response.FacultyDetailsRes
import com.deendayalproject.model.response.FieldVerificationDetailResponse
import com.deendayalproject.model.response.FieldVerificationListResponse
import com.deendayalproject.model.response.FinalSubmitRes
import com.deendayalproject.model.response.FinancialDetailsResponse
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.GetDDSaveDataRes
import com.deendayalproject.model.response.GetImageListRes
import com.deendayalproject.model.response.GetPrevDueQueListRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.GetUrinalWashRes
import com.deendayalproject.model.response.GpResponse
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.model.response.IndoorRFGameResponse
import com.deendayalproject.model.response.InfrastructureDetailsandCompliancesRFQT
import com.deendayalproject.model.response.InsertRes
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
import com.deendayalproject.model.response.IpEnableRes
import com.deendayalproject.model.response.LivingRoomListViewRes
import com.deendayalproject.model.response.LivingAreaDelete
import com.deendayalproject.model.response.LivingAreaListRes
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModifyRFRes
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.NonAreaInformationRoom
import com.deendayalproject.model.response.NonceResponse
import com.deendayalproject.model.response.RFResidintialFacilityResponse
import com.deendayalproject.model.response.RFSupportFacilitiesAvailableResponse
import com.deendayalproject.model.response.ResidentialFacilityQTeam
import com.deendayalproject.model.response.RfFinalSubmitRes
import com.deendayalproject.model.response.RfListResponse
import com.deendayalproject.model.response.RfLivingAreaInformationResponse
import com.deendayalproject.model.response.RfQTeamListRes
import com.deendayalproject.model.response.SectionResponse
import com.deendayalproject.model.response.SectionStatusRes
import com.deendayalproject.model.response.SignageInfo
import com.deendayalproject.model.response.StandardFormResponse
import com.deendayalproject.model.response.StateResponse
import com.deendayalproject.model.response.SubjectDeleteRes
import com.deendayalproject.model.response.SubjectListRes
import com.deendayalproject.model.response.SupportInfrastructureResponse
import com.deendayalproject.model.response.TcAcademiaNonAcademiaRes
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.ToiletDetailsErrorResponse
import com.deendayalproject.model.response.TeachingLearningRes
import com.deendayalproject.model.response.ToiletCountList
import com.deendayalproject.model.response.ToiletListRes
import com.deendayalproject.model.response.ToiletRes
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.ToiletRoomInformationViewRes
import com.deendayalproject.model.response.ToiletViewRes
import com.deendayalproject.model.response.TrainerClassObservationResponse
import com.deendayalproject.model.response.TrainerListRes
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.model.response.UlbRes
import com.deendayalproject.model.response.VillageRes
import com.deendayalproject.model.response.WardRes
import com.deendayalproject.model.uistate.GetCandidateInspectionRequest
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.uidai.ekyc.UidaiResp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("logout")
    suspend fun logOutUser(): Response<LoginResponse>

    @POST("get-nonce")
    suspend fun getSalt(
        @Body request: SaltRequest
    ): NonceResponse

    @POST("modulenforms")
    suspend fun fetchModules(@Body request: ModulesRequest): Response<ModuleResponse>

    @POST("getTrainingCenterList")
    suspend fun getTrainingCenterList(@Body request: TrainingCenterRequest): Response<TrainingCenterResponse>


    @POST(value = "getTrainingCenterVerificationList")
    suspend fun getQTeamTrainingList(@Body request: TrainingCenterRequest): Response<TrainingCenterResponse>

    @POST(value ="getCaptivePiaList")
    suspend fun getFieldVerificationList(@Body request: FieldVerificationListRequest) : Response<FieldVerificationListResponse>


    @POST(value = "getTrainingCenterVerificationSRLM")
    suspend fun getTrainingCenterVerificationSRLM(@Body request: TrainingCenterRequest): Response<TrainingCenterResponse>


    @POST(value = "getResidentialFacilitiesList")
    suspend fun getResidentialFacilitiesList(@Body request: TrainingCenterRequest): Response<RfListResponse>

    @POST("insertCaptiveEmpanelmentVerification")
    suspend fun submitFieldVerification(
        @Body request: FieldVerificationFinalSubmit
    ): Response<FieldVerificationDetailResponse>


    @POST(value = "insertCCTVCompliance")
    suspend fun insertCCTVCompliance(@Body request: CCTVComplianceRequest): Response<CCTVComplianceResponse>

    @POST("getCaptiveOrganizationDetails")
    suspend fun getFieldVerificationDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST("getCaptiveFinancialDetails")
    suspend fun getFieldVerificationFinDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST("getCaptiveTrainingDetails")
    suspend fun getFieldVerificationTrainingDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST("getCaptiveTrainingInfrastructure")
    suspend fun getFieldVerificationTrainingInfraDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST("getCaptiveAssessmentCertification")
    suspend fun getFieldVerificationCertificationDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST("getCaptivePlacementDetails")
    suspend fun getFieldVerificationPlacementDetail(
        @Body request: FieldVerificationDetailRequest
    ): Response<FieldVerificationDetailResponse>

    @POST(value = "insertTcElectricWiringStandard")
    suspend fun insertTcElectricWiringStandard(@Body request: ElectricalWiringRequest): Response<ElectircalWiringReponse>


    @POST(value = "insertTcGeneralDetails")
    suspend fun insertTcGeneralDetails(@Body request: InsertTcGeneralDetailsRequest): Response<InsertTcGeneralDetailsResponse>


    @POST(value = "insertTcBasicInfo")
    suspend fun insertTcBasicInfo(@Body request: TcBasicInfoRequest): Response<InsertTcBasicInfoResponse>


    @POST(value = "insertTcSignagesInfoBoard")
    suspend fun insertTcSignagesInfoBoard(@Body request: TcSignagesInfoBoardRequest): Response<TcSignagesInfoBoardResponse>

    @POST(value = "insertTcAvailabilitySupportInfra")
    suspend fun insertTcAvailabilitySupportInfra(@Body request: TcAvailabilitySupportInfraRequest): Response<TcAvailabilitySupportInfraResponse>

    @POST(value = "insertTcCommonEquipment")
    suspend fun insertTcCommonEquipment(@Body request: TcCommonEquipmentRequest): Response<TcCommonEquipmentResponse>

    @POST(value = "insertTcDescriptionOtherAreas")
    suspend fun insertTcDescriptionOtherAreas(@Body request: TcDescriptionOtherAreasRequest): Response<TcDescriptionOtherAreasResponse>


    @POST(value = "getTrainerCenterInfo")
    suspend fun getTrainerCenterInfo(@Body request: TrainingCenterInfo): Response<TrainingCenterInfoRes>


    @POST(value = "getTCTrainerAndOtherStaffsList")
    suspend fun getTcStaffDetails(@Body request: TrainingCenterInfo): Response<TcStaffAndTrainerResponse>


    @POST(value = "getTrainerCenterInfra")
    suspend fun getTrainerCenterInfra(@Body request: TrainingCenterInfo): Response<TcInfraResponse>


    @POST(value = "insertTcToiletsWashBasins")
    suspend fun insertTcToiletsWashBasins(@Body request: ToiletDetailsRequest): Response<ToiletDetailsErrorResponse>


    @POST(value = "getTcAcademicNonAcademicArea")
    suspend fun getTcAcademicNonAcademicArea(@Body request: TrainingCenterInfo): Response<TcAcademiaNonAcademiaRes>


    @POST(value = "getTcToiletWashBasin")
    suspend fun getTcToiletWashBasin(@Body request: TrainingCenterInfo): Response<ToiletResponse>


    @POST(value = "getDescriptionOtherArea")
    suspend fun getDescriptionOtherArea(@Body request: TrainingCenterInfo): Response<DescOtherAreaRes>


    @POST(value = "getTeachingLearningMaterial")
    suspend fun getTeachingLearningMaterial(@Body request: TrainingCenterInfo): Response<TeachingLearningRes>


    @POST(value = "getGeneralDetails")
    suspend fun getGeneralDetails(@Body request: TrainingCenterInfo): Response<GeneralDetails>


    @POST(value = "getElectricalWiringStandard")
    suspend fun getElectricalWiringStandard(@Body request: TrainingCenterInfo): Response<ElectricalWireRes>


    @POST(value = "getSignagesAndInfoBoard")
    suspend fun getSignagesAndInfoBoard(@Body request: TrainingCenterInfo): Response<SignageInfo>


    @POST(value ="getIpEnabledcamera")
    suspend fun getIpEnabledcamera(@Body request: TrainingCenterInfo) : Response<IpEnableRes>


    @POST(value ="getCommonEquipment")
    suspend fun getCommonEquipment(@Body request: TrainingCenterInfo) : Response<CommonEquipmentRes>


    @POST(value ="getAvailabilitySupportInfra")
    suspend fun getAvailabilitySupportInfra(@Body request: TrainingCenterInfo) : Response<SupportInfrastructureResponse>


    @POST(value ="getAvailabilityStandardForms")
    suspend fun getAvailabilityStandardForms(@Body request: TrainingCenterInfo) : Response<StandardFormResponse>



    @POST(value ="getAcademicRoomDetails")
    suspend fun getAcademicRoomDetails(@Body request: AllRoomDetaisReques) : Response<AllRoomDetailResponse>


    @POST(value ="insertQTeamVerification")
    suspend fun insertQTeamVerification(@Body request: TcQTeamInsertReq) : Response<InsertTcGeneralDetailsResponse>


    @POST(value ="insertSrlmVerification")
    suspend fun insertSrlmVerification(@Body request: TcQTeamInsertReq) : Response<InsertTcGeneralDetailsResponse>

    @POST(value ="trainingCenterFinalInsert")
    suspend fun getFinalSubmitData(@Body request: TrainingCenterInfo) : Response<FinalSubmitRes>

    @POST(value ="getTcSectionStatus")
    suspend fun getSectionsStatus(@Body request: TrainingCenterInfo) : Response<SectionStatusRes>

    @POST("getStateList")
    suspend fun getStateList(@Body request: StateRequest): Response<StateResponse>

    @POST("getDistrictList")
    suspend fun getDistrictList(@Body request: DistrictRequest): Response<DistrictResponse>

    @POST("getBlockList")
    suspend fun getBlockList(@Body request: BlockRequest): Response<BlockResponse>

    @POST("getGPList")
    suspend fun getGPList(@Body request: GpRequest): Response<GpResponse>

    @POST("getVillageList")
    suspend fun getVillageList(@Body request: VillageReq): Response<VillageRes>




    //    Ajit Ranjan TcAcademicNonAcademicArea
//      @POST("deleteAcademicRoom")
//
////    suspend fun deleteRoom(@Body request: DeleteRoomRequest) : Response<DeleteRoomResponse>
//
//     fun deleteRoom(@Body request: DeleteRoomRequest): Call<DeleteRoomResponse>
    @POST(value = "getTcAcademicNonAcademicArea")
    suspend fun getTcAcademicNonAcademic(@Body request: AcademicNonAcademicArea) : Response<AcademicNonAcademicResponse>


// Ajit Ranjan ITLAB


    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertITLabBasicInfo(@Body request: ITLabDetailsRequest) : Response<ITLAbDetailsErrorResponse>

    //    Ajit Ranjan  Office Cum(Counselling room)
    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertOfficeCumCounsellingroomBasicInfo(@Body request: SubmitOfficeCumCounsellingRoomDetailsRequest) : Response<ITLAbDetailsErrorResponse>




    //    Ajit Ranjan  ReceptionArea
    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertReceptionAreaBasicInfo(@Body request: ReceptionAreaRoomDetailsRequest) : Response<ITLAbDetailsErrorResponse>




    //    Ajit Ranjan  OfficeRoom
    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertOfficeroomBasicInfo(@Body request: OfficeRoomDetailsRequest) : Response<ITLAbDetailsErrorResponse>


    //    Ajit Ranjan  ItComeDomainlab
    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertItComeDomainlabBasicInfo(@Body request: ITComeDomainLabDetailsRequest) : Response<ITLAbDetailsErrorResponse>


//    Theory Cum IT Lab

    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun inserttheorycumitlabBasicInfo(@Body request: TCITLDomainLabDetailsRequest) : Response<ITLAbDetailsErrorResponse>




//    Theory Cum Domain Lab

    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun inserttheorycumdomainlabBasicInfo(@Body request: TCDLRequest) : Response<ITLAbDetailsErrorResponse>



//    Theory Cum Domain Lab

    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun insertDomainLabBasicInfo(@Body request: DLRequest) : Response<ITLAbDetailsErrorResponse>
    //    Theory Class Room
    @POST(value = "insertTcAcademicAreaDetailsTheoryClassRoom")
    suspend fun inserttheoryClassroomBasicInfo(@Body request: TCRRequest) : Response<ITLAbDetailsErrorResponse>


    @POST(value = "insertRfBasicInformation")
    suspend fun insertRfBasicInformation(@Body request: insertRfBasicInfoReq): Response<ITLAbDetailsErrorResponse>



    @POST(value = "insertRfInfraDetailsAndComliance")
    suspend fun insertRfInfraDetailsAndComliance(@Body request: InsertRfInfraDetaiReq) : Response<ITLAbDetailsErrorResponse>


    @POST(value = "insertRfLivingAreaInformation")
    suspend fun insertRfLivingAreaInformation(@Body request: InsertLivingAreaReq) : Response<ITLAbDetailsErrorResponse>

    //    ResidentialFacilityQTeamRequest Ajit Ranjan  16/10/2025
    @POST(value ="getRFQteamVerificationList")
    suspend fun getRFQteamVerificationList(@Body request: ResidentialFacilityQTeamRequest) : Response<RfQTeamListRes>



    //    GetRfBasicInformation AjitRanjan 17/10/2025
    @POST(value ="getRfBasicInformation")
    suspend fun getRfBasicInfoo(@Body request: RfCommonReq) : Response<ResidentialFacilityQTeam>


//    Ajit Ranjan cre
    //    ate 21/October/2025  CompliancesRFQTReqRFQT


    @POST(value ="getRfInfraDetailsAndComliance")
    suspend fun getgetCompliancesRFQTReqRFQT(@Body request: CompliancesRFQTReq) : Response<InfrastructureDetailsandCompliancesRFQT>

//    Ajit Ranjan create 24/October/2025  getRfLivingAreaInformation
    @POST(value ="getRfLivingAreaInformation")
    suspend fun getRfLivingAreaInformation(@Body request: RfLivingAreaInformationRQ) : Response<RfLivingAreaInformationResponse>

    //    Ajit Ranjan create 27/October/2025  getRfLivingAreaInformation
    @POST(value ="livingRoomListView")
    suspend fun getlivingRoomListView(@Body request: LivingRoomListViewRQ) : Response<LivingRoomListViewRes>

//    Ajit Ranjan create 27/October/2025  toiletRoomListView


    @POST(value ="toiletRoomListView")
    suspend fun getToiletRoomListView(@Body request: ToiletRoomInformationReq) : Response<ToiletViewRes>

    //    Ajit Ranjan create 30/October/2025  getRfToiletRoomInformation

    @POST(value ="getRfToiletRoomInformation")
    suspend fun ToiletRoomInformation
                (@Body request: ToiletRoomReq) :
            Response<ToiletRoomInformationViewRes>



    @POST(value ="livingRoomListView")
    suspend fun getRfLivingRoomListView(@Body request: LivingRoomReq) : Response<LivingAreaListRes>



    @POST(value ="deleteLivingRoom")
    suspend fun deleteLivingRoom(@Body request: DeleteLivingRoomList) : Response<LivingAreaDelete>


    @POST(value ="toiletRoomListView")
    suspend fun getRfToiletListView(@Body request: LivingRoomReq) : Response<ToiletListRes>




    @POST(value ="toiletSectionListView")
    suspend fun toiletSectionListView(@Body request: LivingRoomReq) : Response<ToiletListRes>

    @POST(value ="deleteToiletRoom")
    suspend fun deleteToiletRoom(@Body request: ToiletDeleteList) : Response<LivingAreaDelete>



    @POST(value = "insertRfToiletRoomInformation")
    suspend fun insertRfToiletRoomInformation(@Body request: InsertToiletDataReq) : Response<ITLAbDetailsErrorResponse>

    @POST(value = "insertRfNonLivingAreaInformation")
    suspend fun insertRfNonLivingAreaInformation(@Body request: InsertNonLivingReq) : Response<ITLAbDetailsErrorResponse>

    @POST(value = "insertRfIndoorGameDetails")
    suspend fun insertRfIndoorGameDetails(@Body request: IndoorGamesRequest) : Response<ITLAbDetailsErrorResponse>


    @POST(value = "insertResidentialFacilitiesAvailable")
    suspend fun insertResidentialFacilitiesAvailable(@Body request: InsertResidentialFacility) : Response<ITLAbDetailsErrorResponse>



    @POST(value = "insertRFSupportFacilitiesAvailable")
    suspend fun insertRFSupportFacilitiesAvailable(@Body request: InsertSupportFacilitiesReq) : Response<ITLAbDetailsErrorResponse>





//    Ajit Ranjan create 03/Novmber/2025  getRfNonLivingAreaInformation
    @POST(value ="getRfNonLivingAreaInformation")
    suspend fun getRfNonLivingAreaInformation
                (@Body request: LivingRoomListViewRQ) :
            Response<NonAreaInformationRoom>






//    Ajit Ranjan create 04/Novmber/2025  getRfIndoorGameDetails
    @POST(value ="getRfIndoorGameDetails")
    suspend fun getRfIndoorGameDetails
                (@Body request: RFGameRequest) :
            Response<IndoorRFGameResponse>




    //    Ajit Ranjan create 06/Novmber/2025  getResidentialFacilitiesAvailable
    @POST(value ="getResidentialFacilitiesAvailable")
    suspend fun getResidentialFacilitiesAvailable
                (@Body request: RfCommonReq) :
            Response<RFResidintialFacilityResponse>


//    Ajit Ranjan create 07/Novmber/2025  getRFSupportFacilitiesAvailable

    @POST(value = "getRFSupportFacilitiesAvailable")
    suspend fun getRFSupportFacilitiesAvailable(@Body request: RFGameRequest):
            Response<RFSupportFacilitiesAvailableResponse>

    //    Ajit Ranjan create 07/Novmber/2025  getRFSupportFacilitiesAvailable
    @POST(value = "insertRFQteamVerification")
    suspend fun getFinalSubmitInsertRFQteamVerificationData(@Body request: RFQteamVerificationRequest): Response<FinalSubmitRes>

//    Ajit Ranjan create 07/Novmber/2025    insertRFSrlmVerification

    @POST(value = "insertRFSrlmVerification")
    suspend fun getFinalSubmitInsertRFinsertRFSrlmVerificationData(@Body request: RFQteamVerificationRequest): Response<FinalSubmitRes>




//    Ajit Ranjan create 07/Novmber/2025  getRFSRLMVerification

    @POST(value = "getRFSRLMVerification")
    suspend fun getRFSRLMVerification(@Body request: TrainingCenterRequest): Response<RfQTeamListRes>






    @POST(value ="getRFSectionStatus")
    suspend fun getRFSectionStatus
                (@Body request: SectionReq) :
            Response<SectionResponse>




    @POST(value ="insertRFFinalSubmission")
    suspend fun insertRFFinalSubmission
                (@Body request: RfFinalSubmitReq) :
            Response<RfFinalSubmitRes>


    @POST(value ="saveInitialResidentialFacility")
    suspend fun saveInitialResidentialFacility
                (@Body request: AddNewRFReq) :
            Response<AddNewRFRes>

    @POST(value ="getResidentialList")
    suspend fun getResidentialList
                (@Body request: ModifyRfList) :
            Response<ModifyRFRes>


//    Ajit Ranjan create 17/Nov/2025  getToiletCountList
    @POST(value ="getToiletCountList")
    suspend fun getToiletCountList
                (@Body request: ToiletCountListReq) :
            Response<ToiletCountList>

    @POST(value = "insertRfToiletWashRoomDetail")
    suspend fun insertRfToiletWashRoomDetail(@Body request: UrinalWashbasinReq) : Response<ITLAbDetailsErrorResponse>


    @POST(value ="getToiletWashbasinDetails")
    suspend fun getToiletWashbasinDetails
                (@Body request: GetUrinalWashReq) :
            Response<GetUrinalWashRes>



    @POST("ulbList")
    suspend fun getUlbAPI( @Body ulbReq: ULBReq): Response<UlbRes>

    @POST("wardList")
    suspend fun getWardAPI(
        @Body wardReq: WardReq
    ): Response<WardRes>


    @POST
    suspend fun postOnAUAFaceAuthNREGA(
        @Url url: String,
        @Body request: UidaiKycRequest
    ): Response<UidaiResp>


    @POST("onGoingBatchList")
    suspend fun getAttendanceBatchListAPI(
        @Body attendanceBatchListReq: AttendanceBatchListReq
    ): Response<AttendanceBatchRes>



    @POST("onGoingBatchCandidateList")
    suspend fun getAttendanceCandidateListAPI(
        @Body attendanceCandidateListReq: AttendanceCandidateListReq
    ): Response<AttendanceCandidateRes>


    @POST("attandanceCheck")
    suspend fun getAttendanceCheckAPI(
        @Body attendanceCheckReq: AttendanceCheckReq
    ): Response<AttendanceCheckRes>


    @POST("insertAttandance")
    suspend fun insertAttendance(
        @Body attendanceInsertReq: AttendanceInsertReq
    ): Response<AttendanceInsertRes>




    @POST("onGoingBatchFaculty")
    suspend fun getFacultyDetails(
        @Body attendanceCandidateListReq: AttendanceCandidateListReq
    ): Response<FacultyDetailsRes>




    @POST("insertFacultyAttandance")
    suspend fun insertFacultyAttandance(
        @Body insertFacultyAttendance: InsertFacultyAttendance
    ): Response<AttendanceInsertRes>





    @POST("getDDInspectionList")
    suspend fun getDueDiligenceDetails(
        @Body getTcInspectionList: GetTcInspectionList
    ): Response<GetTcInspectionRes>




    @POST("getDueDiligenceTcDetails")
    suspend fun getDueDiligenceTcDetails(
        @Body inspectionTcDetailsReq: InspectionTcDetailsReq
    ): Response<InspectionTcDetailsRes>




    @POST("getInspectionPreviousBatchList")
    suspend fun getInspectionPreviousBatchList(
        @Body inspectionPreviousBatchList: InspectionPreviousBatchList
    ): Response<InspectionPreviousBatchRes>





    @POST("getCandiateForInspection")
    suspend fun getCandidateForPreviousBatch(
        @Body candidatePreviousBatchReq: CandidatePreviousBatchReq
    ): Response<CandidatePreviousBatchRes>



    @POST("getDueDiligenceDetails")
    suspend fun getDueDiligenceDetails(
        @Body request: InspectionRequestBody
    ): Response<BaseResponse<List<DueDiligenceItemResponse>>>


    @POST("getPreviousInspection")
    suspend fun getPreviousInspection(
        @Body request: InspectionRequestBody
    ): Response<BaseResponse<List<PreviousInspectionItemResponse>>>


    @POST("getCandiateInspectionDetails")
    suspend fun getCandidateInspectionDetails(
        @Body request: GetCandidateInspectionRequest
    ): Response<BaseResponse<List<CandidateInspectionDetails>>>

    @POST("saveBatchDataVerification")
    suspend fun saveBatchDataVerification(
        @Body request: SaveBatchVerificationRequest
    ): Response<BaseResponse<List<Nothing>>>

    @POST("getCandidateAssessmentInspection")
    suspend fun getCandidateAssessmentInspection(
        @Body request: GetCandidateAssessmentInspectionRequest
    ): Response<BaseResponse<List<CandidateAssessmentInspectionDetails>>>


    @POST("saveCandiateAssessmentInspection")
    suspend fun saveCandidateAssessmentInspection(
        @Body request: SaveCandidateAssessmentInspectionRequest
    ): Response<BaseResponse<List<Nothing>>>


    @POST("assessmentStatusForInspection")
    suspend fun getAssessmentStatusForInspection(
        @Body request: AssessmentStatusInspectionRequest
    ): Response<BaseResponse<List<AssessmentStatusResponse>>>

    @POST("getInspectionOngoingBatchList")
    suspend fun getInspectionOngoingBatchList(
        @Body inspectionPreviousBatchList: InspectionPreviousBatchList
    ): Response<InspectionPreviousBatchRes>


    @POST("getTrainerClassObservationList")
    suspend fun getSubjectList(
        @Body subjectReq: SubjectReq
    ): Response<SubjectListRes>


    @POST("deleteTrainerClassObservationSubject")
    suspend fun deleteSubjectItem(
        @Body deleteReq: SubjectDeleteReq
    ): Response<SubjectDeleteRes>

    @POST("getOngoingBatchCandiate")
    suspend fun getOngoingBatchCandiate(
        @Body candidatePreviousBatchReq: CandidatePreviousBatchReq
    ): Response<CandidatePreviousBatchRes>


    @POST("getCandiateRecords")
    suspend fun getCandidateImageRecords(
        @Body getImageListReq: GetImageListReq
    ): Response<GetImageListRes>


    @POST("saveCandiateRecords")
    suspend fun saveCandidateBasicRecords(
        @Body ongoingSubmitBasicRecordsReq: OngoingSubmitBasicRecordsReq
    ): Response<InsertRes>


    @POST("getCandiateTodayAttendanceStatus")
    suspend fun getCandidateTodayAttendanceStatus(
        @Body getAttendanceDetailsReq: GetAttendanceDetailsReq
    ): Response<GetAttendanceDetailsRes>

    @POST("getCandiateRecordsVerification")
    suspend fun getCandidateRecordsVerification(
        @Body request: GetCandidateRecordsVerificationRequest
    ): Response<BaseResponse<List<CandidateRecordsVerificationDetails>>>

    @POST("getInspectionSectionStatus")
    suspend fun getInspectionSectionStatus(
        @Body request: GetInspectionSectionStatusRequest
    ): Response<BaseResponse<List<InspectionSectionStatusResponse>>>

    @POST("getDistributedLearningMaterialInspection")
    suspend fun getDistributedLearningMaterialInspection(
        @Body request: GetDistributedLearningMaterialInspectionRequest
    ): Response<BaseResponse<List<DistributedLearningMaterialInspectionResponse>>>


    @POST("saveDistributedLearningMaterialInspection")
    suspend fun saveDistributedLearningMaterialInspection(
        @Body request: SaveDistributedLearningMaterialInspectionRequest
    ): Response<BaseResponse<List<Nothing>>>


    @POST("getEntitlementsDistributionInspection")
    suspend fun getEntitlementsDistributionInspection(
        @Body request: GetEntitlementsDistributionInspectionRequest
    ): Response<BaseResponse<List<EntitlementsDistributionInspectionResponse>>>


    @POST("saveEntitlementsDistributionInspection")
    suspend fun saveEntitlementsDistributionInspection(
        @Body request: SaveEntitlementsDistributionInspectionRequest
    ): Response<BaseResponse<List<Nothing>>>


    @POST("getResidentialFacilityVerification")
    suspend fun getResidentialFacilityVerification(
        @Body request: GetResidentialFacilityVerificationRequest
    ): Response<BaseResponse<List<ResidentialFacilityVerificationResponse>>>


    @POST("saveResidentialFacilityVerification")
    suspend fun saveResidentialFacilityVerification(
        @Body request: SaveResidentialFacilityVerificationRequest
    ): Response<BaseResponse<List<Nothing>>>

    @POST("getTrainersForInspection")
    suspend fun getTrainersListInspection(
        @Body trainerListReq: TrainerListReq
    ): Response<TrainerListRes>


    @POST("getTrainerAttendanceInspection")
    suspend fun getTrainerAttendanceInspection(
        @Body request: GetTrainerAttendanceInspectionRequest
    ): Response<BaseResponse<List<TrainerAttendanceInspectionResponse>>>


    @POST("saveTrainerAttendanceInspection")
    suspend fun saveTrainerAttendanceInspection(
        @Body request: SaveTrainerAttendanceInspectionRequest
    ): Response<BaseResponse<List<Nothing>>>


    @POST("saveTrainerClassObservationInspection")
    suspend fun saveTrainerClassObservationInspection(
        @Body request: saveTrainerClassObservationInspectionReq
    ): Response<TrainerClassObservationResponse>

    @POST("getInspectionStandardForm")
    suspend fun getInspectionStandardForm(

        @Body request: GetInspectionStandardFormRequest

    ): Response<BaseResponse<List<InspectionStandardFormDto>>>

    @POST("saveInspectionStandardForm")
    suspend fun saveInspectionStandardForm(

        @Body request: SaveInspectionStandardFormRequest

    ): Response<SaveInspectionStandardFormResponse>



    @POST("getPreviousDueDiligenceObservation")
    suspend fun getPreviousDueDiligenceQuestion(
        @Body getPrevDueQueList: GetPrevDueQueList
    ): Response<GetPrevDueQueListRes>


    @POST("savePreviousDueDiligenceVerification")
    suspend fun savePreviousDueDiligenceQues(
        @Body savePreDDQueReq: SavePreDDQueReq
    ): Response<InsertRes>


    @POST("getPreviousDueDiligenceVerification")
    suspend fun getSavedPreviousDueDiligenceQue(
        @Body getDDSaveDataReq: GetDDSaveDataReq
    ): Response<GetDDSaveDataRes>


    @POST("getCandiateAttendanceInspectionDetails")
    suspend fun getCandidateAttendanceInspection(

        @Body request: GetCandidateAttendanceInspectionRequest

    ): Response<BaseResponse<List<CandidateAttendanceInspectionResponse>>>

    @POST("saveCandiateAttendanceInspection")
    suspend fun saveCandidateAttendanceInspection(

        @Body request: SaveCandidateAttendanceInspectionRequest

    ): Response<InsertRes>


}