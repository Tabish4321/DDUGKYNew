package com.deendayalproject.repository

import PreviousInspectionItemResponse
import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.base.BaseResponse
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.request.SaveBatchVerificationRequest
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.DueDiligenceItemResponse
import com.deendayalproject.model.response.GetAttendanceDetailsRes
import com.deendayalproject.model.response.GetImageListRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.InsertRes
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
import com.deendayalproject.model.uistate.CandidateInspectionDto
import com.deendayalproject.model.uistate.GetCandidateInspectionRequest
import com.deendayalproject.network.ApiService
import com.deendayalproject.util.AppUtil

class InspectionRepository(context: Context) : BaseRepository<ApiService>(context) {


        suspend fun getDueDiligenceDetails  (getTcInspectionList: GetTcInspectionList, header :String): Result<GetTcInspectionRes> =
        safeApiCallWithToken(token = header) {
            apiService.getDueDiligenceDetails(getTcInspectionList)
        }



    suspend fun getDueDiligenceTcDetails  (inspectionTcDetailsReq: InspectionTcDetailsReq, header :String): Result<InspectionTcDetailsRes> =
        safeApiCallWithToken(token = header) {
            apiService.getDueDiligenceTcDetails(inspectionTcDetailsReq)
        }



    suspend fun getInspectionPreviousBatchList  (inspectionPreviousBatchList: InspectionPreviousBatchList, header :String): Result<InspectionPreviousBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getInspectionPreviousBatchList(inspectionPreviousBatchList)
        }


    suspend fun getCandidateForPreviousBatch  (candidatePreviousBatchReq: CandidatePreviousBatchReq, header :String): Result<CandidatePreviousBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getCandidateForPreviousBatch(candidatePreviousBatchReq)
        }



    suspend fun getPreviousInspection(
        request: InspectionRequestBody
    ): Result<List<PreviousInspectionItemResponse>> =
        safeApiCallN {
            apiService.getPreviousInspection(request)
        }
//        safeApiCallWithToken(token = AppUtil.getSavedTokenPreference(context)) {
//            apiService.getPreviousInspection(request)
//        }


    suspend fun getDueDiligence(
        request: InspectionRequestBody
    ): Result<List<DueDiligenceItemResponse>> =
        safeApiCallN {
            apiService.getDueDiligenceDetails(request)
        }






    suspend fun getInspectionOngoingBatchList  (inspectionPreviousBatchList: InspectionPreviousBatchList, header :String): Result<InspectionPreviousBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getInspectionOngoingBatchList(inspectionPreviousBatchList)
        }


    suspend fun getOngoingBatchCandiate  (candidatePreviousBatchReq: CandidatePreviousBatchReq, header :String): Result<CandidatePreviousBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getOngoingBatchCandiate(candidatePreviousBatchReq)
        }





    suspend fun getCandidateImageRecords  (getImageListReq: GetImageListReq, header :String): Result<GetImageListRes> =
        safeApiCallWithToken(token = header) {
            apiService.getCandidateImageRecords(getImageListReq)
        }




    suspend fun saveCandidateBasicRecords  (ongoingSubmitBasicRecordsReq: OngoingSubmitBasicRecordsReq, header :String): Result<InsertRes> =
        safeApiCallWithToken(token = header) {
            apiService.saveCandidateBasicRecords(ongoingSubmitBasicRecordsReq)
        }




    suspend fun getCandidateTodayAttendanceStatus  (getAttendanceDetailsReq: GetAttendanceDetailsReq): Result<GetAttendanceDetailsRes> =
        safeApiCallWithToken(token = "") {
            apiService.getCandidateTodayAttendanceStatus(getAttendanceDetailsReq)
        }




}