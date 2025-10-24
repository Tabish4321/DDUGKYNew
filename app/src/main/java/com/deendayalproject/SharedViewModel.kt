import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.request.AllRoomDetaisReques
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.DLRequest
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
import com.deendayalproject.model.request.ITLabDetailsRequest
import com.deendayalproject.model.request.InsertLivingAreaReq
import com.deendayalproject.model.request.InsertRfInfraDetaiReq
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.LoginRequest
import com.deendayalproject.model.request.ModulesRequest
import com.deendayalproject.model.request.OfficeRoomDetailsRequest
import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
import com.deendayalproject.model.request.ResidentialFacilityQTeamRequest
import com.deendayalproject.model.request.RfLivingAreaInformationResponseRQ
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.SubmitOfficeCumCounsellingRoomDetailsRequest
import com.deendayalproject.model.request.TCDLRequest
import com.deendayalproject.model.request.TCITLDomainLabDetailsRequest
import com.deendayalproject.model.request.TCRRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcQTeamInsertReq
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.request.insertRfBasicInfoReq
import com.deendayalproject.model.response.AcademicNonAcademicResponse
import com.deendayalproject.model.response.AllRoomDetailResponse
import com.deendayalproject.model.response.BlockResponse
import com.deendayalproject.model.response.CCTVComplianceResponse
import com.deendayalproject.model.response.CommonEquipmentRes
import com.deendayalproject.model.response.DescOtherAreaRes
import com.deendayalproject.model.response.DistrictResponse
import com.deendayalproject.model.response.ElectircalWiringReponse
import com.deendayalproject.model.response.ElectricalWireRes
import com.deendayalproject.model.response.FinalSubmitRes
import com.deendayalproject.model.response.GeneralDetails
import com.deendayalproject.model.response.GpResponse
import com.deendayalproject.model.response.ITLAbDetailsErrorResponse
import com.deendayalproject.model.response.InfrastructureDetailsandCompliancesRFQT
import com.deendayalproject.model.response.InsertTcBasicInfoResponse
import com.deendayalproject.model.response.InsertTcGeneralDetailsResponse
import com.deendayalproject.model.response.IpEnableRes
import com.deendayalproject.model.response.LoginResponse
import com.deendayalproject.model.response.ModuleResponse
import com.deendayalproject.model.response.ResidentialFacilityQTeam
import com.deendayalproject.model.response.RfListResponse
import com.deendayalproject.model.response.RfLivingAreaInformationResponse
import com.deendayalproject.model.response.SectionStatusRes
import com.deendayalproject.model.response.SignageInfo
import com.deendayalproject.model.response.StandardFormResponse
import com.deendayalproject.model.response.StateResponse
import com.deendayalproject.model.response.SupportInfrastructureResponse
import com.deendayalproject.model.response.TcAcademiaNonAcademiaRes
import com.deendayalproject.model.response.TcInfraResponse
import com.deendayalproject.model.response.TcStaffAndTrainerResponse
import com.deendayalproject.model.response.TrainingCenterInfoRes
import com.deendayalproject.model.response.TcAvailabilitySupportInfraResponse
import com.deendayalproject.model.response.TcCommonEquipmentResponse
import com.deendayalproject.model.response.TcDescriptionOtherAreasResponse
import com.deendayalproject.model.response.TcSignagesInfoBoardResponse
import com.deendayalproject.model.response.ToiletDetailsErrorResponse
import com.deendayalproject.model.response.TeachingLearningRes
import com.deendayalproject.model.response.ToiletResponse
import com.deendayalproject.model.response.TrainingCenterResponse
import com.deendayalproject.model.response.VillageRes

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



    private val _rfTrainingCenters = MutableLiveData<Result<RfListResponse>>()
    val rfTrainingCenters: LiveData<Result<RfListResponse>> = _rfTrainingCenters



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
//desc area
    private val _insertDescAreaDetails = MutableLiveData<Result<TcDescriptionOtherAreasResponse>>()
    val insertDescAreaDetails: LiveData<Result<TcDescriptionOtherAreasResponse>> = _insertDescAreaDetails

    //wash bain
    private val _insertWashBasinDtails = MutableLiveData<Result<ToiletDetailsErrorResponse>>()
    val insertWashBasinDtails : LiveData<Result<ToiletDetailsErrorResponse>> = _insertWashBasinDtails


    private val _stateList = MutableLiveData<Result<StateResponse>>()
    val stateList: LiveData<Result<StateResponse>> = _stateList

    private val _districtList = MutableLiveData<Result<DistrictResponse>>()
    val districtList: LiveData<Result<DistrictResponse>> = _districtList

    private val _blockList = MutableLiveData<Result<BlockResponse>>()
    val blockList: LiveData<Result<BlockResponse>> = _blockList

    private val _gpList = MutableLiveData<Result<GpResponse>>()
    val gpList: LiveData<Result<GpResponse>> = _gpList

    private val _villageList = MutableLiveData<Result<VillageRes>>()
    val villageList: LiveData<Result<VillageRes>> = _villageList


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




    //itLab
    private val _insertITTabDtails = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertITTabDtails: LiveData<Result<ITLAbDetailsErrorResponse>> = _insertITTabDtails


    private val _AcademicNonAcademicResponse =
        MutableLiveData<Result<AcademicNonAcademicResponse>>()
    val AcademicNonAcademicResponse: LiveData<Result<AcademicNonAcademicResponse>> = _AcademicNonAcademicResponse





    //Office Cum(Counselling room)
    private val _OfficeCumCounsellingroom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val OfficeCumCounsellingroom: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _OfficeCumCounsellingroom

    //    ReceptionArea    Ajit Ranjan
    private val _ReceptionAreaServices = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val ReceptionAreaServices: LiveData<Result<ITLAbDetailsErrorResponse>> = _ReceptionAreaServices



    //Office  room
    private val _Officeroom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val Officeroom: LiveData<Result<ITLAbDetailsErrorResponse>> = _Officeroom

    //IT Come Domain Lab
    private val _ITComeDomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val ITComeDomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _ITComeDomainLab


    //   TheoryCumITLab
    private val _TheoryCumITLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryCumITLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryCumITLab

    //    TheoryCumDomainLab
    private val _TheoryCumDomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryCumDomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryCumDomainLab



    //    DomainLab
    private val _DomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val DomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _DomainLab

    //    Theory Class Room
    private val _TheoryClassRoom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryClassRoom: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryClassRoom
    // Login API call






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

    fun fetchSrlmTeamTrainingList(request: TrainingCenterRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.fetchSrlmTeamTrainingList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _trainingCenters.postValue(result)
            _loading.postValue(false)
        }
    }



    fun fetchRfList(request: TrainingCenterRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.fetchRfList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _rfTrainingCenters.postValue(result)
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


    //wash basin
    fun SubmitWashBasinDataToServer(request: ToiletDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitWashbsinDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertWashBasinDtails.postValue(result)
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

    private val _getAvailabilitySupportInfra = MutableLiveData<Result<SupportInfrastructureResponse>>()
    val getAvailabilitySupportInfra: LiveData<Result<SupportInfrastructureResponse>> = _getAvailabilitySupportInfra

    fun getAvailabilitySupportInfra(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getAvailabilitySupportInfra(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getAvailabilitySupportInfra.postValue(result)
            _loading.postValue(false)
        }
    }



    private val _getAvailabilityStandardForms = MutableLiveData<Result<StandardFormResponse>>()
    val getAvailabilityStandardForms: LiveData<Result<StandardFormResponse>> = _getAvailabilityStandardForms

    fun getAvailabilityStandardForms(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getAvailabilityStandardForms(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getAvailabilityStandardForms.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _getAcademicRoomDetails = MutableLiveData<Result<AllRoomDetailResponse>>()
    val getAcademicRoomDetails: LiveData<Result<AllRoomDetailResponse>> = _getAcademicRoomDetails

    fun getAcademicRoomDetails(request: AllRoomDetaisReques) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getAcademicRoomDetails(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getAcademicRoomDetails.postValue(result)
            _loading.postValue(false)
        }
    }




    private val _insertQTeamVerification = MutableLiveData<Result<InsertTcGeneralDetailsResponse>>()
    val insertQTeamVerification: LiveData<Result<InsertTcGeneralDetailsResponse>> = _insertQTeamVerification

    fun insertQTeamVerification(request: TcQTeamInsertReq) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.insertQTeamVerification(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertQTeamVerification.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _insertSrlmVerification = MutableLiveData<Result<InsertTcGeneralDetailsResponse>>()
    val insertSrlmVerification: LiveData<Result<InsertTcGeneralDetailsResponse>> = _insertSrlmVerification

    fun insertSrlmVerification(request: TcQTeamInsertReq) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.insertSrlmVerification(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertSrlmVerification.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _getFinalSubmitData = MutableLiveData<Result<FinalSubmitRes>>()
    val getFinalSubmitData: LiveData<Result<FinalSubmitRes>> = _getFinalSubmitData

    fun getFinalSubmitData(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getFinalSubmitData(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getFinalSubmitData.postValue(result)
            _loading.postValue(false)
        }
    }

    private val _getSectionsStatusData = MutableLiveData<Result<SectionStatusRes>>()
    val getSectionsStatusData: LiveData<Result<SectionStatusRes>> = _getSectionsStatusData

    fun getSectionsStatusData(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getSectionsStatus(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _getSectionsStatusData.postValue(result)
            _loading.postValue(false)
        }
    }

    fun getStateList(request: StateRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getStateList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _stateList.postValue(result)
            _loading.postValue(false)
        }
    }
    fun getDistrictList(request: DistrictRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getDistrictList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _districtList.postValue(result)
            _loading.postValue(false)
        }
    }
    fun getBlockList(request: BlockRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getBlockList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _blockList.postValue(result)
            _loading.postValue(false)
        }
    }
    fun getGpList(request: GpRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getGpList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _gpList.postValue(result)
            _loading.postValue(false)
        }
    }

    fun getVillageList(request: VillageReq, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getVillageList(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _villageList.postValue(result)
            _loading.postValue(false)
        }
    }







//    Ajit Ranjan (RecyclerView)


    fun DesriptionAcademicNonList(request: AcademicNonAcademicArea, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitDesriptionAcademicNonDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _AcademicNonAcademicResponse.postValue(result)
            _loading.postValue(false)
        }
    }


    //    Ajit Ranjan (IT LAB)
    //
    fun SubmitITLABDataToServer(request: ITLabDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitITLabDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _insertITTabDtails.postValue(result)
            _loading.postValue(false)
        }
    }

    //    Office Cum(Counselling room)    Ajit Ranjan
    fun SubmitOfficeCumCounsellingRoomDataToServer(
        request: SubmitOfficeCumCounsellingRoomDetailsRequest,
        token: String
    ) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitOfficeCumCounsellingroomDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _OfficeCumCounsellingroom.postValue(result)
            _loading.postValue(false)
        }
    }

    //    ReceptionArea    Ajit Ranjan
    fun SubmitReceptionAreaDataToServer(request: ReceptionAreaRoomDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitReceptionAreaDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _ReceptionAreaServices.postValue(result)
            _loading.postValue(false)
        }
    }


    //    Office Room   Ajit Ranjan
    fun SubmitOfficeRoomDataToServer(request: OfficeRoomDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitOfficeRoomDataToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _Officeroom.postValue(result)
            _loading.postValue(false)
        }
    }


    //    IT Come Domain Lab   Ajit Ranjan
    fun SubmitITComeDomainLabDataToServer(request: ITComeDomainLabDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitItComeDomainlabToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _ITComeDomainLab.postValue(result)
            _loading.postValue(false)
        }
    }


    //    Theory Cum IT Lab Lab
    fun SubmitTheoryComeItLabDataToServer(request: TCITLDomainLabDetailsRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitTheoryCumITLabToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _TheoryCumITLab.postValue(result)
            _loading.postValue(false)
        }
    }


    //    Theory Cum Domain Lab Lab
    fun SubmitTCDLDataToServer(request: TCDLRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitTheoryCumDomainLabToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _TheoryCumDomainLab.postValue(result)
            _loading.postValue(false)
        }
    }


    //    Domain Lab
    fun SubmitDLDataToServer(request: DLRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitDomainLabToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _DomainLab.postValue(result)
            _loading.postValue(false)
        }

    }



    //    Theory Class Room
    fun SubmitTheoryClassRoomDataToServer(request: TCRRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.submitTheoryClassRoomToServer(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _DomainLab.postValue(result)
            _loading.postValue(false)
        }

    }


    //    DomainLab
    private val _RfBasicInfo = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfBasicInfo: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfBasicInfo


    fun SubmitRfBasicInformationToServer(request: insertRfBasicInfoReq, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.insertRfBasicInformation(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _RfBasicInfo.postValue(result)
            _loading.postValue(false)
        }

    }



    private val _RfInfra = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfInfra: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfInfra


    fun SubmitRfInfraDetailsAndComlianceToServer(request: InsertRfInfraDetaiReq, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.insertRfInfraDetailsAndComliance(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _RfInfra.postValue(result)
            _loading.postValue(false)
        }

    }





    private val _RfLivingArea = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfLivingArea: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfLivingArea


    fun SubmitRfLivingAreaInformationToServer(request: InsertLivingAreaReq, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.insertRfLivingAreaInformation(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _RfLivingArea.postValue(result)
            _loading.postValue(false)
        }

    }





//    ResidentialFacilityQTeamRequest Ajit Ranjan  16/10/2025


    fun fetchResidentialFacilityQTeamList(request: ResidentialFacilityQTeamRequest, token: String) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.fetchResidentialFacilityQTeamist(request, token)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _trainingCenters.postValue(result)
            _loading.postValue(false)
        }
    }


//    GetRfBasicInformation AjitRanjan 17/10/2025




    //ResidentialFacilityQTeam
    private val _ResidentialFacilityQTeam = MutableLiveData<Result<ResidentialFacilityQTeam>>()
    val ResidentialFacilityQTeam: LiveData<Result<ResidentialFacilityQTeam>> = _ResidentialFacilityQTeam

    fun getRfBasicInformationrInfo(request: TrainingCenterInfo) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getTRfBasicInfo(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _ResidentialFacilityQTeam.postValue(result)
            _loading.postValue(false)
        }
    }


//    Ajit Ranjan create 21/October/2026  CompliancesRFQTReqRFQT

    private val _CompliancesRFQTReqRFQT = MutableLiveData<Result<InfrastructureDetailsandCompliancesRFQT>>()
    val CompliancesRFQTReqRFQT: LiveData<Result<InfrastructureDetailsandCompliancesRFQT>> = _CompliancesRFQTReqRFQT


    private val _fLivingAreaInformation = MutableLiveData<Result<RfLivingAreaInformationResponse>>()
    val fLivingAreaInformation: LiveData<Result<RfLivingAreaInformationResponse>> = _fLivingAreaInformation


    fun getCompliancesRFQTReqRFQT(request: CompliancesRFQTReq) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repository.getCompliancesRFQTReqRFQT(request)
            result.onFailure {
                _errorMessage.postValue(it.message ?: "Unknown error")
            }
            _CompliancesRFQTReqRFQT.postValue(result)
            _loading.postValue(false)
        }
    }
//    Ajit Ranjan create 24/October/2026  getRfLivingAreaInformation
fun getRfLivingAreaInformation(request: RfLivingAreaInformationResponseRQ) {
    _loading.postValue(true)
    viewModelScope.launch {
        val result = repository.getRfLivingAreaInformation(request)
        result.onFailure {
            _errorMessage.postValue(it.message ?: "Unknown error")
        }
        _fLivingAreaInformation.postValue(result)
        _loading.postValue(false)
    }
}
}
