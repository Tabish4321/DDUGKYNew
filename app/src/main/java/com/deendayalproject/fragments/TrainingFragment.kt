package com.deendayalproject.fragments

import SharedViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import android.widget.AutoCompleteTextView
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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.AcademicAreaAdapter
import com.deendayalproject.databinding.FragmentTrainingBinding
import com.deendayalproject.model.SectionHandler
import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.request.CCTVComplianceRequest
import com.deendayalproject.model.request.DLRequest
import com.deendayalproject.model.request.ElectricalWiringRequest
import com.deendayalproject.model.request.ITComeDomainLabDetailsRequest
import com.deendayalproject.model.request.ITLabDetailsRequest
import com.deendayalproject.model.request.InsertTcGeneralDetailsRequest
import com.deendayalproject.model.request.OfficeRoomDetailsRequest
import com.deendayalproject.model.request.ReceptionAreaRoomDetailsRequest
import com.deendayalproject.model.request.SubmitOfficeCumCounsellingRoomDetailsRequest
import com.deendayalproject.model.request.TCDLRequest
import com.deendayalproject.model.request.TCITLDomainLabDetailsRequest
import com.deendayalproject.model.request.TCRRequest
import com.deendayalproject.model.request.TcAvailabilitySupportInfraRequest
import com.deendayalproject.model.request.TcBasicInfoRequest
import com.deendayalproject.model.request.TcCommonEquipmentRequest
import com.deendayalproject.model.request.TcDescriptionOtherAreasRequest
import com.deendayalproject.model.request.TcSignagesInfoBoardRequest
import com.deendayalproject.model.request.ToiletDetailsRequest
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.SectionStatus
import com.deendayalproject.model.response.wrappedList
import com.deendayalproject.util.AppConstant.STATUS_QM
import com.deendayalproject.util.AppConstant.STATUS_SM
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.ProgressDialogUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrainingFragment : Fragment() {

    //      IT LAB All Parameter's use in Variable
//{
//        IT (LAB)
//    Spinner

    private lateinit var Academicadapter: AcademicAreaAdapter
    private var centersList = mutableListOf<wrappedList>()

    private var RoomNumber: String = ""
    private var RoomType: String = ""
    private lateinit var btnSubmitAdddMore: Button
    private lateinit var etLength: EditText
    private lateinit var etWidth: EditText
    private lateinit var tvArea: TextView
    private lateinit var LayoutLinear: LinearLayout
    private lateinit var RecyclerViewData: LinearLayout
    private lateinit var etroomType: EditText
    private lateinit var mainContent: LinearLayout
    private lateinit var lin_tcum_domain_lab: LinearLayout
    private lateinit var lin_itlab: LinearLayout
    private lateinit var lin_counselling_room: LinearLayout
    private lateinit var lin_office_counselling_room: LinearLayout
    private lateinit var lin_non_reception: LinearLayout
    private lateinit var lin_office_room: LinearLayout
    private lateinit var lin_domain_lab: LinearLayout
    private lateinit var lin_theory_cum_it_lab: LinearLayout
    private lateinit var lin_theory_cum_domain_lab: LinearLayout
    private lateinit var lin_theory_class_room: LinearLayout



    private lateinit var AcsdemicSpinner: Spinner
    private lateinit var spinnerITLepbftr: Spinner
    private lateinit var spinnerITLFalseCellingProvide: Spinner
    private lateinit var spinnerITLwhether_all_the_academic: Spinner
    private lateinit var spinnerITLAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerITLInternalSignage: Spinner
    private lateinit var spinnerITLCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerITLabInternetConnections: Spinner
    private lateinit var spinnerITLDoAllComputersHaveTypingTutor: Spinner
    private lateinit var spinnerITLTrainerChair: Spinner
    private lateinit var spinnerITLTrainerTable: Spinner
    private lateinit var spinnerITLtLabPhotograph: Spinner
    private lateinit var spinnerITLDoes_the_room_has: Spinner
    //    TextInputEditText
    private lateinit var etITLTypeofRoofItLab: TextInputEditText
    private lateinit var etITLHeightOfCelling: TextInputEditText
    private lateinit var etITLVentilationAreaInSqFt: TextInputEditText
    private lateinit var etITLSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etITLSoundLevelInDb: TextInputEditText
    private lateinit var etITLLanEnabledComputersInNo: TextInputEditText
    private lateinit var etITLTablets: TextInputEditText
    private lateinit var etITLStoolsChairs: TextInputEditText
    private lateinit var etITLLightsInNo: TextInputEditText
    private lateinit var etITLFansInNo: TextInputEditText
    //    ImageViewBase64
    private var base64ProofPreviewITLTypeofRoofItLab: String? = null
    private var base64ProofITLFalseCellingProvide: String? = null
    private var base64ProofITLHeightOfCelling: String? = null
    private var base64ProofITLVentilationAreaInSqFt: String? = null
    private var base64ProofITLSoundLevelAsPerSpecifications: String? = null
    private var base64ProofITLSoundLevelInDb: String? = null
    private var base64ProofITLwhether_all_the_academic: String? = null
    private var base64ProofITLAcademicRoomInformationBoard: String? = null
    private var base64ProofITLInternalSignage: String? = null
    private var base64ProofITLCctcCamerasWithAudioFacility: String? = null
    private var base64ProofITLLanEnabledComputersInNo: String? = null
    private var base64ProofITLInternetConnections: String? = null
    private var base64ProofITLDoAllComputersHaveTypingTutor: String? = null
    private var base64ProofITLTablets: String? = null
    private var base64ProofITLStoolsChairs: String? = null
    private var base64ProofITLTrainerChair: String? = null
    private var base64ProofITLTrainerTable: String? = null
    private var base64ProofITLLightsInNo: String? = null
    private var base64ProofITLFansInNo: String? = null
    private var base64ProofITLElectricaPowerBackUpForThRoom: String? = null
    private var base64ProofITLItLabPhotograph: String? = null
    private var base64ProofITLDoes_the_room_has: String? = null

    //    ImageView
    private lateinit var ivPreviewITLTypeofRoofItLab: ImageView
    private lateinit var ivPreviewITLFalseCellingProvide: ImageView
    private lateinit var ivPreviewITLHeightOfCelling: ImageView
    private lateinit var ivPreviewITLVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewITLSoundLevelAsPerSpecifications: ImageView
    private lateinit var ivPreviewITLSoundLevelInDb: ImageView
    private lateinit var ivPreviewITLwhether_all_the_academic: ImageView
    private lateinit var ivPreviewITLAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewITLInternalSignage: ImageView
    private lateinit var ivPreviewITLCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewITLLanEnabledComputersInNo: ImageView
    private lateinit var ivPreviewITLInternetConnections: ImageView
    private lateinit var ivPreviewITLDoAllComputersHaveTypingTutor: ImageView
    private lateinit var ivPreviewITLTablets: ImageView
    private lateinit var ivPreviewITLStoolsChairs: ImageView
    private lateinit var ivPreviewITLTrainerChair: ImageView
    private lateinit var ivPreviewITLTrainerTable: ImageView
    private lateinit var ivPreviewITLLightsInNo: ImageView
    private lateinit var ivPreviewITLFansInNo: ImageView
    private lateinit var ivPreviewITLElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewITLItLabPhotograph: ImageView
    private lateinit var ivPreviewITLDoes_the_room_has: ImageView
    //      Button


    //            Office Cum(Counselling room)
////    Spinner
    private lateinit var spinnerOfficeCumFalseCellingProvide: Spinner
    private lateinit var spinnerOfficeCumLepbftr: Spinner
    private lateinit var spinnerOfficeCumTableOfofficeCumpter: Spinner
    //    //    TextInputEditText
    private lateinit var etOfficeRoomPhotograph: TextInputEditText
    private lateinit var etOfficeCumTypeofRoofItLab: TextInputEditText
    private lateinit var etOfficeCumHeightOfCelling: TextInputEditText
    private lateinit var etOfficeCumSplaceforSecuringDoc: TextInputEditText
    private lateinit var etOfficeCumAnOfficeTableNo: TextInputEditText
    private lateinit var etOfficeCumChairs: TextInputEditText
    private lateinit var etOfficeCumPrinterCumScannerInNo: TextInputEditText
    private lateinit var etOfficeCumDigitalCameraInNo: TextInputEditText
    ////    ImageViewBase64
    private var base64ProofPreviewOfficeRoomPhotograph: String? = null
    private var base64ProofOfficeCumTypeofRoofItLab: String? = null
    private var base64ProofOfficeCumFalseCellingProvide: String? = null
    private var base64ProofOfficeCumHeightOfCelling: String? = null
    private var base64ProofOfficeCumSplaceforSecuringDoc: String? = null
    private var base64ProofOfficeCumAnOfficeTableNo: String? = null
    private var base64ProofOfficeCumChairs: String? = null
    private var base64ProofOfficeCumTableOfofficeCumpter: String? = null
    private var base64ProofOfficeCumPrinterCumScannerInNo: String? = null
    private var base64ProofOfficeCumDigitalCameraInNo: String? = null
    private var base64ProofOfficeCumElectricialPowerBackup: String? = null
    ////    ImageView
    private lateinit var ivPreviewOfficeRoomPhotograph: ImageView
    private lateinit var ivPreviewOfficeCumTypeofRoofItLab: ImageView
    private lateinit var ivPreviewOfficeCumFalseCellingProvide: ImageView
    private lateinit var ivPreviewOfficeCumHeightOfCelling: ImageView
    private lateinit var ivPreviewOfficeCumSplaceforSecuringDoc: ImageView
    private lateinit var ivPreviewOfficeCumAnOfficeTableNo: ImageView
    private lateinit var ivPreviewOfficeCumChairs: ImageView
    private lateinit var ivPreviewOfficeCumTableOfofficeCumpter: ImageView
    private lateinit var ivPreviewOfficeCumPrinterCumScannerInNo: ImageView
    private lateinit var ivPreviewOfficeCumDigitalCameraInNo: ImageView
    private lateinit var ivPreviewOfficeCumElectricialPowerBackup: ImageView
//    Reception Area

    private lateinit var spinnerReceptionAreaEPBR: Spinner
    private lateinit var ivPreviewReceptionAreaPhotogragh: ImageView
    private var base64ProofPreviewReceptionAreaPhotogragh: String? = null

//    CounsellingRoomArea


    private lateinit var spinnerCounsellingRoomAreaPhotograph: Spinner
    private lateinit var ivPreviewCounsellingRoomAreaPhotograph: ImageView
    private var base64ProofPreviewCounsellingRoomPhotogragh: String? = ""


    //     OfficeRoom
//      TextInputEditText
    private lateinit var etOROfficeRoomPhotograph: TextInputEditText
    private lateinit var etORTypeofRoofItLab: TextInputEditText
    private lateinit var etORHeightOfCelling: TextInputEditText
    private lateinit var etORSplaceforSecuringDoc: TextInputEditText
    private lateinit var etORAnOfficeTableNo: TextInputEditText
    private lateinit var etORChairs: TextInputEditText
    private lateinit var etORPrinterCumScannerInNo: TextInputEditText
    private lateinit var etORDigitalCameraInNo: TextInputEditText


    //    Spinner
    private lateinit var spinnerORFalseCellingProvide: Spinner
    private lateinit var spinnerORTableOfofficeCumpter: Spinner
    private lateinit var spinnerORPOEPBFTR: Spinner






    //    ////    ImageViewBase64
    private var base64ProofPreviewOROfficeRoomORPhotograph: String? = null
    private var base64ProofORTypeofRoofItLab: String? = null
    private var base64ProofORFalseCellingProvide: String? = null
    private var base64ProofORHeightOfCelling: String? = null
    private var base64ProofORSplaceforSecuringDoc: String? = null
    private var base64ProofORAnOfficeTableNo: String? = null
    private var base64ProofORChairs: String? = null
    private var base64ProofORTableOfofficeCumpter: String? = null
    private var base64ProofORPrinterCumScannerInNo: String? = null
    private var base64ProofORDigitalCameraInNo: String? = null
    private var base64ProofORElectricialPowerBackup: String? = null
    //////    ImageView
    private lateinit var ivPreviewOROfficeRoomPhotograph: ImageView
    private lateinit var ivPreviewORTypeofRoofItLab: ImageView
    private lateinit var ivPreviewORFalseCellingProvide: ImageView
    private lateinit var ivPreviewORHeightOfCelling: ImageView
    private lateinit var ivPreviewORSplaceforSecuringDoc: ImageView
    private lateinit var ivPreviewORAnOfficeTableNo: ImageView
    private lateinit var ivPreviewORChairs: ImageView
    private lateinit var ivPreviewORTableOfofficeCumpter: ImageView
    private lateinit var ivPreviewORPrinterCumScannerInNo: ImageView
    private lateinit var ivPreviewORDigitalCameraInNo: ImageView
    private lateinit var ivPreviewORElectricialPowerBackup: ImageView


//btnSubmitITCDL

//    IT COME DOMAIN LAB




    //      TextInputEditText
    private lateinit var etITCDLTypeofRoofItLab: TextInputEditText
    private lateinit var etITCDLFalseCellingProvide: TextInputEditText
    private lateinit var etITCDLHeightOfCelling: TextInputEditText
    private lateinit var etITCDLVentilationAreaInSqFt: TextInputEditText
    private lateinit var etITCDLSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etITCDLabSoundLevelInDb: TextInputEditText
    private lateinit var etITCDLLanEnabledComputersInNo: TextInputEditText
    private lateinit var etITCDLTablets: TextInputEditText
    private lateinit var etITCDLStoolsChairs: TextInputEditText
    private lateinit var etITCDLLightsInNo: TextInputEditText
    private lateinit var etITCDLFansInNo: TextInputEditText
    private lateinit var etITCDLItLabPhotograph: TextInputEditText
    private lateinit var etITCDLListofDomain: TextInputEditText

    //    Spinner
    private lateinit var spinnerITCDLwhether_all_the_academic: Spinner
    private lateinit var spinnerITCDLAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerITCDLInternalSignage: Spinner
    private lateinit var spinnerITCDLCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerITCDLInternetConnections: Spinner
    private lateinit var spinnerITCDLDoAllComputersHaveTypingTutor: Spinner
    private lateinit var spinnerITCDLTrainerChair: Spinner
    private lateinit var spinnerITCDLTrainerTable: Spinner
    private lateinit var spinnerITCDLElectricaPowerBackUp: Spinner
    private lateinit var spinnerITCDLDoes_the_room_has: Spinner

    //    ////    ImageViewBase64
    private var base64ProofPreviewITCDLTypeofRoofItLab: String? = null
    private var base64ProofITCDLFalseCellingProvide: String? = null
    private var base64ProofITCDLabHeightOfCelling: String? = null
    private var base64ProofITCDLVentilationAreaInSqFt: String? = null
    private var base64ProofITCDLabSoundLevelInDb: String? = null
    private var base64ProofITCDLwhether_all_the_academic: String? = null
    private var base64ProofITCDLAcademicRoomInformationBoard: String? = null
    private var base64ProofITCDLInternalSignage: String? = null
    private var base64ProofITCDLCctcCamerasWithAudioFacility: String? = null
    private var base64ProofITCDLLanEnabledComputersInNo: String? = null
    private var base64ProofITCDLInternetConnections: String? = null
    private var base64ProofITCDLDoAllComputersHaveTypingTutor: String? = null
    private var base64ProofITCDLTablets: String? = null
    private var base64ProofITCDLStoolsChairs: String? = null
    private var base64ProofITCDLTrainerChair: String? = null
    private var base64ProofITCDLLightsInNo: String? = null
    private var base64ProofITCDLTrainerTable: String? = null
    private var base64ProofITCDLFansInNo: String? = null
    private var base64ProofITCDLElectricaPowerBackUpForThRoom: String? = null
    private var base64ProofITCDLItLabPhotograph: String? = null
    private var base64ProofITCDLListofDomain: String? = null
    private var base64ProofITCDLDoes_the_room_has: String? = null









    //////    ImageView
    private lateinit var ivPreviewITCDLTypeofRoofItLab: ImageView
    private lateinit var ivPreviewITCDLFalseCellingProvide: ImageView
    private lateinit var ivPreviewITCDLabHeightOfCelling: ImageView
    private lateinit var ivPreviewITCDLVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewITCDLabSoundLevelInDb: ImageView
    private lateinit var ivPreviewITCDLwhether_all_the_academic: ImageView
    private lateinit var ivPreviewITCDLAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewITCDLInternalSignage: ImageView
    private lateinit var ivPreviewITCDLCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewITCDLLanEnabledComputersInNo: ImageView
    private lateinit var ivPreviewITCDLInternetConnections: ImageView
    private lateinit var ivPreviewITCDLDoAllComputersHaveTypingTutor: ImageView
    private lateinit var ivPreviewITCDLTablets: ImageView
    private lateinit var ivPreviewITCDLStoolsChairs: ImageView
    private lateinit var ivPreviewITCDLTrainerChair: ImageView
    private lateinit var ivPreviewITCDLLightsInNo: ImageView
    private lateinit var ivPreviewITCDLTrainerTable: ImageView
    private lateinit var ivPreviewITCDLFansInNo: ImageView
    private lateinit var ivPreviewITCDLElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewITCDLItLabPhotograph: ImageView
    private lateinit var ivPreviewITCDLListofDomain: ImageView
    private lateinit var ivPreviewITCDLDoes_the_room_has: ImageView


//         Theory Cum IT Lab Lab
//btnSubmitTCIL



    //      TextInputEditText
    private lateinit var etTCILListofDomain: TextInputEditText
    private lateinit var etTCILTypeofRoofItLab: TextInputEditText
    private lateinit var etTCILFalseCellingProvide: TextInputEditText
    private lateinit var etTCILHeightOfCelling: TextInputEditText
    private lateinit var etTCILVentilationAreaInSqFt: TextInputEditText
    private lateinit var etTCILSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etTCILSoundLevelInDb: TextInputEditText
    private lateinit var etTCILLanEnabledComputersInNo: TextInputEditText
    private lateinit var etTCILTablets: TextInputEditText
    private lateinit var etTCILStoolsChairs: TextInputEditText
    private lateinit var etTCILTrainerChair: TextInputEditText
    private lateinit var etTCILTrainerTable: TextInputEditText
    private lateinit var etTCILLightsInNo: TextInputEditText
    private lateinit var etTCILFansInNo: TextInputEditText
    private lateinit var etTCILTheoryCumItLabPhotogragh: TextInputEditText
    //    Spinner
    private lateinit var spinnerTCILwhether_all_the_academic: Spinner
    private lateinit var spinnerTCILLAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerTCILInternalSignage: Spinner
    private lateinit var spinnerTCILCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerTCILInternetConnections: Spinner
    private lateinit var spinnerTCILElectricPowerBackup: Spinner
    private lateinit var spinnerTCILDLDoAllComputersHaveTypingTutor: Spinner
    private lateinit var spinnerTCILIPEnabled: Spinner
    private lateinit var spinnerTCILDLDoes_the_room_has: Spinner
    //    ////    ImageViewBase64
    private var base64ProofPreviewTCILListofDomain: String? = null
    private var base64ProofPreviewTCILTypeofRoofItLab: String? = null
    private var base64ProofPreviewTCILFalseCellingProvide: String? = null
    private var base64ProofPreviewTCILHeightOfCelling: String? = null
    private var base64ProofPreviewTCILVentilationAreaInSqFt: String? = null
    private var base64ProofPreviewTTCILSoundLevelInDb: String? = null
    private var base64ProofPreviewTCILwhether_all_the_academic: String? = null
    private var base64ProofPreviewTCILAcademicRoomInformationBoard: String? = null
    private var base64ProofPreviewTCILInternalSignage: String? = null
    private var base64ProofPreviewTCILCctcCamerasWithAudioFacility: String? = null
    private var base64ProofPreviewTCILLanEnabledComputersInNo: String? = null
    private var base64ProofPreviewTCILInternetConnections: String? = null
    private var base64ProofPreviewTCILDoAllComputersHaveTypingTutor: String? = null
    private var base64ProofPreviewTCILTablets: String? = null
    private var base64ProofPreviewTCILStoolsChairs: String? = null
    private var base64ProofPreviewTCILTrainerTable: String? = null
    private var base64ProofPreviewTCILTrainerChair: String? = null
    private var base64ProofPreviewTCILLightsInNo: String? = null
    private var base64ProofPreviewTCILFansInNo: String? = null
    private var base64ProofPreviewTCILElectricaPowerBackUpForThRoom: String? = null
    private var base64ProofPreviewTCILTheoryCumItLabPhotogragh: String? = null
    private var base64ProofPreviewTCILDoes_the_room_has: String? = null
    //////    ImageView
    private lateinit var ivPreviewTCILListofDomain: ImageView
    private lateinit var ivPreviewTCILTypeofRoofItLab: ImageView
    private lateinit var ivPreviewTCILFalseCellingProvide: ImageView
    private lateinit var ivPreviewTCILHeightOfCelling: ImageView
    private lateinit var ivPreviewTCILVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewTCILSoundLevelInDb: ImageView
    private lateinit var ivPreviewTCILwhether_all_the_academic: ImageView
    private lateinit var ivPreviewTCILAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewTCILInternalSignage: ImageView
    private lateinit var ivPreviewTCILCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewTCILLanEnabledComputersInNo: ImageView
    private lateinit var ivPreviewTCILInternetConnections: ImageView
    private lateinit var ivPreviewTCILDoAllComputersHaveTypingTutor: ImageView
    private lateinit var ivPreviewTCILTablets: ImageView
    private lateinit var ivPreviewTCILStoolsChairs: ImageView
    private lateinit var ivPreviewTCILTrainerTable: ImageView
    private lateinit var ivPreviewTCILTrainerChair: ImageView
    private lateinit var ivPreviewTCILLightsInNo: ImageView
    private lateinit var ivPreviewTCILFansInNo: ImageView
    private lateinit var ivPreviewTCILElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewTCILTheoryCumItLabPhotogragh: ImageView
    private lateinit var ivPreviewTCILDoes_the_room_has: ImageView


    //    Theory Cum Domain Lab
//      TextInputEditText
    private lateinit var etTCDLTypeofRoofItLab: TextInputEditText
    private lateinit var etTCDLFalseCellingProvide: TextInputEditText
    private lateinit var etTCDLHeightOfCelling: TextInputEditText
    private lateinit var etTCDLVentilationAreaInSqFt: TextInputEditText
    private lateinit var etTCDLSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etTCDLSoundLevelInDb: TextInputEditText
    private lateinit var etTCDLLcdDigitalProjector: TextInputEditText
    private lateinit var etTCDLChairForCandidatesInNo: TextInputEditText
    private lateinit var etTCDLTrainerChair: TextInputEditText
    private lateinit var etTCDLTrainerTable: TextInputEditText
    private lateinit var etTCDLWritingBoard: TextInputEditText
    private lateinit var etTCDLLightsInNo: TextInputEditText
    private lateinit var etTCDLFansInNo: TextInputEditText
    private lateinit var etTCDLListofDomain: TextInputEditText
    private lateinit var etTCDLDomainLabPhotogragh: TextInputEditText


    //    Spinner
    private lateinit var spinnerTCDLwhether_all_the_academic: Spinner
    private lateinit var spinnerTCDLAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerTCDLInternalSignage: Spinner
    private lateinit var spinnerTCDLCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerTCDLPowerBackup: Spinner
    private lateinit var spinnerTCDLDoes_the_room_has: Spinner
    //    ////    ImageViewBase64
    private  var base64ProofPreviewTCDLTypeofRoofItLab: String? = null
    private  var base64ProofPreviewTCDLFalseCellingProvide: String? = null
    private  var base64ProofPreviewTCDLHeightOfCelling: String? = null
    private  var base64ProofPreviewTCDLVentilationAreaInSqFt: String? = null
    private  var base64ProofPreviewTCDLSoundLevelInDb: String? = null
    private  var base64ProofPreviewTCDLwhether_all_the_academic: String? = null
    private  var base64ProofPreviewTCDLAcademicRoomInformationBoard: String? = null
    private  var base64ProofPreviewTCDLInternalSignage: String? = null
    private  var base64ProofPreviewTCDLCctcCamerasWithAudioFacility: String? = null
    private  var base64ProofPreviewTCDLLcdDigitalProjector: String? = null
    private  var base64ProofPreviewTCDLChairForCandidatesInNo: String? = null
    private  var base64ProofPreviewTCDLTrainerChair: String? = null
    private  var base64ProofPreviewTCDLTrainerTable: String? = null
    private  var base64ProofPreviewTCDLWritingBoard: String? = null
    private  var base64ProofPreviewTCDLLightsInNo: String? = null
    private  var base64ProofPreviewTCDLFansInNo: String? = null
    private  var base64ProofPreviewTCDLElectricaPowerBackUpForThRoom: String? = null
    private  var base64ProofPreviewTCDLListofDomain: String? = null
    private  var base64ProofPreviewTCDLDomainLabPhotogragh: String? = null
    private  var base64ProofPreviewTCDLDoes_the_room_has: String? = null
    //////    ImageView
    private lateinit var ivPreviewTCDLTypeofRoofItLab: ImageView
    private lateinit var ivPreviewTCDLFalseCellingProvide: ImageView
    private lateinit var ivPreviewTCDLHeightOfCelling: ImageView
    private lateinit var ivPreviewTCDLVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewTCDLSoundLevelInDb: ImageView
    private lateinit var ivPreviewTCDLwhether_all_the_academic: ImageView
    private lateinit var ivPreviewTCDLAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewTCDLInternalSignage: ImageView
    private lateinit var ivPreviewTCDLCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewTCDLLcdDigitalProjector: ImageView
    private lateinit var ivPreviewTCDLChairForCandidatesInNo: ImageView
    private lateinit var ivPreviewTCDLTrainerChair: ImageView
    private lateinit var ivPreviewTCDLTrainerTable: ImageView
    private lateinit var ivPreviewTCDLWritingBoard: ImageView
    private lateinit var ivPreviewTCDLLightsInNo: ImageView
    private lateinit var ivPreviewTCDLFansInNo: ImageView
    private lateinit var ivPreviewTCDLElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewTCDLListofDomain: ImageView
    private lateinit var ivPreviewTCDLDomainLabPhotogragh: ImageView
    private lateinit var ivPreviewTCDLDoes_the_room_has: ImageView




//    Domain Lab


    //      TextInputEditText
    private lateinit var etDLTypeofRoofItLab: TextInputEditText
    private lateinit var etDLFalseCellingProvide: TextInputEditText
    private lateinit var etDLHeightOfCelling: TextInputEditText
    private lateinit var etDLVentilationAreaInSqFt: TextInputEditText
    private lateinit var etDLSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etDLSoundLevelInDb: TextInputEditText
    private lateinit var etDLwhether_all_the_academic: TextInputEditText
    private lateinit var etDLLcdDigitalProjector: TextInputEditText
    private lateinit var etDLChairForCandidatesInNo: TextInputEditText
    private lateinit var etDLTrainerChair: TextInputEditText
    private lateinit var etDLTrainerTable: TextInputEditText
    private lateinit var etDLWritingBoard: TextInputEditText
    private lateinit var etDLLightsInNo: TextInputEditText
    private lateinit var etDLFansInNo: TextInputEditText
    private lateinit var etDLDomainLabPhotogragh: TextInputEditText



//    Spinner

    private lateinit var spinnerDLAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerDLInternalSignage: Spinner
    private lateinit var spinnerDLCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerDLElectricaPowerBackUp: Spinner
    private lateinit var spinnerDLwhether_all_the_academic: Spinner
    private lateinit var spinnerDLListofDomain: Spinner
    private lateinit var spinnerDLDoes_the_room_has: Spinner

    //    ////    ImageViewBase64
    private  var base64ProofPreviewDLTypeofRoofItLab: String? = null
    private  var base64ProofPreviewDLFalseCellingProvide: String? = null
    private  var base64ProofPreviewDLHeightOfCelling: String? = null
    private  var base64ProofPreviewDLVentilationAreaInSqFt: String? = null
    private  var base64ProofPreviewDLSoundLevelInDb: String? = null
    private  var base64ProofPreviewDLwhether_all_the_academic: String? = null
    private  var base64ProofPreviewDLAcademicRoomInformationBoard: String? = null
    private  var base64ProofPreviewDLInternalSignage: String? = null
    private  var base64ProofPreviewDLCctcCamerasWithAudioFacility: String? = null
    //    private  var ivPreviewDLCctcCamerasWithAudioFacility: String? = null
    private  var base64ProofPreviewDLLcdDigitalProjector: String? = null
    private  var base64ProofPreviewDLChairForCandidatesInNo: String? = null
    private  var base64ProofPreviewDLTrainerChair: String? = null
    private  var base64ProofPreviewDLTrainerTable: String? = null
    private  var base64ProofPreviewDLWritingBoard: String? = null
    private  var base64ProofPreviewDLLightsInNo: String? = null
    private  var base64ProofPreviewDLFansInNo: String? = null
    private  var base64ProofPreviewDLElectricaPowerBackUpForThRoom: String? = null
    private  var base64ProofPreviewDLILListofDomain: String? = null
    private  var base64ProofPreviewDLDomainLabPhotogragh: String? = null
    private  var base64ProofPreviewDLDoes_the_room_has: String? = null


    //////    ImageView
    private lateinit var ivPreviewDLTypeofRoofItLab: ImageView
    private lateinit var ivPreviewDLFalseCellingProvide: ImageView
    private lateinit var ivPreviewDLHeightOfCelling: ImageView
    private lateinit var ivPreviewDLVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewDLSoundLevelInDb: ImageView
    private lateinit var ivPreviewDLwhether_all_the_academic: ImageView
    private lateinit var ivPreviewDLAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewDLInternalSignage: ImageView
    private lateinit var ivPreviewDLCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewDLLcdDigitalProjector: ImageView
    private lateinit var ivPreviewDLChairForCandidatesInNo: ImageView
    private lateinit var ivPreviewDLTrainerChair: ImageView
    private lateinit var ivPreviewDLTrainerTable: ImageView
    private lateinit var ivPreviewDLWritingBoard: ImageView
    private lateinit var ivPreviewDLLightsInNo: ImageView
    private lateinit var ivPreviewDLFansInNo: ImageView
    private lateinit var ivPreviewDLElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewDLILListofDomain: ImageView
    private lateinit var ivPreviewDLDomainLabPhotogragh: ImageView
    private lateinit var ivPreviewDLDoes_the_room_has: ImageView

//    Theory Class Room

    //      TextInputEditText
    private lateinit var etTCRTypeofRoofItLab: TextInputEditText
    private lateinit var etTCRFalseCellingProvide: TextInputEditText
    private lateinit var etTCRHeightOfCelling: TextInputEditText
    private lateinit var etTCRVentilationAreaInSqFt: TextInputEditText
    private lateinit var etTCRSoundLevelAsPerSpecifications: TextInputEditText
    private lateinit var etTCRSoundLevelInDb: TextInputEditText
    //    private lateinit var ivPreviewTCRAcademicRoomInformationBoard: TextInputEditText
    private lateinit var etTCRLcdDigitalProjector: TextInputEditText
    private lateinit var lcd_digital_projector: TextInputEditText
    private lateinit var etTCRChairForCandidatesInNo: TextInputEditText
    private lateinit var etTCRTrainerChair: TextInputEditText
    private lateinit var etTCRTrainerTable: TextInputEditText
    private lateinit var etTCRWritingBoard: TextInputEditText
    private lateinit var etTCRLightsInNo: TextInputEditText
    private lateinit var etTCRFansInNo: TextInputEditText
    private lateinit var etTCRDomainLabPhotogragh: TextInputEditText
//    Spinner

    private lateinit var spinnerTCRwhether_all_the_academic: Spinner
    private lateinit var spinnerTCRAcademicRoomInformationBoard: Spinner
    private lateinit var spinnerTCRInternalSignage: Spinner
    private lateinit var spinnerTCRCctcCamerasWithAudioFacility: Spinner
    private lateinit var spinnerTCRDoes_the_room_has: Spinner
    private lateinit var spinnerTCRPowerBackup: Spinner

    //    ////    ImageViewBase64
    private  var base64ProofPreviewTCRTypeofRoofItLab: String? = null
    private  var base64ProofPreviewTCRFalseCellingProvide: String? = null
    private  var base64ProofPreviewTCRHeightOfCelling: String? = null
    private  var base64ProofPreviewTCRVentilationAreaInSqFt: String? = null
    private  var base64ProofPreviewTCRSoundLevelInDb: String? = null
    private  var base64ProofPreviewTCRwhether_all_the_academic: String? = null
    private  var base64ProofPreviewTCRAcademicRoomInformationBoard: String? = null
    private  var base64ProofPreviewTCRCctcCamerasWithAudioFacility: String? = null
    private  var base64ProofPreviewTCRLcdDigitalProjector: String? = null
    private  var base64ProofPreviewTCRChairForCandidatesInNo: String? = null
    private  var base64ProofPreviewTCRTrainerChair: String? = null
    private  var base64ProofPreviewTCRTrainerTable: String? = null
    private  var base64ProofPreviewTCRWritingBoard: String? = null
    private  var base64ProofPreviewTCRLightsInNo: String? = null
    private  var base64ProofPreviewTCRFansInNo: String? = null
    private  var base64ProofPreviewTCRElectricaPowerBackUpForThRoom: String? = null
    private  var base64ProofPreviewTCRDomainLabPhotogragh: String? = null
    private  var base64ProofPreviewTCRDoes_the_room_has: String? = null
    private  var base64ProofPreviewTCRInternalSignage: String? = null


    //////    ImageView

    private lateinit var ivPreviewTCRTypeofRoofItLab: ImageView
    private lateinit var ivPreviewTCRFalseCellingProvide: ImageView
    private lateinit var ivPreviewTCRHeightOfCelling: ImageView
    private lateinit var ivPreviewTCRVentilationAreaInSqFt: ImageView
    private lateinit var ivPreviewTCRSoundLevelInDb: ImageView
    private lateinit var ivPreviewTCRwhether_all_the_academic: ImageView
    private lateinit var ivPreviewTCRAcademicRoomInformationBoard: ImageView
    private lateinit var ivPreviewTCRCctcCamerasWithAudioFacility: ImageView
    private lateinit var ivPreviewivPreviewTCRInternalSignage: ImageView
    private lateinit var ivPreviewTCRLcdDigitalProjector: ImageView
    private lateinit var ivPreviewTCRChairForCandidatesInNo: ImageView
    private lateinit var ivPreviewTCRTrainerChair: ImageView
    private lateinit var ivPreviewTCRTrainerTable: ImageView
    private lateinit var ivPreviewTCRWritingBoard: ImageView
    private lateinit var ivPreviewTCRLightsInNo: ImageView
    private lateinit var ivPreviewTCRFansInNo: ImageView
    private lateinit var ivPreviewTCRElectricaPowerBackUpForThRoom: ImageView
    private lateinit var ivPreviewTCRDomainLabPhotogragh: ImageView
    private lateinit var ivPreviewTCRDoes_the_room_has: ImageView








    private lateinit var binding:  FragmentTrainingBinding

    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoUri: Uri
    private var currentPhotoTarget: String = ""
    private var centerId: String = ""
    private var sanctionOrder: String = ""
    private var status: String? = ""
    private var remarks: String? = ""

    private lateinit var sectionsStatus: SectionStatus

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Training center information
    private lateinit var etLatitude: TextInputEditText
    private lateinit var etLongitude: TextInputEditText

    // CCTV Photos (IP-enabled camera)
    private var base64MonitorFile: String? = null
    private var base64ConformanceFile: String? = null
    private var base64StorageFile: String? = null
    private var base64DVRFile: String? = null

    private lateinit var ivMonitorPreview: ImageView
    private lateinit var ivConformancePreview: ImageView
    private lateinit var ivStoragePreview: ImageView
    private lateinit var ivDVRPreview: ImageView

    ///////////////////////// WashBasin
    private lateinit var etMaleToilets: TextInputEditText
    private lateinit var btnUploadProofMaleToilets: Button
    private lateinit var ivPreviewMaleToiletsProof: ImageView

    private lateinit var btnUploadProofMaleToiletsSignage: Button
    private lateinit var ivPreviewMaleToiletsSignageProof: ImageView

    private lateinit var etFemaleToilets: TextInputEditText
    private lateinit var btnUploadProofFemaleToilets: Button
    private lateinit var ivPreviewFemaleToiletsProof: ImageView

    private lateinit var btnUploadProofFemaleToiletsSignage: Button
    private lateinit var ivPreviewFemaleToiletsSignageProof: ImageView

    private lateinit var etMaleUrinals: TextInputEditText
    private lateinit var btnUploadProofMaleUrinals: Button
    private lateinit var ivPreviewMaleUrinalsProof: ImageView

    private lateinit var etMaleWashBasins: TextInputEditText
    private lateinit var btnUploadProofMaleWashBasins: Button
    private lateinit var ivPreviewMaleWashBasinsProof: ImageView

    private lateinit var etFemaleWashBasins: TextInputEditText
    private lateinit var btnUploadProofFemaleWashBasins: Button
    private lateinit var ivPreviewFemaleWashBasinsProof: ImageView

    private lateinit var actvOverheadTanks: AutoCompleteTextView
    private lateinit var btnUploadProofOverheadTanks: Button
    private lateinit var ivPreviewOverheadTanksProof: ImageView

    private lateinit var actvTypeOfFlooring: AutoCompleteTextView
    private lateinit var btnUploadProofFlooring: Button
    private lateinit var ivPreviewFlooringProof: ImageView

    private lateinit var btnSubmitWasBasin: Button

    private var base64ProofMaleToilets: String? = null

    private var base64ProofMaleToiletsSignage: String? = null

    private var base64ProofFemaleToilets: String? = null

    private var base64ProofFemaleToiletsSignage: String? = null

    private var base64ProofMaleUrinals: String? = null

    private var base64ProofMaleWashBasins: String? = null

    private var base64ProofFemaleWashBasins: String? = null

    private var base64ProofOverheadTanks: String? = null

    private var base64ProofFlooring: String? = null


    // Electrical Wiring
    private var base64SwitchBoardImage: String? = null
    private var base64WireSecurityImage: String? = null

    private lateinit var ivSwitchBoardPreview: ImageView
    private lateinit var ivWireSecurityPreview: ImageView

    // General Details
    private var base64LeakageImage: String? = null
    private var base64StairsImage: String? = null

    private lateinit var ivLeakagePreview: ImageView
    private lateinit var ivStairsPreview: ImageView

    private lateinit var spinnerLeakageCheck: Spinner
    private lateinit var spinnerProtectionStairs: Spinner
    private lateinit var spinnerDDUConformance: Spinner
    private lateinit var spinnerCandidateSafety: Spinner



    // Spinners
    private lateinit var spinnerTcNameBoard: Spinner
    private lateinit var spinnerActivityAchievementBoard: Spinner
    private lateinit var spinnerStudentEntitlementBoard: Spinner
    private lateinit var spinnerContactDetailBoard: Spinner
    private lateinit var spinnerBasicInfoBoard: Spinner
    private lateinit var spinnerCodeConductBoard: Spinner
    private lateinit var spinnerStudentAttendanceBoard: Spinner

    // ImageViews
    private lateinit var ivTcNameBoardPreview: ImageView
    private lateinit var ivActivityAchievementBoardPreview: ImageView
    private lateinit var ivStudentEntitlementBoardPreview: ImageView
    private lateinit var ivContactDetailBoardPreview: ImageView
    private lateinit var ivBasicInfoBoardPreview: ImageView
    private lateinit var ivCodeConductBoardPreview: ImageView
    private lateinit var ivStudentAttendanceBoardPreview: ImageView

    // Base64 Image Strings
    private var base64TcNameBoardImage: String? = null
    private var base64ActivityAchievementBoardImage: String? = null
    private var base64StudentEntitlementBoardImage: String? = null
    private var base64ContactDetailBoardImage: String? = null
    private var base64BasicInfoBoardImage: String? = null
    private var base64CodeConductBoardImage: String? = null
    private var base64StudentAttendanceBoardImage: String? = null

    // Spinners
    private lateinit var spinnerPowerBackup: Spinner
    private lateinit var spinnerCCTV: Spinner
    private lateinit var spinnerDocumentStorage: Spinner
    private lateinit var spinnerGrievanceRegister: Spinner
    private lateinit var spinnerMinimumEquipment: Spinner
    private lateinit var spinnerDirectionBoards: Spinner
    private lateinit var etBiometricDevices: TextInputEditText
    private lateinit var etPrinterScanner: TextInputEditText
    private lateinit var etDigitalCamera: TextInputEditText

    // Base64 Image Strings
    private var base64PowerBackupImage: String? = null
    private var base64BiometricDevices: String? = null
    private var base64CCTVImage: String? = null
    private var base64DocumentStorageImage: String? = null
    private var base64PrinterScanner: String? = null
    private var base64DigitalCamera: String? = null
    private var base64GrievanceRegisterImage: String? = null
    private var base64MinimumEquipmentImage: String? = null
    private var base64DirectionBoardsImage: String? = null

    private lateinit var ivPowerBackupPreview: ImageView
    private lateinit var ivBiometricDevicesPreview: ImageView
    private lateinit var ivCCTVPreview: ImageView
    private lateinit var ivDocumentStoragePreview: ImageView
    private lateinit var ivPrinterScannerPreview: ImageView
    private lateinit var ivDigitalCameraPreview: ImageView
    private lateinit var ivGrievanceRegisterPreview: ImageView
    private lateinit var ivMinimumEquipmentPreview: ImageView
    private lateinit var ivDirectionBoardsPreview: ImageView

    // description of other area
    private var base64ProofUploadImage: String? = null
    private var base64CirculationProofImage: String? = null
    private var base64penSpaceProofImage: String? = null
    private var base64ParkingSpaceProofImage: String? = null

    private lateinit var ivProofPreview: ImageView
    private lateinit var ivCirculationProofPreview: ImageView
    private lateinit var ivOpenSpaceProofPreview: ImageView
    private lateinit var ivParkingProofPreview: ImageView


    private lateinit var etCorridorNo: TextInputEditText
    private lateinit var etDescLength: TextInputEditText
    private lateinit var etDescWidth: TextInputEditText
    private lateinit var etArea: TextInputEditText
    private lateinit var etLights: TextInputEditText
    private lateinit var etFans: TextInputEditText
    private lateinit var etCirculationArea: TextInputEditText
    private lateinit var etOpenSpace: TextInputEditText
    private lateinit var etExclusiveParkingSpace: TextInputEditText
    private lateinit var btnCalculateArea: Button

    //Support Infra
    private lateinit var spinnerFirstAidKit: Spinner
    private lateinit var spinnerSafeDrinkingWater: Spinner
    private lateinit var editFireFightingEquipment: EditText

    // Safe Drinking Water
    private lateinit var ivSafeDrinkingWaterPreview: ImageView
    private var base64SafeDrinkingWater: String? = null

    // Fire Fighting Equipment
    private lateinit var ivFireFightingEquipmentPreview: ImageView
    private var base64FireFightingEquipment: String? = null

    // First Aid Kit
    private lateinit var ivFirstAidKitPreview: ImageView
    private var base64FirstAidKit: String? = null

    private val photoUploadButtons: Map<Int, String> = mapOf(

        //        ITLAB




        (R.id.btnITLTypeofRoofItLab to "itltypeofroofitlab"),
        (R.id.btnITLFalseCellingProvide to "itlfalsecellingprovide"),
        (R.id.btnITLHeightOfCelling to "itlheightofcelling"),
        (R.id.btnITLVentilationAreaInSqFt to "itlventilationareainsqft"),
        (R.id.btnITLSoundLevelAsPerSpecifications to "itlsoundlevelasperspecifications"),
        (R.id.btnITLSoundLevelInDb to "itlsoundlevelindb"),
        (R.id.btnITLwhether_all_the_academic to "itlwhether_all_the_academic"),
        (R.id.btnITLAcademicRoomInformationBoard to "itlacadmicroominformationboard"),
        (R.id.btnITLInternalSignage to "itlinternalsignage"),
        (R.id.btnITLCctcCamerasWithAudioFacility to "itlcctccameraswithaudiofacility"),
        (R.id.btnITLLanEnabledComputersInNo to "itllanenabledcomputersinno"),
        (R.id.btnITLInternetConnections to "itlinternetconnections"),
        (R.id.btnITLDoAllComputersHaveTypingTutor to "itldoallcomputershavetypingtutor"),
        (R.id.btnITLTablets to "itltablets"),
        (R.id.btnITLStoolsChairs to "itlstoolschairs"),
        (R.id.btnITLTrainerChair to "itltrainerchair"),
        (R.id.btnITLTrainerTable to "itltrainertable"),
        (R.id.btnITLLightsInNo to "itllightsinno"),
        (R.id.btnITLFansInNo to "itlfansinno"),
        (R.id.btnITLElectricaPowerBackUpForThRoom to "itlelectricapowerbackupforthroom"),
        (R.id.btnITLItLabPhotograph to "itlitlabphotograph"),
        (R.id.btnITLLDoes_the_room_has to "itlldoes_the_room_has"),

//        Office Cum(Counselling room)  Ajit Ranjan
        (R.id.btnUploadOfficeRoomPhotograph to "btnuploadofficeroomphotograph"),
        (R.id.btnUploadOfficeCumTypeofRoofItLab to "btnuploadofficecumtypeofroofitlab"),
        (R.id.btnOfficeCumFalseCellingProvide to "btnofficecumfalsecellingprovide"),
        (R.id.btnOfficeCumHeightOfCelling to "btnofficecumheightofcelling"),
        (R.id.btnOfficeCumSplaceforSecuringDoc to "btnofficecumsplaceforsecuringdoc"),
        (R.id.btnOfficeCumAnOfficeTableNo to "btnofficecumanofficetableno"),
        (R.id.btnOfficeCumChairs to "btnofficecumchairs"),
        (R.id.btnOfficeCumTableOfofficeCumpter to "btnofficecumtableofofficecumpter"),
        (R.id.btnOfficeCumPrinterCumScannerInNo to "btnofficecumprintercumscannerinno"),
        (R.id.btnOfficeCumDigitalCameraInNo to "btnofficecumdigitalcamerainno"),
        (R.id.btnOfficeCumElectricialPowerBackup to "btnofficecumelectricialpowerbackup"),

//        Reception Area   Ajit Ranjan
        (R.id.btnReceptionAreaPhotogragh to "btnReceptionAreaPhotogragh"),



        (R.id.btnCounsellingRoomAreaPhotograph to "btnCounsellingRoomAreaPhotograph"),






//        Office  Room  Ajit Ranjan

        (R.id.btnOROfficeRoomPhotograph to "btnOROfficeRoomPhotograph"),
        (R.id.btnORTypeofRoofItLab to "btnORTypeofRoofItLab"),
        (R.id.btnORFalseCellingProvide to "btnORFalseCellingProvide"),
        (R.id.btnORHeightOfCelling to "btnORHeightOfCelling"),
        (R.id.btnORSplaceforSecuringDoc to "btnORSplaceforSecuringDoc"),
        (R.id.btnORAnOfficeTableNo to "btnORAnOfficeTableNo"),
        (R.id.btnORChairs to "btnORChairs"),
        (R.id.btnORTableOfofficeCumpter to "btnORTableOfofficeCumpter"),
        (R.id.btnORPrinterCumScannerInNo to "btnORPrinterCumScannerInNo"),
        (R.id.btnORDigitalCameraInNo to "btnORDigitalCameraInNo"),
        (R.id.btnORElectricialPowerBackup to "btnORElectricialPowerBackup"),


//        IT Come Domain Lab Ajit Ranjan

        (R.id.btnITCDLTypeofRoofItLab to "btnITCDLTypeofRoofItLab"),
        (R.id.btnITCDLFalseCellingProvide to "btnITCDLFalseCellingProvide"),
        (R.id.btnITCDLHeightOfCelling to "btnITCDLHeightOfCelling"),
        (R.id.btnITCDLVentilationAreaInSqFt to "btnITCDLVentilationAreaInSqFt"),
        (R.id.btnITCDLabSoundLevelAsPerSpecifications to "btnITCDLabSoundLevelAsPerSpecifications"),
        (R.id.btnITCDLabSoundLevelInDb to "btnITCDLabSoundLevelInDb"),
        (R.id.btnITDLwhether_all_the_academic to "btnITDLwhether_all_the_academic"),
        (R.id.btnITCDLAcademicRoomInformationBoard to "btnITCDLAcademicRoomInformationBoard"),
        (R.id.btnITCDLInternalSignage to "btnITCDLInternalSignage"),
        (R.id.btnITCDLCctcCamerasWithAudioFacility to "btnITCDLCctcCamerasWithAudioFacility"),
        (R.id.btnITCDLLanEnabledComputersInNo to "btnITCDLLanEnabledComputersInNo"),
        (R.id.btnITCDLInternetConnections to "btnITCDLInternetConnections"),
        (R.id.btnITCDLTrainerChair to "btnITCDLTrainerChair"),
        (R.id.btnITCDLTablets to "btnITCDLTablets"),
        (R.id.btnITCDLTrainerTable to "btnITCDLTrainerTable"),
        (R.id.btnITCDLLightsInNo to "btnITCDLLightsInNo"),
        (R.id.btnITCDLFansInNo to "btnITCDLFansInNo"),
        (R.id.btnITCDLElectricaPowerBackUpForThRoom to "btnITCDLElectricaPowerBackUpForThRoom"),
        (R.id.btnITCDLItLabPhotograph to "btnITCDLItLabPhotograph"),
        (R.id.btnITCDLListofDomain to "btnITCDLListofDomain"),
        (R.id.btnITCDLDoes_the_room_has to "btnITCDLDoes_the_room_has"),
        (R.id.btnITCDLDoAllComputersHaveTypingTutor to "btnITCDLDoAllComputersHaveTypingTutor"),
        (R.id.btnITCDLStoolsChairs to "btnITCDLStoolsChairs"),

//        Theory Cum IT Lab Ajit Ranjan Click On Button
        (R.id.btnTCILListofDomain to "btnTCILListofDomain"),
        (R.id.btnTCILTypeofRoofItLab to "btnTCILTypeofRoofItLab"),
        (R.id.btnTCILFalseCellingProvide to "btnTCILFalseCellingProvide"),
        (R.id.btnTCILHeightOfCelling to "btnTCILHeightOfCelling"),
        (R.id.btnTCILVentilationAreaInSqFt to "btnTCILVentilationAreaInSqFt"),
        (R.id.btnTCILSoundLevelAsPerSpecifications to "btnTCILSoundLevelAsPerSpecifications"),
        (R.id.btnTCILSoundLevelInDb to "btnTCILSoundLevelInDb"),
        (R.id.btnTCILwhether_all_the_academic to "btnTCILwhether_all_the_academic"),
        (R.id.btnTCILAcademicRoomInformationBoard to "btnTCILAcademicRoomInformationBoard"),
        (R.id.btnTCILInternalSignage to "btnTCILInternalSignage"),
        (R.id.btnTCILCctcCamerasWithAudioFacility to "btnTCILCctcCamerasWithAudioFacility"),
        (R.id.btnTCILLanEnabledComputersInNo to "btnTCILLanEnabledComputersInNo"),
        (R.id.btnTCILInternetConnections to "btnTCILInternetConnections"),
        (R.id.btnTCILDoAllComputersHaveTypingTutor to "btnTCILDoAllComputersHaveTypingTutor"),
        (R.id.btnTCILTablets to "btnTCILTablets"),
        (R.id.btnTCILStoolsChairs to "btnTCILStoolsChairs"),
        (R.id.btnTCILTrainerChair to "btnTCILTrainerChair"),
        (R.id.btnTCILTrainerTable to "btnTCILTrainerTable"),
        (R.id.btnTCILLightsInNo to "btnTCILLightsInNo"),
        (R.id.btnTCILFansInNo to "btnTCILFansInNo"),
        (R.id.btnTCILElectricaPowerBackUpForThRoom to "btnTCILElectricaPowerBackUpForThRoom"),
        (R.id.btnTCILTheoryCumItLabPhotogragh to "btnTCILTheoryCumItLabPhotogragh"),
        (R.id.btnTCILDoes_the_room_has to "btnTCILDoes_the_room_has"),

//        Theory Cum Domain Lab Ajit Ranjan Click On Button
        (R.id.btnTCDLTypeofRoofItLab to "btnTCDLTypeofRoofItLab"),
        (R.id.btnTCDLFalseCellingProvide to "btnTCDLFalseCellingProvide"),
        (R.id.btnTCDLHeightOfCelling to "btnTCDLHeightOfCelling"),
        (R.id.btnTCDLVentilationAreaInSqFt to "btnTCDLVentilationAreaInSqFt"),
        (R.id.btnTCDLSoundLevelInDb to "btnTCDLSoundLevelInDb"),
        (R.id.btnTCDLwhether_all_the_academic to "btnTCDLwhether_all_the_academic"),
        (R.id.btnTCDLAcademicRoomInformationBoard to "btnTCDLAcademicRoomInformationBoard"),
        (R.id.btnTCDLInternalSignage to "btnTCDLInternalSignage"),
        (R.id.btnTCDLCctcCamerasWithAudioFacility to "btnTCDLCctcCamerasWithAudioFacility"),
        (R.id.btnTCDLLcdDigitalProjector to "btnTCDLLcdDigitalProjector"),
        (R.id.btnTCDLChairForCandidatesInNo to "btnTCDLChairForCandidatesInNo"),
        (R.id.btnTCDLUploaadTrainerChair to "btnTCDLUploaadTrainerChair"),
        (R.id.btnTCDLTrainerTable to "btnTCDLTrainerTable"),
        (R.id.btnTCDLWritingBoard to "btnTCDLWritingBoard"),
        (R.id.btnTCDLLightsInNo to "btnTCDLLightsInNo"),
        (R.id.btnTCDLFansInNo to "btnTCDLFansInNo"),
        (R.id.btnTCDLElectricaPowerBackUpForThRoom to "btnTCDLElectricaPowerBackUpForThRoom"),
        (R.id.btnTCDLListofDomain to "btnTCDLListofDomain"),
        (R.id.btnTCDLDomainLabPhotogragh to "btnTCDLDomainLabPhotogragh"),
        (R.id.btnTCDLDoes_the_room_has to "btnTCDLDoes_the_room_has"),

//        Domain Lab Ajit Ranjan Click On Button
        (R.id.btnDLTypeofRoofItLab to "btnDLTypeofRoofItLab"),
        (R.id.btnDLFalseCellingProvide to "btnDLFalseCellingProvide"),
        (R.id.btnDLHeightOfCelling to "btnDLHeightOfCelling"),
        (R.id.btnDLVentilationAreaInSqFt to "btnDLVentilationAreaInSqFt"),
        (R.id.btnDLSoundLevelAsPerSpecifications to "btnDLSoundLevelAsPerSpecifications"),
        (R.id.btnDLSoundLevelInDb to "btnDLSoundLevelInDb"),
        (R.id.btnDLwhether_all_the_academic to "btnDLwhether_all_the_academic"),
        (R.id.btnDLAcademicRoomInformationBoard to "btnDLAcademicRoomInformationBoard"),
        (R.id.btnDLInternalSignage to "btnDLInternalSignage"),
        (R.id.btnDLCctcCamerasWithAudioFacility to "btnDLCctcCamerasWithAudioFacility"),
        (R.id.btnDLLcdDigitalProjector to "btnDLLcdDigitalProjector"),
        (R.id.btnDLChairForCandidatesInNo to "btnDLChairForCandidatesInNo"),
        (R.id.btnDLUploaadTrainerChair to "btnDLUploaadTrainerChair"),
        (R.id.btnDLTrainerTable to "btnDLTrainerTable"),
        (R.id.btnDLWritingBoard to "btnDLWritingBoard"),
        (R.id.btnDLLightsInNo to "btnDLLightsInNo"),
        (R.id.btnDLFansInNo to "btnDLFansInNo"),
        (R.id.btnDLElectricaPowerBackUpForThRoom to "btnDLElectricaPowerBackUpForThRoom"),
        (R.id.btnDLILListofDomain to "btnDLILListofDomain"),
        (R.id.btnDLDomainLabPhotogragh to "btnDLDomainLabPhotogragh"),
        (R.id.btnDLDoes_the_room_has to "btnDLDoes_the_room_has"),






        (R.id.btnTCRTypeofRoofItLab to "btnTCRTypeofRoofItLab"),
        (R.id.btnTCRFalseCellingProvide to "btnTCRFalseCellingProvide"),
        (R.id.btnTCRHeightOfCelling to "btnTCRHeightOfCelling"),
        (R.id.btnTCRVentilationAreaInSqFt to "btnTCRVentilationAreaInSqFt"),
        (R.id.btnTCRSoundLevelInDb to "btnTCRSoundLevelInDb"),
        (R.id.btnTCRwhether_all_the_academic to "btnTCRwhether_all_the_academic"),
        (R.id.btnTCRAcademicRoomInformationBoard to "btnTCRAcademicRoomInformationBoard"),
        (R.id.btnTCRCctcCamerasWithAudioFacility to "btnTCRCctcCamerasWithAudioFacility"),




        // CCTV
        (R.id.btnUploadMonitorPhoto to "monitor"),
        R.id.btnUploadConformancePhoto to "conformance",
        R.id.btnUploadStoragePhoto to "storage",
        R.id.btnUploadDVRPhoto to "dvr",

        // Electrical
        R.id.btnUploadSwitchBoards to "switchBoard",
        R.id.btnUploadSecuringWires to "WireSecurity",

        // General
        R.id.btnUploadLeaSkageProof to "leakage",
        R.id.btnUploadProtectionStairs to "stairs",

        //Signages info boards
        R.id.btnUploadTrainingCentreNameBoard to "tcNameBoard",
        R.id.btnUploadActivitySummaryBoard to "activityAchievementBoard",
        R.id.btnUploadEntitlementBoard to "studentEntitlementBoard",
        R.id.btnUploadImportantContacts to "contactDetailBoard",
        R.id.btnUploadBasicInfoBoard to "basicInfoBoard",
        R.id.btnUploadCodeOfConductBoard to "codeConductBoard",
        R.id.btnUploadAttendanceSummaryBoard to "studentAttendanceBoard",

        // Support infra
        R.id.btnUploadFirstAidKit to "FirstAidKit",
        R.id.btnUploadFireFightingEquipment to "FireFightingEquipment",
        R.id.btnUploadSafeDrinkingWater to "SafeDrinkingWater",

        //  desc Other areas
        R.id.btnUploadProof to "proofUpload",
        R.id.btnUploadCirculationProof to "circulationProof",
        R.id.btnUploadParkingProof to "parking",
        R.id.btnUploadOpenSpaceProof to "openSpaceProof",

        // Common Equipment
        R.id.btnUploadPowerBackup to "powerBackup",
        R.id.btnUploadBiometricDevices to "biometricDevices",
        R.id.btnUploadCCTV to "cctv",
        R.id.btnUploadDocumentStorage to "documentStorage",
        R.id.btnUploadPrinterScanner to "printerScanner",
        R.id.btnUploadDigitalCamera to "digitalCamera",
        R.id.btnUploadGrievanceRegister to "grievanceRegister",
        R.id.btnUploadMinimumEquipment to "minimumEquipment",
        R.id.btnUploadDirectionBoards to "directionBoards",

        // Wash basin upload buttons
        R.id.btnUploadProofMaleToilets to "maleToiletsProof",
        R.id.btnUploadProofMaleToiletsSignage to "maleToiletsSignageProof",
        R.id.btnUploadProofFemaleToilets to "femaleToiletsProof",
        R.id.btnUploadProofFemaleToiletsSignage to "femaleToiletsSignageProof",
        R.id.btnUploadProofMaleUrinals to "maleUrinalsProof",
        R.id.btnUploadProofMaleWashBasins to "maleWashBasinsProof",
        R.id.btnUploadProofFemaleWashBasins to "femaleWashBasinsProof",
        R.id.btnUploadProofOverheadTanks to "overheadTanksProof",
        R.id.btnUploadProofFlooring to "flooringProof",


//        ITLAB
        (R.id.btnITLTypeofRoofItLab to "itltypeofroofitlab"),
        (R.id.btnITLFalseCellingProvide to "itlfalsecellingprovide"),
        (R.id.btnITLHeightOfCelling to "itlheightofcelling"),
        (R.id.btnITLVentilationAreaInSqFt to "itlventilationareainsqft"),
        (R.id.btnITLSoundLevelAsPerSpecifications to "itlsoundlevelasperspecifications"),
        (R.id.btnITLSoundLevelInDb to "itlsoundlevelindb"),
        (R.id.btnITLwhether_all_the_academic to "itlwhether_all_the_academic"),
        (R.id.btnITLAcademicRoomInformationBoard to "itlacadmicroominformationboard"),
        (R.id.btnITLInternalSignage to "itlinternalsignage"),
        (R.id.btnITLCctcCamerasWithAudioFacility to "itlcctccameraswithaudiofacility"),
        (R.id.btnITLLanEnabledComputersInNo to "itllanenabledcomputersinno"),
        (R.id.btnITLInternetConnections to "itlinternetconnections"),
        (R.id.btnITLDoAllComputersHaveTypingTutor to "itldoallcomputershavetypingtutor"),
        (R.id.btnITLTablets to "itltablets"),
        (R.id.btnITLStoolsChairs to "itlstoolschairs"),
        (R.id.btnITLTrainerChair to "itltrainerchair"),
        (R.id.btnITLTrainerTable to "itltrainertable"),
        (R.id.btnITLLightsInNo to "itllightsinno"),
        (R.id.btnITLFansInNo to "itlfansinno"),
        (R.id.btnITLElectricaPowerBackUpForThRoom to "itlelectricapowerbackupforthroom"),
        (R.id.btnITLItLabPhotograph to "itlitlabphotograph"),
        (R.id.btnITLLDoes_the_room_has to "itlldoes_the_room_has"),

//        Office Cum(Counselling room)  Ajit Ranjan
        (R.id.btnUploadOfficeRoomPhotograph to "btnuploadofficeroomphotograph"),
        (R.id.btnUploadOfficeCumTypeofRoofItLab to "btnuploadofficecumtypeofroofitlab"),
        (R.id.btnOfficeCumFalseCellingProvide to "btnofficecumfalsecellingprovide"),
        (R.id.btnOfficeCumHeightOfCelling to "btnofficecumheightofcelling"),
        (R.id.btnOfficeCumSplaceforSecuringDoc to "btnofficecumsplaceforsecuringdoc"),
        (R.id.btnOfficeCumAnOfficeTableNo to "btnofficecumanofficetableno"),
        (R.id.btnOfficeCumChairs to "btnofficecumchairs"),
        (R.id.btnOfficeCumTableOfofficeCumpter to "btnofficecumtableofofficecumpter"),
        (R.id.btnOfficeCumPrinterCumScannerInNo to "btnofficecumprintercumscannerinno"),
        (R.id.btnOfficeCumDigitalCameraInNo to "btnofficecumdigitalcamerainno"),
        (R.id.btnOfficeCumElectricialPowerBackup to "btnofficecumelectricialpowerbackup"),

//        Reception Area   Ajit Ranjan
        (R.id.btnReceptionAreaPhotogragh to "btnReceptionAreaPhotogragh"),



        (R.id.btnCounsellingRoomAreaPhotograph to "btnCounsellingRoomAreaPhotograph"),






//        Office  Room  Ajit Ranjan

        (R.id.btnOROfficeRoomPhotograph to "btnOROfficeRoomPhotograph"),
        (R.id.btnORTypeofRoofItLab to "btnORTypeofRoofItLab"),
        (R.id.btnORFalseCellingProvide to "btnORFalseCellingProvide"),
        (R.id.btnORHeightOfCelling to "btnORHeightOfCelling"),
        (R.id.btnORSplaceforSecuringDoc to "btnORSplaceforSecuringDoc"),
        (R.id.btnORAnOfficeTableNo to "btnORAnOfficeTableNo"),
        (R.id.btnORChairs to "btnORChairs"),
        (R.id.btnORTableOfofficeCumpter to "btnORTableOfofficeCumpter"),
        (R.id.btnORPrinterCumScannerInNo to "btnORPrinterCumScannerInNo"),
        (R.id.btnORDigitalCameraInNo to "btnORDigitalCameraInNo"),
        (R.id.btnORElectricialPowerBackup to "btnORElectricialPowerBackup"),


//        IT Come Domain Lab Ajit Ranjan

        (R.id.btnITCDLTypeofRoofItLab to "btnITCDLTypeofRoofItLab"),
        (R.id.btnITCDLFalseCellingProvide to "btnITCDLFalseCellingProvide"),
        (R.id.btnITCDLHeightOfCelling to "btnITCDLHeightOfCelling"),
        (R.id.btnITCDLVentilationAreaInSqFt to "btnITCDLVentilationAreaInSqFt"),
        (R.id.btnITCDLabSoundLevelAsPerSpecifications to "btnITCDLabSoundLevelAsPerSpecifications"),
        (R.id.btnITCDLabSoundLevelInDb to "btnITCDLabSoundLevelInDb"),
        (R.id.btnITDLwhether_all_the_academic to "btnITDLwhether_all_the_academic"),
        (R.id.btnITCDLAcademicRoomInformationBoard to "btnITCDLAcademicRoomInformationBoard"),
        (R.id.btnITCDLInternalSignage to "btnITCDLInternalSignage"),
        (R.id.btnITCDLCctcCamerasWithAudioFacility to "btnITCDLCctcCamerasWithAudioFacility"),
        (R.id.btnITCDLLanEnabledComputersInNo to "btnITCDLLanEnabledComputersInNo"),
        (R.id.btnITCDLInternetConnections to "btnITCDLInternetConnections"),
        (R.id.btnITCDLTrainerChair to "btnITCDLTrainerChair"),
        (R.id.btnITCDLTablets to "btnITCDLTablets"),
        (R.id.btnITCDLTrainerTable to "btnITCDLTrainerTable"),
        (R.id.btnITCDLLightsInNo to "btnITCDLLightsInNo"),
        (R.id.btnITCDLFansInNo to "btnITCDLFansInNo"),
        (R.id.btnITCDLElectricaPowerBackUpForThRoom to "btnITCDLElectricaPowerBackUpForThRoom"),
        (R.id.btnITCDLItLabPhotograph to "btnITCDLItLabPhotograph"),
        (R.id.btnITCDLListofDomain to "btnITCDLListofDomain"),
        (R.id.btnITCDLDoes_the_room_has to "btnITCDLDoes_the_room_has"),
        (R.id.btnITCDLDoAllComputersHaveTypingTutor to "btnITCDLDoAllComputersHaveTypingTutor"),
        (R.id.btnITCDLStoolsChairs to "btnITCDLStoolsChairs"),

//        Theory Cum IT Lab Ajit Ranjan Click On Button
        (R.id.btnTCILListofDomain to "btnTCILListofDomain"),
        (R.id.btnTCILTypeofRoofItLab to "btnTCILTypeofRoofItLab"),
        (R.id.btnTCILFalseCellingProvide to "btnTCILFalseCellingProvide"),
        (R.id.btnTCILHeightOfCelling to "btnTCILHeightOfCelling"),
        (R.id.btnTCILVentilationAreaInSqFt to "btnTCILVentilationAreaInSqFt"),
        (R.id.btnTCILSoundLevelAsPerSpecifications to "btnTCILSoundLevelAsPerSpecifications"),
        (R.id.btnTCILSoundLevelInDb to "btnTCILSoundLevelInDb"),
        (R.id.btnTCILwhether_all_the_academic to "btnTCILwhether_all_the_academic"),
        (R.id.btnTCILAcademicRoomInformationBoard to "btnTCILAcademicRoomInformationBoard"),
        (R.id.btnTCILInternalSignage to "btnTCILInternalSignage"),
        (R.id.btnTCILCctcCamerasWithAudioFacility to "btnTCILCctcCamerasWithAudioFacility"),
        (R.id.btnTCILLanEnabledComputersInNo to "btnTCILLanEnabledComputersInNo"),
        (R.id.btnTCILInternetConnections to "btnTCILInternetConnections"),
        (R.id.btnTCILDoAllComputersHaveTypingTutor to "btnTCILDoAllComputersHaveTypingTutor"),
        (R.id.btnTCILTablets to "btnTCILTablets"),
        (R.id.btnTCILStoolsChairs to "btnTCILStoolsChairs"),
        (R.id.btnTCILTrainerChair to "btnTCILTrainerChair"),
        (R.id.btnTCILTrainerTable to "btnTCILTrainerTable"),
        (R.id.btnTCILLightsInNo to "btnTCILLightsInNo"),
        (R.id.btnTCILFansInNo to "btnTCILFansInNo"),
        (R.id.btnTCILElectricaPowerBackUpForThRoom to "btnTCILElectricaPowerBackUpForThRoom"),
        (R.id.btnTCILTheoryCumItLabPhotogragh to "btnTCILTheoryCumItLabPhotogragh"),
        (R.id.btnTCILDoes_the_room_has to "btnTCILDoes_the_room_has"),

//        Theory Cum Domain Lab Ajit Ranjan Click On Button
        (R.id.btnTCDLTypeofRoofItLab to "btnTCDLTypeofRoofItLab"),
        (R.id.btnTCDLFalseCellingProvide to "btnTCDLFalseCellingProvide"),
        (R.id.btnTCDLHeightOfCelling to "btnTCDLHeightOfCelling"),
        (R.id.btnTCDLVentilationAreaInSqFt to "btnTCDLVentilationAreaInSqFt"),
        (R.id.btnTCDLSoundLevelInDb to "btnTCDLSoundLevelInDb"),
        (R.id.btnTCDLwhether_all_the_academic to "btnTCDLwhether_all_the_academic"),
        (R.id.btnTCDLAcademicRoomInformationBoard to "btnTCDLAcademicRoomInformationBoard"),
        (R.id.btnTCDLInternalSignage to "btnTCDLInternalSignage"),
        (R.id.btnTCDLCctcCamerasWithAudioFacility to "btnTCDLCctcCamerasWithAudioFacility"),
        (R.id.btnTCDLLcdDigitalProjector to "btnTCDLLcdDigitalProjector"),
        (R.id.btnTCDLChairForCandidatesInNo to "btnTCDLChairForCandidatesInNo"),
        (R.id.btnTCDLUploaadTrainerChair to "btnTCDLUploaadTrainerChair"),
        (R.id.btnTCDLTrainerTable to "btnTCDLTrainerTable"),
        (R.id.btnTCDLWritingBoard to "btnTCDLWritingBoard"),
        (R.id.btnTCDLLightsInNo to "btnTCDLLightsInNo"),
        (R.id.btnTCDLFansInNo to "btnTCDLFansInNo"),
        (R.id.btnTCDLElectricaPowerBackUpForThRoom to "btnTCDLElectricaPowerBackUpForThRoom"),
        (R.id.btnTCDLListofDomain to "btnTCDLListofDomain"),
        (R.id.btnTCDLDomainLabPhotogragh to "btnTCDLDomainLabPhotogragh"),
        (R.id.btnTCDLDoes_the_room_has to "btnTCDLDoes_the_room_has"),

//        Domain Lab Ajit Ranjan Click On Button
        (R.id.btnDLTypeofRoofItLab to "btnDLTypeofRoofItLab"),
        (R.id.btnDLFalseCellingProvide to "btnDLFalseCellingProvide"),
        (R.id.btnDLHeightOfCelling to "btnDLHeightOfCelling"),
        (R.id.btnDLVentilationAreaInSqFt to "btnDLVentilationAreaInSqFt"),
        (R.id.btnDLSoundLevelAsPerSpecifications to "btnDLSoundLevelAsPerSpecifications"),
        (R.id.btnDLSoundLevelInDb to "btnDLSoundLevelInDb"),
        (R.id.btnDLwhether_all_the_academic to "btnDLwhether_all_the_academic"),
        (R.id.btnDLAcademicRoomInformationBoard to "btnDLAcademicRoomInformationBoard"),
        (R.id.btnDLInternalSignage to "btnDLInternalSignage"),
        (R.id.btnDLCctcCamerasWithAudioFacility to "btnDLCctcCamerasWithAudioFacility"),
        (R.id.btnDLLcdDigitalProjector to "btnDLLcdDigitalProjector"),
        (R.id.btnDLChairForCandidatesInNo to "btnDLChairForCandidatesInNo"),
        (R.id.btnDLUploaadTrainerChair to "btnDLUploaadTrainerChair"),
        (R.id.btnDLTrainerTable to "btnDLTrainerTable"),
        (R.id.btnDLWritingBoard to "btnDLWritingBoard"),
        (R.id.btnDLLightsInNo to "btnDLLightsInNo"),
        (R.id.btnDLFansInNo to "btnDLFansInNo"),
        (R.id.btnDLElectricaPowerBackUpForThRoom to "btnDLElectricaPowerBackUpForThRoom"),
        (R.id.btnDLILListofDomain to "btnDLILListofDomain"),
        (R.id.btnDLDomainLabPhotogragh to "btnDLDomainLabPhotogragh"),
        (R.id.btnDLDoes_the_room_has to "btnDLDoes_the_room_has"),






        (R.id.btnTCRTypeofRoofItLab to "btnTCRTypeofRoofItLab"),
        (R.id.btnTCRFalseCellingProvide to "btnTCRFalseCellingProvide"),
        (R.id.btnTCRHeightOfCelling to "btnTCRHeightOfCelling"),
        (R.id.btnTCRVentilationAreaInSqFt to "btnTCRVentilationAreaInSqFt"),
        (R.id.btnTCRSoundLevelInDb to "btnTCRSoundLevelInDb"),
        (R.id.btnTCRwhether_all_the_academic to "btnTCRwhether_all_the_academic"),
        (R.id.btnTCRAcademicRoomInformationBoard to "btnTCRAcademicRoomInformationBoard"),
        (R.id.btnTCRCctcCamerasWithAudioFacility to "btnTCRCctcCamerasWithAudioFacility"),

        (R.id.btnTCRLcdDigitalProjector to "btnTCRLcdDigitalProjector"),
        (R.id.btnTCRChairForCandidatesInNo to "btnTCRChairForCandidatesInNo"),
        (R.id.btnTCRTrainerChair to "btnTCRTrainerChair"),
        (R.id.btnTCRTrainerTable to "btnTCRTrainerTable"),
        (R.id.btnTCRWritingBoard to "btnTCRWritingBoard"),
        (R.id.btnTCRLightsInNo to "btnTCRLightsInNo"),
        (R.id.btnTCRFansInNo to "btnTCRFansInNo"),
        (R.id.btnTCRElectricaPowerBackUpForThRoom to "btnTCRElectricaPowerBackUpForThRoom"),
        (R.id.btnTCRDomainLabPhotogragh to "btnTCRDomainLabPhotogragh"),
        (R.id.btnTCRDoes_the_room_has to "btnTCRDoes_the_room_has"),
        (R.id.btnTCRInternalSignage to "btnTCRInternalSignage"),




        )

    // Final Submit Button
    private lateinit var btnSubmitFinal: Button

    private fun setupPhotoUploadButtons(view: View) {
        photoUploadButtons.forEach { (buttonId, photoTarget) ->
            view.findViewById<Button>(buttonId).setOnClickListener {
                currentPhotoTarget = photoTarget
                checkAndLaunchCamera()
            }
        }
    }

    private fun <T : View> View.bindView(id: Int): T = findViewById(id)

    // Permission request launcher
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
                        "monitor" -> {
                            ivMonitorPreview.setImageURI(photoUri)
                            ivMonitorPreview.visibility = View.VISIBLE
                            base64MonitorFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "conformance" -> {
                            ivConformancePreview.setImageURI(photoUri)
                            ivConformancePreview.visibility = View.VISIBLE
                            base64ConformanceFile =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "storage" -> {
                            ivStoragePreview.setImageURI(photoUri)
                            ivStoragePreview.visibility = View.VISIBLE
                            base64StorageFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "dvr" -> {
                            ivDVRPreview.setImageURI(photoUri)
                            ivDVRPreview.visibility = View.VISIBLE
                            base64DVRFile = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "switchBoard" -> {
                            ivSwitchBoardPreview.setImageURI(photoUri)
                            ivSwitchBoardPreview.visibility = View.VISIBLE
                            base64SwitchBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "WireSecurity" -> {
                            ivWireSecurityPreview.setImageURI(photoUri)
                            ivWireSecurityPreview.visibility = View.VISIBLE
                            base64WireSecurityImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "leakage" -> {
                            ivLeakagePreview.setImageURI(photoUri)
                            ivLeakagePreview.visibility = View.VISIBLE
                            base64LeakageImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "stairs" -> {
                            ivStairsPreview.setImageURI(photoUri)
                            ivStairsPreview.visibility = View.VISIBLE
                            base64StairsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "tcNameBoard" -> {
                            ivTcNameBoardPreview.setImageURI(photoUri)
                            ivTcNameBoardPreview.visibility = View.VISIBLE
                            base64TcNameBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "activityAchievementBoard" -> {
                            ivActivityAchievementBoardPreview.setImageURI(photoUri)
                            ivActivityAchievementBoardPreview.visibility = View.VISIBLE
                            base64ActivityAchievementBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "studentEntitlementBoard" -> {
                            ivStudentEntitlementBoardPreview.setImageURI(photoUri)
                            ivStudentEntitlementBoardPreview.visibility = View.VISIBLE
                            base64StudentEntitlementBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "contactDetailBoard" -> {
                            ivContactDetailBoardPreview.setImageURI(photoUri)
                            ivContactDetailBoardPreview.visibility = View.VISIBLE
                            base64ContactDetailBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "basicInfoBoard" -> {
                            ivBasicInfoBoardPreview.setImageURI(photoUri)
                            ivBasicInfoBoardPreview.visibility = View.VISIBLE
                            base64BasicInfoBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "codeConductBoard" -> {
                            ivCodeConductBoardPreview.setImageURI(photoUri)
                            ivCodeConductBoardPreview.visibility = View.VISIBLE
                            base64CodeConductBoardImage =
                                AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "studentAttendanceBoard" -> {
                            ivStudentAttendanceBoardPreview.setImageURI(photoUri)
                            ivStudentAttendanceBoardPreview.visibility = View.VISIBLE
                            base64StudentAttendanceBoardImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "SafeDrinkingWater" -> {
                            ivSafeDrinkingWaterPreview.setImageURI(photoUri)
                            ivSafeDrinkingWaterPreview.visibility = View.VISIBLE
                            base64SafeDrinkingWater = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }

                        "FireFightingEquipment" -> {
                            ivFireFightingEquipmentPreview.setImageURI(photoUri)
                            ivFireFightingEquipmentPreview.visibility = View.VISIBLE
                            base64FireFightingEquipment =
                                AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }

                        "FirstAidKit" -> {
                            ivFirstAidKitPreview.setImageURI(photoUri)
                            ivFirstAidKitPreview.visibility = View.VISIBLE
                            base64FirstAidKit = AppUtil.imageUriToBase64(context = requireContext(), photoUri)
                        }
                        "powerBackup" -> {
                            ivPowerBackupPreview.setImageURI(photoUri)
                            ivPowerBackupPreview.visibility = View.VISIBLE
                            base64PowerBackupImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "biometricDevices" -> {
                            ivBiometricDevicesPreview.setImageURI(photoUri)
                            ivBiometricDevicesPreview.visibility = View.VISIBLE
                            base64BiometricDevices = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "cctv" -> {
                            ivCCTVPreview.setImageURI(photoUri)
                            ivCCTVPreview.visibility = View.VISIBLE
                            base64CCTVImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "documentStorage" -> {
                            ivDocumentStoragePreview.setImageURI(photoUri)
                            ivDocumentStoragePreview.visibility = View.VISIBLE
                            base64DocumentStorageImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "printerScanner" -> {
                            ivPrinterScannerPreview.setImageURI(photoUri)
                            ivPrinterScannerPreview.visibility = View.VISIBLE
                            base64PrinterScanner = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "digitalCamera" -> {
                            ivDigitalCameraPreview.setImageURI(photoUri)
                            ivDigitalCameraPreview.visibility = View.VISIBLE
                            base64DigitalCamera = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "grievanceRegister" -> {
                            ivGrievanceRegisterPreview.setImageURI(photoUri)
                            ivGrievanceRegisterPreview.visibility = View.VISIBLE
                            base64GrievanceRegisterImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "minimumEquipment" -> {
                            ivMinimumEquipmentPreview.setImageURI(photoUri)
                            ivMinimumEquipmentPreview.visibility = View.VISIBLE
                            base64MinimumEquipmentImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "directionBoards" -> {
                            ivDirectionBoardsPreview.setImageURI(photoUri)
                            ivDirectionBoardsPreview.visibility = View.VISIBLE
                            base64DirectionBoardsImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "maleToiletsProof" -> {
                            try{
                                ivPreviewMaleToiletsProof.setImageURI(photoUri)
                                ivPreviewMaleToiletsProof.visibility = View.VISIBLE
                                base64ProofMaleToilets = AppUtil.imageUriToBase64(requireContext(), photoUri)
                            }catch (e: Exception){
                                Log.e("ImagePreview", "Error in maleToiletsProof", e)
                            }

                        }
                        "maleToiletsSignageProof" -> {
                            ivPreviewMaleToiletsSignageProof.setImageURI(photoUri)
                            ivPreviewMaleToiletsSignageProof.visibility = View.VISIBLE
                            base64ProofMaleToiletsSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "femaleToiletsProof" -> {
                            ivPreviewFemaleToiletsProof.setImageURI(photoUri)
                            ivPreviewFemaleToiletsProof.visibility = View.VISIBLE
                            base64ProofFemaleToilets = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "femaleToiletsSignageProof" -> {
                            ivPreviewFemaleToiletsSignageProof.setImageURI(photoUri)
                            ivPreviewFemaleToiletsSignageProof.visibility = View.VISIBLE
                            base64ProofFemaleToiletsSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "maleUrinalsProof" -> {
                            ivPreviewMaleUrinalsProof.setImageURI(photoUri)
                            ivPreviewMaleUrinalsProof.visibility = View.VISIBLE
                            base64ProofMaleUrinals = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "maleWashBasinsProof" -> {
                            ivPreviewMaleWashBasinsProof.setImageURI(photoUri)
                            ivPreviewMaleWashBasinsProof.visibility = View.VISIBLE
                            base64ProofMaleWashBasins = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "femaleWashBasinsProof" -> {
                            ivPreviewFemaleWashBasinsProof.setImageURI(photoUri)
                            ivPreviewFemaleWashBasinsProof.visibility = View.VISIBLE
                            base64ProofFemaleWashBasins = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "overheadTanksProof" -> {
                            ivPreviewOverheadTanksProof.setImageURI(photoUri)
                            ivPreviewOverheadTanksProof.visibility = View.VISIBLE
                            base64ProofOverheadTanks = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "flooringProof" -> {
                            ivPreviewFlooringProof.setImageURI(photoUri)
                            ivPreviewFlooringProof.visibility = View.VISIBLE
                            base64ProofFlooring = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "proofUpload" -> {
                            ivProofPreview.setImageURI(photoUri)
                            ivProofPreview.visibility = View.VISIBLE
                            base64ProofUploadImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "circulationProof" -> {
                            ivCirculationProofPreview.setImageURI(photoUri)
                            ivCirculationProofPreview.visibility = View.VISIBLE
                            base64CirculationProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "openSpaceProof" -> {
                            ivOpenSpaceProofPreview.setImageURI(photoUri)
                            ivOpenSpaceProofPreview.visibility = View.VISIBLE
                            base64penSpaceProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                            // Consider renaming base64penSpaceProofImage to base64OpenSpaceProofImage for clarity
                        }

                        "parking" -> {
                            ivParkingProofPreview.setImageURI(photoUri)
                            ivParkingProofPreview.visibility = View.VISIBLE
                            base64ParkingSpaceProofImage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }






//                        Ajit Rnanjan Create page
//                            IT(LAB)

                        "itltypeofroofitlab" -> {
                            ivPreviewITLTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewITLTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewITLTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itlfalsecellingprovide" -> {
                            ivPreviewITLFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewITLFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofITLFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itlheightofcelling" -> {
                            ivPreviewITLHeightOfCelling.setImageURI(photoUri)
                            ivPreviewITLHeightOfCelling.visibility = View.VISIBLE
                            base64ProofITLHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itlventilationareainsqft" -> {
                            ivPreviewITLVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewITLVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofITLVentilationAreaInSqFt = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlsoundlevelasperspecifications" -> {
                            ivPreviewITLSoundLevelAsPerSpecifications.setImageURI(photoUri)
                            ivPreviewITLSoundLevelAsPerSpecifications.visibility = View.VISIBLE
                            base64ProofITLSoundLevelAsPerSpecifications = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlsoundlevelindb" -> {
                            ivPreviewITLSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewITLSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofITLSoundLevelInDb = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlwhether_all_the_academic" -> {
                            ivPreviewITLwhether_all_the_academic.setImageURI(photoUri)
                            ivPreviewITLwhether_all_the_academic.visibility = View.VISIBLE
                            base64ProofITLwhether_all_the_academic = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlacadmicroominformationboard" -> {
                            ivPreviewITLAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewITLAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofITLAcademicRoomInformationBoard = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlinternalsignage" -> {
                            ivPreviewITLInternalSignage.setImageURI(photoUri)
                            ivPreviewITLInternalSignage.visibility = View.VISIBLE
                            base64ProofITLInternalSignage = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlcctccameraswithaudiofacility" -> {
                            ivPreviewITLCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewITLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofITLCctcCamerasWithAudioFacility = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itllanenabledcomputersinno" -> {
                            ivPreviewITLLanEnabledComputersInNo.setImageURI(photoUri)
                            ivPreviewITLLanEnabledComputersInNo.visibility = View.VISIBLE
                            base64ProofITLLanEnabledComputersInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlinternetconnections" -> {
                            ivPreviewITLInternetConnections.setImageURI(photoUri)
                            ivPreviewITLInternetConnections.visibility = View.VISIBLE
                            base64ProofITLInternetConnections = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itldoallcomputershavetypingtutor" -> {
                            ivPreviewITLDoAllComputersHaveTypingTutor.setImageURI(photoUri)
                            ivPreviewITLDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
                            base64ProofITLDoAllComputersHaveTypingTutor = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itltablets" -> {
                            ivPreviewITLTablets.setImageURI(photoUri)
                            ivPreviewITLTablets.visibility = View.VISIBLE
                            base64ProofITLTablets = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itlstoolschairs" -> {
                            ivPreviewITLStoolsChairs.setImageURI(photoUri)
                            ivPreviewITLStoolsChairs.visibility = View.VISIBLE
                            base64ProofITLStoolsChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itltrainerchair" -> {
                            ivPreviewITLTrainerChair.setImageURI(photoUri)
                            ivPreviewITLTrainerChair.visibility = View.VISIBLE
                            base64ProofITLTrainerChair = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "itltrainertable" -> {
                            ivPreviewITLTrainerTable.setImageURI(photoUri)
                            ivPreviewITLTrainerTable.visibility = View.VISIBLE
                            base64ProofITLTrainerTable = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itllightsinno" -> {
                            ivPreviewITLLightsInNo.setImageURI(photoUri)
                            ivPreviewITLLightsInNo.visibility = View.VISIBLE
                            base64ProofITLLightsInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlfansinno" -> {
                            ivPreviewITLFansInNo.setImageURI(photoUri)
                            ivPreviewITLFansInNo.visibility = View.VISIBLE
                            base64ProofITLFansInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlelectricapowerbackupforthroom" -> {
                            ivPreviewITLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                            ivPreviewITLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                            base64ProofITLElectricaPowerBackUpForThRoom = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlitlabphotograph" -> {
                            ivPreviewITLItLabPhotograph.setImageURI(photoUri)
                            ivPreviewITLItLabPhotograph.visibility = View.VISIBLE
                            base64ProofITLItLabPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "itlldoes_the_room_has" -> {
                            ivPreviewITLDoes_the_room_has.setImageURI(photoUri)
                            ivPreviewITLDoes_the_room_has.visibility = View.VISIBLE
                            base64ProofITLDoes_the_room_has = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
//                           Office Cum(Counselling room)


                        "btnuploadofficeroomphotograph" -> {
                            ivPreviewOfficeRoomPhotograph.setImageURI(photoUri)
                            ivPreviewOfficeRoomPhotograph.visibility = View.VISIBLE
                            base64ProofPreviewOfficeRoomPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnuploadofficecumtypeofroofitlab" -> {
                            ivPreviewOfficeCumTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewOfficeCumTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofOfficeCumTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnofficecumfalsecellingprovide" -> {
                            ivPreviewOfficeCumFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewOfficeCumFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofOfficeCumFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnofficecumheightofcelling" -> {
                            ivPreviewOfficeCumHeightOfCelling.setImageURI(photoUri)
                            ivPreviewOfficeCumHeightOfCelling.visibility = View.VISIBLE
                            base64ProofOfficeCumHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnofficecumsplaceforsecuringdoc" -> {
                            ivPreviewOfficeCumSplaceforSecuringDoc.setImageURI(photoUri)
                            ivPreviewOfficeCumSplaceforSecuringDoc.visibility = View.VISIBLE
                            base64ProofOfficeCumSplaceforSecuringDoc = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumanofficetableno" -> {
                            ivPreviewOfficeCumAnOfficeTableNo.setImageURI(photoUri)
                            ivPreviewOfficeCumAnOfficeTableNo.visibility = View.VISIBLE
                            base64ProofOfficeCumAnOfficeTableNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumchairs" -> {
                            ivPreviewOfficeCumChairs.setImageURI(photoUri)
                            ivPreviewOfficeCumChairs.visibility = View.VISIBLE
                            base64ProofOfficeCumChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumtableofofficecumpter" -> {
                            ivPreviewOfficeCumTableOfofficeCumpter.setImageURI(photoUri)
                            ivPreviewOfficeCumTableOfofficeCumpter.visibility = View.VISIBLE
                            base64ProofOfficeCumTableOfofficeCumpter = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumprintercumscannerinno" -> {
                            ivPreviewOfficeCumPrinterCumScannerInNo.setImageURI(photoUri)
                            ivPreviewOfficeCumPrinterCumScannerInNo.visibility = View.VISIBLE
                            base64ProofOfficeCumPrinterCumScannerInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumdigitalcamerainno" -> {
                            ivPreviewOfficeCumDigitalCameraInNo.setImageURI(photoUri)
                            ivPreviewOfficeCumDigitalCameraInNo.visibility = View.VISIBLE
                            base64ProofOfficeCumDigitalCameraInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnofficecumelectricialpowerbackup" -> {
                            ivPreviewOfficeCumElectricialPowerBackup.setImageURI(photoUri)
                            ivPreviewOfficeCumElectricialPowerBackup.visibility = View.VISIBLE
                            base64ProofOfficeCumElectricialPowerBackup = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
//                        ReceptionArea
                        "btnReceptionAreaPhotogragh" -> {
                            ivPreviewReceptionAreaPhotogragh.setImageURI(photoUri)
                            ivPreviewReceptionAreaPhotogragh.visibility = View.VISIBLE
                            base64ProofPreviewReceptionAreaPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnCounsellingRoomAreaPhotograph" -> {
                            ivPreviewCounsellingRoomAreaPhotograph.setImageURI(photoUri)
                            ivPreviewCounsellingRoomAreaPhotograph.visibility = View.VISIBLE
                            base64ProofPreviewCounsellingRoomPhotogragh = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }





//                        Office Room
                        "btnOROfficeRoomPhotograph" -> {
                            ivPreviewOROfficeRoomPhotograph.setImageURI(photoUri)
                            ivPreviewOROfficeRoomPhotograph.visibility = View.VISIBLE
                            base64ProofPreviewOROfficeRoomORPhotograph = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORTypeofRoofItLab" -> {
                            ivPreviewORTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewORTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofORTypeofRoofItLab = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORFalseCellingProvide" -> {
                            ivPreviewORFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewORFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofORFalseCellingProvide = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORHeightOfCelling" -> {
                            ivPreviewORHeightOfCelling.setImageURI(photoUri)
                            ivPreviewORHeightOfCelling.visibility = View.VISIBLE
                            base64ProofORHeightOfCelling = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORSplaceforSecuringDoc" -> {
                            ivPreviewORSplaceforSecuringDoc.setImageURI(photoUri)
                            ivPreviewORSplaceforSecuringDoc.visibility = View.VISIBLE
                            base64ProofORSplaceforSecuringDoc = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORAnOfficeTableNo" -> {
                            ivPreviewORAnOfficeTableNo.setImageURI(photoUri)
                            ivPreviewORAnOfficeTableNo.visibility = View.VISIBLE
                            base64ProofORAnOfficeTableNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORChairs" -> {
                            ivPreviewORChairs.setImageURI(photoUri)
                            ivPreviewORChairs.visibility = View.VISIBLE
                            base64ProofORChairs = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORTableOfofficeCumpter" -> {
                            ivPreviewORTableOfofficeCumpter.setImageURI(photoUri)
                            ivPreviewORTableOfofficeCumpter.visibility = View.VISIBLE
                            base64ProofORTableOfofficeCumpter = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnORPrinterCumScannerInNo" -> {
                            ivPreviewORPrinterCumScannerInNo.setImageURI(photoUri)
                            ivPreviewORPrinterCumScannerInNo.visibility = View.VISIBLE
                            base64ProofORPrinterCumScannerInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORDigitalCameraInNo" -> {
                            ivPreviewORDigitalCameraInNo.setImageURI(photoUri)
                            ivPreviewORDigitalCameraInNo.visibility = View.VISIBLE
                            base64ProofORDigitalCameraInNo = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnORElectricialPowerBackup" -> {
                            ivPreviewORElectricialPowerBackup.setImageURI(photoUri)
                            ivPreviewORElectricialPowerBackup.visibility = View.VISIBLE
                            base64ProofORElectricialPowerBackup = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnITCDLTypeofRoofItLab"->{

                            ivPreviewITCDLTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewITCDLTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewITCDLTypeofRoofItLab= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnITCDLFalseCellingProvide"->{

                            ivPreviewITCDLFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewITCDLFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofITCDLFalseCellingProvide= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnITCDLHeightOfCelling"->{

                            ivPreviewITCDLabHeightOfCelling.setImageURI(photoUri)
                            ivPreviewITCDLabHeightOfCelling.visibility = View.VISIBLE
                            base64ProofITCDLabHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLVentilationAreaInSqFt"->{

                            ivPreviewITCDLVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewITCDLVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofITCDLVentilationAreaInSqFt= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLabSoundLevelInDb"->{

                            ivPreviewITCDLabSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewITCDLabSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofITCDLabSoundLevelInDb= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITDLwhether_all_the_academic"->{

                            ivPreviewITCDLwhether_all_the_academic.setImageURI(photoUri)
                            ivPreviewITCDLwhether_all_the_academic.visibility = View.VISIBLE
                            base64ProofITCDLwhether_all_the_academic= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLAcademicRoomInformationBoard"->{

                            ivPreviewITCDLAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewITCDLAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofITCDLAcademicRoomInformationBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLInternalSignage"->{

                            ivPreviewITCDLInternalSignage.setImageURI(photoUri)
                            ivPreviewITCDLInternalSignage.visibility = View.VISIBLE
                            base64ProofITCDLInternalSignage= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLCctcCamerasWithAudioFacility"->{

                            ivPreviewITCDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewITCDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofITCDLCctcCamerasWithAudioFacility= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLLanEnabledComputersInNo"->{

                            ivPreviewITCDLLanEnabledComputersInNo.setImageURI(photoUri)
                            ivPreviewITCDLLanEnabledComputersInNo.visibility = View.VISIBLE
                            base64ProofITCDLLanEnabledComputersInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLInternetConnections"->{

                            ivPreviewITCDLInternetConnections.setImageURI(photoUri)
                            ivPreviewITCDLInternetConnections.visibility = View.VISIBLE
                            base64ProofITCDLInternetConnections= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLTrainerChair"->{

                            ivPreviewITCDLTrainerChair.setImageURI(photoUri)
                            ivPreviewITCDLTrainerChair.visibility = View.VISIBLE
                            base64ProofITCDLTrainerChair= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLTrainerTable"->{

                            ivPreviewITCDLTrainerTable.setImageURI(photoUri)
                            ivPreviewITCDLTrainerTable.visibility = View.VISIBLE
                            base64ProofITCDLTrainerTable= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLLightsInNo"->{

                            ivPreviewITCDLLightsInNo.setImageURI(photoUri)
                            ivPreviewITCDLLightsInNo.visibility = View.VISIBLE
                            base64ProofITCDLLightsInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLFansInNo"->{

                            ivPreviewITCDLFansInNo.setImageURI(photoUri)
                            ivPreviewITCDLFansInNo.visibility = View.VISIBLE
                            base64ProofITCDLFansInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLElectricaPowerBackUpForThRoom"->{

                            ivPreviewITCDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                            ivPreviewITCDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                            base64ProofITCDLElectricaPowerBackUpForThRoom= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLItLabPhotograph"->{

                            ivPreviewITCDLItLabPhotograph.setImageURI(photoUri)
                            ivPreviewITCDLItLabPhotograph.visibility = View.VISIBLE
                            base64ProofITCDLItLabPhotograph= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLListofDomain"->{

                            ivPreviewITCDLListofDomain.setImageURI(photoUri)
                            ivPreviewITCDLListofDomain.visibility = View.VISIBLE
                            base64ProofITCDLListofDomain= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLDoes_the_room_has"->{

                            ivPreviewITCDLDoes_the_room_has.setImageURI(photoUri)
                            ivPreviewITCDLDoes_the_room_has.visibility = View.VISIBLE
                            base64ProofITCDLDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLTablets"->{

                            ivPreviewITCDLTablets.setImageURI(photoUri)
                            ivPreviewITCDLTablets.visibility = View.VISIBLE
                            base64ProofITCDLTablets = AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLDoAllComputersHaveTypingTutor"->{

                            ivPreviewITCDLDoAllComputersHaveTypingTutor.setImageURI(photoUri)
                            ivPreviewITCDLDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
                            base64ProofITCDLDoAllComputersHaveTypingTutor= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnITCDLStoolsChairs"->{

                            ivPreviewITCDLStoolsChairs.setImageURI(photoUri)
                            ivPreviewITCDLStoolsChairs.visibility = View.VISIBLE
                            base64ProofITCDLStoolsChairs= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }









                        "btnTCILListofDomain"->{
                            ivPreviewTCILListofDomain.setImageURI(photoUri)
                            ivPreviewTCILListofDomain.visibility = View.VISIBLE
                            base64ProofPreviewTCILListofDomain= AppUtil.imageUriToBase64(requireContext(), photoUri)

                        }
                        "btnTCILTypeofRoofItLab"->{
                            ivPreviewTCILTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewTCILTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewTCILTypeofRoofItLab= AppUtil.imageUriToBase64(requireContext(), photoUri)

                        }
                        "btnTCILFalseCellingProvide"->{
                            ivPreviewTCILFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewTCILFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofPreviewTCILFalseCellingProvide= AppUtil.imageUriToBase64(requireContext(), photoUri)

                        }
                        "btnTCILFalseCellingProvide"->{
                            ivPreviewTCILHeightOfCelling.setImageURI(photoUri)
                            ivPreviewTCILHeightOfCelling.visibility = View.VISIBLE
                            base64ProofPreviewTCILHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnTCILHeightOfCelling"->{
                            ivPreviewTCILHeightOfCelling.setImageURI(photoUri)
                            ivPreviewTCILHeightOfCelling.visibility = View.VISIBLE
                            base64ProofPreviewTCILHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILVentilationAreaInSqFt"->{
                            ivPreviewTCILVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewTCILVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofPreviewTCILVentilationAreaInSqFt= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "btnTCILSoundLevelInDb"->{
                            ivPreviewTCILSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewTCILSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofPreviewTTCILSoundLevelInDb= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        } "btnTCILwhether_all_the_academic"->{
                        ivPreviewTCILwhether_all_the_academic.setImageURI(photoUri)
                        ivPreviewTCILwhether_all_the_academic.visibility = View.VISIBLE
                        base64ProofPreviewTCILwhether_all_the_academic= AppUtil.imageUriToBase64(requireContext(), photoUri)

                    }
                        "btnTCILAcademicRoomInformationBoard"->{
                            ivPreviewTCILAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewTCILAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofPreviewTCILAcademicRoomInformationBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILInternalSignage"->{
                            ivPreviewTCILInternalSignage.setImageURI(photoUri)
                            ivPreviewTCILInternalSignage.visibility = View.VISIBLE
                            base64ProofPreviewTCILInternalSignage= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILCctcCamerasWithAudioFacility"->{
                            ivPreviewTCILCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewTCILCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofPreviewTCILCctcCamerasWithAudioFacility= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILLanEnabledComputersInNo"->{
                            ivPreviewTCILLanEnabledComputersInNo.setImageURI(photoUri)
                            ivPreviewTCILLanEnabledComputersInNo.visibility = View.VISIBLE
                            base64ProofPreviewTCILLanEnabledComputersInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILInternetConnections"->{
                            ivPreviewTCILInternetConnections.setImageURI(photoUri)
                            ivPreviewTCILInternetConnections.visibility = View.VISIBLE
                            base64ProofPreviewTCILInternetConnections= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILDoAllComputersHaveTypingTutor"->{
                            ivPreviewTCILDoAllComputersHaveTypingTutor.setImageURI(photoUri)
                            ivPreviewTCILDoAllComputersHaveTypingTutor.visibility = View.VISIBLE
                            base64ProofPreviewTCILDoAllComputersHaveTypingTutor= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILTablets"->{
                            ivPreviewTCILTablets.setImageURI(photoUri)
                            ivPreviewTCILTablets.visibility = View.VISIBLE
                            base64ProofPreviewTCILTablets= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILStoolsChairs"->{
                            ivPreviewTCILStoolsChairs.setImageURI(photoUri)
                            ivPreviewTCILStoolsChairs.visibility = View.VISIBLE
                            base64ProofPreviewTCILStoolsChairs= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILTrainerChair"->{
                            ivPreviewTCILTrainerChair.setImageURI(photoUri)
                            ivPreviewTCILTrainerChair.visibility = View.VISIBLE
                            base64ProofPreviewTCILTrainerChair= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }"btnTCILTrainerTable"->{
                        ivPreviewTCILTrainerTable.setImageURI(photoUri)
                        ivPreviewTCILTrainerTable.visibility = View.VISIBLE
                        base64ProofPreviewTCILTrainerTable= AppUtil.imageUriToBase64(requireContext(), photoUri)

                    }"btnTCILLightsInNo"->{
                        ivPreviewTCILLightsInNo.setImageURI(photoUri)
                        ivPreviewTCILLightsInNo.visibility = View.VISIBLE
                        base64ProofPreviewTCILLightsInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)

                    }"btnTCILFansInNo"->{
                        ivPreviewTCILFansInNo.setImageURI(photoUri)
                        ivPreviewTCILFansInNo.visibility = View.VISIBLE
                        base64ProofPreviewTCILFansInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                        "btnTCILElectricaPowerBackUpForThRoom"->{
                            ivPreviewTCILElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                            ivPreviewTCILElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                            base64ProofPreviewTCILElectricaPowerBackUpForThRoom= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILTheoryCumItLabPhotogragh"->{
                            ivPreviewTCILTheoryCumItLabPhotogragh.setImageURI(photoUri)
                            ivPreviewTCILTheoryCumItLabPhotogragh.visibility = View.VISIBLE
                            base64ProofPreviewTCILTheoryCumItLabPhotogragh= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCILDoes_the_room_has"->{
                            ivPreviewTCILDoes_the_room_has.setImageURI(photoUri)
                            ivPreviewTCILDoes_the_room_has.visibility = View.VISIBLE
                            base64ProofPreviewTCILDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

//                        Theory Cum Domain Lab Ajit Ranjan Click On Button
                        "btnTCDLTypeofRoofItLab"->{
                            ivPreviewTCDLTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewTCDLTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewTCDLTypeofRoofItLab= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLFalseCellingProvide"->{
                            ivPreviewTCDLFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewTCDLFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofPreviewTCDLFalseCellingProvide= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLHeightOfCelling"->{
                            ivPreviewTCDLHeightOfCelling.setImageURI(photoUri)
                            ivPreviewTCDLHeightOfCelling.visibility = View.VISIBLE
                            base64ProofPreviewTCDLHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLVentilationAreaInSqFt"->{
                            ivPreviewTCDLVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewTCDLVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofPreviewTCDLVentilationAreaInSqFt= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLSoundLevelInDb"->{
                            ivPreviewTCDLSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewTCDLSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofPreviewTCDLSoundLevelInDb= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLwhether_all_the_academic"->{
                            ivPreviewTCDLwhether_all_the_academic.setImageURI(photoUri)
                            ivPreviewTCDLwhether_all_the_academic.visibility = View.VISIBLE
                            base64ProofPreviewTCDLwhether_all_the_academic= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLAcademicRoomInformationBoard"->{
                            ivPreviewTCDLAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewTCDLAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofPreviewTCDLAcademicRoomInformationBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLInternalSignage"->{
                            ivPreviewTCDLInternalSignage.setImageURI(photoUri)
                            ivPreviewTCDLInternalSignage.visibility = View.VISIBLE
                            base64ProofPreviewTCDLInternalSignage= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLCctcCamerasWithAudioFacility"->{
                            ivPreviewTCDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewTCDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofPreviewTCDLCctcCamerasWithAudioFacility= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLLcdDigitalProjector"->{
                            ivPreviewTCDLLcdDigitalProjector.setImageURI(photoUri)
                            ivPreviewTCDLLcdDigitalProjector.visibility = View.VISIBLE
                            base64ProofPreviewTCDLLcdDigitalProjector= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLChairForCandidatesInNo"->{
                            ivPreviewTCDLChairForCandidatesInNo.setImageURI(photoUri)
                            ivPreviewTCDLChairForCandidatesInNo.visibility = View.VISIBLE
                            base64ProofPreviewTCDLChairForCandidatesInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLUploaadTrainerChair"->{
                            ivPreviewTCDLTrainerChair.setImageURI(photoUri)
                            ivPreviewTCDLTrainerChair.visibility = View.VISIBLE
                            base64ProofPreviewTCDLTrainerChair= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLTrainerTable"->{
                            ivPreviewTCDLTrainerTable.setImageURI(photoUri)
                            ivPreviewTCDLTrainerTable.visibility = View.VISIBLE
                            base64ProofPreviewTCDLTrainerTable= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLWritingBoard"->{
                            ivPreviewTCDLWritingBoard.setImageURI(photoUri)
                            ivPreviewTCDLWritingBoard.visibility = View.VISIBLE
                            base64ProofPreviewTCDLWritingBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }"btnTCDLLightsInNo"->{
                        ivPreviewTCDLLightsInNo.setImageURI(photoUri)
                        ivPreviewTCDLLightsInNo.visibility = View.VISIBLE
                        base64ProofPreviewTCDLLightsInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnTCDLFansInNo"->{
                        ivPreviewTCDLFansInNo.setImageURI(photoUri)
                        ivPreviewTCDLFansInNo.visibility = View.VISIBLE
                        base64ProofPreviewTCDLFansInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnTCDLElectricaPowerBackUpForThRoom"->{
                        ivPreviewTCDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                        ivPreviewTCDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                        base64ProofPreviewTCDLElectricaPowerBackUpForThRoom= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnTCDLListofDomain"->{
                        ivPreviewTCDLListofDomain.setImageURI(photoUri)
                        ivPreviewTCDLListofDomain.visibility = View.VISIBLE
                        base64ProofPreviewTCDLListofDomain= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }
                        "btnTCDLDomainLabPhotogragh"->{
                            ivPreviewTCDLDomainLabPhotogragh.setImageURI(photoUri)
                            ivPreviewTCDLDomainLabPhotogragh.visibility = View.VISIBLE
                            base64ProofPreviewTCDLDomainLabPhotogragh= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCDLDoes_the_room_has"->{
                            ivPreviewTCDLDoes_the_room_has.setImageURI(photoUri)
                            ivPreviewTCDLDoes_the_room_has.visibility = View.VISIBLE
                            base64ProofPreviewTCDLDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

//                            Domain Lab
                        "btnDLTypeofRoofItLab"->{
                            ivPreviewDLTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewDLTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewDLTypeofRoofItLab= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLFalseCellingProvide"->{
                            ivPreviewDLFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewDLFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofPreviewDLFalseCellingProvide= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLHeightOfCelling"->{
                            ivPreviewDLHeightOfCelling.setImageURI(photoUri)
                            ivPreviewDLHeightOfCelling.visibility = View.VISIBLE
                            base64ProofPreviewDLHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLVentilationAreaInSqFt"->{
                            ivPreviewDLVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewDLVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofPreviewDLVentilationAreaInSqFt= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
//                        "btnDLSoundLevelAsPerSpecifications"->{
//                            ivPreviewDLSoundLevelInDb.setImageURI(photoUri)
//                            ivPreviewDLSoundLevelInDb.visibility = View.VISIBLE
//                            base64ProofPreviewTCDLDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
//                        }
                        "btnDLSoundLevelInDb"->{
                            ivPreviewDLSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewDLSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofPreviewDLSoundLevelInDb= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLwhether_all_the_academic"->{
                            ivPreviewDLwhether_all_the_academic.setImageURI(photoUri)
                            ivPreviewDLwhether_all_the_academic.visibility = View.VISIBLE
                            base64ProofPreviewDLwhether_all_the_academic= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLAcademicRoomInformationBoard"->{
                            ivPreviewDLAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewDLAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofPreviewDLAcademicRoomInformationBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLInternalSignage"->{
                            ivPreviewDLInternalSignage.setImageURI(photoUri)
                            ivPreviewDLInternalSignage.visibility = View.VISIBLE
                            base64ProofPreviewDLInternalSignage= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLCctcCamerasWithAudioFacility"->{
                            ivPreviewDLCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewDLCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofPreviewDLCctcCamerasWithAudioFacility= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLLcdDigitalProjector"->{
                            ivPreviewDLLcdDigitalProjector.setImageURI(photoUri)
                            ivPreviewDLLcdDigitalProjector.visibility = View.VISIBLE
                            base64ProofPreviewDLLcdDigitalProjector= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLChairForCandidatesInNo"->{
                            ivPreviewDLChairForCandidatesInNo.setImageURI(photoUri)
                            ivPreviewDLChairForCandidatesInNo.visibility = View.VISIBLE
                            base64ProofPreviewDLChairForCandidatesInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLUploaadTrainerChair"->{
                            ivPreviewDLTrainerChair.setImageURI(photoUri)
                            ivPreviewDLTrainerChair.visibility = View.VISIBLE
                            base64ProofPreviewDLTrainerChair= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnDLTrainerTable"->{
                            ivPreviewDLTrainerTable.setImageURI(photoUri)
                            ivPreviewDLTrainerTable.visibility = View.VISIBLE
                            base64ProofPreviewDLTrainerTable= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }"btnDLWritingBoard"->{
                        ivPreviewDLWritingBoard.setImageURI(photoUri)
                        ivPreviewDLWritingBoard.visibility = View.VISIBLE
                        base64ProofPreviewDLWritingBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLLightsInNo"->{
                        ivPreviewDLLightsInNo.setImageURI(photoUri)
                        ivPreviewDLLightsInNo.visibility = View.VISIBLE
                        base64ProofPreviewDLLightsInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLFansInNo"->{
                        ivPreviewDLFansInNo.setImageURI(photoUri)
                        ivPreviewDLFansInNo.visibility = View.VISIBLE
                        base64ProofPreviewDLFansInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLElectricaPowerBackUpForThRoom"->{
                        ivPreviewDLElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                        ivPreviewDLElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                        base64ProofPreviewDLElectricaPowerBackUpForThRoom= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLILListofDomain"->{
                        ivPreviewDLILListofDomain.setImageURI(photoUri)
                        ivPreviewDLILListofDomain.visibility = View.VISIBLE
                        base64ProofPreviewDLILListofDomain= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLDomainLabPhotogragh"->{
                        ivPreviewDLDomainLabPhotogragh.setImageURI(photoUri)
                        ivPreviewDLDomainLabPhotogragh.visibility = View.VISIBLE
                        base64ProofPreviewDLDomainLabPhotogragh= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }"btnDLDoes_the_room_has"->{
                        ivPreviewDLDoes_the_room_has.setImageURI(photoUri)
                        ivPreviewDLDoes_the_room_has.visibility = View.VISIBLE
                        base64ProofPreviewDLDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
                    }

//                        Theory Class Room

                        "btnTCRTypeofRoofItLab" -> {
                            ivPreviewTCRTypeofRoofItLab.setImageURI(photoUri)
                            ivPreviewTCRTypeofRoofItLab.visibility = View.VISIBLE
                            base64ProofPreviewTCRTypeofRoofItLab= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRFalseCellingProvide" -> {
                            ivPreviewTCRFalseCellingProvide.setImageURI(photoUri)
                            ivPreviewTCRFalseCellingProvide.visibility = View.VISIBLE
                            base64ProofPreviewTCRFalseCellingProvide= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRHeightOfCelling" -> {
                            ivPreviewTCRHeightOfCelling.setImageURI(photoUri)
                            ivPreviewTCRHeightOfCelling.visibility = View.VISIBLE
                            base64ProofPreviewTCRHeightOfCelling= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRVentilationAreaInSqFt" -> {
                            ivPreviewTCRVentilationAreaInSqFt.setImageURI(photoUri)
                            ivPreviewTCRVentilationAreaInSqFt.visibility = View.VISIBLE
                            base64ProofPreviewTCRVentilationAreaInSqFt= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRSoundLevelInDb" -> {
                            ivPreviewTCRSoundLevelInDb.setImageURI(photoUri)
                            ivPreviewTCRSoundLevelInDb.visibility = View.VISIBLE
                            base64ProofPreviewTCRSoundLevelInDb= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRwhether_all_the_academic" -> {
                            ivPreviewTCRwhether_all_the_academic.setImageURI(photoUri)
                            ivPreviewTCRwhether_all_the_academic.visibility = View.VISIBLE
                            base64ProofPreviewTCRwhether_all_the_academic= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRAcademicRoomInformationBoard" -> {
                            ivPreviewTCRAcademicRoomInformationBoard.setImageURI(photoUri)
                            ivPreviewTCRAcademicRoomInformationBoard.visibility = View.VISIBLE
                            base64ProofPreviewTCRAcademicRoomInformationBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRCctcCamerasWithAudioFacility" -> {
                            ivPreviewTCRCctcCamerasWithAudioFacility.setImageURI(photoUri)
                            ivPreviewTCRCctcCamerasWithAudioFacility.visibility = View.VISIBLE
                            base64ProofPreviewTCRCctcCamerasWithAudioFacility= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }


                        "btnTCRLcdDigitalProjector" -> {
                            ivPreviewTCRLcdDigitalProjector.setImageURI(photoUri)
                            ivPreviewTCRLcdDigitalProjector.visibility = View.VISIBLE
                            base64ProofPreviewTCRLcdDigitalProjector= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRChairForCandidatesInNo" -> {
                            ivPreviewTCRChairForCandidatesInNo.setImageURI(photoUri)
                            ivPreviewTCRChairForCandidatesInNo.visibility = View.VISIBLE
                            base64ProofPreviewTCRChairForCandidatesInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRTrainerChair" -> {
                            ivPreviewTCRTrainerChair.setImageURI(photoUri)
                            ivPreviewTCRTrainerChair.visibility = View.VISIBLE
                            base64ProofPreviewTCRTrainerChair= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRTrainerTable" -> {
                            ivPreviewTCRTrainerTable.setImageURI(photoUri)
                            ivPreviewTCRTrainerTable.visibility = View.VISIBLE
                            base64ProofPreviewTCRTrainerTable= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRWritingBoard" -> {
                            ivPreviewTCRWritingBoard.setImageURI(photoUri)
                            ivPreviewTCRWritingBoard.visibility = View.VISIBLE
                            base64ProofPreviewTCRWritingBoard= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRLightsInNo" -> {
                            ivPreviewTCRLightsInNo.setImageURI(photoUri)
                            ivPreviewTCRLightsInNo.visibility = View.VISIBLE
                            base64ProofPreviewTCRLightsInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRFansInNo" -> {
                            ivPreviewTCRFansInNo.setImageURI(photoUri)
                            ivPreviewTCRFansInNo.visibility = View.VISIBLE
                            base64ProofPreviewTCRFansInNo= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRElectricaPowerBackUpForThRoom" -> {
                            ivPreviewTCRElectricaPowerBackUpForThRoom.setImageURI(photoUri)
                            ivPreviewTCRElectricaPowerBackUpForThRoom.visibility = View.VISIBLE
                            base64ProofPreviewTCRElectricaPowerBackUpForThRoom= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRDomainLabPhotogragh" -> {
                            ivPreviewTCRDomainLabPhotogragh.setImageURI(photoUri)
                            ivPreviewTCRDomainLabPhotogragh.visibility = View.VISIBLE
                            base64ProofPreviewTCRDomainLabPhotogragh= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }
                        "btnTCRDoes_the_room_has" -> {
                            ivPreviewTCRDoes_the_room_has.setImageURI(photoUri)
                            ivPreviewTCRDoes_the_room_has.visibility = View.VISIBLE
                            base64ProofPreviewTCRDoes_the_room_has= AppUtil.imageUriToBase64(requireContext(), photoUri)
                        }

                        "btnTCRInternalSignage" -> {
                            ivPreviewivPreviewTCRInternalSignage.setImageURI(photoUri)
                            ivPreviewivPreviewTCRInternalSignage.visibility = View.VISIBLE
                            base64ProofPreviewTCRInternalSignage= AppUtil.imageUriToBase64(requireContext(), photoUri)
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
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrainingBinding.bind(view)
        setupExpandableSections(view)
        setupPhotoUploadButtons(view)
        collectSectionStatus()
        collectFinalSubmitData()

        centerId = arguments?.getString("centerId").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()
        status = arguments?.getString("status")
        remarks = arguments?.getString("remarks")
        RecyClerViewUI()
        if (status == STATUS_QM || status == STATUS_SM) {
            AlertDialog.Builder(requireContext())
                .setTitle("Remarks")
                .setMessage(remarks)
                .setPositiveButton("Okay") { dialog: DialogInterface?, _: Int ->
                    dialog?.dismiss()
                }
                .show()
        }

        val requestTcInfraReq = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext())
        )

        viewModel.getSectionsStatusData(requestTcInfraReq)

        // Initialize Training center information views
        etLatitude = view.bindView(R.id.etLatitude)
        etLongitude = view.bindView(R.id.etLongitude)



        binding.backButton.setOnClickListener {

            findNavController().navigateUp()
        }


        //RecyClerViewUI()

//         Initilize  IT LAB  AJIT PMAYG Crate ID

        etITLTypeofRoofItLab = view.bindView(R.id.etITLTypeofRoofItLab)
        etITLHeightOfCelling = view.bindView(R.id.etITLHeightOfCelling)
        etITLVentilationAreaInSqFt = view.bindView(R.id.etITLVentilationAreaInSqFt)
        etITLSoundLevelAsPerSpecifications = view.bindView(R.id.etITLSoundLevelAsPerSpecifications)
        etITLSoundLevelInDb = view.bindView(R.id.etITLSoundLevelInDb)
        etITLLanEnabledComputersInNo = view.bindView(R.id.etITLLanEnabledComputersInNo)
        etITLTablets = view.bindView(R.id.etITLTablets)
        etITLStoolsChairs = view.bindView(R.id.etITLStoolsChairs)
        etITLLightsInNo = view.bindView(R.id.etITLLightsInNo)
        etITLFansInNo = view.bindView(R.id.etITLFansInNo)



        //         Office Cum(Counselling room)    Ajit Ranjan  EditText Id GET
        etOfficeRoomPhotograph = view.bindView(R.id.etOfficeRoomPhotograph)
        etOfficeCumTypeofRoofItLab = view.bindView(R.id.etOfficeCumTypeofRoofItLab)
        etOfficeCumHeightOfCelling = view.bindView(R.id.etOfficeCumHeightOfCelling)
        etOfficeCumSplaceforSecuringDoc = view.bindView(R.id.etOfficeCumSplaceforSecuringDoc)
        etOfficeCumAnOfficeTableNo = view.bindView(R.id.etOfficeCumAnOfficeTableNo)
        etOfficeCumChairs = view.bindView(R.id.etOfficeCumChairs)
        etOfficeCumPrinterCumScannerInNo = view.bindView(R.id.etOfficeCumPrinterCumScannerInNo)
        etOfficeCumDigitalCameraInNo = view.bindView(R.id.etOfficeCumDigitalCameraInNo)



//        (Office room)    Ajit Ranjan  EditText Id GET


        etOROfficeRoomPhotograph = view.bindView(R.id.etOROfficeRoomPhotograph)
        etORTypeofRoofItLab = view.bindView(R.id.etORTypeofRoofItLab)
        etORHeightOfCelling = view.bindView(R.id.etORHeightOfCelling)
        etORSplaceforSecuringDoc = view.bindView(R.id.etORSplaceforSecuringDoc)
        etORAnOfficeTableNo = view.bindView(R.id.etORAnOfficeTableNo)
        etORChairs = view.bindView(R.id.etORChairs)
        etORPrinterCumScannerInNo = view.bindView(R.id.etORPrinterCumScannerInNo)
        etORDigitalCameraInNo = view.bindView(R.id.etORDigitalCameraInNo)





//        IT COME DOMAIN LAB  Ajit Ranjan  EditText Id GET
        etITCDLTypeofRoofItLab = view.bindView(R.id.etITCDLTypeofRoofItLab)
        etITCDLFalseCellingProvide = view.bindView(R.id.etITCDLFalseCellingProvide)
        etITCDLHeightOfCelling = view.bindView(R.id.etITCDLHeightOfCelling)
        etITCDLVentilationAreaInSqFt = view.bindView(R.id.etITCDLVentilationAreaInSqFt)
        etITCDLSoundLevelAsPerSpecifications = view.bindView(R.id.etITCDLSoundLevelAsPerSpecifications)
        etITCDLabSoundLevelInDb = view.bindView(R.id.etITCDLabSoundLevelInDb)
        etITCDLLanEnabledComputersInNo = view.bindView(R.id.etITCDLLanEnabledComputersInNo)
        etITCDLTablets = view.bindView(R.id.etITCDLTablets)
        etITCDLStoolsChairs = view.bindView(R.id.etITCDLStoolsChairs)
        etITCDLLightsInNo = view.bindView(R.id.etITCDLLightsInNo)
        etITCDLFansInNo = view.bindView(R.id.etITCDLFansInNo)
        etITCDLItLabPhotograph = view.bindView(R.id.etITCDLItLabPhotograph)
        etITCDLListofDomain = view.bindView(R.id.etITCDLListofDomain)

//        Theory Cum IT Lab Ajit Ranjan
        etTCILListofDomain = view.bindView(R.id.etTCILListofDomain)
        etTCILTypeofRoofItLab = view.bindView(R.id.etTCILTypeofRoofItLab)
        etTCILFalseCellingProvide = view.bindView(R.id.etTCILFalseCellingProvide)
        etTCILHeightOfCelling = view.bindView(R.id.etTCILHeightOfCelling)
        etTCILVentilationAreaInSqFt = view.bindView(R.id.etTCILVentilationAreaInSqFt)
        etTCILSoundLevelAsPerSpecifications = view.bindView(R.id.etTCILSoundLevelAsPerSpecifications)
        etTCILSoundLevelInDb = view.bindView(R.id.etTCILSoundLevelInDb)
        etTCILLanEnabledComputersInNo = view.bindView(R.id.etTCILLanEnabledComputersInNo)
        etTCILTablets = view.bindView(R.id.etTCILTablets)
        etTCILStoolsChairs = view.bindView(R.id.etTCILStoolsChairs)
        etTCILTrainerChair = view.bindView(R.id.etTCILTrainerChair)
        etTCILTrainerTable = view.bindView(R.id.etTCILTrainerTable)
        etTCILLightsInNo = view.bindView(R.id.etTCILLightsInNo)
        etTCILFansInNo = view.bindView(R.id.etTCILFansInNo)
        etTCILTheoryCumItLabPhotogragh = view.bindView(R.id.etTCILTheoryCumItLabPhotogragh)



//        Theory Cum DOMAIN Lab Ajit Ranjan
        etTCDLTypeofRoofItLab = view.bindView(R.id.etTCDLTypeofRoofItLab)
        etTCDLFalseCellingProvide = view.bindView(R.id.etTCDLFalseCellingProvide)
        etTCDLHeightOfCelling = view.bindView(R.id.etTCDLHeightOfCelling)
        etTCDLVentilationAreaInSqFt = view.bindView(R.id.etTCDLVentilationAreaInSqFt)
        etTCDLSoundLevelAsPerSpecifications = view.bindView(R.id.etTCDLSoundLevelAsPerSpecifications)
        etTCDLSoundLevelInDb = view.bindView(R.id.etTCDLSoundLevelInDb)
        etTCDLLcdDigitalProjector = view.bindView(R.id.etTCDLLcdDigitalProjector)
        etTCDLChairForCandidatesInNo = view.bindView(R.id.etTCDLChairForCandidatesInNo)
        etTCDLTrainerChair = view.bindView(R.id.etTCDLTrainerChair)
        etTCDLTrainerTable = view.bindView(R.id.etTCDLTrainerTable)
        etTCDLWritingBoard = view.bindView(R.id.etTCDLWritingBoard)
        etTCDLLightsInNo = view.bindView(R.id.etTCDLLightsInNo)
        etTCDLFansInNo = view.bindView(R.id.etTCDLFansInNo)
        etTCDLListofDomain = view.bindView(R.id.etTCDLListofDomain)
        etTCDLDomainLabPhotogragh = view.bindView(R.id.etTCDLDomainLabPhotogragh)

//        DOMAIN Lab Ajit Ranjan


        etDLTypeofRoofItLab = view.bindView(R.id.etDLTypeofRoofItLab)
        etDLFalseCellingProvide = view.bindView(R.id.etDLFalseCellingProvide)
        etDLHeightOfCelling = view.bindView(R.id.etDLHeightOfCelling)
        etDLVentilationAreaInSqFt = view.bindView(R.id.etDLVentilationAreaInSqFt)
        etDLSoundLevelAsPerSpecifications = view.bindView(R.id.etDLSoundLevelAsPerSpecifications)
        etDLSoundLevelInDb = view.bindView(R.id.etDLSoundLevelInDb)
        etDLLcdDigitalProjector = view.bindView(R.id.etDLLcdDigitalProjector)
        etDLChairForCandidatesInNo = view.bindView(R.id.etDLChairForCandidatesInNo)
        etDLTrainerChair = view.bindView(R.id.etDLTrainerChair)
        etDLTrainerTable = view.bindView(R.id.etDLTrainerTable)
        etDLWritingBoard = view.bindView(R.id.etDLWritingBoard)
        etDLLightsInNo = view.bindView(R.id.etDLLightsInNo)
        etDLFansInNo = view.bindView(R.id.etDLFansInNo)
        etDLDomainLabPhotogragh = view.bindView(R.id.etDLDomainLabPhotogragh)

//           TCR

        etTCRTypeofRoofItLab = view.bindView(R.id.etTCRTypeofRoofItLab)
        etTCRFalseCellingProvide = view.bindView(R.id.etTCRFalseCellingProvide)
        etTCRHeightOfCelling = view.bindView(R.id.etTCRHeightOfCelling)
        etTCRVentilationAreaInSqFt = view.bindView(R.id.etTCRVentilationAreaInSqFt)
        etTCRSoundLevelAsPerSpecifications = view.bindView(R.id.etTCRSoundLevelAsPerSpecifications)
        etTCRSoundLevelInDb = view.bindView(R.id.etTCRSoundLevelInDb)
        ivPreviewTCRAcademicRoomInformationBoard = view.bindView(R.id.ivPreviewTCRAcademicRoomInformationBoard)
        ivPreviewTCRCctcCamerasWithAudioFacility = view.bindView(R.id.ivPreviewTCRCctcCamerasWithAudioFacility)
        ivPreviewivPreviewTCRInternalSignage = view.bindView(R.id.ivPreviewTCRInternalSignage)
        etTCRLcdDigitalProjector = view.bindView(R.id.etTCRLcdDigitalProjector)
        etTCRChairForCandidatesInNo = view.bindView(R.id.etTCRChairForCandidatesInNo)
        etTCRTrainerChair = view.bindView(R.id.etTCRTrainerChair)
        etTCRTrainerTable = view.bindView(R.id.etTCRTrainerTable)
        etTCRWritingBoard = view.bindView(R.id.etTCRWritingBoard)
        etTCRLightsInNo = view.bindView(R.id.etTCRLightsInNo)
        etTCRFansInNo = view.bindView(R.id.etTCRFansInNo)
        etTCRDomainLabPhotogragh = view.bindView(R.id.etTCRDomainLabPhotogragh)

        // Initialize Wash Basin views
        etMaleToilets = view.bindView(R.id.etMaleToilets)
        btnUploadProofMaleToilets = view.bindView(R.id.btnUploadProofMaleToilets)
        ivPreviewMaleToiletsProof = view.bindView(R.id.ivPreviewProofMaleToilets)

        btnUploadProofMaleToiletsSignage = view.bindView(R.id.btnUploadProofMaleToiletsSignage)
        ivPreviewMaleToiletsSignageProof = view.bindView(R.id.ivPreviewProofMaleToiletsSignage)

        etFemaleToilets = view.bindView(R.id.etFemaleToilets)
        btnUploadProofFemaleToilets = view.bindView(R.id.btnUploadProofFemaleToilets)
        ivPreviewFemaleToiletsProof = view.bindView(R.id.ivPreviewProofFemaleToilets)

        btnUploadProofFemaleToiletsSignage = view.bindView(R.id.btnUploadProofFemaleToiletsSignage)
        ivPreviewFemaleToiletsSignageProof = view.bindView(R.id.ivPreviewProofFemaleToiletsSignage)

        etMaleUrinals = view.bindView(R.id.etMaleUrinals)
        btnUploadProofMaleUrinals = view.bindView(R.id.btnUploadProofMaleUrinals)
        ivPreviewMaleUrinalsProof = view.bindView(R.id.ivPreviewProofMaleUrinals)

        etMaleWashBasins = view.bindView(R.id.etMaleWashBasins)
        btnUploadProofMaleWashBasins = view.bindView(R.id.btnUploadProofMaleWashBasins)
        ivPreviewMaleWashBasinsProof = view.bindView(R.id.ivPreviewProofMaleWashBasins)

        etFemaleWashBasins = view.bindView(R.id.etFemaleWashBasins)
        btnUploadProofFemaleWashBasins = view.bindView(R.id.btnUploadProofFemaleWashBasins)
        ivPreviewFemaleWashBasinsProof = view.bindView(R.id.ivPreviewProofFemaleWashBasins)

        actvOverheadTanks = view.bindView(R.id.actvOverheadTanks)
        btnUploadProofOverheadTanks = view.bindView(R.id.btnUploadProofOverheadTanks)
        ivPreviewOverheadTanksProof = view.bindView(R.id.ivPreviewProofOverheadTanks)

        actvTypeOfFlooring = view.bindView(R.id.actvTypeOfFlooring)
        btnUploadProofFlooring = view.bindView(R.id.btnUploadProofFlooring)
        ivPreviewFlooringProof = view.bindView(R.id.ivPreviewProofFlooring)


        // Initialize CCTV ImageViews
        ivMonitorPreview = view.findViewById(R.id.ivMonitorPreview)
        ivConformancePreview = view.findViewById(R.id.ivConformancePreview)
        ivStoragePreview = view.findViewById(R.id.ivStoragePreview)
        ivDVRPreview = view.findViewById(R.id.ivDVRPreview)

        // Initialize Electrical Wiring ImageViews
        ivSwitchBoardPreview = view.findViewById(R.id.ivSwitchBoardPreview)
        ivWireSecurityPreview = view.findViewById(R.id.ivWireSecurityPreview)

        // Initialize General Details ImageViews
        ivLeakagePreview = view.findViewById(R.id.ivLeakagePreview)
        ivStairsPreview = view.findViewById(R.id.ivStairsPreview)

        //Initialize support infra ImageViews

        ivFirstAidKitPreview = view.findViewById(R.id.ivFirstAidKitPreview)
        ivFireFightingEquipmentPreview = view.findViewById(R.id.ivFireFightingEquipmentPreview)
        ivSafeDrinkingWaterPreview = view.findViewById(R.id.ivSafeDrinkingWaterPreview)
        spinnerFirstAidKit = view.findViewById(R.id.spinnerFirstAidKit)

        spinnerSafeDrinkingWater = view.findViewById(R.id.spinnerSafeDrinkingWater)
        editFireFightingEquipment = view.findViewById<TextInputEditText>(R.id.editFireFightingEquipment)








        ivPowerBackupPreview = view.findViewById(R.id.ivPowerBackupPreview)
        ivBiometricDevicesPreview = view.findViewById(R.id.ivBiometricDevicesPreview)
        ivCCTVPreview = view.findViewById(R.id.ivCCTVPreview)
        ivDocumentStoragePreview = view.findViewById(R.id.ivDocumentStoragePreview)
        ivPrinterScannerPreview = view.findViewById(R.id.ivPrinterScannerPreview)
        ivDigitalCameraPreview = view.findViewById(R.id.ivDigitalCameraPreview)
        ivGrievanceRegisterPreview = view.findViewById(R.id.ivGrievanceRegisterPreview)
        ivMinimumEquipmentPreview = view.findViewById(R.id.ivMinimumEquipmentPreview)
        ivDirectionBoardsPreview = view.findViewById(R.id.ivDirectionBoardsPreview)
        etBiometricDevices = view.findViewById(R.id.etBiometricDevices)
        etPrinterScanner = view.findViewById(R.id.etPrinterScanner)
        etDigitalCamera = view.findViewById(R.id.etDigitalCamera)
        spinnerPowerBackup=view.findViewById(R.id.spinnerPowerBackup)
        spinnerCCTV=view.findViewById(R.id.spinnerCCTV)
        spinnerDocumentStorage=view.findViewById(R.id.spinnerDocumentStorage)
        spinnerGrievanceRegister=view.findViewById(R.id.spinnerGrievanceRegister)
        spinnerMinimumEquipment=view.findViewById(R.id.spinnerMinimumEquipment)
        spinnerDirectionBoards=view.findViewById(R.id.spinnerDirectionBoards)

        // Description of other areas
        ivProofPreview = view.findViewById(R.id.ivProofPreview)
        ivCirculationProofPreview = view.findViewById(R.id.ivCirculationProofPreview)
        ivOpenSpaceProofPreview = view.findViewById(R.id.ivOpenSpaceProofPreview)
        ivParkingProofPreview = view.findViewById(R.id.ivParkingProofPreview)
        etCorridorNo =view.findViewById(R.id.etCorridorNo)
        etDescLength=view.findViewById(R.id.etDescLength)
        etDescWidth =view.findViewById(R.id.etDescWidth)
        etArea =view.findViewById(R.id.etArea)
        etLights =view.findViewById(R.id.etLights)
        etFans=view.findViewById(R.id.etFans)
        etCirculationArea =view.findViewById(R.id.etCirculationArea)
        etOpenSpace =view.findViewById(R.id.etOpenSpace)
        etExclusiveParkingSpace =view.findViewById(R.id.etExclusiveParkingSpace)

        // Description of Ajit Ranjnan


        AcsdemicSpinner = view.findViewById(R.id.AcsdemicSpinner)
        // General Details Spinners




        lin_domain_lab = view.findViewById(R.id.lin_domain_lab)
        lin_itlab = view.findViewById(R.id.lin_itlab)



        lin_office_counselling_room = view.findViewById(R.id.lin_office_counselling_room)
        lin_counselling_room = view.findViewById(R.id.lin_counselling_room)
        lin_non_reception = view.findViewById(R.id.lin_non_reception)
        lin_office_room = view.findViewById(R.id.lin_office_room)
        lin_tcum_domain_lab = view.findViewById(R.id.lin_tcum_domain_lab)
        lin_theory_cum_it_lab = view.findViewById(R.id.lin_theory_cum_it_lab)
        lin_theory_cum_domain_lab = view.findViewById(R.id.lin_theory_cum_domain_lab)
        lin_theory_class_room = view.findViewById(R.id.lin_theory_class_room)


        etLength = view.findViewById(R.id.etLength)
        etWidth = view.findViewById(R.id.etWidth)
        tvArea = view.findViewById(R.id.tvArea)
        etLength.visibility = View.GONE
        etWidth.visibility = View.GONE
        tvArea.visibility = View.GONE
        etroomType = view.findViewById(R.id.etroomType)
        etroomType.visibility = View.GONE

//        etetDescriptionOfAcademic_NonAcademicAreashWidth = view.findViewById(R.id.etetDescriptionOfAcademic_NonAcademicAreashWidth)
        btnSubmitAdddMore = view.findViewById(R.id.btnSubmitAdddMore)
        LayoutLinear = view.findViewById(R.id.LayoutLinear)
        RecyclerViewData = view.findViewById(R.id.RecyclerViewData)



//        IT LAB
        ivPreviewITLTypeofRoofItLab = view.findViewById(R.id.ivPreviewITLTypeofRoofItLab)
        ivPreviewITLFalseCellingProvide = view.findViewById(R.id.ivPreviewITLFalseCellingProvide)
        ivPreviewITLHeightOfCelling = view.findViewById(R.id.ivPreviewITLHeightOfCelling)
        ivPreviewITLVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewITLVentilationAreaInSqFt)
        ivPreviewITLSoundLevelAsPerSpecifications = view.findViewById(R.id.ivPreviewITLSoundLevelAsPerSpecifications)
        ivPreviewITLSoundLevelInDb = view.findViewById(R.id.ivPreviewITLSoundLevelInDb)
        ivPreviewITLwhether_all_the_academic = view.findViewById(R.id.ivPreviewITLwhether_all_the_academic)
        ivPreviewITLAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewITLAcademicRoomInformationBoard)
        ivPreviewITLInternalSignage = view.findViewById(R.id.ivPreviewITLInternalSignage)
        ivPreviewITLCctcCamerasWithAudioFacility = view.findViewById(R.id.ivPreviewITLCctcCamerasWithAudioFacility)
        ivPreviewITLLanEnabledComputersInNo = view.findViewById(R.id.ivPreviewITLLanEnabledComputersInNo)
        ivPreviewITLInternetConnections = view.findViewById(R.id.ivPreviewITLInternetConnections)
        ivPreviewITLDoAllComputersHaveTypingTutor = view.findViewById(R.id.ivPreviewITLDoAllComputersHaveTypingTutor)
        ivPreviewITLTablets = view.findViewById(R.id.ivPreviewITLTablets)
        ivPreviewITLStoolsChairs = view.findViewById(R.id.ivPreviewITLStoolsChairs)
        ivPreviewITLTrainerChair = view.findViewById(R.id.ivPreviewITLTrainerChair)
        ivPreviewITLTrainerTable = view.findViewById(R.id.ivPreviewITLTrainerTable)
        ivPreviewITLLightsInNo = view.findViewById(R.id.ivPreviewITLLightsInNo)
        ivPreviewITLFansInNo = view.findViewById(R.id.ivPreviewITLFansInNo)
        ivPreviewITLElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewITLElectricaPowerBackUpForThRoom)
        ivPreviewITLItLabPhotograph = view.findViewById(R.id.ivPreviewITLItLabPhotograph)
        ivPreviewITLDoes_the_room_has = view.findViewById(R.id.ivPreviewITLDoes_the_room_has)

//        Office Cum(Counselling room)


        ivPreviewOfficeRoomPhotograph = view.findViewById(R.id.ivPreviewOfficeRoomPhotograph)
        ivPreviewOfficeCumTypeofRoofItLab = view.findViewById(R.id.ivPreviewOfficeCumTypeofRoofItLab)
        ivPreviewOfficeCumFalseCellingProvide = view.findViewById(R.id.ivPreviewOfficeCumFalseCellingProvide)
        ivPreviewOfficeCumHeightOfCelling = view.findViewById(R.id.ivPreviewOfficeCumHeightOfCelling)
        ivPreviewOfficeCumSplaceforSecuringDoc = view.findViewById(R.id.ivPreviewOfficeCumSplaceforSecuringDoc)
        ivPreviewOfficeCumAnOfficeTableNo = view.findViewById(R.id.ivPreviewOfficeCumAnOfficeTableNo)
        ivPreviewOfficeCumChairs = view.findViewById(R.id.ivPreviewOfficeCumChairs)
        ivPreviewOfficeCumTableOfofficeCumpter = view.findViewById(R.id.ivPreviewOfficeCumTableOfofficeCumpter)
        ivPreviewOfficeCumPrinterCumScannerInNo = view.findViewById(R.id.ivPreviewOfficeCumPrinterCumScannerInNo)
        ivPreviewOfficeCumDigitalCameraInNo = view.findViewById(R.id.ivPreviewOfficeCumDigitalCameraInNo)
        ivPreviewOfficeCumElectricialPowerBackup = view.findViewById(R.id.ivPreviewOfficeCumElectricialPowerBackup)

//        Reception Area

        ivPreviewReceptionAreaPhotogragh = view.findViewById(R.id.ivPreviewReceptionAreaPhotogragh)

//        CounsellingRoomArea
        ivPreviewCounsellingRoomAreaPhotograph = view.findViewById(R.id.ivPreviewCounsellingRoomAreaPhotograph)

//        Office  Room


        ivPreviewOROfficeRoomPhotograph = view.findViewById(R.id.ivPreviewOROfficeRoomPhotograph)
        ivPreviewORTypeofRoofItLab = view.findViewById(R.id.ivPreviewORTypeofRoofItLab)
        ivPreviewORFalseCellingProvide = view.findViewById(R.id.ivPreviewORFalseCellingProvide)
        ivPreviewORHeightOfCelling = view.findViewById(R.id.ivPreviewORHeightOfCelling)
        ivPreviewORSplaceforSecuringDoc = view.findViewById(R.id.ivPreviewORSplaceforSecuringDoc)
        ivPreviewORAnOfficeTableNo = view.findViewById(R.id.ivPreviewORAnOfficeTableNo)
        ivPreviewORChairs = view.findViewById(R.id.ivPreviewORChairs)
        ivPreviewORTableOfofficeCumpter = view.findViewById(R.id.ivPreviewORTableOfofficeCumpter)
        ivPreviewORPrinterCumScannerInNo = view.findViewById(R.id.ivPreviewORPrinterCumScannerInNo)
        ivPreviewORDigitalCameraInNo = view.findViewById(R.id.ivPreviewORDigitalCameraInNo)
        ivPreviewORElectricialPowerBackup = view.findViewById(R.id.ivPreviewORElectricialPowerBackup)



//


//        It Come Domain Lab


        ivPreviewITCDLTypeofRoofItLab = view.findViewById(R.id.ivPreviewITCDLTypeofRoofItLab)
        ivPreviewITCDLFalseCellingProvide = view.findViewById(R.id.ivPreviewITCDLFalseCellingProvide)
        ivPreviewITCDLabHeightOfCelling = view.findViewById(R.id.ivPreviewITCDLabHeightOfCelling)
        ivPreviewITCDLVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewITCDLVentilationAreaInSqFt)
        ivPreviewITCDLabSoundLevelInDb = view.findViewById(R.id.ivPreviewITCDLabSoundLevelInDb)
        ivPreviewITCDLwhether_all_the_academic = view.findViewById(R.id.ivPreviewITCDLwhether_all_the_academic)
        ivPreviewITCDLAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewITCDLAcademicRoomInformationBoard)
        ivPreviewITCDLInternalSignage = view.findViewById(R.id.ivPreviewITCDLInternalSignage)
        ivPreviewITCDLCctcCamerasWithAudioFacility = view.findViewById(R.id.ivPreviewITCDLCctcCamerasWithAudioFacility)
        ivPreviewITCDLLanEnabledComputersInNo = view.findViewById(R.id.ivPreviewITCDLLanEnabledComputersInNo)
        ivPreviewITCDLInternetConnections = view.findViewById(R.id.ivPreviewITCDLInternetConnections)
        ivPreviewITCDLDoAllComputersHaveTypingTutor = view.findViewById(R.id.ivPreviewITCDLDoAllComputersHaveTypingTutor)
        ivPreviewITCDLTablets = view.findViewById(R.id.ivPreviewITCDLTablets)
        ivPreviewITCDLStoolsChairs = view.findViewById(R.id.ivPreviewITCDLStoolsChairs)
        ivPreviewITCDLTrainerChair = view.findViewById(R.id.ivPreviewITCDLTrainerChair)
        ivPreviewITCDLLightsInNo = view.findViewById(R.id.ivPreviewITCDLLightsInNo)
        ivPreviewITCDLTrainerTable = view.findViewById(R.id.ivPreviewITCDLTrainerTable)
        ivPreviewITCDLFansInNo = view.findViewById(R.id.ivPreviewITCDLFansInNo)
        ivPreviewITCDLElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewITCDLElectricaPowerBackUpForThRoom)
        ivPreviewITCDLItLabPhotograph = view.findViewById(R.id.ivPreviewITCDLItLabPhotograph)
        ivPreviewITCDLListofDomain = view.findViewById(R.id.ivPreviewITCDLListofDomain)
        ivPreviewITCDLDoes_the_room_has = view.findViewById(R.id.ivPreviewITCDLDoes_the_room_has)

//            Thorey Come It Lab

        ivPreviewTCILListofDomain = view.findViewById(R.id.ivPreviewTCILListofDomain)
        ivPreviewTCILTypeofRoofItLab = view.findViewById(R.id.ivPreviewTCILTypeofRoofItLab)
        ivPreviewTCILFalseCellingProvide = view.findViewById(R.id.ivPreviewTCILFalseCellingProvide)
        ivPreviewTCILHeightOfCelling = view.findViewById(R.id.ivPreviewTCILHeightOfCelling)
        ivPreviewTCILVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewTCILVentilationAreaInSqFt)
        ivPreviewTCILSoundLevelInDb = view.findViewById(R.id.ivPreviewTCILSoundLevelInDb)
        ivPreviewTCILwhether_all_the_academic = view.findViewById(R.id.ivPreviewTCILwhether_all_the_academic)
        ivPreviewTCILAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewTCILAcademicRoomInformationBoard)
        ivPreviewTCILInternalSignage = view.findViewById(R.id.ivPreviewTCILInternalSignage)
        ivPreviewTCILCctcCamerasWithAudioFacility = view.findViewById(R.id.ivPreviewTCILCctcCamerasWithAudioFacility)
        ivPreviewTCILLanEnabledComputersInNo = view.findViewById(R.id.ivPreviewTCILLanEnabledComputersInNo)
        ivPreviewTCILInternetConnections = view.findViewById(R.id.ivPreviewTCILInternetConnections)
        ivPreviewTCILDoAllComputersHaveTypingTutor = view.findViewById(R.id.ivPreviewTCILDoAllComputersHaveTypingTutor)
        ivPreviewTCILTablets = view.findViewById(R.id.ivPreviewTCILTablets)
        ivPreviewTCILStoolsChairs = view.findViewById(R.id.ivPreviewTCILStoolsChairs)
        ivPreviewTCILTrainerTable = view.findViewById(R.id.ivPreviewTCILTrainerTable)
        ivPreviewTCILTrainerChair = view.findViewById(R.id.ivPreviewTCILTrainerChair)
        ivPreviewTCILLightsInNo = view.findViewById(R.id.ivPreviewTCILLightsInNo)
        ivPreviewTCILFansInNo = view.findViewById(R.id.ivPreviewTCILFansInNo)
        ivPreviewTCILElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewTCILElectricaPowerBackUpForThRoom)
        ivPreviewTCILTheoryCumItLabPhotogragh = view.findViewById(R.id.ivPreviewTCILTheoryCumItLabPhotogragh)
        ivPreviewTCILDoes_the_room_has = view.findViewById(R.id.ivPreviewTCILDoes_the_room_has)

//        Theory Cum Domain Lab Ajit Ranjan Click On Button



        ivPreviewTCDLTypeofRoofItLab = view.findViewById(R.id.ivPreviewTCDLTypeofRoofItLab)
        ivPreviewTCDLFalseCellingProvide = view.findViewById(R.id.ivPreviewTCDLFalseCellingProvide)
        ivPreviewTCDLHeightOfCelling = view.findViewById(R.id.ivPreviewTCDLHeightOfCelling)
        ivPreviewTCDLVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewTCDLVentilationAreaInSqFt)
        ivPreviewTCDLSoundLevelInDb = view.findViewById(R.id.ivPreviewTCDLSoundLevelInDb)
        ivPreviewTCDLwhether_all_the_academic = view.findViewById(R.id.ivPreviewTCDLwhether_all_the_academic)
        ivPreviewTCDLAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewTCDLAcademicRoomInformationBoard)
        ivPreviewTCDLInternalSignage = view.findViewById(R.id.ivPreviewTCDLInternalSignage)
        ivPreviewTCDLCctcCamerasWithAudioFacility = view.findViewById(R.id.ivPreviewTCDLCctcCamerasWithAudioFacility)
        ivPreviewTCDLLcdDigitalProjector = view.findViewById(R.id.ivPreviewTCDLLcdDigitalProjector)
        ivPreviewTCDLChairForCandidatesInNo = view.findViewById(R.id.ivPreviewTCDLChairForCandidatesInNo)
        ivPreviewTCDLTrainerChair = view.findViewById(R.id.ivPreviewTCDLTrainerChair)
        ivPreviewTCDLTrainerTable = view.findViewById(R.id.ivPreviewTCDLTrainerTable)
        ivPreviewTCDLWritingBoard = view.findViewById(R.id.ivPreviewTCDLWritingBoard)
        ivPreviewTCDLLightsInNo = view.findViewById(R.id.ivPreviewTCDLLightsInNo)
        ivPreviewTCDLFansInNo = view.findViewById(R.id.ivPreviewTCDLFansInNo)
        ivPreviewTCDLElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewTCDLElectricaPowerBackUpForThRoom)
        ivPreviewTCDLListofDomain = view.findViewById(R.id.ivPreviewTCDLListofDomain)
        ivPreviewTCDLDomainLabPhotogragh = view.findViewById(R.id.ivPreviewTCDLDomainLabPhotogragh)
        ivPreviewTCDLDoes_the_room_has = view.findViewById(R.id.ivPreviewTCDLDoes_the_room_has)

//                     Domain Lab Ajit Ranjan Click On Button
        ivPreviewDLTypeofRoofItLab = view.findViewById(R.id.ivPreviewDLTypeofRoofItLab)
        ivPreviewDLHeightOfCelling = view.findViewById(R.id.ivPreviewDLHeightOfCelling)
        ivPreviewDLFalseCellingProvide = view.findViewById(R.id.ivPreviewDLFalseCellingProvide)
        ivPreviewDLVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewDLVentilationAreaInSqFt)
        ivPreviewDLSoundLevelInDb = view.findViewById(R.id.ivPreviewDLSoundLevelInDb)
        ivPreviewDLwhether_all_the_academic = view.findViewById(R.id.ivPreviewDLwhether_all_the_academic)
        ivPreviewDLAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewDLAcademicRoomInformationBoard)
        ivPreviewDLInternalSignage = view.findViewById(R.id.ivPreviewDLInternalSignage)
        ivPreviewDLCctcCamerasWithAudioFacility = view.findViewById(R.id.ivPreviewDLCctcCamerasWithAudioFacility)
        ivPreviewDLLcdDigitalProjector = view.findViewById(R.id.ivPreviewDLLcdDigitalProjector)
        ivPreviewDLChairForCandidatesInNo = view.findViewById(R.id.ivPreviewDLChairForCandidatesInNo)
        ivPreviewDLTrainerChair = view.findViewById(R.id.ivPreviewDLTrainerChair)
        ivPreviewDLTrainerTable = view.findViewById(R.id.ivPreviewDLTrainerTable)
        ivPreviewDLWritingBoard = view.findViewById(R.id.ivPreviewDLWritingBoard)
        ivPreviewDLLightsInNo = view.findViewById(R.id.ivPreviewDLLightsInNo)
        ivPreviewDLFansInNo = view.findViewById(R.id.ivPreviewDLFansInNo)
        ivPreviewDLElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewDLElectricaPowerBackUpForThRoom)
        ivPreviewDLILListofDomain = view.findViewById(R.id.ivPreviewDLILListofDomain)
        ivPreviewDLDomainLabPhotogragh = view.findViewById(R.id.ivPreviewDLDomainLabPhotogragh)
        ivPreviewDLDoes_the_room_has = view.findViewById(R.id.ivPreviewDLDoes_the_room_has)


//         TCR Ajit Ranjan (PMAYG)Theory Class Room



        ivPreviewTCRTypeofRoofItLab = view.findViewById(R.id.ivPreviewTCRTypeofRoofItLab)
        ivPreviewTCRFalseCellingProvide = view.findViewById(R.id.ivPreviewTCRFalseCellingProvide)
        ivPreviewTCRHeightOfCelling = view.findViewById(R.id.ivPreviewTCRHeightOfCelling)
        ivPreviewTCRVentilationAreaInSqFt = view.findViewById(R.id.ivPreviewTCRVentilationAreaInSqFt)
        ivPreviewTCRSoundLevelInDb = view.findViewById(R.id.ivPreviewTCRSoundLevelInDb)
        ivPreviewTCRwhether_all_the_academic = view.findViewById(R.id.ivPreviewTCRwhether_all_the_academic)
        ivPreviewTCRAcademicRoomInformationBoard = view.findViewById(R.id.ivPreviewTCRAcademicRoomInformationBoard)
        ivPreviewTCRLcdDigitalProjector = view.findViewById(R.id.ivPreviewTCRLcdDigitalProjector)
        ivPreviewTCRChairForCandidatesInNo = view.findViewById(R.id.ivPreviewTCRChairForCandidatesInNo)
        ivPreviewTCRTrainerChair = view.findViewById(R.id.ivPreviewTCRTrainerChair)
        ivPreviewTCRTrainerTable = view.findViewById(R.id.ivPreviewTCRTrainerTable)
        ivPreviewTCRWritingBoard = view.findViewById(R.id.ivPreviewTCRWritingBoard)
        ivPreviewTCRLightsInNo = view.findViewById(R.id.ivPreviewTCRLightsInNo)
        ivPreviewTCRFansInNo = view.findViewById(R.id.ivPreviewTCRFansInNo)
        ivPreviewTCRElectricaPowerBackUpForThRoom = view.findViewById(R.id.ivPreviewTCRElectricaPowerBackUpForThRoom)
        ivPreviewTCRDomainLabPhotogragh = view.findViewById(R.id.ivPreviewTCRDomainLabPhotogragh)


        ivPreviewTCRDoes_the_room_has = view.findViewById(R.id.ivPreviewTCRDoes_the_room_has)
        ivFireFightingEquipmentPreview = view.findViewById(R.id.ivFireFightingEquipmentPreview)
        ivSafeDrinkingWaterPreview = view.findViewById(R.id.ivSafeDrinkingWaterPreview)
        spinnerFirstAidKit = view.findViewById(R.id.spinnerFirstAidKit)
        spinnerSafeDrinkingWater = view.findViewById(R.id.spinnerSafeDrinkingWater)
        editFireFightingEquipment = view.findViewById<TextInputEditText>(R.id.editFireFightingEquipment)

        // ImageView preview  commmon equipment IDs
        ivPowerBackupPreview = view.findViewById(R.id.ivPowerBackupPreview)
        ivBiometricDevicesPreview = view.findViewById(R.id.ivBiometricDevicesPreview)
        ivCCTVPreview = view.findViewById(R.id.ivCCTVPreview)
        ivDocumentStoragePreview = view.findViewById(R.id.ivDocumentStoragePreview)
        ivPrinterScannerPreview = view.findViewById(R.id.ivPrinterScannerPreview)
        ivDigitalCameraPreview = view.findViewById(R.id.ivDigitalCameraPreview)
        ivGrievanceRegisterPreview = view.findViewById(R.id.ivGrievanceRegisterPreview)
        ivMinimumEquipmentPreview = view.findViewById(R.id.ivMinimumEquipmentPreview)
        ivDirectionBoardsPreview = view.findViewById(R.id.ivDirectionBoardsPreview)
        etBiometricDevices = view.findViewById(R.id.etBiometricDevices)
        etPrinterScanner = view.findViewById(R.id.etPrinterScanner)
        etDigitalCamera = view.findViewById(R.id.etDigitalCamera)
        spinnerPowerBackup=view.findViewById(R.id.spinnerPowerBackup)
        spinnerCCTV=view.findViewById(R.id.spinnerCCTV)
        spinnerDocumentStorage=view.findViewById(R.id.spinnerDocumentStorage)
        spinnerGrievanceRegister=view.findViewById(R.id.spinnerGrievanceRegister)
        spinnerMinimumEquipment=view.findViewById(R.id.spinnerMinimumEquipment)
        spinnerDirectionBoards=view.findViewById(R.id.spinnerDirectionBoards)

        // Description of other areas
        ivProofPreview = view.findViewById(R.id.ivProofPreview)
        ivCirculationProofPreview = view.findViewById(R.id.ivCirculationProofPreview)
        ivOpenSpaceProofPreview = view.findViewById(R.id.ivOpenSpaceProofPreview)
        ivParkingProofPreview = view.findViewById(R.id.ivParkingProofPreview)
        etCorridorNo =view.findViewById(R.id.etCorridorNo)
        etDescLength=view.findViewById(R.id.etDescLength)
        etDescWidth =view.findViewById(R.id.etDescWidth)
        etArea =view.findViewById(R.id.etArea)
        etLights =view.findViewById(R.id.etLights)
        etFans=view.findViewById(R.id.etFans)
        etCirculationArea =view.findViewById(R.id.etCirculationArea)
        etOpenSpace =view.findViewById(R.id.etOpenSpace)
        etExclusiveParkingSpace =view.findViewById(R.id.etExclusiveParkingSpace)

        //Button Final Submit
        btnCalculateArea = view.findViewById(R.id.btnCalculateArea)
        btnSubmitFinal = view.findViewById(R.id.btnSubmitFinal)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check and request permission
        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }

        // Setup Yes/No adapter
        val yesNoAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Yes", "No")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        ///////////////////////////
        val overheadTankOptions = listOf("--Select--", "Available", "Not Available")
        val flooringOptions = listOf("--Select--", "Cement", "TILE", "Polished")
        // Find your AutoCompleteTextViews in fragment or activity
        //val actvOverheadTanks: AutoCompleteTextView = view.findViewById(R.id.actvOverheadTanks)
        //val actvTypeOfFlooring: AutoCompleteTextView = view.findViewById(R.id.actvTypeOfFlooring)

// Create ArrayAdapter for Overhead Tanks dropdown
        val overheadTankAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            overheadTankOptions
        )

// Create ArrayAdapter for Flooring dropdown
        val flooringAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            flooringOptions
        )

// Set adapter on AutoCompleteTextViews
        actvOverheadTanks.setAdapter(overheadTankAdapter)
        actvTypeOfFlooring.setAdapter(flooringAdapter)

        // Setup CCTV & Electrical Spinners& upport infra spinners
        val spinners = listOf(
            R.id.spinnerMonitorAccessible, R.id.spinnerConformance, R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP, R.id.spinnerIPEnabled, R.id.spinnerResolution,
            R.id.spinnerVideoStream, R.id.spinnerRemoteAccessBrowser, R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols, R.id.spinnerColorVideoAudio, R.id.spinnerStorageFacility,
            R.id.spinnerSwitchBoards, R.id.spinnerCandidateSafety,R.id.spinnerFirstAidKit,R.id.spinnerSafeDrinkingWater,R.id.spinnerPowerBackup,R.id.spinnerCCTV,R.id.spinnerStorage,R.id.spinnerSecure


//               IT LAB
            , R.id.spinnerITLepbftr,R.id.spinnerITLFalseCellingProvide,R.id.spinnerITLAcademicRoomInformationBoard
            ,R.id.spinnerITLInternalSignage,R.id.spinnerITLCctcCamerasWithAudioFacility,R.id.spinnerITLabInternetConnections
            ,R.id.spinnerITLDoAllComputersHaveTypingTutor,R.id.spinnerITLTrainerChair,R.id.spinnerITLTrainerTable
            ,R.id.spinnerITLtLabPhotograph,R.id.spinnerITLDoes_the_room_has,



//           OfficeCum
            R.id.spinnerOfficeCumFalseCellingProvide,R.id.spinnerOfficeCumLepbftr,R.id.spinnerOfficeCumTableOfofficeCumpter,

//               OfficeCum
            R.id.spinnerReceptionAreaEPBR,
//               CounsellingRoomArea

            R.id.spinnerCounsellingRoomAreaPhotograph,

//               Office Room
            R.id.spinnerORFalseCellingProvide,
            R.id.spinnerORTableOfofficeCumpter,
            R.id.spinnerORPOEPBFTR,



//               IT Come Domain Lab


            R.id.spinnerITCDLwhether_all_the_academic,
            R.id.spinnerITCDLAcademicRoomInformationBoard,
            R.id.spinnerITCDLInternalSignage,
            R.id.spinnerITCDLCctcCamerasWithAudioFacility,
            R.id.spinnerITCDLInternetConnections,
            R.id.spinnerITCDLDoAllComputersHaveTypingTutor,
            R.id.spinnerITCDLTrainerChair,
            R.id.spinnerITCDLTrainerTable,
            R.id.spinnerITCDLElectricaPowerBackUp,
            R.id.spinnerITCDLDoes_the_room_has,

//               Theory Cum IT Lab Ajit Ranjan Spinner's Id

            R.id.spinnerTCILwhether_all_the_academic,
            R.id.spinnerTCILLAcademicRoomInformationBoard,
            R.id.spinnerTCILInternalSignage,
            R.id.spinnerTCILCctcCamerasWithAudioFacility,
            R.id.spinnerTCILInternetConnections,
            R.id.spinnerTCILDLDoAllComputersHaveTypingTutor,
            R.id.spinnerTCILIPowerBackup,
            R.id.spinnerTCILDLDoes_the_room_has,

//               Theory Cum Domain Lab Ajit Ranjan Spinner's Id

            R.id.spinnerTCDLwhether_all_the_academic,
            R.id.spinnerTCDLAcademicRoomInformationBoard,
            R.id.spinnerTCDLInternalSignage,
            R.id.spinnerTCDLCctcCamerasWithAudioFacility,
            R.id.spinnerTCDLPowerBackup,
            R.id.spinnerTCDLDoes_the_room_has,


//               Domain Lab Ajit Ranjan Spinner's Id

            R.id.spinnerDLAcademicRoomInformationBoard,
            R.id.spinnerDLInternalSignage,
            R.id.spinnerDLCctcCamerasWithAudioFacility,

            R.id.spinnerDLElectricaPowerBackUp,
            R.id.spinnerDLwhether_all_the_academic,
            R.id.spinnerDLListofDomain,
            R.id.spinnerDLDoes_the_room_has,


//                  TCR
            R.id.spinnerTCRwhether_all_the_academic,
            R.id.spinnerTCRAcademicRoomInformationBoard,
            R.id.spinnerTCRInternalSignage,
            R.id.spinnerTCRCctcCamerasWithAudioFacility,
            R.id.spinnerTCRDoes_the_room_has,
            R.id.spinnerTCRPowerBackup,






            )








        spinners.forEach {
            view.findViewById<Spinner>(it).adapter = yesNoAdapter
        }
        val spinnersWithButtons = listOf(
            Pair(view.findViewById<Spinner>(R.id.spinnerMonitorAccessible), view.findViewById<Button>(R.id.btnUploadMonitorPhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerConformance), view.findViewById<Button>(R.id.btnUploadConformancePhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerStorage), view.findViewById<Button>(R.id.btnUploadStoragePhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDVRStaticIP), view.findViewById<Button>(R.id.btnUploadDVRPhoto)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSwitchBoards), view.findViewById<Button>(R.id.btnUploadSwitchBoards)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSecure), view.findViewById<Button>(R.id.btnUploadSecuringWires)),
            Pair(view.findViewById<Spinner>(R.id.spinnerLeakageCheck), view.findViewById<Button>(R.id.btnUploadLeaSkageProof)),
            Pair(view.findViewById<Spinner>(R.id.spinnerProtectionStairs), view.findViewById<Button>(R.id.btnUploadProtectionStairs)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTrainingCentreNameBoard), view.findViewById<Button>(R.id.btnUploadTrainingCentreNameBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerActivitySummaryBoard), view.findViewById<Button>(R.id.btnUploadActivitySummaryBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerEntitlementBoard), view.findViewById<Button>(R.id.btnUploadEntitlementBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerImportantContacts), view.findViewById<Button>(R.id.btnUploadImportantContacts)),
            Pair(view.findViewById<Spinner>(R.id.spinnerBasicInfoBoard), view.findViewById<Button>(R.id.btnUploadBasicInfoBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerCodeOfConductBoard), view.findViewById<Button>(R.id.btnUploadCodeOfConductBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerAttendanceSummaryBoard), view.findViewById<Button>(R.id.btnUploadAttendanceSummaryBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerFirstAidKit), view.findViewById<Button>(R.id.btnUploadFirstAidKit)),
            Pair(view.findViewById<Spinner>(R.id.spinnerSafeDrinkingWater), view.findViewById<Button>(R.id.btnUploadSafeDrinkingWater)),
            Pair(view.findViewById<Spinner>(R.id.spinnerPowerBackup), view.findViewById<Button>(R.id.btnUploadPowerBackup)),
            Pair(view.findViewById<Spinner>(R.id.spinnerCCTV), view.findViewById<Button>(R.id.btnUploadCCTV)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDocumentStorage), view.findViewById<Button>(R.id.btnUploadDocumentStorage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerGrievanceRegister), view.findViewById<Button>(R.id.btnSubmitGeneralDetails)),
            Pair(view.findViewById<Spinner>(R.id.spinnerMinimumEquipment), view.findViewById<Button>(R.id.btnUploadMinimumEquipment)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDirectionBoards), view.findViewById<Button>(R.id.btnUploadDirectionBoards)),



//                    It Lab  Ajit Ranjan
            Pair(view.findViewById<Spinner>(R.id.spinnerITLepbftr), view.findViewById<Button>(R.id.btnITLElectricaPowerBackUpForThRoom)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLFalseCellingProvide), view.findViewById<Button>(R.id.btnITLFalseCellingProvide)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnITLAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLInternalSignage), view.findViewById<Button>(R.id.btnITLInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnITLCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLabInternetConnections), view.findViewById<Button>(R.id.btnITLInternetConnections)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLDoAllComputersHaveTypingTutor), view.findViewById<Button>(R.id.btnITLDoAllComputersHaveTypingTutor)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLTrainerChair), view.findViewById<Button>(R.id.btnITLTrainerChair)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLTrainerTable), view.findViewById<Button>(R.id.btnITLTrainerTable)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLtLabPhotograph), view.findViewById<Button>(R.id.btnITLItLabPhotograph)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITLDoes_the_room_has), view.findViewById<Button>(R.id.btnITLLDoes_the_room_has)),

//                    Office Cum(Counselling room)   Ajit Ranjan

            Pair(view.findViewById<Spinner>(R.id.spinnerOfficeCumFalseCellingProvide), view.findViewById<Button>(R.id.btnOfficeCumFalseCellingProvide)),
            Pair(view.findViewById<Spinner>(R.id.spinnerOfficeCumLepbftr), view.findViewById<Button>(R.id.btnOfficeCumElectricialPowerBackup)),
            Pair(view.findViewById<Spinner>(R.id.spinnerOfficeCumTableOfofficeCumpter), view.findViewById<Button>(R.id.btnOfficeCumTableOfofficeCumpter)),

//            ReceptionArea

            Pair(view.findViewById<Spinner>(R.id.spinnerReceptionAreaEPBR), view.findViewById<Button>(R.id.btnReceptionAreaPhotogragh)),



            Pair(view.findViewById<Spinner>(R.id.spinnerCounsellingRoomAreaPhotograph), view.findViewById<Button>(R.id.btnCounsellingRoomAreaPhotograph)),

//            Office Room
            Pair(view.findViewById<Spinner>(R.id.spinnerORFalseCellingProvide), view.findViewById<Button>(R.id.btnORFalseCellingProvide)),
            Pair(view.findViewById<Spinner>(R.id.spinnerORTableOfofficeCumpter), view.findViewById<Button>(R.id.btnORTableOfofficeCumpter)),
            Pair(view.findViewById<Spinner>(R.id.spinnerORPOEPBFTR), view.findViewById<Button>(R.id.btnORElectricialPowerBackup)),

//            IT Come Domain Lab

            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLwhether_all_the_academic), view.findViewById<Button>(R.id.btnITDLwhether_all_the_academic)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnITCDLAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLInternalSignage), view.findViewById<Button>(R.id.btnITCDLInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnITCDLCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLInternetConnections), view.findViewById<Button>(R.id.btnITCDLInternetConnections)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLDoAllComputersHaveTypingTutor), view.findViewById<Button>(R.id.btnITCDLDoAllComputersHaveTypingTutor)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLTrainerChair), view.findViewById<Button>(R.id.btnITCDLTrainerChair)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLTrainerTable), view.findViewById<Button>(R.id.btnITCDLTrainerTable)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLElectricaPowerBackUp), view.findViewById<Button>(R.id.btnITCDLElectricaPowerBackUpForThRoom)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLDoes_the_room_has), view.findViewById<Button>(R.id.btnITCDLDoes_the_room_has)),

//                    Theory Cum IT Lab Ajit Ranjan Spinner set adapter


            Pair(view.findViewById<Spinner>(R.id.spinnerTCILwhether_all_the_academic), view.findViewById<Button>(R.id.btnITDLwhether_all_the_academic)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILLAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnITCDLAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILInternalSignage), view.findViewById<Button>(R.id.btnITCDLInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnITCDLCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILInternetConnections), view.findViewById<Button>(R.id.btnITCDLInternetConnections)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILIPowerBackup), view.findViewById<Button>(R.id.btnTCILElectricaPowerBackUpForThRoom)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILDLDoAllComputersHaveTypingTutor), view.findViewById<Button>(R.id.btnITCDLDoAllComputersHaveTypingTutor)),
            Pair(view.findViewById<Spinner>(R.id.spinnerITCDLTrainerChair), view.findViewById<Button>(R.id.btnITCDLTrainerChair)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCILDLDoes_the_room_has), view.findViewById<Button>(R.id.btnITCDLTrainerTable)),
//                    Theory Cum Domain Lab Ajit Ranjan Spinner set adapter
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLwhether_all_the_academic), view.findViewById<Button>(R.id.btnTCDLwhether_all_the_academic)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnTCDLAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLInternalSignage), view.findViewById<Button>(R.id.btnTCDLInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnTCDLCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLPowerBackup), view.findViewById<Button>(R.id.btnTCDLElectricaPowerBackUpForThRoom)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCDLDoes_the_room_has), view.findViewById<Button>(R.id.btnTCDLDoes_the_room_has)),



            //                    Domain Lab Ajit Ranjan Spinner set adapter
            Pair(view.findViewById<Spinner>(R.id.spinnerDLAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnDLAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLInternalSignage), view.findViewById<Button>(R.id.btnDLInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnDLCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLElectricaPowerBackUp), view.findViewById<Button>(R.id.btnDLElectricaPowerBackUpForThRoom)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLwhether_all_the_academic), view.findViewById<Button>(R.id.btnDLwhether_all_the_academic)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLListofDomain), view.findViewById<Button>(R.id.btnDLILListofDomain)),
            Pair(view.findViewById<Spinner>(R.id.spinnerDLDoes_the_room_has), view.findViewById<Button>(R.id.btnDLDoes_the_room_has)),



//                TCR

            Pair(view.findViewById<Spinner>(R.id.spinnerTCRwhether_all_the_academic), view.findViewById<Button>(R.id.btnTCRwhether_all_the_academic)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCRAcademicRoomInformationBoard), view.findViewById<Button>(R.id.btnTCRAcademicRoomInformationBoard)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCRInternalSignage), view.findViewById<Button>(R.id.btnTCRInternalSignage)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCRCctcCamerasWithAudioFacility), view.findViewById<Button>(R.id.btnTCRCctcCamerasWithAudioFacility)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCRDoes_the_room_has), view.findViewById<Button>(R.id.btnTCRDoes_the_room_has)),
            Pair(view.findViewById<Spinner>(R.id.spinnerTCRPowerBackup), view.findViewById<Button>(R.id.btnTCRElectricaPowerBackUpForThRoom)),






            )

        spinnersWithButtons.forEach { (spinner, button) ->
            spinner.adapter = yesNoAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selected = parent.getItemAtPosition(position).toString()
                    //button.visibility = if (selected == "No") View.GONE else View.VISIBLE
                    button.visibility = View.VISIBLE
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }



//        It Lab    Spinner Id Ajit Ranjan
        spinnerITLepbftr = view.findViewById(R.id.spinnerITLepbftr)
        spinnerITLFalseCellingProvide = view.findViewById(R.id.spinnerITLFalseCellingProvide)
        spinnerITLAcademicRoomInformationBoard = view.findViewById(R.id.spinnerITLAcademicRoomInformationBoard)
        spinnerITLInternalSignage = view.findViewById(R.id.spinnerITLInternalSignage)
        spinnerITLCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerITLCctcCamerasWithAudioFacility)
        spinnerITLabInternetConnections = view.findViewById(R.id.spinnerITLabInternetConnections)
        spinnerITLwhether_all_the_academic = view.findViewById(R.id.spinnerITLwhether_all_the_academic)
        spinnerITLDoAllComputersHaveTypingTutor = view.findViewById(R.id.spinnerITLDoAllComputersHaveTypingTutor)
        spinnerITLTrainerChair = view.findViewById(R.id.spinnerITLTrainerChair)
        spinnerITLTrainerTable = view.findViewById(R.id.spinnerITLTrainerTable)
        spinnerITLtLabPhotograph = view.findViewById(R.id.spinnerITLtLabPhotograph)
        spinnerITLDoes_the_room_has = view.findViewById(R.id.spinnerITLDoes_the_room_has)

//        Spinner setAdapter Ajit Ranjan
        spinnerITLepbftr.setAdapter(yesNoAdapter)
        spinnerITLFalseCellingProvide.setAdapter(yesNoAdapter)
        spinnerITLwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerITLAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerITLInternalSignage.setAdapter(yesNoAdapter)
        spinnerITLCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerITLabInternetConnections.setAdapter(yesNoAdapter)
        spinnerITLDoAllComputersHaveTypingTutor.setAdapter(yesNoAdapter)
        spinnerITLTrainerChair.setAdapter(yesNoAdapter)
        spinnerITLTrainerTable.setAdapter(yesNoAdapter)
        spinnerITLtLabPhotograph.setAdapter(yesNoAdapter)
        spinnerITLDoes_the_room_has.setAdapter(yesNoAdapter)



//               Office Cum(Counselling room)    Spinner Id Ajit Ranjan
        spinnerOfficeCumFalseCellingProvide = view.findViewById(R.id.spinnerOfficeCumFalseCellingProvide)
        spinnerOfficeCumLepbftr = view.findViewById(R.id.spinnerOfficeCumLepbftr)
        spinnerOfficeCumTableOfofficeCumpter = view.findViewById(R.id.spinnerOfficeCumTableOfofficeCumpter)
//        Spinner setAdapter Ajit Ranjan  Office Cum(Counselling room)
        spinnerOfficeCumFalseCellingProvide.setAdapter(yesNoAdapter)
        spinnerOfficeCumLepbftr.setAdapter(yesNoAdapter)
        spinnerOfficeCumTableOfofficeCumpter.setAdapter(yesNoAdapter)
//      ReceptionArea  Spinner Id Ajit Ranjan

        spinnerReceptionAreaEPBR = view.findViewById(R.id.spinnerReceptionAreaEPBR)

        spinnerOfficeCumLepbftr.setAdapter(yesNoAdapter)

//        CounsellingRoomArea
        spinnerCounsellingRoomAreaPhotograph = view.findViewById(R.id.spinnerCounsellingRoomAreaPhotograph)
        spinnerCounsellingRoomAreaPhotograph.setAdapter(yesNoAdapter)

//          Office Room

        spinnerORFalseCellingProvide = view.findViewById(R.id.spinnerORFalseCellingProvide)
        spinnerORTableOfofficeCumpter = view.findViewById(R.id.spinnerORTableOfofficeCumpter)
        spinnerORPOEPBFTR = view.findViewById(R.id.spinnerORPOEPBFTR)
        spinnerORFalseCellingProvide.setAdapter(yesNoAdapter)
        spinnerORTableOfofficeCumpter.setAdapter(yesNoAdapter)
        spinnerORPOEPBFTR.setAdapter(yesNoAdapter)

//            IT Come Domain Lab
        spinnerITCDLwhether_all_the_academic = view.findViewById(R.id.spinnerITCDLwhether_all_the_academic)
        spinnerITCDLAcademicRoomInformationBoard = view.findViewById(R.id.spinnerITCDLAcademicRoomInformationBoard)
        spinnerITCDLInternalSignage = view.findViewById(R.id.spinnerITCDLInternalSignage)
        spinnerITCDLCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerITCDLCctcCamerasWithAudioFacility)
        spinnerITCDLInternetConnections = view.findViewById(R.id.spinnerITCDLInternetConnections)
        spinnerITCDLDoAllComputersHaveTypingTutor = view.findViewById(R.id.spinnerITCDLDoAllComputersHaveTypingTutor)
        spinnerITCDLTrainerChair = view.findViewById(R.id.spinnerITCDLTrainerChair)
        spinnerITCDLTrainerTable = view.findViewById(R.id.spinnerITCDLTrainerTable)
        spinnerITCDLElectricaPowerBackUp = view.findViewById(R.id.spinnerITCDLElectricaPowerBackUp)
        spinnerITCDLDoes_the_room_has = view.findViewById(R.id.spinnerITCDLDoes_the_room_has)

        spinnerITCDLwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerITCDLAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerITCDLInternalSignage.setAdapter(yesNoAdapter)
        spinnerITCDLCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerITCDLInternetConnections.setAdapter(yesNoAdapter)
        spinnerITCDLDoAllComputersHaveTypingTutor.setAdapter(yesNoAdapter)
        spinnerITCDLTrainerChair.setAdapter(yesNoAdapter)
        spinnerITCDLTrainerTable.setAdapter(yesNoAdapter)
        spinnerITCDLElectricaPowerBackUp.setAdapter(yesNoAdapter)
        spinnerITCDLDoes_the_room_has.setAdapter(yesNoAdapter)


        //                    Theory Cum IT Lab Ajit Ranjan Spinner set adapter
        spinnerTCILwhether_all_the_academic = view.findViewById(R.id.spinnerTCILwhether_all_the_academic)
        spinnerTCILLAcademicRoomInformationBoard = view.findViewById(R.id.spinnerTCILLAcademicRoomInformationBoard)
        spinnerTCILInternalSignage = view.findViewById(R.id.spinnerTCILInternalSignage)
        spinnerTCILCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerTCILCctcCamerasWithAudioFacility)
        spinnerTCILInternetConnections = view.findViewById(R.id.spinnerTCILInternetConnections)
        spinnerTCILElectricPowerBackup = view.findViewById(R.id.spinnerTCILIPowerBackup)
        spinnerTCILDLDoAllComputersHaveTypingTutor = view.findViewById(R.id.spinnerTCILDLDoAllComputersHaveTypingTutor)
//        spinnerTCILIPEnabled = view.findViewById(R.id.spinnerTCILIPEnabled)
        spinnerTCILDLDoes_the_room_has = view.findViewById(R.id.spinnerTCILDLDoes_the_room_has)

//
        spinnerTCILwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerTCILLAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerTCILInternalSignage.setAdapter(yesNoAdapter)
        spinnerTCILCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerTCILInternetConnections.setAdapter(yesNoAdapter)
        spinnerTCILElectricPowerBackup.setAdapter(yesNoAdapter)
        spinnerTCILDLDoAllComputersHaveTypingTutor.setAdapter(yesNoAdapter)
//        spinnerTCILIPEnabled.setAdapter(yesNoAdapter)
        spinnerTCILDLDoes_the_room_has.setAdapter(yesNoAdapter)
//        Theory Cum Domain Lab Ajit Ranjan Spinner set adapter
        spinnerTCDLwhether_all_the_academic = view.findViewById(R.id.spinnerTCDLwhether_all_the_academic)
        spinnerTCDLAcademicRoomInformationBoard = view.findViewById(R.id.spinnerTCDLAcademicRoomInformationBoard)
        spinnerTCDLInternalSignage = view.findViewById(R.id.spinnerTCDLInternalSignage)
        spinnerTCDLCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerTCDLCctcCamerasWithAudioFacility)
        spinnerTCDLPowerBackup = view.findViewById(R.id.spinnerTCDLPowerBackup)
        spinnerTCDLDoes_the_room_has = view.findViewById(R.id.spinnerTCDLDoes_the_room_has)
        spinnerTCDLwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerTCDLAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerTCDLInternalSignage.setAdapter(yesNoAdapter)
        spinnerTCDLCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerTCDLPowerBackup.setAdapter(yesNoAdapter)
        spinnerTCDLDoes_the_room_has.setAdapter(yesNoAdapter)



        //                    Domain Lab Ajit Ranjan Spinner set adapter




        spinnerDLAcademicRoomInformationBoard = view.findViewById(R.id.spinnerDLAcademicRoomInformationBoard)
        spinnerDLInternalSignage = view.findViewById(R.id.spinnerDLInternalSignage)
        spinnerDLCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerDLCctcCamerasWithAudioFacility)

        spinnerDLElectricaPowerBackUp = view.findViewById(R.id.spinnerDLElectricaPowerBackUp)
        spinnerDLwhether_all_the_academic = view.findViewById(R.id.spinnerDLwhether_all_the_academic)
        spinnerDLListofDomain = view.findViewById(R.id.spinnerDLListofDomain)
        spinnerDLDoes_the_room_has = view.findViewById(R.id.spinnerDLDoes_the_room_has)


        spinnerDLAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerDLInternalSignage.setAdapter(yesNoAdapter)
        spinnerDLCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerDLwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerDLElectricaPowerBackUp.setAdapter(yesNoAdapter)
        spinnerDLListofDomain.setAdapter(yesNoAdapter)
        spinnerDLDoes_the_room_has.setAdapter(yesNoAdapter)


//          TCR Ajit Ranjan(PMAYG)
        spinnerTCRwhether_all_the_academic = view.findViewById(R.id.spinnerTCRwhether_all_the_academic)
        spinnerTCRAcademicRoomInformationBoard = view.findViewById(R.id.spinnerTCRAcademicRoomInformationBoard)
        spinnerTCRInternalSignage = view.findViewById(R.id.spinnerTCRInternalSignage)
        spinnerTCRCctcCamerasWithAudioFacility = view.findViewById(R.id.spinnerTCRCctcCamerasWithAudioFacility)

        spinnerTCRDoes_the_room_has = view.findViewById(R.id.spinnerTCRDoes_the_room_has)
        spinnerTCRPowerBackup = view.findViewById(R.id.spinnerTCRPowerBackup)

        spinnerTCRwhether_all_the_academic.setAdapter(yesNoAdapter)
        spinnerTCRAcademicRoomInformationBoard.setAdapter(yesNoAdapter)
        spinnerTCRInternalSignage.setAdapter(yesNoAdapter)
        spinnerTCRCctcCamerasWithAudioFacility.setAdapter(yesNoAdapter)
        spinnerTCRDoes_the_room_has.setAdapter(yesNoAdapter)
        spinnerTCRPowerBackup.setAdapter(yesNoAdapter)











        // General Details Spinners
        spinnerLeakageCheck = view.findViewById(R.id.spinnerLeakageCheck)
        spinnerProtectionStairs = view.findViewById(R.id.spinnerProtectionStairs)
        spinnerDDUConformance = view.findViewById(R.id.spinnerDDUConformance)
        spinnerCandidateSafety = view.findViewById(R.id.spinnerCandidateSafety)

        spinnerLeakageCheck.setAdapter(yesNoAdapter)
        spinnerProtectionStairs.setAdapter(yesNoAdapter)
        spinnerDDUConformance.setAdapter(yesNoAdapter)
        spinnerCandidateSafety.setAdapter(yesNoAdapter)

        // signage info baords spinners
        spinnerTcNameBoard = view.findViewById(R.id.spinnerTrainingCentreNameBoard)
        spinnerActivityAchievementBoard = view.findViewById(R.id.spinnerActivitySummaryBoard)
        spinnerStudentEntitlementBoard = view.findViewById(R.id.spinnerEntitlementBoard)
        spinnerContactDetailBoard = view.findViewById(R.id.spinnerImportantContacts)
        spinnerBasicInfoBoard = view.findViewById(R.id.spinnerBasicInfoBoard)
        spinnerCodeConductBoard = view.findViewById(R.id.spinnerCodeOfConductBoard)
        spinnerStudentAttendanceBoard = view.findViewById(R.id.spinnerAttendanceSummaryBoard)


        // ImageView setup
        ivTcNameBoardPreview = view.findViewById(R.id.ivTcNameBoardPreview)
        ivActivityAchievementBoardPreview = view.findViewById(R.id.ivActivityAchievementBoardPreview)
        ivStudentEntitlementBoardPreview = view.findViewById(R.id.ivStudentEntitlementBoardPreview)
        ivContactDetailBoardPreview = view.findViewById(R.id.ivContactDetailBoardPreview)
        ivBasicInfoBoardPreview = view.findViewById(R.id.ivBasicInfoBoardPreview)
        ivCodeConductBoardPreview = view.findViewById(R.id.ivCodeConductBoardPreview)
        ivStudentAttendanceBoardPreview = view.findViewById(R.id.ivStudentAttendanceBoardPreview)

        spinnerTcNameBoard.adapter = yesNoAdapter
        spinnerActivityAchievementBoard.adapter = yesNoAdapter
        spinnerStudentEntitlementBoard.adapter = yesNoAdapter
        spinnerContactDetailBoard.adapter = yesNoAdapter
        spinnerBasicInfoBoard.adapter = yesNoAdapter
        spinnerCodeConductBoard.adapter = yesNoAdapter
        spinnerStudentAttendanceBoard.adapter = yesNoAdapter





        // Submit buttons
        view.findViewById<Button>(R.id.btnSubmitCCTVComplianceDetails).setOnClickListener {
            if (validateCCTVForm(view)) submitCCTVData(view)
            else Toast.makeText(
                requireContext(),
                "Complete all CCTV fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitElectricalDetails)?.setOnClickListener {
            if (validateElectricalForm(view)) submitElectricalData(view)
            else Toast.makeText(
                requireContext(),
                "Complete all electrical fields and photos.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitGeneralDetails).setOnClickListener {
            if (validateGeneralDetailsForm()) submitGeneralDetails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields and photos for General Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitTCeDetails).setOnClickListener {
            if (validateTcBasicInfoFields()) submitTCInfoDeatails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for TC Details.",
                Toast.LENGTH_LONG
            ).show()

        }
        view.findViewById<Button>(R.id.btnSignagesInfoDetails).setOnClickListener {
            if (validateSignagesInfoBoards()) submitSignagesInfoBoards()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Signages&InfoBoards Details.",
                Toast.LENGTH_LONG
            ).show()

        }
        view.findViewById<Button>(R.id.btnSubmitSupportInfrastructure).setOnClickListener {
            if (validateSupportInfrastructure()) submitInfraDetails()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Availabilty of support InfraStructure Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        //17th  sept
        view.findViewById<Button>(R.id.btnCommonEquipmentDetails).setOnClickListener {
            if (validateCommonEquipment()) submitCommonEquipment()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Common Equipment Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        view.findViewById<Button>(R.id.btnSubmitDescOtherArea).setOnClickListener {
            if (validateDescriptionForm()) submitDescriptionOtherAreas()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Description of other area Details.",
                Toast.LENGTH_LONG
            ).show()
        }
        //wash basin
        view.findViewById<Button>(R.id.btnWashBasinDetails).setOnClickListener {
            if(validateToiletsAndWashBasins()) submitWashBasins()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Toilet & WashBasin Details.",
                Toast.LENGTH_LONG
            ).show()
        }
//        AJIT RANJAN PMAYG   IT LAB

        view.findViewById<Button>(R.id.btnSubmitITL).setOnClickListener {
            if(validateItLAB()) submitItLab()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for (IT Lab) Details.",
                Toast.LENGTH_LONG
            ).show()
        }

//        Office Cum(Counselling room)    Ajit Ranjan
        view.findViewById<Button>(R.id.btnSubmitOCRDetails).setOnClickListener {
            if(validateOfficeCumCounsellingRoom()) SubmitOfficeCumCounsellingRoom()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Office Cum(Counselling room) Details.",
                Toast.LENGTH_LONG
            ).show()
        }
//        ReceptionArea




        view.findViewById<Button>(R.id.btnSubmitReceptionArea).setOnClickListener {
            if(validateReceptionArea()) SubmitReceptionArea()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields Reception Area  Details.",
                Toast.LENGTH_LONG
            ).show()
        }
//            we are valifdate same validation no require because same feilds and same parameter use in
        view.findViewById<Button>(R.id.btnSubmitCounsellingRoomDetails).setOnClickListener {
            if(validateCounsellingRoom()) SubmitCounsellingRoom()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit CounsellingRoom Details.",
                Toast.LENGTH_LONG
            ).show()
//            SubmitReceptionArea()
        }

        view.findViewById<Button>(R.id.btnSubmitOfficeRoom).setOnClickListener {
            if(validateOfficeRoom()) SubmitOfficeRoom()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit CounsellingRoom Details.",
                Toast.LENGTH_LONG
            ).show()
//            SubmitOfficeRoom()
        }
//
        view.findViewById<Button>(R.id.btnSubmitITCDL).setOnClickListener {
            if(validateITComeDomainLab()) SubmitITComeDomainLab()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit IT Come Domain Lab Details.",
                Toast.LENGTH_LONG
            ).show()
//            SubmitITComeDomainLab()
        }
        view.findViewById<Button>(R.id.btnSubmitTCIL).setOnClickListener {

            if(validateTheoryCumITLab()) SubmitTCITLABLab()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit Theory Cum IT Lab Details.",
                Toast.LENGTH_LONG
            ).show()

//            SubmitTCITLABLab()
        }



        view.findViewById<Button>(R.id.btnSubmitTCDL).setOnClickListener {
            if(validateTheoryCumDomainLab()) SubmitTCDL()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit Theory Cum Domain Lab Details.",
                Toast.LENGTH_LONG
            ).show()

        }
        view.findViewById<Button>(R.id.btnSubmitDL).setOnClickListener {
            if(validateDomainLab()) SubmitDL()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit Domain Lab Details.",
                Toast.LENGTH_LONG
            ).show()

//            SubmitDL()


        }
        view.findViewById<Button>(R.id.btnSubmitTCR).setOnClickListener {
            if(validateTheoryClassRoom()) SubmitTCR()
            else Toast.makeText(
                requireContext(),
                "Please complete all fields for Submit Theory Class Room Details.",
                Toast.LENGTH_LONG
            ).show()
//            SubmitTCR()
        }

        tvArea.isFocusable = false
        tvArea.isClickable = false


        etWidth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                calculateAndShowArea()  // run only when Width changes
            }
        })



        // Calculate Area
        btnCalculateArea.setOnClickListener {
            val length = etDescLength.text.toString().toDoubleOrNull() ?: 0.0
            val width = etDescWidth.text.toString().toDoubleOrNull() ?: 0.0

            etArea.setText("${length * width}")
        }


        view.findViewById<Button>(R.id.btnSubmitAdddMore).setOnClickListener {



            LayoutLinear.visibility = View.VISIBLE

        }

        // Spinner values
        val items = listOf(
            "Select Area",
            "Office Cum Counselling",
            "Reception Area",
            "Counselling Room",
            "Office Room",
            "IT cum Domain Lab",
            "Theory Cum IT Lab",
            "Theory Cum Domain Lab",
            "IT Lab",
            "Domain Lab",
            "Theory Class Room"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        AcsdemicSpinner.adapter = adapter

        AcsdemicSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()

//                if (selectedItem == "Select Area") return
                RoomType=selectedItem
//                    Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT)
//                    .show()
                when (selectedItem) {
                    "Select Area" -> {

                        etLength.visibility = View.GONE
                        etWidth.visibility = View.GONE
                        tvArea.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "Office Cum Counselling" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_office_counselling_room.visibility = View.VISIBLE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "Theory Cum Domain Lab" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.VISIBLE
                        lin_theory_class_room.visibility = View.GONE
                    }

                    "Reception Area" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.VISIBLE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "Counselling Room" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.VISIBLE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "Office Room" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.VISIBLE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "IT cum Domain Lab" ->{
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.VISIBLE
                        lin_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE
                    }
                    "Theory Cum IT Lab" ->{
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.VISIBLE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE

                    }
                    "IT Lab" ->{
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_itlab.visibility = View.VISIBLE

                        lin_domain_lab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE

                    }
                    "Domain Lab" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.  GONE
                        lin_itlab.visibility = View.  GONE
                        lin_office_room.visibility = View.  GONE
                        lin_non_reception.visibility = View.  GONE
                        lin_domain_lab.visibility = View.VISIBLE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.GONE

                    }
                    "Theory Class Room" -> {
                        etLength.visibility = View.VISIBLE
                        etWidth.visibility = View.VISIBLE
                        tvArea.visibility = View.VISIBLE
                        lin_office_counselling_room.visibility = View.GONE
                        lin_tcum_domain_lab.visibility = View.GONE
                        lin_counselling_room.visibility = View.GONE
                        lin_itlab.visibility = View.GONE
                        lin_office_room.visibility = View.GONE
                        lin_non_reception.visibility = View.GONE
                        lin_domain_lab.visibility = View.GONE
                        lin_theory_cum_it_lab.visibility = View.GONE
                        lin_theory_cum_domain_lab.visibility = View.GONE
                        lin_theory_class_room.visibility = View.VISIBLE
                    }

                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }


        // Final Submit
        btnSubmitFinal.setOnClickListener {
            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                sanctionOrder = sanctionOrder,
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getFinalSubmitData(requestTcInfraReq)

        }

        // Observers



//         It Lab  Ajit Ranjan PMAYG
        viewModel.insertITTabDtails.observe(viewLifecycleOwner) {

                result ->
            result.onSuccess { response ->
                // ✅ Dismiss progress dialog
                ProgressDialogUtil.dismissProgressDialog()
                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)

                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc
//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewITLTypeofRoofItLab= null
                base64ProofITLFalseCellingProvide= null
                base64ProofITLHeightOfCelling= null
                base64ProofITLVentilationAreaInSqFt= null
                base64ProofITLSoundLevelAsPerSpecifications= null
                base64ProofITLSoundLevelInDb= null
                base64ProofITLwhether_all_the_academic= null
                base64ProofITLAcademicRoomInformationBoard= null
                base64ProofITLInternalSignage= null
                base64ProofITLCctcCamerasWithAudioFacility= null
                base64ProofITLLanEnabledComputersInNo= null
                base64ProofITLInternetConnections= null
                base64ProofITLDoAllComputersHaveTypingTutor= null
                base64ProofITLTablets= null
                base64ProofITLStoolsChairs= null
                base64ProofITLTrainerChair= null
                base64ProofITLTrainerTable= null
                base64ProofITLLightsInNo= null
                base64ProofITLFansInNo= null
                base64ProofITLElectricaPowerBackUpForThRoom= null
                base64ProofITLItLabPhotograph= null
                base64ProofITLDoes_the_room_has= null











            }
            result.onFailure {
                // ✅ Dismiss progress dialog
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas(IT Lab) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
//        OfficeCumCounsellingroom  Ajit Ranjan PMAYG
        viewModel.OfficeCumCounsellingroom.observe(viewLifecycleOwner){ result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()
                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)
                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc

//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewOfficeRoomPhotograph= null
                base64ProofOfficeCumTypeofRoofItLab= null
                base64ProofOfficeCumFalseCellingProvide= null
                base64ProofOfficeCumHeightOfCelling= null
                base64ProofOfficeCumSplaceforSecuringDoc= null
                base64ProofOfficeCumAnOfficeTableNo= null
                base64ProofOfficeCumChairs= null
                base64ProofOfficeCumTableOfofficeCumpter= null
                base64ProofOfficeCumPrinterCumScannerInNo= null
                base64ProofOfficeCumDigitalCameraInNo= null
                base64ProofOfficeCumElectricialPowerBackup= null












            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Office Cum Counselling room details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
//        ReceptionAreaEPBR
        viewModel.ReceptionAreaServices.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()
                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)
                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
//                    val responseDesc = response.responseDesc
//                    AlertDialog.Builder(context)
//                        .setTitle("Success")
//                        .setCancelable(false)
//                        .setMessage(responseDesc)
//                        .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                        }
//                        .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewReceptionAreaPhotogragh= null
            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
//                    " Description Of ReceptionArea details submission failed: ${it.message}",
                    " details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        //        Office Room  Ajit Ranjan PMAYG
        viewModel.Officeroom.observe(viewLifecycleOwner){ result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)
                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc
//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewOROfficeRoomORPhotograph= null
                base64ProofORTypeofRoofItLab= null
                base64ProofORFalseCellingProvide= null
                base64ProofORHeightOfCelling= null
                base64ProofORSplaceforSecuringDoc= null
                base64ProofORAnOfficeTableNo= null
                base64ProofORChairs= null
                base64ProofORTableOfofficeCumpter= null
                base64ProofORPrinterCumScannerInNo= null
                base64ProofORDigitalCameraInNo= null
                base64ProofORElectricialPowerBackup= null












            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Office Room details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
//        ITComeDomainLab  Ajit Ranjan PMAYG
        viewModel.ITComeDomainLab.observe(viewLifecycleOwner) {
                result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()
                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)

                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc

//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewITCDLTypeofRoofItLab= null
                base64ProofITCDLFalseCellingProvide= null
                base64ProofITCDLabHeightOfCelling= null
                base64ProofITCDLVentilationAreaInSqFt= null
                base64ProofITCDLabSoundLevelInDb= null
                base64ProofITCDLwhether_all_the_academic= null
                base64ProofITCDLAcademicRoomInformationBoard= null
                base64ProofITCDLInternalSignage= null
                base64ProofITCDLCctcCamerasWithAudioFacility= null
                base64ProofITCDLLanEnabledComputersInNo= null
                base64ProofITCDLInternetConnections= null
                base64ProofITCDLDoAllComputersHaveTypingTutor= null
                base64ProofITCDLTablets= null
                base64ProofITCDLStoolsChairs= null
                base64ProofITCDLTrainerChair= null
                base64ProofITCDLLightsInNo= null
                base64ProofITCDLTrainerTable= null
                base64ProofITCDLFansInNo= null
                base64ProofITCDLElectricaPowerBackUpForThRoom= null
                base64ProofITCDLItLabPhotograph= null
//              base64ProofITCDLListofDomain= null
                base64ProofITCDLDoes_the_room_has= null
            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas(IT Come Domain Lab) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
//        Theory Cum IT Lab  Ajit Ranjan PMAYG
        viewModel.TheoryCumITLab.observe(viewLifecycleOwner) {
                result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()


                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)
                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc
//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }

                base64ProofPreviewTCILListofDomain= null
                base64ProofPreviewTCILTypeofRoofItLab= null
                base64ProofPreviewTCILFalseCellingProvide= null
                base64ProofPreviewTCILHeightOfCelling= null
                base64ProofPreviewTCILVentilationAreaInSqFt= null
                base64ProofPreviewTTCILSoundLevelInDb= null
                base64ProofPreviewTCILwhether_all_the_academic= null
                base64ProofPreviewTCILAcademicRoomInformationBoard= null
                base64ProofPreviewTCILInternalSignage= null
                base64ProofPreviewTCILCctcCamerasWithAudioFacility= null
                base64ProofPreviewTCILLanEnabledComputersInNo= null
                base64ProofPreviewTCILInternetConnections= null
                base64ProofPreviewTCILDoAllComputersHaveTypingTutor= null
                base64ProofPreviewTCILTablets= null
                base64ProofPreviewTCILStoolsChairs= null
                base64ProofPreviewTCILTrainerTable= null
                base64ProofPreviewTCILTrainerChair= null
                base64ProofPreviewTCILLightsInNo= null
                base64ProofPreviewTCILFansInNo= null
                base64ProofPreviewTCILElectricaPowerBackUpForThRoom= null
                base64ProofPreviewTCILTheoryCumItLabPhotogragh= null
                base64ProofPreviewTCILDoes_the_room_has= null



            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas( Theory Cum IT Lab) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
//        Theory Cum Domain Lab  Ajit Ranjan PMAYG
        viewModel.TheoryCumDomainLab.observe(viewLifecycleOwner) {
                result ->

            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()
                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)
                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc

//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewTCDLTypeofRoofItLab= null
                base64ProofPreviewTCDLFalseCellingProvide= null
                base64ProofPreviewTCDLHeightOfCelling= null
                base64ProofPreviewTCDLVentilationAreaInSqFt= null
                base64ProofPreviewTCDLSoundLevelInDb= null
                base64ProofPreviewTCDLwhether_all_the_academic= null
                base64ProofPreviewTCDLAcademicRoomInformationBoard= null
                base64ProofPreviewTCDLInternalSignage= null
                base64ProofPreviewTCDLCctcCamerasWithAudioFacility= null
                base64ProofPreviewTCDLLcdDigitalProjector= null
                base64ProofPreviewTCDLChairForCandidatesInNo= null
                base64ProofPreviewTCDLTrainerChair= null
                base64ProofPreviewTCDLTrainerTable= null
                base64ProofPreviewTCDLWritingBoard= null
                base64ProofPreviewTCDLLightsInNo= null
                base64ProofPreviewTCDLFansInNo= null
                base64ProofPreviewTCDLElectricaPowerBackUpForThRoom= null
                base64ProofPreviewTCDLListofDomain= null
                base64ProofPreviewTCDLDomainLabPhotogragh= null
                base64ProofPreviewTCDLDoes_the_room_has= null
            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas( Theory Cum Domain Lab) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

//        Domain Lab  Ajit Ranjan PMAYG
        viewModel.DomainLab.observe(viewLifecycleOwner) {
                result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()

                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)

                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")
                val responseDesc = response.responseDesc
//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//                        findNavController().navigateUp()  // ✅ go back
//                    }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewDLTypeofRoofItLab= null
                base64ProofPreviewDLFalseCellingProvide= null
                base64ProofPreviewDLHeightOfCelling= null
                base64ProofPreviewDLVentilationAreaInSqFt= null
                base64ProofPreviewDLSoundLevelInDb= null
                base64ProofPreviewDLwhether_all_the_academic= null
                base64ProofPreviewDLAcademicRoomInformationBoard= null
                base64ProofPreviewDLInternalSignage= null
                base64ProofPreviewDLCctcCamerasWithAudioFacility= null
                base64ProofPreviewDLLcdDigitalProjector= null
                base64ProofPreviewDLChairForCandidatesInNo= null
                base64ProofPreviewDLTrainerChair= null
                base64ProofPreviewDLTrainerTable= null
                base64ProofPreviewDLWritingBoard= null
                base64ProofPreviewDLLightsInNo= null
                base64ProofPreviewDLFansInNo= null
                base64ProofPreviewDLElectricaPowerBackUpForThRoom= null
                base64ProofPreviewDLDomainLabPhotogragh= null
                base64ProofPreviewDLDoes_the_room_has= null
            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas(Domain Lab) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

//        Theory Class Room
        viewModel.TheoryClassRoom.observe(viewLifecycleOwner) {
                result ->
            result.onSuccess { response ->
                ProgressDialogUtil.dismissProgressDialog()

                // ✅ Print/log all values
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonResponse = gson.toJson(response)

                Log.d("InsertITTabDetails", "✅ Success Response:\n$jsonResponse")

//                val requestDate = response.requestDate
//                val responseCode = response.responseCode
                val responseDesc = response.responseDesc

//                AlertDialog.Builder(context)
//                    .setTitle("Success")
//                    .setCancelable(false)
//                    .setMessage(responseDesc)
//                    .setPositiveButton("Yes") { _, _ ->
                RecyClerViewUI()
//            }
//                    .show()
                val otherAreaSection = view?.findViewById<ViewGroup>(R.id.layoutdescription_of_academicContent)
                otherAreaSection?.let { AppUtil.clearAllInputs(it) }
                base64ProofPreviewTCRTypeofRoofItLab= null
                base64ProofPreviewTCRFalseCellingProvide= null
                base64ProofPreviewTCRHeightOfCelling= null
                base64ProofPreviewTCRVentilationAreaInSqFt= null
                base64ProofPreviewTCRSoundLevelInDb= null
                base64ProofPreviewTCRwhether_all_the_academic= null
                base64ProofPreviewTCRAcademicRoomInformationBoard= null
                base64ProofPreviewTCRInternalSignage= null
                base64ProofPreviewTCRCctcCamerasWithAudioFacility= null
                base64ProofPreviewTCRLcdDigitalProjector= null
                base64ProofPreviewTCRChairForCandidatesInNo= null
                base64ProofPreviewTCRTrainerChair= null
                base64ProofPreviewTCRTrainerTable= null
                base64ProofPreviewTCRWritingBoard= null
                base64ProofPreviewTCRLightsInNo= null
                base64ProofPreviewTCRFansInNo= null
                base64ProofPreviewTCRElectricaPowerBackUpForThRoom= null
                base64ProofPreviewTCRDomainLabPhotogragh= null
                base64ProofPreviewTCRDoes_the_room_has= null
            }
            result.onFailure {
                ProgressDialogUtil.dismissProgressDialog()
                Toast.makeText(
                    requireContext(),
                    " Description Of Academic/Non-Academic Areas(Theory Class Room) details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }










        viewModel.insertCCTVdata.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "CCTV data submitted successfully!",
                    Toast.LENGTH_LONG
                ).show()
                binding.layoutCCTVComplianceContent.gone()
                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)



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
                    Toast.LENGTH_LONG
                ).show()

                binding.layoutElectricalWiringContent.gone()

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)

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
                    Toast.LENGTH_LONG
                ).show()
                binding.layoutGeneralDetailsContent.gone()

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)

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
                    Toast.LENGTH_LONG
                ).show()

                binding.layoutTCBasicInfoContent.gone()
                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)
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
                    Toast.LENGTH_LONG
                ).show()
                binding.layoutSignagesInfoBoardsContent.gone()

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)

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
                    Toast.LENGTH_LONG
                ).show()


                binding.layoutSupportInfrastructureContent.gone()
                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)

            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Support Infrastructure details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        viewModel.insertCommonEquipDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Common equipment details submitted successfully!",
                    Toast.LENGTH_LONG
                ).show()

                binding.layoutCommonEquipmentContent.gone()

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)


            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    " Common equipment details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        viewModel.insertDescAreaDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Description of other area details submitted successfully!",
                    Toast.LENGTH_LONG
                ).show()
                binding.layoutDescriptionOtherAreasContent.gone()
                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)


            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    " Description of other area  details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        viewModel.insertWashBasinDtails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(
                    requireContext(),
                    "Toilet & WashBasin details submitted successfully!",
                    Toast.LENGTH_LONG
                ).show()

                binding.layoutToiletsWashBasinsContent.gone()

                val requestTcInfraReq = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                viewModel.getSectionsStatusData(requestTcInfraReq)

            }
            result.onFailure {
                Toast.makeText(
                    requireContext(),
                    " Toilet & WashBasin details submission failed: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }
    private fun setupExpandableSections(view: View) {
        val sections = listOf(
            Triple(R.id.headerTCBasicInfo, R.id.layoutTCBasicInfoContent, R.id.ivToggleTCBasicInfo),
            /*Triple(
                R.id.headerInfrastructure,
                R.id.layoutInfrastructureContent,
                R.id.ivToggleInfrastructure
            ),*/
            Triple(
                R.id.headerCCTVCompliance,
                R.id.layoutCCTVComplianceContent,
                R.id.ivToggleCCTVCompliance
            ),
            Triple(
                R.id.headerElectricalWiring,
                R.id.layoutElectricalWiringContent,
                R.id.ivToggleElectricalWiring
            ),
            Triple(
                R.id.headerGeneralDetails,
                R.id.layoutGeneralDetailsContent,
                R.id.ivToggleGeneralDetails
            ),
            Triple(
                R.id.headerCommonEquipment,
                R.id.layoutCommonEquipmentContent,
                R.id.ivToggleCommonEquipment
            ),
            Triple(
                R.id.headerSignagesInfoBoards,
                R.id.layoutSignagesInfoBoardsContent,
                R.id.ivToggleSignagesInfoBoards
            ),
            /* Triple(
                 R.id.headerAvailableTrainers,
                 R.id.layoutAvailableTrainersContent,
                 R.id.ivToggleAvailableTrainers
             ),*/
            /*Triple(
                R.id.headerTeachingLearningMaterials,
                R.id.layoutTeachingLearningMaterialsContent,
                R.id.ivToggleTeachingLearningMaterials
            ),*/
            Triple(
                R.id.headerDescriptionOtherAreas,
                R.id.layoutDescriptionOtherAreasContent,
                R.id.ivToggleDescriptionOtherAreas
            ),
            Triple(
                R.id.headerToiletsWashBasins,
                R.id.layoutToiletsWashBasinsContent,
                R.id.ivToggleToiletsWashBasins
            ),
            Triple(
                R.id.headerSupportInfrastructure,
                R.id.layoutSupportInfrastructureContent,
                R.id.ivToggleSupportInfrastructure
            ),
            Triple(
                R.id.headerCommonEquipment,
                R.id.layoutCommonEquipmentContent,
                R.id.ivToggleCommonEquipment
            ),
            Triple(
                R.id.headerDescriptionOfAcademic_NonAcademicAreas,
                R.id.layoutDescriptionOfAcademic_NonAcademicAreas,
                R.id.ivToggleDescriptionOfAcademicNonAcademicAreas
            ),

            )
        val expansionStates = MutableList(sections.size) { false }

        sections.forEachIndexed { index, (headerId, contentId, iconId) ->
            val header = view.findViewById<LinearLayout>(headerId)
            val content = view.findViewById<LinearLayout>(contentId)
            val icon = view.findViewById<ImageView>(iconId)

            header.setOnClickListener {
                expansionStates[index] = !expansionStates[index]

                if (!expansionStates[index]) {
                    content.visibility = View.GONE

                    val sectionCount = when (index) {
                        0 -> sectionsStatus.infoSection
                        1 -> sectionsStatus.careraSection
                        2 -> sectionsStatus.wiringSection
                        3 -> sectionsStatus.generalDetailsSection
                        5 -> sectionsStatus.signageSection
                        6 -> sectionsStatus.descOtherAreaSection
                        7 -> sectionsStatus.toiletWashBasinSection
                        8 -> sectionsStatus.supportInfraSection
                        9 -> sectionsStatus.commonEquipSection
                        10 -> sectionsStatus.academicSection
                        else -> 0
                    }

                    val iconRes = if (sectionCount > 0) {
                        R.drawable.ic_verified
                    }
                    else {
                        R.drawable.ic_dropdown_arrow
                    }

                    icon.setImageResource(iconRes)
                    return@setOnClickListener
                }

                // Prepare a reusable request object
                val request = TrainingCenterInfo(
                    appVersion = BuildConfig.VERSION_NAME,
                    loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                    tcId = centerId.toInt(),
                    sanctionOrder = sanctionOrder,
                    imeiNo = AppUtil.getAndroidId(requireContext())
                )

                // Map each index to its section status + API call + collector
                val sectionHandlers = mapOf(
                    0 to SectionHandler(sectionsStatus.infoSection, {
                        viewModel.getTrainerCenterInfo(request)
                        collectTCInfoResponse(content, icon)
                    }),
                    1 to SectionHandler(sectionsStatus.careraSection, {
                        viewModel.getIpEnabledCamera(request)
                        collectTCIpEnabele(content, icon)
                    }),
                    2 to SectionHandler(sectionsStatus.wiringSection, {
                        viewModel.getElectricalWiringStandard(request)
                        collectTCElectrical(content, icon)
                    }),
                    3 to SectionHandler(sectionsStatus.generalDetailsSection, {
                        viewModel.getGeneralDetails(request)
                    }),
                    5 to SectionHandler(sectionsStatus.signageSection, {
                        viewModel.getSignagesAndInfoBoard(request)
                        collectTCSignage(content, icon)
                    }),
                    6 to SectionHandler(sectionsStatus.descOtherAreaSection, {
                        viewModel.getDescriptionOtherArea(request)
                        collectTCDescOtherArea(content, icon)
                    }),
                    7 to SectionHandler(sectionsStatus.toiletWashBasinSection, {
                        viewModel.getTcToiletWashBasin(request)
                        collectTCToiletAndWash(content, icon)
                    }),
                    8 to SectionHandler(sectionsStatus.supportInfraSection, {
                        viewModel.getAvailabilitySupportInfra(request)
                        collectTCSupportInfra(content, icon)
                    }),
                    9 to SectionHandler(sectionsStatus.commonEquipSection, {
                        viewModel.getCommonEquipment(request)
                        collectTCCommonEquipment(content, icon)
                    })
                )

                val handler = sectionHandlers[index]

                // If section doesn't exist, just expand content
                if (handler == null) {
                    content.visibility = View.VISIBLE
                    icon.setImageResource(R.drawable.outline_arrow_upward_24)
                    return@setOnClickListener
                }

                // Show confirmation dialog if section already exists
                if (handler.sectionCount > 0) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Alert")
                        .setMessage("Do you want to edit this section?")
                        .setPositiveButton("Yes") { dialog, _ ->
                            dialog.dismiss()
                            handler.action()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                            expansionStates[index] = !expansionStates[index]
                        }
                        .show()
                } else {
                    content.visibility = View.VISIBLE
                    icon.setImageResource(R.drawable.outline_arrow_upward_24)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse(content: LinearLayout, icon: ImageView) {
        viewModel.trainingCentersInfo.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {
                            val distanceBus =  x.distanceFromBusStand
                            val distanceAuto = x.distanceFromAutoStand
                            val distanceRailway = x.distanceFromRailway

                            view?.findViewById<TextInputEditText>(R.id.etDistanceBusStand)?.setText(distanceBus)
                            view?.findViewById<TextInputEditText>(R.id.etDistanceAutoStand)?.setText(distanceAuto)
                            view?.findViewById<TextInputEditText>(R.id.etDistanceRailwayStation)?.setText(distanceRailway)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCIpEnabele(content: LinearLayout, icon: ImageView) {

        viewModel.getIpEnabledCamera.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            spinnerDetailsMap[R.id.spinnerIPEnabled] = x.ipEnable
                            spinnerDetailsMap[R.id.spinnerResolution] = x.resolution
                            spinnerDetailsMap[R.id.spinnerVideoStream] = x.videoStream
                            spinnerDetailsMap[R.id.spinnerRemoteAccessBrowser] = x.remoteAccessBrowser
                            spinnerDetailsMap[R.id.spinnerSimultaneousAccess] = x.simultaneousAccess
                            spinnerDetailsMap[R.id.spinnerSupportedProtocols] = x.supportedProtocol
                            spinnerDetailsMap[R.id.spinnerColorVideoAudio] = x.colorVideoAudit
                            spinnerDetailsMap[R.id.spinnerStorageFacility] = x.storageFacility
                            spinnerDetailsMap[R.id.spinnerMonitorAccessible] = x.centralMonitor
                            spinnerDetailsMap[R.id.spinnerConformance] = x.cctvConformance
                            spinnerDetailsMap[R.id.spinnerStorage] = x.cctvStorage
                            spinnerDetailsMap[R.id.spinnerDVRStaticIP] = x.dvrStaticIp

                            base64MonitorFile = x.centralMonitorImagePath.toString()
                            base64ConformanceFile = x.cctvConformanceImagePath.toString()
                            base64StorageFile = x.cctvStorageImagePath.toString()
                            base64DVRFile = x.dvrStaticIpImagePath.toString()

                            imagesMap[ivMonitorPreview] = base64MonitorFile
                            imagesMap[ivConformancePreview] = base64ConformanceFile
                            imagesMap[ivStoragePreview] = base64StorageFile
                            imagesMap[ivDVRPreview] = base64DVRFile

                            updateSpinner(spinnerDetailsMap)
                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCToiletAndWash(content: LinearLayout, icon: ImageView) {

        viewModel.getTcToiletWashBasin.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            etMaleToilets.setText(x.maleToilet.toString())
                            etFemaleToilets.setText(x.femaleToilet.toString())
                            etMaleUrinals.setText(x.maleUrinal.toString())
                            etMaleWashBasins.setText( x.maleWashBasin.toString())
                            etFemaleWashBasins.setText(x.femaleWashBasin.toString())
                            actvOverheadTanks.setText(x.overheadTanks, false)
                            actvTypeOfFlooring.setText(x.flooringType, false)

                            base64ProofMaleToilets = x.maleToiletImage
                            base64ProofMaleToiletsSignage = x.maleToiletSignageImage
                            base64ProofFemaleToilets = x.femaleToiletImage
                            base64ProofFemaleToiletsSignage = x.femaleToiletSignageImage
                            base64ProofMaleUrinals = x.maleUrinalImage
                            base64ProofMaleWashBasins = x.maleWashBasinImage
                            base64ProofFemaleWashBasins = x.femaleWashBasinImage
                            base64ProofOverheadTanks = x.overheadTankImage
                            base64ProofFlooring = x.flooringTypeImage

                            imagesMap[ivPreviewMaleToiletsProof] = base64ProofMaleToilets
                            imagesMap[ivPreviewMaleToiletsSignageProof] = base64ProofMaleToiletsSignage
                            imagesMap[ivPreviewFemaleToiletsProof] = base64ProofFemaleToilets
                            imagesMap[ivPreviewFemaleToiletsSignageProof] = base64ProofFemaleToiletsSignage
                            imagesMap[ivPreviewMaleUrinalsProof] = base64ProofMaleUrinals
                            imagesMap[ivPreviewMaleWashBasinsProof] = base64ProofMaleWashBasins
                            imagesMap[ivPreviewFemaleWashBasinsProof] = base64ProofFemaleWashBasins
                            imagesMap[ivPreviewOverheadTanksProof] = base64ProofOverheadTanks
                            imagesMap[ivPreviewFlooringProof] = base64ProofFlooring

                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCCommonEquipment(content: LinearLayout, icon: ImageView) {

        viewModel.getCommonEquipment.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            etBiometricDevices.setText(x.biomatricDeviceInstallation)
                            etPrinterScanner.setText(x.printerScanner.toString())
                            etDigitalCamera.setText(x.digitalCamera.toString())

                            spinnerDetailsMap[spinnerPowerBackup] = x.ecPowerBackup
                            spinnerDetailsMap[spinnerCCTV] = x.cctvMoniotrInstall
                            spinnerDetailsMap[spinnerDocumentStorage] = x.storageSecuring
                            spinnerDetailsMap[spinnerGrievanceRegister] = x.grievanceRegister.toString()
                            spinnerDetailsMap[spinnerMinimumEquipment] = x.minimumEquipment.toString()
                            spinnerDetailsMap[spinnerDirectionBoards] = x.directionBoard.toString()

                            base64PowerBackupImage = x.ecPowerBackupImage.toString()
                            base64BiometricDevices = x.biomatricDeviceInstallationImage.toString()
                            base64CCTVImage = x.cctvMoniotrInstallImage.toString()
                            base64DocumentStorageImage = x.storageSecuringImage.toString()
                            base64PrinterScanner = x.printerScannerImage.toString()
                            base64DigitalCamera = x.digitalCameraImage.toString()
                            base64GrievanceRegisterImage = x.grievanceRegisterImage.toString()
                            base64MinimumEquipmentImage = x.minimumEquipmentImage.toString()
                            base64DirectionBoardsImage = x.directionBoardImage.toString()

                            imagesMap[ivPowerBackupPreview] = base64PowerBackupImage
                            imagesMap[ivBiometricDevicesPreview] = base64BiometricDevices
                            imagesMap[ivCCTVPreview] = base64CCTVImage
                            imagesMap[ivDocumentStoragePreview] = base64DocumentStorageImage
                            imagesMap[ivPrinterScannerPreview] = base64PrinterScanner
                            imagesMap[ivDigitalCameraPreview] = base64DigitalCamera
                            imagesMap[ivGrievanceRegisterPreview] = base64GrievanceRegisterImage
                            imagesMap[ivMinimumEquipmentPreview] = base64MinimumEquipmentImage
                            imagesMap[ivDirectionBoardsPreview] = base64DirectionBoardsImage

                            showBase64Image(imagesMap)
                            updateSpinner(spinnerDetailsMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCSignage(content: LinearLayout, icon: ImageView) {

        viewModel.getSignagesAndInfoBoard.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            spinnerDetailsMap[spinnerTcNameBoard] = x.tcNameImage.toString()
                            spinnerDetailsMap[spinnerActivityAchievementBoard] = x.activityAchivementImage.toString()
                            spinnerDetailsMap[spinnerStudentEntitlementBoard] = x.studentEntitlementImage.toString()
                            spinnerDetailsMap[spinnerContactDetailBoard] = x.contactDetailsImage.toString()
                            spinnerDetailsMap[spinnerBasicInfoBoard] = x.basicInfoImage.toString()
                            spinnerDetailsMap[spinnerCodeConductBoard] = x.codeConductImage.toString()
                            spinnerDetailsMap[spinnerStudentAttendanceBoard] = x.studentsAttendanceImage.toString()

                            base64TcNameBoardImage = x.tcName
                            base64ActivityAchievementBoardImage = x.activityAchivement
                            base64StudentEntitlementBoardImage = x.studentEntitlement
                            base64ContactDetailBoardImage = x.contactDetails
                            base64BasicInfoBoardImage = x.basicInfo
                            base64CodeConductBoardImage = x.codeConduct
                            base64StudentAttendanceBoardImage = x.studentsAttendance

                            imagesMap[ivTcNameBoardPreview] = base64TcNameBoardImage
                            imagesMap[ivActivityAchievementBoardPreview] = base64ActivityAchievementBoardImage
                            imagesMap[ivStudentEntitlementBoardPreview] = base64StudentEntitlementBoardImage
                            imagesMap[ivContactDetailBoardPreview] = base64ContactDetailBoardImage
                            imagesMap[ivBasicInfoBoardPreview] = base64BasicInfoBoardImage
                            imagesMap[ivCodeConductBoardPreview] = base64CodeConductBoardImage
                            imagesMap[ivStudentAttendanceBoardPreview] = base64StudentAttendanceBoardImage

                            updateSpinner(spinnerDetailsMap)
                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCGeneral(content: LinearLayout, icon: ImageView) {

        viewModel.getGeneralDetails.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            spinnerDetailsMap[spinnerLeakageCheck] = x.signLeakage
                            spinnerDetailsMap[spinnerProtectionStairs] = x.stairsProtection
                            spinnerDetailsMap[spinnerDDUConformance] = x.ddugkyConfrence
                            spinnerDetailsMap[spinnerCandidateSafety] = x.centerSafty

                            base64LeakageImage = x.signLeakageImage
                            base64StairsImage = x.stairsProtectionImage

                            imagesMap[ivLeakagePreview] = base64LeakageImage
                            imagesMap[ivStairsPreview] = base64StairsImage

                            updateSpinner(spinnerDetailsMap)
                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCElectrical(content: LinearLayout, icon: ImageView) {

        viewModel.getElectricalWiringStandard.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            spinnerDetailsMap[R.id.spinnerSecure] = x.wireSecurity
                            spinnerDetailsMap[R.id.spinnerSwitchBoards] = x.switchBoard

                            base64SwitchBoardImage = x.switchBoardImage.toString()
                            base64WireSecurityImage = x.wireSecurityImage.toString()

                            imagesMap[ivSwitchBoardPreview] = base64SwitchBoardImage
                            imagesMap[ivWireSecurityPreview] = base64WireSecurityImage

                            updateSpinner(spinnerDetailsMap)
                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCSupportInfra(content: LinearLayout, icon: ImageView) {

        viewModel.getAvailabilitySupportInfra.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val spinnerDetailsMap = mutableMapOf<Any, String?>()
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            editFireFightingEquipment.setText(x.fireFighterEquip)

                            spinnerDetailsMap[spinnerSafeDrinkingWater] = x.drinkingWater
                            spinnerDetailsMap[spinnerFirstAidKit] = x.firstAidKit

                            base64SafeDrinkingWater = x.drinkingWaterImage.toString()
                            base64FireFightingEquipment = x.fireFighterEquipImage.toString()
                            base64FirstAidKit = x.firstAidKitImage.toString()

                            imagesMap[ivSafeDrinkingWaterPreview] = base64SafeDrinkingWater
                            imagesMap[ivFireFightingEquipmentPreview] = base64FireFightingEquipment
                            imagesMap[ivFirstAidKitPreview] = base64FirstAidKit

                            updateSpinner(spinnerDetailsMap)
                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectTCDescOtherArea(content: LinearLayout, icon: ImageView) {

        viewModel.getDescriptionOtherArea.observe(viewLifecycleOwner) { result ->
            content.visibility = View.VISIBLE
            icon.setImageResource(R.drawable.outline_arrow_upward_24)

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {
                            val imagesMap = mutableMapOf<ImageView, String?>()

                            etCorridorNo.setText(x.corridorNo)
                            etDescLength.setText(x.length)
                            etDescWidth.setText(x.width)
                            etArea.setText(x.areas)
                            etLights.setText( x.numberOfLights)
                            etFans.setText( x.numberOfFans)
                            etCirculationArea.setText(x.circulationArea)
                            etOpenSpace.setText(x.openSpace)
                            etExclusiveParkingSpace.setText(x.parkingSpace)

                            base64ProofUploadImage = x.descProofImagePath.toString()
                            base64CirculationProofImage = x.circulationAreaImagePath.toString()
                            base64penSpaceProofImage = x.openSpaceImagePath.toString()
                            base64ParkingSpaceProofImage = x.parkingSpaceImagePath.toString()

                            imagesMap[ivProofPreview] = base64ProofUploadImage
                            imagesMap[ivCirculationProofPreview] = base64CirculationProofImage
                            imagesMap[ivOpenSpaceProofPreview] = base64penSpaceProofImage
                            imagesMap[ivParkingProofPreview] = base64ParkingSpaceProofImage

                            showBase64Image(imagesMap)
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun updateSpinner(spinnerDetailsMap: Map<Any, String?>) {
        spinnerDetailsMap.forEach { (spinner, value) ->
            when (spinner) {
                is Int ->  view?.findViewById<Spinner>(spinner)?.setSelection(updateSelection(value))
                is Spinner-> spinner.setSelection(updateSelection(value))
            }
        }
    }

    private fun updateSelection(value: String?) : Int {
        return  when(value) {
            "Yes" -> 1
            "No" -> 2
            else -> 0
        }
    }

    private fun showBase64Image(imagesMap: Map<ImageView, String?>) {
        imagesMap.forEach { (imageView, base64ImageString) ->
            // Decode Base64 → Bitmap
            val bitmap: Bitmap? = if (!base64ImageString.isNullOrBlank()) {
                try {
                    val cleanBase64 = base64ImageString
                        .replace("data:image/png;base64,", "")
                        .replace("data:image/jpg;base64,", "")
                        .replace("data:image/jpeg;base64,", "")
                        .replace("\\s".toRegex(), "")

                    val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }

            // If bitmap is null → show default image
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            } else {
                imageView.setImageResource(R.drawable.no_image) // your fallback drawable
            }
            imageView.visibility = View.VISIBLE
        }
    }

    private fun collectFinalSubmitData() {

        viewModel.getFinalSubmitData.observe(viewLifecycleOwner) { result ->

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        Toast.makeText(
                            requireContext(),
                            "Details sent successfully to Q-Team",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        it.responseDesc,
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun collectSectionStatus() {

        viewModel.getSectionsStatusData.observe(viewLifecycleOwner) { result ->

            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        sectionsStatus = it.wrappedList?.get(0)!!

                        if (sectionsStatus.infoSection > 0) {
                            binding.ivToggleTCBasicInfo.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.careraSection > 0) {
                            binding.ivToggleCCTVCompliance.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.toiletWashBasinSection > 0) {
                            binding.ivToggleToiletsWashBasins.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.commonEquipSection > 0) {
                            binding.ivToggleCommonEquipment.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.signageSection > 0) {
                            binding.ivToggleSignagesInfoBoards.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.generalDetailsSection > 0) {
                            binding.ivToggleGeneralDetails.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.wiringSection > 0) {
                            binding.ivToggleElectricalWiring.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.supportInfraSection > 0) {
                            binding.ivToggleSupportInfrastructure.setImageResource(R.drawable.ic_verified)
                        }
                        if (sectionsStatus.descOtherAreaSection > 0) {
                            binding.ivToggleDescriptionOtherAreas.setImageResource(R.drawable.ic_verified)
                        }

                        if (sectionsStatus.academicSection > 0) {
                            binding.ivToggleDescriptionOtherAreas.setImageResource(R.drawable.ic_verified)
                        }
                    }

                    202 -> Toast.makeText(
                        requireContext(),
                        it.responseDesc,
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
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
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


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        // Uses high accuracy priority for precise location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {


                    etLatitude.setText(location.latitude.toString())
                    etLongitude.setText(location.longitude.toString())
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to get location: ${it.message}", Toast.LENGTH_SHORT).show()
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

    private fun validateCCTVForm(view: View): Boolean {
        val spinnerOk = listOf(
            R.id.spinnerMonitorAccessible,
            R.id.spinnerConformance,
            R.id.spinnerStorage,
            R.id.spinnerDVRStaticIP,
            R.id.spinnerIPEnabled,
            R.id.spinnerResolution,
            R.id.spinnerVideoStream,
            R.id.spinnerRemoteAccessBrowser,
            R.id.spinnerSimultaneousAccess,
            R.id.spinnerSupportedProtocols,
            R.id.spinnerColorVideoAudio,
            R.id.spinnerStorageFacility
        ).all {
            view.findViewById<Spinner>(it).selectedItem.toString() != "--Select--"
        }

        val photosOk = base64MonitorFile != null && base64ConformanceFile != null &&
                base64StorageFile != null && base64DVRFile != null
        return spinnerOk && photosOk
    }

    private fun validateElectricalForm(view: View): Boolean {
        val switchSelected =
            view.findViewById<Spinner>(R.id.spinnerSwitchBoards).selectedItem != "--Select--"
        val wireSelected =
            view.findViewById<Spinner>(R.id.spinnerSecure).selectedItem != "--Select--"
        val photosOk = base64SwitchBoardImage != null && base64WireSecurityImage != null
        return switchSelected && wireSelected && photosOk
    }

    private fun validateGeneralDetailsForm(): Boolean {
        return spinnerLeakageCheck.selectedItem.toString() != "--Select--"
                && spinnerProtectionStairs.selectedItem.toString() != "--Select--"
                && spinnerDDUConformance.selectedItem.toString() != "--Select--"
                && spinnerCandidateSafety.selectedItem.toString() != "--Select--"
                && base64LeakageImage != null
                && base64StairsImage != null
    }

    private fun validateSupportInfrastructure(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
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

        // Helper function to check EditText input
        fun checkEditText(editText: EditText, fieldName: String): Boolean {
            return if (editText.text.toString().trim().isEmpty()) {
                editText.error = "Please enter $fieldName"
                editText.requestFocus()
                false
            } else {
                true
            }
        }
        // Validate First Aid Kit spinner
        if (!checkSpinner(spinnerFirstAidKit, "First Aid Kit")) isValid = false

        // Validate Safe Drinking Water spinner
        if (!checkSpinner(spinnerSafeDrinkingWater, "Safe Drinking Water")) isValid = false

        // Validate Fire Fighting Equipment EditText
        if (!checkEditText(editFireFightingEquipment, "Fire Fighting Equipment")) isValid = false

        return isValid
    }

    private fun validateTcBasicInfoFields(): Boolean {
        var isValid = true

        val distanceBus = requireView().findViewById<TextInputEditText>(R.id.etDistanceBusStand)
        val distanceAuto = requireView().findViewById<TextInputEditText>(R.id.etDistanceAutoStand)
        val distanceRailway =
            requireView().findViewById<TextInputEditText>(R.id.etDistanceRailwayStation)
        val latitude = requireView().findViewById<TextInputEditText>(R.id.etLatitude)
        val longitude = requireView().findViewById<TextInputEditText>(R.id.etLongitude)

        // Helper to validate one field
        fun validateField(field: TextInputEditText, fieldName: String): Boolean {
            return if (field.text.isNullOrBlank()) {
                field.error = "$fieldName cannot be empty"
                false
            } else {
                field.error = null
                true
            }
        }
        isValid = validateField(distanceBus, "Distance from Bus Stand") && isValid
        isValid = validateField(distanceAuto, "Distance from Auto Stand") && isValid
        isValid = validateField(distanceRailway, "Distance from Railway Station") && isValid
        isValid = validateField(latitude, "Latitude") && isValid
        isValid = validateField(longitude, "Longitude") && isValid


        return isValid
    }

    private fun validateCommonEquipment(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
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

        // Helper function to check TextInputEditText input
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
        if (!checkSpinner(spinnerPowerBackup, "Electrical Power Backup")) isValid = false
        if (!checkSpinner(spinnerCCTV, "Installation of CCTV Monitor")) isValid = false
        if (!checkSpinner(spinnerDocumentStorage, "Storage Place for Securing Documents")) isValid =
            false
        if (!checkSpinner(spinnerGrievanceRegister, "Grievance Register")) isValid = false
        if (!checkSpinner(spinnerMinimumEquipment, "Minimum Equipment as per SF 5.1P")) isValid =
            false
        if (!checkSpinner(spinnerDirectionBoards, "Direction Boards")) isValid = false

        // Validate required TextInputEditTexts
        if (!checkTextInput(etBiometricDevices, "Biometric Devices details")) isValid = false
        if (!checkTextInput(etPrinterScanner, "Printer Cum Scanner number")) isValid = false
        if (!checkTextInput(etDigitalCamera, "Digital Camera number")) isValid = false

        return isValid
    }

    private fun validateSignagesInfoBoards(): Boolean {
        var isValid = true

        // Helper function to check spinner selection
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

        if (!checkSpinner(spinnerTcNameBoard, "Training Centre Name Board")) isValid = false
        if (!checkSpinner(spinnerActivityAchievementBoard, "Activity Summary Board")) isValid =
            false
        if (!checkSpinner(spinnerStudentEntitlementBoard, "Student Entitlement Board")) isValid =
            false
        if (!checkSpinner(spinnerContactDetailBoard, "Contact Detail Board")) isValid = false
        if (!checkSpinner(spinnerBasicInfoBoard, "Basic Info Board")) isValid = false
        if (!checkSpinner(spinnerCodeConductBoard, "Code of Conduct Board")) isValid = false
        if (!checkSpinner(spinnerStudentAttendanceBoard, "Attendance Summary Board")) isValid =
            false

        return isValid
    }

    private fun validateDescriptionForm(): Boolean {
        var isValid = true
        // Helper to check if a TextInputEditText is empty
        fun checkEmpty(editText: TextInputEditText, fieldName: String): Boolean {
            val text = editText.text?.toString()?.trim()
            return if (text.isNullOrEmpty()) {
                editText.error = "$fieldName is required"
                false
            } else {
                editText.error = null
                true
            }
        }
        // Validate all required fields
        isValid = checkEmpty(etCorridorNo, "Corridor No") && isValid
        isValid = checkEmpty(etDescLength, "Length") && isValid
        isValid = checkEmpty(etDescWidth, "Width") && isValid
        isValid = checkEmpty(etArea, "Area") && isValid
        isValid = checkEmpty(etLights, "Lights") && isValid
        isValid = checkEmpty(etFans, "Fans") && isValid
        isValid = checkEmpty(etCirculationArea, "Circulation Area") && isValid
        isValid = checkEmpty(etOpenSpace, "Open Space") && isValid
        isValid = checkEmpty(etExclusiveParkingSpace, "Exclusive Parking Space") && isValid
        return isValid
    }

    private fun validateToiletsAndWashBasins(): Boolean {
        var isValid = true

        // Map of EditText fields and their error messages
        val editTextFields = mapOf(
            etMaleToilets to "Male Toilets is required",
            etFemaleToilets to "Female Toilets is required",
            etMaleUrinals to "Male Urinals is required",
            etMaleWashBasins to "Male Wash Basins is required",
            etFemaleWashBasins to "Female Wash Basins is required"
        )

        // Validate all EditTexts
        for ((field, errorMsg) in editTextFields) {
            if (field.text.isNullOrBlank()) {
                field.error = errorMsg
                isValid = false
            } else {
                field.error = null
            }
        }

        // Map of AutoCompleteTextView fields and their error messages
        val autoCompleteFields = mapOf(
            actvOverheadTanks to "Please select Overhead Tanks option",
            actvTypeOfFlooring to "Please select Type of Flooring"
        )

        // Validate AutoCompleteTextViews
        for ((field, errorMsg) in autoCompleteFields) {
            val text = field.text.toString()
            if (text.isBlank() || text == "--Select--") {
                field.error = errorMsg
                isValid = false
            } else {
                field.error = null
            }
        }

        // Map of base64 image strings and error messages
        val proofImages = mapOf(
            base64ProofMaleToilets to "Please upload proof for Male Toilets",
            base64ProofMaleToiletsSignage to "Please upload proof for Male Toilets Signage",
            base64ProofFemaleToilets to "Please upload proof for Female Toilets",
            base64ProofFemaleToiletsSignage to "Please upload proof for Female Toilets Signage",
            base64ProofMaleUrinals to "Please upload proof for Male Urinals",
            base64ProofMaleWashBasins to "Please upload proof for Male Wash Basins",
            base64ProofFemaleWashBasins to "Please upload proof for Female Wash Basins",
            base64ProofOverheadTanks to "Please upload proof for Overhead Tanks"
        )

        // Validate proof images
        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    private fun submitCCTVData(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
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
            cctvStorageFile = base64StorageFile ?: "",
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
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.submitCCTVDataToServer(request, token)
    }
    private fun submitElectricalData(view: View) {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = ElectricalWiringRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            switchBoard = view.findViewById<Spinner>(R.id.spinnerSwitchBoards).selectedItem.toString(),
            switchBoardImage = base64SwitchBoardImage ?: "",
            wireSecurity = view.findViewById<Spinner>(R.id.spinnerSecure).selectedItem.toString(),
            wireSecurityImage = base64WireSecurityImage ?: "",
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.submitElectricalData(request, token)
    }

    /*
        private fun submitGeneralDetails() {
            val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
                .getString("ACCESS_TOKEN", "") ?: ""

            val request = InsertTcGeneralDetailsRequest(
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                imeiNo = AppUtil.getAndroidId(requireContext()),
                appVersion = BuildConfig.VERSION_NAME,
                signLeakages = dropdownLeakageCheck.selectedItem.toString(),
                signLeakagesImage = base64LeakageImage ?: "",
                stairsProtection = dropdownProtectionStairs.selectedItem.toString(),
                stairsProtectionImage = base64StairsImage ?: "",
                dduConformance = dropdownDDUConformance.selectedItem.toString(),
                centerSafety = dropdownCandidateSafety.selectedItem.toString(),
                tcId = centerId,
                sanctionOrder = AppUtil.getSavedSanctionOrder(requireContext())
            )
            viewModel.submitGeneralDetails(request, token)

        }
    */
    private fun submitGeneralDetails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val request = InsertTcGeneralDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            signLeakages = spinnerLeakageCheck.selectedItem.toString(),
            signLeakagesImage = base64LeakageImage ?: "",
            stairsProtection = spinnerProtectionStairs.selectedItem.toString(),
            stairsProtectionImage = base64StairsImage ?: "",
            dduConformance = spinnerDDUConformance.selectedItem.toString(),
            centerSafety = spinnerCandidateSafety.selectedItem.toString(),
            tcId = centerId,
            sanctionOrder = sanctionOrder
        )
        viewModel.submitGeneralDetails(request, token)
    }

    private fun submitTCInfoDeatails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val distanceBus =
            view?.findViewById<TextInputEditText>(R.id.etDistanceBusStand)?.text.toString()
        val distanceAuto =
            view?.findViewById<TextInputEditText>(R.id.etDistanceAutoStand)?.text.toString()
        val distanceRailway =
            view?.findViewById<TextInputEditText>(R.id.etDistanceRailwayStation)?.text.toString()
        val latitude = view?.findViewById<TextInputEditText>(R.id.etLatitude)?.text.toString()
        val longitude = view?.findViewById<TextInputEditText>(R.id.etLongitude)?.text.toString()

        // For geoAddress, assuming you have an EditText or a way to get this value
        val geoAddress = ""  // Replace with actual input if available

        val request = TcBasicInfoRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            distanceBus = distanceBus,
            distanceAuto = distanceAuto,
            distanceRailway = distanceRailway,
            latitude = latitude,
            longitude = longitude,
            geoAddress = geoAddress

        )
        viewModel.submitTcBasicDataToServer(request, token)
    }
    //Rohit Signages Info Details
    private fun submitSignagesInfoBoards() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val trainingCentreNameBoard = spinnerTcNameBoard.selectedItem.toString()
        val activitySummaryBoard = spinnerActivityAchievementBoard.selectedItem.toString()
        val entitlementBoard = spinnerStudentEntitlementBoard.selectedItem.toString()
        val importantContacts = spinnerContactDetailBoard.selectedItem.toString()
        val basicInfoBoard = spinnerBasicInfoBoard.selectedItem.toString()
        val codeOfConductBoard = spinnerCodeConductBoard.selectedItem.toString()
        val attendanceSummaryBoard = spinnerStudentAttendanceBoard.selectedItem.toString()

        val request = TcSignagesInfoBoardRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder,

            tcNameBoard = trainingCentreNameBoard,
            tcNameBoardImage = base64TcNameBoardImage ?: "",

            activityAchievmentBoard = activitySummaryBoard,
            activityAchievmentBoardImage = base64ActivityAchievementBoardImage ?: "",

            studentEntitlementResponsibilityBoard = entitlementBoard,
            studentEntitlementResponsibilityBoardImage = base64StudentEntitlementBoardImage ?: "",

            contactDetailImpPeople = importantContacts,
            contactDetailImpPeopleImage = base64ContactDetailBoardImage ?: "",

            basicInfoBoard = basicInfoBoard,
            basicInfoBoardImage = base64BasicInfoBoardImage ?: "",

            codeConductBoard = codeOfConductBoard,
            codeConductBoardImage = base64CodeConductBoardImage ?: "",

            studentAttendanceEntitlementBoard = attendanceSummaryBoard,
            studentAttendanceEntitlementBoardImage = base64StudentAttendanceBoardImage ?: ""
        )
        viewModel.submitTcInfoSignageDataToServer(request, token)
    }
    private fun submitInfraDetails() {
        val token = requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val firstAidKit = spinnerFirstAidKit.selectedItem.toString()
        val safeDrinkingWater = spinnerSafeDrinkingWater.selectedItem.toString()
        val fireFightingEquipment =
            view?.findViewById<TextInputEditText>(R.id.editFireFightingEquipment)?.text.toString()

        val request = TcAvailabilitySupportInfraRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId =centerId,
            sanctionOrder = sanctionOrder,
            drinkingWater = safeDrinkingWater,
            drinkingWaterImage = base64SafeDrinkingWater ?: "",
            fireFightingEquipment = fireFightingEquipment,
            fireFightingEquipmentImage = base64FireFightingEquipment ?: "",
            firstAidKit = firstAidKit,
            firstAidKitImage = base64FirstAidKit ?: "",
        )
        viewModel.submitTcSupportInfraDataToserver(request, token)
    }
    private fun submitCommonEquipment() {
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val electricalPowerBackup = spinnerPowerBackup.selectedItem.toString()
        val biometricDevicesDetails = etBiometricDevices.text.toString()
        val cctvInstallation = spinnerCCTV.selectedItem.toString()
        val documentStorage = spinnerDocumentStorage.selectedItem.toString()
        val printerScannerCount = etPrinterScanner.text.toString()
        val digitalCameraCount = etDigitalCamera.text.toString()
        val grievanceRegister = spinnerGrievanceRegister.selectedItem.toString()
        val minimumEquipment = spinnerMinimumEquipment.selectedItem.toString()
        val directionBoards = spinnerDirectionBoards.selectedItem.toString()

        val request = TcCommonEquipmentRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder,

            ecPowerBackup = electricalPowerBackup,
            ecPowerBackupImage = base64PowerBackupImage ?: "",

            biometricDeviceInstall = biometricDevicesDetails,
            biometricDeviceInstallImage = base64BiometricDevices ?: "",

            cctvMonitorInstall = cctvInstallation,
            cctvMonitorInstallImage = base64CCTVImage ?: "",

            documentStorageSecuring = documentStorage,
            documentStorageSecuringImage = base64DocumentStorageImage ?: "",

            printerScanner = printerScannerCount,
            printerScannerlImage = base64PrinterScanner ?: "",

            digitalCamera = digitalCameraCount,
            digitalCameraImage = base64DigitalCamera ?: "",

            grievanceRegister = grievanceRegister,
            grievanceRegisterImage = base64GrievanceRegisterImage ?: "",

            minimumEquipment = minimumEquipment,
            minimumEquipmentImage = base64MinimumEquipmentImage ?: "",

            directionBoards = directionBoards,
            directionBoardsImage = base64DirectionBoardsImage ?: ""
        )

        viewModel.submitTcCommonEquipment(request, token)
    }
    private fun submitDescriptionOtherAreas() {
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val corridorNo = etCorridorNo.text.toString()
        val descLength = etDescLength.text.toString()
        val descWidth = etDescWidth.text.toString()
        val area = etArea.text.toString()
        val lights = etLights.text.toString()
        val fans = etFans.text.toString()
        val circulationArea = etCirculationArea.text.toString()
        val openSpace = etOpenSpace.text.toString()
        val exclusiveParkingSpace = etExclusiveParkingSpace.text.toString()

        val request = TcDescriptionOtherAreasRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder,
            corridorNo = corridorNo,
            length = descLength,
            width = descWidth,
            areas = area,
            lights = lights,
            fans = fans,
            circulationArea = circulationArea,
            circulationAreaImage = base64CirculationProofImage ?: "",
            openSpace = openSpace,
            openSpaceImage = base64penSpaceProofImage ?: "",
            parkingSpace = exclusiveParkingSpace,
            parkingSpaceImage = base64ParkingSpaceProofImage ?: "",
            proofImage = base64ProofUploadImage?:"",
        )
        viewModel.submitTcDescriptionArea(request, token)
    }

    private fun submitWashBasins() {
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        val maleToiletsCount = etMaleToilets.text.toString().toIntOrNull() ?: 0
        val femaleToiletsCount = etFemaleToilets.text.toString().toIntOrNull() ?: 0
        val maleUrinalsCount = etMaleUrinals.text.toString().toIntOrNull() ?: 0
        val maleWashBasinsCount = etMaleWashBasins.text.toString().toIntOrNull() ?: 0
        val femaleWashBasinsCount = etFemaleWashBasins.text.toString().toIntOrNull() ?: 0
        val overheadTanksStatus = actvOverheadTanks.text.toString()
        val typeOfFlooring = actvTypeOfFlooring.text.toString()

        val request = ToiletDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = sanctionOrder,

            maleToilet = maleToiletsCount,
            maleToiletProof = base64ProofMaleToilets ?: "",

            maleToiletSignageProof = base64ProofMaleToiletsSignage ?: "",

            femaleToilet = femaleToiletsCount,
            femaleToiletProof = base64ProofFemaleToilets ?: "",

            femaleToiletSignageProof = base64ProofFemaleToiletsSignage ?: "",

            maleUrinals = maleUrinalsCount,
            maleUrinalsImage = base64ProofMaleUrinals ?: "",

            maleWashBasin = maleWashBasinsCount,
            maleWashBasinImage = base64ProofMaleWashBasins ?: "",

            femaleWashBasin = femaleWashBasinsCount,
            femaleWashBasinImage = base64ProofFemaleWashBasins ?: "",

            overheadTanks = overheadTanksStatus,
            overheadTanksImage = base64ProofOverheadTanks ?: "",

            flooringType = typeOfFlooring,
            flooringTypeImage = base64ProofFlooring ?: ""
        )

        viewModel.SubmitWashBasinDataToServer(request, token)
    }




//     IT LAB(validateItLAB)    Ajit Ranjan


    private fun validateItLAB(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditTexts ---
        val editTextFields = mapOf(
            etLength to "Type of Length is required",
            etWidth to "Type of Width is required",
            etITLTypeofRoofItLab to "Type of Roof is required",
            etITLHeightOfCelling to "Height of Ceiling is required",
            etITLVentilationAreaInSqFt to "Ventilation Area (in Sq Ft) is required",
            etITLSoundLevelAsPerSpecifications to "Sound Level as per Specifications is required",
            etITLSoundLevelInDb to "Sound Level in dB is required",
            etITLLanEnabledComputersInNo to "LAN Enabled Computers (in No) is required",
            etITLTablets to "Tablets count is required",
            etITLStoolsChairs to "Stools/Chairs count is required",
            etITLLightsInNo to "Lights (in No) is required",
            etITLFansInNo to "Fans (in No) is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner validation helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate Spinners ---
        val spinnerList = listOf(
            spinnerITLepbftr to getString(R.string.electrical_power_backup_for_the_room),
            spinnerITLFalseCellingProvide to getString(R.string.false_ceiling_provided),
            spinnerITLwhether_all_the_academic to getString(R.string.whether_all_the_academic_centres_have_been_sound_proofed_with_air_conditioning),
            spinnerITLAcademicRoomInformationBoard to getString(R.string.academic_room_information_board),
            spinnerITLInternalSignage to getString(R.string.internal_signage),
            spinnerITLCctcCamerasWithAudioFacility to getString(R.string.cctv_cameras_with_audio_facility),
            spinnerITLabInternetConnections to getString(R.string.internet_connections),
            spinnerITLDoAllComputersHaveTypingTutor to getString(R.string.do_all_computers_have_typing_tutor),
            spinnerITLTrainerChair to getString(R.string.trainer_chair),
            spinnerITLTrainerTable to getString(R.string.trainer_table),
            spinnerITLtLabPhotograph to getString(R.string.it_lab_photograph),
            spinnerITLDoes_the_room_has to getString(R.string.does_the_room_has_air_conditioning)
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Image Proof Validation ---
        val spinnerToProofMap = mapOf(
            spinnerITLepbftr to Pair(base64ProofITLElectricaPowerBackUpForThRoom, "Electrical Power Backup proof"),
            spinnerITLFalseCellingProvide to Pair(base64ProofITLFalseCellingProvide, "False Ceiling proof"),
            spinnerITLwhether_all_the_academic to Pair(base64ProofITLwhether_all_the_academic, "Sound Proofing proof"),
            spinnerITLAcademicRoomInformationBoard to Pair(base64ProofITLAcademicRoomInformationBoard, "Academic Room Info Board proof"),
            spinnerITLInternalSignage to Pair(base64ProofITLInternalSignage, "Internal Signage proof"),
            spinnerITLCctcCamerasWithAudioFacility to Pair(base64ProofITLCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerITLabInternetConnections to Pair(base64ProofITLInternetConnections, "Internet Connection proof"),
            spinnerITLDoAllComputersHaveTypingTutor to Pair(base64ProofITLDoAllComputersHaveTypingTutor, "Typing Tutor proof"),
            spinnerITLTrainerChair to Pair(base64ProofITLTrainerChair, "Trainer Chair proof"),
            spinnerITLTrainerTable to Pair(base64ProofITLTrainerTable, "Trainer Table proof"),
            spinnerITLtLabPhotograph to Pair(base64ProofITLItLabPhotograph, "IT Lab Photograph proof"),
            spinnerITLDoes_the_room_has to Pair(base64ProofITLDoes_the_room_has, "Room AC proof")
        )

        // --- 5️⃣ Conditional Validation: "Yes" ⇒ image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 6️⃣ Optional Extra Validation: if no spinner selected but base64 empty ---
        val proofImages = mapOf(
            base64ProofPreviewITLTypeofRoofItLab to "Please upload proof for ITL Type of Roof",
            base64ProofITLHeightOfCelling to "Please upload proof for ITL Height of Ceiling",
            base64ProofITLVentilationAreaInSqFt to "Please upload proof for ITL Ventilation Area",
            base64ProofITLSoundLevelAsPerSpecifications to "Please upload proof for ITL Sound Level as per Specifications",
            base64ProofITLSoundLevelInDb to "Please upload proof for ITL Sound Level in dB",
            base64ProofITLLanEnabledComputersInNo to "Please upload proof for ITL LAN Enabled Computers",
            base64ProofITLTablets to "Please upload proof for ITL Tablets",
            base64ProofITLStoolsChairs to "Please upload proof for ITL Stools/Chairs",
            base64ProofITLLightsInNo to "Please upload proof for ITL Lights",
            base64ProofITLFansInNo to "Please upload proof for ITL Fans"
        )

        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
//    Office Cum(Counselling room)    Ajit Ranjan

    private fun validateOfficeCumCounsellingRoom(): Boolean {

        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etOfficeRoomPhotograph to "Office Room Photograph is required",
            etOfficeCumTypeofRoofItLab to "Type of Roof is required",
            etOfficeCumHeightOfCelling to "Height of Ceiling is required",
            etOfficeCumSplaceforSecuringDoc to "Space for Securing Documents is required",
            etOfficeCumAnOfficeTableNo to "Office Table Number is required",
            etOfficeCumChairs to "Number of Chairs is required",
            etOfficeCumPrinterCumScannerInNo to "Printer/Scanner count is required",
            etOfficeCumDigitalCameraInNo to "Digital Camera count is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate All Spinners ---
        val spinnerList = listOf(
            spinnerOfficeCumFalseCellingProvide to "False Ceiling Provided",
            spinnerOfficeCumLepbftr to "Electrical Power Backup for the Room",
            spinnerOfficeCumTableOfofficeCumpter to "Office Computer Table Available"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Spinner to Base64 Proof Mapping ---
        val spinnerToProofMap = mapOf(
            spinnerOfficeCumFalseCellingProvide to Pair(base64ProofOfficeCumFalseCellingProvide, "False Ceiling proof"),
            spinnerOfficeCumLepbftr to Pair(base64ProofOfficeCumElectricialPowerBackup, "Electrical Power Backup proof"),
            spinnerOfficeCumTableOfofficeCumpter to Pair(base64ProofOfficeCumTableOfofficeCumpter, "Office Computer Table proof")
        )

        // --- 5️⃣ Conditional Validation: if Spinner = "Yes" then Base64 image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 6️⃣ Always Required Image Proofs ---
        val proofImages = mapOf(
            base64ProofPreviewOfficeRoomPhotograph to "Please upload proof for Office Room Photograph",
            base64ProofOfficeCumTypeofRoofItLab to "Please upload proof for Type of Roof",
            base64ProofOfficeCumHeightOfCelling to "Please upload proof for Height of Ceiling",
            base64ProofOfficeCumSplaceforSecuringDoc to "Please upload proof for Space for Securing Documents",
            base64ProofOfficeCumAnOfficeTableNo to "Please upload proof for Office Table Number",
            base64ProofOfficeCumChairs to "Please upload proof for Chairs",
            base64ProofOfficeCumPrinterCumScannerInNo to "Please upload proof for Printer/Scanner",
            base64ProofOfficeCumDigitalCameraInNo to "Please upload proof for Digital Camera"
        )

        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    //    ReceptionArea
    private fun validateReceptionArea(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required")

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate All Spinners ---
        val spinnerList = listOf(
            spinnerReceptionAreaEPBR to getString(R.string.electrical_power_backup_for_the_room)
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Spinner to Base64 Proof Mapping ---
        val spinnerToProofMap = mapOf(
            spinnerReceptionAreaEPBR to Pair(base64ProofPreviewReceptionAreaPhotogragh,getString(R.string.electrical_power_backup_for_the_room)),
        )
        // --- 5️⃣ Conditional Validation: if Spinner = "Yes" then Base64 image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 6️⃣ Always Required Image Proofs ---
        val proofImages = mapOf(
            base64ProofPreviewReceptionAreaPhotogragh to "Please upload proof for  Photogragh",
        )

        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    private fun validateCounsellingRoom(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required")

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate All Spinners ---
        val spinnerList = listOf(
            spinnerCounsellingRoomAreaPhotograph to getString(R.string.counselling_room)
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Spinner to Base64 Proof Mapping ---
        val spinnerToProofMap = mapOf(
            spinnerCounsellingRoomAreaPhotograph to Pair(base64ProofPreviewCounsellingRoomPhotogragh,getString(R.string.counselling_area_photograph)),
        )
        // --- 5️⃣ Conditional Validation: if Spinner = "Yes" then Base64 image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 6️⃣ Always Required Image Proofs ---
        val proofImages = mapOf(
            base64ProofPreviewCounsellingRoomPhotogragh to "Please upload proof for Counselling Room Photogragh",
        )

        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    private fun validateOfficeRoom(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etOROfficeRoomPhotograph to "Office Room Photograph description is required",
            etORTypeofRoofItLab to "Type of Roof is required",
            etORHeightOfCelling to "Height of Ceiling is required",
            etORSplaceforSecuringDoc to "Space for securing documents is required",
            etORAnOfficeTableNo to "Number of Office Tables is required",
            etORChairs to "Number of Chairs is required",
            etORPrinterCumScannerInNo to "Number of Printer/Scanners is required",
            etORDigitalCameraInNo to "Number of Digital Cameras is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate All Spinners ---
        val spinnerList = listOf(
            spinnerORFalseCellingProvide to "False Ceiling Provided",
            spinnerORTableOfofficeCumpter to "Office Computer Table Available",
            spinnerORPOEPBFTR to "Electrical Power Backup for the Room"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Spinner to Base64 Proof Mapping ---
        val spinnerToProofMap = mapOf(
            spinnerORFalseCellingProvide to Pair(base64ProofORFalseCellingProvide, "False Ceiling proof"),
            spinnerORTableOfofficeCumpter to Pair(base64ProofORTableOfofficeCumpter, "Office Computer Table proof"),
            spinnerORPOEPBFTR to Pair(base64ProofORElectricialPowerBackup, "Electrical Power Backup proof")
        )

        // --- 5️⃣ Conditional Validation: if Spinner = "Yes" then Base64 image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Please upload $proofName (You selected Yes)",
                    Toast.LENGTH_SHORT
                ).show()
                isValid = false
            }
        }

        // --- 6️⃣ Always Required Image Proofs ---
        val proofImages = mapOf(
            base64ProofPreviewOROfficeRoomORPhotograph to "Please upload proof for Office Room Photograph",
            base64ProofORTypeofRoofItLab to "Please upload proof for Type of Roof",
            base64ProofORHeightOfCelling to "Please upload proof for Height of Ceiling",
            base64ProofORSplaceforSecuringDoc to "Please upload proof for Space for Securing Documents",
            base64ProofORAnOfficeTableNo to "Please upload proof for Office Table Number",
            base64ProofORChairs to "Please upload proof for Chairs",
            base64ProofORPrinterCumScannerInNo to "Please upload proof for Printer/Scanner",
            base64ProofORDigitalCameraInNo to "Please upload proof for Digital Camera"
        )

        for ((base64, errorMsg) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    private fun validateITComeDomainLab(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etITCDLTypeofRoofItLab to "Type of Roof is required",
            etITCDLFalseCellingProvide to "False Ceiling information is required",
            etITCDLHeightOfCelling to "Height of Ceiling is required",
            etITCDLVentilationAreaInSqFt to "Ventilation Area (in Sq Ft) is required",
            etITCDLSoundLevelAsPerSpecifications to "Sound Level (As per Specifications) is required",
            etITCDLabSoundLevelInDb to "Sound Level (in dB) is required",
            etITCDLLanEnabledComputersInNo to "Number of LAN-enabled Computers is required",
            etITCDLTablets to "Number of Tablets is required",
            etITCDLStoolsChairs to "Number of Stools & Chairs is required",
            etITCDLLightsInNo to "Number of Lights is required",
            etITCDLFansInNo to "Number of Fans is required",
            etITCDLItLabPhotograph to "IT Come Domain Lab Photograph is required"
//            etITCDLListofDomain to "List of Domains is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate All Spinners ---
        val spinnerList = listOf(
            spinnerITCDLwhether_all_the_academic to "Whether all the Academic Rooms are Available",
            spinnerITCDLAcademicRoomInformationBoard to "Academic Room Information Board",
            spinnerITCDLInternalSignage to "Internal Signage",
            spinnerITCDLCctcCamerasWithAudioFacility to "CCTV Cameras with Audio Facility",
            spinnerITCDLInternetConnections to "Internet Connection Availability",
            spinnerITCDLDoAllComputersHaveTypingTutor to "Typing Tutor Availability on all Computers",
            spinnerITCDLTrainerChair to "Trainer Chair Availability",
            spinnerITCDLTrainerTable to "Trainer Table Availability",
            spinnerITCDLElectricaPowerBackUp to "Electrical Power Backup",
            spinnerITCDLDoes_the_room_has to "Whether the Room has Required Facilities"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Spinner to Base64 Proof Mapping ---
        val spinnerToProofMap = mapOf(
            spinnerITCDLwhether_all_the_academic to Pair(base64ProofITCDLwhether_all_the_academic, "Academic Room Availability proof"),
            spinnerITCDLAcademicRoomInformationBoard to Pair(base64ProofITCDLAcademicRoomInformationBoard, "Academic Room Information Board proof"),
            spinnerITCDLInternalSignage to Pair(base64ProofITCDLInternalSignage, "Internal Signage proof"),
            spinnerITCDLCctcCamerasWithAudioFacility to Pair(base64ProofITCDLCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerITCDLInternetConnections to Pair(base64ProofITCDLInternetConnections, "Internet Connection proof"),
            spinnerITCDLDoAllComputersHaveTypingTutor to Pair(base64ProofITCDLDoAllComputersHaveTypingTutor, "Typing Tutor proof"),
            spinnerITCDLTrainerChair to Pair(base64ProofITCDLTrainerChair, "Trainer Chair proof"),
            spinnerITCDLTrainerTable to Pair(base64ProofITCDLTrainerTable, "Trainer Table proof"),
            spinnerITCDLElectricaPowerBackUp to Pair(base64ProofITCDLElectricaPowerBackUpForThRoom, "Electrical Power Backup proof"),
            spinnerITCDLDoes_the_room_has to Pair(base64ProofITCDLDoes_the_room_has, "Room Facility proof")
        )

        // --- 5️⃣ Conditional Validation: if Spinner = "Yes" then Base64 image mandatory ---
        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 6️⃣ Always Required Image Proofs ---
        val proofImages = mapOf(
            base64ProofPreviewITCDLTypeofRoofItLab to "Please upload proof for Type of Roof",
            base64ProofITCDLFalseCellingProvide to "Please upload proof for False Ceiling",
            base64ProofITCDLabHeightOfCelling to "Please upload proof for Height of Ceiling",
            base64ProofITCDLVentilationAreaInSqFt to "Please upload proof for Ventilation Area (in Sq Ft)",
            base64ProofITCDLabSoundLevelInDb to "Please upload proof for Sound Level (in dB)",
            base64ProofITCDLLanEnabledComputersInNo to "Please upload proof for LAN-enabled Computers",
            base64ProofITCDLTablets to "Please upload proof for Tablets",
            base64ProofITCDLStoolsChairs to "Please upload proof for Stools and Chairs",
            base64ProofITCDLLightsInNo to "Please upload proof for Lights",
            base64ProofITCDLFansInNo to "Please upload proof for Fans",
            base64ProofITCDLItLabPhotograph to "Please upload proof for IT Come Domain Lab Photograph",
//            base64ProofITCDLListofDomain to "Please upload proof for List of Domains"
        )

        for ((base64, message) in proofImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }

    //    TheoryCumITLab
    private fun validateTheoryCumITLab(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etTCILListofDomain to "Theory Cum IT Lab: List of Domain is required",
            etTCILFalseCellingProvide to "Theory Cum IT Lab: False Ceiling Provide is required",
            etTCILTypeofRoofItLab to "Theory Cum IT Lab: Type of Roof is required",
            etTCILHeightOfCelling to "Theory Cum IT Lab: Height of Ceiling is required",
            etTCILVentilationAreaInSqFt to "Theory Cum IT Lab: Ventilation Area in Sq Ft is required",
//        etTCILSoundLevelAsPerSpecifications to "Theory Cum IT Lab: Sound Level as per specifications is required",
//        etTCILSoundLevelInDb to "Theory Cum IT Lab: Sound Level in dB is required",
            etTCILLanEnabledComputersInNo to "Theory Cum IT Lab: LAN-enabled computers number is required",
            etTCILTablets to "Theory Cum IT Lab: Number of tablets is required",
            etTCILStoolsChairs to "Theory Cum IT Lab: Number of stools & chairs is required",
            etTCILTrainerTable to "Theory Cum IT Lab: Trainer table is required",
            etTCILTrainerChair to "Theory Cum IT Lab: Trainer chair is required",
            etTCILLightsInNo to "Theory Cum IT Lab: Number of lights is required",
            etTCILFansInNo to "Theory Cum IT Lab: Number of fans is required",
            etTCILTheoryCumItLabPhotogragh to "Theory Cum IT Lab: Lab photograph is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate all Spinners ---
        val spinnerList = listOf(
            spinnerTCILwhether_all_the_academic to "Whether all the academic rooms are available",
            spinnerTCILLAcademicRoomInformationBoard to "Academic Room Information Board",
            spinnerTCILInternalSignage to "Internal Signage",
            spinnerTCILCctcCamerasWithAudioFacility to "CCTV Cameras with Audio Facility",
            spinnerTCILInternetConnections to "Internet Connections",
            spinnerTCILElectricPowerBackup to "Electrical Power Backup",
            spinnerTCILDLDoAllComputersHaveTypingTutor to "Typing Tutor on all computers",
//        spinnerTCILIPEnabled to "LAN-enabled computers",
            spinnerTCILDLDoes_the_room_has to "Room Air Conditioning"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Conditional Image Validation (if spinner = "Yes") ---
        val spinnerToProofMap = mapOf(
            spinnerTCILwhether_all_the_academic to Pair(base64ProofPreviewTCILwhether_all_the_academic, "Academic Room Availability proof"),
            spinnerTCILLAcademicRoomInformationBoard to Pair(base64ProofPreviewTCILAcademicRoomInformationBoard, "Academic Room Information Board proof"),
            spinnerTCILInternalSignage to Pair(base64ProofPreviewTCILInternalSignage, "Internal Signage proof"),
            spinnerTCILCctcCamerasWithAudioFacility to Pair(base64ProofPreviewTCILCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerTCILInternetConnections to Pair(base64ProofPreviewTCILInternetConnections, "Internet Connection proof"),
            spinnerTCILDLDoAllComputersHaveTypingTutor to Pair(base64ProofPreviewTCILDoAllComputersHaveTypingTutor, "Typing Tutor proof"),
//        spinnerTCILIPEnabled to Pair(base64ProofPreviewTCILLanEnabledComputersInNo, "LAN-enabled Computers proof"),
            spinnerTCILDLDoes_the_room_has to Pair(base64ProofPreviewTCILDoes_the_room_has, "Room Facility proof")
        )

        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 5️⃣ Always Required Base64 Images ---
        val requiredImages = mapOf(
            base64ProofPreviewTCILListofDomain to "List of Domain proof",
            base64ProofPreviewTCILTypeofRoofItLab to "Type of Roof proof",
            base64ProofPreviewTCILFalseCellingProvide to "False Ceiling proof",
            base64ProofPreviewTCILHeightOfCelling to "Height of Ceiling proof",
            base64ProofPreviewTCILVentilationAreaInSqFt to "Ventilation Area proof",
//        base64ProofPreviewTTCILSoundLevelInDb to "Sound Level proof",
            base64ProofPreviewTCILTablets to "Tablets proof",
            base64ProofPreviewTCILStoolsChairs to "Stools & Chairs proof",
            base64ProofPreviewTCILTrainerTable to "Trainer Table proof",
            base64ProofPreviewTCILLanEnabledComputersInNo to "Enabled Computers In No",
            base64ProofPreviewTCILTrainerChair to "Trainer Chair proof",
            base64ProofPreviewTCILLightsInNo to "Lights proof",
            base64ProofPreviewTCILFansInNo to "Fans proof",
            base64ProofPreviewTCILTheoryCumItLabPhotogragh to "Lab Photograph proof"
        )

        for ((base64, message) in requiredImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $message", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    //    TheoryCumDomainLab
    private fun validateTheoryCumDomainLab(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etTCDLTypeofRoofItLab to "Theory Cum Domain Lab: Type of Roof is required",
            etTCDLFalseCellingProvide to "Theory Cum Domain Lab: False Ceiling Provide is required",
            etTCDLHeightOfCelling to "Theory Cum Domain Lab: Height of Ceiling is required",
            etTCDLVentilationAreaInSqFt to "Theory Cum Domain Lab: Ventilation Area in Sq Ft is required",
            etTCDLSoundLevelAsPerSpecifications to "Theory Cum Domain Lab: Sound Level as per specifications is required",
            etTCDLSoundLevelInDb to "Theory Cum Domain Lab: Sound Level in dB is required",
            etTCDLLcdDigitalProjector to "Theory Cum Domain Lab: LCD/Digital Projector is required",
            etTCDLChairForCandidatesInNo to "Theory Cum Domain Lab: Chair for Candidates is required",
            etTCDLTrainerChair to "Theory Cum Domain Lab: Trainer Chair is required",
            etTCDLTrainerTable to "Theory Cum Domain Lab: Trainer Table is required",
            etTCDLWritingBoard to "Theory Cum Domain Lab: Writing Board is required",
            etTCDLLightsInNo to "Theory Cum Domain Lab: Number of Lights is required",
            etTCDLFansInNo to "Theory Cum Domain Lab: Number of Fans is required",
//            etTCDLListofDomain to "Theory Cum Domain Lab: List of Domain is required",
            etTCDLDomainLabPhotogragh to "Theory Cum Domain Lab: Lab Photograph is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate all Spinners ---
        val spinnerList = listOf(
            spinnerTCDLwhether_all_the_academic to "Whether all academic rooms are available",
            spinnerTCDLAcademicRoomInformationBoard to "Academic Room Information Board",
            spinnerTCDLInternalSignage to "Internal Signage",
            spinnerTCDLCctcCamerasWithAudioFacility to "CCTV Cameras with Audio Facility",
            spinnerTCDLPowerBackup to "Power Backup",
            spinnerTCDLDoes_the_room_has to "Room Air Conditioning"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Conditional Image Validation (if spinner = "Yes") ---
        val spinnerToProofMap = mapOf(
            spinnerTCDLwhether_all_the_academic to Pair(base64ProofPreviewTCDLwhether_all_the_academic, "Academic Room Availability proof"),
            spinnerTCDLAcademicRoomInformationBoard to Pair(base64ProofPreviewTCDLAcademicRoomInformationBoard, "Academic Room Information Board proof"),
            spinnerTCDLInternalSignage to Pair(base64ProofPreviewTCDLInternalSignage, "Internal Signage proof"),
            spinnerTCDLCctcCamerasWithAudioFacility to Pair(base64ProofPreviewTCDLCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerTCDLPowerBackup to Pair(base64ProofPreviewTCDLElectricaPowerBackUpForThRoom, "Power Backup proof"),
            spinnerTCDLDoes_the_room_has to Pair(base64ProofPreviewTCDLDoes_the_room_has, "Room Air Conditioning proof")
        )

        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 5️⃣ Always Required Base64 Images ---
        val requiredImages = mapOf(
            base64ProofPreviewTCDLTypeofRoofItLab to "Type of Roof proof",
            base64ProofPreviewTCDLFalseCellingProvide to "False Ceiling proof",
            base64ProofPreviewTCDLHeightOfCelling to "Height of Ceiling proof",
            base64ProofPreviewTCDLVentilationAreaInSqFt to "Ventilation Area proof",
            base64ProofPreviewTCDLSoundLevelInDb to "Sound Level proof",
            base64ProofPreviewTCDLLcdDigitalProjector to "LCD/Digital Projector proof",
            base64ProofPreviewTCDLChairForCandidatesInNo to "Chair for Candidates proof",
            base64ProofPreviewTCDLTrainerChair to "Trainer Chair proof",
            base64ProofPreviewTCDLTrainerTable to "Trainer Table proof",
            base64ProofPreviewTCDLWritingBoard to "Writing Board proof",
            base64ProofPreviewTCDLLightsInNo to "Lights proof",
            base64ProofPreviewTCDLFansInNo to "Fans proof",
//            base64ProofPreviewTCDLListofDomain to "List of Domain proof",
            base64ProofPreviewTCDLDomainLabPhotogragh to "Lab Photograph proof"
        )

        for ((base64, message) in requiredImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $message", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    //    DomainLab
    private fun validateDomainLab(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Length is required",
            etWidth to "Width is required",
            etDLTypeofRoofItLab to "Domain Lab: Type of Roof is required",
            etDLFalseCellingProvide to "Domain Lab: False Ceiling Provide is required",
            etDLHeightOfCelling to "Domain Lab: Height of Ceiling is required",
            etDLVentilationAreaInSqFt to "Domain Lab: Ventilation Area in Sq Ft is required",
            etDLSoundLevelAsPerSpecifications to "Domain Lab: Sound Level as per specifications is required",
            etDLSoundLevelInDb to "Domain Lab: Sound Level in dB is required",
            etDLLcdDigitalProjector to "Domain Lab: LCD/Digital Projector is required",
            etDLChairForCandidatesInNo to "Domain Lab: Chair for Candidates is required",
            etDLTrainerChair to "Domain Lab: Trainer Chair is required",
            etDLTrainerTable to "Domain Lab: Trainer Table is required",
            etDLWritingBoard to "Domain Lab: Writing Board is required",
            etDLLightsInNo to "Domain Lab: Number of Lights is required",
            etDLFansInNo to "Domain Lab: Number of Fans is required",
//            etDLDomainLabPhotogragh to "Domain Lab: Lab Photograph is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate all Spinners ---
        val spinnerList = listOf(
            spinnerDLwhether_all_the_academic to "Whether all academic rooms are available",
            spinnerDLAcademicRoomInformationBoard to "Academic Room Information Board",
            spinnerDLInternalSignage to "Internal Signage",
            spinnerDLCctcCamerasWithAudioFacility to "CCTV Cameras with Audio Facility",
            spinnerDLElectricaPowerBackUp to "Power Backup",
            spinnerDLDoes_the_room_has to "Room Air Conditioning",
            spinnerDLListofDomain to "Domain Lab List of Domain"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Conditional Image Validation (if spinner = "Yes") ---
        val spinnerToProofMap = mapOf(
            spinnerDLwhether_all_the_academic to Pair(base64ProofPreviewDLwhether_all_the_academic, "Academic Room Availability proof"),
            spinnerDLAcademicRoomInformationBoard to Pair(base64ProofPreviewDLAcademicRoomInformationBoard, "Academic Room Information Board proof"),
            spinnerDLInternalSignage to Pair(base64ProofPreviewDLInternalSignage, "Internal Signage proof"),
            spinnerDLCctcCamerasWithAudioFacility to Pair(base64ProofPreviewDLCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerDLElectricaPowerBackUp to Pair(base64ProofPreviewDLElectricaPowerBackUpForThRoom, "Power Backup proof"),
            spinnerDLDoes_the_room_has to Pair(base64ProofPreviewDLDoes_the_room_has, "Room Air Conditioning proof"),
            spinnerDLListofDomain to Pair(base64ProofPreviewDLILListofDomain, "Domain Lab List of Domain proof")
        )

        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 5️⃣ Always Required Base64 Images ---
        val requiredImages = mapOf(
            base64ProofPreviewDLTypeofRoofItLab to "Type of Roof proof",
            base64ProofPreviewDLFalseCellingProvide to "False Ceiling proof",
            base64ProofPreviewDLHeightOfCelling to "Height of Ceiling proof",
            base64ProofPreviewDLVentilationAreaInSqFt to "Ventilation Area proof",
            base64ProofPreviewDLSoundLevelInDb to "Sound Level proof",
            base64ProofPreviewDLLcdDigitalProjector to "LCD/Digital Projector proof",
            base64ProofPreviewDLChairForCandidatesInNo to "Chair for Candidates proof",
            base64ProofPreviewDLTrainerChair to "Trainer Chair proof",
            base64ProofPreviewDLTrainerTable to "Trainer Table proof",
            base64ProofPreviewDLWritingBoard to "Writing Board proof",
            base64ProofPreviewDLLightsInNo to "Lights proof",
            base64ProofPreviewDLFansInNo to "Fans proof",
//            base64ProofPreviewDLDomainLabPhotogragh to "Lab Photograph proof"
        )

        for ((base64, message) in requiredImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $message", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }
    //    Theory Class Room
    private fun validateTheoryClassRoom(): Boolean {
        var isValid = true
        var firstFocusSet = false

        // --- 1️⃣ Validate EditText Fields ---
        val editTextFields = mapOf(
            etLength to "Theory Class Room: Length is required",
            etWidth to "Theory Class Room: Width is required",
            etTCRTypeofRoofItLab to "Theory Class Room: Type of Roof is required",
            etTCRFalseCellingProvide to "Theory Class Room: False Ceiling Provide is required",
            etTCRHeightOfCelling to "Theory Class Room: Height of Ceiling is required",
            etTCRVentilationAreaInSqFt to "Theory Class Room: Ventilation Area in Sq Ft is required",
            etTCRSoundLevelAsPerSpecifications to "Theory Class Room: Sound Level as per specifications is required",
            etTCRSoundLevelInDb to "Theory Class Room: Sound Level in dB is required",
            etTCRLcdDigitalProjector to "Theory Class Room: Digital Projector is required",
            etTCRChairForCandidatesInNo to "Theory Class Room: Chair for Candidates is required",
            etTCRTrainerChair to "Theory Class Room: Trainer Chair is required",
            etTCRTrainerTable to "Theory Class Room: Trainer Table is required",
            etTCRWritingBoard to "Theory Class Room: Writing Board is required",
            etTCRLightsInNo to "Theory Class Room: Number of Lights is required",
            etTCRFansInNo to "Theory Class Room: Number of Fans is required",
            etTCRDomainLabPhotogragh to "Theory Class Room: Photograph is required"
        )

        for ((field, message) in editTextFields) {
            val text = field.text?.toString()?.trim().orEmpty()
            if (text.isEmpty()) {
                field.error = message
                if (!firstFocusSet) {
                    field.requestFocus()
                    firstFocusSet = true
                }
                isValid = false
            } else {
                field.error = null
            }
        }

        // --- 2️⃣ Spinner Validation Helper ---
        fun checkSpinner(spinner: Spinner, fieldName: String): Boolean {
            if (spinner.selectedItemPosition <= 0) {
                Toast.makeText(requireContext(), "Please select $fieldName", Toast.LENGTH_SHORT).show()
                if (!firstFocusSet) {
                    spinner.requestFocus()
                    firstFocusSet = true
                }
                return false
            }
            return true
        }

        // --- 3️⃣ Validate all Spinners ---
        val spinnerList = listOf(
            spinnerTCRwhether_all_the_academic to "Whether all the Academic Rooms are available",
            spinnerTCRAcademicRoomInformationBoard to "Academic Room Information Board",
            spinnerTCRInternalSignage to "Internal Signage",
            spinnerTCRCctcCamerasWithAudioFacility to "CCTV Cameras with Audio Facility",
            spinnerTCRDoes_the_room_has to "Room Air Conditioning / Facilities"
        )

        for ((spinner, name) in spinnerList) {
            if (!checkSpinner(spinner, name)) isValid = false
        }

        // --- 4️⃣ Conditional Image Validation (if spinner = "Yes") ---
        val spinnerToProofMap = mapOf(
            spinnerTCRwhether_all_the_academic to Pair(base64ProofPreviewTCRwhether_all_the_academic, "Academic Room Availability proof"),
            spinnerTCRAcademicRoomInformationBoard to Pair(base64ProofPreviewTCRAcademicRoomInformationBoard, "Academic Room Information Board proof"),
            spinnerTCRInternalSignage to Pair(base64ProofPreviewTCRInternalSignage, "Internal Signage proof"),
            spinnerTCRCctcCamerasWithAudioFacility to Pair(base64ProofPreviewTCRCctcCamerasWithAudioFacility, "CCTV Camera proof"),
            spinnerTCRDoes_the_room_has to Pair(base64ProofPreviewTCRDoes_the_room_has, "Room Air Conditioning proof")
        )

        for ((spinner, proofPair) in spinnerToProofMap) {
            val (base64, proofName) = proofPair
            val selected = spinner.selectedItem?.toString()?.trim()?.lowercase() ?: ""
            if (selected == "yes" && base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $proofName (You selected Yes)", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        // --- 5️⃣ Always Required Base64 Images ---
        val requiredImages = mapOf(
            base64ProofPreviewTCRTypeofRoofItLab to "Type of Roof proof",
            base64ProofPreviewTCRFalseCellingProvide to "False Ceiling proof",
            base64ProofPreviewTCRHeightOfCelling to "Height of Ceiling proof",
            base64ProofPreviewTCRVentilationAreaInSqFt to "Ventilation Area proof",
            base64ProofPreviewTCRSoundLevelInDb to "Sound Level proof",
            base64ProofPreviewTCRLcdDigitalProjector to "Digital Projector proof",
            base64ProofPreviewTCRChairForCandidatesInNo to "Chair for Candidates proof",
            base64ProofPreviewTCRTrainerChair to "Trainer Chair proof",
            base64ProofPreviewTCRTrainerTable to "Trainer Table proof",
            base64ProofPreviewTCRWritingBoard to "Writing Board proof",
            base64ProofPreviewTCRLightsInNo to "Lights proof",
            base64ProofPreviewTCRFansInNo to "Fans proof",
            base64ProofPreviewTCRDomainLabPhotogragh to "Photograph proof"
        )

        for ((base64, message) in requiredImages) {
            if (base64.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Please upload $message", Toast.LENGTH_SHORT).show()
                isValid = false
            }
        }

        return isValid
    }






//       ItLab    Ajit Ranjan

    private fun submitItLab() {




        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""

        // ✅ Show Progress Dialog before making API call
        // ✅ Show progress dialog using utility
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()
        val ITLTypeofRoofItLab = etITLTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val ITLHeightOfCelling = etITLHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val ventilationArea = etITLVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val ITLSoundLevelAsPerSpecifications = etITLSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val ITLSoundLevelInDb = etITLSoundLevelInDb.text.toString().toIntOrNull() ?: 0
        val ITLLanEnabledComputersInNo = etITLLanEnabledComputersInNo.text.toString()
        val ITLTablets = etITLTablets.text.toString()
        val ITLStoolsChairs = etITLStoolsChairs.text.toString()
        val ITLLightsInNo = etITLLightsInNo.text.toString()
        val ITLFansInNo = etITLFansInNo.text.toString()
        val request = ITLabDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType = ITLTypeofRoofItLab.toString(),
            roofTypeAttachment =  base64ProofPreviewITLTypeofRoofItLab ?: "",
            falseCeiling = spinnerITLFalseCellingProvide.selectedItem.toString()
            , falseCeilingAttachment = base64ProofITLFalseCellingProvide?: "",
            ceilingHeight = ITLHeightOfCelling.toString(),
            ceilingHeightAttachment = base64ProofITLHeightOfCelling?: "",
            ventilationArea=ventilationArea.toString(),
            ventilationAreaAttachment=base64ProofITLVentilationAreaInSqFt?: "",
            soundLevelSpecifications = ITLSoundLevelAsPerSpecifications.toString(),
            soundLevel=ITLSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofITLSoundLevelInDb?: "",
            centerSoungProofed=spinnerITLwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofITLwhether_all_the_academic?: "",
            roomInfoBoard=spinnerITLAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofITLAcademicRoomInformationBoard?: "",
            internalSignage=spinnerITLInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofITLInternalSignage?: "",
            audioCamera=spinnerITLCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment=base64ProofITLCctcCamerasWithAudioFacility?: "",
            lanEnabledComputers=ITLLanEnabledComputersInNo.toString(),
            lanEnabledComputersAttachment=base64ProofITLLanEnabledComputersInNo?: "",
            internetConnection=spinnerITLabInternetConnections.selectedItem.toString(),
            internetConnectionAttachment=base64ProofITLInternetConnections?: "",
            typingTuterComputers=spinnerITLDoAllComputersHaveTypingTutor.selectedItem.toString(),
            typingTuterComputersAttachment=base64ProofITLDoAllComputersHaveTypingTutor?: "",
            tablets=ITLTablets.toString(),
            tabletsAttachment=base64ProofITLTablets?: "",
            candidatesChair=ITLStoolsChairs.toString(),
            candidatesChairAttachment=base64ProofITLStoolsChairs?: "",
            trainersChair=spinnerITLTrainerChair.selectedItem.toString(),
            trainersChairAttachment=base64ProofITLTrainerChair?: "",
            trainersTable=spinnerITLTrainerTable.selectedItem.toString(),
            trainersTableAttachment=base64ProofITLTrainerTable?: "",
            lightsAcademic=ITLLightsInNo.toString(),
            lightsAttachment=base64ProofITLLightsInNo?: "",
            fansAcademic=ITLFansInNo.toString(),
            fansAttachment=base64ProofITLFansInNo?: "",
            electricalPowerBackup=spinnerITLepbftr.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofITLElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=spinnerITLtLabPhotograph.selectedItem.toString(),
            roomPhotographAttachment=base64ProofITLItLabPhotograph?: "",
            airConditioningRoom=spinnerITLDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofITLDoes_the_room_has?: "",
        )

        viewModel.SubmitITLABDataToServer(request, token)




    }
    //    Office Cum(Counselling room)    Ajit Ranjan
    private fun SubmitOfficeCumCounsellingRoom() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()


        val OfficeRoomPhotograph = etOfficeRoomPhotograph.text.toString().toIntOrNull() ?: 0
        val OfficeCumTypeofRoofItLab = etOfficeCumTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val OfficeCumHeightOfCelling = etOfficeCumHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val OfficeCumSplaceforSecuringDoc = etOfficeCumSplaceforSecuringDoc.text.toString().toIntOrNull() ?: 0
        val OfficeCumAnOfficeTableNo = etOfficeCumAnOfficeTableNo.text.toString().toIntOrNull() ?: 0
        val OfficeCumChairs = etOfficeCumChairs.text.toString().toIntOrNull() ?: 0
        val OfficeCumPrinterCumScannerInNo = etOfficeCumPrinterCumScannerInNo.text.toString()
        val OfficeCumDigitalCameraInNo = etOfficeCumDigitalCameraInNo.text.toString()

        val request = SubmitOfficeCumCounsellingRoomDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType = OfficeCumTypeofRoofItLab.toString(),
            roofTypeAttachment =  base64ProofOfficeCumTypeofRoofItLab ?: "",
            falseCeiling = spinnerOfficeCumFalseCellingProvide.selectedItem.toString(),
            falseCeilingAttachment = base64ProofOfficeCumFalseCellingProvide?: "",
            ceilingHeight = OfficeCumHeightOfCelling.toString(),
            ceilingHeightAttachment = base64ProofOfficeCumHeightOfCelling?: "",
            storageSecuringDocument=OfficeCumSplaceforSecuringDoc.toString(),
            storageSecuringDocumentAttachment=base64ProofOfficeCumSplaceforSecuringDoc?: "",
            officeComputerTable=spinnerOfficeCumTableOfofficeCumpter.selectedItem.toString(),
            officeComputerTableAttachment=base64ProofOfficeCumTableOfofficeCumpter?: "",


            officeChair=OfficeCumChairs.toString(),
            officeChairAttachment=base64ProofOfficeCumChairs?: "",
            printerScannerAcademic=OfficeCumPrinterCumScannerInNo.toString(),
            printerScannerAttachment=base64ProofOfficeCumPrinterCumScannerInNo?: "",
            digitalCameraAcademic=OfficeCumDigitalCameraInNo.toString(),
            digitalCameraAttachment=base64ProofOfficeCumPrinterCumScannerInNo?: "",
            electricalPowerBackup = spinnerOfficeCumLepbftr.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofOfficeCumElectricialPowerBackup?: "",
            roomPhotograph=OfficeRoomPhotograph.toString(),
            roomPhotographAttachment =base64ProofPreviewOfficeRoomPhotograph?: "",

            )

        viewModel.SubmitOfficeCumCounsellingRoomDataToServer(request, token)
    }

    private fun SubmitReceptionArea() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""




        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()
        val request = ReceptionAreaRoomDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
//            sanctionOrder = sanctionOrder,
            sanctionOrder = AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,

            roomPhotograph=spinnerReceptionAreaEPBR.selectedItem.toString(),
            roomPhotographAttachment =base64ProofPreviewReceptionAreaPhotogragh?: "",

            )

        viewModel.SubmitReceptionAreaDataToServer(request, token)
    }
    private fun SubmitCounsellingRoom() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()
        val request = ReceptionAreaRoomDetailsRequest(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
//            sanctionOrder = sanctionOrder,
            sanctionOrder = AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,

            roomPhotograph=spinnerCounsellingRoomAreaPhotograph.selectedItem.toString(),
            roomPhotographAttachment =base64ProofPreviewCounsellingRoomPhotogragh?: "",

            )

        viewModel.SubmitReceptionAreaDataToServer(request, token)
    }
    private fun SubmitOfficeRoom() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val ORPhotograph = etOROfficeRoomPhotograph.text.toString().toIntOrNull() ?: 0
        val ORTypeofRoofItLab = etORTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val ORHeightOfCelling = etORHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val ORSplaceforSecuringDoc = etORSplaceforSecuringDoc.text.toString().toIntOrNull() ?: 0
        val ORAnOfficeTableNo = etORAnOfficeTableNo.text.toString().toIntOrNull() ?: 0
        val ORChairs = etORChairs.text.toString().toIntOrNull() ?: 0
        val ORPrinterCumScannerInNo = etORPrinterCumScannerInNo.text.toString()
        val ORDigitalCameraInNo = etORDigitalCameraInNo.text.toString()

        val request = OfficeRoomDetailsRequest(

            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,  tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roomPhotograph=ORPhotograph.toString(),
            roomPhotographAttachment=base64ProofPreviewOROfficeRoomORPhotograph?: "",
            roofType = ORTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofORTypeofRoofItLab ?: "",
            falseCeiling =spinnerORFalseCellingProvide.selectedItem.toString(),
            falseCeilingAttachment = base64ProofORFalseCellingProvide?: "",
            ceilingHeight=ORHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofORHeightOfCelling?: "",
            storageSecuringDocument=ORSplaceforSecuringDoc.toString(),
            storageSecuringDocumentAttachment=base64ProofORSplaceforSecuringDoc?: "",
            officeTable=ORAnOfficeTableNo.toString(),
            officeTableAttachment=base64ProofORAnOfficeTableNo?: "",
            officeChair=ORChairs.toString(),
            officeChairAttachment=base64ProofORChairs?: "",
            officeComputerTable=spinnerORTableOfofficeCumpter.selectedItem.toString(),
            officeComputerTableAttachment=base64ProofORTableOfofficeCumpter?: "",
            printerScannerAcademic=ORPrinterCumScannerInNo.toString(),
            printerScannerAttachment=base64ProofOfficeCumPrinterCumScannerInNo?: "",
            digitalCameraAcademic=ORDigitalCameraInNo.toString(),
            digitalCameraAttachment=base64ProofORPrinterCumScannerInNo?: "",
            electricalPowerBackup=spinnerORPOEPBFTR.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofORElectricialPowerBackup?: "")


        viewModel.SubmitOfficeRoomDataToServer(request, token)
    }




    private fun SubmitITComeDomainLab() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val ITCDLTypeofRoofItLab = etITCDLTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val ITCDLFalseCellingProvide = etITCDLFalseCellingProvide.text.toString().toIntOrNull() ?: 0
        val ITCDLHeightOfCelling = etITCDLHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val ITCDLVentilationAreaInSqFt = etITCDLVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val ITCDLSoundLevelAsPerSpecifications = etITCDLSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val ITCDLabSoundLevelInDb = etITCDLabSoundLevelInDb.text.toString().toIntOrNull() ?: 0
        val ITCDLLanEnabledComputersInNo = etITCDLLanEnabledComputersInNo.text.toString()
        val ITCDLTablets = etITCDLTablets.text.toString()
        val ITCDLStoolsChairs = etITCDLStoolsChairs.text.toString()
        val ITCDLLightsInNo = etITCDLLightsInNo.text.toString()
        val ITCDLFansInNo = etITCDLFansInNo.text.toString()
        val ITCDLItLabPhotograph = etITCDLItLabPhotograph.text.toString()
        val ITCDLListofDomain = etITCDLListofDomain.text.toString()




        val request = ITComeDomainLabDetailsRequest(

            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,  tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType=ITCDLTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofPreviewITCDLTypeofRoofItLab?: ""
            ,falseCeiling=ITCDLFalseCellingProvide.toString(),
            falseCeilingAttachment=base64ProofITCDLFalseCellingProvide?: "",
            ceilingHeight=ITCDLHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofITCDLabHeightOfCelling?: "",
            ventilationArea=ITCDLVentilationAreaInSqFt.toString(),
            ventilationAreaAttachment=base64ProofITCDLVentilationAreaInSqFt?: "",
            soundLevelSpecifications=ITCDLSoundLevelAsPerSpecifications.toString(),
            soundLevel=ITCDLabSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofITCDLabSoundLevelInDb?: "",
            centerSoungProofed=spinnerITCDLwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofITCDLwhether_all_the_academic?: "",
            roomInfoBoard=spinnerITCDLAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofITCDLAcademicRoomInformationBoard?: "",
            internalSignage = spinnerITCDLInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofITCDLInternalSignage?: "",
            audioCamera = spinnerITCDLCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment = base64ProofITCDLCctcCamerasWithAudioFacility?: "",
            lanEnabledComputers =ITCDLLanEnabledComputersInNo.toString(),
            lanEnabledComputersAttachment = base64ProofITCDLLanEnabledComputersInNo?: "",
            internetConnection = spinnerITCDLInternetConnections.selectedItem.toString(),
            internetConnectionAttachment=base64ProofITCDLInternetConnections?: "",
            typingTuterComputers=spinnerITCDLDoAllComputersHaveTypingTutor.selectedItem.toString(),
            typingTuterComputersAttachment=base64ProofITCDLDoAllComputersHaveTypingTutor?: "",
            tablets = ITCDLTablets.toString(),
            tabletsAttachment = base64ProofITCDLTablets?: "",
            candidatesChair=ITCDLStoolsChairs.toString(),
            candidatesChairAttachment=base64ProofITCDLStoolsChairs?: "",
            trainersChair=spinnerITCDLTrainerChair.selectedItem.toString(),
            trainersChairAttachment=base64ProofITCDLTrainerChair?: "",
            trainersTable=spinnerITCDLTrainerTable.selectedItem.toString(),
            trainersTableAttachment=base64ProofITCDLTrainerTable?: "",
            lightsAcademic=ITCDLLightsInNo.toString(),
            lightsAttachment=base64ProofITCDLLightsInNo?: "",
            fansAcademic=ITCDLFansInNo.toString(),
            fansAttachment=base64ProofITCDLFansInNo?: "",
            electricalPowerBackup=spinnerITCDLElectricaPowerBackUp.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofITCDLElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=ITCDLItLabPhotograph.toString(),
            roomPhotographAttachment=base64ProofITCDLItLabPhotograph?: "",
            domainRelatedEquipment="",
            domainRelatedEquipmentAttachment="",
            airConditioningRoom=spinnerITCDLDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofITCDLDoes_the_room_has?: "")


        viewModel.SubmitITComeDomainLabDataToServer(request, token)
    }

    private fun SubmitTCITLABLab() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val TCILListofDomain = etTCILListofDomain.text.toString().toIntOrNull() ?: 0
        val TCILTypeofRoofItLab = etTCILTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val TCILFalseCellingProvide = etTCILFalseCellingProvide.text.toString().toIntOrNull() ?: 0
        val TCILHeightOfCelling = etTCILHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val TCILVentilationAreaInSqFt = etTCILVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val TCILSoundLevelAsPerSpecifications = etTCILSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val TCILSoundLevelInDb = etTCILSoundLevelInDb.text.toString()
        val TCILLanEnabledComputersInNo = etTCILLanEnabledComputersInNo.text.toString()
        val TCILTablets = etTCILTablets.text.toString()
        val TCILStoolsChairs = etTCILStoolsChairs.text.toString()
        val TCILTrainerTable = etTCILTrainerTable.text.toString()
        val TCILTrainerChair = etTCILTrainerChair.text.toString()
        val TCILLightsInNo = etTCILLightsInNo.text.toString()
        val TCILFansInNo = etTCILFansInNo.text.toString()
        val TCILTheoryCumItLabPhotogragh = etTCILTheoryCumItLabPhotogragh.text.toString()




        val request = TCITLDomainLabDetailsRequest(

            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType=TCILTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofPreviewTCILTypeofRoofItLab?: ""
            ,falseCeiling=TCILFalseCellingProvide.toString(),
            falseCeilingAttachment=base64ProofPreviewTCILFalseCellingProvide?: "",
            ceilingHeight=TCILHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofPreviewTCILHeightOfCelling?: "",
            ventilationArea=TCILVentilationAreaInSqFt.toString(),
            ventilationAreaAttachment=base64ProofPreviewTCILVentilationAreaInSqFt?: "",
            soundLevelSpecifications=TCILSoundLevelAsPerSpecifications.toString(),
            soundLevel=TCILSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofPreviewTTCILSoundLevelInDb?: "",
            centerSoungProofed=spinnerTCILwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofPreviewTCILwhether_all_the_academic?: "",
            roomInfoBoard=spinnerTCILLAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofPreviewTCILAcademicRoomInformationBoard?: "",
            internalSignage = spinnerTCILInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofPreviewTCILInternalSignage?: "",
            audioCamera = spinnerTCILCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment = base64ProofPreviewTCILCctcCamerasWithAudioFacility?: "",
            lanEnabledComputers =TCILLanEnabledComputersInNo.toString(),
            lanEnabledComputersAttachment = base64ProofPreviewTCILLanEnabledComputersInNo?: "",
            internetConnection = spinnerTCILInternetConnections.selectedItem.toString(),
            internetConnectionAttachment=base64ProofPreviewTCILInternetConnections?: "",
            typingTuterComputers=spinnerTCILDLDoAllComputersHaveTypingTutor.selectedItem.toString(),
            typingTuterComputersAttachment=base64ProofPreviewTCILDoAllComputersHaveTypingTutor?: "",
            tablets = TCILTablets.toString(),
            tabletsAttachment = base64ProofPreviewTCILTablets?: "",
            candidatesChair=TCILStoolsChairs.toString(),
            candidatesChairAttachment=base64ProofPreviewTCILStoolsChairs?: "",
            trainersChair=TCILTrainerChair.toString(),
            trainersChairAttachment=base64ProofPreviewTCILTrainerChair?: "",
            trainersTable=TCILTrainerTable.toString(),
            trainersTableAttachment=base64ProofPreviewTCILTrainerTable?: "",
            lightsAcademic=TCILLightsInNo.toString(),
            lightsAttachment=base64ProofPreviewTCILLightsInNo?: "",
            fansAcademic=TCILFansInNo.toString(),
            fansAttachment=base64ProofPreviewTCILFansInNo?: "",
            electricalPowerBackup=spinnerTCILElectricPowerBackup.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofPreviewTCILElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=TCILTheoryCumItLabPhotogragh.toString(),
            roomPhotographAttachment=base64ProofPreviewTCILTheoryCumItLabPhotogragh?: "",
            domainRelatedEquipment=TCILListofDomain.toString(),
            domainRelatedEquipmentAttachment=base64ProofPreviewTCILListofDomain?: "",
            airConditioningRoom=spinnerTCILDLDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofPreviewTCILDoes_the_room_has?: "")


        viewModel.SubmitTheoryComeItLabDataToServer(request, token)
    }


    private fun SubmitTCDL() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val TCDLTypeofRoofItLab = etTCDLTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val TCDLFalseCellingProvide = etTCDLFalseCellingProvide.text.toString().toIntOrNull() ?: 0
        val TCDLHeightOfCelling = etTCDLHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val TCDLVentilationAreaInSqFt = etTCDLVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val TCDLSoundLevelAsPerSpecifications = etTCDLSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val TCDLSoundLevelInDb = etTCDLSoundLevelInDb.text.toString().toIntOrNull() ?: 0
        val TCDLLcdDigitalProjector = etTCDLLcdDigitalProjector.text.toString()
        val TCDLChairForCandidatesInNo = etTCDLChairForCandidatesInNo.text.toString()
        val TCDLTrainerChair = etTCDLTrainerChair.text.toString()
        val TCDLTrainerTable = etTCDLTrainerTable.text.toString()
        val TCDLWritingBoard = etTCDLWritingBoard.text.toString()
        val TCDLLightsInNo = etTCDLLightsInNo.text.toString()
        val TCDLFansInNo = etTCDLFansInNo.text.toString()
        val TCDLListofDomain = etTCDLListofDomain.text.toString()
        val TCDLDomainLabPhotogragh = etTCDLDomainLabPhotogragh.text.toString()




        val request = TCDLRequest(


//
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType=TCDLTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofPreviewTCDLTypeofRoofItLab?: "",
            falseCeiling=TCDLFalseCellingProvide.toString(),
            falseCeilingAttachment=base64ProofPreviewTCDLFalseCellingProvide?: "",
            ceilingHeight=TCDLHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofPreviewTCDLHeightOfCelling?: "",
            ventilationArea=TCDLVentilationAreaInSqFt.toString(),
            ventilationAreaAttachment=base64ProofPreviewTCDLVentilationAreaInSqFt?: "",
            soundLevelSpecifications=TCDLSoundLevelAsPerSpecifications.toString(),
            soundLevel=TCDLSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofPreviewTCDLSoundLevelInDb?: "",
            centerSoungProofed=spinnerTCDLwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofPreviewTCDLwhether_all_the_academic?: "",
            roomInfoBoard=spinnerTCDLAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofPreviewTCDLAcademicRoomInformationBoard?: "",
            internalSignage = spinnerTCDLInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofPreviewTCDLInternalSignage?: "",
            audioCamera = spinnerTCDLCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment = base64ProofPreviewTCDLCctcCamerasWithAudioFacility?: "",
            digitalProjector =TCDLLcdDigitalProjector.toString(),
            digitalProjectorAttachment = base64ProofPreviewTCDLLcdDigitalProjector?: "",
            candidatesChair=TCDLChairForCandidatesInNo.toString(),
            candidatesChairAttachment=base64ProofPreviewTCDLChairForCandidatesInNo?: "",
            trainersChair=TCDLTrainerChair.toString(),
            trainersChairAttachment=base64ProofPreviewTCDLTrainerChair?: "",
            trainersTable=TCDLTrainerTable.toString(),
            trainersTableAttachment=base64ProofPreviewTCDLTrainerTable?: "",
            lightsAcademic=TCDLLightsInNo.toString(),
            lightsAttachment=base64ProofPreviewTCDLLightsInNo?: "",
            fansAcademic=TCDLFansInNo.toString(),
            fansAttachment=base64ProofPreviewTCDLFansInNo?: "",
            electricalPowerBackup=spinnerTCDLPowerBackup.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofPreviewTCDLElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=TCDLDomainLabPhotogragh.toString(),
            roomPhotographAttachment=base64ProofPreviewTCDLListofDomain?: "",
            domainRelatedEquipment=TCDLListofDomain.toString(),
            domainRelatedEquipmentAttachment=base64ProofPreviewTCDLDomainLabPhotogragh?: "",

            airConditioningRoom=spinnerTCDLDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofPreviewTCDLDoes_the_room_has?: "",
            writingBoard=TCDLWritingBoard.toString(),
            writingBoardAttachment=base64ProofPreviewTCDLWritingBoard?: ""




        )


        viewModel.SubmitTCDLDataToServer(request, token)
    }
    private fun SubmitDL() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val DLTypeofRoofItLab = etDLTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val DLFalseCellingProvide = etDLFalseCellingProvide.text.toString().toIntOrNull() ?: 0
        val DLHeightOfCelling = etDLHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val DLVentilationAreaInSqFt = etDLVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val DLSoundLevelAsPerSpecifications = etDLSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val DLSoundLevelInDb = etDLSoundLevelInDb.text.toString().toIntOrNull() ?: 0
        val DLLcdDigitalProjector = etDLLcdDigitalProjector.text.toString()
        val DLChairForCandidatesInNo = etDLChairForCandidatesInNo.text.toString()
        val DLTrainerChair = etDLTrainerChair.text.toString()
        val DLTrainerTable = etDLTrainerTable.text.toString()
        val DLWritingBoard = etDLWritingBoard.text.toString()
        val DLLightsInNo = etDLLightsInNo.text.toString()
        val DLFansInNo = etDLFansInNo.text.toString()
        val DLListofDomain = etDLDomainLabPhotogragh.text.toString()
        val DLDomainLabPhotogragh = etTCDLDomainLabPhotogragh.text.toString()




        val request = DLRequest(


//
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType.toString(),
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType=DLTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofPreviewDLTypeofRoofItLab?: "",
            falseCeiling=DLFalseCellingProvide.toString(),
            falseCeilingAttachment=base64ProofPreviewDLFalseCellingProvide?: "",
            ceilingHeight=DLHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofPreviewDLHeightOfCelling?: "",
            ventilationArea=DLVentilationAreaInSqFt.toString(),
            ventilationAreaAttachment=base64ProofPreviewDLVentilationAreaInSqFt?: "",
            soundLevelSpecifications=DLSoundLevelAsPerSpecifications.toString(),
            soundLevel=DLSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofPreviewDLSoundLevelInDb?: "",
            centerSoungProofed=spinnerDLwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofPreviewDLwhether_all_the_academic?: "",
            roomInfoBoard=spinnerDLAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofPreviewDLAcademicRoomInformationBoard?: "",
            internalSignage = spinnerDLInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofPreviewDLInternalSignage?: "",
            audioCamera = spinnerDLCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment = base64ProofPreviewDLCctcCamerasWithAudioFacility?: "",
            digitalProjector =DLLcdDigitalProjector.toString(),
            digitalProjectorAttachment = base64ProofPreviewDLLcdDigitalProjector?: "",
            candidatesChair=DLChairForCandidatesInNo.toString(),
            candidatesChairAttachment=base64ProofPreviewDLChairForCandidatesInNo?: "",
            trainersChair=DLTrainerChair.toString(),
            trainersChairAttachment=base64ProofPreviewDLTrainerChair?: "",
            trainersTable=DLTrainerTable.toString(),
            trainersTableAttachment=base64ProofPreviewDLTrainerTable?: "",
            lightsAcademic=DLLightsInNo.toString(),
            lightsAttachment=base64ProofPreviewDLLightsInNo?: "",
            fansAcademic=DLFansInNo.toString(),
            fansAttachment=base64ProofPreviewDLFansInNo?: "",
            electricalPowerBackup=spinnerDLElectricaPowerBackUp.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofPreviewDLElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=DLDomainLabPhotogragh.toString(),
            roomPhotographAttachment=base64ProofPreviewDLDomainLabPhotogragh?: "",
            domainRelatedEquipment=DLListofDomain.toString(),
            domainRelatedEquipmentAttachment=base64ProofPreviewDLILListofDomain?: "",
            airConditioningRoom=spinnerDLDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofPreviewDLDoes_the_room_has?: "",
            writingBoard=DLWritingBoard.toString(),
            writingBoardAttachment=base64ProofPreviewDLWritingBoard?: ""



        )


        viewModel.SubmitDLDataToServer(request, token)
    }
    private fun SubmitTCR() {
        ProgressDialogUtil.showProgressDialog(requireContext(), "Please Wait...")
        val token = requireContext()
            .getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("ACCESS_TOKEN", "") ?: ""
        val Length = etLength.text.toString()
        val Width = etWidth.text.toString()
        val Area = tvArea.text.toString()





        val TCRTypeofRoofItLab = etTCRTypeofRoofItLab.text.toString().toIntOrNull() ?: 0
        val TCRFalseCellingProvide = etTCRFalseCellingProvide.text.toString().toIntOrNull() ?: 0
        val TCRHeightOfCelling = etTCRHeightOfCelling.text.toString().toIntOrNull() ?: 0
        val TCRVentilationAreaInSqFt = etTCRVentilationAreaInSqFt.text.toString().toIntOrNull() ?: 0
        val TCRSoundLevelAsPerSpecifications = etTCRSoundLevelAsPerSpecifications.text.toString().toIntOrNull() ?: 0
        val TCRSoundLevelInDb = etTCRSoundLevelInDb.text.toString().toIntOrNull() ?: 0
        val TCRLcdDigitalProjector = etTCRLcdDigitalProjector.text.toString()
        val TCRChairForCandidatesInNo = etTCRChairForCandidatesInNo.text.toString()
        val TCRTrainerChair = etTCRTrainerChair.text.toString()
        val TCRTrainerTable = etTCRTrainerTable.text.toString()
        val TCRWritingBoard = etTCRWritingBoard.text.toString()
        val TCRLightsInNo = etTCRLightsInNo.text.toString()
        val TCRFansInNo = etTCRFansInNo.text.toString()
//        val TCRListofDomain = etTCRDomainLabPhotogragh.text.toString()
        val TCRDomainLabPhotogragh = etTCRDomainLabPhotogragh.text.toString()




        val request = TCRRequest(


//
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder=AppUtil.getsanctionOrderPreference(requireContext()),
            roomType = RoomType,
            roomLength = Length,
            roomWidth = Width,
            roomArea = Area,
            roofType=TCRTypeofRoofItLab.toString(),
            roofTypeAttachment=base64ProofPreviewTCRTypeofRoofItLab?: "",
            falseCeiling=TCRFalseCellingProvide.toString(),
            falseCeilingAttachment=base64ProofPreviewTCRFalseCellingProvide?: "",
            ceilingHeight=TCRHeightOfCelling.toString(),
            ceilingHeightAttachment=base64ProofPreviewTCRHeightOfCelling?: "",
            ventilationArea=TCRVentilationAreaInSqFt.toString(),
            ventilationAreaAttachment=base64ProofPreviewTCRVentilationAreaInSqFt?: "",
            soundLevelSpecifications=TCRSoundLevelAsPerSpecifications.toString(),
            soundLevel=TCRSoundLevelInDb.toString(),
            soundLevelAttachment=base64ProofPreviewTCRSoundLevelInDb?: "",
            centerSoungProofed=spinnerTCRwhether_all_the_academic.selectedItem.toString(),
            centerSoungProofedAttachment=base64ProofPreviewTCRwhether_all_the_academic?: "",
            roomInfoBoard=spinnerTCRAcademicRoomInformationBoard.selectedItem.toString(),
            roomInfoBoardAttachment=base64ProofPreviewTCRAcademicRoomInformationBoard?: "",
            internalSignage = spinnerTCRInternalSignage.selectedItem.toString(),
            internalSignageAttachment = base64ProofPreviewTCRInternalSignage?: "",
            audioCamera = spinnerTCRCctcCamerasWithAudioFacility.selectedItem.toString(),
            audioCameraAttachment = base64ProofPreviewTCRCctcCamerasWithAudioFacility?: "",
            digitalProjector =TCRLcdDigitalProjector.toString(),
            digitalProjectorAttachment = base64ProofPreviewTCRLcdDigitalProjector?: "",
            candidatesChair=TCRChairForCandidatesInNo.toString(),
            candidatesChairAttachment=base64ProofPreviewTCRChairForCandidatesInNo?: "",
            trainersChair=TCRTrainerChair.toString(),
            trainersChairAttachment=base64ProofPreviewTCRTrainerChair?: "",
            trainersTable=TCRTrainerTable.toString(),
            trainersTableAttachment=base64ProofPreviewTCRTrainerTable?: "",
            lightsAcademic=TCRLightsInNo.toString(),
            lightsAttachment=base64ProofPreviewTCRLightsInNo?: "",
            fansAcademic=TCRFansInNo.toString(),
            fansAttachment=base64ProofPreviewTCRFansInNo?: "",
            electricalPowerBackup=spinnerTCRPowerBackup.selectedItem.toString(),
            electricalPowerBackupAttachment=base64ProofPreviewTCRElectricaPowerBackUpForThRoom?: "",
            roomPhotograph=TCRDomainLabPhotogragh.toString(),
            roomPhotographAttachment=base64ProofPreviewTCRDomainLabPhotogragh?: "",
            airConditioningRoom=spinnerTCRDoes_the_room_has.selectedItem.toString(),
            airConditioningRoomAttachment=base64ProofPreviewTCRDoes_the_room_has?: "",
            writingBoard=TCRWritingBoard.toString(),
            writingBoardAttachment=base64ProofPreviewTCRWritingBoard?: ""



        )


        viewModel.SubmitTheoryClassRoomDataToServer(request, token)
    }

    private fun RecyClerViewUI(){




        Academicadapter = AcademicAreaAdapter(centersList)
        RoomNumber=AppUtil.gettopValuePreference(requireContext())


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = Academicadapter
        DesriptionAcademicNon()




        val request = AcademicNonAcademicArea(
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            imeiNo = AppUtil.getAndroidId(requireContext()),
            appVersion = BuildConfig.VERSION_NAME,
            tcId = centerId,
            sanctionOrder = AppUtil.getsanctionOrderPreference(requireContext()),
        )
        viewModel.DesriptionAcademicNonList(request, AppUtil.getSavedTokenPreference(requireContext()))

    }



    private fun DesriptionAcademicNon(){
        viewModel.AcademicNonAcademicResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {
                        // ✅ Get list from API and update adapter
                        val list = it.wrappedList ?: emptyList()
                        Academicadapter.updateData(list)

                        // ✅ Print largest roomNo in Logcat
                        if (list.isNotEmpty()) {
                            // Convert roomNo safely to Int for comparison
                            val largestItem = list.maxByOrNull { item ->
                                item.roomNo.toIntOrNull() ?: Int.MIN_VALUE
                            }

                            if (largestItem != null) {
                                LayoutLinear.visibility = View. GONE
                                RecyclerViewData.visibility = View.VISIBLE
                                btnSubmitAdddMore.visibility = View.VISIBLE
//                                    ValueData=largestItem.roomNo
//                                    getNextNumber(ValueData!!)
                                getNextNumber(largestItem.roomNo)

//                                    Log.d("AdapterData", "Largest RoomNo = ${largestItem.roomNo}")
                            } else {
                                getNextNumber("1")
                                LayoutLinear.visibility = View.VISIBLE

                                RecyclerViewData.visibility = View.GONE
                                btnSubmitAdddMore.visibility = View.GONE
                                Log.d("AdapterData", "No valid numeric RoomNo found")
                            }
                        } else {
                            Log.d("AdapterData", "List is empty — no RoomNo to check")
                        }
                    }

                    202 ->


                    {

//                        Toast.makeText(requireContext(), "No data available.", Toast.LENGTH_SHORT).show()
                        btnSubmitAdddMore.visibility = View.GONE
                        LayoutLinear.visibility = View.VISIBLE
                        RecyclerViewData.visibility = View.GONE

                    }




                    301 -> Toast.makeText(requireContext(), "Please upgrade your app.", Toast.LENGTH_SHORT).show()
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                LayoutLinear.visibility = View.VISIBLE
                RecyclerViewData.visibility = View.GONE
                btnSubmitAdddMore.visibility = View.GONE

            }
        }
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private val usedNumbers = mutableSetOf<Int>()

    fun getNextNumber(valueData: String): Int {
        // Convert current value to Int safely
        var num = valueData.toIntOrNull() ?: 1

        // Find the next free number
        while (usedNumbers.contains(num) && num < 1000) {
            num++
        }

        if (num > 1000) {
            etroomType.setText("All numbers used")
            throw Exception("No available numbers below 1000")
        }

        // Mark this number as used
        usedNumbers.add(num)

        // Prepare next number
        val nextValue = (num + 1).toString()

        // ✅ Update EditText with next number
        etroomType.setText(nextValue)
        etroomType.isFocusable = false
        etroomType.isClickable = false

        Log.d("NextNumber", "Current Number Used = $num, Next Available = $nextValue")

        return num
    }

    private fun calculateAndShowArea() {

        val lengthStr = etLength.text.toString().trim()
        val widthStr = etWidth.text.toString().trim()

        val length = lengthStr.toDoubleOrNull() ?: 0.0
        val width = widthStr.toDoubleOrNull() ?: 0.0

        val area = length * width
        tvArea.text = "$area"
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }
}
