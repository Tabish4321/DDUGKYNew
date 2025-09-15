package com.deendayalproject.fragments

import SharedViewModel
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
    var isInfraExpanded = false
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


    val roomList = listOf(
        RoomModel("101", "12", "10", "120", "Bedroom"),
        RoomModel("102", "14", "12", "168", "Living Room"),
        RoomModel("103", "10", "8", "80", "Kitchen")
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = DescriptionAcademiaAdapter(roomList) { room ->
            Toast.makeText(requireContext(), "Viewing Room: ${room.roomNo}", Toast.LENGTH_SHORT).show()
        }



        //Adapter Information
        tcInfoAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, approvalList
        )
        binding.SpinnerTcInfo.setAdapter(tcInfoAdapter)

        binding.SpinnerTcInfo.setOnItemClickListener { parent, view, position, id ->
            selectedTcInfoApproval = parent.getItemAtPosition(position).toString()
            if (selectedTcInfoApproval== "Send for modification"){

                binding.InfoRemarks.visibility = View.VISIBLE
                binding.etInfoRemarks.visibility = View.VISIBLE

            }
            else{

                binding.InfoRemarks.visibility = View.GONE
                binding.etInfoRemarks.visibility = View.GONE
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



        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvViewTrainerAndStaff.setOnClickListener {
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


        binding.btnInfoNext.setOnClickListener {
            if (selectedTcInfoApproval.isEmpty()) {
                Toast.makeText(requireContext(), "Kindly select Approval first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            if (selectedTcInfoApproval == "Send for modification") {
                selectedTcInfoRemarks = binding.etInfoRemarks.text.toString()

                if (selectedTcInfoRemarks.isEmpty()) {
                    Toast.makeText(requireContext(), "Kindly enter remarks first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else selectedTcInfoRemarks=""

            // Common UI updates
            binding.trainingInfoExpand.visibility = View.GONE
            binding.viewInfo.visibility = View.GONE
            binding.tvTrainInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
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
            }

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

            binding.trainingInfoExpand.visibility = View.VISIBLE
            binding.viewInfo.visibility = View.VISIBLE
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

            // Common UI updates
            binding.trainingDescAcademiaExpand.visibility = View.GONE
            binding.viewDescAcademia.visibility = View.GONE
            binding.tvTrainDescAcademia.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_verified, 0)
            //binding.mainDescAcademia.visibility = View.VISIBLE
           //binding.viewDescAcademia.visibility = View.VISIBLE

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



    }


}




