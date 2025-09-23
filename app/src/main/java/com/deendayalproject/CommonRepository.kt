import android.content.Context
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.model.LoginErrorResponse
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
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.TcAcademiaNonAcademiaRes
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.TeachingLearningRes
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.util.AppUtil
import com.google.gson.Gson
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



    suspend fun submitCCTVDataToServer(request: CCTVComplianceRequest, token: String): Result<CCTVComplianceResponse> {
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.insertCCTVCompliance(request) // ✅ token now passed correctly
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
    ): Result<ElectircalWiringReponse> {
        return try {
            val bearerToken = "Bearer $token"
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
    ): Result<InsertTcGeneralDetailsResponse> {
        return try {
            val bearerToken = "Bearer $token"
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

    //TC basic info
    suspend fun submitTcBasicDataToServer(
        request: TcBasicInfoRequest,
        token: String
    ): Result<InsertTcBasicInfoResponse> {
        return try {
            val bearerToken = "Bearer $token"
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
            val bearerToken = "Bearer $token"
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
            val bearerToken = "Bearer $token"
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
            val bearerToken = "Bearer $token"
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
            val bearerToken = "Bearer $token"
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





}



