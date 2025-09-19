package com.deendayalproject.network

import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TrainingCenterResponse
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

}



