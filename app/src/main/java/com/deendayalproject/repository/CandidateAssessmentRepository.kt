package com.deendayalproject.repository

import android.content.Context
import com.deendayalproject.base.BaseRepository
import com.deendayalproject.base.BaseResponse
import com.deendayalproject.model.request.OngoingSubmitBasicRecordsReq
import com.deendayalproject.model.request.SaveInspectionStandardFormRequest
import com.deendayalproject.model.request.assesmentInspection.AssessmentStatusInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetCandidateRecordsVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.GetDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionSectionStatusRequest
import com.deendayalproject.model.request.assesmentInspection.GetInspectionStandardFormRequest
import com.deendayalproject.model.request.assesmentInspection.GetResidentialFacilityVerificationRequest
import com.deendayalproject.model.request.assesmentInspection.SaveCandidateAssessmentInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveDistributedLearningMaterialInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveEntitlementsDistributionInspectionRequest
import com.deendayalproject.model.request.assesmentInspection.SaveResidentialFacilityVerificationRequest
import com.deendayalproject.model.response.CandidateAssessmentResponse.AssessmentStatusResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateAssessmentInspectionDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.CandidateRecordsVerificationDetails
import com.deendayalproject.model.response.CandidateAssessmentResponse.DistributedLearningMaterialInspectionResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.EntitlementsDistributionInspectionResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.InspectionSectionStatusResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.InspectionStandardFormDto
import com.deendayalproject.model.response.CandidateAssessmentResponse.ResidentialFacilityVerificationResponse
import com.deendayalproject.model.response.CandidateAssessmentResponse.SaveInspectionStandardFormResponse
import com.deendayalproject.model.response.InsertRes
import com.deendayalproject.network.ApiService

/**
 * Created by Rishi Porwal
 */
class CandidateAssessmentRepository(
    context: Context
) : BaseRepository<ApiService>(context) {

    suspend fun getAssessmentInspection(
        request: GetCandidateAssessmentInspectionRequest
    ): Result<List<CandidateAssessmentInspectionDetails>> =
        safeApiCallN {
            apiService.getCandidateAssessmentInspection(request)
        }

    suspend fun saveAssessmentInspection(
        request: SaveCandidateAssessmentInspectionRequest
    ): Result<List<Nothing>> =
        safeApiCallN {
            apiService.saveCandidateAssessmentInspection(request)
        }

    suspend fun getAssessmentStatus(
        request: AssessmentStatusInspectionRequest
    ): Result<List<AssessmentStatusResponse>> =
        safeApiCallN {
            apiService.getAssessmentStatusForInspection(request)
        }


    suspend fun saveCandidateBasicRecords  (ongoingSubmitBasicRecordsReq: OngoingSubmitBasicRecordsReq): Result<InsertRes> =
        safeApiCall {
            apiService.saveCandidateBasicRecords(ongoingSubmitBasicRecordsReq)
        }


    suspend fun getCandidateRecordsVerification(
        request: GetCandidateRecordsVerificationRequest
    ): Result<List<CandidateRecordsVerificationDetails>> =
        safeApiCallN {
            apiService.getCandidateRecordsVerification(request)
        }

    suspend fun getInspectionSectionStatus(
        request: GetInspectionSectionStatusRequest
    ): Result<List<InspectionSectionStatusResponse>> =
        safeApiCallN {
            apiService.getInspectionSectionStatus(request)
        }

    suspend fun getDistributedLearningMaterialInspection(
        request: GetDistributedLearningMaterialInspectionRequest
    ): Result<List<DistributedLearningMaterialInspectionResponse>> =
        safeApiCallN {
            apiService.getDistributedLearningMaterialInspection(request)
        }


    suspend fun saveDistributedLearningMaterialInspection(
        request: SaveDistributedLearningMaterialInspectionRequest
    ): Result<List<Nothing>> =
        safeApiCallN {
            apiService.saveDistributedLearningMaterialInspection(request)
        }


    suspend fun getEntitlementsDistributionInspection(
        request: GetEntitlementsDistributionInspectionRequest
    ): Result<List<EntitlementsDistributionInspectionResponse>> =
        safeApiCallN {
            apiService.getEntitlementsDistributionInspection(request)
        }


    suspend fun saveEntitlementsDistributionInspection(
        request: SaveEntitlementsDistributionInspectionRequest
    ): Result<List<Nothing>> =
        safeApiCallN {
            apiService.saveEntitlementsDistributionInspection(request)
        }



    suspend fun getResidentialFacilityVerification(
        request: GetResidentialFacilityVerificationRequest
    ): Result<List<ResidentialFacilityVerificationResponse>> =
        safeApiCallN {
            apiService.getResidentialFacilityVerification(request)
        }

    suspend fun saveResidentialFacilityVerification(
        request: SaveResidentialFacilityVerificationRequest
    ): Result<List<Nothing>> =
        safeApiCallN {
            apiService.saveResidentialFacilityVerification(request)
        }

    suspend fun getInspectionStandardForm(

        request: GetInspectionStandardFormRequest

    ): Result<List<InspectionStandardFormDto>> = safeApiCallN {

        apiService.getInspectionStandardForm(request)
    }

    suspend fun saveInspectionStandardForm(

        request: SaveInspectionStandardFormRequest

    ): Result<SaveInspectionStandardFormResponse> = safeApiCall {

        apiService.saveInspectionStandardForm(request)
    }

}