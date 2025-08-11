import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.TrainingCenterResponse

import kotlinx.coroutines.launch
import retrofit2.HttpException

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CommonRepository(application.applicationContext)

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _modules = MutableLiveData<Result<ModuleResponse>>()
    val modules: LiveData<Result<ModuleResponse>> = _modules

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _sessionExpired = MutableLiveData<Boolean>()
    val sessionExpired: LiveData<Boolean> = _sessionExpired

    private val _response = MutableLiveData<TrainingCenterResponse>()
    val response: LiveData<TrainingCenterResponse> get() = _response


    private val _trainingCenters = MutableLiveData<Result<TrainingCenterResponse>>()
    val trainingCenters: LiveData<Result<TrainingCenterResponse>> = _trainingCenters


    // Login API call

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            val result = repository.loginUser(request)
            result.onSuccess { response ->
            }
            _loginResult.postValue(result)
        }
    }

    // fetch Module and forms
    fun fetch(modulesRequest: ModulesRequest, token: String) {
        viewModelScope.launch {
            val result = repository.fetchModules(modulesRequest, token)
            result.onSuccess {
                // do something on success if needed
            }
            result.onFailure { throwable ->
                // Check if error is 401 token expired
                if (throwable is retrofit2.HttpException && throwable.code() == 401) {
                    _sessionExpired.postValue(true)
                } else {
                    _errorMessage.postValue(throwable.message ?: "Unknown error")
                }
            }
            _modules.postValue(result)
        }
    }

    fun fetchTrainingCenters(request: TrainingCenterRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.fetchTrainingCenters(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _trainingCenters.postValue(result)
            _loading.postValue(false)
        }
    }
}

