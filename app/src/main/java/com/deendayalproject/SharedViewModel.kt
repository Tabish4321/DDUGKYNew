import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deendayalproject.model.request.*
import com.deendayalproject.model.response.*
import com.deendayalproject.repository.repomanager.RepositoryManager
import com.deendayalproject.uidai.ekyc.UidaiKycRequest
import com.deendayalproject.uidai.ekyc.UidaiResp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Response

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val repositoryManager = RepositoryManager.getInstance(application.applicationContext)
    //private  val repositoryManager = RepositoryManager


    // All LiveData declarations remain EXACTLY the same
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

    private val _trainingRfCenters = MutableLiveData<Result<RfQTeamListRes>>()
    val trainingRfCenters: LiveData<Result<RfQTeamListRes>> = _trainingRfCenters

    private val _rfTrainingCenters = MutableLiveData<Result<RfListResponse>>()
    val rfTrainingCenters: LiveData<Result<RfListResponse>> = _rfTrainingCenters

    private val _fieldprnDetails = MutableLiveData<Result<FieldVerificationListResponse>>()
    val fieldprnDetails: LiveData<Result<FieldVerificationListResponse>> = _fieldprnDetails

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
    val insertCommonEquipDetails: LiveData<Result<TcCommonEquipmentResponse>> =
        _insertCommonEquipDetails

    // desc area
    private val _insertDescAreaDetails = MutableLiveData<Result<TcDescriptionOtherAreasResponse>>()
    val insertDescAreaDetails: LiveData<Result<TcDescriptionOtherAreasResponse>> = _insertDescAreaDetails

    // wash basin
    private val _insertWashBasinDtails = MutableLiveData<Result<ToiletDetailsErrorResponse>>()
    val insertWashBasinDtails: LiveData<Result<ToiletDetailsErrorResponse>> = _insertWashBasinDtails

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

    private val _fieldDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val fieldDetail: LiveData<Result<FieldVerificationDetailResponse>> = _fieldDetail

    private val _finDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val finDetail: LiveData<Result<FieldVerificationDetailResponse>> = _finDetail

    private val _trainingDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val trainingDetail: LiveData<Result<FieldVerificationDetailResponse>> = _trainingDetail

    private val _trainingInfraDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val trainingInfraDetail: LiveData<Result<FieldVerificationDetailResponse>> =
        _trainingInfraDetail

    private val _certificationDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val certificationDetail: LiveData<Result<FieldVerificationDetailResponse>> =
        _certificationDetail

    private val _placementDetail = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val placementDetail: LiveData<Result<FieldVerificationDetailResponse>> = _placementDetail

    private val _submitFieldVerification = MutableLiveData<Result<FieldVerificationDetailResponse>>()
    val submitFieldVerificationDetails: LiveData<Result<FieldVerificationDetailResponse>> = _submitFieldVerification


    //itLab
    private val _insertITTabDtails = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertITTabDtails: LiveData<Result<ITLAbDetailsErrorResponse>> = _insertITTabDtails

    private val _AcademicNonAcademicResponse =
        MutableLiveData<Result<AcademicNonAcademicResponse>>()
    val AcademicNonAcademicResponse: LiveData<Result<AcademicNonAcademicResponse>> =
        _AcademicNonAcademicResponse

    //Office Cum(Counselling room)
    private val _OfficeCumCounsellingroom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val OfficeCumCounsellingroom: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _OfficeCumCounsellingroom

    // ReceptionArea
    private val _ReceptionAreaServices = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val ReceptionAreaServices: LiveData<Result<ITLAbDetailsErrorResponse>> = _ReceptionAreaServices

    //Office room
    private val _Officeroom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val Officeroom: LiveData<Result<ITLAbDetailsErrorResponse>> = _Officeroom

    //IT Come Domain Lab
    private val _ITComeDomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val ITComeDomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _ITComeDomainLab

    // TheoryCumITLab
    private val _TheoryCumITLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryCumITLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryCumITLab

    // TheoryCumDomainLab
    private val _TheoryCumDomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryCumDomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryCumDomainLab

    // DomainLab
    private val _DomainLab = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val DomainLab: LiveData<Result<ITLAbDetailsErrorResponse>> = _DomainLab

    // Theory Class Room
    private val _TheoryClassRoom = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val TheoryClassRoom: LiveData<Result<ITLAbDetailsErrorResponse>> = _TheoryClassRoom

    // Training Center Info
    private val _trainingCentersInfo = MutableLiveData<Result<TrainingCenterInfoRes>>()
    val trainingCentersInfo: LiveData<Result<TrainingCenterInfoRes>> = _trainingCentersInfo

    private val _getTcStaffDetails = MutableLiveData<Result<TcStaffAndTrainerResponse>>()
    val getTcStaffDetails: LiveData<Result<TcStaffAndTrainerResponse>> = _getTcStaffDetails

    private val _getTrainerCenterInfra = MutableLiveData<Result<TcInfraResponse>>()
    val getTrainerCenterInfra: LiveData<Result<TcInfraResponse>> = _getTrainerCenterInfra

    private val _getTcAcademicNonAcademicArea = MutableLiveData<Result<TcAcademiaNonAcademiaRes>>()
    val getTcAcademicNonAcademicArea: LiveData<Result<TcAcademiaNonAcademiaRes>> =
        _getTcAcademicNonAcademicArea

    private val _getTcToiletWashBasin = MutableLiveData<Result<ToiletResponse>>()
    val getTcToiletWashBasin: LiveData<Result<ToiletResponse>> = _getTcToiletWashBasin

    private val _getDescriptionOtherArea = MutableLiveData<Result<DescOtherAreaRes>>()
    val getDescriptionOtherArea: LiveData<Result<DescOtherAreaRes>> = _getDescriptionOtherArea

    private val _getTeachingLearningMaterial = MutableLiveData<Result<TeachingLearningRes>>()
    val getTeachingLearningMaterial: LiveData<Result<TeachingLearningRes>> =
        _getTeachingLearningMaterial

    private val _getGeneralDetails = MutableLiveData<Result<GeneralDetails>>()
    val getGeneralDetails: LiveData<Result<GeneralDetails>> = _getGeneralDetails

    private val _getElectricalWiringStandard = MutableLiveData<Result<ElectricalWireRes>>()
    val getElectricalWiringStandard: LiveData<Result<ElectricalWireRes>> =
        _getElectricalWiringStandard

    private val _getSignagesAndInfoBoard = MutableLiveData<Result<SignageInfo>>()
    val getSignagesAndInfoBoard: LiveData<Result<SignageInfo>> = _getSignagesAndInfoBoard

    private val _getIpEnabledCamera = MutableLiveData<Result<IpEnableRes>>()
    val getIpEnabledCamera: LiveData<Result<IpEnableRes>> = _getIpEnabledCamera

    private val _getCommonEquipment = MutableLiveData<Result<CommonEquipmentRes>>()
    val getCommonEquipment: LiveData<Result<CommonEquipmentRes>> = _getCommonEquipment

    private val _getAvailabilitySupportInfra =
        MutableLiveData<Result<SupportInfrastructureResponse>>()
    val getAvailabilitySupportInfra: LiveData<Result<SupportInfrastructureResponse>> =
        _getAvailabilitySupportInfra

    private val _getAvailabilityStandardForms = MutableLiveData<Result<StandardFormResponse>>()
    val getAvailabilityStandardForms: LiveData<Result<StandardFormResponse>> =
        _getAvailabilityStandardForms

    private val _getAcademicRoomDetails = MutableLiveData<Result<AllRoomDetailResponse>>()
    val getAcademicRoomDetails: LiveData<Result<AllRoomDetailResponse>> = _getAcademicRoomDetails

    private val _insertQTeamVerification = MutableLiveData<Result<InsertTcGeneralDetailsResponse>>()
    val insertQTeamVerification: LiveData<Result<InsertTcGeneralDetailsResponse>> =
        _insertQTeamVerification

    private val _insertSrlmVerification = MutableLiveData<Result<InsertTcGeneralDetailsResponse>>()
    val insertSrlmVerification: LiveData<Result<InsertTcGeneralDetailsResponse>> =
        _insertSrlmVerification

    private val _getFinalSubmitData = MutableLiveData<Result<FinalSubmitRes>>()
    val getFinalSubmitData: LiveData<Result<FinalSubmitRes>> = _getFinalSubmitData

    private val _getSectionsStatusData = MutableLiveData<Result<SectionStatusRes>>()
    val getSectionsStatusData: LiveData<Result<SectionStatusRes>> = _getSectionsStatusData

    // RF Operations
    private val _SubmitRfToiletDataToServer = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val SubmitRfToiletDataToServer: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _SubmitRfToiletDataToServer

    private val _insertRfNonLivingAreaInformation =
        MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertRfNonLivingAreaInformation: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _insertRfNonLivingAreaInformation

    private val _insertRfIndoorGameDetails = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertRfIndoorGameDetails: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _insertRfIndoorGameDetails

    private val _insertResidentialFacilitiesAvailable =
        MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertResidentialFacilitiesAvailable: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _insertResidentialFacilitiesAvailable

    private val _insertRFSupportFacilitiesAvailable =
        MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertRFSupportFacilitiesAvailable: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _insertRFSupportFacilitiesAvailable

    private val _RfBasicInfo = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfBasicInfo: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfBasicInfo

    private val _RfInfra = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfInfra: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfInfra

    private val _RfLivingArea = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val RfLivingArea: LiveData<Result<ITLAbDetailsErrorResponse>> = _RfLivingArea

    private val _ResidentialFacilityQTeam = MutableLiveData<Result<ResidentialFacilityQTeam>>()
    val ResidentialFacilityQTeam: LiveData<Result<ResidentialFacilityQTeam>> =
        _ResidentialFacilityQTeam

    private val _CompliancesRFQTReqRFQT =
        MutableLiveData<Result<InfrastructureDetailsandCompliancesRFQT>>()
    val CompliancesRFQTReqRFQT: LiveData<Result<InfrastructureDetailsandCompliancesRFQT>> =
        _CompliancesRFQTReqRFQT

    private val _fLivingAreaInformation = MutableLiveData<Result<RfLivingAreaInformationResponse>>()
    val fLivingAreaInformation: LiveData<Result<RfLivingAreaInformationResponse>> =
        _fLivingAreaInformation

    private val _livingRoomListView = MutableLiveData<Result<LivingRoomListViewRes>>()
    val livingRoomListView: LiveData<Result<LivingRoomListViewRes>> = _livingRoomListView

    private val _ToiletRoomListView = MutableLiveData<Result<ToiletViewRes>>()
    val ToiletRoomListView: LiveData<Result<ToiletViewRes>> = _ToiletRoomListView

    private val _ToiletRoomInformationView = MutableLiveData<Result<ToiletRoomInformationViewRes>>()
    val ToiletRoomInformationView: LiveData<Result<ToiletRoomInformationViewRes>> =
        _ToiletRoomInformationView

    private val _getRfLivingRoomListView = MutableLiveData<Result<LivingAreaListRes>>()
    val getRfLivingRoomListView: LiveData<Result<LivingAreaListRes>> = _getRfLivingRoomListView

    private val _deleteLivingRoom = MutableLiveData<Result<LivingAreaDelete>>()
    val deleteLivingRoom: LiveData<Result<LivingAreaDelete>> = _deleteLivingRoom

    private val _getRfToiletListView = MutableLiveData<Result<ToiletListRes>>()
    val getRfToiletListView: LiveData<Result<ToiletListRes>> = _getRfToiletListView

    private val _toiletSectionListView = MutableLiveData<Result<ToiletListRes>>()
    val toiletSectionListView: LiveData<Result<ToiletListRes>> = _toiletSectionListView

    private val _deleteToiletRoom = MutableLiveData<Result<LivingAreaDelete>>()
    val deleteToiletRoom: LiveData<Result<LivingAreaDelete>> = _deleteToiletRoom

    private val _NonAreaInformationRoom = MutableLiveData<Result<NonAreaInformationRoom>>()
    val NonAreaInformationRoom: LiveData<Result<NonAreaInformationRoom>> = _NonAreaInformationRoom

    private val _RfIndoorGameDetails = MutableLiveData<Result<IndoorRFGameResponse>>()
    val RfIndoorGameDetails: LiveData<Result<IndoorRFGameResponse>> = _RfIndoorGameDetails

    private val _RFResidentialFacilitiesAvailable =
        MutableLiveData<Result<RFResidintialFacilityResponse>>()
    val RFResidentialFacilitiesAvailable: LiveData<Result<RFResidintialFacilityResponse>> =
        _RFResidentialFacilitiesAvailable

    private val _RFSupportFacilitiesAvailable =
        MutableLiveData<Result<RFSupportFacilitiesAvailableResponse>>()
    val RFSupportFacilitiesAvailable: LiveData<Result<RFSupportFacilitiesAvailableResponse>> =
        _RFSupportFacilitiesAvailable

    private val _insertRFQteamVerification = MutableLiveData<Result<FinalSubmitRes>>()
    val insertRFQteamVerification: LiveData<Result<FinalSubmitRes>> = _insertRFQteamVerification

    private val _insertRFSrlmVerification = MutableLiveData<Result<FinalSubmitRes>>()
    val insertRFSrlmVerification: LiveData<Result<FinalSubmitRes>> = _insertRFSrlmVerification

    private val _getRFSectionStatus = MutableLiveData<Result<SectionResponse>>()
    val getRFSectionStatus: LiveData<Result<SectionResponse>> = _getRFSectionStatus

    private val _insertRFFinalSubmission = MutableLiveData<Result<RfFinalSubmitRes>>()
    val insertRFFinalSubmission: LiveData<Result<RfFinalSubmitRes>> = _insertRFFinalSubmission

    private val _saveInitialResidentialFacility = MutableLiveData<Result<AddNewRFRes>>()
    val saveInitialResidentialFacility: LiveData<Result<AddNewRFRes>> =
        _saveInitialResidentialFacility

    private val _getResidentialList = MutableLiveData<Result<ModifyRFRes>>()
    val getResidentialList: LiveData<Result<ModifyRFRes>> = _getResidentialList

    private val _insertRfToiletWashRoomDetail = MutableLiveData<Result<ITLAbDetailsErrorResponse>>()
    val insertRfToiletWashRoomDetail: LiveData<Result<ITLAbDetailsErrorResponse>> =
        _insertRfToiletWashRoomDetail

    private val _getToiletWashbasinDetails = MutableLiveData<Result<GetUrinalWashRes>>()
    val getToiletWashbasinDetails: LiveData<Result<GetUrinalWashRes>> = _getToiletWashbasinDetails

    private val _ToiletCountListView = MutableLiveData<Result<ToiletCountList>>()
    val ToiletCountListView: LiveData<Result<ToiletCountList>> = _ToiletCountListView

    // Helper method for API calls
    private fun <T> handleApiCall(
        apiCall: suspend () -> Result<T>,
        resultLiveData: MutableLiveData<Result<T>>? = null
    ) {
        _loading.postValue(true)
        viewModelScope.launch {
            try {
                val result = apiCall()
                result.onFailure { throwable ->
                    if (throwable is retrofit2.HttpException && throwable.code() == 401) {
                        _sessionExpired.postValue(true)
                    } else {
                        _errorMessage.postValue(throwable.message ?: "Unknown error")
                    }
                }
                resultLiveData?.postValue(result)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "Unknown error")
                resultLiveData?.postValue(Result.failure(e))
            } finally {
                _loading.postValue(false)
            }
        }
    }

    // Field Verification Methods
    fun getFieldVerificationDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = { repositoryManager.fieldVerification.getFieldVerificationDetail(request) },
            resultLiveData = _fieldDetail
        )
    }

    fun getFieldVerificationFinDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = { repositoryManager.fieldVerification.getFieldVerificationFinDetail(request) },
            resultLiveData = _finDetail
        )
    }

    fun getFieldVerificationTrainingDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.getFieldVerificationTrainingDetail(
                    request
                )
            },
            resultLiveData = _trainingDetail
        )
    }

    fun getFieldVerificationTrainingInfraDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.getFieldVerificationTrainingInfraDetail(
                    request
                )
            },
            resultLiveData = _trainingInfraDetail
        )
    }

    fun getFieldVerificationCertificationDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.getFieldVerificationCertificationDetail(
                    request
                )
            },
            resultLiveData = _certificationDetail
        )
    }

    fun getFieldVerificationPlacementDetail(request: FieldVerificationDetailRequest) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.getFieldVerificationPlacementDetail(
                    request
                )
            },
            resultLiveData = _placementDetail
        )
    }

    fun submitFieldVerification(request: FieldVerificationFinalSubmit) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.submitFieldVerification(request)

            },
            resultLiveData = _submitFieldVerification
        )

//        _loading.postValue(true)
//        viewModelScope.launch {
//            val result = repository.submitFieldVerification(request)
//            result.onFailure {
//                _errorMessage.postValue(it.message ?: "Unknown error")
//            }
//            _submitFieldVerification.postValue(result)
//            _loading.postValue(false)
//        }
    }

    fun submitElectricalData(request: ElectricalWiringRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.infrastructure.submitWiringDataToServer(request, token) },
            resultLiveData = _insertIpenabledata
        )
    }

    // Login API call
    fun loginUser(request: LoginRequest) {
        _loading.postValue(true)
        viewModelScope.launch {
            val result = repositoryManager.auth.loginUser(request)
            result.onSuccess { response ->
                // Handle success if needed
            }
            result.onFailure { throwable ->
                if (throwable is retrofit2.HttpException && throwable.code() == 401) {
                    _sessionExpired.postValue(true)
                } else {
                    _errorMessage.postValue(throwable.message ?: "Unknown error")
                }
            }
            _loginResult.postValue(result)
            _loading.postValue(false)
        }
    }

    // fetch Module and forms
    fun fetch(modulesRequest: ModulesRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.auth.fetchModules(modulesRequest, token) },
            resultLiveData = _modules
        )
    }

    fun fetchTrainingCenters(request: TrainingCenterRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.fetchTrainingCenters(request, token) },
            resultLiveData = _trainingCenters
        )
    }

    fun fetchQTeamTrainingList(request: TrainingCenterRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.fetchQTeamTrainingList(request, token) },
            resultLiveData = _trainingCenters
        )
    }

    fun fetchFieldVerificationList(request: FieldVerificationListRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.fieldVerification.fetchFieldVerificationList(
                    request,
                    token
                )
            },
            resultLiveData = _fieldprnDetails
        )
    }

    fun fetchSrlmTeamTrainingList(request: TrainingCenterRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.trainingCenter.fetchSrlmTeamTrainingList(
                    request,
                    token
                )
            },
            resultLiveData = _trainingCenters
        )
    }

    fun fetchRfList(request: TrainingCenterRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.fetchRfList(request, token) },
            resultLiveData = _rfTrainingCenters
        )
    }

    //IP enabled camera insert
    fun submitCCTVDataToServer(request: CCTVComplianceRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.infrastructure.submitCCTVDataToServer(request, token) },
            resultLiveData = _insertCCTVdata
        )
    }

    //wash basin
    fun SubmitWashBasinDataToServer(request: ToiletDetailsRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.infrastructure.submitWashbsinDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _insertWashBasinDtails
        )
    }

    // general details insert
    fun submitGeneralDetails(request: InsertTcGeneralDetailsRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.infrastructure.submitGeneralDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _insertGeneralDetails
        )
    }

    //TC basic info
    fun submitTcBasicDataToServer(request: TcBasicInfoRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.infrastructure.submitTcBasicDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _insertTCInfoDeatils
        )
    }

    fun submitTcInfoSignageDataToServer(request: TcSignagesInfoBoardRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.infrastructure.submitSignagesBoardsDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _insertSignagesInfoBoardsDetails
        )
    }

    fun submitTcSupportInfraDataToserver(
        request: TcAvailabilitySupportInfraRequest,
        token: String
    ) {
        handleApiCall(
            apiCall = { repositoryManager.infrastructure.submitInfraDataToServer(request, token) },
            resultLiveData = _insertSupportInfraDetails
        )
    }

    fun submitTcCommonEquipment(request: TcCommonEquipmentRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.infrastructure.submitCommonEquipmentDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _insertCommonEquipDetails
        )
    }

    fun submitTcDescriptionArea(request: TcDescriptionOtherAreasRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.infrastructure.submitDescDataToServer(request, token) },
            resultLiveData = _insertDescAreaDetails
        )
    }

    fun getTrainerCenterInfo(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTrainerCenterInfo(request) },
            resultLiveData = _trainingCentersInfo
        )
    }

    fun getTcStaffDetails(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTcStaffDetails(request) },
            resultLiveData = _getTcStaffDetails
        )
    }

    fun getTrainerCenterInfra(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTrainerCenterInfra(request) },
            resultLiveData = _getTrainerCenterInfra
        )
    }

    fun getTcAcademicNonAcademicArea(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTcAcademicNonAcademicArea(request) },
            resultLiveData = _getTcAcademicNonAcademicArea
        )
    }

    fun getTcToiletWashBasin(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTcToiletWashBasin(request) },
            resultLiveData = _getTcToiletWashBasin
        )
    }

    fun getDescriptionOtherArea(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getDescriptionOtherArea(request) },
            resultLiveData = _getDescriptionOtherArea
        )
    }

    fun getTeachingLearningMaterial(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getTeachingLearningMaterial(request) },
            resultLiveData = _getTeachingLearningMaterial
        )
    }

    fun getGeneralDetails(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getGeneralDetails(request) },
            resultLiveData = _getGeneralDetails
        )
    }

    fun getElectricalWiringStandard(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getElectricalWiringStandard(request) },
            resultLiveData = _getElectricalWiringStandard
        )
    }

    fun getSignagesAndInfoBoard(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getSignagesAndInfoBoard(request) },
            resultLiveData = _getSignagesAndInfoBoard
        )
    }

    fun getIpEnabledCamera(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getIpEnabledcamera(request) },
            resultLiveData = _getIpEnabledCamera
        )
    }

    fun getCommonEquipment(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getCommonEquipment(request) },
            resultLiveData = _getCommonEquipment
        )
    }

    fun getAvailabilitySupportInfra(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getAvailabilitySupportInfra(request) },
            resultLiveData = _getAvailabilitySupportInfra
        )
    }

    fun getAvailabilityStandardForms(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getAvailabilityStandardForms(request) },
            resultLiveData = _getAvailabilityStandardForms
        )
    }

    fun getAcademicRoomDetails(request: AllRoomDetaisReques) {
        handleApiCall(
            apiCall = { repositoryManager.academic.getAcademicRoomDetails(request) },
            resultLiveData = _getAcademicRoomDetails
        )
    }



    private val _postOnAUAFaceAuthNREGA = MutableLiveData<Result<UidaiResp>>()
    val postOnAUAFaceAuthNREGA: LiveData<Result<UidaiResp>> = _postOnAUAFaceAuthNREGA



    fun postOnAUAFaceAuthNREGA(url:String, uidaiKycRequest: UidaiKycRequest){
        handleApiCall(
            apiCall = { repositoryManager.academic.postOnAUAFaceAuthNREGA(url,uidaiKycRequest) },
            resultLiveData = _postOnAUAFaceAuthNREGA
        )
    }

    fun insertQTeamVerification(request: TcQTeamInsertReq) {
        handleApiCall(
            apiCall = { repositoryManager.verification.insertQTeamVerification(request) },
            resultLiveData = _insertQTeamVerification
        )
    }

    fun insertSrlmVerification(request: TcQTeamInsertReq) {
        handleApiCall(
            apiCall = { repositoryManager.verification.insertSrlmVerification(request) },
            resultLiveData = _insertSrlmVerification
        )
    }

    fun getFinalSubmitData(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getFinalSubmitData(request) },
            resultLiveData = _getFinalSubmitData
        )
    }

    fun getSectionsStatusData(request: TrainingCenterInfo) {
        handleApiCall(
            apiCall = { repositoryManager.trainingCenter.getSectionsStatus(request) },
            resultLiveData = _getSectionsStatusData
        )
    }

    fun getStateList(request: StateRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.location.getStateList(request, token) },
            resultLiveData = _stateList
        )
    }

    fun getDistrictList(request: DistrictRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.location.getDistrictList(request, token) },
            resultLiveData = _districtList
        )
    }

    fun getBlockList(request: BlockRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.location.getBlockList(request, token) },
            resultLiveData = _blockList
        )
    }

    fun getGpList(request: GpRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.location.getGpList(request, token) },
            resultLiveData = _gpList
        )
    }

    fun getVillageList(request: VillageReq, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.location.getVillageList(request, token) },
            resultLiveData = _villageList
        )
    }


    private val _getUlbList = MutableLiveData<Result<UlbRes>>()
    val getUlbList: LiveData<Result<UlbRes>> = _getUlbList


    fun getUlbAPI(ulbReq: ULBReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getUlbAPI(ulbReq, header) },
            resultLiveData = _getUlbList
        )
    }


    private val _getWardAPI = MutableLiveData<Result<WardRes>>()
    val getWardAPI: LiveData<Result<WardRes>> = _getWardAPI

    fun getWardAPI(wardReq: WardReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getWardAPI(wardReq, header) },
            resultLiveData = _getWardAPI
        )

    }




    private val _getAttendanceBatchListAPI = MutableLiveData<Result<AttendanceBatchRes>>()
    val getAttendanceBatchListAPI: LiveData<Result<AttendanceBatchRes>> = _getAttendanceBatchListAPI

    fun getAttendanceBatchListAPI(attendanceBatchListReq: AttendanceBatchListReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getAttendanceBatchListAPI(attendanceBatchListReq, header) },
            resultLiveData = _getAttendanceBatchListAPI
        )

    }




    private val _getAttendanceCandidateListAPI = MutableLiveData<Result<AttendanceCandidateRes>>()
    val getAttendanceCandidateListAPI: LiveData<Result<AttendanceCandidateRes>> = _getAttendanceCandidateListAPI

    fun getAttendanceCandidateListAPI(attendanceCandidateListReq: AttendanceCandidateListReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getAttendanceCandidateListAPI(attendanceCandidateListReq, header) },
            resultLiveData = _getAttendanceCandidateListAPI
        )

    }





    private val _getAttendanceCheckAPI = MutableLiveData<Result<AttendanceCheckRes>>()
    val getAttendanceCheckAPI: LiveData<Result<AttendanceCheckRes>> = _getAttendanceCheckAPI

        fun getAttendanceCheckAPI(attendanceCheckReq: AttendanceCheckReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getAttendanceCheckAPI(attendanceCheckReq, header) },
            resultLiveData = _getAttendanceCheckAPI
        )

    }


    private val _insertAttendance = MutableLiveData<Result<AttendanceInsertRes>>()
    val insertAttendance: LiveData<Result<AttendanceInsertRes>> = _insertAttendance

    fun insertAttendance(attendanceInsertReq: AttendanceInsertReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.insertAttendance(attendanceInsertReq, header) },
            resultLiveData = _insertAttendance
        )

    }



    private val _insertFacultyAttandance = MutableLiveData<Result<AttendanceInsertRes>>()
    val insertFacultyAttandance: LiveData<Result<AttendanceInsertRes>> = _insertFacultyAttandance

    fun insertFacultyAttandance(insertFacultyAttendance: InsertFacultyAttendance,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.insertFacultyAttandance(insertFacultyAttendance, header) },
            resultLiveData = _insertFacultyAttandance
        )

    }





    private val _getFacultyDetails = MutableLiveData<Result<FacultyDetailsRes>>()
    val getFacultyDetails: LiveData<Result<FacultyDetailsRes>> = _getFacultyDetails

    fun getFacultyDetails(attendanceCandidateListReq: AttendanceCandidateListReq,header :String){
        handleApiCall(
            apiCall = { repositoryManager.location.getFacultyDetails(attendanceCandidateListReq, header) },
            resultLiveData = _getFacultyDetails
        )

    }






    // Ajit Ranjan (RecyclerView)
    fun DesriptionAcademicNonList(request: AcademicNonAcademicArea, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.academic.submitDesriptionAcademicNonDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _AcademicNonAcademicResponse
        )
    }

    // Ajit Ranjan (IT LAB)
    fun SubmitITLABDataToServer(request: ITLabDetailsRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitITLabDataToServer(request, token) },
            resultLiveData = _insertITTabDtails
        )
    }

    // Office Cum(Counselling room) Ajit Ranjan
    fun SubmitOfficeCumCounsellingRoomDataToServer(
        request: SubmitOfficeCumCounsellingRoomDetailsRequest,
        token: String
    ) {
        handleApiCall(
            apiCall = {
                repositoryManager.academic.submitOfficeCumCounsellingroomDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _OfficeCumCounsellingroom
        )
    }

    // ReceptionArea Ajit Ranjan
    fun SubmitReceptionAreaDataToServer(request: ReceptionAreaRoomDetailsRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.academic.submitReceptionAreaDataToServer(
                    request,
                    token
                )
            },
            resultLiveData = _ReceptionAreaServices
        )
    }

    // Office Room Ajit Ranjan
    fun SubmitOfficeRoomDataToServer(request: OfficeRoomDetailsRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitOfficeRoomDataToServer(request, token) },
            resultLiveData = _Officeroom
        )
    }

    // IT Come Domain Lab Ajit Ranjan
    fun SubmitITComeDomainLabDataToServer(request: ITComeDomainLabDetailsRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitItComeDomainlabToServer(request, token) },
            resultLiveData = _ITComeDomainLab
        )
    }

    // Theory Cum IT Lab Lab
    fun SubmitTheoryComeItLabDataToServer(request: TCITLDomainLabDetailsRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitTheoryCumITLabToServer(request, token) },
            resultLiveData = _TheoryCumITLab
        )
    }

    // Theory Cum Domain Lab Lab
    fun SubmitTCDLDataToServer(request: TCDLRequest, token: String) {
        handleApiCall(
            apiCall = {
                repositoryManager.academic.submitTheoryCumDomainLabToServer(
                    request,
                    token
                )
            },
            resultLiveData = _TheoryCumDomainLab
        )
    }

    // Domain Lab
    fun SubmitDLDataToServer(request: DLRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitDomainLabToServer(request, token) },
            resultLiveData = _DomainLab
        )
    }

    // Theory Class Room
    fun SubmitTheoryClassRoomDataToServer(request: TCRRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.academic.submitTheoryClassRoomToServer(request, token) },
            resultLiveData = _TheoryClassRoom
        )
    }


    fun SubmitRfToiletDataToServer(request: InsertToiletDataReq, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfToiletRoomInformation(request, token) },
            resultLiveData = _SubmitRfToiletDataToServer
        )
    }

    fun SubmitRfNonLivingAreaDataToServer(request: InsertNonLivingReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfNonLivingAreaInformation(request) },
            resultLiveData = _insertRfNonLivingAreaInformation
        )
    }

    fun SubmitRfIndoorGameDetails(request: IndoorGamesRequest) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfIndoorGameDetails(request) },
            resultLiveData = _insertRfIndoorGameDetails
        )
    }

    fun SubmitRfAvaibilityDetails(request: InsertResidentialFacility) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertResidentialFacilitiesAvailable(request) },
            resultLiveData = _insertResidentialFacilitiesAvailable
        )
    }

    fun SubmitRfSupportFacilitiesDetails(request: InsertSupportFacilitiesReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRFSupportFacilitiesAvailable(request) },
            resultLiveData = _insertRFSupportFacilitiesAvailable
        )
    }

    fun SubmitRfBasicInformationToServer(request: insertRfBasicInfoReq, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfBasicInformation(request, token) },
            resultLiveData = _RfBasicInfo
        )
    }

    fun SubmitRfInfraDetailsAndComlianceToServer(request: InsertRfInfraDetaiReq, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfInfraDetailsAndComliance(request, token) },
            resultLiveData = _RfInfra
        )
    }

    fun SubmitRfLivingAreaInformationToServer(request: InsertLivingAreaReq, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.insertRfLivingAreaInformation(request, token) },
            resultLiveData = _RfLivingArea
        )
    }

    // ResidentialFacilityQTeamRequest Ajit Ranjan 16/10/2025
    fun fetchResidentialFacilityQTeamList(request: ResidentialFacilityQTeamRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.fetchResidentialFacilityQTeamist(request, token) },
            resultLiveData = _trainingRfCenters
        )
    }

    // GetRfBasicInformation AjitRanjan 17/10/2025
    fun getRfBasicInformationrInfo(request: RfCommonReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getTRfBasicInfo(request) },
            resultLiveData = _ResidentialFacilityQTeam
        )
    }

    // Ajit Ranjan create 21/October/2025 CompliancesRFQTReqRFQT
    fun getCompliancesRFQTReqRFQT(request: CompliancesRFQTReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getCompliancesRFQTReqRFQT(request) },
            resultLiveData = _CompliancesRFQTReqRFQT
        )
    }

    // Ajit Ranjan create 24/October/2025 getRfLivingAreaInformation
    fun getRfLivingAreaInformation(request: RfLivingAreaInformationRQ) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRfLivingAreaInformation(request) },
            resultLiveData = _fLivingAreaInformation
        )
    }

    // Ajit Ranjan create 27/October/2025 getlivingRoomListView
    fun getlivingRoomListView(request: LivingRoomListViewRQ) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRflivingRoomListView(request) },
            resultLiveData = _livingRoomListView
        )
    }

    // Ajit Ranjan create 27/October/2025 toiletRoomListView
    fun getToiletRoomListView(request: ToiletRoomInformationReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getToiletRoomListView(request) },
            resultLiveData = _ToiletRoomListView
        )
    }

    // Ajit Ranjan create 30/October/2025 getRfToiletRoomInformation
    fun getRfToiletRoomInformation(request: ToiletRoomReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getToiletRoomInformation(request) },
            resultLiveData = _ToiletRoomInformationView
        )
    }

    fun getRfLivingRoomListView(request: LivingRoomReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRfLivingRoomListView(request) },
            resultLiveData = _getRfLivingRoomListView
        )
    }

    fun deleteLivingRoom(request: DeleteLivingRoomList) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.deleteLivingRoom(request) },
            resultLiveData = _deleteLivingRoom
        )
    }

    fun getRfToiletListView(request: LivingRoomReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRfToiletListView(request) },
            resultLiveData = _getRfToiletListView
        )
    }

    fun getToiletSectionListView(request: LivingRoomReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.toiletSectionListView(request) },
            resultLiveData = _toiletSectionListView
        )
    }

    fun deleteToiletRoom(request: ToiletDeleteList) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.deleteToiletRoom(request) },
            resultLiveData = _deleteToiletRoom
        )
    }

    // Ajit Ranjan create 03/November/2025 getRfNonLivingAreaInformation
    fun getRfNonLivingAreaInformation(request: LivingRoomListViewRQ) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRfNonLivingAreaInformation(request) },
            resultLiveData = _NonAreaInformationRoom
        )
    }

    // Ajit Ranjan create 04/November/2025 getRfIndoorGameDetails
    fun getRfIndoorGameDetails(request: RFGameRequest) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRfInGaDetails(request) },
            resultLiveData = _RfIndoorGameDetails
        )
    }

    // Ajit Ranjan create 06/November/2025 getResidentialFacilitiesAvailable
    fun getResidentialFacilitiesAvailable(request: RfCommonReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getResidentialFacilitiesAvailable(request) },
            resultLiveData = _RFResidentialFacilitiesAvailable
        )
    }

    // Ajit Ranjan create 07/November/2025 getRFSupportFacilitiesAvailable
    fun getRFSupportFacilitiesAvailable(request: RFGameRequest) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRFSupportFacilitiesAvailable(request) },
            resultLiveData = _RFSupportFacilitiesAvailable
        )
    }

    // Ajit Ranjan create 07/November/2025 insertRFQteamVerificationRequest
    fun getFinalSubmitinsertRFQteamVerificationRequestData(request: RFQteamVerificationRequest) {
        handleApiCall(
            apiCall = { repositoryManager.verification.getFinalSubmitinsertRFQteamVerificationRequestData(request) },
            resultLiveData = _insertRFQteamVerification
        )
    }

    // Ajit Ranjan create 07/November/2025 insertRFSrlmVerification
    fun getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(request: RFQteamVerificationRequest) {
        handleApiCall(
            apiCall = { repositoryManager.verification.getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(request) },
            resultLiveData = _insertRFSrlmVerification
        )
    }

    // Ajit Ranjan create 07/November/2025 getRFSRLMVerification
    fun getRFSRLMVerification(request: TrainingCenterRequest, token: String) {
        handleApiCall(
            apiCall = { repositoryManager.verification.fetchRFSRLMVerificationList(request, token) },
            resultLiveData = _trainingRfCenters
        )
    }

    fun getRFSectionStatus(request: SectionReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getRFSectionStatus(request) },
            resultLiveData = _getRFSectionStatus
        )
    }

    fun insertRFFinalSubmission(request: RfFinalSubmitReq) {
        handleApiCall(
            apiCall = { repositoryManager.rfOperations.insertRFFinalSubmission(request) },
            resultLiveData = _insertRFFinalSubmission
        )
    }

    fun saveInitialResidentialFacility(request: AddNewRFReq) {
        handleApiCall(
            apiCall = { repositoryManager.rfOperations.saveInitialResidentialFacility(request) },
            resultLiveData = _saveInitialResidentialFacility
        )
    }

    fun getResidentialList(request: ModifyRfList) {
        handleApiCall(
            apiCall = { repositoryManager.rfOperations.getResidentialList(request) },
            resultLiveData = _getResidentialList
        )
    }

    fun insertRfToiletWashRoomDetail(request: UrinalWashbasinReq) {
        handleApiCall(
            apiCall = { repositoryManager.rfOperations.insertRfToiletWashRoomDetail(request) },
            resultLiveData = _insertRfToiletWashRoomDetail
        )
    }

    fun getToiletWashbasinDetails(request: GetUrinalWashReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getToiletWashbasinDetails(request) },
            resultLiveData = _getToiletWashbasinDetails
        )
    }

    // Ajit Ranjan create 17/Nov/2025 getToiletCountList
    fun getToiletCountList(request: ToiletCountListReq) {
        handleApiCall(
            apiCall = { repositoryManager.residentialFacility.getToiletCountList(request) },
            resultLiveData = _ToiletCountListView
        )
    }
}
