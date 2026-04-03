package com.deendayalproject.repository

import android.content.Context
import com.bumptech.glide.load.engine.Resource
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.AttendanceBatchListReq
import com.deendayalproject.model.request.AttendanceCandidateListReq
import com.deendayalproject.model.request.AttendanceCheckReq
import com.deendayalproject.model.request.AttendanceInsertReq
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.FansCountReq
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.InsertFacultyAttendance
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.ULBReq
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.request.WardReq
import com.deendayalproject.model.response.AttendanceBatchRes
import com.deendayalproject.model.response.AttendanceCandidateRes
import com.deendayalproject.model.response.AttendanceCheckRes
import com.deendayalproject.model.response.AttendanceInsertRes
import com.deendayalproject.model.response.BlockResponse
import com.deendayalproject.model.response.DistrictResponse
import com.deendayalproject.model.response.FacultyDetailsRes
import com.deendayalproject.model.response.FansCountRes
import com.deendayalproject.model.response.GpResponse
import com.deendayalproject.model.response.StateResponse
import com.deendayalproject.model.response.UlbRes
import com.deendayalproject.model.response.VillageRes
import com.deendayalproject.model.response.WardRes
import com.deendayalproject.network.ApiService
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.uidai.ekyc.UidaiResp
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class LocationRepository(context: Context) : BaseRepository<ApiService>(context) {

    suspend fun getStateList(request: StateRequest, token: String): Result<StateResponse> =
        safeApiCallWithToken(token) {
            apiService.getStateList(request)
        }

    suspend fun getDistrictList(request: DistrictRequest, token: String): Result<DistrictResponse> =
        safeApiCallWithToken(token) {
            apiService.getDistrictList(request)
        }

    suspend fun getBlockList(request: BlockRequest, token: String): Result<BlockResponse> =
        safeApiCallWithToken(token) {
            apiService.getBlockList(request)
        }

    suspend fun getGpList(request: GpRequest, token: String): Result<GpResponse> =
        safeApiCallWithToken(token) {
            apiService.getGPList(request)
        }

    suspend fun getVillageList(request: VillageReq, token: String): Result<VillageRes> =
        safeApiCallWithToken(token) {
            apiService.getVillageList(request)
        }

    suspend fun getUlbAPI(ulbReq: ULBReq, header :String): Result<UlbRes> =
      safeApiCallWithToken(token = header) {
            apiService.getUlbAPI(ulbReq)
        }

    suspend fun getWardAPI(wardReq: WardReq, header :String): Result<WardRes> =
        safeApiCallWithToken(token = header) {
            apiService.getWardAPI(wardReq)
        }


    suspend fun getAttendanceBatchListAPI(attendanceBatchListReq: AttendanceBatchListReq, header :String): Result<AttendanceBatchRes> =
        safeApiCallWithToken(token = header) {
            apiService.getAttendanceBatchListAPI(attendanceBatchListReq)
        }




    suspend fun getAttendanceCandidateListAPI(attendanceCandidateListReq: AttendanceCandidateListReq, header :String): Result<AttendanceCandidateRes> =
        safeApiCallWithToken(token = header) {
            apiService.getAttendanceCandidateListAPI(attendanceCandidateListReq)
        }



    suspend fun getAttendanceCheckAPI(attendanceCheckReq: AttendanceCheckReq, header :String): Result<AttendanceCheckRes> =
        safeApiCallWithToken(token = header) {
            apiService.getAttendanceCheckAPI(attendanceCheckReq)
        }


    suspend fun insertAttendance(attendanceInsertReq: AttendanceInsertReq, header :String): Result<AttendanceInsertRes> =
        safeApiCallWithToken(token = header) {
            apiService.insertAttendance(attendanceInsertReq)
        }


    suspend fun insertFacultyAttandance(insertFacultyAttendance: InsertFacultyAttendance, header :String): Result<AttendanceInsertRes> =
        safeApiCallWithToken(token = header) {
            apiService.insertFacultyAttandance(insertFacultyAttendance)
        }



    suspend fun getFacultyDetails(attendanceCandidateListReq: AttendanceCandidateListReq, header :String): Result<FacultyDetailsRes> =
        safeApiCallWithToken(token = header) {
            apiService.getFacultyDetails(attendanceCandidateListReq)
        }





}