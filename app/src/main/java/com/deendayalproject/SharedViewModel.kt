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
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
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

import kotlinx.coroutines.launch

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
    val insertCCTVdata: LiveData<Result<CCTVComplianceResponse>> = _insertCCTVdata

    // insert ipenabled section
    private val _insertIpenabledata = MutableLiveData<Result<ElectircalWiringReponse>>()
    val insertIpenabledata: LiveData<Result<ElectircalWiringReponse>> = _insertIpenabledata

    // insert general details
    private val _insertGeneralDetails = MutableLiveData<Result<InsertTcGeneralDetailsResponse>>()
    val insertGeneralDetails: LiveData<Result<InsertTcGeneralDetailsResponse>> =
        _insertGeneralDetails

    // insert TC details
    private val _insertTCInfoDeatils = MutableLiveData<Result<InsertTcBasicInfoResponse>>()
    val insertTCInfoDetails: LiveData<Result<InsertTcBasicInfoResponse>> = _insertTCInfoDeatils

    // TC signages and info boards

    private val _insertSignagesInfoBoardsDetails =
        MutableLiveData<Result<TcSignagesInfoBoardResponse>>()
    val insertSignagesInfoBoardsDetails: LiveData<Result<TcSignagesInfoBoardResponse>> =
        _insertSignagesInfoBoardsDetails


    // TC Support Infra
    private val _insertSupportInfraDetails =
        MutableLiveData<Result<TcAvailabilitySupportInfraResponse>>()
    val insertSupportInfraDetails: LiveData<Result<TcAvailabilitySupportInfraResponse>> =
        _insertSupportInfraDetails

    // Common equipment

    private val _insertCommonEquipDetails = MutableLiveData<Result<TcCommonEquipmentResponse>>()
    val insertCommonEquipDetails: LiveData<Result<TcCommonEquipmentResponse>> = _insertCommonEquipDetails

    private val _insertDescAreaDetails = MutableLiveData<Result<TcDescriptionOtherAreasResponse>>()
    val insertDescAreaDetails: LiveData<Result<TcDescriptionOtherAreasResponse>> = _insertDescAreaDetails
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

    /*   fun triggerSesionExpired(){
        _sessionExpired.postValue(true)
    }*/

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

    fun fetchQTeamTrainingList(request: TrainingCenterRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.fetchQTeamTrainingList(request, token)
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
    fun submitElectricalData(request: ElectricalWiringRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitWiringDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertIpenabledata.postValue(result)
            _loading.postValue(false)
        }

    }

    // general details insert
    fun submitGeneralDetails(request: InsertTcGeneralDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitGeneralDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertGeneralDetails.postValue(result)
            _loading.postValue(false)
        }

    }

    //TC basic info
    fun submitTcBasicDataToServer(request: TcBasicInfoRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitTcBasicDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertTCInfoDeatils.postValue(result)
            _loading.postValue(false)

        }
    }

    fun submitTcInfoSignageDataToServer(request: TcSignagesInfoBoardRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitSignagesBoardsDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertSignagesInfoBoardsDetails.postValue(result)
            _loading.postValue(false)
        }
    }

    fun submitTcSupportInfraDataToserver(
        request: TcAvailabilitySupportInfraRequest,
        token: String
    ) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitInfraDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertSupportInfraDetails.postValue(result)
            _loading.postValue(false)
        }
    }


    fun submitTcCommonEquipment(request: TcCommonEquipmentRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitCommonEquipmentDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertCommonEquipDetails.postValue(result)
            _loading.postValue(false)
        }
    }

    fun submitTcDescriptionArea(request: TcDescriptionOtherAreasRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitDescDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertDescAreaDetails.postValue(result)
            _loading.postValue(false)
        }
    }





    private val _trainingCentersInfo = MutableLiveData<Result<TrainingCenterInfoRes>>()
    val trainingCentersInfo: LiveData<Result<TrainingCenterInfoRes>> = _trainingCentersInfo


    fun getTrainerCenterInfo(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTrainerCenterInfo(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _trainingCentersInfo.postValue(result)
            _loading.postValue(false)
        }
    }




    private val _getTcStaffDetails = MutableLiveData<Result<TcStaffAndTrainerResponse>>()
    val getTcStaffDetails: LiveData<Result<TcStaffAndTrainerResponse>> = _getTcStaffDetails


    fun getTcStaffDetails(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTcStaffDetails(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getTcStaffDetails.postValue(result)
            _loading.postValue(false)
        }
    }


    private val _getTrainerCenterInfra = MutableLiveData<Result<TcInfraResponse>>()
    val getTrainerCenterInfra: LiveData<Result<TcInfraResponse>> = _getTrainerCenterInfra


    fun getTrainerCenterInfra(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTrainerCenterInfra(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getTrainerCenterInfra.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _getTcAcademicNonAcademicArea = MutableLiveData<Result<TcAcademiaNonAcademiaRes>>()
    val getTcAcademicNonAcademicArea: LiveData<Result<TcAcademiaNonAcademiaRes>> = _getTcAcademicNonAcademicArea


    fun getTcAcademicNonAcademicArea(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTcAcademicNonAcademicArea(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getTcAcademicNonAcademicArea.postValue(result)
            _loading.postValue(false)
        }
    }


    private val _getTcToiletWashBasin = MutableLiveData<Result<ToiletResponse>>()
    val getTcToiletWashBasin: LiveData<Result<ToiletResponse>> = _getTcToiletWashBasin


    fun getTcToiletWashBasin(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTcToiletWashBasin(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getTcToiletWashBasin.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _getDescriptionOtherArea = MutableLiveData<Result<DescOtherAreaRes>>()
    val getDescriptionOtherArea: LiveData<Result<DescOtherAreaRes>> = _getDescriptionOtherArea


    fun getDescriptionOtherArea(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getDescriptionOtherArea(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getDescriptionOtherArea.postValue(result)
            _loading.postValue(false)
        }
    }



    private val _getTeachingLearningMaterial = MutableLiveData<Result<TeachingLearningRes>>()
    val getTeachingLearningMaterial: LiveData<Result<TeachingLearningRes>> = _getTeachingLearningMaterial


    fun getTeachingLearningMaterial(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTeachingLearningMaterial(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getTeachingLearningMaterial.postValue(result)
            _loading.postValue(false)
        }
    }




    private val _getGeneralDetails = MutableLiveData<Result<GeneralDetails>>()
    val getGeneralDetails: LiveData<Result<GeneralDetails>> = _getGeneralDetails


    fun getGeneralDetails(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getGeneralDetails(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getGeneralDetails.postValue(result)
            _loading.postValue(false)
        }
    }



    private val _getElectricalWiringStandard = MutableLiveData<Result<ElectricalWireRes>>()
    val getElectricalWiringStandard: LiveData<Result<ElectricalWireRes>> = _getElectricalWiringStandard

    fun getElectricalWiringStandard(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getElectricalWiringStandard(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getElectricalWiringStandard.postValue(result)
            _loading.postValue(false)
        }
    }


    private val _getSignagesAndInfoBoard = MutableLiveData<Result<SignageInfo>>()
    val getSignagesAndInfoBoard: LiveData<Result<SignageInfo>> = _getSignagesAndInfoBoard

    fun getSignagesAndInfoBoard(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getSignagesAndInfoBoard(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getSignagesAndInfoBoard.postValue(result)
            _loading.postValue(false)
        }
    }




    private val _getIpEnabledCamera = MutableLiveData<Result<IpEnableRes>>()
    val getIpEnabledCamera: LiveData<Result<IpEnableRes>> = _getIpEnabledCamera

    fun getIpEnabledCamera(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getIpEnabledcamera(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getIpEnabledCamera.postValue(result)
            _loading.postValue(false)
        }
    }


    private val _getCommonEquipment = MutableLiveData<Result<CommonEquipmentRes>>()
    val getCommonEquipment: LiveData<Result<CommonEquipmentRes>> = _getCommonEquipment

    fun getCommonEquipment(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getCommonEquipment(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getCommonEquipment.postValue(result)
            _loading.postValue(false)
        }
    }




}


