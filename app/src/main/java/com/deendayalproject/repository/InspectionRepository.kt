package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
import com.deendayalproject.network.ApiService

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


    suspend fun getCandidateForInspection  (candidatePreviousBatchReq: CandidatePreviousBatchReq, header :String): Result<CandidatePreviousBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getCandidateForInspection(candidatePreviousBatchReq)
        }










}