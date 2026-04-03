import android.content.Context
import android.os.Build
import com.deendayalproject.BuildConfig
import com.deendayalproject.network.TokenInterceptor
import com.deendayalproject.network.ApiService
import com.deendayalproject.network.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    fun getUrl()= BuildConfig.BASE_URL
    fun getApiService(context: Context): ApiService {
        val tokenInterceptor = TokenInterceptor(context)
        val client = OkHttpClient.Builder()
            .cache(null)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor (AuthInterceptor() )
            .addInterceptor(tokenInterceptor)
            .addInterceptor(LoggingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(getUrl())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

