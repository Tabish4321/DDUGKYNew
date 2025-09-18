package com.deendayalproject.fragments

import SharedViewModel
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.adapter.DescriptionAcademiaAdapter
import com.deendayalproject.adapter.TrainerStaffAdapter
import com.deendayalproject.databinding.FragmentQTeamFormBinding
import com.deendayalproject.model.RoomModel
import com.deendayalproject.model.response.TrainerStaff

class QTeamFormFragment : Fragment() {

    private var _binding: FragmentQTeamFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

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

    private val roomList = listOf(
        RoomModel("101", "12", "10", "120", "Bedroom"),
        RoomModel("102", "14", "12", "168", "Living Room"),
        RoomModel("103", "10", "8", "80", "Kitchen")
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQTeamFormBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        init()


    }
    private fun init(){

        listener()

    }
    private fun listener(){

        centerId = arguments?.getString("centerId").toString()
        centerName = arguments?.getString("centerName").toString()
        sanctionOrder = arguments?.getString("sanctionOrder").toString()


        binding.trainingCenterInfoLayout.tvCenterName.text=centerName
        binding.trainingCenterInfoLayout.tvSanctionOrder.text=sanctionOrder

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = DescriptionAcademiaAdapter(roomList) { room ->
            Toast.makeText(requireContext(), "Viewing Room: ${room.roomNo}", Toast.LENGTH_SHORT).show()
        }


        // All Adapter

        //Adapter Information
        tcInfoAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.trainingCenterInfoLayout.SpinnerTcInfo.setAdapter(tcInfoAdapter)

        binding.trainingCenterInfoLayout.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedTcInfoApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcInfoApproval== "Send for modification"){

                binding.trainingCenterInfoLayout.InfoRemarks.visibility = View.VISIBLE
                binding.trainingCenterInfoLayout.etInfoRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcDescAcademiaApproval== "Send for modification"){

                binding.DescAcademiaRemarks.visibility = View.VISIBLE
                binding.etDescAcademiaRemarks.visibility = View.VISIBLE

            }
            else{

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

            if (selectedTcInfraApproval== "Send for modification"){

                binding.InfraRemarks.visibility = View.VISIBLE
                binding.etInfraRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcBasinApproval== "Send for modification"){

                binding.BasinRemarks.visibility = View.VISIBLE
                binding.etBasinRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcDescOtherAreaApproval== "Send for modification"){

                binding.DescOtherAreaRemarks.visibility = View.VISIBLE
                binding.etDescOtherAreaRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcTeachingApproval== "Send for modification"){

                binding.TeachingRemarks.visibility = View.VISIBLE
                binding.etTeachingRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcGeneralApproval== "Send for modification"){

                binding.GeneralRemarks.visibility = View.VISIBLE
                binding.etGeneralRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcElectricalApproval== "Send for modification"){

                binding.ElectricalRemarks.visibility = View.VISIBLE
                binding.etElectricalRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcSignageApproval== "Send for modification"){

                binding.signageLayout.SignageRemarks.visibility = View.VISIBLE
                binding.signageLayout.etSignageRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcIpEnableApproval== "Send for modification"){

                binding.ipCameraLayout.IpEnableRemarks.visibility = View.VISIBLE
                binding.ipCameraLayout.etIpEnableRemarks.visibility = View.VISIBLE

            }
            else{

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
            if (selectedTcCommonEquipmentApproval== "Send for modification"){

                binding.commonEquipmentLayout.CommonEquipmentRemarks.visibility = View.VISIBLE
                binding.commonEquipmentLayout.etCommonEquipmentRemarks.visibility = View.VISIBLE

            }
            else{

                binding.commonEquipmentLayout.CommonEquipmentRemarks.visibility = View.GONE
                binding.commonEquipmentLayout.etCommonEquipmentRemarks.visibility = View.GONE
            }

        }




        //Adapter Avail Support Infra Adapter
        tcAvailSupportInfraAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setAdapter(tcAvailSupportInfraAdapter)

        binding.availSupportInfraLayout.SpinnerAvailSupportInfra.setOnItemClickListener { parent, view, position, id ->
            selectedTcAvailSupportInfraApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcAvailSupportInfraApproval== "Send for modification"){

                binding.availSupportInfraLayout.AvailSupportInfraRemarks.visibility = View.VISIBLE
                binding.availSupportInfraLayout.etAvailSupportInfraRemarks.visibility = View.VISIBLE

            }
            else{

                binding.availSupportInfraLayout.AvailSupportInfraRemarks.visibility = View.GONE
                binding.availSupportInfraLayout.etAvailSupportInfraRemarks.visibility = View.GONE
            }

        }




        // AvailOfStandardFormAdapter
        tcAvailOfStandardFormAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setAdapter(tcAvailOfStandardFormAdapter)

        binding.availOfStandardFormsLayout.SpinnerAvailOfStandardForms.setOnItemClickListener { parent, view, position, id ->
            selectedTcAvailOfStandardFormApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcAvailOfStandardFormApproval== "Send for modification"){

                binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks.visibility = View.VISIBLE
                binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.visibility = View.VISIBLE

            }
            else{


                binding.availOfStandardFormsLayout.AvailOfStandardFormsRemarks.visibility = View.GONE
                binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.visibility = View.GONE
            }

        }

        // All Buttons

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.trainingCenterInfoLayout.tvViewTrainerAndStaff.setOnClickListener {
            val dialog = Dialog(requireContext()) // or requireContext() if in Fragment
            dialog.setContentView(R.layout.dialog_trainer_staff)

            val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvTrainerStaff)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // Example data list
            val dataList = listOf(
                TrainerStaff(
                    profileType = "Trainer",
                    name = "John Doe",
                    designation = "Senior Trainer",
                    engagementType = "Full-Time",
                    domainNonDomain = "Domain",
                    assignedCourse = "Electrical Engineering",
                    whetherTotCer = "Yes",
                    totCertificateNo = "TOT123456"
                ),
                TrainerStaff(
                    profileType = "Staff",
                    name = "Jane Smith",
                    designation = "Support Staff",
                    engagementType = "Part-Time",
                    domainNonDomain = "Non-Domain",
                    assignedCourse = "NA",
                    whetherTotCer = "No",
                    totCertificateNo = "-"
                )
            )

            recyclerView.adapter = TrainerStaffAdapter(dataList)

            dialog.show()
        }
        binding.trainingCenterInfoLayout.btnInfoNext.setOnClickListener {
            if (selectedTcInfoApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcInfoApproval == "Send for modification") {
                selectedTcInfoRemarks = binding.trainingCenterInfoLayout.etInfoRemarks.text.toString()

                if (selectedTcInfoRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcInfoRemarks=""

            // Common UI updates
            binding.trainingCenterInfoLayout.trainingInfoExpand.visibility = View.GONE
            binding.trainingCenterInfoLayout.viewInfo.visibility = View.GONE
            binding.trainingCenterInfoLayout.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
            binding.mainInfra.visibility = View.VISIBLE
            binding.viewInfra.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

        binding.btnInfraNext.setOnClickListener {
            if (selectedTcInfraApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcInfraApproval == "Send for modification") {
                selectedTcInfraRemarks = binding.etInfraRemarks.text.toString()
                if (selectedTcInfraRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else selectedTcInfraRemarks=""

            // Common UI updates
            binding.trainingInfraExpand.visibility = View.GONE
            binding.viewInfra.visibility = View.GONE
            binding.tvTrainInfra.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcDescAcademiaApproval == "Send for modification") {
                selectedTcDescAcademiaRemarks = binding.etDescAcademiaRemarks.text.toString()
                if (selectedTcDescAcademiaRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcDescAcademiaRemarks=""
            // Common UI updates
            binding.trainingDescAcademiaExpand.visibility = View.GONE
            binding.viewDescAcademia.visibility = View.GONE
            binding.tvTrainDescAcademia.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcBasinApproval == "Send for modification") {
                selectedTcBasinRemarks = binding.etBasinRemarks.text.toString()
                if (selectedTcBasinRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcBasinRemarks=""
            // Common UI updates
            binding.trainingToiletExpand.visibility = View.GONE
            binding.viewToilet.visibility = View.GONE
            binding.tvTrainToilet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcDescOtherAreaApproval == "Send for modification") {
                selectedTcDescOtherAreaRemarks = binding.etDescOtherAreaRemarks.text.toString()
                if (selectedTcDescOtherAreaRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcDescOtherAreaRemarks=""
            // Common UI updates
            binding.trainingDescOfOtherAreaExpand.visibility = View.GONE
            binding.viewDescOfOtherArea.visibility = View.GONE
            binding.tvTrainDescOfOtherArea.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcTeachingApproval == "Send for modification") {
                selectedTcTeachingRemarks = binding.etTeachingRemarks.text.toString()
                if (selectedTcTeachingRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcTeachingRemarks=""
            // Common UI updates
            binding.trainingTeachingExpand.visibility = View.GONE
            binding.viewTeaching.visibility = View.GONE
            binding.tvTrainTeaching.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcGeneralApproval == "Send for modification") {
                selectedTcGeneralRemarks = binding.etGeneralRemarks.text.toString()
                if (selectedTcGeneralRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcGeneralRemarks=""
            // Common UI updates
            binding.trainingGeneralDetailsExpand.visibility = View.GONE
            binding.viewGeneralDetails.visibility = View.GONE
            binding.tvTrainGeneralDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcElectricalApproval == "Send for modification") {
                selectedTcElectricalRemarks = binding.etElectricalRemarks.text.toString()
                if (selectedTcElectricalRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcElectricalRemarks=""
            // Common UI updates
            binding.trainingElectricalDetailsExpand.visibility = View.GONE
            binding.viewElectricalDetails.visibility = View.GONE
            binding.tvTrainElectricalDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcSignageApproval == "Send for modification") {
                selectedTcSignageRemarks = binding.signageLayout.etSignageRemarks.text.toString()
                if (selectedTcSignageRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcSignageRemarks=""
            // Common UI updates
            binding.signageLayout.trainingSignageBoardlDetailsExpand.visibility = View.GONE
            binding.signageLayout.viewSignageBoardDetails.visibility = View.GONE
            binding.signageLayout.tvTrainSignageBoardDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcIpEnableApproval == "Send for modification") {
                selectedTcIpEnableRemarks = binding.ipCameraLayout.etIpEnableRemarks.text.toString()
                if (selectedTcIpEnableRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcIpEnableRemarks=""
            // Common UI updates
            binding.ipCameraLayout.viewIPEnableCameraDetails.visibility = View.GONE
            binding.ipCameraLayout.trainingIPEnableCameralDetailsExpand.visibility = View.GONE

            binding.ipCameraLayout.tvTrainIPEnableCameraDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcCommonEquipmentApproval == "Send for modification") {
                selectedTcCommonEquipmentRemarks = binding.commonEquipmentLayout.etCommonEquipmentRemarks.text.toString()
                if (selectedTcCommonEquipmentRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcCommonEquipmentRemarks=""
            // Common UI updates
            binding.commonEquipmentLayout.viewCommonEquipmentDetails.visibility = View.GONE
            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand.visibility = View.GONE

            binding.commonEquipmentLayout.tvTrainCommonEquipmentDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)


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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcAvailSupportInfraApproval == "Send for modification") {
                selectedTcAvailSupportInfraRemarks = binding.availSupportInfraLayout.etAvailSupportInfraRemarks.text.toString()
                if (selectedTcAvailSupportInfraRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcAvailSupportInfraRemarks=""
            // Common UI updates
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.GONE
            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand.visibility = View.GONE
            binding.availSupportInfraLayout.tvTrainAvailSupportInfra.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)


            binding.mainAvailOfStandardForms.visibility = View.VISIBLE
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.VISIBLE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }
        binding.availSupportInfraLayout.btnAvailSupportInfraPrevious.setOnClickListener {

            binding.commonEquipmentLayout.trainingCommonEquipmentDetailsExpand.visibility = View.VISIBLE
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
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedTcAvailOfStandardFormApproval == "Send for modification") {
                selectedTcAvailOfStandardFormRemarks =
                    binding.availOfStandardFormsLayout.etAvailOfStandardFormsRemarks.text.toString()
                if (selectedTcAvailOfStandardFormRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
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

                    binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.GONE
                    binding.availOfStandardFormsLayout.trainingAvailOfStandardFormsExpand.visibility = View.GONE
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

            binding.availSupportInfraLayout.trainingAvailSupportInfraExpand.visibility = View.VISIBLE
            binding.availSupportInfraLayout.viewAvailSupportInfra.visibility = View.VISIBLE

            binding.mainAvailOfStandardForms.visibility = View.GONE
            binding.availOfStandardFormsLayout.viewAvailOfStandardForms.visibility = View.GONE

            binding.scroll.post {
                binding.scroll.smoothScrollTo(0, 0)
            }

        }

    }




}




