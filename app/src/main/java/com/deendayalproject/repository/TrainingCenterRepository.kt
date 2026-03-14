package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.CommonEquipmentRes
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.ElectricalWireRes
import com.deendayalproject.model.response.FinalSubmitRes
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.IpEnableRes
import com.deendayalproject.model.response.SectionStatusRes
import com.deendayalproject.model.response.SignageInfo
import com.deendayalproject.model.response.StandardFormResponse
import com.deendayalproject.model.response.SupportInfrastructureResponse
import com.deendayalproject.model.response.TcAcademiaNonAcademiaRes
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.TeachingLearningRes
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.network.ApiService

class TrainingCenterRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun fetchTrainingCenters(
        request: TrainingCenterRequest,
        token: String
    ): Result<TrainingCenterResponse> = safeApiCallWithToken(token) {
        apiService.getTrainingCenterList(request)
    }

    suspend fun fetchQTeamTrainingList(request: TrainingCenterRequest, token: String): Result<TrainingCenterResponse> =
        safeApiCallWithToken(token) {
            apiService.getQTeamTrainingList(request)
        }

    suspend fun fetchSrlmTeamTrainingList(request: TrainingCenterRequest, token: String): Result<TrainingCenterResponse> =
        safeApiCallWithToken(token) {
            apiService.getTrainingCenterVerificationSRLM(request)
        }

    suspend fun getTrainerCenterInfo(request: TrainingCenterInfo): Result<TrainingCenterInfoRes> =
        safeApiCall {
            apiService.getTrainerCenterInfo(request)
        }

    suspend fun getTcStaffDetails(request: TrainingCenterInfo): Result<TcStaffAndTrainerResponse> =
        safeApiCall {
            apiService.getTcStaffDetails(request)
        }

    suspend fun getTrainerCenterInfra(request: TrainingCenterInfo): Result<TcInfraResponse> =
        safeApiCall {
            apiService.getTrainerCenterInfra(request)
        }

    suspend fun getTcAcademicNonAcademicArea(request: TrainingCenterInfo): Result<TcAcademiaNonAcademiaRes> =
        safeApiCall {
            apiService.getTcAcademicNonAcademicArea(request)
        }

    suspend fun getTcToiletWashBasin(request: TrainingCenterInfo): Result<ToiletResponse> =
        safeApiCall {
            apiService.getTcToiletWashBasin(request)
        }

    suspend fun getDescriptionOtherArea(request: TrainingCenterInfo): Result<DescOtherAreaRes> =
        safeApiCall {
            apiService.getDescriptionOtherArea(request)
        }

    suspend fun getTeachingLearningMaterial(request: TrainingCenterInfo): Result<TeachingLearningRes> =
        safeApiCall {
            apiService.getTeachingLearningMaterial(request)
        }

    suspend fun getGeneralDetails(request: TrainingCenterInfo): Result<GeneralDetails> =
        safeApiCall {
            apiService.getGeneralDetails(request)
        }

    suspend fun getElectricalWiringStandard(request: TrainingCenterInfo): Result<ElectricalWireRes> =
        safeApiCall {
            apiService.getElectricalWiringStandard(request)
        }

    suspend fun getSignagesAndInfoBoard(request: TrainingCenterInfo): Result<SignageInfo> =
        safeApiCall {
            apiService.getSignagesAndInfoBoard(request)
        }

    suspend fun getIpEnabledcamera(request: TrainingCenterInfo): Result<IpEnableRes> =
        safeApiCall {
            apiService.getIpEnabledcamera(request)
        }

    suspend fun getCommonEquipment(request: TrainingCenterInfo): Result<CommonEquipmentRes> =
        safeApiCall {
            apiService.getCommonEquipment(request)
        }

    suspend fun getAvailabilitySupportInfra(request: TrainingCenterInfo): Result<SupportInfrastructureResponse> =
        safeApiCall {
            apiService.getAvailabilitySupportInfra(request)
        }

    suspend fun getAvailabilityStandardForms(request: TrainingCenterInfo): Result<StandardFormResponse> =
        safeApiCall {
            apiService.getAvailabilityStandardForms(request)
        }

    suspend fun getFinalSubmitData(request: TrainingCenterInfo): Result<FinalSubmitRes> =
        safeApiCall {
            apiService.getFinalSubmitData(request)
        }

    suspend fun getSectionsStatus(request: TrainingCenterInfo): Result<SectionStatusRes> =
        safeApiCall {
            apiService.getSectionsStatus(request)
        }
}