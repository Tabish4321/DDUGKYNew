import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

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



    // insert cctv section
    private val _insertCCTVdata = MutableLiveData<Result<CCTVComplianceResponse>>()
    val insertCCTVdata: LiveData<Result<CCTVComplianceResponse>> =_insertCCTVdata

    // insert ipenabled section
    private val _insertIpenabledata = MutableLiveData<Result<ElectircalWiringReponse>>()
    val insertIpenabledata : LiveData<Result<ElectircalWiringReponse>> = _insertIpenabledata



    // insert general details

    private val _insertGeneralDetails = MutableLiveData<Result< InsertTcGeneralDetailsResponse>>()
    val insertGeneralDetails : LiveData<Result<InsertTcGeneralDetailsResponse>> = _insertGeneralDetails

    // Login API call
    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            val result = repository.loginUser(request)
            result.onSuccess { response ->

            }

            result.onFailure { throwable ->
                // Check if error is 401 token expired
                if (throwable is retrofit2.HttpException && throwable.code() == 401) {
                    _sessionExpired.postValue(true)
                } else {
                    _errorMessage.postValue(throwable.message ?: "Unknown error")
                }
            }
            _loginResult.postValue(result)
        }
    }

    fun triggerSesionExpired(){
        _sessionExpired.postValue(true)
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

//IP enabled camera insert
    fun submitCCTVDataToServer(request: CCTVComplianceRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitCCTVDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertCCTVdata.postValue(result)
            _loading.postValue(false)
        }
    }

    // electric wiring insert
    fun submitElectricalData(request: ElectricalWiringRequest, token: String){
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitWiringDataToServer(request,token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertIpenabledata.postValue(result)
            _loading.postValue(false)
        }

        }


    // general details insert
    fun submitGeneralDetails(request: InsertTcGeneralDetailsRequest, token: String){
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitGeneralDataToServer(request,token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertGeneralDetails.postValue(result)
            _loading.postValue(false)
        }

    }
    }


