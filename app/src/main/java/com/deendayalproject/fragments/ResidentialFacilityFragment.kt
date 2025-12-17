package com.deendayalproject.fragments


import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.BlockAdapter
import com.deendayalproject.adapter.DistrictAdapter
import com.deendayalproject.adapter.IndoorGameAdapter
import com.deendayalproject.adapter.LivingRoomListAdapter
import com.deendayalproject.adapter.PanchayatAdapter
import com.deendayalproject.adapter.StateAdapter
import com.deendayalproject.adapter.ToiletAdapter
import com.deendayalproject.adapter.VillageAdapter
import com.deendayalproject.databinding.FragmentResidentialBinding
import com.deendayalproject.model.IndoorGame
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.CompliancesRFQTReq
import com.deendayalproject.model.request.DeleteLivingRoomList
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.GetUrinalWashReq
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.IndoorGameItem
import com.deendayalproject.model.request.IndoorGamesRequest
import com.deendayalproject.model.request.InsertLivingAreaReq
import com.deendayalproject.model.request.InsertNonLivingReq
import com.deendayalproject.model.request.InsertResidentialFacility
import com.deendayalproject.model.request.InsertRfInfraDetaiReq
import com.deendayalproject.model.request.InsertSupportFacilitiesReq
import com.deendayalproject.model.request.InsertToiletDataReq
import com.deendayalproject.model.request.LivingRoomListViewRQ
import com.deendayalproject.model.request.LivingRoomReq
import com.deendayalproject.model.request.RFGameRequest
import com.deendayalproject.model.request.RfCommonReq
import com.deendayalproject.model.request.RfFinalSubmitReq
import com.deendayalproject.model.request.SectionReq
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.ToiletDeleteList
import com.deendayalproject.model.request.ULBReq
import com.deendayalproject.model.request.UrinalWashbasinReq
import com.deendayalproject.model.request.VillageReq
import com.deendayalproject.model.request.WardReq
import com.deendayalproject.model.request.insertRfBasicInfoReq
import com.deendayalproject.model.response.BlockModel
import com.deendayalproject.model.response.DistrictModel
import com.deendayalproject.model.response.GpModel
import com.deendayalproject.model.response.LivingRoomListItem
import com.deendayalproject.model.response.SectionRFData
import com.deendayalproject.model.response.StateModel
import com.deendayalproject.model.response.ToiletItem
import com.deendayalproject.model.response.UlbItem
import com.deendayalproject.model.response.VillageModel
import com.deendayalproject.model.response.WardItem
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResidentialFacilityFragment : Fragment() {

    private var _binding: FragmentResidentialBinding? = null
    private val binding get() = _binding!!
    private lateinit var sectionsStatus: SectionRFData
    private lateinit var viewModel: SharedViewModel
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private var currentPhotoTarget: String = ""
    private var selectedRfFoodPre: String = ""
    private var latValue: String = ""
    private var selectedRoomPermitted: Int = 0
    private var langValue: String = ""
    private var deletedItem: LivingRoomListItem? = null
    private var deletedToiletItem: ToiletItem? = null
    private var base64PVDocFile: String? = ""
    private var base64ALDocFile: String? = ""
    private var base64OwnerBuildingDocFile: String? = ""
    private var base64BuildingAreaDocFile: String? = ""
    private var base64RoofOfBuildingDocFile: String? = ""
    private var base64WhetherStructurallySoundDocFile: String? = ""
    private var base64SignOfLeakageDocFile: String? = ""
    private var base64ConformanceDDUGKYDocFile: String? = ""
    private var base64ProtectionofStairsDocFile: String? = ""
    private var base64CirculatingAreaProof: String? = ""
    private var base64CorridorDocFile: String? = ""
    private var base64SecuringWiresDocFile: String? = ""
    private var base64SwitchBoardsDocFile: String? = ""
    private var base64HostelNameBoardDocFile: String? = ""
    private var base64EntitlementBoardDocFile: String? = ""
    private var base64ContactDetailDocFile: String? = ""
    private var base64BasicInfoBoardDocFile: String? = ""
    private var base64FoodSpecificationBoardDocFile: String? = ""
    private var base64TypeLivingRoofDocFile: String? = ""
    private var base64CeilingDocFile: String? = ""
    private var base64AirConditioningDocFile: String? = ""
    private var base64AirHieghtOfCelingDocFile: String? = ""
    private var base64WindowAreaDocFile: String? = ""
    private var base64CotDocFile: String? = ""
    private var base64MattressDocFile: String? = ""
    private var base64BedSheetDocFile: String? = ""
    private var base64CupBoardDocFile: String? = ""
    private var base64LightsDocFile: String? = ""
    private var base64FansDocFile: String? = ""
    private var base64LivingAreaInfoBoardDocFile: String? = ""
    private var base64LightsInToiletDocFile: String? = ""
    private var base64ToiletFlooringDocFile: String? = ""
    private var base64UrinalsDocFile: String? = ""
    private var base64WashBasinDocFile: String? = ""
    private var base64OverHeadTankDocFile: String? = ""

    private var base64RunningWaterDocFile: String? = ""
    private var base64FoodPreparedTrainingDocFile: String? = ""
    private var base64IndoorGameDocFile: String = ""
    private var base64ReceptionAreaDocFile: String? = ""
    private var base64RecreationAreaDocFile: String? = ""
    private var base64DinningAreaDocFile: String? = ""
    private var base64RecreationDiningAreaDocFile: String? = ""
    private var base64WhetherHostelsSeparatedDocFile: String? = ""
    private var base64WardenWhereMalesStayDocFile: String? = ""
    private var base64WardenWhereLadyStayDocFile: String? = ""
    private var base64SecurityGaurdsDocFile: String? = ""
    private var base64WhetherFemaleDoctorDocFile: String? = ""
    private var base64WhetherMaleDoctorDocFile: String? = ""
    private var base64SafeDrinkingDocFile: String? = ""
    private var base64FirstAidKitDocFile: String? = ""
    private var base64FireFightingEquipmentDocFile: String? = ""
    private var base64BiometricDeviceDocFile: String? = ""
    private var base64ElectricalPowerDocFile: String? = ""
    private var base64GrievanceRegisterDocFile: String? = ""
    private val progress: AlertDialog? by lazy {
        AppUtil.getProgressDialog(context)
    }

    private lateinit var ivPoliceVerificationDocPreview: ImageView
    private lateinit var ivAppointmentLetterDocPreview: ImageView
    private lateinit var ivOwnerBuildingDocPreview: ImageView
    private lateinit var ivRoofOfBuildingPreview: ImageView
    private lateinit var ivWhetherStructurallySoundPreview: ImageView
    private lateinit var ivSignOfLeakagePreview: ImageView
    private lateinit var ivConformanceDDUGKYPreview: ImageView
    private lateinit var ivProtectionofStairsPreview: ImageView
    private lateinit var ivCorridorPreview: ImageView
    private lateinit var ivSecuringWiresPreview: ImageView
    private lateinit var ivSwitchBoardsPreview: ImageView
    private lateinit var ivHostelNameBoardPreview: ImageView
    private lateinit var ivEntitlementBoardPreview: ImageView
    private lateinit var ivContactDetailPreview: ImageView
    private lateinit var ivBasicInfoBoardPreview: ImageView
    private lateinit var ivFoodSpecificationBoardPreview: ImageView
    private lateinit var ivTypeLivingRoofPreview: ImageView
    private lateinit var ivCeilingPreview: ImageView
    private lateinit var ivAirConditioningPreview: ImageView
    private lateinit var ivLivingAreaInfoBoardPreview: ImageView
    private lateinit var ivLightsInToiletPreview: ImageView
    private lateinit var ivToiletFlooringPreview: ImageView
    private lateinit var ivFoodPreparedTrainingPreview: ImageView
    private lateinit var ivReceptionAreaPreview: ImageView
    private lateinit var ivWhetherHostelsSeparatedPreview: ImageView
    private lateinit var ivWardenWhereMalesStayPreview: ImageView
    private lateinit var ivWardenWhereLadyStayPreview: ImageView
    private lateinit var ivSecurityGaurdsPreview: ImageView
    private lateinit var ivWhetherFemaleDoctorPreview: ImageView
    private lateinit var ivWhetherMaleDoctorPreview: ImageView
    private lateinit var ivSafeDrinkingPreview: ImageView
    private lateinit var ivFirstAidKitPreview: ImageView
    private lateinit var ivFireFightingEquipmentPreview: ImageView
    private lateinit var ivBiometricDevicePreview: ImageView
    private lateinit var ivElectricalPowerPreview: ImageView
    private lateinit var ivGrievanceRegisterPreview: ImageView

    /////////////////////////Basic Info Section////////////
    private lateinit var etFacilityName: TextInputEditText
    private lateinit var etHouseNo: TextInputEditText
    private lateinit var etStreet: TextInputEditText
    private lateinit var etPoliceStation: TextInputEditText
    private lateinit var etLandmark: TextInputEditText
    private lateinit var etPinCode: TextInputEditText
    private lateinit var etMobile: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etDistanceFromBusStand: TextInputEditText
    private lateinit var etDistanceFromAutoStand: TextInputEditText
    private lateinit var etDistanceFromRailwayStand: TextInputEditText
    private lateinit var etDistanceFromTrainingToResidentialCentre: TextInputEditText
    private lateinit var etWardenName: TextInputEditText
    private lateinit var etWardenEmpID: TextInputEditText
    private lateinit var etWardenAddress: TextInputEditText
    private lateinit var etWardenEmailId: TextInputEditText
    private lateinit var etWardenMobile: TextInputEditText
    private lateinit var spinnerState: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerBlock: Spinner
    private lateinit var spinnerGp: Spinner
    private lateinit var spinnerVillage: Spinner
    private lateinit var spinnerTypeOfArea: Spinner
    private lateinit var spinnerCatOfTCLocation: Spinner
    private lateinit var spinnerPickupAndDropFacility: Spinner
    private lateinit var spinnerWardenGender: Spinner
    private lateinit var btnUploadPoliceVerificationDoc: Button
    private lateinit var btnUploadAppointmentLetterDoc: Button

    /////////////////////////Infrastructure Info Section////////////
    private lateinit var spinnerOwnerBuilding: Spinner
    private lateinit var spinnerRoofOfBuilding: Spinner
    private lateinit var spinnerWhetherStructurallySound: Spinner
    private lateinit var spinnerVisibleSignOfLeakage: Spinner
    private lateinit var spinnerConformanceDDUGKY: Spinner
    private lateinit var spinnerProtectionofStairs: Spinner
    private lateinit var spinnerCorridor: Spinner
    private lateinit var spinnerSecuringWires: Spinner
    private lateinit var spinnerSwitchBoards: Spinner
    private lateinit var spinnerHostelNameBoard: Spinner
    private lateinit var spinnerEntitlementBoard: Spinner
    private lateinit var spinnerContactDetail: Spinner
    private lateinit var spinnerBasicInfoBoard: Spinner
    private lateinit var spinnerFoodSpecificationBoard: Spinner
    private lateinit var etAreaOfBuilding: TextInputEditText
    private lateinit var etCirculatingArea: TextInputEditText
    private lateinit var etAreaForOutDoorGames: TextInputEditText

    /////////////////////////Living Area Information Section////////////
    private lateinit var spinnerTypeLivingRoof: Spinner
    private lateinit var spinnerCeiling: Spinner
    private lateinit var spinnerAirConditioning: Spinner
    private lateinit var spinnerLivingAreaInfoBoard: Spinner
    private lateinit var etHeightOfCeiling: TextInputEditText
    private lateinit var etRoomLength: TextInputEditText
    private lateinit var etRoomWidth: TextInputEditText
    private lateinit var etRoomArea: TextView
    private lateinit var etRoomWindowArea: TextInputEditText
    private lateinit var etCot: TextInputEditText
    private lateinit var etMattress: TextInputEditText
    private lateinit var etBedSheet: TextInputEditText
    private lateinit var etCupboard: TextInputEditText

    private lateinit var etLivingAreaLights: TextInputEditText
    private lateinit var etLivingAreaFans: TextInputEditText

    /////////////////////////Toilets Information Section////////////
    private lateinit var spinnerToiletType: Spinner
    private lateinit var spinnerToiletFlooringType: Spinner
    private lateinit var spinnerConnectionToRunningWater: Spinner
    private lateinit var spinnerOverheadTanks: Spinner
    private lateinit var etLightsInToilet: TextInputEditText
    private lateinit var etFemaleUrinal: TextInputEditText
    private lateinit var etFemaleWashbasins: TextInputEditText

    /////////////////////////Non-Living Areas Section////////////
    private lateinit var spinnerFoodPreparedTrainingCenter: Spinner
    private lateinit var spinnerDiningRecreationAreaSeparate: Spinner
    private lateinit var spinnerIsReceptionAreaAva: Spinner
    private lateinit var spinnerTvAvailable: Spinner
    private lateinit var etKitchenLength: TextInputEditText
    private lateinit var etKitchenWidth: TextInputEditText
    private lateinit var etKitchenArea: TextView
    private lateinit var etStoolsChairsBenches: TextInputEditText
    private lateinit var etWashArea: TextInputEditText
    private lateinit var etDiningLength: TextInputEditText
    private lateinit var etDiningWidth: TextInputEditText
    private lateinit var etDiningArea: TextView
    private lateinit var etRecreationLength: TextInputEditText
    private lateinit var etRecreationWidth: TextInputEditText
    private lateinit var etRecreationArea: TextView

    /////////////////////////Indoor Game Section////////////
    private lateinit var etIndoorGameName: TextInputEditText

    /////////////////////////Residential Facilities Section////////////
    private lateinit var spinnerWhetherHostelsSeparated: Spinner
    private lateinit var spinnerWardenWhereMalesStay: Spinner
    private lateinit var spinnerWardenWhereLadyStay: Spinner
    private lateinit var spinnerSecurityGaurdsAvailable: Spinner
    private lateinit var spinnerWhetherFemaleDoctorAvailable: Spinner
    private lateinit var spinnerWhetherMaleDoctorAvailable: Spinner

    /////////////////////////Support Facilities Section////////////
    private lateinit var spinnerSafeDrinikingAvailable: Spinner
    private lateinit var spinnerFirstAidKitAvailable: Spinner
    private lateinit var spinnerFireFightingEquipmentAvailable: Spinner
    private lateinit var spinnerBiometricDeviceAvailable: Spinner
    private lateinit var spinnerElectricalPowerBackupAvailable: Spinner
    private lateinit var spinnerGrievanceRegisterAvailable: Spinner
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var livingRoomAdapter: LivingRoomListAdapter
    private lateinit var ToiletAdapter: ToiletAdapter


    private var isBasicInfoVisible = true
    private var isInfraVisible = true
    private var isLivingAreaVisible = true
    private var isToiletsVisible = true
    private var isToiletsAdditionalVisible = true
    private var isNonLivingVisible = true
    private var isIndoorGameVisible = true
    private var isResidentialFaVisible = true
    private var isSupportFaVisible = true

    private lateinit var adapterGame: IndoorGameAdapter
    private val indoorGamesList = mutableListOf<IndoorGame>()
    private var gameCounter = 1
    var selectedStateCode = "0"
    var selectedLgdDistrictCode = "0"
    var selectedDistrictCode = "0"
    var selectedBlockCode = "0"
    var selectedGpCode = "0"
    var selectedVillageCode = "0"
    var centerId = ""
    var sanctionOrder = ""
    var facilityId = ""
    var status = ""
    var remarks = ""
    val yesNoList = listOf("--Select--", "Yes", "No")

    private lateinit var spinnerAreaType: Spinner // Urban/Rural selector
    private lateinit var villageContainer: LinearLayout
    private lateinit var urbanContainer: LinearLayout
    private lateinit var spinnerSelectULB: Spinner
    private lateinit var spinnerSelectWard: Spinner

    private var urbanULBs = emptyList<UlbItem>()

    private var urbanWards = emptyList<WardItem>()


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    Log.d("Camera", "Captured image URI: $photoUri")
                    when (currentPhotoTarget) {
                        "PoliceVerificationDoc" -> {
                            ivPoliceVerificationDocPreview.setImageURI(photoUri)
                            ivPoliceVerificationDocPreview.visibility = View.VISIBLE
                            base64PVDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "AppointmentLetterDoc" -> {
                            ivAppointmentLetterDocPreview.setImageURI(photoUri)
                            ivAppointmentLetterDocPreview.visibility = View.VISIBLE
                            base64ALDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "OwnerBuildingDoc" -> {
                            ivOwnerBuildingDocPreview.setImageURI(photoUri)
                            ivOwnerBuildingDocPreview.visibility = View.VISIBLE
                            base64OwnerBuildingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "RoofOfBuilding" -> {
                            ivRoofOfBuildingPreview.setImageURI(photoUri)
                            ivRoofOfBuildingPreview.visibility = View.VISIBLE
                            base64RoofOfBuildingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WhetherStructurallySound" -> {
                            ivWhetherStructurallySoundPreview.setImageURI(photoUri)
                            ivWhetherStructurallySoundPreview.visibility = View.VISIBLE
                            base64WhetherStructurallySoundDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SignOfLeakage" -> {
                            ivSignOfLeakagePreview.setImageURI(photoUri)
                            ivSignOfLeakagePreview.visibility = View.VISIBLE
                            base64SignOfLeakageDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ConformanceDDUGKY" -> {
                            ivConformanceDDUGKYPreview.setImageURI(photoUri)
                            ivConformanceDDUGKYPreview.visibility = View.VISIBLE
                            base64ConformanceDDUGKYDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ProtectionofStairs" -> {
                            ivProtectionofStairsPreview.setImageURI(photoUri)
                            ivProtectionofStairsPreview.visibility = View.VISIBLE
                            base64ProtectionofStairsDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "AreaOfBuilding" -> {
                            binding.ivAreaOfBuildingDocPreview.setImageURI(photoUri)
                            binding.ivAreaOfBuildingDocPreview.visibility = View.VISIBLE
                            base64BuildingAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "CirculationArea" -> {
                            binding.ivCirculationAreaDocPreview.setImageURI(photoUri)
                            binding.ivCirculationAreaDocPreview.visibility = View.VISIBLE
                            base64CirculatingAreaProof =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "Corridor" -> {
                            ivCorridorPreview.setImageURI(photoUri)
                            ivCorridorPreview.visibility = View.VISIBLE
                            base64CorridorDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SecuringWires" -> {
                            ivSecuringWiresPreview.setImageURI(photoUri)
                            ivSecuringWiresPreview.visibility = View.VISIBLE
                            base64SecuringWiresDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SwitchBoards" -> {
                            ivSwitchBoardsPreview.setImageURI(photoUri)
                            ivSwitchBoardsPreview.visibility = View.VISIBLE
                            base64SwitchBoardsDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "HostelNameBoard" -> {
                            ivHostelNameBoardPreview.setImageURI(photoUri)
                            ivHostelNameBoardPreview.visibility = View.VISIBLE
                            base64HostelNameBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "EntitlementBoard" -> {
                            ivEntitlementBoardPreview.setImageURI(photoUri)
                            ivEntitlementBoardPreview.visibility = View.VISIBLE
                            base64EntitlementBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ContactDetail" -> {
                            ivContactDetailPreview.setImageURI(photoUri)
                            ivContactDetailPreview.visibility = View.VISIBLE
                            base64ContactDetailDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "BasicInfoBoard" -> {
                            ivBasicInfoBoardPreview.setImageURI(photoUri)
                            ivBasicInfoBoardPreview.visibility = View.VISIBLE
                            base64BasicInfoBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "FoodSpecificationBoard" -> {
                            ivFoodSpecificationBoardPreview.setImageURI(photoUri)
                            ivFoodSpecificationBoardPreview.visibility = View.VISIBLE
                            base64FoodSpecificationBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "TypeLivingRoof" -> {
                            ivTypeLivingRoofPreview.setImageURI(photoUri)
                            ivTypeLivingRoofPreview.visibility = View.VISIBLE
                            base64TypeLivingRoofDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "Ceiling" -> {
                            ivCeilingPreview.setImageURI(photoUri)
                            ivCeilingPreview.visibility = View.VISIBLE
                            base64CeilingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "AirConditioning" -> {
                            ivAirConditioningPreview.setImageURI(photoUri)
                            ivAirConditioningPreview.visibility = View.VISIBLE
                            base64AirConditioningDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "WindowArea" -> {
                            binding.ivLivingAreaWindowAreaPreview.setImageURI(photoUri)
                            binding.ivLivingAreaWindowAreaPreview.visibility = View.VISIBLE
                            base64WindowAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "HieghtOfCeiling" -> {
                            binding.ivLivingAreaHieghtOfCeilingPreview.setImageURI(photoUri)
                            binding.ivLivingAreaHieghtOfCeilingPreview.visibility = View.VISIBLE
                            base64AirHieghtOfCelingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "COT" -> {
                            binding.ivCotPreview.setImageURI(photoUri)
                            binding.ivCotPreview.visibility = View.VISIBLE
                            base64CotDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "Mattress" -> {
                            binding.ivMattressPreview.setImageURI(photoUri)
                            binding.ivMattressPreview.visibility = View.VISIBLE
                            base64MattressDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "BedSheet" -> {
                            binding.ivBedSheetPreview.setImageURI(photoUri)
                            binding.ivBedSheetPreview.visibility = View.VISIBLE
                            base64BedSheetDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "Cupboard" -> {
                            binding.ivCupboardPreview.setImageURI(photoUri)
                            binding.ivCupboardPreview.visibility = View.VISIBLE
                            base64CupBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "LivingAreaLights" -> {
                            binding.ivLivingAreaLightsPreview.setImageURI(photoUri)
                            binding.ivLivingAreaLightsPreview.visibility = View.VISIBLE
                            base64LightsDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "LivingAreaFans" -> {
                            binding.ivLivingAreaFansPreview.setImageURI(photoUri)
                            binding.ivLivingAreaFansPreview.visibility = View.VISIBLE
                            base64FansDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "LivingAreaInfoBoard" -> {
                            ivLivingAreaInfoBoardPreview.setImageURI(photoUri)
                            ivLivingAreaInfoBoardPreview.visibility = View.VISIBLE
                            base64LivingAreaInfoBoardDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "LightsInToilet" -> {
                            ivLightsInToiletPreview.setImageURI(photoUri)
                            ivLightsInToiletPreview.visibility = View.VISIBLE
                            base64LightsInToiletDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ToiletFlooring" -> {
                            ivToiletFlooringPreview.setImageURI(photoUri)
                            ivToiletFlooringPreview.visibility = View.VISIBLE
                            base64ToiletFlooringDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "RunningWater" -> {
                            binding.ivRunningWaterPreview.setImageURI(photoUri)
                            binding.ivRunningWaterPreview.visibility = View.VISIBLE
                            base64RunningWaterDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "Urinals" -> {
                            binding.urinalWashbasin.ivUrinalPreview.setImageURI(photoUri)
                            binding.urinalWashbasin.ivUrinalPreview.visibility = View.VISIBLE
                            base64UrinalsDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "Washbasin" -> {
                            binding.urinalWashbasin.ivWashBasinPreview.setImageURI(photoUri)
                            binding.urinalWashbasin.ivWashBasinPreview.visibility = View.VISIBLE
                            base64WashBasinDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "OverHead" -> {
                            binding.urinalWashbasin.ivOverHeadPreview.setImageURI(photoUri)
                            binding.urinalWashbasin.ivOverHeadPreview.visibility = View.VISIBLE
                            base64OverHeadTankDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "FoodPreparedTraining" -> {
                            ivFoodPreparedTrainingPreview.setImageURI(photoUri)
                            ivFoodPreparedTrainingPreview.visibility = View.VISIBLE
                            base64FoodPreparedTrainingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ReceptionArea" -> {
                            ivReceptionAreaPreview.setImageURI(photoUri)
                            ivReceptionAreaPreview.visibility = View.VISIBLE
                            base64ReceptionAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "DiningArea" -> {
                            binding.ivDiningPreview.setImageURI(photoUri)
                            binding.ivDiningPreview.visibility = View.VISIBLE
                            base64DinningAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "RecreationArea" -> {
                            binding.ivRecreactionAreaPreview.setImageURI(photoUri)
                            binding.ivRecreactionAreaPreview.visibility = View.VISIBLE
                            base64RecreationAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "DiningAndRecreationArea" -> {
                            binding.ivDiningAndRecreactionAreaPreview.setImageURI(photoUri)
                            binding.ivDiningAndRecreactionAreaPreview.visibility = View.VISIBLE
                            base64RecreationDiningAreaDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "IndoorGame" -> {
                            binding.ivIndoorGamePreview.setImageURI(photoUri)
                            binding.ivIndoorGamePreview.visibility = View.VISIBLE
                            base64IndoorGameDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri).toString()
                        }

                        "WhetherHostelsSeparated" -> {
                            ivWhetherHostelsSeparatedPreview.setImageURI(photoUri)
                            ivWhetherHostelsSeparatedPreview.visibility = View.VISIBLE
                            base64WhetherHostelsSeparatedDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WardenWhereMalesStay" -> {
                            ivWardenWhereMalesStayPreview.setImageURI(photoUri)
                            ivWardenWhereMalesStayPreview.visibility = View.VISIBLE
                            base64WardenWhereMalesStayDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WardenWhereLadyStay" -> {
                            ivWardenWhereLadyStayPreview.setImageURI(photoUri)
                            ivWardenWhereLadyStayPreview.visibility = View.VISIBLE
                            base64WardenWhereLadyStayDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SecurityGaurds" -> {
                            ivSecurityGaurdsPreview.setImageURI(photoUri)
                            ivSecurityGaurdsPreview.visibility = View.VISIBLE
                            base64SecurityGaurdsDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WhetherFemaleDoctor" -> {
                            ivWhetherFemaleDoctorPreview.setImageURI(photoUri)
                            ivWhetherFemaleDoctorPreview.visibility = View.VISIBLE
                            base64WhetherFemaleDoctorDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "WhetherMaleDoctor" -> {
                            ivWhetherMaleDoctorPreview.setImageURI(photoUri)
                            ivWhetherMaleDoctorPreview.visibility = View.VISIBLE
                            base64WhetherMaleDoctorDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "SafeDrinking" -> {
                            ivSafeDrinkingPreview.setImageURI(photoUri)
                            ivSafeDrinkingPreview.visibility = View.VISIBLE
                            base64SafeDrinkingDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "FirstAidKit" -> {
                            ivFirstAidKitPreview.setImageURI(photoUri)
                            ivFirstAidKitPreview.visibility = View.VISIBLE
                            base64FirstAidKitDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "FireFightingEquipment" -> {
                            ivFireFightingEquipmentPreview.setImageURI(photoUri)
                            ivFireFightingEquipmentPreview.visibility = View.VISIBLE
                            base64FireFightingEquipmentDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "BiometricDevice" -> {
                            ivBiometricDevicePreview.setImageURI(photoUri)
                            ivBiometricDevicePreview.visibility = View.VISIBLE
                            base64BiometricDeviceDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "ElectricalPower" -> {
                            ivElectricalPowerPreview.setImageURI(photoUri)
                            ivElectricalPowerPreview.visibility = View.VISIBLE
                            base64ElectricalPowerDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "GrievanceRegister" -> {
                            ivGrievanceRegisterPreview.setImageURI(photoUri)
                            ivGrievanceRegisterPreview.visibility = View.VISIBLE
                            base64GrievanceRegisterDocFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) launchCamera()
                else Toast.makeText(
                    requireContext(), "Camera permission is required.", Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {


        _binding = FragmentResidentialBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findById(view)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]






        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        facilityId = arguments?.getString("facilityId").toString()
        remarks = arguments?.getString("remarks").toString()
        status = arguments?.getString("status").toString()

        observeViewModelLivingAreaList()
        observeViewModelToiletList()
        observeDeleteToiletResponse()

        setupToiletRecyclerView()
        setupReceptionAreaSpinner()
        collectSectionStatus()
        collectRFInfoResponse()
        collectInsfrastructureDetailsAndComplains()
        CollectGetToiletWashbasinDetails()
        NonAreaInformation()
        ResidentialFacilitiesAvailable()
        observeFinalSubmissionResponse()
        ResidentialSupportFacilitiesAvailable()
        observeState()
        observeDistrict()
        observeBlock()
        observeGp()
        observeVillage()
        collectUlbResponse()
        collectWardResponse()

        setupAreaTypeSpinner()

        setupUrbanSpinners()



        binding.spinnerDiningRecreationAreaSeparate.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selected = yesNoList[position]

                    if (selected == "Yes") {
                        binding.llForSeperateHide.visible()
                        binding.llForNotSeperateHide.gone()
                    } else {
                        binding.llForSeperateHide.gone()
                        binding.llForNotSeperateHide.visible()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }



        if (status == "QM" || status == "SM") {

            showEditRemarksDialog()

        }


        val request = StateRequest(
            appVersion = BuildConfig.VERSION_NAME,
        )
        viewModel.getStateList(request, AppUtil.getSavedTokenPreference(requireContext()))


        val sectionReq = SectionReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            facilityId = facilityId

        )

        viewModel.getRFSectionStatus(sectionReq)
        showProgressBar()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.btnBack.setOnClickListener {
            //  requireActivity().onBackPressedDispatcher.onBackPressed()
            //  findNavController().popBackStack()
            findNavController().navigateUp()
        }


        binding.btnFinalSubmi.setOnClickListener {

            val finalSubmitReq = RfFinalSubmitReq(
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                appVersion = BuildConfig.VERSION_NAME,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                tcId = centerId,
                sanctionOrder = sanctionOrder,
                facilityId = facilityId

            )

            viewModel.insertRFFinalSubmission(finalSubmitReq)
            showProgressBar()

        }

        livingRoomAdapter = LivingRoomListAdapter(mutableListOf()) { roomItem ->

            val request = DeleteLivingRoomList(

                appVersion = BuildConfig.VERSION_NAME,
                livingRoomId = roomItem.livingRoomId.toString()
            )

            showProgressBar()
            viewModel.deleteLivingRoom(request)


        }

        binding.livingAreaRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = livingRoomAdapter
        }
        binding.root.setOnTouchListener { v, event ->
            AppUtil.hideKeyboard(requireActivity())
            v.performClick()
            false
        }


        // Expand all section


        binding.basicInfoInclude?.headerTCBasicInfo?.setOnClickListener {

            if (sectionsStatus.basiInfoSection > 0) {
                showEditSectionDialog("Basic Info") {

                    val requestTcInfo = RfCommonReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                        tcId = centerId.toInt(),
                        sanctionOrder = sanctionOrder,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        facilityId = facilityId.toInt()
                    )
                    viewModel.getRfBasicInformationrInfo(requestTcInfo)
                    showProgressBar()


                }


            } else {

                if (isBasicInfoVisible) {
                    binding.basicInfoInclude?.layoutTCBasicInfoContent?.visible()
                    binding.basicInfoInclude?.ivToggleTCBasicInfo?.setImageResource(R.drawable.outline_arrow_upward_24)

                    isBasicInfoVisible = false
                } else {
                    binding.basicInfoInclude?.layoutTCBasicInfoContent?.gone()
                    binding.basicInfoInclude?.ivToggleTCBasicInfo?.setImageResource(R.drawable.ic_dropdown_arrow)
                    isBasicInfoVisible = true
                }
            }


        }

        binding.headerInfraDetailCompliance.setOnClickListener {


            if (sectionsStatus.infraDtlComplianceSection > 0) {

                showEditSectionDialog("Infra Basic") {

                    val requestCompliancesRFQT = CompliancesRFQTReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                        facilityId = facilityId,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        tcId = centerId,
                        sanctionOrder = sanctionOrder
                    )

                    viewModel.getCompliancesRFQTReqRFQT(requestCompliancesRFQT)
                    showProgressBar()

                }

            } else {

                if (isInfraVisible) {
                    binding.layoutInfraDetailComplianceContent.visible()
                    binding.ivToggleInfraDetailCompliance.setImageResource(R.drawable.outline_arrow_upward_24)

                    isInfraVisible = false
                } else {
                    binding.layoutInfraDetailComplianceContent.gone()
                    binding.ivToggleInfraDetailCompliance.setImageResource(R.drawable.ic_dropdown_arrow)
                    isInfraVisible = true
                }

            }


        }

        binding.headerLivingAreaInfo.setOnClickListener {


            if (isLivingAreaVisible) {
                binding.hideRectcler.visible()
                binding.ivToggleLivingAreaInfo.setImageResource(R.drawable.outline_arrow_upward_24)
                isLivingAreaVisible = false

                val rfLAreaListReq = LivingRoomReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    facilityId = facilityId.toInt()

                )

                viewModel.getRfLivingRoomListView(rfLAreaListReq)


            } else {
                val sectionReq = SectionReq(
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    appVersion = BuildConfig.VERSION_NAME,
                    imeiNo = AppUtil.getAndroidId(requireContext()),
                    tcId = centerId,
                    sanctionOrder = sanctionOrder,
                    facilityId = facilityId

                )

                viewModel.getRFSectionStatus(sectionReq)
                showProgressBar()
                binding.hideRectcler.gone()
                binding.layoutLivingAreaInfoContent.gone()
                binding.ivToggleLivingAreaInfo.setImageResource(R.drawable.ic_dropdown_arrow)
                isLivingAreaVisible = true
            }

        }

        binding.btnAddLivingArea.setOnClickListener {
            binding.layoutLivingAreaInfoContent.visible()

        }

        binding.btnAddtoilet.setOnClickListener {
            binding.layoutToiletsContent.visible()

        }



        binding.headerToilets.setOnClickListener {


            if (isToiletsVisible) {
                binding.hideRecyclerToilet.visible()
                binding.ivToggleToilets.setImageResource(R.drawable.outline_arrow_upward_24)
                isToiletsVisible = false

                val rfLAreaListReq = LivingRoomReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    facilityId = facilityId.toInt()

                )

                viewModel.getToiletSectionListView(rfLAreaListReq)


            } else {

                val sectionReq = SectionReq(
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    appVersion = BuildConfig.VERSION_NAME,
                    imeiNo = AppUtil.getAndroidId(requireContext()),
                    tcId = centerId,
                    sanctionOrder = sanctionOrder,
                    facilityId = facilityId

                )

                viewModel.getRFSectionStatus(sectionReq)
                showProgressBar()
                binding.hideRecyclerToilet.gone()
                binding.layoutToiletsContent.gone()
                binding.ivToggleToilets.setImageResource(R.drawable.ic_dropdown_arrow)
                isToiletsVisible = true
            }

        }


        binding.urinalWashbasin.headerUrinalWashbasin.setOnClickListener {

            if (sectionsStatus.toiletAdditionalSection > 0) {

                showEditSectionDialog("Urinal/WashBasin/Overhead") {
                    val requestTcInfo = GetUrinalWashReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                        tcId = centerId,
                        sanctionOrder = sanctionOrder,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        facilityId = facilityId
                    )
                    viewModel.getToiletWashbasinDetails(requestTcInfo)
                    showProgressBar()


                }


            } else {

                if (isToiletsAdditionalVisible) {
                    binding.urinalWashbasin.layoutUrinalWashbasinContent.visible()
                    binding.urinalWashbasin.ivToggleUrinalWashbasin.setImageResource(R.drawable.outline_arrow_upward_24)

                    isToiletsAdditionalVisible = false
                } else {
                    binding.urinalWashbasin.layoutUrinalWashbasinContent.gone()
                    binding.urinalWashbasin.ivToggleUrinalWashbasin.setImageResource(R.drawable.ic_dropdown_arrow)
                    isToiletsAdditionalVisible = true
                }
            }


        }






        binding.headerNonLivingArea.setOnClickListener {


            if (sectionsStatus.nonLivingAreaSection > 0) {

                showEditSectionDialog("Non Living Area") {

                    val requestLRLVRQ = LivingRoomListViewRQ(
                        appVersion = BuildConfig.VERSION_NAME,
                        tcId = centerId.toInt(),
                        sanctionOrder = sanctionOrder,
                        facilityId = facilityId.toInt()

                    )
                    viewModel.getRfNonLivingAreaInformation(requestLRLVRQ)

                    showProgressBar()


                }


            } else {
                if (isNonLivingVisible) {
                    binding.layoutNonLivingAreaContent.visible()
                    binding.ivToggleNonLivingArea.setImageResource(R.drawable.outline_arrow_upward_24)
                    isNonLivingVisible = false
                } else {
                    binding.layoutNonLivingAreaContent.gone()
                    binding.ivToggleNonLivingArea.setImageResource(R.drawable.ic_dropdown_arrow)
                    isNonLivingVisible = true
                }
            }


        }


        binding.headerIndoorGameDetail.setOnClickListener {
            if (sectionsStatus.indoorGameDtlSection > 0) {

                showEditSectionDialog("Indoor Game") {


                    val rfGameRequest = RFGameRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        tcId = centerId.toInt(),
                        sanctionOrder = sanctionOrder,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        facilityId = facilityId.toInt()
                    )
                    viewModel.getRfIndoorGameDetails(rfGameRequest)
                    showProgressBar()

                    IndoorGameRecyclerView()

                }

            } else {

                if (isIndoorGameVisible) {
                    binding.layoutIndoorGameDetailContent.visible()
                    binding.ivToggleIndoorGameDetail.setImageResource(R.drawable.outline_arrow_upward_24)
                    isIndoorGameVisible = false
                } else {
                    val sectionReq = SectionReq(
                        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                        appVersion = BuildConfig.VERSION_NAME,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        tcId = centerId,
                        sanctionOrder = sanctionOrder,
                        facilityId = facilityId

                    )

                    viewModel.getRFSectionStatus(sectionReq)
                    showProgressBar()
                    binding.layoutIndoorGameDetailContent.gone()
                    binding.ivToggleIndoorGameDetail.setImageResource(R.drawable.ic_dropdown_arrow)
                    isIndoorGameVisible = true
                }
            }


        }

        binding.headerRfAvailable.setOnClickListener {

            if (sectionsStatus.rfAvailableSection > 0) {

                showEditSectionDialog("Residential Facilities Available") {


                    val requestTcInfo = RfCommonReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                        tcId = centerId.toInt(),
                        sanctionOrder = sanctionOrder,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        facilityId = facilityId.toInt()

                    )

                    viewModel.getResidentialFacilitiesAvailable(requestTcInfo)

                    showProgressBar()

                }

            } else {

                if (isResidentialFaVisible) {
                    binding.layoutRfAvailableContent.visible()
                    binding.ivToggleRfAvailable.setImageResource(R.drawable.outline_arrow_upward_24)
                    isResidentialFaVisible = false
                } else {
                    binding.layoutRfAvailableContent.gone()
                    binding.ivToggleRfAvailable.setImageResource(R.drawable.ic_dropdown_arrow)
                    isResidentialFaVisible = true
                }
            }


        }

        binding.headerSfAvailable.setOnClickListener {


            if (sectionsStatus.supportFacltySection > 0) {

                showEditSectionDialog("Support Facilities Available") {


                    val rfGameRequest = RFGameRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        tcId = centerId.toInt(),
                        sanctionOrder = sanctionOrder,
                        imeiNo = AppUtil.getAndroidId(requireContext()),
                        facilityId = facilityId.toInt()

                    )
                    viewModel.getRFSupportFacilitiesAvailable(rfGameRequest)


                    showProgressBar()

                }

            } else {

                if (isSupportFaVisible) {
                    binding.layoutSfAvailableContent.visible()
                    binding.ivToggleSfAvailable.setImageResource(R.drawable.outline_arrow_upward_24)
                    isSupportFaVisible = false
                } else {
                    binding.layoutSfAvailableContent.gone()
                    binding.ivToggleSfAvailable.setImageResource(R.drawable.ic_dropdown_arrow)
                    isSupportFaVisible = true
                }

            }

        }



        adapterGame = IndoorGameAdapter(indoorGamesList) { game ->
            removeGame(game)
        }
        binding.recyclerView.adapter = adapterGame
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.btnAddMoreGame.setOnClickListener {
            val gameName = binding.etIndoorGameName.text.toString().trim()

            if (gameName.isEmpty() || base64IndoorGameDocFile == "") {
                Toast.makeText(
                    requireContext(), "Please enter game name and Upload Pic", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val newGame = IndoorGame(gameCounter++, gameName, base64IndoorGameDocFile)
            indoorGamesList.add(newGame)
            adapterGame.notifyItemInserted(indoorGamesList.size - 1)

            binding.etIndoorGameName.text?.clear()
            base64IndoorGameDocFile = ""

            binding.ivIndoorGamePreview.visibility = View.GONE

        }


        binding.btnSubmitInDoorGameInfo.setOnClickListener {
            if (indoorGamesList.size < 8) {
                Toast.makeText(
                    requireContext(),
                    "Please add at least 8 indoor games before submitting",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Hit The API
                SubmitRfIndoorGameDetails()
            }
        }


        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }


        // All submit response


        observeDeleteResponse()


        viewModel.RfBasicInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Basic Info data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.basicInfoInclude?.layoutTCBasicInfoContent?.gone()
                        isBasicInfoVisible = true

                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()


                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Basic Info submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        viewModel.RfInfra.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()

                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Infra Detail submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.layoutInfraDetailComplianceContent.gone()
                        isInfraVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Infra Detail submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        viewModel.RfLivingArea.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        clearAllFields()
                        Toast.makeText(
                            requireContext(),

                            "Living Area submitted successfully!", Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutLivingAreaInfoContent.gone()
                        binding.hideRectcler.gone()
                        isLivingAreaVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId
                        )
                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()
                    }
                }

            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.SubmitRfToiletDataToServer.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()



                when (it.responseCode) {


                    200 -> {
                        clearToiletForm()
                        isToiletsVisible = true

                        Toast.makeText(
                            requireContext(),
                            "Toilet data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutToiletsContent.gone()
                        binding.hideRecyclerToilet.gone()
                        isLivingAreaVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.insertRfToiletWashRoomDetail.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Urinals/Washbasin/OverHead data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.urinalWashbasin.layoutUrinalWashbasinContent.gone()
                        isToiletsAdditionalVisible = true

                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()


                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Basic Info submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }




        viewModel.insertRfNonLivingAreaInformation.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Non Living Area data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutNonLivingAreaContent.gone()
                        isNonLivingVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }

                }

            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        viewModel.insertRfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Indoor game data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutIndoorGameDetailContent.gone()
                        isIndoorGameVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        viewModel.insertResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Residential Facilities Available data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutRfAvailableContent.gone()
                        isResidentialFaVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        viewModel.insertRFSupportFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()


                when (it.responseCode) {


                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Support Facilities Available data submitted successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.layoutSfAvailableContent.gone()
                        isSupportFaVisible = true
                        val sectionReq = SectionReq(
                            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                            appVersion = BuildConfig.VERSION_NAME,
                            imeiNo = AppUtil.getAndroidId(requireContext()),
                            tcId = centerId,
                            sanctionOrder = sanctionOrder,
                            facilityId = facilityId

                        )

                        viewModel.getRFSectionStatus(sectionReq)
                        showProgressBar()

                    }


                }


            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(
                    requireContext(),
                    "Living Area submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    selectedStateCode = "0"
                    selectedDistrictCode = "0"
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    selectedVillageCode = "0"

                    spinnerVillage.setSelection(0, false)
                    spinnerDistrict.setSelection(0, false)
                    spinnerBlock.setSelection(0, false)
                    spinnerGp.setSelection(0, false)

                } else {
                    val adapter = spinnerState.adapter as? StateAdapter ?: return
                    val stateModel = adapter.getItem(position) ?: return

                    selectedStateCode = stateModel.stateCode

                    val request = DistrictRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        stateCode = selectedStateCode,
                    )
                    viewModel.getDistrictList(
                        request, AppUtil.getSavedTokenPreference(requireContext())
                    )
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    selectedDistrictCode = "0"
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    selectedVillageCode = "0"
                    spinnerVillage.setSelection(0)
                    spinnerBlock.setSelection(0)
                    spinnerGp.setSelection(0)
                } else {
                    val districtModel = (spinnerDistrict.adapter as DistrictAdapter).getItem(position)
                    selectedLgdDistrictCode = districtModel!!.lgdDistrictCode
                    selectedDistrictCode = districtModel!!.districtCode

                    val request = BlockRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        districtCode = selectedDistrictCode,
                    )
                    viewModel.getBlockList(
                        request, AppUtil.getSavedTokenPreference(requireContext())
                    )
                    // Toast.makeText(applicationContext,""+selectedStateCode,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        spinnerBlock.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    selectedVillageCode = "0"
                    spinnerVillage.setSelection(0)
                    spinnerGp.setSelection(0)
                } else {
                    val blockModel = (spinnerBlock.adapter as BlockAdapter).getItem(position)
                    selectedBlockCode = blockModel!!.blockCode

                    val request = GpRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        blockCode = selectedBlockCode,
                    )
                    viewModel.getGpList(request, AppUtil.getSavedTokenPreference(requireContext()))
                    // Toast.makeText(applicationContext,""+selectedStateCode,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        spinnerGp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
            ) {
                if (position == 0) {
                    selectedGpCode = "0"
                    selectedVillageCode = "0"
                    spinnerVillage.setSelection(0)
                } else {
                    val gpModel = (spinnerGp.adapter as PanchayatAdapter).getItem(position)
                    selectedGpCode = gpModel!!.gpCode
                    val requestVill = VillageReq(
                        appVersion = BuildConfig.VERSION_NAME,
                        gpCode = selectedGpCode,
                    )
                    viewModel.getVillageList(
                        requestVill, AppUtil.getSavedTokenPreference(requireContext())
                    )

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        spinnerVillage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long
            ) {
                if (position == 0) {

                    selectedVillageCode = "0"

                } else {
                    val villageModel =
                        (spinnerVillage.adapter as VillageAdapter).getItem(position)
                    selectedVillageCode = villageModel!!.villageCode


                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        setupAutoAreaCalculationForRoom(
            binding.etRoomLength, binding.etRoomWidth, binding.etRoomArea
        )

        setupAutoAreaCalculation(
            binding.etKitchenLength, binding.etKitchenWidth, binding.etKitchenArea
        )

        setupAutoAreaCalculation(
            binding.etDiningLength, binding.etDiningWidth, binding.etDiningArea
        )
        setupAutoAreaCalculation(
            binding.etRecreationLength, binding.etRecreationWidth, binding.etRecreationArea
        )


        setupAutoAreaCalculation(
            binding.etDiningAndRecreactionLength,
            binding.etDiningAndRecreactionWidth,
            binding.etDiningAndRecreactionArea
        )


        // Setup Yes/No adapter
        val yesNoAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Yes", "No")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val spinners = listOf(
            R.id.spinnerPickupAndDropFacility,
            R.id.spinnerWhetherStructurallySound,
            R.id.spinnerVisibleSignOfLeakage,
            R.id.spinnerConformanceDDUGKY,
            R.id.spinnerProtectionofStairs,
            R.id.spinnerCorridor,
            R.id.spinnerSecuringWires,
            R.id.spinnerSwitchBoards,
            R.id.spinnerHostelNameBoard,
            R.id.spinnerEntitlementBoard,
            R.id.spinnerContactDetail,
            R.id.spinnerBasicInfoBoard,
            R.id.spinnerFoodSpecificationBoard,
            R.id.spinnerAirConditioning,
            R.id.spinnerLivingAreaInfoBoard,
            R.id.spinnerConnectionToRunningWater,
            R.id.spinnerOverheadTanks,
            R.id.spinnerFoodPreparedTrainingCenter,
            R.id.spinnerDiningRecreationAreaSeparate,
            R.id.spinnerTvAvailable,
            R.id.spinnerWhetherHostelsSeparated,
            R.id.spinnerWardenWhereMalesStay,
            R.id.spinnerWardenWhereLadyStay,
            R.id.spinnerSecurityGaurdsAvailable,
            R.id.spinnerWhetherFemaleDoctorAvailable,
            R.id.spinnerWhetherMaleDoctorAvailable,
            R.id.spinnerSafeDrinikingAvailable,
            R.id.spinnerFirstAidKitAvailable,
            R.id.spinnerFireFightingEquipmentAvailable,
            R.id.spinnerBiometricDeviceAvailable,
            R.id.spinnerElectricalPowerBackupAvailable,
            R.id.spinnerGrievanceRegisterAvailable,
            R.id.spinnerCeiling,
            R.id.spinnerReceptionArea
        )
        spinners.forEach {
            view.findViewById<Spinner>(it).adapter = yesNoAdapter
        }

        val areaTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Hilly Areas", "Non-Hilly Areas")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerTypeOfArea.adapter = areaTypeAdapter


        val categoryOfTcAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "X", "Y", "Z", "Others")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCatOfTCLocation.adapter = categoryOfTcAdapter


        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Male", "Female")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerWardenGender.adapter = genderAdapter


        val facilityTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Male", "Female")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.basicInfoInclude?.spinnerFacilityType?.adapter = facilityTypeAdapter


        val ownershipOfBuildingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Own (O)", "Rent (R)", "Govt. (G)")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerOwnerBuilding.adapter = ownershipOfBuildingAdapter

        val roofOfBuildingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "RCC", "Non-RCC", "RCC & Non-RCC")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerRoofOfBuilding.adapter = roofOfBuildingAdapter

        val typeOfRoofAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "RCC", "Non-RCC")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerTypeLivingRoof.adapter = typeOfRoofAdapter
        val toiletTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Toilet", "Bathroom", "Toilet Cum Bathroom")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerToiletType.adapter = toiletTypeAdapter
        val flooringTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Cement", "Tiles")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerToiletFlooringType.adapter = flooringTypeAdapter

        btnUploadPoliceVerificationDoc.setOnClickListener {
            currentPhotoTarget = "PoliceVerificationDoc"
            checkAndLaunchCamera()
        }
        btnUploadAppointmentLetterDoc.setOnClickListener {
            currentPhotoTarget = "AppointmentLetterDoc"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadOwnerBuilding).setOnClickListener {
            currentPhotoTarget = "OwnerBuildingDoc"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadRoofOfBuilding).setOnClickListener {
            currentPhotoTarget = "RoofOfBuilding"
            checkAndLaunchCamera()

        }

        view.findViewById<Button>(R.id.btnUploadWhetherStructurallySound).setOnClickListener {
            currentPhotoTarget = "WhetherStructurallySound"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadSignOfLeakage).setOnClickListener {
            currentPhotoTarget = "SignOfLeakage"
            checkAndLaunchCamera()

        }

        view.findViewById<Button>(R.id.btnUploadConformanceDDUGKY).setOnClickListener {
            currentPhotoTarget = "ConformanceDDUGKY"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadProtectionofStairs).setOnClickListener {
            currentPhotoTarget = "ProtectionofStairs"
            checkAndLaunchCamera()

        }

        view.findViewById<Button>(R.id.btnUploadAreaOfBuilding).setOnClickListener {
            currentPhotoTarget = "AreaOfBuilding"
            checkAndLaunchCamera()

        }

        view.findViewById<Button>(R.id.btnUploadCirculationArea).setOnClickListener {
            currentPhotoTarget = "CirculationArea"
            checkAndLaunchCamera()

        }


        view.findViewById<Button>(R.id.btnUploadCorridor).setOnClickListener {
            currentPhotoTarget = "Corridor"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadSecuringWires).setOnClickListener {
            currentPhotoTarget = "SecuringWires"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadSwitchBoards).setOnClickListener {
            currentPhotoTarget = "SwitchBoards"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadHostelNameBoard).setOnClickListener {
            currentPhotoTarget = "HostelNameBoard"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadEntitlementBoard).setOnClickListener {
            currentPhotoTarget = "EntitlementBoard"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadContactDetail).setOnClickListener {
            currentPhotoTarget = "ContactDetail"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadBasicInfoBoard).setOnClickListener {
            currentPhotoTarget = "BasicInfoBoard"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadFoodSpecificationBoard).setOnClickListener {
            currentPhotoTarget = "FoodSpecificationBoard"
            checkAndLaunchCamera()

        }

        view.findViewById<Button>(R.id.btnUploadTypeLivingRoof).setOnClickListener {
            currentPhotoTarget = "TypeLivingRoof"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadCeiling).setOnClickListener {
            currentPhotoTarget = "Ceiling"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadAirConditioning).setOnClickListener {
            currentPhotoTarget = "AirConditioning"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadLivingWindowArea).setOnClickListener {
            currentPhotoTarget = "WindowArea"
            checkAndLaunchCamera()
        }


        view.findViewById<Button>(R.id.btnUploadLivingAreaHieghtOfCeiling).setOnClickListener {
            currentPhotoTarget = "HieghtOfCeiling"
            checkAndLaunchCamera()
        }





        view.findViewById<Button>(R.id.btnUploadCot).setOnClickListener {
            currentPhotoTarget = "COT"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadMattress).setOnClickListener {
            currentPhotoTarget = "Mattress"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadBedSheet).setOnClickListener {
            currentPhotoTarget = "BedSheet"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnCupboard).setOnClickListener {
            currentPhotoTarget = "Cupboard"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnLivingAreaLights).setOnClickListener {
            currentPhotoTarget = "LivingAreaLights"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnLivingAreaFans).setOnClickListener {
            currentPhotoTarget = "LivingAreaFans"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadLivingAreaInfoBoard).setOnClickListener {
            currentPhotoTarget = "LivingAreaInfoBoard"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadLightsInToilet).setOnClickListener {
            currentPhotoTarget = "LightsInToilet"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadToiletFlooring).setOnClickListener {
            currentPhotoTarget = "ToiletFlooring"
            checkAndLaunchCamera()

        }



        view.findViewById<Button>(R.id.btnUploadRunningWater).setOnClickListener {
            currentPhotoTarget = "RunningWater"
            checkAndLaunchCamera()

        }



        binding.urinalWashbasin.btnUploadUrinal.setOnClickListener {
            currentPhotoTarget = "Urinals"
            checkAndLaunchCamera()

        }



        binding.urinalWashbasin.btnUploadWashBasin.setOnClickListener {
            currentPhotoTarget = "Washbasin"
            checkAndLaunchCamera()

        }


        binding.urinalWashbasin.btnUploadOverHead.setOnClickListener {
            currentPhotoTarget = "OverHead"
            checkAndLaunchCamera()

        }


        view.findViewById<Button>(R.id.btnUploadFoodPreparedTraining).setOnClickListener {
            currentPhotoTarget = "FoodPreparedTraining"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadReceptionArea).setOnClickListener {
            currentPhotoTarget = "ReceptionArea"
            checkAndLaunchCamera()
        }

        binding.btnUploadDining.setOnClickListener {
            currentPhotoTarget = "DiningArea"
            checkAndLaunchCamera()
        }


        binding.btnUploadRecreactionArea.setOnClickListener {
            currentPhotoTarget = "RecreationArea"
            checkAndLaunchCamera()
        }


        binding.btnUploadDiningAndRecreactionArea.setOnClickListener {
            currentPhotoTarget = "DiningAndRecreationArea"
            checkAndLaunchCamera()
        }

        view.findViewById<TextView>(R.id.btnUploadIndoorGame).setOnClickListener {
            currentPhotoTarget = "IndoorGame"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadWhetherHostelsSeparated).setOnClickListener {
            currentPhotoTarget = "WhetherHostelsSeparated"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadWardenWhereMalesStay).setOnClickListener {
            currentPhotoTarget = "WardenWhereMalesStay"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadWardenWhereLadyStay).setOnClickListener {
            currentPhotoTarget = "WardenWhereLadyStay"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadSecurityGaurds).setOnClickListener {
            currentPhotoTarget = "SecurityGaurds"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadMaleDoctor).setOnClickListener {
            currentPhotoTarget = "WhetherMaleDoctor"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadWhetherFemaleDoctor).setOnClickListener {
            currentPhotoTarget = "WhetherFemaleDoctor"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadSafeDrinking).setOnClickListener {
            currentPhotoTarget = "SafeDrinking"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadFirstAidKit).setOnClickListener {
            currentPhotoTarget = "FirstAidKit"
            checkAndLaunchCamera()

        }
        view.findViewById<Button>(R.id.btnUploadFireFightingEquipment).setOnClickListener {
            currentPhotoTarget = "FireFightingEquipment"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadBiometricDevice).setOnClickListener {
            currentPhotoTarget = "BiometricDevice"
            checkAndLaunchCamera()
        }

        view.findViewById<Button>(R.id.btnUploadElectricalPower).setOnClickListener {
            currentPhotoTarget = "ElectricalPower"
            checkAndLaunchCamera()
        }
        view.findViewById<Button>(R.id.btnUploadGrievanceRegister).setOnClickListener {
            currentPhotoTarget = "GrievanceRegister"
            checkAndLaunchCamera()
        }


        //Form Submission Section
        view.findViewById<Button>(R.id.btnSubmitBasicInfo).setOnClickListener {
            if (validateBasicInfoForm(view)) {
                submitRFBasicInfoForm(view)

            } else Toast.makeText(
                requireContext(),
                "Complete all Basic Information  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }


        view.findViewById<Button>(R.id.btnSubmitInfraInfo).setOnClickListener {
            if (validateInfraInfoForm(view)) submitRFInfraForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Infrastructure Details and Compliance's  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitLivingAreaInfo).setOnClickListener {
            if (validateLivingAreaInfoForm(view)) submitRFLivingAreaForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Living Area Information  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitToiletInfo).setOnClickListener {
            if (validateToiletInfoForm(view)) submitRFToiletForm(view)
            else Toast.makeText(
                requireContext(), "Complete all Toilets  fields and photos.", Toast.LENGTH_LONG
            ).show()
        }








        binding.urinalWashbasin.btnSubmitToiletAdditional.setOnClickListener {

            if (validateAdditionalToiletInfoForm(view)) submitRFAdditionalToiletForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Urinals/Washbasin/Overhead  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }

        view.findViewById<Button>(R.id.btnSubmitNonLivingAreaInfo).setOnClickListener {
            if (validateNonLivingAreaInfoForm(view)) submitRFNonLivingAreaForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Non-Living Areas  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }

        view.findViewById<Button>(R.id.btnResidentialFacilitiesInfo).setOnClickListener {
            if (validateResidentialFacilitiesInfoForm(view)) SubmitRfAvaibilityDetails(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Residential Facilities Available  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSupportFacilityInfo).setOnClickListener {
            if (validateSupportFacilitiesInfoForm(view)) SubmitRfSupportFacilitiesDetails(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Support Facilities Available  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }


    }

    private fun observeState() {
        viewModel.stateList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()
                when (response.responseCode) {
                    200 -> populateSpinnerState(
                        (response.wrappedList ?: emptyList()) as ArrayList<StateModel?>,
                        spinnerState
                    )

                 //   202 -> { //Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()}

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(
                    requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun observeDistrict() {
        viewModel.districtList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()
                var dist: ArrayList<DistrictModel> =response.wrappedList
                when (response.responseCode) {

                    200 -> populateSpinnerDistrict(dist as ArrayList<DistrictModel?>, spinnerDistrict)

                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeBlock() {
        viewModel.blockList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()
                when (response.responseCode) {
                    200 -> populateSpinnerBlock(
                        (response.wrappedList ?: emptyList()) as ArrayList<BlockModel>, spinnerBlock
                    )

             //       202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeGp() {
        viewModel.gpList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()
                when (response.responseCode) {
                    200 -> populateSpinnerGp(
                        (response.wrappedList ?: emptyList()) as ArrayList<GpModel?>, spinnerGp
                    )

                   // 202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeVillage() {
        viewModel.villageList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()

                when (response.responseCode) {
                    200 -> populateSpinnerVillage(
                        (response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>,
                        spinnerVillage
                    )

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun populateSpinnerState(
        alStateModel: ArrayList<StateModel?>, sp: Spinner
    ): StateAdapter? {

        if (alStateModel.isNotEmpty()) {
            alStateModel.add(0, StateModel("--Select--", "0"))
            val adapter =
                StateAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel)
            sp.adapter = adapter
            adapter.notifyDataSetChanged()
            return adapter
        }
        return null
    }

    private fun populateSpinnerDistrict(
        alStateModel: ArrayList<DistrictModel?>, sp: Spinner
    ) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, DistrictModel("--Select--", "0","0"))
            val dbAdapter = DistrictAdapter(
                requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }

    private fun populateSpinnerBlock(alStateModel: ArrayList<BlockModel>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, BlockModel("--Select--", "0"))
            val dbAdapter = BlockAdapter(
                requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }

    private fun populateSpinnerGp(alStateModel: ArrayList<GpModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, GpModel("--Select--", "0"))
            val dbAdapter = PanchayatAdapter(
                requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }


    private fun populateSpinnerVillage(
        alStateModel: ArrayList<VillageModel?>, sp: Spinner
    ) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, VillageModel("--Select--", "0"))
            val dbAdapter = VillageAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel)
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }


    private fun findById(view: View) {
        /////////////////////////Basic Info Section////////////
        etFacilityName = view.findViewById(R.id.etFacilityName)
        etHouseNo = view.findViewById(R.id.etHouseNo)
        etStreet = view.findViewById(R.id.etStreet)
        etPoliceStation = view.findViewById(R.id.etPoliceStation)
        etLandmark = view.findViewById(R.id.etLandmark)
        etPinCode = view.findViewById(R.id.etPinCode)
        etMobile = view.findViewById(R.id.etMobile)
        etPhone = view.findViewById(R.id.etPhone)
        etEmail = view.findViewById(R.id.etEmail)
        etDistanceFromBusStand = view.findViewById(R.id.etDistanceFromBusStand)
        etDistanceFromAutoStand = view.findViewById(R.id.etDistanceFromAutoStand)
        etDistanceFromRailwayStand = view.findViewById(R.id.etDistanceFromRailwayStand)
        etDistanceFromTrainingToResidentialCentre =
            view.findViewById(R.id.etDistanceFromTrainingToResidentialCentre)
        etWardenName = view.findViewById(R.id.etWardenName)
        etWardenEmpID = view.findViewById(R.id.etWardenEmpID)
        etWardenAddress = view.findViewById(R.id.etWardenAddress)
        etWardenEmailId = view.findViewById(R.id.etWardenEmailId)
        etWardenMobile = view.findViewById(R.id.etWardenMobile)
        spinnerState = view.findViewById(R.id.spinnerState)
        spinnerDistrict = view.findViewById(R.id.spinnerDistrict)
        spinnerBlock = view.findViewById(R.id.spinnerBlock)
        spinnerGp = view.findViewById(R.id.spinnerGp)
        spinnerVillage = view.findViewById(R.id.spinnerVillage)
        spinnerTypeOfArea = view.findViewById(R.id.spinnerTypeOfArea)
        spinnerCatOfTCLocation = view.findViewById(R.id.spinnerCatOfTCLocation)
        spinnerPickupAndDropFacility = view.findViewById<Spinner>(R.id.spinnerPickupAndDropFacility)
        spinnerWardenGender = view.findViewById(R.id.spinnerWardenGender)
        btnUploadPoliceVerificationDoc = view.findViewById(R.id.btnUploadPoliceVerificationDoc)
        ivPoliceVerificationDocPreview = view.findViewById(R.id.ivPoliceVerificationDocPreview)
        btnUploadAppointmentLetterDoc = view.findViewById(R.id.btnUploadAppointmentLetterDoc)
        ivAppointmentLetterDocPreview = view.findViewById(R.id.ivAppointmentLetterDocPreview)
        ivOwnerBuildingDocPreview = view.findViewById(R.id.ivOwnerBuildingDocPreview)
        ivRoofOfBuildingPreview = view.findViewById(R.id.ivRoofOfBuildingPreview)
        ivWhetherStructurallySoundPreview =
            view.findViewById(R.id.ivWhetherStructurallySoundPreview)
        ivSignOfLeakagePreview = view.findViewById(R.id.ivSignOfLeakagePreview)
        ivConformanceDDUGKYPreview = view.findViewById(R.id.ivConformanceDDUGKYPreview)
        ivProtectionofStairsPreview = view.findViewById(R.id.ivProtectionofStairsPreview)
        ivCorridorPreview = view.findViewById(R.id.ivCorridorPreview)
        ivSecuringWiresPreview = view.findViewById(R.id.ivSecuringWiresPreview)
        ivSwitchBoardsPreview = view.findViewById(R.id.ivSwitchBoardsPreview)
        ivHostelNameBoardPreview = view.findViewById(R.id.ivHostelNameBoardPreview)
        ivEntitlementBoardPreview = view.findViewById(R.id.ivEntitlementBoardPreview)
        ivContactDetailPreview = view.findViewById(R.id.ivContactDetailPreview)
        ivBasicInfoBoardPreview = view.findViewById(R.id.ivBasicInfoBoardPreview)
        ivFoodSpecificationBoardPreview = view.findViewById(R.id.ivFoodSpecificationBoardPreview)
        ivTypeLivingRoofPreview = view.findViewById(R.id.ivTypeLivingRoofPreview)
        ivCeilingPreview = view.findViewById(R.id.ivCeilingPreview)
        ivAirConditioningPreview = view.findViewById(R.id.ivAirConditioningPreview)
        ivLivingAreaInfoBoardPreview = view.findViewById(R.id.ivLivingAreaInfoBoardPreview)
        ivLightsInToiletPreview = view.findViewById(R.id.ivLightsInToiletPreview)
        ivToiletFlooringPreview = view.findViewById(R.id.ivToiletFlooringPreview)
        ivFoodPreparedTrainingPreview = view.findViewById(R.id.ivFoodPreparedTrainingPreview)
        ivReceptionAreaPreview = view.findViewById(R.id.ivReceptionAreaPreview)
        ivWhetherHostelsSeparatedPreview = view.findViewById(R.id.ivWhetherHostelsSeparatedPreview)
        ivWardenWhereMalesStayPreview = view.findViewById(R.id.ivWardenWhereMalesStayPreview)
        ivWardenWhereLadyStayPreview = view.findViewById(R.id.ivWardenWhereLadyStayPreview)
        ivSecurityGaurdsPreview = view.findViewById(R.id.ivSecurityGaurdsPreview)
        ivWhetherFemaleDoctorPreview = view.findViewById(R.id.ivWhetherFemaleDoctorPreview)
        ivWhetherMaleDoctorPreview = view.findViewById(R.id.ivMaleDoctorPreview)
        ivSafeDrinkingPreview = view.findViewById(R.id.ivSafeDrinkingPreview)
        ivFirstAidKitPreview = view.findViewById(R.id.ivFirstAidKitPreview)
        ivFireFightingEquipmentPreview = view.findViewById(R.id.ivFireFightingEquipmentPreview)
        ivBiometricDevicePreview = view.findViewById(R.id.ivBiometricDevicePreview)
        ivElectricalPowerPreview = view.findViewById(R.id.ivElectricalPowerPreview)
        ivGrievanceRegisterPreview = view.findViewById(R.id.ivGrievanceRegisterPreview)

        /////////////////////////Infrastructure Info Section////////////
        spinnerOwnerBuilding = view.findViewById(R.id.spinnerOwnerBuilding)
        spinnerRoofOfBuilding = view.findViewById(R.id.spinnerRoofOfBuilding)
        spinnerWhetherStructurallySound = view.findViewById(R.id.spinnerWhetherStructurallySound)
        spinnerVisibleSignOfLeakage = view.findViewById(R.id.spinnerVisibleSignOfLeakage)
        spinnerConformanceDDUGKY = view.findViewById(R.id.spinnerConformanceDDUGKY)
        spinnerProtectionofStairs = view.findViewById(R.id.spinnerProtectionofStairs)
        spinnerCorridor = view.findViewById(R.id.spinnerCorridor)
        spinnerSecuringWires = view.findViewById(R.id.spinnerSecuringWires)
        spinnerSwitchBoards = view.findViewById(R.id.spinnerSwitchBoards)
        spinnerHostelNameBoard = view.findViewById(R.id.spinnerHostelNameBoard)
        spinnerEntitlementBoard = view.findViewById(R.id.spinnerEntitlementBoard)
        spinnerContactDetail = view.findViewById(R.id.spinnerContactDetail)
        spinnerBasicInfoBoard = view.findViewById(R.id.spinnerBasicInfoBoard)
        spinnerFoodSpecificationBoard = view.findViewById(R.id.spinnerFoodSpecificationBoard)
        etAreaOfBuilding = view.findViewById(R.id.etAreaOfBuilding)
        etCirculatingArea = view.findViewById(R.id.etCirculatingArea)
        etAreaForOutDoorGames = view.findViewById(R.id.etAreaForOutDoorGames)

        /////////////////////////Living Area Information Section////////////
        spinnerTypeLivingRoof = view.findViewById(R.id.spinnerTypeLivingRoof)
        spinnerCeiling = view.findViewById(R.id.spinnerCeiling)
        spinnerAirConditioning = view.findViewById(R.id.spinnerAirConditioning)
        spinnerLivingAreaInfoBoard = view.findViewById(R.id.spinnerLivingAreaInfoBoard)
        etHeightOfCeiling = view.findViewById(R.id.etHeightOfCeiling)
        etRoomLength = view.findViewById(R.id.etRoomLength)
        etRoomWidth = view.findViewById(R.id.etRoomWidth)
        etRoomArea = view.findViewById(R.id.etRoomArea)
        etRoomWindowArea = view.findViewById(R.id.etRoomWindowArea)
        etCot = view.findViewById(R.id.etCot)
        etMattress = view.findViewById(R.id.etMattress)
        etBedSheet = view.findViewById(R.id.etBedSheet)
        etCupboard = view.findViewById(R.id.etCupboard)
        etLivingAreaLights = view.findViewById(R.id.etLivingAreaLights)
        etLivingAreaFans = view.findViewById(R.id.etLivingAreaFans)
        /////////////////////////Toilets Information Section////////////
        spinnerToiletType = view.findViewById(R.id.spinnerToiletType)
        spinnerToiletFlooringType = view.findViewById(R.id.spinnerToiletFlooringType)
        spinnerConnectionToRunningWater = view.findViewById(R.id.spinnerConnectionToRunningWater)
        spinnerOverheadTanks = view.findViewById(R.id.spinnerOverheadTanks)
        etLightsInToilet = view.findViewById(R.id.etLightsInToilet)
        etFemaleUrinal = view.findViewById(R.id.etFemaleUrinal)
        etFemaleWashbasins = view.findViewById(R.id.etFemaleWashbasins)
        /////////////////////////Non-Living Areas Section////////////
        spinnerFoodPreparedTrainingCenter =
            view.findViewById(R.id.spinnerFoodPreparedTrainingCenter)
        spinnerDiningRecreationAreaSeparate =
            view.findViewById(R.id.spinnerDiningRecreationAreaSeparate)
        spinnerIsReceptionAreaAva = view.findViewById(R.id.spinnerReceptionArea)
        spinnerTvAvailable = view.findViewById(R.id.spinnerTvAvailable)
        etKitchenLength = view.findViewById(R.id.etKitchenLength)
        etKitchenWidth = view.findViewById(R.id.etKitchenWidth)
        etKitchenArea = view.findViewById(R.id.etKitchenArea)
        etStoolsChairsBenches = view.findViewById(R.id.etStoolsChairsBenches)
        etWashArea = view.findViewById(R.id.etWashArea)
        etDiningLength = view.findViewById(R.id.etDiningLength)
        etDiningWidth = view.findViewById(R.id.etDiningWidth)
        etDiningArea = view.findViewById(R.id.etDiningArea)
        etRecreationLength = view.findViewById(R.id.etRecreationLength)
        etRecreationWidth = view.findViewById(R.id.etRecreationWidth)
        etRecreationArea = view.findViewById(R.id.etRecreationArea)

        /////////////////////////Indoor Game Section////////////
        etIndoorGameName = view.findViewById(R.id.etIndoorGameName)
        /////////////////////////Residential Facilities Section////////////
        spinnerWhetherHostelsSeparated = view.findViewById(R.id.spinnerWhetherHostelsSeparated)
        spinnerWardenWhereMalesStay = view.findViewById(R.id.spinnerWardenWhereMalesStay)
        spinnerWardenWhereLadyStay = view.findViewById(R.id.spinnerWardenWhereLadyStay)
        spinnerSecurityGaurdsAvailable = view.findViewById(R.id.spinnerSecurityGaurdsAvailable)
        spinnerWhetherFemaleDoctorAvailable =
            view.findViewById(R.id.spinnerWhetherFemaleDoctorAvailable)
        spinnerWhetherMaleDoctorAvailable =
            view.findViewById(R.id.spinnerWhetherMaleDoctorAvailable)
        /////////////////////////Support Facilities Section////////////
        spinnerSafeDrinikingAvailable = view.findViewById(R.id.spinnerSafeDrinikingAvailable)
        spinnerFirstAidKitAvailable = view.findViewById(R.id.spinnerFirstAidKitAvailable)
        spinnerFireFightingEquipmentAvailable =
            view.findViewById(R.id.spinnerFireFightingEquipmentAvailable)
        spinnerBiometricDeviceAvailable = view.findViewById(R.id.spinnerBiometricDeviceAvailable)
        spinnerElectricalPowerBackupAvailable =
            view.findViewById(R.id.spinnerElectricalPowerBackupAvailable)
        spinnerGrievanceRegisterAvailable =
            view.findViewById(R.id.spinnerGrievanceRegisterAvailable)


        spinnerAreaType = view.findViewById(R.id.spinnerSelectType) // Make sure this spinner exists in your XML
        villageContainer = view.findViewById(R.id.village_container)
        urbanContainer = view.findViewById(R.id.urban_container)
        spinnerSelectULB = view.findViewById(R.id.spinnerSelectULB)
        spinnerSelectWard = view.findViewById(R.id.spinnerSelectWard)

    }


    private fun checkAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) launchCamera()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        if (photoFile == null) {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT)
                .show()
            return
        }
        photoUri = FileProvider.getUriForFile(
            requireContext(), "${requireContext().packageName}.provider", photoFile
        )
        cameraLauncher.launch(photoUri)
    }

    private fun createImageFile(): File? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun ulbAdapterinitialize(urbanULBs: List<UlbItem>){

            val ulbAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, urbanULBs.mapNotNull { it.ulbName }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerSelectULB.adapter = ulbAdapter
        ulbAdapter.notifyDataSetChanged()
    }

    private fun wardAdapterinitialize(urbanWards:List<WardItem>){
        val wardAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            urbanWards.mapNotNull { it.wardName }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerSelectWard.adapter = wardAdapter
        wardAdapter.notifyDataSetChanged()
        //urbanWards.any(spinnerSelectULB.selectedItem.toString())
        // You can add item selection listeners if needed
        spinnerSelectULB.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.getWardAPI(
                    WardReq(
                        BuildConfig.VERSION_NAME,
                        AppUtil.getSavedLoginIdPreference(requireContext()),
                        ulbHashMap[spinnerSelectULB.selectedItem.toString()].toString()
                    ),AppUtil.getSavedTokenPreference(requireContext()))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupUrbanSpinners() {
        // Setup ULB spinner
        ulbAdapterinitialize(urbanULBs)

        // Setup Ward spinner
        wardAdapterinitialize(urbanWards)
    }

    private fun setupAreaTypeSpinner() {
        val areaTypeList = listOf("--Select--", "Rural", "Urban")
        val areaTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            areaTypeList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerAreaType.adapter = areaTypeAdapter

        spinnerAreaType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        // "--Select--"
                        villageContainer.gone()
                        urbanContainer.gone()
                    }
                    1 -> {
                        // "Rural" selected
                        villageContainer.visible()
                        urbanContainer.gone()

                        val request = BlockRequest(
                            appVersion = BuildConfig.VERSION_NAME,
                            districtCode = selectedDistrictCode,
                        )
                        viewModel.getBlockList(
                            request, AppUtil.getSavedTokenPreference(requireContext())
                        )

//                        val requestVill = VillageReq(
//                            appVersion = BuildConfig.VERSION_NAME,
//                            gpCode = selectedDistrictCode,
//                        )
//
//
//                        viewModel.getVillageList(
//                            requestVill, AppUtil.getSavedTokenPreference(requireContext())
//                        )


                       // viewModel.getVillageList(ULBReq(BuildConfig.VERSION_NAME,userPreferences.getUseID(),selectedDistrictLgdCodeItem),AppUtil.getSavedTokenPreference(requireContext()))


                        // Ensure village fields are mandatory for validation
                        setupVillageValidation(true)
                        setupUrbanValidation(false)
                    }
                    2 -> {
                        // "Urban" selected
                        villageContainer.gone()
                        urbanContainer.visible()

                        viewModel.getUlbAPI(
                            ULBReq(
                                BuildConfig.VERSION_NAME,
                                AppUtil.getSavedLoginIdPreference(requireContext()),
                                selectedLgdDistrictCode
                            ),AppUtil.getSavedTokenPreference(requireContext()))

                        setupVillageValidation(false)
                        setupUrbanValidation(true)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                villageContainer.gone()
                urbanContainer.gone()
            }
        }
    }

    var ulbHashMap: HashMap<String, String> = hashMapOf()

    private fun collectUlbResponse() {
        viewModel.getUlbList.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()

                when (response.responseCode) {
                    200 ->{
//                        response.wrappedList.forEach { it ->
//                            urbanULBs
//                        }
                        var urbanulbN: List<UlbItem> = response.wrappedList.mapNotNull { it }
                        urbanulbN.forEach { ulbItem ->
                            ulbHashMap[ulbItem.ulbName] = ulbItem.ulbCode
                        }
                        ulbAdapterinitialize(urbanulbN)
                    }

                      //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    var wardHashMap: HashMap<String, String> = hashMapOf()


    private fun collectWardResponse() {
        lifecycleScope.launch {
            viewModel.getWardAPI.observe(viewLifecycleOwner) { it ->
                it.onSuccess { response ->
                    hideProgressBar()

                    when (response.responseCode) {
                        200 ->{
                           var urbanWardsN: List<WardItem> = response.wrappedList.mapNotNull { it }
// This will filter out
                            urbanWardsN.forEach { ulbItem ->
                                wardHashMap[ulbItem.wardName] = ulbItem.wardCode
                            }
                            wardAdapterinitialize(urbanWardsN)
                        }

                        //  populateSpinnerVillage((response.wrappedList ?: emptyList()) as ArrayList<VillageModel?>, spinnerSelectULB )

                        202 -> Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                        301 -> Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()

                        401 -> AppUtil.showSessionExpiredDialog(
                            findNavController(),
                            requireContext()
                        )
                    }
                }
                it.onFailure {
                    hideProgressBar()

                    Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }






    private fun validateBasicInfoForm(view: View): Boolean {
        var isValid = true
        // Validate Select type selection
        if (!checkSpinner(spinnerAreaType, "Area Type")) isValid = false

        // Check if Rural is selected
        if (spinnerAreaType.selectedItemPosition == 1) { // Rural
            if (!checkSpinner(spinnerBlock, "Block")) isValid = false
            if (!checkSpinner(spinnerGp, "Gram Panchayat")) isValid = false
            if (!checkSpinner(spinnerVillage, "Village")) isValid = false
        }

        // Check if Urban is selected
        if (spinnerAreaType.selectedItemPosition == 2) { // Urban
            if (!checkSpinner(spinnerSelectULB, "ULB")) isValid = false
            if (!checkSpinner(spinnerSelectWard, "Ward")) isValid = false
        }

        // Validate all required Spinners
        if (!checkSpinner(spinnerState, "State")) isValid = false
        if (!checkSpinner(spinnerDistrict, "District")) isValid = false
       // if (!checkSpinner(spinnerBlock, "Block")) isValid = false
      //  if (!checkSpinner(spinnerVillage, "Village")) isValid = false
     //   if (!checkSpinner(spinnerGp, "Gp")) isValid = false
        if (!checkSpinner(spinnerTypeOfArea, "TypeOfArea")) isValid = false
        if (!checkSpinner(spinnerCatOfTCLocation, "Category Of Location")) isValid = false
        if (!checkSpinner(spinnerPickupAndDropFacility, "PickupAndDropFacility")) isValid = false
        if (!checkSpinner(spinnerWardenGender, "Warden Gender")) isValid = false
        if (!checkSpinner(binding.basicInfoInclude!!.spinnerFacilityType  , "Residential Facility type")) isValid = false

        // Validate required TextInputEditTexts
        if (!checkTextInput(etFacilityName, "Residential Facility Name")) isValid = false
        if (!checkTextInput(etHouseNo, "House No.")) isValid = false
        if (!checkTextInput(etStreet, "Street")) isValid = false
        if (!checkTextInput(etPoliceStation, "PoliceStation")) isValid = false
        if (!checkTextInput(etLandmark, "Landmark")) isValid = false
        if (!checkTextInput(etPinCode, "Pin code")) isValid = false
        if (!checkTextInput(etMobile, "Mobile No.")) isValid = false
        if (!checkTextInput(etPhone, "Residential Facility Phone No. with STD Code")) isValid =
            false
        if (!checkTextInput(etEmail, "Email Id")) isValid = false
        if (!checkTextInput(
                etDistanceFromBusStand, "Approximate Distance from a Prominent Bus Stand (In Mtrs.)"
            )
        ) isValid = false
        if (!checkTextInput(
                etDistanceFromAutoStand, "Approximate Distance from an Auto Stand (In Mtrs.)"
            )
        ) isValid = false
        if (!checkTextInput(
                etDistanceFromRailwayStand,
                "Approximate Distance from a Prominent Railway Station (In Mtrs.)"
            )
        ) isValid = false
        if (!checkTextInput(
                etDistanceFromTrainingToResidentialCentre,
                "Distance from the Training Centre to Residential Centre (In Kms.)"
            )
        ) isValid = false
        if (!checkTextInput(etWardenName, "Warden's Name")) isValid = false
        if (!checkTextInput(etWardenEmpID, "Warden's Employee ID")) isValid = false
        if (!checkTextInput(etWardenAddress, "Warden's Address")) isValid = false
        if (!checkTextInput(etWardenEmailId, "Warden's Email Id")) isValid = false
        if (!checkTextInput(etWardenMobile, "Warden's Mobile No.")) isValid = false
        if (base64ALDocFile == null || base64PVDocFile == null) isValid = false

        return isValid


    }

    private fun setupVillageValidation(isMandatory: Boolean) {
        if (isMandatory) {
            // Make village fields mandatory
            spinnerBlock.isEnabled = true
            spinnerGp.isEnabled = true
            spinnerVillage.isEnabled = true
        } else {
            // Optional - you can clear selections if needed
             spinnerBlock.setSelection(0)
             spinnerGp.setSelection(0)
             spinnerVillage.setSelection(0)
        }
    }

    private fun setupUrbanValidation(isMandatory: Boolean) {
        if (isMandatory) {
            // Make urban fields mandatory
            spinnerSelectULB.isEnabled = true
            spinnerSelectWard.isEnabled = true
        } else {
            // Optional - you can clear selections if needed
             spinnerSelectULB.setSelection(0)
             spinnerSelectWard.setSelection(0)
        }
    }

    private fun validateInfraInfoForm(view: View): Boolean {
        var isValid = true

        // ✅ Validate All Spinners
        isValid = isValid && checkSpinner(spinnerOwnerBuilding, "Ownership of Building")
        isValid = isValid && checkSpinner(spinnerRoofOfBuilding, "Roof of the Building")
        isValid = isValid && checkSpinner(
            spinnerWhetherStructurallySound, "Whether it is Structurally Sound"
        )
        isValid = isValid && checkSpinner(spinnerVisibleSignOfLeakage, "Visible Signs of Leakages")
        isValid =
            isValid && checkSpinner(spinnerConformanceDDUGKY, "Conformance to DDU-GKY Standards")
        isValid =
            isValid && checkSpinner(spinnerProtectionofStairs, "Protection of Stairs / Balconies")
        isValid = isValid && checkSpinner(spinnerCorridor, "Corridor")
        isValid = isValid && checkSpinner(spinnerSecuringWires, "Securing of Wires Done")
        isValid = isValid && checkSpinner(spinnerSwitchBoards, "Switch Boards and Panel Boards")
        isValid = isValid && checkSpinner(spinnerHostelNameBoard, "Hostel Name Board")
        isValid = isValid && checkSpinner(spinnerEntitlementBoard, "Entitlement Board")
        isValid =
            isValid && checkSpinner(spinnerContactDetail, "Contact Detail of Important People")
        isValid = isValid && checkSpinner(spinnerBasicInfoBoard, "Basic Information Board")
        isValid = isValid && checkSpinner(spinnerFoodSpecificationBoard, "Food Specification Board")

        // ✅ Validate Required Text Inputs
        isValid = isValid && checkTextInput(etAreaOfBuilding, "Area of the Building")
        isValid = isValid && checkTextInput(etCirculatingArea, "Circulating Area")
        isValid = isValid && checkTextInput(etAreaForOutDoorGames, "Area for Outdoor Games")

        // ✅ Validate Image Upload Only When Spinner = "YES"

        isValid = isValid && validateImageIfYes(
            spinnerWhetherStructurallySound,
            base64WhetherStructurallySoundDocFile,
            "Upload image for Structurally Sound"
        )
        isValid = isValid && validateImageIfYes(
            spinnerVisibleSignOfLeakage,
            base64SignOfLeakageDocFile,
            "Upload image for Visible Sign of Leakage"
        )
        isValid = isValid && validateImageIfYes(
            spinnerConformanceDDUGKY,
            base64ConformanceDDUGKYDocFile,
            "Upload image for Conformance to DDU-GKY"
        )
        isValid = isValid && validateImageIfYes(
            spinnerProtectionofStairs,
            base64ProtectionofStairsDocFile,
            "Upload image for Protection of Stairs"
        )
        isValid = isValid && validateImageIfYes(
            spinnerCorridor, base64CorridorDocFile, "Upload image for Corridor"
        )
        isValid = isValid && validateImageIfYes(
            spinnerSecuringWires, base64SecuringWiresDocFile, "Upload image for Securing Wires"
        )
        isValid = isValid && validateImageIfYes(
            spinnerSwitchBoards, base64SwitchBoardsDocFile, "Upload image for Switch Boards"
        )
        isValid = isValid && validateImageIfYes(
            spinnerHostelNameBoard,
            base64HostelNameBoardDocFile,
            "Upload image for Hostel Name Board"
        )
        isValid = isValid && validateImageIfYes(
            spinnerEntitlementBoard,
            base64EntitlementBoardDocFile,
            "Upload image for Entitlement Board"
        )
        isValid = isValid && validateImageIfYes(
            spinnerContactDetail, base64ContactDetailDocFile, "Upload image for Contact Detail"
        )
        isValid = isValid && validateImageIfYes(
            spinnerBasicInfoBoard, base64BasicInfoBoardDocFile, "Upload image for Basic Info Board"
        )
        isValid = isValid && validateImageIfYes(
            spinnerFoodSpecificationBoard,
            base64FoodSpecificationBoardDocFile,
            "Upload image for Food Specification Board"
        )


        if (base64OwnerBuildingDocFile == null || base64RoofOfBuildingDocFile == null || base64CirculatingAreaProof == null) isValid =
            false
        return isValid
    }

    private fun validateLivingAreaInfoForm(view: View): Boolean {
        var isValid = true

        // Validate all required Spinners
        if (!checkSpinner(spinnerTypeLivingRoof, "Type of Roof(RCC/Non RCC)")) isValid = false
        if (!checkSpinner(spinnerCeiling, "False Ceiling Provided")) isValid = false
        if (!checkSpinner(spinnerAirConditioning, "Does the room has Air Conditioning ?")) isValid =
            false
        if (!checkSpinner(
                spinnerLivingAreaInfoBoard, "Living Area Information Board as per SF 5.1 B4 ?"
            )
        ) isValid = false

        // Validate required TextInputEditTexts
        if (!checkTextInput(etHeightOfCeiling, "Height of Ceiling(In ft)")) isValid = false
        if (!checkTextInput(etRoomLength, "Length (In ft)")) isValid = false
        if (!checkTextInput(etRoomWidth, "Width (In ft)")) isValid = false
        if (!checkTextInput(etRoomWindowArea, "Window Area (In Sq. ft)")) isValid = false
        if (!checkTextInput(etCot, "Cot (In No.)")) isValid = false
        if (!checkTextInput(etMattress, "Mattress (In No.)")) isValid = false
        if (!checkTextInput(etBedSheet, "Bed Sheet (In No.)")) isValid = false
        if (!checkTextInput(
                etCupboard, "Cupboard / Almirah / Trunk with Locking Arrangements (In No.)"
            )
        ) isValid = false
        if (!checkTextInput(etLivingAreaLights, "Lights")) isValid = false
        if (!checkTextInput(etLivingAreaFans, "Fans")) isValid = false



        isValid = isValid && validateImageIfYes(
            spinnerCeiling, base64CeilingDocFile, "Upload image for False Ceiling Provided"
        )
        isValid = isValid && validateImageIfYes(
            spinnerAirConditioning,
            base64AirConditioningDocFile,
            "Upload image for Air Conditioning"
        )
        isValid = isValid && validateImageIfYes(
            spinnerLivingAreaInfoBoard,
            base64LivingAreaInfoBoardDocFile,
            "Upload image for Living Area Info Board"
        )

        if (base64TypeLivingRoofDocFile == null || base64CotDocFile == null || base64MattressDocFile == null || base64BedSheetDocFile == null || base64CupBoardDocFile == null || base64LightsDocFile == null || base64FansDocFile == null || base64AirHieghtOfCelingDocFile == null || base64WindowAreaDocFile == null) isValid =
            false

        return isValid
    }

    fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
        return if (spinner.selectedItemPosition == 0) {
            spinner.requestFocus()
            Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    fun checkTextInput(editText: TextInputEditText, fieldName: String): Boolean {
        return if (editText.text.isNullOrBlank()) {
            editText.error = "Please enter $fieldName"
            editText.requestFocus()
            false
        } else {
            true
        }
    }

    private fun validateToiletInfoForm(view: View): Boolean {
        var isValid = true


        // Validate all required Spinners
        if (!checkSpinner(spinnerToiletType, "Toilet Type")) isValid = false
        if (!checkSpinner(spinnerToiletFlooringType, "Type of Flooring")) isValid = false
        if (!checkSpinner(spinnerConnectionToRunningWater, "Connection To Running Water")) isValid =
            false/* if (!checkSpinner(spinnerOverheadTanks, "Overhead Tanks")) isValid = false


        // Validate required TextInputEditTexts
       if (!checkTextInput(etLightsInToilet, "Lights (In No.)")) isValid = false
        if (!checkTextInput(etFemaleUrinal, "Female Urinals")) isValid = false
        if (!checkTextInput(etFemaleWashbasins, "Female Washbasins")) isValid = false*/

        if (base64LightsInToiletDocFile == null || base64ToiletFlooringDocFile == null) isValid =
            false
        isValid = isValid && validateImageIfYes(
            spinnerConnectionToRunningWater,
            base64RunningWaterDocFile,
            "Upload image for Connection To Running Water"
        )
        return isValid
    }


    private fun validateAdditionalToiletInfoForm(view: View): Boolean {
        var isValid = true
        // Validate all required Spinners

        if (!checkSpinner(spinnerOverheadTanks, "Overhead Tanks")) isValid = false


        // Validate required TextInputEditTexts
        if (!checkTextInput(etFemaleUrinal, "Female Urinals")) isValid = false
        if (!checkTextInput(etFemaleWashbasins, "Female Washbasins")) isValid = false

        if (base64UrinalsDocFile == "" || base64WashBasinDocFile == "") isValid = false



        isValid = isValid && validateImageIfYes(
            spinnerOverheadTanks,
            base64OverHeadTankDocFile,
            "Upload image for Connection To OverHead Tank"
        )

        return isValid


    }


    private fun validateNonLivingAreaInfoForm(view: View): Boolean {
        var isValid = true
        // Validate all required Spinners
        if (!checkSpinner(
                spinnerFoodPreparedTrainingCenter,
                "Whether Food for the Candidates is being Prepared in the Premises of the Training Center?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerDiningRecreationAreaSeparate,
                "Are the Dining and Recreation Area Separate?"
            )
        ) isValid = false
        if (!checkSpinner(spinnerIsReceptionAreaAva, "Is Reception area is available?")) isValid =
            false
        if (!checkSpinner(
                spinnerTvAvailable,
                "Whether TV with a Cable or Satellite Connection is Available for Viewing?"
            )
        ) isValid = false

        if (selectedRfFoodPre == "Yes") {

            if (!checkTextInput(etKitchenLength, "Length (In ft)")) isValid = false
            if (!checkTextInput(etKitchenWidth, "Width (In ft)")) isValid = false
        }

        // Validate required TextInputEditTexts
        if (!checkTextInput(etStoolsChairsBenches, "No.of Stools/Chairs/Benches")) isValid = false
        if (spinnerDiningRecreationAreaSeparate.selectedItem.toString() == "Yes") {
            if (!checkTextInput(etDiningLength, "Length (in ft)")) isValid = false
            if (!checkTextInput(etDiningWidth, "Width (in ft)")) isValid = false
            if (!checkTextInput(etRecreationLength, "Length (in ft)")) isValid = false
            if (!checkTextInput(etRecreationWidth, "Width (in ft)")) isValid = false

        } else {
            if (!checkTextInput(binding.etDiningAndRecreactionWidth, "Length (in ft)")) isValid =
                false
            if (!checkTextInput(binding.etDiningAndRecreactionLength, "Width (in ft)")) isValid =
                false

        }

        isValid = isValid && validateImageIfYes(
            spinnerDiningRecreationAreaSeparate,
            base64RecreationAreaDocFile,
            "Upload image for Recreation Area"
        )
        isValid = isValid && validateImageIfYes(
            spinnerDiningRecreationAreaSeparate,
            base64DinningAreaDocFile,
            "Upload image for Dining Area"
        )
        isValid = isValid && validateImageIfNo(
            spinnerDiningRecreationAreaSeparate,
            base64RecreationDiningAreaDocFile,
            "Upload image for Recreation and Dining Area"
        )
        if (!checkTextInput(etWashArea, "Wash Area")) isValid = false
        isValid = isValid && validateImageIfYes(
            spinnerFoodPreparedTrainingCenter,
            base64FoodPreparedTrainingDocFile,
            "Upload image for Food Preparation"
        )
        isValid = isValid && validateImageIfYes(
            spinnerIsReceptionAreaAva, base64ReceptionAreaDocFile, "Upload image for Reception Area"
        )
        return isValid
    }

    private fun validateResidentialFacilitiesInfoForm(view: View): Boolean {
        var isValid = true
        // Validate all required Spinners
        if (!checkSpinner(
                spinnerWhetherHostelsSeparated,
                "Whether Hostels for Male and Female Candidates Separated?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerWardenWhereMalesStay, "Warden/care taker for hostels where males stay?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerWardenWhereLadyStay, "Lady warden/caretaker for hostels where females stay?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerSecurityGaurdsAvailable, "Are Security Gaurds Available ?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerWhetherFemaleDoctorAvailable,
                "Whether Female Doctor on call is Available or Not ?"
            )
        ) isValid = false
        if (!checkSpinner(
                spinnerWhetherMaleDoctorAvailable,
                "Whether Male Doctor on call is Available or Not?"
            )
        ) isValid = false

        isValid = isValid && validateImageIfYes(
            spinnerWhetherHostelsSeparated,
            base64WhetherHostelsSeparatedDocFile,
            "Upload image for Hostels Separated"
        )

        isValid = isValid && validateImageIfYes(
            spinnerWardenWhereMalesStay,
            base64WardenWhereMalesStayDocFile,
            "Upload image for Warden/Caretaker (Male Hostel)"
        )

        isValid = isValid && validateImageIfYes(
            spinnerWardenWhereLadyStay,
            base64WardenWhereLadyStayDocFile,
            "Upload image for Lady Warden/Caretaker (Female Hostel)"
        )

        isValid = isValid && validateImageIfYes(
            spinnerSecurityGaurdsAvailable,
            base64SecurityGaurdsDocFile,
            "Upload image for Security Guards"
        )

        isValid = isValid && validateImageIfYes(
            spinnerWhetherFemaleDoctorAvailable,
            base64WhetherFemaleDoctorDocFile,
            "Upload image for Female Doctor"
        )

        isValid = isValid && validateImageIfYes(
            spinnerWhetherMaleDoctorAvailable,
            base64WhetherMaleDoctorDocFile,
            "Upload image for Male Doctor"
        )
        return isValid


    }

    private fun validateSupportFacilitiesInfoForm(view: View): Boolean {
        var isValid = true

        // Validate all required Spinners
        if (!checkSpinner(spinnerSafeDrinikingAvailable, "Safe Driniking Available ?")) isValid =
            false
        if (!checkSpinner(spinnerFirstAidKitAvailable, "First Aid Kit?")) isValid = false
        if (!checkSpinner(
                spinnerFireFightingEquipmentAvailable, "Fire-fighting Equipment?"
            )
        ) isValid = false
        if (!checkSpinner(spinnerBiometricDeviceAvailable, "Biometric Device?")) isValid = false
        if (!checkSpinner(
                spinnerElectricalPowerBackupAvailable, "Electrical Power Backup?"
            )
        ) isValid = false
        if (!checkSpinner(spinnerGrievanceRegisterAvailable, "Grievance Register?")) isValid = false


        if (base64SafeDrinkingDocFile == null || base64FirstAidKitDocFile == null || base64FireFightingEquipmentDocFile == null || base64BiometricDeviceDocFile == null || base64ElectricalPowerDocFile == null || base64GrievanceRegisterDocFile == null) isValid =
            false

        isValid = isValid && validateImageIfYes(
            spinnerSafeDrinikingAvailable,
            base64SafeDrinkingDocFile,
            "Please upload document for Safe Drinking"
        )

        isValid = isValid && validateImageIfYes(
            spinnerFirstAidKitAvailable,
            base64FirstAidKitDocFile,
            "Please upload document for First Aid Kit"
        )

        isValid = isValid && validateImageIfYes(
            spinnerFireFightingEquipmentAvailable,
            base64FireFightingEquipmentDocFile,
            "Please upload document for Fire-fighting Equipment"
        )

        isValid = isValid && validateImageIfYes(
            spinnerBiometricDeviceAvailable,
            base64BiometricDeviceDocFile,
            "Please upload document for Biometric Device"
        )

        isValid = isValid && validateImageIfYes(
            spinnerElectricalPowerBackupAvailable,
            base64ElectricalPowerDocFile,
            "Please upload document for Electrical Power Backup"
        )

        isValid = isValid && validateImageIfYes(
            spinnerGrievanceRegisterAvailable,
            base64GrievanceRegisterDocFile,
            "Please upload document for Grievance Register"
        )

        return isValid
    }

    private fun calculateAndShowArea(
        etLength: EditText, etWidth: EditText, tvArea: TextView
    ) {
        val lengthStr = etLength.text.toString().trim()
        val widthStr = etWidth.text.toString().trim()

        // Safely convert inputs to numbers
        val length = lengthStr.toDoubleOrNull() ?: 0.0
        val width = widthStr.toDoubleOrNull() ?: 0.0

        // Calculate area
        val area = length * width

        // Format to 2 decimal places for clean output
        tvArea.text = String.format("%.2f", area)
    }

    private fun setupAutoAreaCalculation(
        etLengths: EditText, etWidths: EditText, tvAreas: TextView
    ) {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateAndShowArea(etLengths, etWidths, tvAreas)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etLengths.addTextChangedListener(watcher)
        etWidths.addTextChangedListener(watcher)
    }


    private fun calculateAndShowAreaForRoom(
        etLength: EditText, etWidth: EditText, tvArea: TextView
    ) {
        val lengthStr = etLength.text.toString().trim()
        val widthStr = etWidth.text.toString().trim()

        // Safely convert inputs to numbers
        val length = lengthStr.toDoubleOrNull() ?: 0.0
        val width = widthStr.toDoubleOrNull() ?: 0.0

        // Calculate area
        val area = length * width

        tvArea.text = String.format("%.2f", area)

        val value = etRoomArea.text.toString().toDoubleOrNull()
        if (value != null) {
            selectedRoomPermitted = (value / 25).toInt()
            Log.d("Result", "Approx value: $selectedRoomPermitted")
            binding.etStudentsPermitted.text = selectedRoomPermitted.toString()
        } else {
            Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
        }


    }

    private fun setupAutoAreaCalculationForRoom(
        etLengths: EditText, etWidths: EditText, tvAreas: TextView
    ) {
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calculateAndShowAreaForRoom(etLengths, etWidths, tvAreas)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        etLengths.addTextChangedListener(watcher)
        etWidths.addTextChangedListener(watcher)
    }


    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocation = ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocation == PackageManager.PERMISSION_GRANTED || coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentLocation() {
        // Uses high accuracy priority for precise location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    binding.basicInfoInclude?.tvLatLang?.text =
                        location.latitude.toString() + "," + location.longitude.toString()
                    latValue = location.latitude.toString()
                    langValue = location.longitude.toString()
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(), "Failed to get location: ${it.message}", Toast.LENGTH_SHORT
                ).show()
            }
    }


    private fun submitRFBasicInfoForm(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""


        val areaType = when (spinnerAreaType.selectedItemPosition) {
            1 -> "Rural"
            2 -> "Urban"
            else -> ""
        }

        // Get urban/rural specific data
        val ulbName = if (spinnerAreaType.selectedItemPosition == 2) {
            spinnerSelectULB.selectedItem.toString()
        } else ""

        val wardName = if (spinnerAreaType.selectedItemPosition == 2) {
            spinnerSelectWard.selectedItem.toString()
        } else ""

        val request = insertRfBasicInfoReq(
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            schemeName = "DDUGKY",
            residentialFacilityName = etFacilityName.text.toString(),
            residentialType = binding.basicInfoInclude?.spinnerFacilityType?.selectedItem.toString(),
            residentialCenterLocation = "",
            houseNo = etHouseNo.text.toString(),
            streetNo1 = etStreet.text.toString(),
            streetNo2 = "",
            landMark = etLandmark.text.toString(),
            stateCode = selectedStateCode,
            districtCode = selectedDistrictCode,
            blockCode = selectedBlockCode,
            gpCode = selectedGpCode,
            villageCode = selectedVillageCode,
            policeStation = etPoliceStation.text.toString(),
            pincode = etPinCode.text.toString(),
            mobile = etMobile.text.toString(),
            residentialFacilityPhoneNo = etPhone.text.toString(),
            email = etEmail.text.toString(),
            typeOfArea = spinnerTypeOfArea.selectedItem.toString(),
            latitude = latValue,
            longitude = langValue,
            geoAddress = "",
            categoryOfTC = spinnerCatOfTCLocation.selectedItem.toString(),
            distBusStand = etDistanceFromBusStand.text.toString(),
            distAutoStand = etDistanceFromAutoStand.text.toString(),
            distRailStand = etDistanceFromRailwayStand.text.toString(),
            distfromTC = etDistanceFromTrainingToResidentialCentre.text.toString(),
            pickUpDrop = spinnerPickupAndDropFacility.selectedItem.toString(),
            wardName = etWardenName.text.toString(),
            wardGender = spinnerWardenGender.selectedItem.toString(),
            wardEmployeeId = etWardenEmpID.text.toString(),
            wardAddress = etWardenAddress.text.toString(),
            wardEmail = etWardenEmailId.text.toString(),
            wardMobile = etWardenMobile.text.toString(),
            empLetterFile = base64ALDocFile!!,
            policeVerificationFile = base64PVDocFile!!,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            resFacilityId = facilityId.toInt(),
            locality = areaType,
            wardCode = wardHashMap[spinnerSelectWard.selectedItem.toString()].toString(),
            ulbCode = ulbHashMap[spinnerSelectULB.selectedItem.toString()].toString()
        )

        viewModel.SubmitRfBasicInformationToServer(request, token)
        showProgressBar()
    }


    private fun submitRFInfraForm(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = InsertRfInfraDetaiReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            ownership = spinnerOwnerBuilding.selectedItem.toString(),
            buildingArea = etAreaOfBuilding.text.toString(),
            roof = spinnerRoofOfBuilding.selectedItem.toString(),
            plastering = spinnerWhetherStructurallySound.selectedItem.toString(),
            empLetterFile = base64OwnerBuildingDocFile!!,
            buildingPlan = base64BuildingAreaDocFile!!,
            photosBuilding = base64RoofOfBuildingDocFile!!,
            photosWalls = base64WhetherStructurallySoundDocFile!!,
            leakages = spinnerVisibleSignOfLeakage.selectedItem.toString(),
            conformanceDDU = spinnerConformanceDDUGKY.selectedItem.toString(),
            protectionStairs = spinnerProtectionofStairs.selectedItem.toString(),
            circulatingArea = etCirculatingArea.text.toString(),
            corridor = spinnerCorridor.selectedItem.toString(),
            securingWiresDone = spinnerSecuringWires.selectedItem.toString(),
            switchBoardsPanelBoards = spinnerSwitchBoards.selectedItem.toString(),
            hostelNameBoard = spinnerHostelNameBoard.selectedItem.toString(),
            studentEntitlementBoard = spinnerEntitlementBoard.selectedItem.toString(),
            contactDetailImportantPeople = spinnerContactDetail.selectedItem.toString(),
            basicInformationBoard = spinnerBasicInfoBoard.selectedItem.toString(),
            foodSpecificationBoard = spinnerFoodSpecificationBoard.selectedItem.toString(),
            openSpaceArea = etAreaForOutDoorGames.text.toString(),
            leakagesProof = base64SignOfLeakageDocFile!!,
            conformanceDDUProof = base64ConformanceDDUGKYDocFile!!,
            protectionStairsProof = base64ProtectionofStairsDocFile!!,
            circulatingAreaProof = base64CirculatingAreaProof!!,
            corridorProof = base64CorridorDocFile!!,
            securingWiresDoneProof = base64SecuringWiresDocFile!!,
            switchBoardsPanelBoardsProof = base64SwitchBoardsDocFile!!,
            hostelNameBoardProof = base64HostelNameBoardDocFile!!,
            studentEntitlementBoardProof = base64EntitlementBoardDocFile!!,
            contactDetailImportantPeopleproof = base64ContactDetailDocFile!!,
            basicInformationBoardproof = base64BasicInfoBoardDocFile!!,
            foodSpecificationBoardproof = base64FoodSpecificationBoardDocFile!!

        )

        viewModel.SubmitRfInfraDetailsAndComlianceToServer(request, token)
        showProgressBar()
    }


    private fun submitRFLivingAreaForm(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""


        val request = InsertLivingAreaReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            roofType = spinnerTypeLivingRoof.selectedItem.toString(),
            typeOfRoofFilePath = base64TypeLivingRoofDocFile!!,
            falseCeiling = spinnerCeiling.selectedItem.toString(),
            falseCeilingFilePath = base64CeilingDocFile!!,
            ceilingHeight = etHeightOfCeiling.text.toString(),
            length = etRoomLength.text.toString(),
            width = etRoomWidth.text.toString(),
            area = etRoomArea.text.toString(),
            windowArea = etRoomWindowArea.text.toString(),
            airConditioning = spinnerAirConditioning.selectedItem.toString(),
            airConditioningFilePath = base64AirConditioningDocFile!!,
            cot = etCot.text.toString(),
            cotFilePath = base64CotDocFile!!,
            mattress = etMattress.text.toString(),
            mattressFilePath = base64MattressDocFile!!,
            bedSheet = etBedSheet.text.toString(),
            bedSheetFilePath = base64BedSheetDocFile!!,
            storage = etCupboard.text.toString(),
            storageFilePath = base64CupBoardDocFile!!,
            infoBoard = spinnerLivingAreaInfoBoard.selectedItem.toString(),
            infoBoardFilePath = base64LivingAreaInfoBoardDocFile!!,
            studentsPermitted = selectedRoomPermitted.toString(),
            lights = etLivingAreaLights.text.toString(),
            lightsFilePath = base64LightsDocFile!!,
            fans = etLivingAreaFans.text.toString(),
            fansFilePath = base64FansDocFile!!,
            ceilingHeightFilePath = base64AirHieghtOfCelingDocFile!!,
            areaFilePath = "",
            windowAreaFilePath = base64WindowAreaDocFile!!
        )

        viewModel.SubmitRfLivingAreaInformationToServer(request, token)
        showProgressBar()
    }


    private fun submitRFToiletForm(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""


        val request = InsertToiletDataReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            type = spinnerToiletType.selectedItem.toString(),
            lights = binding.etLightsInToilet.text.toString().toIntOrNull() ?: 0,
            proofLight = base64LightsInToiletDocFile!!,
            flooring = spinnerToiletFlooringType.selectedItem.toString(),
            proofFloor = base64ToiletFlooringDocFile!!,
            runningWater = spinnerConnectionToRunningWater.selectedItem.toString(),
            runningWaterFile = base64RunningWaterDocFile!!
        )

        viewModel.SubmitRfToiletDataToServer(request, token)
        showProgressBar()
    }


    private fun submitRFAdditionalToiletForm(view: View) {


        val request = UrinalWashbasinReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            urinal = binding.urinalWashbasin.etFemaleUrinal.text.toString().toIntOrNull() ?: 0,
            washbasin = binding.urinalWashbasin.etFemaleWashbasins.text.toString().toIntOrNull()
                ?: 0,
            overheadTank = binding.urinalWashbasin.spinnerOverheadTanks.selectedItem.toString(),
            urinalFile = base64UrinalsDocFile!!,
            washbasinFile = base64WashBasinDocFile!!,
            overheadTankFile = base64OverHeadTankDocFile!!
        )

        viewModel.insertRfToiletWashRoomDetail(request)
        showProgressBar()
    }


    private fun submitRFNonLivingAreaForm(view: View) {

        val request = InsertNonLivingReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            preparedFood = spinnerFoodPreparedTrainingCenter.selectedItem.toString(),
            preparedFoodFile = base64FoodPreparedTrainingDocFile!!,
            kitchenLength = etKitchenLength.text.toString().toDoubleOrNull() ?: 0.0,
            kitchenWidth = etKitchenWidth.text.toString().toDoubleOrNull() ?: 0.0,
            kitchenArea = etKitchenArea.text.toString().toDoubleOrNull() ?: 0.0,
            separateAreas = spinnerDiningRecreationAreaSeparate.selectedItem.toString(),
            noOfSeats = etStoolsChairsBenches.text.toString().toIntOrNull() ?: 0,
            washArea = etWashArea.text.toString(),
            tvAvailable = spinnerTvAvailable.selectedItem.toString(),
            diningLength = binding.etDiningLength.text.toString().toDoubleOrNull() ?: 0.0,
            diningArea = binding.etDiningArea.text.toString().toDoubleOrNull() ?: 0.0,
            diningWidth = binding.etDiningWidth.text.toString().toDoubleOrNull() ?: 0.0,
            recreationLength = binding.etRecreationLength.text.toString().toDoubleOrNull() ?: 0.0,
            recreationWidth = binding.etRecreationWidth.text.toString().toDoubleOrNull() ?: 0.0,
            recreationArea = binding.etRecreationArea.text.toString().toDoubleOrNull() ?: 0.0,
            receptionArea = binding.spinnerReceptionArea.selectedItem.toString(),
            receptionAreaFile = base64ReceptionAreaDocFile!!,

            diningRecreationLength = binding.etDiningAndRecreactionLength.text.toString()
                .toDoubleOrNull() ?: 0.0,
            diningRecreationWidth = binding.etDiningAndRecreactionWidth.text.toString()
                .toDoubleOrNull() ?: 0.0,
            diningRecreationArea = binding.etDiningAndRecreactionArea.text.toString()
                .toDoubleOrNull() ?: 0.0,
            diningRecreationAreaFile = base64RecreationDiningAreaDocFile!!,
            diningAreaFile = base64DinningAreaDocFile!!,
            recreationAreaFile = base64RecreationAreaDocFile!!
        )

        viewModel.SubmitRfNonLivingAreaDataToServer(request)
        showProgressBar()
    }


    private fun SubmitRfIndoorGameDetails() {

        val finalArray = indoorGamesList.map { game ->
            IndoorGameItem(
                indoreGameName = game.gameName, indoreGameFile = game.gamePhoto
            )
        }

        val request = IndoorGamesRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            facilityId = facilityId.toInt(),
            finalArray = finalArray
        )

        viewModel.SubmitRfIndoorGameDetails(request)
        showProgressBar()
    }

    private fun SubmitRfAvaibilityDetails(view: View) {

        val request = InsertResidentialFacility(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            hostelsSeparated = spinnerWhetherHostelsSeparated.selectedItem.toString(),
            hostelsSeparatedFile = base64WhetherHostelsSeparatedDocFile!!,
            wardenCaretakerMale = spinnerWardenWhereMalesStay.selectedItem.toString(),
            wardenCaretakerMaleFile = base64WardenWhereMalesStayDocFile!!,
            wardenCaretakerFemale = spinnerWardenWhereLadyStay.selectedItem.toString(),
            wardenCaretakerFemaleFile = base64WardenWhereLadyStayDocFile!!,
            securityGuards = spinnerSecurityGaurdsAvailable.selectedItem.toString(),
            securityGuardsFile = base64SecurityGaurdsDocFile!!,
            femaleDoctor = spinnerWhetherFemaleDoctorAvailable.selectedItem.toString(),
            femaleDoctorFile = base64WhetherFemaleDoctorDocFile!!,
            maleDoctor = spinnerWhetherMaleDoctorAvailable.selectedItem.toString(),
            maleDoctorFile = base64WhetherMaleDoctorDocFile!!,
            facilityId = facilityId
        )

        viewModel.SubmitRfAvaibilityDetails(request)
        showProgressBar()
    }


    private fun SubmitRfSupportFacilitiesDetails(view: View) {

        val request = InsertSupportFacilitiesReq(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            sanctionOrder = sanctionOrder,
            trainingCentre = centerId.toInt(),
            safeDrinking = spinnerSafeDrinikingAvailable.selectedItem.toString(),
            safeDrinkingFile = base64SafeDrinkingDocFile!!,
            firstAidKit = spinnerFirstAidKitAvailable.selectedItem.toString(),
            firstAidKitFile = base64FirstAidKitDocFile!!,
            fireFighting = spinnerFireFightingEquipmentAvailable.selectedItem.toString(),
            fireFightingFile = base64FireFightingEquipmentDocFile!!,
            biometricDevice = spinnerBiometricDeviceAvailable.selectedItem.toString(),
            biometricDeviceFile = base64BiometricDeviceDocFile!!,
            powerBackup = spinnerElectricalPowerBackupAvailable.selectedItem.toString(),
            powerBackupFile = base64ElectricalPowerDocFile!!,
            grievanceRegister = spinnerGrievanceRegisterAvailable.selectedItem.toString(),
            grievanceRegisterFile = base64GrievanceRegisterDocFile!!,
            facilityId = facilityId
        )

        viewModel.SubmitRfSupportFacilitiesDetails(request)
        showProgressBar()
    }


    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun showProgressBar() {
        if (context != null && isAdded && progress?.isShowing == false) {
            progress?.show()
        }
    }

    fun hideProgressBar() {
        if (progress?.isShowing == true) {
            progress?.dismiss()
        }
    }

    private fun removeGame(game: IndoorGame) {
        val index = indoorGamesList.indexOf(game)
        if (index != -1) {
            indoorGamesList.removeAt(index)
            adapterGame.notifyItemRemoved(index)

            // Recalculate numbering
            indoorGamesList.forEachIndexed { i, g ->
                indoorGamesList[i] = g.copy(gameNumber = i + 1)
            }
            adapterGame.notifyDataSetChanged()
            gameCounter = indoorGamesList.size + 1
        }
    }


    private fun observeViewModelLivingAreaList() {
        viewModel.getRfLivingRoomListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        livingRoomAdapter.updateList(it.wrappedList)

                    }

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun observeDeleteResponse() {
        viewModel.deleteLivingRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                Toast.makeText(requireContext(), "Room Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()

                deletedItem?.let { item ->
                    livingRoomAdapter.removeItem(item)
                    deletedItem = null
                }

                if (livingRoomAdapter.itemCount == 0) {
                    isLivingAreaVisible = true
                }

                val rfLAreaListReq = LivingRoomReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    sanctionOrder = sanctionOrder,
                    tcId = centerId.toInt(),
                    facilityId = facilityId.toInt()

                )

                viewModel.getRfLivingRoomListView(rfLAreaListReq)


            }

            result.onFailure {
                hideProgressBar()
                Toast.makeText(
                    requireContext(), "Room Deletion failed: ${it.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun setupToiletRecyclerView() {
        ToiletAdapter = ToiletAdapter(mutableListOf()) { toiletItem ->
            showProgressBar()
            deletedToiletItem = toiletItem

            val deleteRequest = ToiletDeleteList(
                appVersion = BuildConfig.VERSION_NAME, rfToiletId = toiletItem.rfToiletId.toString()
            )

            viewModel.deleteToiletRoom(deleteRequest)
        }

        binding.toiletRecycler.apply {
            adapter = ToiletAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModelToiletList() {
        viewModel.toiletSectionListView.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        ToiletAdapter.updateData(it.wrappedList)
                    }

                    202 -> Toast.makeText(
                        requireContext(), "No Toilet data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeDeleteToiletResponse() {
        viewModel.deleteToiletRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                Toast.makeText(requireContext(), "Toilet Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()

                deletedToiletItem?.let { item ->
                    ToiletAdapter.removeItem(item)
                    deletedToiletItem = null
                }

                if (ToiletAdapter.itemCount == 0) {
                    isToiletsVisible = true
                }

                // ✅ Re-fetch and immediately observe the updated list
                val request = LivingRoomReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    sanctionOrder = sanctionOrder,
                    tcId = centerId.toInt(),
                    facilityId = facilityId.toInt()

                )

                viewModel.getToiletSectionListView(request)

                // ✅ Observe fresh result directly here
                viewModel.toiletSectionListView.observe(viewLifecycleOwner) { listResult ->
                    listResult.onSuccess { listResponse ->
                        if (listResponse.responseCode == 200) {
                            ToiletAdapter.updateData(listResponse.wrappedList)
                        }
                    }
                }
            }

            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Delete failed: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setupReceptionAreaSpinner() {


        binding.spinnerFoodPreparedTrainingCenter.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    selectedRfFoodPre = parent.getItemAtPosition(position).toString()

                    binding.llFoodPrepared.visibility =
                        if (selectedRfFoodPre == "Yes") View.VISIBLE else View.GONE
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    private fun validateImageIfYes(spinner: Spinner, base64: String?, message: String): Boolean {
        return if (spinner.selectedItem.toString() == "Yes" && base64.isNullOrEmpty()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            false
        } else true
    }


    private fun validateImageIfNo(spinner: Spinner, base64: String?, message: String): Boolean {
        return if (spinner.selectedItem.toString() == "No" && base64.isNullOrEmpty()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            false
        } else true
    }


    private fun collectSectionStatus() {

        viewModel.getRFSectionStatus.observe(viewLifecycleOwner) { result ->

            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        sectionsStatus = it.wrappedList.get(0)

                        if (sectionsStatus.basiInfoSection > 0) {
                            binding.basicInfoInclude?.ivToggleTCBasicInfo?.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.infraDtlComplianceSection > 0) {
                            binding.ivToggleInfraDetailCompliance.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.livingAreaInfoSection > 0) {
                            binding.ivToggleLivingAreaInfo.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.toiletSection > 0) {
                            binding.ivToggleToilets.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.nonLivingAreaSection > 0) {
                            binding.ivToggleNonLivingArea.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.indoorGameDtlSection > 0) {
                            binding.ivToggleIndoorGameDetail.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.rfAvailableSection > 0) {
                            binding.ivToggleRfAvailable.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.supportFacltySection > 0) {
                            binding.ivToggleSfAvailable.setImageResource(R.drawable.ic_verified)
                        }

                        if (sectionsStatus.toiletAdditionalSection > 0) {
                            binding.urinalWashbasin.ivToggleUrinalWashbasin.setImageResource(R.drawable.ic_verified)
                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(), it.responseDesc, Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
        }
    }

    fun showEditSectionDialog(sectionName: String, onYesClicked: () -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_section, null)

        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogView).setCancelable(false)
                .create()

        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnYes = dialogView.findViewById<MaterialButton>(R.id.btnYes)
        val btnNo = dialogView.findViewById<MaterialButton>(R.id.btnNo)

        tvMessage.text = "Do you want to edit the $sectionName section?"

        btnYes.setOnClickListener {
            dialog.dismiss()
            onYesClicked.invoke()   // <-- Call respective API
        }

        btnNo.setOnClickListener {
            val sectionReq = SectionReq(
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                appVersion = BuildConfig.VERSION_NAME,
                imeiNo = AppUtil.getAndroidId(requireContext()),
                sanctionOrder = sanctionOrder,
                tcId = centerId,
                facilityId = facilityId

            )

            viewModel.getRFSectionStatus(sectionReq)
            showProgressBar()

            binding.basicInfoInclude?.layoutTCBasicInfoContent?.gone()
            binding.layoutInfraDetailComplianceContent.gone()
            binding.layoutLivingAreaInfoContent.gone()
            binding.layoutToiletsContent.gone()
            binding.layoutNonLivingAreaContent.gone()
            binding.layoutIndoorGameDetailContent.gone()
            binding.layoutRfAvailableContent.gone()
            binding.layoutSfAvailableContent.gone()
            binding.urinalWashbasin.layoutUrinalWashbasinContent.gone()

            dialog.dismiss()
        }

        dialog.show()
    }


    fun showEditRemarksDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_section, null)

        val dialog =
            MaterialAlertDialogBuilder(requireContext()).setView(dialogView).setCancelable(false)
                .create()

        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val btnYes = dialogView.findViewById<MaterialButton>(R.id.btnYes)
        val btnNo = dialogView.findViewById<MaterialButton>(R.id.btnNo)

        tvMessage.text = remarks

        btnYes.setOnClickListener {
            dialog.dismiss()

        }

        btnNo.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }


    @SuppressLint("SetTextI18n")
    private fun collectRFInfoResponse() {
        viewModel.ResidentialFacilityQTeam.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {
                        binding.basicInfoInclude?.layoutTCBasicInfoContent?.visible()
                        binding.basicInfoInclude?.ivToggleTCBasicInfo?.setImageResource(R.drawable.outline_arrow_upward_24)
                        isBasicInfoVisible = false

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            val request = StateRequest(
                                appVersion = BuildConfig.VERSION_NAME,
                            )
                            viewModel.getStateList(
                                request, AppUtil.getSavedTokenPreference(requireContext())
                            )


//                            x.areaType?.let { areaType ->
//                                setSpinnerValue(spinnerAreaType, areaType)
//
//                                if (areaType == "Urban") {
//                                    setSpinnerValue(spinnerSelectULB, x.ulbName)
//                                    setSpinnerValue(spinnerSelectWard, x.wardName)
//                                }
//                            }




                            binding.basicInfoInclude?.etFacilityName?.setText(x.residentialFacilityName)
                            setSpinnerValue(spinnerWardenGender, x.residentialType!!)
                            binding.basicInfoInclude?.etHouseNo?.setText(x.houseNo)
                            binding.basicInfoInclude?.etStreet?.setText(x.streetNo1)
                            binding.basicInfoInclude?.etLandmark?.setText(x.landmark)
                            setSpinnerValue(spinnerState, x.stateName!!)
                            observeState()/*setSpinnerValue(spinnerDistrict, x.districtName!!)
                            setSpinnerValue(spinnerBlock, x.blockName!!)
                            setSpinnerValue(spinnerGp, x.gpName!!)
                            setSpinnerValue(spinnerVillage, x.villageName!!)*/

                            selectedStateCode = x.stateCode.toString()
                            selectedDistrictCode = x.districtCode.toString()
                            selectedBlockCode = x.villageCode.toString()
                            selectedGpCode = x.gpCode.toString()
                            selectedVillageCode = x.villageCode.toString()


                            binding.basicInfoInclude?.etPoliceStation?.setText(x.policeStation)
                            binding.basicInfoInclude?.etPinCode?.setText(x.pincode)
                            binding.basicInfoInclude?.tvLatLang?.text = x.latitude + "," + x.longitude
                            binding.basicInfoInclude?.etMobile?.setText(x.mobile)
                            binding.basicInfoInclude?.etPhone?.setText(x.residentialFacilitiesPhNo)
                            binding.basicInfoInclude?.etEmail?.setText(x.email)
                            setSpinnerValue(spinnerTypeOfArea, x.typeOfArea!!)
                            setSpinnerValue(spinnerCatOfTCLocation, x.categoryOfTc!!)
                            setSpinnerValue(spinnerPickupAndDropFacility, x.pickUpDrop!!)
                            setSpinnerValue(spinnerWardenGender, x.wardgender!!)
                            binding.basicInfoInclude?.etDistanceFromBusStand?.setText(x.distBusStand)
                            binding.basicInfoInclude?.etDistanceFromAutoStand?.setText(x.distAutoStand)
                            binding.basicInfoInclude?.etDistanceFromRailwayStand?.setText(x.distRailStand)
                            binding.basicInfoInclude?.etDistanceFromTrainingToResidentialCentre?.setText(x.distFromTc)
                            binding.basicInfoInclude?.etWardenName?.setText(x.wardName)
                            binding.basicInfoInclude?.etWardenEmpID?.setText(x.wardEmpId)
                            binding.basicInfoInclude?.etWardenAddress?.setText(x.wardAddress)
                            binding.basicInfoInclude?.etWardenEmailId?.setText(x.wardEmail)
                            binding.basicInfoInclude?.etWardenMobile?.setText(x.wardMobile)
                            base64PVDocFile = x.policeVerfictnImage
                            base64ALDocFile = x.empLetterImage

                            setBase64ToImage(
                                binding.basicInfoInclude!!.ivPoliceVerificationDocPreview, x.policeVerfictnImage
                            )
                            setBase64ToImage(binding.basicInfoInclude!!.ivAppointmentLetterDocPreview, base64ALDocFile)

                            binding.basicInfoInclude?.ivPoliceVerificationDocPreview?.visible()
                            binding.basicInfoInclude?.ivAppointmentLetterDocPreview?.visible()

                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun collectInsfrastructureDetailsAndComplains() {
        viewModel.CompliancesRFQTReqRFQT.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                binding.layoutInfraDetailComplianceContent.visible()


                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            setSpinnerValue(spinnerOwnerBuilding, x.ownership!!)
                            setBase64ToImage(binding.ivOwnerBuildingDocPreview, x.selfDeclaration)
                            base64OwnerBuildingDocFile = x.selfDeclaration
                            binding.ivOwnerBuildingDocPreview.visible()


                            binding.etAreaOfBuilding.setText(x.buildingArea)
                            setBase64ToImage(
                                binding.ivAreaOfBuildingDocPreview, x.buildingPhotosFile
                            )
                            base64BuildingAreaDocFile = x.buildingPhotosFile
                            binding.ivAreaOfBuildingDocPreview.visible()


                            setSpinnerValue(spinnerRoofOfBuilding, x.roof!!)
                            setBase64ToImage(
                                binding.ivRoofOfBuildingPreview, x.buildingPlanFile
                            )// missing
                            base64RoofOfBuildingDocFile = x.buildingPlanFile
                            binding.ivRoofOfBuildingPreview.visible()


                            setSpinnerValue(spinnerWhetherStructurallySound, x.plastring!!)
                            setBase64ToImage(
                                binding.ivWhetherStructurallySoundPreview, x.wallPhotosFile
                            )
                            base64WhetherStructurallySoundDocFile = x.wallPhotosFile
                            binding.ivWhetherStructurallySoundPreview.visible()


                            setSpinnerValue(spinnerVisibleSignOfLeakage, x.leakage!!)
                            setBase64ToImage(binding.ivSignOfLeakagePreview, x.leakagesProofFile)
                            base64SignOfLeakageDocFile = x.leakagesProofFile
                            binding.ivSignOfLeakagePreview.visible()


                            setSpinnerValue(spinnerConformanceDDUGKY, x.conformanceDdu!!)
                            setBase64ToImage(
                                binding.ivConformanceDDUGKYPreview, x.conformanceDduProofFile
                            )
                            base64ConformanceDDUGKYDocFile = x.conformanceDduProofFile
                            binding.ivConformanceDDUGKYPreview.visible()

                            setSpinnerValue(spinnerProtectionofStairs, x.protectionStairs!!)
                            setBase64ToImage(
                                binding.ivProtectionofStairsPreview, x.protectionStairsProofFile
                            )
                            base64ProtectionofStairsDocFile = x.protectionStairsProofFile
                            binding.ivProtectionofStairsPreview.visible()


                            binding.etCirculatingArea.setText(x.circulatingArea)
                            setBase64ToImage(
                                binding.ivCirculationAreaDocPreview, x.circulatingAreaProofFile
                            )
                            base64CirculatingAreaProof = x.circulatingAreaProofFile
                            binding.ivCirculationAreaDocPreview.visible()


                            setSpinnerValue(spinnerCorridor, x.corridor!!)
                            setBase64ToImage(binding.ivCorridorPreview, x.corridorProofFile)
                            base64CorridorDocFile = x.corridorProofFile
                            binding.ivCorridorPreview.visible()


                            setSpinnerValue(spinnerSecuringWires, x.securingWiresDone!!)
                            setBase64ToImage(
                                binding.ivSecuringWiresPreview, x.securingWiresDoneProofFile
                            )
                            base64SecuringWiresDocFile = x.securingWiresDoneProofFile
                            binding.ivSecuringWiresPreview.visible()


                            setSpinnerValue(spinnerSwitchBoards, x.switchBoardsPanelBoards!!)
                            setBase64ToImage(
                                binding.ivSwitchBoardsPreview, x.switchBoardsPanelBoardsProofFile
                            )
                            base64SwitchBoardsDocFile = x.switchBoardsPanelBoardsProofFile
                            binding.ivSwitchBoardsPreview.visible()


                            setSpinnerValue(spinnerHostelNameBoard, x.hostelNameBoard!!)
                            setBase64ToImage(
                                binding.ivHostelNameBoardPreview, x.hostelNameBoardProofFile
                            )
                            base64HostelNameBoardDocFile = x.hostelNameBoardProofFile
                            binding.ivHostelNameBoardPreview.visible()

                            setSpinnerValue(spinnerEntitlementBoard, x.studentEntitlementBoard!!)
                            setBase64ToImage(
                                binding.ivEntitlementBoardPreview,
                                x.studentEntitlementBoardProofFile
                            )
                            base64EntitlementBoardDocFile = x.studentEntitlementBoardProofFile
                            binding.ivEntitlementBoardPreview.visible()


                            setSpinnerValue(spinnerContactDetail, x.contactDetailImportantPeople!!)
                            setBase64ToImage(
                                binding.ivContactDetailPreview,
                                x.contactDetailImportantPeopleproofFile
                            )
                            base64ContactDetailDocFile = x.contactDetailImportantPeopleproofFile
                            binding.ivContactDetailPreview.visible()


                            setSpinnerValue(spinnerBasicInfoBoard, x.basicInformationBoard!!)
                            setBase64ToImage(
                                binding.ivBasicInfoBoardPreview, x.basicInformationBoardproofFile
                            )
                            base64BasicInfoBoardDocFile = x.basicInformationBoardproofFile
                            binding.ivBasicInfoBoardPreview.visible()


                            setSpinnerValue(
                                spinnerFoodSpecificationBoard, x.foodSpecificationBoard!!
                            )
                            setBase64ToImage(
                                binding.ivFoodSpecificationBoardPreview,
                                x.foodSpecificationBoardFile
                            )
                            base64FoodSpecificationBoardDocFile = x.foodSpecificationBoardFile
                            binding.ivFoodSpecificationBoardPreview.visible()


                            binding.etAreaForOutDoorGames.setText(x.openSpaceArea)


                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun CollectGetToiletWashbasinDetails() {
        viewModel.getToiletWashbasinDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {

                        for (x in it.wrappedList) {

                            binding.urinalWashbasin.layoutUrinalWashbasinContent.visible()

                            binding.urinalWashbasin.etFemaleWashbasins.setText(x.washbasin)
                            binding.urinalWashbasin.etFemaleUrinal.setText(x.urinal)
                            binding.urinalWashbasin.etFemaleWashbasins.setText(x.washbasin)
                            setSpinnerValue(
                                binding.urinalWashbasin.spinnerOverheadTanks,
                                x.overheadTank
                            )


                            setBase64ToImage(
                                binding.urinalWashbasin.ivWashBasinPreview, x.washbasinFile
                            )

                            base64WashBasinDocFile = x.washbasinFile

                            binding.urinalWashbasin.ivWashBasinPreview.visible()

                            setBase64ToImage(
                                binding.urinalWashbasin.ivUrinalPreview, x.urinalFile
                            )
                            base64UrinalsDocFile = x.urinalFile


                            binding.urinalWashbasin.ivUrinalPreview.visible()



                            setBase64ToImage(
                                binding.urinalWashbasin.ivOverHeadPreview, x.overheadTankFile
                            )
                            base64OverHeadTankDocFile = x.overheadTankFile


                            binding.urinalWashbasin.ivOverHeadPreview.visible()
                        }


                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        "No data available.",
                        Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(),
                        "Please upgrade your app.",
                        Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()

                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->

        }
    }


    private fun NonAreaInformation() {
        viewModel.NonAreaInformationRoom.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                binding.layoutNonLivingAreaContent.visible()
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {


                            setSpinnerValue(spinnerFoodPreparedTrainingCenter, x.preparedFood)
                            setBase64ToImage(
                                binding.ivFoodPreparedTrainingPreview, x.preprationFoodPdf
                            )
                            binding.ivFoodPreparedTrainingPreview.visible()
                            base64FoodPreparedTrainingDocFile = x.preprationFoodPdf

                            binding.etKitchenLength.setText(x.kitchenLength)
                            binding.etKitchenWidth.setText(x.kitchenWidth)
                            binding.etKitchenArea.text = x.kitchenArea

                            setSpinnerValue(spinnerDiningRecreationAreaSeparate, x.separateAreas)

                            binding.etStoolsChairsBenches.setText(x.noOfSeats)
                            binding.etWashArea.setText(x.washArea)

                            setSpinnerValue(spinnerTvAvailable, x.tvAvailable)
                            binding.etDiningLength.setText(x.diningLength)
                            binding.etDiningWidth.setText(x.diningWidth)
                            binding.etDiningArea.text = x.diningArea


                            binding.etDiningAndRecreactionLength.setText(x.diningRecreationLengh)
                            binding.etDiningAndRecreactionWidth.setText(x.diningRecreationWidth)
                            binding.etDiningAndRecreactionArea.text = x.diningRecreationArea

                            setBase64ToImage(
                                binding.ivDiningAndRecreactionAreaPreview,
                                x.diningRecreationAreaFile
                            )

                            base64RecreationDiningAreaDocFile = x.diningRecreationAreaFile
                            binding.ivDiningAndRecreactionAreaPreview.visible()





                            binding.etRecreationLength.setText(x.recreationLength)
                            binding.etRecreationWidth.setText(x.recreationWidth)
                            binding.etRecreationArea.text = x.recreationArea
                            setSpinnerValue(
                                spinnerIsReceptionAreaAva, x.receptionArea
                            ) // selection of Yes No Showing area value
                            setBase64ToImage(binding.ivReceptionAreaPreview, x.receptionAreaPdf)
                            binding.etWashArea.setText(x.washArea) //  getting blank
                            base64ReceptionAreaDocFile = x.receptionAreaPdf
                            binding.ivReceptionAreaPreview.visible()


                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(), "No data available.", Toast.LENGTH_SHORT
                    ).show()

                    301 -> Toast.makeText(
                        requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                    ).show()

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun ResidentialFacilitiesAvailable() {
        viewModel.RFResidentialFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()

                when (it.responseCode) {
                    200 -> {
                        binding.layoutRfAvailableContent.visible()

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            setSpinnerValue(spinnerWhetherHostelsSeparated, x.hostelsSeparated)
                            setBase64ToImage(
                                binding.ivWhetherHostelsSeparatedPreview, x.hostelsSeparatedPdf
                            )
                            binding.ivWhetherHostelsSeparatedPreview.visible()
                            base64WhetherHostelsSeparatedDocFile = x.hostelsSeparatedPdf


                            setSpinnerValue(spinnerWardenWhereMalesStay, x.wardenCaretakerMale)
                            setBase64ToImage(
                                binding.ivWardenWhereMalesStayPreview, x.wardenCaretakerMalePdf
                            )
                            binding.ivWardenWhereMalesStayPreview.visible()
                            base64WardenWhereMalesStayDocFile = x.wardenCaretakerMalePdf


                            setSpinnerValue(spinnerWardenWhereLadyStay, x.wardenCaretakerFemale)
                            setBase64ToImage(
                                binding.ivWardenWhereLadyStayPreview, x.wardenCaretakerFemalePdf
                            )
                            binding.ivWardenWhereLadyStayPreview.visible()
                            base64WardenWhereLadyStayDocFile = x.wardenCaretakerFemalePdf


                            setSpinnerValue(spinnerSecurityGaurdsAvailable, x.securityGuards)
                            setBase64ToImage(binding.ivSecurityGaurdsPreview, x.securityGuardsPdf)
                            binding.ivSecurityGaurdsPreview.visible()
                            base64SecurityGaurdsDocFile = x.securityGuardsPdf


                            setSpinnerValue(spinnerWhetherFemaleDoctorAvailable, x.femaleDoctor)
                            setBase64ToImage(
                                binding.ivWhetherFemaleDoctorPreview, x.femaleDoctorPdf
                            )
                            binding.ivWhetherFemaleDoctorPreview.visible()
                            base64WhetherFemaleDoctorDocFile = x.femaleDoctorPdf

                            setSpinnerValue(spinnerWhetherMaleDoctorAvailable, x.maleDoctor)
                            setBase64ToImage(binding.ivMaleDoctorPreview, x.maleDoctorPdf)
                            binding.ivMaleDoctorPreview.visible()
                            base64WhetherMaleDoctorDocFile = x.maleDoctorPdf
                        }


                    }

                    202 -> {


                        Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                    }


                    301 -> {

                        Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> AppUtil.showSessionExpiredDialog(
                        findNavController(), requireContext()
                    )
                }
            }

            result.onFailure {

                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun ResidentialSupportFacilitiesAvailable() {
        viewModel.RFSupportFacilitiesAvailable.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()

                when (it.responseCode) {
                    200 -> {
                        binding.layoutSfAvailableContent.visible()

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            setSpinnerValue(spinnerSafeDrinikingAvailable, x.safeDrinking)
                            setBase64ToImage(binding.ivSafeDrinkingPreview, x.safeDrinkingPdf)
                            binding.ivSafeDrinkingPreview.visible()
                            base64SafeDrinkingDocFile = x.safeDrinkingPdf


                            setSpinnerValue(spinnerFirstAidKitAvailable, x.firstAidKit)
                            setBase64ToImage(binding.ivFirstAidKitPreview, x.firstAidKitPdf)
                            binding.ivFirstAidKitPreview.visible()
                            base64FirstAidKitDocFile = x.firstAidKitPdf


                            setSpinnerValue(spinnerFireFightingEquipmentAvailable, x.fireFighting)
                            setBase64ToImage(
                                binding.ivFireFightingEquipmentPreview, x.fireFightingPdf
                            )
                            binding.ivFireFightingEquipmentPreview.visible()
                            base64FireFightingEquipmentDocFile = x.fireFightingPdf


                            setSpinnerValue(spinnerBiometricDeviceAvailable, x.biometricDevice)
                            setBase64ToImage(binding.ivBiometricDevicePreview, x.biometricDevicePdf)
                            binding.ivBiometricDevicePreview.visible()
                            base64BiometricDeviceDocFile = x.biometricDevicePdf


                            setSpinnerValue(spinnerElectricalPowerBackupAvailable, x.powerBackup)
                            setBase64ToImage(binding.ivElectricalPowerPreview, x.powerBackupPdf)
                            binding.ivElectricalPowerPreview.visible()
                            base64ElectricalPowerDocFile = x.powerBackupPdf

                            setSpinnerValue(spinnerGrievanceRegisterAvailable, x.grievanceRegister)
                            setBase64ToImage(
                                binding.ivGrievanceRegisterPreview, x.grievanceRegisterPdf
                            )
                            binding.ivGrievanceRegisterPreview.visible()
                            base64GrievanceRegisterDocFile = x.grievanceRegisterPdf
                        }


                    }

                    202 -> {


                        Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()

                    }


                    301 -> {

                        Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> AppUtil.showSessionExpiredDialog(
                        findNavController(), requireContext()
                    )
                }
            }

            result.onFailure {

                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun observeFinalSubmissionResponse() {
        viewModel.insertRFFinalSubmission.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgressBar()
                when (it.responseCode) {
                    200 -> {

                        Toast.makeText(
                            requireContext(),
                            "Data sent to the QTeam for verification",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()

                    }

                    202 -> {


                        Toast.makeText(
                            requireContext(), it.responseDesc, Toast.LENGTH_SHORT
                        ).show()

                    }


                    301 -> {

                        Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()
                    }


                    401 -> AppUtil.showSessionExpiredDialog(
                        findNavController(), requireContext()
                    )
                }


            }

            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setSpinnerValue(spinner: Spinner?, value: String?) {
        if (spinner == null || value.isNullOrEmpty()) return

        val adapter = spinner.adapter ?: return

        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString().equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                return
            }
        }
    }

    fun setBase64ToImage(imageView: ImageView, base64String: String?) {
        if (base64String.isNullOrEmpty() || base64String == "NA") {
            imageView.setImageResource(R.drawable.no_image)
            return
        }

        try {
            val pureBase64 = base64String.substringAfter(",") // Removes header if exists
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)

            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                imageView.setImageResource(R.drawable.no_image)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            imageView.setImageResource(R.drawable.no_image)
        }
    }

    private fun clearAllFields() {
        // EditTexts
        binding.etHeightOfCeiling.text?.clear()
        binding.etRoomLength.text?.clear()
        binding.etRoomWidth.text?.clear()
        binding.etRoomArea.text = ""
        binding.etRoomWindowArea.text?.clear()
        binding.etCot.text?.clear()
        binding.etMattress.text?.clear()
        binding.etBedSheet.text?.clear()
        binding.etCupboard.text?.clear()
        binding.etLivingAreaLights.text?.clear()
        binding.etLivingAreaFans.text?.clear()

        binding.spinnerTypeLivingRoof.setSelection(0)
        binding.spinnerCeiling.setSelection(0)
        binding.spinnerAirConditioning.setSelection(0)
        binding.spinnerLivingAreaInfoBoard.setSelection(0)

        base64TypeLivingRoofDocFile = ""
        base64CeilingDocFile = ""
        base64AirConditioningDocFile = ""
        base64CotDocFile = ""
        base64MattressDocFile = ""
        base64BedSheetDocFile = ""
        base64CupBoardDocFile = ""
        base64LivingAreaInfoBoardDocFile = ""
        base64LightsDocFile = ""
        base64FansDocFile = ""
        base64AirHieghtOfCelingDocFile = ""
        base64WindowAreaDocFile = ""

        // Reset custom variables
        selectedRoomPermitted = 0


        binding.ivTypeLivingRoofPreview.gone()
        binding.ivCeilingPreview.gone()
        binding.ivLivingAreaHieghtOfCeilingPreview.gone()
        binding.ivLivingAreaWindowAreaPreview.gone()
        binding.ivAirConditioningPreview.gone()
        binding.ivCotPreview.gone()
        binding.ivMattressPreview.gone()
        binding.ivBedSheetPreview.gone()
        binding.ivCupboardPreview.gone()
        binding.ivLivingAreaInfoBoardPreview.gone()
        binding.ivLivingAreaLightsPreview.gone()
        binding.ivLivingAreaFansPreview.gone()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun IndoorGameRecyclerView() {
        viewModel.RfIndoorGameDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                hideProgressBar()
                when (response.responseCode) {
                    200 -> {
                        binding.layoutIndoorGameDetailContent.visible()

                        val wrappedList =
                            response.wrappedList // list of IndoorRFGameResponseDetails

                        // Clear the old list before adding new data
                        indoorGamesList.clear()

                        var counter = 1
                        wrappedList.forEach { item ->
                            val newGame = IndoorGame(
                                gameNumber = counter++,
                                gameName = item.indoorGameName,
                                gamePhoto = item.indoorGamePdf
                            )
                            indoorGamesList.add(newGame)
                        }

                        // Notify adapter
                        adapterGame.notifyDataSetChanged()
                    }

                    202 -> {
                        Toast.makeText(
                            requireContext(), "No data available.", Toast.LENGTH_SHORT
                        ).show()
                    }

                    301 -> {
                        Toast.makeText(
                            requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT
                        ).show()
                    }

                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }

            result.onFailure {
                hideProgressBar()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun clearToiletForm() {
        // Clear EditTexts
        binding.etLightsInToilet.text?.clear()

        // Reset Spinners to first item (index 0)
        binding.spinnerToiletType.setSelection(0)
        binding.spinnerToiletFlooringType.setSelection(0)
        binding.spinnerConnectionToRunningWater.setSelection(0)

        // Reset all Base64 file variables
        base64LightsInToiletDocFile = ""
        base64ToiletFlooringDocFile = ""
        base64RunningWaterDocFile = ""


        // Hide any image previews if used
        binding.ivLightsInToiletPreview.visibility = View.GONE
        binding.ivToiletFlooringPreview.visibility = View.GONE
        binding.ivRunningWaterPreview.visibility = View.GONE


    }
}