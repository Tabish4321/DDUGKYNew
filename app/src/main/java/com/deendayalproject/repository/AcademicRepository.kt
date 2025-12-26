package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.DLRequest
import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
import com.deendayalproject.model.request.ITLabDetailsRequest
import com.deendayalproject.model.request.OfficeRoomDetailsRequest
import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
import com.deendayalproject.model.request.SubmitOfficeCumCounsellingRoomDetailsRequest
import com.deendayalproject.model.request.TCDLRequest
import com.deendayalproject.model.request.TCITLDomainLabDetailsRequest
import com.deendayalproject.model.request.TCRRequest
import com.deendayalproject.model.response.AcademicNonAcademicResponse
import com.deendayalproject.model.response.AllRoomDetailResponse
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.network.ApiService
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.uidai.ekyc.UidaiResp

class AcademicRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun submitDesriptionAcademicNonDataToServer(request: AcademicNonAcademicArea, token: String): Result<AcademicNonAcademicResponse> =
        safeApiCallWithToken(token) {
            apiService.getTcAcademicNonAcademic(request)
        }

    suspend fun submitITLabDataToServer(request: ITLabDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertITLabBasicInfo(request)
        }

    suspend fun submitOfficeCumCounsellingroomDataToServer(request: SubmitOfficeCumCounsellingRoomDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertOfficeCumCounsellingroomBasicInfo(request)
        }

    suspend fun submitReceptionAreaDataToServer(request: ReceptionAreaRoomDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertReceptionAreaBasicInfo(request)
        }

    suspend fun submitOfficeRoomDataToServer(request: OfficeRoomDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertOfficeroomBasicInfo(request)
        }

    suspend fun submitItComeDomainlabToServer(request: ITComeDomainLabDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertItComeDomainlabBasicInfo(request)
        }

    suspend fun submitTheoryCumITLabToServer(request: TCITLDomainLabDetailsRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.inserttheorycumitlabBasicInfo(request)
        }

    suspend fun submitTheoryCumDomainLabToServer(request: TCDLRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.inserttheorycumdomainlabBasicInfo(request)
        }

    suspend fun submitDomainLabToServer(request: DLRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.insertDomainLabBasicInfo(request)
        }

    suspend fun submitTheoryClassRoomToServer(request: TCRRequest, token: String): Result<ITLAbDetailsErrorResponse> =
        safeApiCallWithToken(token) {
            apiService.inserttheoryClassroomBasicInfo(request)
        }

    suspend fun getAcademicRoomDetails(request: AllRoomDetaisReques): Result<AllRoomDetailResponse> =
        safeApiCall {
            apiService.getAcademicRoomDetails(request)
        }

    suspend fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest): Result<UidaiResp> =
        safeApiCall {
            apiService.postOnAUAFaceAuthNREGA(url,uidaiKycRequest)        }


}