import android.content.Context
import com.deendayalproject.network.TokenInterceptor
import com.deendayalproject.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

  //  private const val BASE_URL = "https://kaushal.rural.gov.in/backend/ddugkyapp/"

    // private const val BASE_URL ="http://10.197.183.148:7003/ddugkyapp/"

    private const val BASE_URL = "https://kaushal.dord.gov.in/demobackend/ddugkyapp/"

    //private const val BASE_URL ="http://10.0.2.2:7003/ddugkyapp/"

    fun getApiService(context: Context): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val tokenInterceptor = TokenInterceptor(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor) // ✅ Add your custom token interceptor here
            .addInterceptor(logging) // Logging should go after tokenInterceptor
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
