import android.content.Context
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.model.LoginErrorResponse
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.AllRoomDetailResponse
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.CommonEquipmentRes
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.ElectricalWireRes
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.IpEnableRes
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.SignageInfo
import com.deendayalproject.model.response.StandardFormResponse
import com.deendayalproject.model.response.SupportInfrastructureResponse
import com.deendayalproject.model.response.TcAcademiaNonAcademiaRes
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.TeachingLearningRes
import com.deendayalproject.model.response.ToiletDetailsErrorResponse
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.util.AppUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CommonRepository(private val context: Context) {

    private val apiService = RetrofitClient.getApiService(context)


    suspend fun loginUser(request: LoginRequest): Result<LoginResponse> {

        return try {
            val response = apiService.loginUser(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.responseCode == 200 && !body.accessToken.isNullOrEmpty()) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body?.responseDesc ?: "Login failed"))
                }
            } else {
                val error = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(error, LoginErrorResponse::class.java)
                Result.failure(Exception(errorResponse?.errorMsg ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchModules(request: ModulesRequest, token: String): Result<ModuleResponse> {
        return try {
            val response = apiService.fetchModules(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                if (response.code() == 401) {
                    Result.failure(HttpException(response))
                } else {
                    Result.failure(HttpException(response))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun fetchTrainingCenters(
        request: TrainingCenterRequest,
        token: String
    ): Result<TrainingCenterResponse> {
        return try {
            val response = apiService.getTrainingCenterList(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun fetchQTeamTrainingList(request: TrainingCenterRequest, token: String): Result<TrainingCenterResponse> {
        return try {
            val response = apiService.getQTeamTrainingList(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                if (response.code() == 202) {
                    Result.failure(HttpException(response))
                } else {
                    Result.failure(HttpException(response))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchSrlmTeamTrainingList(request: TrainingCenterRequest, token: String): Result<TrainingCenterResponse> {
        return try {
            val response = apiService.getTrainingCenterVerificationSRLM(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                if (response.code() == 202) {
                    Result.failure(HttpException(response))
                } else {
                    Result.failure(HttpException(response))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun submitCCTVDataToServer(request: CCTVComplianceRequest, token: String): Result<CCTVComplianceResponse>   = withContext(Dispatchers.IO) {
         try {
             "Bearer $token"
            val response = apiService.insertCCTVCompliance(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitWiringDataToServer(
        request: ElectricalWiringRequest,
        token: String
    ): Result<ElectircalWiringReponse> = withContext(Dispatchers.IO)  {
         try {
             "Bearer $token"
            val response = apiService.insertTcElectricWiringStandard(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitGeneralDataToServer(
        request: InsertTcGeneralDetailsRequest,
        token: String
    ): Result<InsertTcGeneralDetailsResponse> = withContext(Dispatchers.IO) {
         try {
             "Bearer $token"
            val response = apiService.insertTcGeneralDetails(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

 // wash bain info
    suspend fun submitWashbsinDataToServer(request: ToiletDetailsRequest, token: String): Result<ToiletDetailsErrorResponse> = withContext(Dispatchers.IO) {
        try {
            "Bearer $token"
            val response = apiService.insertTcToiletsWashBasins(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //TC basic info
    suspend fun submitTcBasicDataToServer(
        request: TcBasicInfoRequest,
        token: String
    ): Result<InsertTcBasicInfoResponse> = withContext(Dispatchers.IO)  {
         try {
             "Bearer $token"
            val response = apiService.insertTcBasicInfo(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }



    // signages info boards
    suspend fun submitSignagesBoardsDataToServer(
        request: TcSignagesInfoBoardRequest,
        token: String
    ): Result<TcSignagesInfoBoardResponse> {
        return try {
            "Bearer $token"
            val response = apiService.insertTcSignagesInfoBoard(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    // Tc availability of support infra
    suspend fun submitInfraDataToServer(
        request: TcAvailabilitySupportInfraRequest,
        token: String
    ): Result<TcAvailabilitySupportInfraResponse> {
        return try {
            "Bearer $token"
            val response = apiService.insertTcAvailabilitySupportInfra(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }





    suspend fun submitCommonEquipmentDataToServer(
        request: TcCommonEquipmentRequest,
        token: String
    ): Result<TcCommonEquipmentResponse> {
        return try {
            "Bearer $token"
            val response = apiService.insertTcCommonEquipment(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun submitDescDataToServer(
        request: TcDescriptionOtherAreasRequest,
        token: String
    ): Result<TcDescriptionOtherAreasResponse> {
        return try {
            "Bearer $token"
            val response = apiService.insertTcDescriptionOtherAreas(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getTrainerCenterInfo(request: TrainingCenterInfo) : Result<TrainingCenterInfoRes>{
        return try {
           // val bearerToken = "Bearer $token"
            val response = apiService.getTrainerCenterInfo(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getTcStaffDetails(request: TrainingCenterInfo) : Result<TcStaffAndTrainerResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getTcStaffDetails(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getTrainerCenterInfra(request: TrainingCenterInfo) : Result<TcInfraResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getTrainerCenterInfra(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




        suspend fun getTcAcademicNonAcademicArea(request: TrainingCenterInfo) : Result<TcAcademiaNonAcademiaRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getTcAcademicNonAcademicArea(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getTcToiletWashBasin(request: TrainingCenterInfo) : Result<ToiletResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getTcToiletWashBasin(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




    suspend fun getDescriptionOtherArea(request: TrainingCenterInfo) : Result<DescOtherAreaRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getDescriptionOtherArea(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getTeachingLearningMaterial(request: TrainingCenterInfo) : Result<TeachingLearningRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getTeachingLearningMaterial(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGeneralDetails(request: TrainingCenterInfo) : Result<GeneralDetails>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getGeneralDetails(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getElectricalWiringStandard(request: TrainingCenterInfo) : Result<ElectricalWireRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getElectricalWiringStandard(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getSignagesAndInfoBoard(request: TrainingCenterInfo) : Result<SignageInfo>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getSignagesAndInfoBoard(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getIpEnabledcamera(request: TrainingCenterInfo) : Result<IpEnableRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getIpEnabledcamera(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    suspend fun getCommonEquipment(request: TrainingCenterInfo) : Result<CommonEquipmentRes>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getCommonEquipment(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getAvailabilitySupportInfra(request: TrainingCenterInfo) : Result<SupportInfrastructureResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getAvailabilitySupportInfra(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


        suspend fun getAvailabilityStandardForms(request: TrainingCenterInfo) : Result<StandardFormResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getAvailabilityStandardForms(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAcademicRoomDetails(request: AllRoomDetaisReques) : Result<AllRoomDetailResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.getAcademicRoomDetails(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun insertQTeamVerification(request: TcQTeamInsertReq) : Result<InsertTcGeneralDetailsResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.insertQTeamVerification(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun insertSrlmVerification(request: TcQTeamInsertReq) : Result<InsertTcGeneralDetailsResponse>{
        return try {
            // val bearerToken = "Bearer $token"
            val response = apiService.insertSrlmVerification(request)
            if (response.isSuccessful){
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}



