package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.BlockAdapter
import com.deendayalproject.adapter.DistrictAdapter
import com.deendayalproject.adapter.PanchayatAdapter
import com.deendayalproject.adapter.StateAdapter
import com.deendayalproject.databinding.FragmentQTeamFormBinding
import com.deendayalproject.databinding.FragmentResidentialBinding
import com.deendayalproject.model.request.BlockRequest
import com.deendayalproject.model.request.DistrictRequest
import com.deendayalproject.model.request.GpRequest
import com.deendayalproject.model.request.StateRequest
import com.deendayalproject.model.request.TrainingCenterRequest
import com.deendayalproject.model.response.BlockModel
import com.deendayalproject.model.response.DistrictModel
import com.deendayalproject.model.response.GpModel
import com.deendayalproject.model.response.StateModel
import com.deendayalproject.util.AppUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String



class ResidentialFacilityFragment : Fragment() {

    private var _binding: FragmentResidentialBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private var currentPhotoTarget: String = ""
    private var latLangValue: String = ""
    private var base64PVDocFile: String? = null
    private var base64ALDocFile: String? = null
    private var base64OwnerBuildingDocFile: String? = null
    private var base64RoofOfBuildingDocFile: String? = null
    private var base64WhetherStructurallySoundDocFile: String? = null
    private var base64SignOfLeakageDocFile: String? = null
    private var base64ConformanceDDUGKYDocFile: String? = null
    private var base64ProtectionofStairsDocFile: String? = null
    private var base64CorridorDocFile: String? = null
    private var base64SecuringWiresDocFile: String? = null
    private var base64SwitchBoardsDocFile: String? = null
    private var base64HostelNameBoardDocFile: String? = null
    private var base64EntitlementBoardDocFile: String? = null
    private var base64ContactDetailDocFile: String? = null
    private var base64BasicInfoBoardDocFile: String? = null
    private var base64FoodSpecificationBoardDocFile: String? = null
    private var base64TypeLivingRoofDocFile: String? = null
    private var base64CeilingDocFile: String? = null
    private var base64AirConditioningDocFile: String? = null
    private var base64CotDocFile: String? = null
    private var base64MattressDocFile: String? = null
    private var base64BedSheetDocFile: String? = null
    private var base64CupBoardDocFile: String? = null
    private var base64LightsDocFile: String? = null
    private var base64FansDocFile: String? = null
    private var base64LivingAreaInfoBoardDocFile: String? = null
    private var base64LightsInToiletDocFile: String? = null
    private var base64ToiletFlooringDocFile: String? = null
    private var base64FoodPreparedTrainingDocFile: String? = null
    private var base64ReceptionAreaDocFile: String? = null
    private var base64WhetherHostelsSeparatedDocFile: String? = null
    private var base64WardenWhereMalesStayDocFile: String? = null
    private var base64WardenWhereLadyStayDocFile: String? = null
    private var base64SecurityGaurdsDocFile: String? = null
    private var base64WhetherFemaleDoctorDocFile: String? = null
    private var base64SafeDrinkingDocFile: String? = null
    private var base64FirstAidKitDocFile: String? = null
    private var base64FireFightingEquipmentDocFile: String? = null
    private var base64BiometricDeviceDocFile: String? = null
    private var base64ElectricalPowerDocFile: String? = null
    private var base64GrievanceRegisterDocFile: String? = null

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
    private lateinit var ivSafeDrinkingPreview: ImageView
    private lateinit var ivFirstAidKitPreview: ImageView
    private lateinit var ivFireFightingEquipmentPreview: ImageView
    private lateinit var ivBiometricDevicePreview: ImageView
    private lateinit var ivElectricalPowerPreview: ImageView
    private lateinit var ivGrievanceRegisterPreview: ImageView

    /////////////////////////Basic Info Section////////////
    private lateinit var etFacilityName: TextInputEditText
    private lateinit var etFacilityType: TextInputEditText
    private lateinit var etHouseNo: TextInputEditText
    private lateinit var etStreet: TextInputEditText
    private lateinit var etLandmark: TextInputEditText
    private lateinit var etVillage: TextInputEditText
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
    private lateinit var etStudentsPermitted: TextInputEditText
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
    private lateinit var etCandidatesPermissible: TextInputEditText
    /////////////////////////Non-Living Areas Section////////////
    private lateinit var spinnerFoodPreparedTrainingCenter: Spinner
    private lateinit var spinnerDiningRecreationAreaSeparate: Spinner
    private lateinit var spinnerTvAvailable: Spinner
    private lateinit var spinnerReceptionAreaAvailable: Spinner
    private lateinit var etKitchenLength: TextInputEditText
    private lateinit var etKitchenWidth: TextInputEditText
    private lateinit var etKitchenArea: TextInputEditText
    private lateinit var etStoolsChairsBenches: TextInputEditText
    private lateinit var etWashArea: TextInputEditText
    private lateinit var etDiningLength: TextInputEditText
    private lateinit var etDiningWidth: TextInputEditText
    private lateinit var etDiningArea: TextInputEditText
    private lateinit var etRecreationLength: TextInputEditText
    private lateinit var etRecreationWidth: TextInputEditText
    private lateinit var etRecreationArea: TextInputEditText

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


    var selectedStateCode ="0";
    var selectedDistrictCode ="0";
    var selectedBlockCode ="0";
    var selectedGpCode ="0";


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
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
                            base64OwnerBuildingDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "RoofOfBuilding" -> {
                            ivRoofOfBuildingPreview.setImageURI(photoUri)
                            ivRoofOfBuildingPreview.visibility = View.VISIBLE
                            base64RoofOfBuildingDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "WhetherStructurallySound" -> {
                            ivWhetherStructurallySoundPreview.setImageURI(photoUri)
                            ivWhetherStructurallySoundPreview.visibility = View.VISIBLE
                            base64WhetherStructurallySoundDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "SignOfLeakage" -> {
                            ivSignOfLeakagePreview.setImageURI(photoUri)
                            ivSignOfLeakagePreview.visibility = View.VISIBLE
                            base64SignOfLeakageDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ConformanceDDUGKY" -> {
                            ivConformanceDDUGKYPreview.setImageURI(photoUri)
                            ivConformanceDDUGKYPreview.visibility = View.VISIBLE
                            base64ConformanceDDUGKYDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ProtectionofStairs" -> {
                            ivProtectionofStairsPreview.setImageURI(photoUri)
                            ivProtectionofStairsPreview.visibility = View.VISIBLE
                            base64ProtectionofStairsDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "Corridor" -> {
                            ivCorridorPreview.setImageURI(photoUri)
                            ivCorridorPreview.visibility = View.VISIBLE
                            base64CorridorDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "SecuringWires" -> {
                            ivSecuringWiresPreview.setImageURI(photoUri)
                            ivSecuringWiresPreview.visibility = View.VISIBLE
                            base64SecuringWiresDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "SwitchBoards" -> {
                            ivSwitchBoardsPreview.setImageURI(photoUri)
                            ivSwitchBoardsPreview.visibility = View.VISIBLE
                            base64SwitchBoardsDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "HostelNameBoard" -> {
                            ivHostelNameBoardPreview.setImageURI(photoUri)
                            ivHostelNameBoardPreview.visibility = View.VISIBLE
                            base64HostelNameBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "EntitlementBoard" -> {
                            ivEntitlementBoardPreview.setImageURI(photoUri)
                            ivEntitlementBoardPreview.visibility = View.VISIBLE
                            base64EntitlementBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ContactDetail" -> {
                            ivContactDetailPreview.setImageURI(photoUri)
                            ivContactDetailPreview.visibility = View.VISIBLE
                            base64ContactDetailDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "BasicInfoBoard" -> {
                            ivBasicInfoBoardPreview.setImageURI(photoUri)
                            ivBasicInfoBoardPreview.visibility = View.VISIBLE
                            base64BasicInfoBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "FoodSpecificationBoard" -> {
                            ivFoodSpecificationBoardPreview.setImageURI(photoUri)
                            ivFoodSpecificationBoardPreview.visibility = View.VISIBLE
                            base64FoodSpecificationBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "TypeLivingRoof" -> {
                            ivTypeLivingRoofPreview.setImageURI(photoUri)
                            ivTypeLivingRoofPreview.visibility = View.VISIBLE
                            base64TypeLivingRoofDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "Ceiling" -> {
                            ivCeilingPreview.setImageURI(photoUri)
                            ivCeilingPreview.visibility = View.VISIBLE
                            base64CeilingDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "AirConditioning" -> {
                            ivAirConditioningPreview.setImageURI(photoUri)
                            ivAirConditioningPreview.visibility = View.VISIBLE
                            base64AirConditioningDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                            "COT" -> {
                        binding.ivCotPreview.setImageURI(photoUri)
                        binding.ivCotPreview.visibility = View.VISIBLE
                        base64CotDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }


                        "Mattress" -> {
                            binding.ivMattressPreview.setImageURI(photoUri)
                            binding.ivMattressPreview.visibility = View.VISIBLE
                            base64MattressDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "BedSheet" -> {
                            binding.ivBedSheetPreview.setImageURI(photoUri)
                            binding.ivBedSheetPreview.visibility = View.VISIBLE
                            base64BedSheetDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "Cupboard" -> {
                            binding.ivCupboardPreview.setImageURI(photoUri)
                            binding.ivCupboardPreview.visibility = View.VISIBLE
                            base64CupBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "LivingAreaLights" -> {
                            binding.ivLivingAreaLightsPreview.setImageURI(photoUri)
                            binding.ivLivingAreaLightsPreview.visibility = View.VISIBLE
                            base64LightsDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "LivingAreaFans" -> {
                            binding.ivLivingAreaFansPreview.setImageURI(photoUri)
                            binding.ivLivingAreaFansPreview.visibility = View.VISIBLE
                            base64FansDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "LivingAreaInfoBoard" -> {
                            ivLivingAreaInfoBoardPreview.setImageURI(photoUri)
                            ivLivingAreaInfoBoardPreview.visibility = View.VISIBLE
                            base64LivingAreaInfoBoardDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "LightsInToilet" -> {
                            ivLightsInToiletPreview.setImageURI(photoUri)
                            ivLightsInToiletPreview.visibility = View.VISIBLE
                            base64LightsInToiletDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ToiletFlooring" -> {
                            ivToiletFlooringPreview.setImageURI(photoUri)
                            ivToiletFlooringPreview.visibility = View.VISIBLE
                            base64ToiletFlooringDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "FoodPreparedTraining" -> {
                            ivFoodPreparedTrainingPreview.setImageURI(photoUri)
                            ivFoodPreparedTrainingPreview.visibility = View.VISIBLE
                            base64FoodPreparedTrainingDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ReceptionArea" -> {
                            ivReceptionAreaPreview.setImageURI(photoUri)
                            ivReceptionAreaPreview.visibility = View.VISIBLE
                            base64ReceptionAreaDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "WhetherHostelsSeparated" -> {
                            ivWhetherHostelsSeparatedPreview.setImageURI(photoUri)
                            ivWhetherHostelsSeparatedPreview.visibility = View.VISIBLE
                            base64WhetherHostelsSeparatedDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "WardenWhereMalesStay" -> {
                            ivWardenWhereMalesStayPreview.setImageURI(photoUri)
                            ivWardenWhereMalesStayPreview.visibility = View.VISIBLE
                            base64WardenWhereMalesStayDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "WardenWhereLadyStay" -> {
                            ivWardenWhereLadyStayPreview.setImageURI(photoUri)
                            ivWardenWhereLadyStayPreview.visibility = View.VISIBLE
                            base64WardenWhereLadyStayDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "SecurityGaurds" -> {
                            ivSecurityGaurdsPreview.setImageURI(photoUri)
                            ivSecurityGaurdsPreview.visibility = View.VISIBLE
                            base64SecurityGaurdsDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "WhetherFemaleDoctor" -> {
                            ivWhetherFemaleDoctorPreview.setImageURI(photoUri)
                            ivWhetherFemaleDoctorPreview.visibility = View.VISIBLE
                            base64WhetherFemaleDoctorDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "SafeDrinking" -> {
                            ivSafeDrinkingPreview.setImageURI(photoUri)
                            ivSafeDrinkingPreview.visibility = View.VISIBLE
                            base64SafeDrinkingDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "FirstAidKit" -> {
                            ivFirstAidKitPreview.setImageURI(photoUri)
                            ivFirstAidKitPreview.visibility = View.VISIBLE
                            base64FirstAidKitDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "FireFightingEquipment" -> {
                            ivFireFightingEquipmentPreview.setImageURI(photoUri)
                            ivFireFightingEquipmentPreview.visibility = View.VISIBLE
                            base64FireFightingEquipmentDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "BiometricDevice" -> {
                            ivBiometricDevicePreview.setImageURI(photoUri)
                            ivBiometricDevicePreview.visibility = View.VISIBLE
                            base64BiometricDeviceDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "ElectricalPower" -> {
                            ivElectricalPowerPreview.setImageURI(photoUri)
                            ivElectricalPowerPreview.visibility = View.VISIBLE
                            base64ElectricalPowerDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "GrievanceRegister" -> {
                            ivGrievanceRegisterPreview.setImageURI(photoUri)
                            ivGrievanceRegisterPreview.visibility = View.VISIBLE
                            base64GrievanceRegisterDocFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Photo capture failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) launchCamera()
                else Toast.makeText(
                    requireContext(),
                    "Camera permission is required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentResidentialBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExpandableSections(view)
        findById(view)
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        observeViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }




        val request = StateRequest(
            appVersion = BuildConfig.VERSION_NAME,
        )
        viewModel.getStateList(request, AppUtil.getSavedTokenPreference(requireContext()))
        spinnerState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position == 0) {
                    selectedStateCode = "0"
                    selectedDistrictCode = "0"
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    spinnerDistrict.setSelection(0)
                    spinnerBlock.setSelection(0)
                    spinnerGp.setSelection(0)
                } else {
                    val stateModel = (spinnerState.getAdapter() as StateAdapter).getItem(position)
                    selectedStateCode = stateModel!!.stateCode
                    val request = DistrictRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        stateCode = selectedStateCode,
                    )
                    viewModel.getDistrictList(request, AppUtil.getSavedTokenPreference(requireContext()))
                    // Toast.makeText(applicationContext,""+selectedStateCode,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        spinnerDistrict.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position == 0) {
                    selectedDistrictCode = "0"
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    spinnerBlock.setSelection(0)
                    spinnerGp.setSelection(0)
                } else {
                    val districtModel = (spinnerDistrict.getAdapter() as DistrictAdapter).getItem(position)
                    selectedDistrictCode = districtModel!!.districtCode

                    val request = BlockRequest(
                        appVersion = BuildConfig.VERSION_NAME,
                        districtCode = selectedDistrictCode,
                    )
                    viewModel.getBlockList(request, AppUtil.getSavedTokenPreference(requireContext()))
                    // Toast.makeText(applicationContext,""+selectedStateCode,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })
        spinnerBlock.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position == 0) {
                    selectedBlockCode = "0"
                    selectedGpCode = "0"
                    spinnerGp.setSelection(0)
                } else {
                    val blockModel = (spinnerBlock.getAdapter() as BlockAdapter).getItem(position)
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
        })
        spinnerGp.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long) {
                if (position == 0) {
                    selectedGpCode = "0"
                } else {
                    val gpModel = (spinnerGp.getAdapter() as PanchayatAdapter).getItem(position)
                    selectedGpCode = gpModel!!.gpCode


                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        etRoomWidth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateAndShowArea()  // run only when Width changes
            }
        })


        // Setup Yes/No adapter
        val yesNoAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Yes", "No")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val spinners = listOf(
            R.id.spinnerPickupAndDropFacility,R.id.spinnerWhetherStructurallySound,R.id.spinnerVisibleSignOfLeakage,R.id.spinnerConformanceDDUGKY,
            R.id.spinnerProtectionofStairs,R.id.spinnerCorridor,R.id.spinnerSecuringWires,R.id.spinnerSwitchBoards,R.id.spinnerHostelNameBoard,
            R.id.spinnerEntitlementBoard,R.id.spinnerContactDetail,R.id.spinnerBasicInfoBoard,R.id.spinnerFoodSpecificationBoard,
            R.id.spinnerAirConditioning,R.id.spinnerLivingAreaInfoBoard,R.id.spinnerConnectionToRunningWater,R.id.spinnerOverheadTanks,
            R.id.spinnerFoodPreparedTrainingCenter,R.id.spinnerDiningRecreationAreaSeparate,R.id.spinnerTvAvailable,
            R.id.spinnerReceptionAreaAvailable,R.id. spinnerWhetherHostelsSeparated,R.id. spinnerWardenWhereMalesStay,
            R.id. spinnerWardenWhereLadyStay,R.id. spinnerSecurityGaurdsAvailable,R.id. spinnerWhetherFemaleDoctorAvailable,
            R.id. spinnerWhetherMaleDoctorAvailable,R.id. spinnerSafeDrinikingAvailable,R.id. spinnerFirstAidKitAvailable,
            R.id. spinnerFireFightingEquipmentAvailable,R.id. spinnerBiometricDeviceAvailable,R.id.spinnerElectricalPowerBackupAvailable,
            R.id.spinnerGrievanceRegisterAvailable, R.id.spinnerCeiling
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
        spinnerTypeOfArea.adapter=areaTypeAdapter


        val categoryOfTcAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "X", "Y", "Z", "Others")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCatOfTCLocation.adapter=categoryOfTcAdapter


        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Male", "Female")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerWardenGender.adapter=genderAdapter

        val ownershipOfBuildingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Own (O)", "Rent (R)", "Govt. (G)")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerOwnerBuilding.adapter=ownershipOfBuildingAdapter

     val roofOfBuildingAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "rcc", "non-rcc", "rcc& non-rcc")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerRoofOfBuilding.adapter=roofOfBuildingAdapter

  val typeOfRoofAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "rcc", "non-rcc")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerTypeLivingRoof.adapter=typeOfRoofAdapter
    val toiletTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--",  "toilet", "bathroom", "toilet cum bathroom" )
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerToiletType.adapter=toiletTypeAdapter
    val flooringTypeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "cement", "tiles" )
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerToiletFlooringType.adapter=flooringTypeAdapter

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
           view.findViewById<Button>(R.id.btnUploadFoodPreparedTraining).setOnClickListener {
            currentPhotoTarget = "FoodPreparedTraining"
            checkAndLaunchCamera()
        }
           view.findViewById<Button>(R.id.btnUploadReceptionArea).setOnClickListener {
            currentPhotoTarget = "ReceptionArea"
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
        view.findViewById<Button>(R.id.btnUploadWhetherFemaleDoctor).setOnClickListener {
            currentPhotoTarget = "WhetherFemaleDoctor"
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
            if (validateBasicInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Basic Information  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitInfraInfo).setOnClickListener {
            if (validateInfraInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Infrastructure Details and Compliances  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitLivingAreaInfo).setOnClickListener {
            if (validateLivingAreaInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Living Area Information  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitToiletInfo).setOnClickListener {
            if (validateToiletInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Toilets  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitNonLivingAreaInfo).setOnClickListener {
            if (validateNonLivingAreaInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Non-Living Areas  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitInDoorGameInfo).setOnClickListener {
            if (validateIndoorGameInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Indoor Game Details  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnResidentialFacilitiesInfo).setOnClickListener {
            if (validateResidentialFacilitiesInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Residential Facilities Available  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSupportFacilityInfo).setOnClickListener {
            if (validateSupportFacilitiesInfoForm(view)) submitBasicInfoForm(view)
            else Toast.makeText(
                requireContext(),
                "Complete all Support Facilities Available  fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }



        // Observers
       /* viewModel.insertCCTVdata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "CCTV data submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "CCTV submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.insertIpenabledata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Electrical data submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Electrical submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.insertGeneralDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "General details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "General details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertTCInfoDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Training details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Training details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertSignagesInfoBoardsDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Signages&InfoBoards details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Signages&InfoBoards details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        viewModel.insertSupportInfraDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Support Infrastructure details submitted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Support Infrastructure details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }*/


        /*  val spinnersWithButtons = listOf(
                  *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerMonitorAccessible),
                view.findViewById<Button>(R.id.btnUploadMonitorPhoto)
            ),*//*
           *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerConformance),
                view.findViewById<Button>(R.id.btnUploadConformancePhoto)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerStorage),
                view.findViewById<Button>(R.id.btnUploadStoragePhoto)
            ),*//*
           *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerDVRStaticIP),
                view.findViewById<Button>(R.id.btnUploadDVRPhoto)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerSwitchBoards),
                view.findViewById<Button>(R.id.btnUploadSwitchBoards)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerSecuringWires),
                view.findViewById<Button>(R.id.btnUploadSecuringWires)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerLeakageCheck),
                view.findViewById<Button>(R.id.btnUploadLeakageProof)
            ),*//*
           *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerProtectionStairs),
                view.findViewById<Button>(R.id.btnUploadProtectionStairs)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerTrainingCentreNameBoard),
                view.findViewById<Button>(R.id.btnUploadTrainingCentreNameBoard)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerActivitySummaryBoard),
                view.findViewById<Button>(R.id.btnUploadActivitySummaryBoard)
            ),*//*
           *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerEntitlementBoard),
                view.findViewById<Button>(R.id.btnUploadEntitlementBoard)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerImportantContacts),
                view.findViewById<Button>(R.id.btnUploadImportantContacts)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerBasicInfoBoard),
                view.findViewById<Button>(R.id.btnUploadBasicInfoBoard)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerCodeOfConductBoard),
                view.findViewById<Button>(R.id.btnUploadCodeOfConductBoard)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerAttendanceSummaryBoard),
                view.findViewById<Button>(R.id.btnUploadAttendanceSummaryBoard)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerFirstAidKit),
                view.findViewById<Button>(R.id.btnUploadFirstAidKit)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerSafeDrinkingWater),
                view.findViewById<Button>(R.id.btnUploadSafeDrinkingWater)
            ),*//*
           *//* Pair(
                view.findViewById<Spinner>(R.id.spinnerPowerBackup),
                view.findViewById<Button>(R.id.btnUploadPowerBackup)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerCCTV),
                view.findViewById<Button>(R.id.btnUploadPowerBackup)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerDocumentStorage),
                view.findViewById<Button>(R.id.btnUploadDocumentStorage)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerGrievanceRegister),
                view.findViewById<Button>(R.id.btnSubmitGeneralDetails)
            ),*//*
          *//*  Pair(
                view.findViewById<Spinner>(R.id.spinnerMinimumEquipment),
                view.findViewById<Button>(R.id.btnUploadMinimumEquipment)
            ),*//*
         *//*   Pair(
                view.findViewById<Spinner>(R.id.spinnerDirectionBoards),
                view.findViewById<Button>(R.id.btnUploadDirectionBoards)
            ),*//*
        )*/

        /*  spinnersWithButtons.forEach { (spinner, button) ->
              spinner.adapter = yesNoAdapter
              spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                  override fun onItemSelected(
                      parent: AdapterView<*>,
                      view: View?,
                      position: Int,
                      id: Long
                  ) {
                      val selected = parent.getItemAtPosition(position).toString()
                      button.visibility = if (selected == "No") View.GONE else View.VISIBLE
                  }

                  override fun onNothingSelected(parent: AdapterView<*>) {}
              }
          }*/
    }

    private fun observeViewModel() {
        viewModel.stateList.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->populateSpinnerState((it.wrappedList ?: emptyList()) as ArrayList<StateModel?>,spinnerState) //adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
           // binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }


        viewModel.districtList.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->populateSpinnerDistrict((it.wrappedList ?: emptyList()) as ArrayList<DistrictModel?>,spinnerDistrict) //adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            // binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.blockList.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->populateSpinnerBlock((it.wrappedList ?: emptyList()) as ArrayList<BlockModel?>,spinnerBlock) //adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            // binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.gpList.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 ->populateSpinnerGp((it.wrappedList ?: emptyList()) as ArrayList<GpModel?>,spinnerGp) //adapter.updateData(it.wrappedList ?: emptyList())
                    202 -> Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            // binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

    }
    private fun populateSpinnerState(alStateModel: java.util.ArrayList<StateModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, StateModel("--Select--","0"))
            val dbAdapter = StateAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }
    private fun populateSpinnerDistrict(alStateModel: java.util.ArrayList<DistrictModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, DistrictModel("--Select--","0"))
            val dbAdapter = DistrictAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }
    private fun populateSpinnerBlock(alStateModel: java.util.ArrayList<BlockModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, BlockModel("--Select--","0"))
            val dbAdapter = BlockAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }
    private fun populateSpinnerGp(alStateModel: java.util.ArrayList<GpModel?>, sp: Spinner) {
        if (!alStateModel.isEmpty() && alStateModel.size > 0) {
            alStateModel!!.add(0, GpModel("--Select--","0"))
            val dbAdapter = PanchayatAdapter(requireContext(), android.R.layout.simple_spinner_item, alStateModel
            )
            dbAdapter.notifyDataSetChanged()
            sp.adapter = dbAdapter
        }
    }
    private fun findById(view: View) {
        /////////////////////////Basic Info Section////////////
        etFacilityName= view.findViewById(R.id.etFacilityName)
        etFacilityType= view.findViewById(R.id.etFacilityType)
        etHouseNo= view.findViewById(R.id.etHouseNo)
        etStreet= view.findViewById(R.id.etStreet)
        etLandmark= view.findViewById(R.id.etLandmark)
        etVillage= view.findViewById(R.id.etVillage)
        etPinCode= view.findViewById(R.id.etPinCode)
        etMobile= view.findViewById(R.id.etMobile)
        etPhone= view.findViewById(R.id.etPhone)
        etEmail= view.findViewById(R.id.etEmail)
        etDistanceFromBusStand= view.findViewById(R.id.etDistanceFromBusStand)
        etDistanceFromAutoStand= view.findViewById(R.id.etDistanceFromAutoStand)
        etDistanceFromRailwayStand= view.findViewById(R.id.etDistanceFromRailwayStand)
        etDistanceFromTrainingToResidentialCentre= view.findViewById(R.id.etDistanceFromTrainingToResidentialCentre)
        etWardenName= view.findViewById(R.id.etWardenName)
        etWardenEmpID= view.findViewById(R.id.etWardenEmpID)
        etWardenAddress= view.findViewById(R.id.etWardenAddress)
        etWardenEmailId= view.findViewById(R.id.etWardenEmailId)
        etWardenMobile= view.findViewById(R.id.etWardenMobile)
        spinnerState= view.findViewById(R.id.spinnerState)
        spinnerDistrict= view.findViewById(R.id.spinnerDistrict)
        spinnerBlock= view.findViewById(R.id.spinnerBlock)
        spinnerGp= view.findViewById(R.id.spinnerGp)
        spinnerTypeOfArea= view.findViewById(R.id.spinnerTypeOfArea)
        spinnerCatOfTCLocation= view.findViewById(R.id.spinnerCatOfTCLocation)
        spinnerPickupAndDropFacility= view.findViewById<Spinner>(R.id.spinnerPickupAndDropFacility)
        spinnerWardenGender= view.findViewById(R.id.spinnerWardenGender)
        btnUploadPoliceVerificationDoc= view.findViewById(R.id.btnUploadPoliceVerificationDoc)
        ivPoliceVerificationDocPreview= view.findViewById(R.id.ivPoliceVerificationDocPreview)
        btnUploadAppointmentLetterDoc= view.findViewById(R.id.btnUploadAppointmentLetterDoc)
        ivAppointmentLetterDocPreview= view.findViewById(R.id.ivAppointmentLetterDocPreview)
        ivOwnerBuildingDocPreview= view.findViewById(R.id.ivOwnerBuildingDocPreview)
        ivRoofOfBuildingPreview= view.findViewById(R.id.ivRoofOfBuildingPreview)
        ivWhetherStructurallySoundPreview= view.findViewById(R.id.ivWhetherStructurallySoundPreview)
        ivSignOfLeakagePreview= view.findViewById(R.id.ivSignOfLeakagePreview)
        ivConformanceDDUGKYPreview= view.findViewById(R.id.ivConformanceDDUGKYPreview)
        ivProtectionofStairsPreview= view.findViewById(R.id.ivProtectionofStairsPreview)
        ivCorridorPreview= view.findViewById(R.id.ivCorridorPreview)
        ivSecuringWiresPreview= view.findViewById(R.id.ivSecuringWiresPreview)
        ivSwitchBoardsPreview= view.findViewById(R.id.ivSwitchBoardsPreview)
        ivHostelNameBoardPreview= view.findViewById(R.id.ivHostelNameBoardPreview)
        ivEntitlementBoardPreview= view.findViewById(R.id.ivEntitlementBoardPreview)
        ivContactDetailPreview= view.findViewById(R.id.ivContactDetailPreview)
        ivBasicInfoBoardPreview= view.findViewById(R.id.ivBasicInfoBoardPreview)
        ivFoodSpecificationBoardPreview= view.findViewById(R.id.ivFoodSpecificationBoardPreview)
        ivTypeLivingRoofPreview= view.findViewById(R.id.ivTypeLivingRoofPreview)
        ivCeilingPreview= view.findViewById(R.id.ivCeilingPreview)
        ivAirConditioningPreview= view.findViewById(R.id.ivAirConditioningPreview)
        ivLivingAreaInfoBoardPreview= view.findViewById(R.id.ivLivingAreaInfoBoardPreview)
        ivLightsInToiletPreview= view.findViewById(R.id.ivLightsInToiletPreview)
        ivToiletFlooringPreview= view.findViewById(R.id.ivToiletFlooringPreview)
        ivFoodPreparedTrainingPreview= view.findViewById(R.id.ivFoodPreparedTrainingPreview)
        ivReceptionAreaPreview= view.findViewById(R.id.ivReceptionAreaPreview)
        ivWhetherHostelsSeparatedPreview= view.findViewById(R.id.ivWhetherHostelsSeparatedPreview)
        ivWardenWhereMalesStayPreview= view.findViewById(R.id.ivWardenWhereMalesStayPreview)
        ivWardenWhereLadyStayPreview= view.findViewById(R.id.ivWardenWhereLadyStayPreview)
        ivSecurityGaurdsPreview= view.findViewById(R.id.ivSecurityGaurdsPreview)
        ivWhetherFemaleDoctorPreview= view.findViewById(R.id.ivWhetherFemaleDoctorPreview)
        ivSafeDrinkingPreview= view.findViewById(R.id.ivSafeDrinkingPreview)
        ivFirstAidKitPreview= view.findViewById(R.id.ivFirstAidKitPreview)
        ivFireFightingEquipmentPreview= view.findViewById(R.id.ivFireFightingEquipmentPreview)
        ivBiometricDevicePreview= view.findViewById(R.id.ivBiometricDevicePreview)
        ivElectricalPowerPreview= view.findViewById(R.id.ivElectricalPowerPreview)
        ivGrievanceRegisterPreview= view.findViewById(R.id.ivGrievanceRegisterPreview)

        /////////////////////////Infrastructure Info Section////////////
        spinnerOwnerBuilding= view.findViewById(R.id.spinnerOwnerBuilding)
        spinnerRoofOfBuilding= view.findViewById(R.id.spinnerRoofOfBuilding)
        spinnerWhetherStructurallySound= view.findViewById(R.id.spinnerWhetherStructurallySound)
        spinnerVisibleSignOfLeakage= view.findViewById(R.id.spinnerVisibleSignOfLeakage)
        spinnerConformanceDDUGKY= view.findViewById(R.id.spinnerConformanceDDUGKY)
        spinnerProtectionofStairs= view.findViewById(R.id.spinnerProtectionofStairs)
        spinnerCorridor= view.findViewById(R.id.spinnerCorridor)
        spinnerSecuringWires= view.findViewById(R.id.spinnerSecuringWires)
        spinnerSwitchBoards= view.findViewById(R.id.spinnerSwitchBoards)
        spinnerHostelNameBoard= view.findViewById(R.id.spinnerHostelNameBoard)
        spinnerEntitlementBoard= view.findViewById(R.id.spinnerEntitlementBoard)
        spinnerContactDetail= view.findViewById(R.id.spinnerContactDetail)
        spinnerBasicInfoBoard= view.findViewById(R.id.spinnerBasicInfoBoard)
        spinnerFoodSpecificationBoard= view.findViewById(R.id.spinnerFoodSpecificationBoard)
        etAreaOfBuilding= view.findViewById(R.id.etAreaOfBuilding)
        etCirculatingArea= view.findViewById(R.id.etCirculatingArea)
        etAreaForOutDoorGames= view.findViewById(R.id.etAreaForOutDoorGames)

        /////////////////////////Living Area Information Section////////////
        spinnerTypeLivingRoof= view.findViewById(R.id.spinnerTypeLivingRoof)
        spinnerCeiling= view.findViewById(R.id.spinnerCeiling)
        spinnerAirConditioning= view.findViewById(R.id.spinnerAirConditioning)
        spinnerLivingAreaInfoBoard= view.findViewById(R.id.spinnerLivingAreaInfoBoard)
        etHeightOfCeiling= view.findViewById(R.id.etHeightOfCeiling)
        etRoomLength= view.findViewById(R.id.etRoomLength)
        etRoomWidth= view.findViewById(R.id.etRoomWidth)
        etRoomArea= view.findViewById(R.id.etRoomArea)
        etRoomWindowArea= view.findViewById(R.id.etRoomWindowArea)
        etCot= view.findViewById(R.id.etCot)
        etMattress= view.findViewById(R.id.etMattress)
        etBedSheet= view.findViewById(R.id.etBedSheet)
        etCupboard= view.findViewById(R.id.etCupboard)
        etStudentsPermitted= view.findViewById(R.id.etStudentsPermitted)
        etLivingAreaLights= view.findViewById(R.id.etLivingAreaLights)
        etLivingAreaFans= view.findViewById(R.id.etLivingAreaFans)
        /////////////////////////Toilets Information Section////////////
        spinnerToiletType= view.findViewById(R.id.spinnerToiletType)
        spinnerToiletFlooringType= view.findViewById(R.id.spinnerToiletFlooringType)
        spinnerConnectionToRunningWater= view.findViewById(R.id.spinnerConnectionToRunningWater)
        spinnerOverheadTanks= view.findViewById(R.id.spinnerOverheadTanks)
        etLightsInToilet= view.findViewById(R.id.etLightsInToilet)
        etFemaleUrinal= view.findViewById(R.id.etFemaleUrinal)
        etFemaleWashbasins= view.findViewById(R.id.etFemaleWashbasins)
        etCandidatesPermissible= view.findViewById(R.id.etCandidatesPermissible)
        /////////////////////////Non-Living Areas Section////////////
        spinnerFoodPreparedTrainingCenter= view.findViewById(R.id.spinnerFoodPreparedTrainingCenter)
        spinnerDiningRecreationAreaSeparate= view.findViewById(R.id.spinnerDiningRecreationAreaSeparate)
        spinnerTvAvailable= view.findViewById(R.id.spinnerTvAvailable)
        spinnerReceptionAreaAvailable= view.findViewById(R.id.spinnerReceptionAreaAvailable)
        etKitchenLength= view.findViewById(R.id.etKitchenLength)
        etKitchenWidth= view.findViewById(R.id.etKitchenWidth)
        etKitchenArea= view.findViewById(R.id.etKitchenArea)
        etStoolsChairsBenches= view.findViewById(R.id.etStoolsChairsBenches)
        etWashArea= view.findViewById(R.id.etWashArea)
        etDiningLength= view.findViewById(R.id.etDiningLength)
        etDiningWidth= view.findViewById(R.id.etDiningWidth)
        etDiningArea= view.findViewById(R.id.etDiningArea)
        etRecreationLength= view.findViewById(R.id.etRecreationLength)
        etRecreationWidth= view.findViewById(R.id.etRecreationWidth)
        etRecreationArea= view.findViewById(R.id.etRecreationArea)

        /////////////////////////Indoor Game Section////////////
        etIndoorGameName= view.findViewById(R.id.etIndoorGameName)
        /////////////////////////Residential Facilities Section////////////
        spinnerWhetherHostelsSeparated= view.findViewById(R.id.spinnerWhetherHostelsSeparated)
        spinnerWardenWhereMalesStay= view.findViewById(R.id.spinnerWardenWhereMalesStay)
        spinnerWardenWhereLadyStay= view.findViewById(R.id.spinnerWardenWhereLadyStay)
        spinnerSecurityGaurdsAvailable= view.findViewById(R.id.spinnerSecurityGaurdsAvailable)
        spinnerWhetherFemaleDoctorAvailable= view.findViewById(R.id.spinnerWhetherFemaleDoctorAvailable)
        spinnerWhetherMaleDoctorAvailable= view.findViewById(R.id.spinnerWhetherMaleDoctorAvailable)
        /////////////////////////Support Facilities Section////////////
        spinnerSafeDrinikingAvailable= view.findViewById(R.id.spinnerSafeDrinikingAvailable)
        spinnerFirstAidKitAvailable= view.findViewById(R.id.spinnerFirstAidKitAvailable)
        spinnerFireFightingEquipmentAvailable= view.findViewById(R.id.spinnerFireFightingEquipmentAvailable)
        spinnerBiometricDeviceAvailable= view.findViewById(R.id.spinnerBiometricDeviceAvailable)
        spinnerElectricalPowerBackupAvailable= view.findViewById(R.id.spinnerElectricalPowerBackupAvailable)
        spinnerGrievanceRegisterAvailable= view.findViewById(R.id.spinnerGrievanceRegisterAvailable)

    }

    private fun setupExpandableSections(view: View) {
        val sections = listOf(
            Triple(
                R.id.headerTCBasicInfo,
                R.id.layoutTCBasicInfoContent,
                R.id.ivToggleTCBasicInfo),
            Triple(
                R.id.headerInfraDetailCompliance,
                R.id.layoutInfraDetailComplianceContent,
                R.id.ivToggleInfraDetailCompliance
            ),
            Triple(
                R.id.headerLivingAreaInfo,
                R.id.layoutLivingAreaInfoContent,
                R.id.ivToggleLivingAreaInfo
            ),
            Triple(
                R.id.headerToilets,
                R.id.layoutToiletsContent,
                R.id.ivToggleToilets
            ),
            Triple(
                R.id.headerToilets,
                R.id.layoutToiletsContent,
                R.id.ivToggleToilets
            ),
            Triple(
                R.id.headerNonLivingArea,
                R.id.layoutNonLivingAreaContent,
                R.id.ivToggleNonLivingArea
            ),
            Triple(
                R.id.headerIndoorGameDetail,
                R.id.layoutIndoorGameDetailContent,
                R.id.ivToggleIndoorGameDetail
            ),
            Triple(
                R.id.headerRfAvailable,
                R.id.layoutRfAvailableContent,
                R.id.ivToggleRfAvailable
            ),
            Triple(
                R.id.headerSfAvailable,
                R.id.layoutSfAvailableContent,
                R.id.ivToggleSfAvailable
            ),


            )
        val expansionStates = MutableList(sections.size) { false }

        sections.forEachIndexed { index, (headerId, contentId, iconId) ->
            val header = view.findViewById<LinearLayout>(headerId)
            val content = view.findViewById<LinearLayout>(contentId)
            val icon = view.findViewById<ImageView>(iconId)

            header.setOnClickListener {
                expansionStates[index] = !expansionStates[index]
                content.visibility = if (expansionStates[index]) View.VISIBLE else View.GONE
                icon.setImageResource(
                    if (expansionStates[index]) R.drawable.outline_arrow_upward_24
                    else R.drawable.ic_dropdown_arrow
                )
            }
        }
    }

    private fun checkAndLaunchCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
            launchCamera()
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
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
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

   private fun validateBasicInfoForm(view: View): Boolean {
       var isValid = true
       
       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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

       // Validate all required Spinners
       if (!checkSpinner(spinnerState, "State")) isValid = false
       if (!checkSpinner(spinnerDistrict, "District")) isValid = false
       if (!checkSpinner(spinnerBlock, "Block")) isValid = false
       if (!checkSpinner(spinnerGp, "Gp")) isValid = false
       if (!checkSpinner(spinnerTypeOfArea, "TypeOfArea")) isValid = false
       if (!checkSpinner(spinnerCatOfTCLocation, "Category Of Location")) isValid = false
       if (!checkSpinner(spinnerPickupAndDropFacility, "PickupAndDropFacility")) isValid = false
       if (!checkSpinner(spinnerWardenGender, "Warden Gender")) isValid = false

       // Validate required TextInputEditTexts
       if (!checkTextInput(etFacilityName, "Residential Facility Name")) isValid = false
       if (!checkTextInput(etFacilityType, "Residential Facility type")) isValid = false
       if (!checkTextInput(etHouseNo, "House No.")) isValid = false
       if (!checkTextInput(etStreet, "Street")) isValid = false
       if (!checkTextInput(etLandmark, "Landmark")) isValid = false
       if (!checkTextInput(etVillage, "Village/Ward No.")) isValid = false
       if (!checkTextInput(etPinCode, "Pin code")) isValid = false
       if (!checkTextInput(etMobile, "Mobile No.")) isValid = false
       if (!checkTextInput(etPhone, "Residential Facility Phone No. with STD Code")) isValid = false
       if (!checkTextInput(etEmail, "Email Id")) isValid = false
       if (!checkTextInput(etDistanceFromBusStand, "Approximate Distance from a Prominent Bus Stand (In Mtrs.)")) isValid = false
       if (!checkTextInput(etDistanceFromAutoStand, "Approximate Distance from an Auto Stand (In Mtrs.)")) isValid = false
       if (!checkTextInput(etDistanceFromRailwayStand, "Approximate Distance from a Prominent Railway Station (In Mtrs.)")) isValid = false
       if (!checkTextInput(etDistanceFromTrainingToResidentialCentre, "Distance from the Training Centre to Residential Centre (In Kms.)")) isValid = false
       if (!checkTextInput(etWardenName, "Warden's Name")) isValid = false
       if (!checkTextInput(etWardenEmpID, "Warden's Employee ID")) isValid = false
       if (!checkTextInput(etWardenAddress, "Warden's Address")) isValid = false
       if (!checkTextInput(etWardenEmailId, "Warden's Email Id")) isValid = false
       if (!checkTextInput(etWardenMobile, "Warden's Mobile No.")) isValid = false
       if(base64ALDocFile == null || base64PVDocFile == null ) isValid = false

       return isValid
       
       
    }
   private fun validateInfraInfoForm(view: View): Boolean {
       var isValid = true

       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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

       // Validate all required Spinners
       if (!checkSpinner(spinnerOwnerBuilding, "Ownership of Building")) isValid = false
       if (!checkSpinner(spinnerRoofOfBuilding, "Roof of the Building")) isValid = false
       if (!checkSpinner(spinnerWhetherStructurallySound, "Whether it is Structurally Sound on Visual Inspection Plastering and Painting of Internal and External Walls and Ceiling")) isValid = false
       if (!checkSpinner(spinnerVisibleSignOfLeakage, "Visible Signs of Leakages from Walls and Roof Applicable for both RCC and Non RCC Structures")) isValid = false
       if (!checkSpinner(spinnerConformanceDDUGKY, "Conformance to DDU-GKY look and feel Standards as per sub section 5.1.1")) isValid = false
       if (!checkSpinner(spinnerProtectionofStairs, "Protection of Stairs, Balconies, and Other Locations")) isValid = false
       if (!checkSpinner(spinnerCorridor, "Corridor")) isValid = false
       if (!checkSpinner(spinnerSecuringWires, "Securing of Wires Done")) isValid = false
       if (!checkSpinner(spinnerSwitchBoards, "Switch Boards and Panel Boards")) isValid = false
       if (!checkSpinner(spinnerHostelNameBoard, "Hostel Name Board")) isValid = false
       if (!checkSpinner(spinnerEntitlementBoard, "Student Entitlement Board and Responsibilities Board")) isValid = false
       if (!checkSpinner(spinnerContactDetail, "Contact Detail of Important People")) isValid = false
       if (!checkSpinner(spinnerBasicInfoBoard, "Basic Information Board")) isValid = false
       if (!checkSpinner(spinnerFoodSpecificationBoard, "Food Specification Board")) isValid = false

       // Validate required TextInputEditTexts
       if (!checkTextInput(etAreaOfBuilding, "Area of the Building (Sq. Ft.) (Include Corridors but Exclude Open to Sky Spaces Like Court Yards etc)")) isValid = false
       if (!checkTextInput(etCirculatingArea, "Circulating Area")) isValid = false
       if (!checkTextInput(etAreaForOutDoorGames, "Area (In Sq ft.)")) isValid = false
       if(base64OwnerBuildingDocFile == null || base64RoofOfBuildingDocFile == null||base64WhetherStructurallySoundDocFile == null || base64SignOfLeakageDocFile == null
           || base64ConformanceDDUGKYDocFile == null || base64ProtectionofStairsDocFile == null||base64CorridorDocFile == null || base64SecuringWiresDocFile == null
           || base64SwitchBoardsDocFile == null || base64HostelNameBoardDocFile == null||base64EntitlementBoardDocFile == null || base64ContactDetailDocFile == null
           ||base64FoodSpecificationBoardDocFile==null) isValid = false

       return isValid


    }
   private fun validateLivingAreaInfoForm(view: View): Boolean {
       var isValid = true

       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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

       // Validate all required Spinners
       if (!checkSpinner(spinnerTypeLivingRoof, "Type of Roof(RCC/Non RCC)")) isValid = false
       if (!checkSpinner(spinnerCeiling, "False Ceiling Provided")) isValid = false
       if (!checkSpinner(spinnerAirConditioning, "Does the room has Air Conditioning ?")) isValid = false
       if (!checkSpinner(spinnerLivingAreaInfoBoard, "Living Area Information Board as per SF 5.1 B4 ?")) isValid = false

       // Validate required TextInputEditTexts
       if (!checkTextInput(etHeightOfCeiling, "Height of Ceiling(In ft)")) isValid = false
       if (!checkTextInput(etRoomLength, "Length (In ft)")) isValid = false
       if (!checkTextInput(etRoomWidth, "Width (In ft)")) isValid = false
       if (!checkTextInput(etRoomWindowArea, "Window Area (In Sq. ft)")) isValid = false
       if (!checkTextInput(etCot, "Cot (In No.)")) isValid = false
       if (!checkTextInput(etMattress, "Mattress (In No.)")) isValid = false
       if (!checkTextInput(etBedSheet, "Bed Sheet (In No.)")) isValid = false
       if (!checkTextInput(etCupboard, "Cupboard / Almirah / Trunk with Locking Arrangements (In No.)")) isValid = false
       if (!checkTextInput(etStudentsPermitted, "Number of Students Permitted")) isValid = false
       if (!checkTextInput(etLivingAreaLights, "Lights")) isValid = false
       if (!checkTextInput(etLivingAreaFans, "Fans")) isValid = false
       if(base64TypeLivingRoofDocFile == null || base64CeilingDocFile == null ||base64AirConditioningDocFile == null || base64LivingAreaInfoBoardDocFile == null
           || base64CotDocFile == null || base64MattressDocFile == null ||base64BedSheetDocFile == null || base64CupBoardDocFile == null
           || base64LightsDocFile == null || base64FansDocFile == null) isValid = false


       return isValid


    }
   private fun validateToiletInfoForm(view: View): Boolean {
       var isValid = true

       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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

       // Validate all required Spinners
       if (!checkSpinner(spinnerToiletType, "Toilet Type")) isValid = false
       if (!checkSpinner(spinnerToiletFlooringType, "Type of Flooring")) isValid = false
       if (!checkSpinner(spinnerConnectionToRunningWater, "Connection To Running Water")) isValid = false
       if (!checkSpinner(spinnerOverheadTanks, "Overhead Tanks")) isValid = false


       // Validate required TextInputEditTexts
       if (!checkTextInput(etLightsInToilet, "Lights (In No.)")) isValid = false
       if (!checkTextInput(etFemaleUrinal, "Female Urinals")) isValid = false
       if (!checkTextInput(etFemaleWashbasins, "Female Washbasins")) isValid = false
       if (!checkTextInput(etCandidatesPermissible, "Maximum No. of Candidates Permissible")) isValid = false

       if(base64LightsInToiletDocFile == null || base64ToiletFlooringDocFile == null ) isValid = false



       return isValid


    }

   private fun validateNonLivingAreaInfoForm(view: View): Boolean {
       var isValid = true

       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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

       // Validate all required Spinners
       if (!checkSpinner(spinnerFoodPreparedTrainingCenter, "Whether Food for the Candidates is being Prepared in the Premises of the Training Center?")) isValid = false
       if (!checkSpinner(spinnerDiningRecreationAreaSeparate, "Are the Dining and Recreation Area Separate?")) isValid = false
       if (!checkSpinner(spinnerTvAvailable, "Whether TV with a Cable or Satellite Connection is Available for Viewing?")) isValid = false
       if (!checkSpinner(spinnerReceptionAreaAvailable, "Is reception area available?")) isValid = false


       // Validate required TextInputEditTexts
       if (!checkTextInput(etKitchenLength, "Length (In ft)")) isValid = false
       if (!checkTextInput(etKitchenWidth, "Width (In ft)")) isValid = false
       if (!checkTextInput(etKitchenArea, "Area")) isValid = false
       if (!checkTextInput(etStoolsChairsBenches, "No.of Stools/Chairs/Benches")) isValid = false
       if (!checkTextInput(etWashArea, "Wash Area")) isValid = false
       if (!checkTextInput(etDiningLength, "Length (in ft)")) isValid = false
       if (!checkTextInput(etDiningWidth, "Width (in ft)")) isValid = false
       if (!checkTextInput(etDiningArea, "Area")) isValid = false
       if (!checkTextInput(etRecreationLength, "Length (in ft)")) isValid = false
       if (!checkTextInput(etRecreationWidth, "Width (in ft)")) isValid = false
       if (!checkTextInput(etRecreationArea, "Area")) isValid = false


       return isValid


    }
   private fun validateIndoorGameInfoForm(view: View): Boolean {
       var isValid = true

       fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
           return if (spinner.selectedItemPosition == 0) {
               spinner.requestFocus()
               Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                   .show()
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
       // Validate required TextInputEditTexts
       if (!checkTextInput(etIndoorGameName, "Indoor Game Name")) isValid = false


       return isValid


    }

    private fun validateResidentialFacilitiesInfoForm(view: View): Boolean {
        var isValid = true

        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            return if (spinner.selectedItemPosition == 0) {
                spinner.requestFocus()
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                    .show()
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

        // Validate all required Spinners
        if (!checkSpinner(spinnerWhetherHostelsSeparated, "Whether Hostels for Male and Female Candidates Separated?")) isValid = false
        if (!checkSpinner(spinnerWardenWhereMalesStay, "Warden/care taker for hostels where males stay?")) isValid = false
        if (!checkSpinner(spinnerWardenWhereLadyStay, "Lady warden/caretaker for hostels where females stay?")) isValid = false
        if (!checkSpinner(spinnerSecurityGaurdsAvailable, "Are Security Gaurds Available ?")) isValid = false
        if (!checkSpinner(spinnerWhetherFemaleDoctorAvailable, "Whether Female Doctor on call is Available or Not ?")) isValid = false
        if (!checkSpinner(spinnerWhetherMaleDoctorAvailable, "Whether Male Doctor on call is Available or Not?")) isValid = false


        return isValid


    }
    private fun validateSupportFacilitiesInfoForm(view: View): Boolean {
        var isValid = true

        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            return if (spinner.selectedItemPosition == 0) {
                spinner.requestFocus()
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT)
                    .show()
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

        // Validate all required Spinners
        if (!checkSpinner(spinnerSafeDrinikingAvailable, "Safe Driniking Available ?")) isValid = false
        if (!checkSpinner(spinnerFirstAidKitAvailable, "First Aid Kit?")) isValid = false
        if (!checkSpinner(spinnerFireFightingEquipmentAvailable, "Fire-fighting Equipment?")) isValid = false
        if (!checkSpinner(spinnerBiometricDeviceAvailable, "Biometric Device?")) isValid = false
        if (!checkSpinner(spinnerElectricalPowerBackupAvailable, "Electrical Power Backup?")) isValid = false
        if (!checkSpinner(spinnerGrievanceRegisterAvailable, "Grievance Register?")) isValid = false


        return isValid


    }

    private fun calculateAndShowArea() {

        val lengthStr = etRoomLength.text.toString().trim()
        val widthStr = etRoomWidth.text.toString().trim()

        val length = lengthStr.toDoubleOrNull() ?: 0.0
        val width = widthStr.toDoubleOrNull() ?: 0.0

        val area = length * width
        etRoomArea.text = "$area"
    }


    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED ||
                coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getCurrentLocation() {
        // Uses high accuracy priority for precise location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {


                    binding.tvLatLang.text= location.latitude.toString()+","+location.longitude.toString()
                    latLangValue = location.latitude.toString()+","+location.longitude.toString()
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitBasicInfoForm(view: View) {
      /*  val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = CCTVComplianceRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            centralMonitor = view.findViewById<Spinner>(R.id.spinnerMonitorAccessible).selectedItem.toString(),
            centralMonitorFile = base64MonitorFile ?: "",
            cctvConformance = view.findViewById<Spinner>(R.id.spinnerConformance).selectedItem.toString(),
            cctvConformanceFile = base64ConformanceFile ?: "",
            cctvStorage = view.findViewById<Spinner>(R.id.spinnerStorage).selectedItem.toString(),
            cctvStorageFile = base64PVDocFile ?: "",
            dvrStaticIp = view.findViewById<Spinner>(R.id.spinnerDVRStaticIP).selectedItem.toString(),
            dvrStaticIpFile = base64DVRFile ?: "",
            ipEnabled = view.findViewById<Spinner>(R.id.spinnerIPEnabled).selectedItem.toString(),
            resolution = view.findViewById<Spinner>(R.id.spinnerResolution).selectedItem.toString(),
            videoStream = view.findViewById<Spinner>(R.id.spinnerVideoStream).selectedItem.toString(),
            remoteAccessBrowser = view.findViewById<Spinner>(R.id.spinnerRemoteAccessBrowser).selectedItem.toString(),
            simultaneousAccess = view.findViewById<Spinner>(R.id.spinnerSimultaneousAccess).selectedItem.toString(),
            supportedProtocols = view.findViewById<Spinner>(R.id.spinnerSupportedProtocols).selectedItem.toString(),
            colorVideoAudio = view.findViewById<Spinner>(R.id.spinnerColorVideoAudio).selectedItem.toString(),
            storageFacility = view.findViewById<Spinner>(R.id.spinnerStorageFacility).selectedItem.toString(),
        )
        viewModel.submitBasicInfoFormToServer(request, token)*/
    }


}
