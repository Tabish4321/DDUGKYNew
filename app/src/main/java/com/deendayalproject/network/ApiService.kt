package com.deendayalproject.network

import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.DLRequest
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
import com.deendayalproject.model.request.ITLabDetailsRequest
import com.deendayalproject.model.request.InsertLivingAreaReq
import com.deendayalproject.model.request.InsertRfInfraDetaiReq
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.OfficeRoomDetailsRequest
import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.request.RfLivingAreaInformationResponseRQ
import com.deendayalproject.model.request.StateRequest
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
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.request.insertRfBasicInfoReq
import com.deendayalproject.model.response.AcademicNonAcademicResponse
import com.deendayalproject.model.response.AllRoomDetailResponse
import com.deendayalproject.model.response.BlockResponse
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.CommonEquipmentRes
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.DistrictResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.ElectricalWireRes
import com.deendayalproject.model.response.FinalSubmitRes
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.GpResponse
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.model.response.InfrastructureDetailsandCompliancesRFQT
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.IpEnableRes
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.ResidentialFacilityQTeam
import com.deendayalproject.model.response.RfListResponse
import com.deendayalproject.model.response.RfLivingAreaInformationResponse
import com.deendayalproject.model.response.SectionStatusRes
import com.deendayalproject.model.response.SignageInfo
import com.deendayalproject.model.response.StandardFormResponse
import com.deendayalproject.model.response.StateResponse
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
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.model.response.VillageRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("modulenforms")
    suspend fun fetchModules(@Body request: ModulesRequest): Response<ModuleResponse>

    @POST("getTrainingCenterList")
    suspend fun getTrainingCenterList(@Body request: TrainingCenterRequest): Response<TrainingCenterResponse>



    @POST(value ="getTrainingCenterVerificationList")
    suspend fun getQTeamTrainingList(@Body request: TrainingCenterRequest) : Response<TrainingCenterResponse>


    @POST(value ="getTrainingCenterVerificationSRLM")
    suspend fun getTrainingCenterVerificationSRLM(@Body request: TrainingCenterRequest) : Response<TrainingCenterResponse>



    @POST(value ="getResidentialFacilitiesList")
    suspend fun getResidentialFacilitiesList(@Body request: TrainingCenterRequest) : Response<RfListResponse>




    @POST(value = "insertCCTVCompliance")
    suspend fun insertCCTVCompliance(@Body request: CCTVComplianceRequest): Response<CCTVComplianceResponse>


    @POST(value = "insertTcElectricWiringStandard")
    suspend fun insertTcElectricWiringStandard(@Body request : ElectricalWiringRequest) : Response<ElectircalWiringReponse>


    @POST(value ="insertTcGeneralDetails")
    suspend fun insertTcGeneralDetails(@Body request: InsertTcGeneralDetailsRequest) : Response<InsertTcGeneralDetailsResponse>


    @POST(value = "insertTcBasicInfo")
    suspend fun insertTcBasicInfo(@Body request: TcBasicInfoRequest) : Response<InsertTcBasicInfoResponse>


    @POST(value = "insertTcSignagesInfoBoard")
    suspend fun insertTcSignagesInfoBoard(@Body request: TcSignagesInfoBoardRequest) : Response<TcSignagesInfoBoardResponse>

    @POST(value = "insertTcAvailabilitySupportInfra")
    suspend fun insertTcAvailabilitySupportInfra(@Body request: TcAvailabilitySupportInfraRequest) : Response<TcAvailabilitySupportInfraResponse>

    @POST(value = "insertTcCommonEquipment")
    suspend fun  insertTcCommonEquipment(@Body request : TcCommonEquipmentRequest) : Response<TcCommonEquipmentResponse>

    @POST(value = "insertTcDescriptionOtherAreas")
    suspend fun  insertTcDescriptionOtherAreas(@Body request: TcDescriptionOtherAreasRequest) : Response<TcDescriptionOtherAreasResponse>



    @POST(value ="getTrainerCenterInfo")
    suspend fun getTrainerCenterInfo(@Body request: TrainingCenterInfo) : Response<TrainingCenterInfoRes>




    @POST(value ="getTCTrainerAndOtherStaffsList")
    suspend fun getTcStaffDetails(@Body request: TrainingCenterInfo) : Response<TcStaffAndTrainerResponse>



    @POST(value ="getTrainerCenterInfra")
    suspend fun getTrainerCenterInfra(@Body request: TrainingCenterInfo) : Response<TcInfraResponse>


    @POST(value ="insertTcToiletsWashBasins")
    suspend fun insertTcToiletsWashBasins(@Body request: ToiletDetailsRequest) : Response<ToiletDetailsErrorResponse>




    @POST(value ="getTcAcademicNonAcademicArea")
    suspend fun getTcAcademicNonAcademicArea(@Body request: TrainingCenterInfo) : Response<TcAcademiaNonAcademiaRes>



    @POST(value ="getTcToiletWashBasin")
    suspend fun getTcToiletWashBasin(@Body request: TrainingCenterInfo) : Response<ToiletResponse>



    @POST(value ="getDescriptionOtherArea")
    suspend fun getDescriptionOtherArea(@Body request: TrainingCenterInfo) : Response<DescOtherAreaRes>


    @POST(value ="getTeachingLearningMaterial")
    suspend fun getTeachingLearningMaterial(@Body request: TrainingCenterInfo) : Response<TeachingLearningRes>


    @POST(value ="getGeneralDetails")
    suspend fun getGeneralDetails(@Body request: TrainingCenterInfo) : Response<GeneralDetails>




    @POST(value ="getElectricalWiringStandard")
    suspend fun getElectricalWiringStandard(@Body request: TrainingCenterInfo) : Response<ElectricalWireRes>


    @POST(value ="getSignagesAndInfoBoard")
    suspend fun getSignagesAndInfoBoard(@Body request: TrainingCenterInfo) : Response<SignageInfo>


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


    @POST(value ="insertQTeamVerification")
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
    suspend fun insertRfBasicInformation(@Body request: insertRfBasicInfoReq) : Response<ITLAbDetailsErrorResponse>



    @POST(value = "insertRfInfraDetailsAndComliance")
    suspend fun insertRfInfraDetailsAndComliance(@Body request: InsertRfInfraDetaiReq) : Response<ITLAbDetailsErrorResponse>


    @POST(value = "insertRfLivingAreaInformation")
    suspend fun insertRfLivingAreaInformation(@Body request: InsertLivingAreaReq) : Response<ITLAbDetailsErrorResponse>

    //    ResidentialFacilityQTeamRequest Ajit Ranjan  16/10/2025
    @POST(value ="getRFQteamVerificationList")
    suspend fun getRFQteamVerificationList(@Body request: ResidentialFacilityQTeamRequest) : Response<TrainingCenterResponse>



    //    GetRfBasicInformation AjitRanjan 17/10/2025
    @POST(value ="getRfBasicInformation")
    suspend fun getRfBasicInfoo(@Body request: TrainingCenterInfo) : Response<ResidentialFacilityQTeam>


//    Ajit Ranjan create 21/October/2026  CompliancesRFQTReqRFQT


    @POST(value ="getRfInfraDetailsAndComliance")
    suspend fun getgetCompliancesRFQTReqRFQT(@Body request: CompliancesRFQTReq) : Response<InfrastructureDetailsandCompliancesRFQT>

//    Ajit Ranjan create 24/October/2026  getRfLivingAreaInformation
    @POST(value ="getRfLivingAreaInformation")
    suspend fun getRfLivingAreaInformation(@Body request: RfLivingAreaInformationResponseRQ) : Response<RfLivingAreaInformationResponse>


}