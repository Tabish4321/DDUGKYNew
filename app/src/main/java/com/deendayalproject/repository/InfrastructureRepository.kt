package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.ToiletDetailsErrorResponse
import com.deendayalproject.network.ApiService


class InfrastructureRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun submitCCTVDataToServer(request: CCTVComplianceRequest, token: String): Result<CCTVComplianceResponse> =
        safeApiCallWithToken(token) {
            apiService.insertCCTVCompliance(request)
        }

    suspend fun submitWiringDataToServer(request: ElectricalWiringRequest, token: String): Result<ElectircalWiringReponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcElectricWiringStandard(request)
        }

    suspend fun submitGeneralDataToServer(request: InsertTcGeneralDetailsRequest, token: String): Result<InsertTcGeneralDetailsResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcGeneralDetails(request)
        }

    suspend fun submitWashbsinDataToServer(request: ToiletDetailsRequest, token: String): Result<ToiletDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcToiletsWashBasins(request)
        }

    suspend fun submitTcBasicDataToServer(request: TcBasicInfoRequest, token: String): Result<InsertTcBasicInfoResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcBasicInfo(request)
        }

    suspend fun submitSignagesBoardsDataToServer(request: TcSignagesInfoBoardRequest, token: String): Result<TcSignagesInfoBoardResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcSignagesInfoBoard(request)
        }

    suspend fun submitInfraDataToServer(request: TcAvailabilitySupportInfraRequest, token: String): Result<TcAvailabilitySupportInfraResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcAvailabilitySupportInfra(request)
        }

    suspend fun submitCommonEquipmentDataToServer(request: TcCommonEquipmentRequest, token: String): Result<TcCommonEquipmentResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcCommonEquipment(request)
        }

    suspend fun submitDescDataToServer(request: TcDescriptionOtherAreasRequest, token: String): Result<TcDescriptionOtherAreasResponse> =
        safeApiCallWithToken(token) {
            apiService.insertTcDescriptionOtherAreas(request)
        }
}