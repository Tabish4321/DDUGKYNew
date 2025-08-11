import android.content.Context
import com.deendayalproject.model.LoginErrorResponse
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.TrainingCenterResponse
import com.google.gson.Gson

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



    suspend fun fetchModules(moduleRequest: ModulesRequest,token: String ): Result<ModuleResponse> {
        val apiService = RetrofitClient.getApiService(context) // Ensure 'context' is accessible here

        return try {
            val response = apiService.fetchModules(moduleRequest)  // pass directly, no wrapping
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
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



}




