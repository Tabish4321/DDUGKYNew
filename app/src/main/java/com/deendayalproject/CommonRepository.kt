import android.content.Context
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.model.LoginErrorResponse
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
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
                    Result.failure(HttpException(response)) // force failure to trigger session handling
                } else {
                    Result.failure(HttpException(response))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun fetchTrainingCenters(request: TrainingCenterRequest, token: String): Result<TrainingCenterResponse> {
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

    suspend fun submitWiringDataToServer(request: ElectricalWiringRequest, token: String) : Result<ElectircalWiringReponse>{
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.insertTcElectricWiringStandard(request)
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

    suspend fun submitGeneralDataToServer(request: InsertTcGeneralDetailsRequest,token: String) : Result<InsertTcGeneralDetailsResponse>{
        return try {
            val bearerToken = "Bearer $token"
            val response = apiService.insertTcGeneralDetails(request)
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




