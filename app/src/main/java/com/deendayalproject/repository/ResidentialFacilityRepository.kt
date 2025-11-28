package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.DeleteLivingRoomList
import com.deendayalproject.model.request.GetUrinalWashReq
import com.deendayalproject.model.request.IndoorGamesRequest
import com.deendayalproject.model.request.InsertLivingAreaReq
import com.deendayalproject.model.request.InsertNonLivingReq
import com.deendayalproject.model.request.InsertResidentialFacility
import com.deendayalproject.model.request.InsertRfInfraDetaiReq
import com.deendayalproject.model.request.InsertSupportFacilitiesReq
import com.deendayalproject.model.request.InsertToiletDataReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.LivingRoomReq
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.request.RfCommonReq
import com.deendayalproject.model.request.RfLivingAreaInformationRQ
import com.deendayalproject.model.request.SectionReq
import com.deendayalproject.model.request.ToiletCountListReq
import com.deendayalproject.model.request.ToiletDeleteList
import com.deendayalproject.model.request.ToiletRoomInformationReq
import com.deendayalproject.model.request.ToiletRoomReq
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.insertRfBasicInfoReq
import com.deendayalproject.model.response.GetUrinalWashRes
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.model.response.IndoorRFGameResponse
import com.deendayalproject.model.response.InfrastructureDetailsandCompliancesRFQT
import com.deendayalproject.model.response.LivingAreaDelete
import com.deendayalproject.model.response.LivingAreaListRes
import com.deendayalproject.model.response.LivingRoomListViewRes
import com.deendayalproject.model.response.NonAreaInformationRoom
import com.deendayalproject.model.response.RFResidintialFacilityResponse
import com.deendayalproject.model.response.RFSupportFacilitiesAvailableResponse
import com.deendayalproject.model.response.ResidentialFacilityQTeam
import com.deendayalproject.model.response.RfListResponse
import com.deendayalproject.model.response.RfLivingAreaInformationResponse
import com.deendayalproject.model.response.RfQTeamListRes
import com.deendayalproject.model.response.SectionResponse
import com.deendayalproject.model.response.ToiletCountList
import com.deendayalproject.model.response.ToiletListRes
import com.deendayalproject.model.response.ToiletRoomInformationViewRes
import com.deendayalproject.model.response.ToiletViewRes
import com.deendayalproject.network.ApiService

class ResidentialFacilityRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun fetchRfList(request: TrainingCenterRequest, token: String): Result<RfListResponse> =
        safeApiCallWithToken(token) {
            apiService.getResidentialFacilitiesList(request)
        }

    suspend fun insertRfBasicInformation(request: insertRfBasicInfoReq, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertRfBasicInformation(request)
        }

    suspend fun insertRfInfraDetailsAndComliance(request: InsertRfInfraDetaiReq, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertRfInfraDetailsAndComliance(request)
        }

    suspend fun insertRfToiletRoomInformation(request: InsertToiletDataReq, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertRfToiletRoomInformation(request)
        }

    suspend fun insertRfNonLivingAreaInformation(request: InsertNonLivingReq): Result<ITLAbDetailsErrorResponse> =
        safeApiCall {
            apiService.insertRfNonLivingAreaInformation(request)
        }

    suspend fun insertRfIndoorGameDetails(request: IndoorGamesRequest): Result<ITLAbDetailsErrorResponse> =
        safeApiCall {
            apiService.insertRfIndoorGameDetails(request)
        }

    suspend fun insertResidentialFacilitiesAvailable(request: InsertResidentialFacility): Result<ITLAbDetailsErrorResponse> =
        safeApiCall {
            apiService.insertResidentialFacilitiesAvailable(request)
        }

    suspend fun insertRFSupportFacilitiesAvailable(request: InsertSupportFacilitiesReq): Result<ITLAbDetailsErrorResponse> =
        safeApiCall {
            apiService.insertRFSupportFacilitiesAvailable(request)
        }

    suspend fun insertRfLivingAreaInformation(request: InsertLivingAreaReq, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertRfLivingAreaInformation(request)
        }

    suspend fun fetchResidentialFacilityQTeamist(request: ResidentialFacilityQTeamRequest, token: String): Result<RfQTeamListRes> =
        safeApiCallWithToken(token) {
            apiService.getRFQteamVerificationList(request)
        }

    suspend fun getTRfBasicInfo(request: RfCommonReq): Result<ResidentialFacilityQTeam> =
        safeApiCall {
            apiService.getRfBasicInfoo(request)
        }

    suspend fun getCompliancesRFQTReqRFQT(request: CompliancesRFQTReq): Result<InfrastructureDetailsandCompliancesRFQT> =
        safeApiCall {
            apiService.getgetCompliancesRFQTReqRFQT(request)
        }

    suspend fun getRfLivingAreaInformation(request: RfLivingAreaInformationRQ): Result<RfLivingAreaInformationResponse> =
        safeApiCall {
            apiService.getRfLivingAreaInformation(request)
        }

    suspend fun getRflivingRoomListView(request: LivingRoomListViewRQ): Result<LivingRoomListViewRes> =
        safeApiCall {
            apiService.getlivingRoomListView(request)
        }

    suspend fun getToiletRoomListView(request: ToiletRoomInformationReq): Result<ToiletViewRes> =
        safeApiCall {
            apiService.getToiletRoomListView(request)
        }

    suspend fun getToiletRoomInformation(request: ToiletRoomReq): Result<ToiletRoomInformationViewRes> =
        safeApiCall {
            apiService.ToiletRoomInformation(request)
        }

    suspend fun getRfLivingRoomListView(request: LivingRoomReq): Result<LivingAreaListRes> =
        safeApiCall {
            apiService.getRfLivingRoomListView(request)
        }

    suspend fun deleteLivingRoom(request: DeleteLivingRoomList): Result<LivingAreaDelete> =
        safeApiCall {
            apiService.deleteLivingRoom(request)
        }

    suspend fun getRfToiletListView(request: LivingRoomReq): Result<ToiletListRes> =
        safeApiCall {
            apiService.getRfToiletListView(request)
        }

    suspend fun toiletSectionListView(request: LivingRoomReq): Result<ToiletListRes> =
        safeApiCall {
            apiService.toiletSectionListView(request)
        }

    suspend fun deleteToiletRoom(request: ToiletDeleteList): Result<LivingAreaDelete> =
        safeApiCall {
            apiService.deleteToiletRoom(request)
        }

    suspend fun getRfNonLivingAreaInformation(request: LivingRoomListViewRQ): Result<NonAreaInformationRoom> =
        safeApiCall {
            apiService.getRfNonLivingAreaInformation(request)
        }

    suspend fun getRfInGaDetails(request: RFGameRequest): Result<IndoorRFGameResponse> =
        safeApiCall {
            apiService.getRfIndoorGameDetails(request)
        }

    suspend fun getResidentialFacilitiesAvailable(request: RfCommonReq): Result<RFResidintialFacilityResponse> =
        safeApiCall {
            apiService.getResidentialFacilitiesAvailable(request)
        }

    suspend fun getRFSupportFacilitiesAvailable(request: RFGameRequest): Result<RFSupportFacilitiesAvailableResponse> =
        safeApiCall {
            apiService.getRFSupportFacilitiesAvailable(request)
        }

    suspend fun getRFSectionStatus(request: SectionReq): Result<SectionResponse> =
        safeApiCall {
            apiService.getRFSectionStatus(request)
        }

    suspend fun getToiletWashbasinDetails(request: GetUrinalWashReq): Result<GetUrinalWashRes> =
        safeApiCall {
            apiService.getToiletWashbasinDetails(request)
        }

    suspend fun getToiletCountList(request: ToiletCountListReq): Result<ToiletCountList> =
        safeApiCall {
            apiService.getToiletCountList(request)
        }
}