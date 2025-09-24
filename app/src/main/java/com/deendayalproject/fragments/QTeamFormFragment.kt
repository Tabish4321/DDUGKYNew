package com.deendayalproject.fragments

import SharedViewModel
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.adapter.DescriptionAcademiaAdapter
import com.deendayalproject.adapter.TrainerStaffAdapter
import com.deendayalproject.databinding.FragmentQTeamFormBinding
import com.deendayalproject.model.request.TrainingCenterInfo
import com.deendayalproject.model.response.Trainer
import com.deendayalproject.util.AppUtil
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.deendayalproject.model.response.RoomItem
import com.deendayalproject.util.AppUtil.hasStoragePermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.material.imageview.ShapeableImageView
import java.io.File

class QTeamFormFragment : Fragment() {

    private var _binding: FragmentQTeamFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    var dataStaffList: MutableList<Trainer> = mutableListOf()
    var academiaList: MutableList<RoomItem> = mutableListOf()


    private val approvalList = listOf("Approved", "Send for modification")
    private lateinit var tcInfoAdapter: ArrayAdapter<String>
    private var selectedTcInfoApproval = ""
    private var selectedTcInfoRemarks = ""

    private lateinit var tcDescAcademiaAdapter: ArrayAdapter<String>
    private var selectedTcDescAcademiaApproval = ""
    private var selectedTcDescAcademiaRemarks = ""


    private lateinit var tcInfraAdapter: ArrayAdapter<String>
    private var selectedTcInfraApproval = ""
    private var selectedTcInfraRemarks = ""

    private lateinit var tcBasinAdapter: ArrayAdapter<String>
    private var selectedTcBasinApproval = ""
    private var selectedTcBasinRemarks = ""


    private lateinit var tcDescOtherAreaAdapter: ArrayAdapter<String>
    private var selectedTcDescOtherAreaApproval = ""
    private var selectedTcDescOtherAreaRemarks = ""


    private lateinit var tcTeachingAdapter: ArrayAdapter<String>
    private var selectedTcTeachingApproval = ""
    private var selectedTcTeachingRemarks = ""


    private lateinit var tcGeneralAdapter: ArrayAdapter<String>
    private var selectedTcGeneralApproval = ""
    private var selectedTcGeneralRemarks = ""


    private lateinit var tcElectricalAdapter: ArrayAdapter<String>
    private var selectedTcElectricalApproval = ""
    private var selectedTcElectricalRemarks = ""


    private lateinit var tcSignageAdapter: ArrayAdapter<String>
    private var selectedTcSignageApproval = ""
    private var selectedTcSignageRemarks = ""


    private lateinit var tcIpEnableAdapter: ArrayAdapter<String>
    private var selectedTcIpEnableApproval = ""
    private var selectedTcIpEnableRemarks = ""


    private lateinit var tcCommonEquipmentAdapter: ArrayAdapter<String>
    private var selectedTcCommonEquipmentApproval = ""
    private var selectedTcCommonEquipmentRemarks = ""


    private lateinit var tcAvailSupportInfraAdapter: ArrayAdapter<String>
    private var selectedTcAvailSupportInfraApproval = ""
    private var selectedTcAvailSupportInfraRemarks = ""


    private lateinit var tcAvailOfStandardFormAdapter: ArrayAdapter<String>
    private var selectedTcAvailOfStandardFormApproval = ""
    private var selectedTcAvailOfStandardFormRemarks = ""


    private var centerId = ""
    private var centerName = ""
    private var sanctionOrder = ""


    private var selfDeclarationPdf = ""
    private var buildingPdf = ""
    private var schematicPdf = ""
    private var internalExternalWallPdf = ""


    private var maleToiletImage = ""
    private var maleToiletSignageImage = ""
    private var maleToiletUrinalsImage = ""
    private var maleToiletWashbasinImage = ""


    private var femaleToiletImage = ""
    private var femaleToiletSignageImage = ""
    private var femaleToiletWashbasinImage = ""
    private var ovrHeadTankImage = ""
    private var typeOfFlooringImage = ""


    private var fansImage = ""
    private var circulationAreaImage = ""
    private var openSpaceImage = ""
    private var parkingSpaceImage = ""
    private var welcomeKitImage = ""
    private var signOfLeakageImage = ""
    private var protectionStairsBalImage = ""
    private var securingWiringImage = ""
    private var switchBoardImage = ""


    private var tcNameBoardImage = ""
    private var activitySummaryBoardImage = ""
    private var studentEntitlementBoardImage = ""
    private var contactDetailImpoPeopleImage = ""
    private var basicInfoBoardImage = ""
    private var codeOfConductImage = ""
    private var studentAttendanceImage = ""
    private var centralMonitorImage = ""
    private var conformationOfCCTVImage = ""
    private var storageOfCCtvImage = ""
    private var dvrImage = ""


    private var electricPowerImage = ""
    private var installBiometricImage = ""
    private var installationCCTVImage = ""
    private var storagePlaceSecuringDocImage = ""
    private var printerCumImage = ""
    private var digitalCameraImage = ""
    private var grievanceImage = ""
    private var minimumEquipmentImage = ""
    private var directionBoardsImage = ""
    private var safeDrinkingImage = ""
    private var fireFightingImage = ""
    private var firstAidImage = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQTeamFormBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        init()

        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()

        // TrainingCenterInfo API
        val requestTcInfo = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getTrainerCenterInfo(requestTcInfo)


        collectTCInfoResponse()


        // TrainingCenterStaffList API
        val requestStaffList = TrainingCenterInfo(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            tcId = centerId.toInt(),
            imeiNo = AppUtil.getAndroidId(requireContext())
        )
        viewModel.getTcStaffDetails(requestStaffList)

        collectTCStaffResponse()

    }

    private fun init() {

        collectTCElectrical()
        collectTCGeneral()
        collectTCTeaching()
        collectTCDescOtherArea()
        collectTCToiletAndWash()
        collectTCAcademiaNonAcademia()
        collectTCInfraResponse()
        collectTCSignage()
        collectTCIpEnabele()
        collectTCCommonEquipment()



        listener()



    }

    private fun listener() {



        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = DescriptionAcademiaAdapter(academiaList) { room ->

            when (room.roomType) {
                "Training Room" -> {
                    showCustomDialog(R.layout.office_room_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val officeRoomPhoto = view.findViewById<TextView>(R.id.valueOfficeRoomPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        officeRoomPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Office Room Photo clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Rest Room" -> {
                    showCustomDialog(R.layout.it_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                      //  val labMachines = view.findViewById<TextView>(R.id.)

                        backButton.setOnClickListener { dialog.dismiss() }
                      /*  labMachines.setOnClickListener {
                            Toast.makeText(requireContext(), "Lab Machines clicked!", Toast.LENGTH_SHORT).show()
                        }*/
                    }
                }

                "Living Room" -> {
                    showCustomDialog(R.layout.office_cum_counceling_room_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                      //  val booksCount = view.findViewById<TextView>(R.id.valueBooksCount)

                        backButton.setOnClickListener { dialog.dismiss() }
                        /*booksCount.setOnClickListener {
                            Toast.makeText(requireContext(), "Books Count clicked!", Toast.LENGTH_SHORT).show()
                        }*/
                    }
                }
            }

/*
            when (room.roomType) {

                "Theory Class Room" -> {
                    showCustomDialog(R.layout.theory_class_room_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val classPhoto = view.findViewById<TextView>(R.id.valueClassPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        classPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Theory Class Room clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Office Cum Counselling" -> {
                    showCustomDialog(R.layout.office_counselling_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val officePhoto = view.findViewById<TextView>(R.id.valueOfficePhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        officePhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Office Cum Counselling clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Reception Area" -> {
                    showCustomDialog(R.layout.reception_area_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val receptionPhoto = view.findViewById<TextView>(R.id.valueReceptionPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        receptionPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Reception Area clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Counselling Room" -> {
                    showCustomDialog(R.layout.counselling_room_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val counsellingPhoto = view.findViewById<TextView>(R.id.valueCounsellingPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        counsellingPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Counselling Room clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Office Room" -> {
                    showCustomDialog(R.layout.office_room_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val officeRoomPhoto = view.findViewById<TextView>(R.id.valueOfficeRoomPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        officeRoomPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Office Room clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "IT cum Domain Lab" -> {
                    showCustomDialog(R.layout.it_cum_domain_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val itDomainLabPhoto = view.findViewById<TextView>(R.id.valueItDomainLabPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        itDomainLabPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "IT cum Domain Lab clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Theory Cum IT Lab" -> {
                    showCustomDialog(R.layout.theory_cum_it_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val theoryItPhoto = view.findViewById<TextView>(R.id.valueTheoryItPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        theoryItPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Theory Cum IT Lab clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Theory Cum Domain Lab" -> {
                    showCustomDialog(R.layout.theory_cum_domain_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val theoryDomainPhoto = view.findViewById<TextView>(R.id.valueTheoryDomainPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        theoryDomainPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Theory Cum Domain Lab clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "IT Lab" -> {
                    showCustomDialog(R.layout.it_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val itLabPhoto = view.findViewById<TextView>(R.id.valueItLabPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        itLabPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "IT Lab clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                "Domain Lab" -> {
                    showCustomDialog(R.layout.domain_lab_layout) { view, dialog ->
                        val backButton = view.findViewById<ShapeableImageView>(R.id.backButton)
                        val domainLabPhoto = view.findViewById<TextView>(R.id.valueDomainLabPhoto)

                        backButton.setOnClickListener { dialog.dismiss() }
                        domainLabPhoto.setOnClickListener {
                            Toast.makeText(requireContext(), "Domain Lab clicked!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                else -> {
                    Toast.makeText(requireContext(), "No layout found for ${room.roomType}", Toast.LENGTH_SHORT).show()
                }
            }
*/
        }


        // All Adapter

        //Adapter Information
        tcInfoAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.trainingCenterInfoLayout.SpinnerTcInfo.setAdapter(tcInfoAdapter)

        binding.trainingCenterInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedTcInfoApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcInfoApproval == "Send for modification") {

                binding.trainingCenterInfoLayout.InfoRemarks.visibility = View.VISIBLE
                binding.trainingCenterInfoLayout.etInfoRemarks.visibility = View.VISIBLE

            } else {

                binding.trainingCenterInfoLayout.InfoRemarks.visibility = View.GONE
                binding.trainingCenterInfoLayout.etInfoRemarks.visibility = View.GONE
            }

        }


        //Adapter Description Academia
        tcDescAcademiaAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerDescAcademia.setAdapter(tcDescAcademiaAdapter)

        binding.SpinnerDescAcademia.setOnItemClickListener { parent, view, position, id ->
            selectedTcDescAcademiaApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcDescAcademiaApproval == "Send for modification") {

                binding.DescAcademiaRemarks.visibility = View.VISIBLE
                binding.etDescAcademiaRemarks.visibility = View.VISIBLE

            } else {

                binding.DescAcademiaRemarks.visibility = View.GONE
                binding.etDescAcademiaRemarks.visibility = View.GONE
            }

        }

        //Adapter Infrastructure
        tcInfraAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerTcInfra.setAdapter(tcInfraAdapter)


        binding.SpinnerTcInfra.setOnItemClickListener { parent, view, position, id ->
            selectedTcInfraApproval = parent.getItemAtPosition(position).toString()

            if (selectedTcInfraApproval == "Send for modification") {

                binding.InfraRemarks.visibility = View.VISIBLE
                binding.etInfraRemarks.visibility = View.VISIBLE

            } else {

                binding.InfraRemarks.visibility = View.GONE
                binding.etInfraRemarks.visibility = View.GONE
            }

        }

        //Adapter Basin
        tcBasinAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerBasin.setAdapter(tcBasinAdapter)

        binding.SpinnerBasin.setOnItemClickListener { parent, view, position, id ->
            selectedTcBasinApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcBasinApproval == "Send for modification") {

                binding.BasinRemarks.visibility = View.VISIBLE
                binding.etBasinRemarks.visibility = View.VISIBLE

            } else {

                binding.BasinRemarks.visibility = View.GONE
                binding.etBasinRemarks.visibility = View.GONE
            }

        }


        //Adapter DescOtherArea
        tcDescOtherAreaAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerDescOtherArea.setAdapter(tcDescOtherAreaAdapter)

        binding.SpinnerDescOtherArea.setOnItemClickListener { parent, view, position, id ->
            selectedTcDescOtherAreaApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcDescOtherAreaApproval == "Send for modification") {

                binding.DescOtherAreaRemarks.visibility = View.VISIBLE
                binding.etDescOtherAreaRemarks.visibility = View.VISIBLE

            } else {

                binding.DescOtherAreaRemarks.visibility = View.GONE
                binding.etDescOtherAreaRemarks.visibility = View.GONE
            }

        }


        //Adapter Teaching
        tcTeachingAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerTeaching.setAdapter(tcTeachingAdapter)

        binding.SpinnerTeaching.setOnItemClickListener { parent, view, position, id ->
            selectedTcTeachingApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcTeachingApproval == "Send for modification") {

                binding.TeachingRemarks.visibility = View.VISIBLE
                binding.etTeachingRemarks.visibility = View.VISIBLE

            } else {

                binding.TeachingRemarks.visibility = View.GONE
                binding.etTeachingRemarks.visibility = View.GONE
            }

        }


        //Adapter General
        tcGeneralAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerGeneral.setAdapter(tcGeneralAdapter)

        binding.SpinnerGeneral.setOnItemClickListener { parent, view, position, id ->
            selectedTcGeneralApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcGeneralApproval == "Send for modification") {

                binding.GeneralRemarks.visibility = View.VISIBLE
                binding.etGeneralRemarks.visibility = View.VISIBLE

            } else {

                binding.GeneralRemarks.visibility = View.GONE
                binding.etGeneralRemarks.visibility = View.GONE
            }

        }


        //Adapter Electrical
        tcElectricalAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerElectrical.setAdapter(tcElectricalAdapter)

        binding.SpinnerElectrical.setOnItemClickListener { parent, view, position, id ->
            selectedTcElectricalApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcElectricalApproval == "Send for modification") {

                binding.ElectricalRemarks.visibility = View.VISIBLE
                binding.etElectricalRemarks.visibility = View.VISIBLE

            } else {

                binding.ElectricalRemarks.visibility = View.GONE
                binding.etElectricalRemarks.visibility = View.GONE
            }

        }


        //Adapter Signage
        tcSignageAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.signageLayout.SpinnerSignage.setAdapter(tcSignageAdapter)

        binding.signageLayout.SpinnerSignage.setOnItemClickListener { parent, view, position, id ->
            selectedTcSignageApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcSignageApproval == "Send for modification") {

                binding.signageLayout.SignageRemarks.visibility = View.VISIBLE
                binding.signageLayout.etSignageRemarks.visibility = View.VISIBLE

            } else {

                binding.signageLayout.SignageRemarks.visibility = View.GONE
                binding.signageLayout.etSignageRemarks.visibility = View.GONE
            }

        }


        //Adapter IpEnable
        tcIpEnableAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.ipCameraLayout.SpinnerIpEnable.setAdapter(tcIpEnableAdapter)

        binding.ipCameraLayout.SpinnerIpEnable.setOnItemClickListener { parent, view, position, id ->
            selectedTcIpEnableApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcIpEnableApproval == "Send for modification") {

                binding.ipCameraLayout.IpEnableRemarks.visibility = View.VISIBLE
                binding.ipCameraLayout.etIpEnableRemarks.visibility = View.VISIBLE

            } else {

                binding.ipCameraLayout.IpEnableRemarks.visibility = View.GONE
                binding.ipCameraLayout.etIpEnableRemarks.visibility = View.GONE
            }

        }


        //Adapter Common Equipment
        tcCommonEquipmentAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.commonEquipmentLayout.SpinnerCommonEquipment.setAdapter(tcCommonEquipmentAdapter)

        binding.commonEquipmentLayout.SpinnerCommonEquipment.setOnItemClickListener { parent, view, position, id ->
            selectedTcCommonEquipmentApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcCommonEquipmentApproval == "Send for modification") {

                binding.commonEquipmentLayout.CommonEquipmentRemarks.visibility = View.VISIBLE
                binding.commonEquipmentLayout.etCommonEquipmentRemarks.visibility = View.VISIBLE

            } else {

                binding.commonEquipmentLayout.CommonEquipmentRemarks.visibility = View.GONE
                binding.commonEquipmentLayout.etCommonEquipmentRemarks.visibility = View.GONE
            }

        }


        //Adapter Avail Support Infra Adapter
        tcAvailSupportInfraAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setAdapter(
            tcAvailSupportInfraAdapter
        )

        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setOnItemClickListener { parent, view, position, id ->
            selectedTcAvailSupportInfraApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcAvailSupportInfraApproval == "Send for modification") {

                binding.availSupportInfraLayout.AvailSupportInfraRemarks.visibility = View.VISIBLE
                binding.availSupportInfraLayout.etAvailSupportInfraRemarks.visibility = View.VISIBLE

            } else {

                binding.availSupportInfraLayout.AvailSupportInfraRemarks.visibility = View.GONE
                binding.availSupportInfraLayout.etAvailSupportInfraRemarks.visibility = View.GONE
            }

        }


        // AvailOfStandardFormAdapter
        tcAvailOfStandardFormAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setAdapter(
            tcAvailOfStandardFormAdapter
        )

        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setOnItemClickListener { parent, view, position, id ->
            selectedTcAvailOfStandardFormApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcAvailOfStandardFormApproval == "Send for modification") {

                binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks.visibility =
                    View.VISIBLE
                binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.visibility =
                    View.VISIBLE

            } else {


                binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks.visibility =
                    View.GONE
                binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.visibility =
                    View.GONE
            }

        }

        // All Buttons

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }



        // Wash basin image set
        binding.valueMaleToilet.setOnClickListener {

            showBase64ImageDialog(requireContext(), maleToiletImage, "maleToilet Image")

        }

        binding.valueProofMaleSignageToilet.setOnClickListener {

            showBase64ImageDialog(requireContext(), maleToiletSignageImage, "male Toilet Signage Image ")

        }

        binding.valueMaleUrinals.setOnClickListener {

            showBase64ImageDialog(requireContext(), maleToiletUrinalsImage, "male Toilet Urinals Image ")

        }


        binding.valueMaleWashBasin.setOnClickListener {

            showBase64ImageDialog(requireContext(), maleToiletWashbasinImage, "male Toilet Urinals Image ")

        }


        binding.valueFemaleToilet.setOnClickListener {

            showBase64ImageDialog(requireContext(), femaleToiletImage, "female Toilet Image ")

        }


        binding.valueProofFemaleSignageToilet.setOnClickListener {

            showBase64ImageDialog(requireContext(), femaleToiletSignageImage, "female Toilet Signage Image")

        }


        binding.valueFemaleWashBasin.setOnClickListener {

            showBase64ImageDialog(requireContext(), femaleToiletWashbasinImage, "female Toilet Washbasin Image")

        }


        binding.valueOverheadTank.setOnClickListener {

            showBase64ImageDialog(requireContext(), ovrHeadTankImage, "over Head Tank Image")

        }

        binding.valueTypeOfFlooring.setOnClickListener {

            showBase64ImageDialog(requireContext(), typeOfFlooringImage, "type Of Flooring Image")

        }


        // desc area image set


        binding.valueFans.setOnClickListener {

            showBase64ImageDialog(requireContext(), fansImage, "fan Image")

        }


        binding.valueCirculationArea.setOnClickListener {

            showBase64ImageDialog(requireContext(), circulationAreaImage, "circulation Area Image")

        }


        binding.valueOpenSpace.setOnClickListener {

            showBase64ImageDialog(requireContext(), openSpaceImage, "open Space Image")

        }



        binding.valueParking.setOnClickListener {

            showBase64ImageDialog(requireContext(), parkingSpaceImage, "parking Space Image")

        }


        // Infra pdf set
        binding.tvSelfDeclarationPdf.setOnClickListener {

            openBase64Pdf(requireContext(), selfDeclarationPdf)

        }


        binding.tvPhotosOfBuildingPdf.setOnClickListener {
            openBase64Pdf(requireContext(), buildingPdf)

        }


        binding.tvSchematicBuildingPlanPdf.setOnClickListener {
            openBase64Pdf(requireContext(), schematicPdf)

        }


        binding.tvInternalExternalWallsPdf.setOnClickListener {
            openBase64Pdf(requireContext(), internalExternalWallPdf)

        }



        //Availiblity Teaching  image set


        binding.valueIsWelcomeKitAvail.setOnClickListener {

            showBase64ImageDialog(requireContext(), welcomeKitImage, "welcome Kit Image")

        }


        //General Details image set

        binding.valueSignOfLiakage.setOnClickListener {

            showBase64ImageDialog(requireContext(), signOfLeakageImage, "sign Of Leakage Image")

        }

        binding.valueProtectionOfStairs.setOnClickListener {

            showBase64ImageDialog(requireContext(), protectionStairsBalImage, "protection Stairs Balcony Image")

        }


        //Electrical wiring
        binding.valueSecuringWire.setOnClickListener {

            showBase64ImageDialog(requireContext(), securingWiringImage, "securing Wiring Image")

        }

        binding.valueSwitchBoard.setOnClickListener {

            showBase64ImageDialog(requireContext(), switchBoardImage, "switch Board Image")

        }

        //signage's and info boards

        binding.signageLayout.valueCenterNameBoard.setOnClickListener {

            showBase64ImageDialog(requireContext(), tcNameBoardImage, "Training Center Name Board")

        }


        binding.signageLayout.valueSummaryAcheivement.setOnClickListener {

            showBase64ImageDialog(requireContext(), activitySummaryBoardImage, "Activity Summary Achievement")

        }


        binding.signageLayout.valueStudentEntitlement.setOnClickListener {

            showBase64ImageDialog(requireContext(), studentEntitlementBoardImage, "student Entitlement Board Image")

        }


        binding.signageLayout.valueContactDetail.setOnClickListener {

            showBase64ImageDialog(requireContext(), contactDetailImpoPeopleImage, "contact Detail Important People Image")

        }

        binding.signageLayout.valueBasicInfoBoard.setOnClickListener {

            showBase64ImageDialog(requireContext(), basicInfoBoardImage, "basic Info Board Image")

        }

        binding.signageLayout.valueCodeOfConduct.setOnClickListener {

            showBase64ImageDialog(requireContext(), codeOfConductImage, "code of conduct")

        }

        binding.signageLayout.valueAttendanceSummary.setOnClickListener {

            showBase64ImageDialog(requireContext(), studentAttendanceImage, "student Attendance Image")

        }

        // Ip Enable

        binding.ipCameraLayout.valueCentralMonitor.setOnClickListener {

            showBase64ImageDialog(requireContext(), centralMonitorImage, "central Monitor Image")

        }


        binding.ipCameraLayout.valueConformanceCCTV.setOnClickListener {

            showBase64ImageDialog(requireContext(), conformationOfCCTVImage, "conformation Of CCTV Image")

        }

        binding.ipCameraLayout.valueStorageCCTV.setOnClickListener {

            showBase64ImageDialog(requireContext(), storageOfCCtvImage, "storage Of CCtv Image")

        }


        binding.ipCameraLayout.valueDvrStaticIP.setOnClickListener {

            showBase64ImageDialog(requireContext(), dvrImage, "DVR is Connected")

        }


            // common equipment

        binding.commonEquipmentLayout.valueElectricalPowerBackup.setOnClickListener {

            showBase64ImageDialog(requireContext(), electricPowerImage, "electric Power Image")

        }

        binding.commonEquipmentLayout.valueBiometricDevices.setOnClickListener {

            showBase64ImageDialog(requireContext(), installBiometricImage, "install Biometric Image")

        }


        binding.commonEquipmentLayout.valueCCTVMonitor.setOnClickListener {

            showBase64ImageDialog(requireContext(), installationCCTVImage, "installation CCTV Image")

        }

        binding.commonEquipmentLayout.valueStorageDocs.setOnClickListener {

            showBase64ImageDialog(requireContext(), storagePlaceSecuringDocImage, "storage Place Securing Doc Image")

        }

        binding.commonEquipmentLayout.valuePrinterScanner.setOnClickListener {

            showBase64ImageDialog(requireContext(), printerCumImage, "printer Cum Image")

        }

        binding.commonEquipmentLayout.valueDigitalCamera.setOnClickListener {

            showBase64ImageDialog(requireContext(), digitalCameraImage, "digital Camera Image")

        }


        binding.commonEquipmentLayout.valueGrievanceRegister.setOnClickListener {

            showBase64ImageDialog(requireContext(), grievanceImage, "grievance Image")

        }

        binding.commonEquipmentLayout.valueMinEquipment.setOnClickListener {

            showBase64ImageDialog(requireContext(), minimumEquipmentImage, "minimum Equipment Image")

        }


        binding.commonEquipmentLayout.valueDirectionBoards.setOnClickListener {

            showBase64ImageDialog(requireContext(), directionBoardsImage, "direction Boards Image")

        }

        //Availability of support infra


        binding.availSupportInfraLayout.valueSafeDrinkingWater.setOnClickListener {

            showBase64ImageDialog(requireContext(), safeDrinkingImage, "safe Drinking Image")

        }

        binding.availSupportInfraLayout.valueFireFighting.setOnClickListener {

            showBase64ImageDialog(requireContext(), fireFightingImage, "fire Fighting Image")

        }


        binding.availSupportInfraLayout.valueFirstAidKit.setOnClickListener {

            showBase64ImageDialog(requireContext(), firstAidImage, "first Aid Image")

        }





        binding.trainingCenterInfoLayout.tvViewTrainerAndStaff.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_trainer_staff)

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvTrainerStaff)
            val closeButton = dialog.findViewById<TextView>(R.id.tvClose)

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = TrainerStaffAdapter(dataStaffList)

            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }


        binding.trainingCenterInfoLayout.btnInfoNext.setOnClickListener {
            if (selectedTcInfoApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcInfoApproval == "Send for modification") {
                selectedTcInfoRemarks =
                    binding.trainingCenterInfoLayout.etInfoRemarks.text.toString()

                if (selectedTcInfoRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcInfoRemarks = ""

            // Common UI updates

            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getTrainerCenterInfra(requestTcInfraReq)



            binding.trainingCenterInfoLayout.trainingInfoExpand.visibility = View.GONE
            binding.trainingCenterInfoLayout.viewInfo.visibility = View.GONE
            binding.trainingCenterInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainInfra.visibility = View.VISIBLE
            binding.viewInfra.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }


        binding.btnInfraNext.setOnClickListener {
            if (selectedTcInfraApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcInfraApproval == "Send for modification") {
                selectedTcInfraRemarks = binding.etInfraRemarks.text.toString()
                if (selectedTcInfraRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcInfraRemarks = ""



            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getTcAcademicNonAcademicArea(requestTcInfraReq)



            // Common UI updates
            binding.trainingInfraExpand.visibility = View.GONE
            binding.viewInfra.visibility = View.GONE
            binding.tvTrainInfra.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainDescAcademia.visibility = View.VISIBLE
            binding.viewDescAcademia.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnInfraPrevious.setOnClickListener {

            binding.trainingCenterInfoLayout.trainingInfoExpand.visibility = View.VISIBLE
            binding.trainingCenterInfoLayout.viewInfo.visibility = View.VISIBLE
            binding.mainInfra.visibility = View.GONE
            binding.viewInfra.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }


        binding.btnDescAcademiaNext.setOnClickListener {
            if (selectedTcDescAcademiaApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcDescAcademiaApproval == "Send for modification") {
                selectedTcDescAcademiaRemarks = binding.etDescAcademiaRemarks.text.toString()
                if (selectedTcDescAcademiaRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcDescAcademiaRemarks = ""


            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getTcToiletWashBasin(requestTcInfraReq)



            // Common UI updates
            binding.trainingDescAcademiaExpand.visibility = View.GONE
            binding.viewDescAcademia.visibility = View.GONE
            binding.tvTrainDescAcademia.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainToilet.visibility = View.VISIBLE
            binding.viewToilet.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnDescAcademiaPrevious.setOnClickListener {

            binding.trainingInfraExpand.visibility = View.VISIBLE
            binding.viewInfra.visibility = View.VISIBLE
            binding.mainDescAcademia.visibility = View.GONE
            binding.viewDescAcademia.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnBasinNext.setOnClickListener {
            if (selectedTcBasinApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcBasinApproval == "Send for modification") {
                selectedTcBasinRemarks = binding.etBasinRemarks.text.toString()
                if (selectedTcBasinRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcBasinRemarks = ""
            // Common UI updates




            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getDescriptionOtherArea(requestTcInfraReq)




            binding.trainingToiletExpand.visibility = View.GONE
            binding.viewToilet.visibility = View.GONE
            binding.tvTrainToilet.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainDescOfOtherArea.visibility = View.VISIBLE
            binding.viewDescOfOtherArea.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnBasinPrevious.setOnClickListener {

            binding.trainingDescAcademiaExpand.visibility = View.VISIBLE
            binding.viewDescAcademia.visibility = View.VISIBLE
            binding.mainToilet.visibility = View.GONE
            binding.viewToilet.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnDescOtherAreaNext.setOnClickListener {
            if (selectedTcDescOtherAreaApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcDescOtherAreaApproval == "Send for modification") {
                selectedTcDescOtherAreaRemarks = binding.etDescOtherAreaRemarks.text.toString()
                if (selectedTcDescOtherAreaRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcDescOtherAreaRemarks = ""




            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getTeachingLearningMaterial(requestTcInfraReq)

            // Common UI updates
            binding.trainingDescOfOtherAreaExpand.visibility = View.GONE
            binding.viewDescOfOtherArea.visibility = View.GONE
            binding.tvTrainDescOfOtherArea.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainTeaching.visibility = View.VISIBLE
            binding.viewTeaching.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnDescOtherAreaPrevious.setOnClickListener {

            binding.trainingToiletExpand.visibility = View.VISIBLE
            binding.viewToilet.visibility = View.VISIBLE
            binding.mainDescOfOtherArea.visibility = View.GONE
            binding.viewDescOfOtherArea.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnTeachingNext.setOnClickListener {
            if (selectedTcTeachingApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcTeachingApproval == "Send for modification") {
                selectedTcTeachingRemarks = binding.etTeachingRemarks.text.toString()
                if (selectedTcTeachingRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcTeachingRemarks = ""
            // Common UI updates


            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getGeneralDetails(requestTcInfraReq)




            binding.trainingTeachingExpand.visibility = View.GONE
            binding.viewTeaching.visibility = View.GONE
            binding.tvTrainTeaching.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainGeneralDetails.visibility = View.VISIBLE
            binding.viewGeneralDetails.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnTeachingPrevious.setOnClickListener {

            binding.trainingDescOfOtherAreaExpand.visibility = View.VISIBLE
            binding.viewDescOfOtherArea.visibility = View.VISIBLE
            binding.mainTeaching.visibility = View.GONE
            binding.viewTeaching.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnGeneralNext.setOnClickListener {
            if (selectedTcGeneralApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcGeneralApproval == "Send for modification") {
                selectedTcGeneralRemarks = binding.etGeneralRemarks.text.toString()
                if (selectedTcGeneralRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcGeneralRemarks = ""



            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getElectricalWiringStandard(requestTcInfraReq)



            // Common UI updates
            binding.trainingGeneralDetailsExpand.visibility = View.GONE
            binding.viewGeneralDetails.visibility = View.GONE
            binding.tvTrainGeneralDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainElectricalDetails.visibility = View.VISIBLE
            binding.viewElectricalDetails.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnGeneralPrevious.setOnClickListener {

            binding.trainingTeachingExpand.visibility = View.VISIBLE
            binding.viewTeaching.visibility = View.VISIBLE
            binding.mainGeneralDetails.visibility = View.GONE
            binding.viewGeneralDetails.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnElectricalNext.setOnClickListener {
            if (selectedTcElectricalApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcElectricalApproval == "Send for modification") {
                selectedTcElectricalRemarks = binding.etElectricalRemarks.text.toString()
                if (selectedTcElectricalRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcElectricalRemarks = ""

            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getSignagesAndInfoBoard(requestTcInfraReq)


            // Common UI updates
            binding.trainingElectricalDetailsExpand.visibility = View.GONE
            binding.viewElectricalDetails.visibility = View.GONE
            binding.tvTrainElectricalDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainSignageBoardDetails.visibility = View.VISIBLE
            binding.signageLayout.viewSignageBoardDetails.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.btnElectricalPrevious.setOnClickListener {

            binding.trainingGeneralDetailsExpand.visibility = View.VISIBLE
            binding.viewGeneralDetails.visibility = View.VISIBLE
            binding.mainElectricalDetails.visibility = View.GONE
            binding.viewElectricalDetails.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.signageLayout.btnSignageNext.setOnClickListener {
            if (selectedTcSignageApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcSignageApproval == "Send for modification") {
                selectedTcSignageRemarks = binding.signageLayout.etSignageRemarks.text.toString()
                if (selectedTcSignageRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcSignageRemarks = ""


            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getIpEnabledCamera(requestTcInfraReq)


            // Common UI updates
            binding.signageLayout.trainingSignageBoardlDetailsExpand.visibility = View.GONE
            binding.signageLayout.viewSignageBoardDetails.visibility = View.GONE
            binding.signageLayout.tvTrainSignageBoardDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainIPEnableCameraDetails.visibility = View.VISIBLE
            binding.ipCameraLayout.viewIPEnableCameraDetails.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.signageLayout.btnSignagePrevious.setOnClickListener {

            binding.trainingElectricalDetailsExpand.visibility = View.VISIBLE
            binding.viewElectricalDetails.visibility = View.VISIBLE
            binding.mainSignageBoardDetails.visibility = View.GONE
            binding.signageLayout.viewSignageBoardDetails.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.ipCameraLayout.btnIpEnableNext.setOnClickListener {
            if (selectedTcIpEnableApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcIpEnableApproval == "Send for modification") {
                selectedTcIpEnableRemarks = binding.ipCameraLayout.etIpEnableRemarks.text.toString()
                if (selectedTcIpEnableRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcIpEnableRemarks = ""



            val requestTcInfraReq = TrainingCenterInfo(
                appVersion = BuildConfig.VERSION_NAME,
                loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
                tcId = centerId.toInt(),
                imeiNo = AppUtil.getAndroidId(requireContext())
            )
            viewModel.getCommonEquipment(requestTcInfraReq)


            // Common UI updates
            binding.ipCameraLayout.viewIPEnableCameraDetails.visibility = View.GONE
            binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand.visibility = View.GONE

            binding.ipCameraLayout.tvTrainIPEnableCameraDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )
            binding.mainCommonEquipmentDetails.visibility = View.VISIBLE
            binding.commonEquipmentLayout.viewCommonEquipmentDetails.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.ipCameraLayout.btnIpEnablePrevious.setOnClickListener {

            binding.signageLayout.trainingSignageBoardlDetailsExpand.visibility = View.VISIBLE
            binding.signageLayout.viewSignageBoardDetails.visibility = View.VISIBLE
            binding.mainIPEnableCameraDetails.visibility = View.GONE
            binding.ipCameraLayout.viewIPEnableCameraDetails.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.commonEquipmentLayout.btnCommonEquipmentNext.setOnClickListener {
            if (selectedTcCommonEquipmentApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcCommonEquipmentApproval == "Send for modification") {
                selectedTcCommonEquipmentRemarks =
                    binding.commonEquipmentLayout.etCommonEquipmentRemarks.text.toString()
                if (selectedTcCommonEquipmentRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcCommonEquipmentRemarks = ""
            // Common UI updates
            binding.commonEquipmentLayout.viewCommonEquipmentDetails.visibility = View.GONE
            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand.visibility =
                View.GONE

            binding.commonEquipmentLayout.tvTrainCommonEquipmentDetails.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )


            binding.mainAvailSupportInfra.visibility = View.VISIBLE
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.commonEquipmentLayout.btnCommonEquipmentPrevious.setOnClickListener {

            binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand.visibility = View.VISIBLE
            binding.ipCameraLayout.viewIPEnableCameraDetails.visibility = View.VISIBLE

            binding.mainCommonEquipmentDetails.visibility = View.GONE
            binding.commonEquipmentLayout.viewCommonEquipmentDetails.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.availSupportInfraLayout.btnAvailSupportInfraNext.setOnClickListener {
            if (selectedTcAvailSupportInfraApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener

            }

            if (selectedTcAvailSupportInfraApproval == "Send for modification") {
                selectedTcAvailSupportInfraRemarks =
                    binding.availSupportInfraLayout.etAvailSupportInfraRemarks.text.toString()
                if (selectedTcAvailSupportInfraRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else selectedTcAvailSupportInfraRemarks = ""
            // Common UI updates
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.GONE
            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand.visibility = View.GONE
            binding.availSupportInfraLayout.tvTrainAvailSupportInfra.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_verified,
                0
            )


            binding.mainAvailOfStandardForms.visibility = View.VISIBLE
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.availSupportInfraLayout.btnAvailSupportInfraPrevious.setOnClickListener {

            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand.visibility =
                View.VISIBLE
            binding.commonEquipmentLayout.viewCommonEquipmentDetails.visibility = View.VISIBLE

            binding.mainAvailSupportInfra.visibility = View.GONE
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.availOfStandardFormsLayout.btnAvailOfStandardFormsNext.setOnClickListener {

            // 🔹 First: Run all validations
            if (selectedTcAvailOfStandardFormApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (selectedTcAvailOfStandardFormApproval == "Send for modification") {
                selectedTcAvailOfStandardFormRemarks =
                    binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.text.toString()
                if (selectedTcAvailOfStandardFormRemarks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Kindly enter remarks first",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            } else {
                selectedTcAvailOfStandardFormRemarks = ""
            }

            // 🔹 If validations passed → show confirmation dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to submit these details?")
                .setCancelable(false)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Submit") { dialog, _ ->

                    //  Hit the insert API

                    binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility =
                        View.GONE
                    binding.availOfStandardFormsLayout.trainingAvailOfStandardFormsExpand.visibility =
                        View.GONE
                    binding.availOfStandardFormsLayout.tvTrainAvailOfStandardForms.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_verified, 0
                    )

                    binding.scroll.post {
                        binding.scroll.smoothScrollTo(0, 0)
                    }

                    dialog.dismiss()
                }
                .show()
        }
        binding.availOfStandardFormsLayout.btnAvailOfStandardFormsPrevious.setOnClickListener {

            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand.visibility =
                View.VISIBLE
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.VISIBLE

            binding.mainAvailOfStandardForms.visibility = View.GONE
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

    }


    @SuppressLint("SetTextI18n")
    private fun collectTCInfoResponse() {
        viewModel.trainingCentersInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val tcInfoData = it.wrappedList
                        for (x in tcInfoData) {

                            binding.trainingCenterInfoLayout.tvSchemeName.text = x.schemeName
                            binding.trainingCenterInfoLayout.tvCenterName.text = x.centerName
                            binding.trainingCenterInfoLayout.tvProjectState.text = x.projectState
                            binding.trainingCenterInfoLayout.tvTypeOfArea.text = x.addressType
                            binding.trainingCenterInfoLayout.tvlatAndLang.text = x.latitude + " , " + x.longitude


                            binding.trainingCenterInfoLayout.tvDistanceBus.text =
                                x.distanceFromBusStand
                            binding.trainingCenterInfoLayout.tvDistanceAuto.text =
                                x.distanceFromAutoStand
                            binding.trainingCenterInfoLayout.tvSanctionOrder.text =
                                x.sanctionOrderNo
                            binding.trainingCenterInfoLayout.tvTypeOfTraining.text = x.tcType
                            binding.trainingCenterInfoLayout.tvNatureOfTraining.text = x.tcNature
                            binding.trainingCenterInfoLayout.tvSpecialArea.text = x.specialArea
                            binding.trainingCenterInfoLayout.tvTrainingCenterAddress.text =
                                x.latitude + "," + x.tcAddress
                            binding.trainingCenterInfoLayout.tvTrainingCenterEmail.text =
                                x.tcEmailID
                            binding.trainingCenterInfoLayout.tvMobileNumber.text = x.tcMobileNo
                            binding.trainingCenterInfoLayout.tvLandlineNumber.text = x.tcLandline
                            binding.trainingCenterInfoLayout.tvParliamentaryConstituency.text =
                                x.parliamentaryConstituency
                            binding.trainingCenterInfoLayout.tvAssemblyConstituency.text =
                                x.assemblyConstituency
                            binding.trainingCenterInfoLayout.tvCenterIncharge.text =
                                x.centerIncharge
                            binding.trainingCenterInfoLayout.tvCenterInchargeMobile.text =
                                x.inchargeMobileNo
                            binding.trainingCenterInfoLayout.tvCenterInchargeEmail.text =
                                x.inchargeMailId

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


    private fun collectTCStaffResponse() {
        viewModel.getTcStaffDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        dataStaffList = it.wrappedList

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


    private fun collectTCInfraResponse() {

        viewModel.getTrainerCenterInfra.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                            binding.tvOwnershipOfBuilding.text = x.buildingOwner
                            binding.tvAreaOfBuilding.text = x.buildingArea
                            binding.tvRoofOfBuilding.text = x.buildingRoof
                            binding.tvPlasteringPainting.text = x.painting

                            selfDeclarationPdf=x.selfDeclaration
                            buildingPdf= x.roofCeilingPhoto
                            schematicPdf= x.buildingPlan
                            internalExternalWallPdf= x.buildingWallImage


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

    private fun collectTCAcademiaNonAcademia() {

        viewModel.getTcAcademicNonAcademicArea.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {


                        academiaList.clear()
                        academiaList.addAll(it.wrappedList)
                        binding.recyclerView.adapter?.notifyDataSetChanged()


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


    private fun collectTCToiletAndWash() {

        viewModel.getTcToiletWashBasin.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                            maleToiletImage = x.maleToiletImage
                            maleToiletSignageImage = x.maleToiletSignageImage
                            maleToiletUrinalsImage = x.maleUrinalImage
                            maleToiletWashbasinImage = x.maleWashBasinImage
                            femaleToiletImage = x.femaleToiletImage
                            femaleToiletSignageImage = x.femaleToiletSignageImage
                            femaleToiletWashbasinImage = x.femaleWashBasinImage
                            ovrHeadTankImage = x.overheadTankImage
                            typeOfFlooringImage = x.flooringTypeImage
                            binding.yesNoMaleToilet.text= x.maleToilet.toString()
                            binding.yesNoMaleUrinals.text= x.maleUrinal.toString()
                            binding.yesNoMaleWashBasin.text= x.maleWashBasin.toString()
                            binding.yesNoFemaleToilet.text= x.femaleToilet.toString()
                            binding.yesNoFemaleWashBasin.text= x.femaleWashBasin.toString()
                            binding.yesNoOverheadTank.text= x.overheadTanks
                            binding.yesNoTypeOfFlooring.text= x.flooringType



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


    private fun collectTCDescOtherArea() {

        viewModel.getDescriptionOtherArea.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                             binding.valueCorridorNo.text=  x.corridorNo
                             binding.valueLenghth.text=  x.length
                             binding.valueWidth.text=  x.width
                             binding.valueArea.text=  x.areas
                             binding.valueLights.text=  x.numberOfLights
                             binding.yesNoFans.text=  x.numberOfFans
                             binding.yesNoCirculationArea.text=  x.circulationArea
                             binding.yesNoOpenSpace.text=  x.openSpace
                             binding.yesNoParking.text=  x.parkingSpace


                            fansImage = x.descProofImagePath.toString()
                            circulationAreaImage = x.circulationAreaImagePath.toString()
                            openSpaceImage = x.openSpaceImagePath.toString()
                            parkingSpaceImage = x.parkingSpaceImagePath.toString()



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

    private fun collectTCTeaching() {

        viewModel.getTeachingLearningMaterial.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {



                            binding.yesNoTrade.text= x.trade
                            binding.yesNoNatureofTraining.text= x.trainingNature
                            binding.yesNoTradeAsPerProject.text= x.tradesAvailable
                            binding.yesNoIsTrainingPlanAvail.text= x.trainingPlan
                            binding.yesNoIsDomainCirAvail.text= x.domainCurriculum
                            binding.yesNoIsActivityCumLess.text= x.availableACLP
                            binding.yesNoIsWelcomeKitAvail.text= x.welcomeKit
                            binding.yesNoNameOfCertifyingAg.text= x.certifingAgencyName
                            binding.yesNoAssessmentMaterial.text= x.assessmentMaterial


                            welcomeKitImage = x.welcomeKitPdf






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

    private fun collectTCGeneral() {

        viewModel.getGeneralDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {


                            signOfLeakageImage = x.signLeakageImage
                            protectionStairsBalImage = x.stairsProtectionImage

                            binding.yesNoSignOfLiakage.text = x.signLeakage
                            binding.yesNoProtectionOfStairs.text = x.stairsProtection
                            binding.yesNoconformanceDDUGKY.text = x.ddugkyConfrence
                            binding.yesNoCandidateComeSafely.text = x.centerSafty

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


    private fun collectTCElectrical() {

        viewModel.getElectricalWiringStandard.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                            securingWiringImage = x.wireSecurityImage.toString()
                            switchBoardImage = x.switchBoardImage.toString()


                            binding.yesNoSecuringWire.text = x.wireSecurity
                            binding.yesNoSwitchBoard.text = x.switchBoard

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

    private fun collectTCSignage() {

        viewModel.getSignagesAndInfoBoard.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                          tcNameBoardImage = x.tcNameImage.toString()
                          activitySummaryBoardImage = x.activityAchivementImage.toString()
                          studentEntitlementBoardImage = x.studentEntitlementImage.toString()
                          contactDetailImpoPeopleImage = x.contactDetailsImage.toString()
                          basicInfoBoardImage = x.basicInfoImage.toString()
                          codeOfConductImage = x.codeConductImage.toString()
                          studentAttendanceImage = x.studentsAttendanceImage.toString()


                            binding.signageLayout.yesNoCenterNameBoard.text= x.tcName
                            binding.signageLayout.yesNoSummaryAcheivement.text= x.activityAchivement
                            binding.signageLayout.yesNoStudentEntitlement.text= x.studentEntitlement
                            binding.signageLayout.yesNoContactDetail.text= x.contactDetails
                            binding.signageLayout.yesNoBasicInfoBoard.text= x.basicInfo
                            binding.signageLayout.yesNoCodeOfConduct.text= x.codeConduct
                            binding.signageLayout.yesNoAttendanceSummary.text= x.studentsAttendance

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


    private fun collectTCIpEnabele() {

        viewModel.getIpEnabledCamera.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                            centralMonitorImage = x.centralMonitorImagePath.toString()
                            conformationOfCCTVImage= x.cctvConformanceImagePath.toString()
                            storageOfCCtvImage = x.cctvStorageImagePath.toString()
                            dvrImage = x.dvrStaticIpImagePath.toString()



                            binding.ipCameraLayout.yesNoCentralMonitor.text= x.centralMonitor
                            binding.ipCameraLayout.yesNoConformanceCCTV.text= x.cctvConformance
                            binding.ipCameraLayout.yesNoStorageCCTV.text= x.cctvStorage
                            binding.ipCameraLayout.yesNoDvrStaticIP.text= x.dvrStaticIp
                            binding.ipCameraLayout.yesNoIpEnabled.text= x.ipEnable
                            binding.ipCameraLayout.yesNoResolution.text= x.resolution
                            binding.ipCameraLayout.yesNoVideoStream.text= x.videoStream
                            binding.ipCameraLayout.yesNoRemoteAccessWeb.text= x.remoteAccessBrowser
                            binding.ipCameraLayout.yesNoRemoteAccessUsers.text= x.simultaneousAccess
                            binding.ipCameraLayout.yesNoSupportedProtocols.text= x.supportedProtocol
                            binding.ipCameraLayout.yesNoColorAudio.text= x.colorVideoAudit
                            binding.ipCameraLayout.yesNoStorageFacility.text= x.storageFacility


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

    private fun collectTCCommonEquipment() {

        viewModel.getCommonEquipment.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                when (it.responseCode) {
                    200 -> {

                        val dataInfra = it.wrappedList

                        for (x in dataInfra) {

                            electricPowerImage = x.ecPowerBackupImage.toString()
                            installBiometricImage = x.biomatricDeviceInstallationImage.toString()
                            installationCCTVImage = x.cctvMoniotrInstallImage.toString()
                            storagePlaceSecuringDocImage = x.storageSecuringImage.toString()
                            printerCumImage = x.printerScannerImage.toString()
                            digitalCameraImage = x.digitalCameraImage.toString()
                            grievanceImage = x.grievanceRegisterImage.toString()
                            minimumEquipmentImage = x.minimumEquipmentImage.toString()
                            directionBoardsImage = x.directionBoardImage.toString()




                            binding.commonEquipmentLayout.yesNoElectricalPowerBackup.text= x.ecPowerBackup
                            binding.commonEquipmentLayout.yesNoBiometricDevices.text= x.biomatricDeviceInstallation
                            binding.commonEquipmentLayout.yesNoCCTVMonitor.text= x.cctvMoniotrInstall
                            binding.commonEquipmentLayout.yesNoStorageDocs.text= x.storageSecuring
                            binding.commonEquipmentLayout.yesNoPrinterScanner.text= x.printerScanner.toString()
                            binding.commonEquipmentLayout.yesNoDigitalCamera.text= x.digitalCamera.toString()
                            binding.commonEquipmentLayout.yesNoGrievanceRegister.text= x.grievanceRegister.toString()
                            binding.commonEquipmentLayout.yesNoMinEquipment.text= x.minimumEquipment.toString()
                            binding.commonEquipmentLayout.yesNoDirectionBoards.text= x.directionBoard.toString()



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










    fun openBase64Pdf(context: Context, base64: String) {
        try {
            // Clean Base64
            val cleanBase64 = base64
                .replace("data:application/pdf;base64,", "")
                .replace("\\s".toRegex(), "")

            // Decode
            val pdfBytes = Base64.decode(cleanBase64, Base64.DEFAULT or Base64.NO_WRAP)

            // Save temporarily in cache
            val pdfFile = File.createTempFile("temp_", ".pdf", context.cacheDir)
            pdfFile.outputStream().use { it.write(pdfBytes) }

            // Build URI using FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",  // must match manifest
                pdfFile
            )

            // Launch chooser
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Open PDF with"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to open PDF", Toast.LENGTH_SHORT).show()
        }
    }



    fun showBase64ImageDialog(context: Context, base64ImageString: String?, title: String = "Image") {
        val imageView = ImageView(context)

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

        imageView.adjustViewBounds = true
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setPadding(20, 20, 20, 20)

        // Show in dialog
        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(imageView)
            .setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showCustomDialog(layoutRes: Int, setupView: (View, AlertDialog) -> Unit) {
        val dialogView = layoutInflater.inflate(layoutRes, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Allow full width
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setupView(dialogView, dialog)

        dialog.show()
    }



}



