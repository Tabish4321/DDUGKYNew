package com.deendayalproject.repository

import PreviousInspectionItemResponse
import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.model.request.CandidatePreviousBatchReq
import com.deendayalproject.model.request.GetAttendanceDetailsReq
import com.deendayalproject.model.request.GetDDSaveDataReq
import com.deendayalproject.model.request.GetImageListReq
import com.deendayalproject.model.request.GetPrevDueQueList
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.InspectionPreviousBatchList
import com.deendayalproject.model.request.InspectionRequestBody
import com.deendayalproject.model.request.InspectionTcDetailsReq
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.request.PreviousInsQuesReq
import com.deendayalproject.model.request.PreviousInsQuesReqN
import com.deendayalproject.model.request.SavePreDDQueReq
import com.deendayalproject.model.request.SubjectDeleteReq
import com.deendayalproject.model.request.SubjectReq
import com.deendayalproject.model.request.TrainerListReq
import com.deendayalproject.model.request.TrainingCenterOpenStatusReq
import com.deendayalproject.model.request.assesmentInspection.GetCandidateRecordsVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.GetTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveTrainerAttendanceInspectionRequest
import com.deendayalproject.model.request.savePreviousInspectionQuesReq
import com.deendayalproject.model.request.saveTrainerClassObservationInspectionReq
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateRecordsVerificationDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.TrainerAttendanceInspectionResponse
import com.deendayalproject.model.response.CandidatePreviousBatchRes
import com.deendayalproject.model.response.DueDiligenceItemResponse
import com.deendayalproject.model.response.GetAttendanceDetailsRes
import com.deendayalproject.model.response.GetDDSaveDataRes
import com.deendayalproject.model.response.GetImageListRes
import com.deendayalproject.model.response.GetPrevDueQueListRes
import com.deendayalproject.model.response.GetTcInspectionRes
import com.deendayalproject.model.response.InsertRes
import com.deendayalproject.model.response.InspectionPreviousBatchRes
import com.deendayalproject.model.response.InspectionTcDetailsRes
import com.deendayalproject.model.response.PreviousInsQues
import com.deendayalproject.model.response.SubjectDeleteRes
import com.deendayalproject.model.response.SubjectListRes
import com.deendayalproject.model.response.TrainerClassObservationResponse
import com.deendayalproject.model.response.TrainerListRes
import com.deendayalproject.model.response.TrainingCenterOpenStatusRes
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


    suspend fun getSubjectList  (subjectReq: SubjectReq, header :String): Result<SubjectListRes> =
        safeApiCallWithToken(token = header) {
            apiService.getSubjectList(subjectReq)
        }

    suspend fun deleteSubjectItem  (subjectDeleteReq: SubjectDeleteReq, header :String): Result<SubjectDeleteRes> =
        safeApiCallWithToken(token = header) {
            apiService.deleteSubjectItem(subjectDeleteReq)
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



    suspend fun getTrainersListInspection  (trainerListReq: TrainerListReq): Result<TrainerListRes> =
        safeApiCallWithToken(token = "") {
            apiService.getTrainersListInspection(trainerListReq)
        }


    suspend fun getTrainerAttendanceInspection(

        request: GetTrainerAttendanceInspectionRequest

    ): Result<List<TrainerAttendanceInspectionResponse>> =

        safeApiCallN {

            apiService.getTrainerAttendanceInspection(request)

        }


    suspend fun saveTrainerAttendanceInspection(

        request: SaveTrainerAttendanceInspectionRequest

    ): Result<List<Nothing>> =

        safeApiCallN {
            apiService.saveTrainerAttendanceInspection(request)
        }






    suspend fun saveTrainerClassObservationInspection(

        request: saveTrainerClassObservationInspectionReq

    ): Result<TrainerClassObservationResponse> =
        safeApiCall {
            apiService.saveTrainerClassObservationInspection(request)
        }





    suspend fun getPreviousDueDiligenceQuestion  (getPrevDueQueList: GetPrevDueQueList, header :String): Result<GetPrevDueQueListRes> =
        safeApiCallWithToken(token = header) {
            apiService.getPreviousDueDiligenceQuestion(getPrevDueQueList)
        }




    suspend fun savePreviousDueDiligenceQues  (savePreDDQueReq: SavePreDDQueReq, header :String): Result<InsertRes> =
        safeApiCallWithToken(token = header) {
            apiService.savePreviousDueDiligenceQues(savePreDDQueReq)
        }



    suspend fun getSavedPreviousDueDiligenceQue  (getDDSaveDataReq: GetDDSaveDataReq, header :String): Result<GetDDSaveDataRes> =
        safeApiCallWithToken(token = header) {
            apiService.getSavedPreviousDueDiligenceQue(getDDSaveDataReq)
        }




    suspend fun getPreviousInsQues  (previousInsQuesReq: PreviousInsQuesReq, header :String): Result<PreviousInsQues> =
        safeApiCallWithToken(token = header) {
            apiService.getPreviousInsQues(previousInsQuesReq)
        }





    suspend fun savePreviousInspectionObservation  (savePreviousInspectionQuesReq: savePreviousInspectionQuesReq, header :String): Result<InsertRes> =
        safeApiCallWithToken(token = header) {
            apiService.savePreviousInspectionObservation(savePreviousInspectionQuesReq)
        }

    suspend fun insertTrainingCenterOpenStatus(request: TrainingCenterOpenStatusReq, header: String ): Result<TrainingCenterOpenStatusRes> =
        safeApiCallWithToken(token = header) {
            apiService.insertTrainingCenterOpenStatus(request)
        }



}


