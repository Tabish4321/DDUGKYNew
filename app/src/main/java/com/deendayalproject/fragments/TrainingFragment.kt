
package com.deendayalproject.fragments
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.deendayalproject.R

class TrainingFragment : Fragment() {

    private var isBasicInfoExpanded = false
    private var isInfrastructureExpanded = false
    private var isCCTVExpanded = false

    /*  //Flags for CCTV Section(Rohit)
    private var isMonitorClicked = false
    private var isConformanceClicked = false
    private var isStorageClicked = false
    private var isDVRClicked = false

    private lateinit var currentPhotoPath: String
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var currentTarget: String = ""
    private lateinit var photoUri: Uri
*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_training, container, false)

        /* cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            {
                if (success) {
                    when (currentTarget) {
                        "dvr" -> isDVRClicked = true
                        "monitor" -> isMonitorClicked = true
                        "storage" -> isStorageClicked = true
                        "conformance" -> isConformanceClicked = true
                    }
                }
            }
        }*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle TC Basic Info Section
        val headerBasicInfo = view.findViewById<LinearLayout>(R.id.headerTCBasicInfo)
        val layoutBasicInfo = view.findViewById<LinearLayout>(R.id.layoutTCBasicInfoContent)
        val iconBasicInfo = view.findViewById<ImageView>(R.id.ivToggleTCBasicInfo)

        headerBasicInfo.setOnClickListener {
            isBasicInfoExpanded = !isBasicInfoExpanded
            layoutBasicInfo.visibility = if (isBasicInfoExpanded) View.VISIBLE else View.GONE
            iconBasicInfo.setImageResource(
                if (isBasicInfoExpanded) R.drawable.outline_arrow_upward_24 else R.drawable.ic_dropdown_arrow
            )
        }

        // Handle Infrastructure Section
        val headerInfra = view.findViewById<LinearLayout>(R.id.headerInfrastructure)
        val layoutInfra = view.findViewById<LinearLayout>(R.id.layoutInfrastructureContent)
        val iconInfra = view.findViewById<ImageView>(R.id.ivToggleInfrastructure)


        headerInfra.setOnClickListener {
            isInfrastructureExpanded = !isInfrastructureExpanded
            layoutInfra.visibility = if (isInfrastructureExpanded) View.VISIBLE else View.GONE
            iconInfra.setImageResource(
                if (isInfrastructureExpanded) R.drawable.outline_arrow_upward_24 else R.drawable.ic_dropdown_arrow
            )
        }

        // Handle CCTV Compliance Section
        val headerCCTV = view.findViewById<LinearLayout>(R.id.headerCCTVCompliance)
        val layoutCCTV = view.findViewById<LinearLayout>(R.id.layoutCCTVComplianceContent)
        val iconCCTV = view.findViewById<ImageView>(R.id.ivToggleCCTVCompliance)


        headerCCTV.setOnClickListener {
            isCCTVExpanded = !isCCTVExpanded
            layoutCCTV.visibility = if (isCCTVExpanded) View.VISIBLE else View.GONE
            iconCCTV.setImageResource(
                if (isCCTVExpanded) R.drawable.outline_arrow_upward_24 else R.drawable.ic_dropdown_arrow
            )
        }

        val spinnerMonitorAccessible = view.findViewById<Spinner>(R.id.spinnerMonitorAccessible)
        val yesNoAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf("--Select--", "Yes", "No")
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerMonitorAccessible.adapter = yesNoAdapter
    }
}




