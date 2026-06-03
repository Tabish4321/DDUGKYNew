package com.deendayalproject.fragments.composeFragment


import TrainingCenterStatusQuestion
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.base.BaseFragment
import com.deendayalproject.databinding.InspectionListFragmentBinding
import com.deendayalproject.fragments.composeui.EmptyScreen
import com.deendayalproject.fragments.composeui.common.ShimmerTrainingList
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.ImagePreviewDialog
import com.deendayalproject.fragments.composeui.ongoingcandidateverification.ProofWithQuestion
import com.deendayalproject.fragments.composeui.trainingCenListAandDetails.TrainingCenterListScreen
import com.deendayalproject.model.request.GetTcInspectionList
import com.deendayalproject.model.request.TrainingCenterOpenStatusReq
import com.deendayalproject.model.response.TrainingCenterListInspecRes
import com.deendayalproject.model.uistate.TrainingCenterOpenUiState
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.AppUtil.bitmapToCompressedBase64
import com.deendayalproject.viewmodel.InspectionViewModel
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream

class InspectionListFragment : BaseFragment<InspectionListFragmentBinding>(
    bindingInflater = InspectionListFragmentBinding::inflate
) {

    private val viewModel: InspectionViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun initializeViews() {

        binding.composeInspectionListView.apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                var showTrainingCenterDialog by remember { mutableStateOf(false) }
                var selectedTrainingCenter by remember {
                    mutableStateOf<TrainingCenterListInspecRes?>(
                        null
                    )
                }
                var trainingCenterUiState by remember { mutableStateOf(TrainingCenterOpenUiState()) }
                val trainingCenterOpenApiResponse by viewModel.trainingCenterOpenStatus.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                val dueDiligenceListResponse by viewModel.dueDiligenceList.collectAsState()
                val isLoading by viewModel.loading.collectAsState()

                var showImagePreview by remember {
                    mutableStateOf(false)
                }

                val cameraLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicturePreview()
                ) { bitmap ->

                    bitmap?.let {
                        val base64 = bitmapToCompressedBase64(it)
//                        val byteArrayOutputStream = ByteArrayOutputStream()
//
//                        it.compress(
//                            Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream
//                        )
//                        val base64Image = Base64.encodeToString(
//                            byteArrayOutputStream.toByteArray(), Base64.DEFAULT
//                        )
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val jsonResponse = gson.toJson(base64)
                        Log.d("Base64", "✅ Success Response:\n$jsonResponse")

                        trainingCenterUiState = trainingCenterUiState.copy(
                            attachmentBase64 = base64, attachmentError = false
                        )
                    }
                }


                //  API Call on first load
                LaunchedEffect(Unit) {
                    viewModel.getDueDiligenceDetails(
                        GetTcInspectionList(BuildConfig.VERSION_NAME),
                        AppUtil.getSavedTokenPreference(requireContext())
                    )
                }

                LaunchedEffect(trainingCenterOpenApiResponse) {
                    trainingCenterOpenApiResponse ?: return@LaunchedEffect
                    Toast.makeText(
                        requireContext(),
                        trainingCenterOpenApiResponse?.responseDesc,
                        Toast.LENGTH_SHORT
                    ).show()
                    showTrainingCenterDialog = false
                    if (trainingCenterUiState.answer.equals("YES", true)) {
                        selectedTrainingCenter?.let { proceedInspection(it) }
                    } else {
                        findNavController().navigateUp()
                    }
                }

                //  Error Snackbar Collector
                LaunchedEffect(Unit) {
                    viewModel.errorMessage.collect {
                        // snackbarHostState.showSnackbar(it)
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }

                //  Session Expired Collector
                LaunchedEffect(Unit) {
                    viewModel.sessionExpired.collect {
                        snackbarHostState.showSnackbar("Session Expired")

                    }
                }


                Scaffold(
                    contentWindowInsets = WindowInsets(0), snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }) { padding ->

                    Box(modifier = Modifier.padding(padding)) {
                        when {
                            isLoading -> {
                                ShimmerTrainingList()
                            }

                            dueDiligenceListResponse == null -> {

                                EmptyScreen(
                                    { findNavController().navigateUp() }, "Training Centers"
                                )
                            }

                            else -> {

                                val data = dueDiligenceListResponse?.wrappedList ?: emptyList()

                                TrainingCenterListScreen(

                                    items = data.map {

                                        TrainingCenterListInspecRes(
                                            id = it.trainingCenterId,
                                            prnNumber = it.prnRegistrationNo,
                                            sanctionLetterNo = it.sanctionOrder,
                                            inspectionType = it.inspectionType,
                                            inspectionId = it.inspectionId,
                                            centerType = it.centerType,
                                            inspectionDate = it.inspectionDate,
                                            trainingCenterCode = it.inspectionCode,
                                            piaName = it.piaName,
                                            trainingCenterName = it.trainingCenterName,
                                            inspectionCode = it.inspectionCode
                                        )
                                    },

                                    isLoading = false,

                                    onBackClick = {
                                        findNavController().navigateUp()
                                    },

                                    onItemClick = { selectedItem ->

                                        selectedTrainingCenter = selectedItem

                                        trainingCenterUiState = TrainingCenterOpenUiState()

                                        showTrainingCenterDialog = true
                                    })


                                // ===========================
                                // ADD DIALOG HERE
                                // ===========================

                                if (showTrainingCenterDialog && selectedTrainingCenter != null) {

                                    Dialog(

                                        onDismissRequest = {

                                            showTrainingCenterDialog = false
                                        }

                                    ) {

                                        Card(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            ElevatedCard(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(8.dp),
                                                colors = CardDefaults.elevatedCardColors(
                                                    containerColor = Color.White
                                                ),
                                                elevation = CardDefaults.elevatedCardElevation(
                                                    defaultElevation = 6.dp
                                                )
                                            ) {
                                                TrainingCenterStatusQuestion(
                                                    answer = trainingCenterUiState.answer,
                                                    remarks = trainingCenterUiState.remarks,
                                                    imageBase64 = trainingCenterUiState.attachmentBase64,
                                                    remarksError = trainingCenterUiState.remarksError,
                                                    imageError = trainingCenterUiState.attachmentError,
                                                    onAnswerChange = { answer ->
                                                        trainingCenterUiState =
                                                            trainingCenterUiState.copy(
                                                                answer = answer,
                                                                remarksError = false,
                                                                attachmentError = false
                                                            )
                                                    },
                                                    onRemarksChange = { remarks ->
                                                        trainingCenterUiState =
                                                            trainingCenterUiState.copy(
                                                                remarks = remarks,
                                                                remarksError = false
                                                            )
                                                    },
                                                    onCaptureImage = { cameraLauncher.launch(null) },
                                                    onPreviewImage = { showImagePreview = true })

                                                if (showImagePreview && !trainingCenterUiState.attachmentBase64.isNullOrEmpty()) {

                                                    ImagePreviewDialog(

                                                        base64 = trainingCenterUiState.attachmentBase64.orEmpty(),

                                                        onClose = {

                                                            showImagePreview = false
                                                        })
                                                }

                                                Spacer(
                                                    modifier = Modifier.height(20.dp)
                                                )

                                                Row(

                                                    modifier = Modifier.fillMaxWidth(),

                                                    horizontalArrangement = Arrangement.Center

                                                ) {

//                                                    TextButton(
//
//                                                        onClick = {
//
//                                                            showTrainingCenterDialog = false
//                                                        }
//
//                                                    ) {
//
//                                                        Text("Cancel")
//                                                    }

                                                    Spacer(
                                                        modifier = Modifier.width(10.dp)
                                                    )

                                                    Button(

                                                        onClick = {

                                                            val selectedItem =
                                                                selectedTrainingCenter
                                                                    ?: return@Button

//                                                            proceedInspection(selectedItem)
//                                                            return@Button
                                                            when (trainingCenterUiState.answer) {

                                                                "YES" -> {

                                                                    TrainingCenterOpenStatusApi(
                                                                        selectedItem = selectedItem,
                                                                        answer = "Yes",
                                                                        remarks = "",
                                                                        attachment = ""
                                                                    )
                                                                }

                                                                "NO" -> {

                                                                    val remarksValid =
                                                                        trainingCenterUiState.remarks.trim()
                                                                            .isNotEmpty()

                                                                    val attachmentValid =
                                                                        !trainingCenterUiState.attachmentBase64.isNullOrEmpty()

                                                                    if (!remarksValid || !attachmentValid) {

                                                                        trainingCenterUiState =
                                                                            trainingCenterUiState.copy(

                                                                                remarksError = !remarksValid,

                                                                                attachmentError = !attachmentValid
                                                                            )

                                                                        return@Button
                                                                    }

                                                                    TrainingCenterOpenStatusApi(
                                                                        selectedItem = selectedItem,
                                                                        answer = "No",
                                                                        remarks = trainingCenterUiState.remarks,

                                                                        attachment = trainingCenterUiState.attachmentBase64.orEmpty()
                                                                    )
                                                                }

                                                                else -> {

                                                                    Toast.makeText(
                                                                        requireContext(),
                                                                        "Please select Yes or No",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }

                                                        }

                                                    ) {
                                                        Text("Submit")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }
    }

    private fun TrainingCenterOpenStatusApi(
        selectedItem: TrainingCenterListInspecRes,
        answer: String,
        remarks: String,
        attachment: String
    ) {
        val request = TrainingCenterOpenStatusReq(
            appVersion = BuildConfig.VERSION_NAME,
            trainingCenterId = selectedItem.id.toString(),
            inspectionId = selectedItem.inspectionId,
            trainingCenterOpenOrNot = answer,
            tcOpenOrNotRemark = remarks,
            tcOpenOrNotAttachment = attachment
        )

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonResponse = gson.toJson(request)
        Log.d("TrainingCenterOpenStatusReq", "✅ Success Response:\n$jsonResponse")
        viewModel.insertTrainingCenterOpenStatus(
            request = request,
            header = AppUtil.getSavedTokenPreference(requireContext())
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun proceedInspection(selectedItem: TrainingCenterListInspecRes) {

        val formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val currentDate = java.time.LocalDate.now()

        val inspectionDate = try {
            selectedItem.inspectionDate?.let {
                java.time.LocalDate.parse(it, formatter)
            } ?: currentDate
        } catch (e: Exception) {
            currentDate
        }

        if (inspectionDate == currentDate) {

            AppUtil.saveInspectionIdPreference(
                requireContext(), selectedItem.inspectionId
            )

            AppUtil.saveTrainingCenterIdPreference(
                requireContext(), selectedItem.id.toString()
            )

            AppUtil.saveCenterTypePreference(
                requireContext(), selectedItem.centerType
            )

            AppUtil.saveSanctionOrderInsPreference(
                requireContext(), selectedItem.sanctionLetterNo
            )

            findNavController().navigate(
                InspectionListFragmentDirections.actionInspectionListFragmentToInspectionBasicDetailsFragment(
                        selectedItem.prnNumber,
                        selectedItem.sanctionLetterNo,
                        selectedItem.inspectionType,
                        selectedItem.id
                    )
            )

        } else {

            Toast.makeText(
                requireContext(), "This Inspection will Active on ${
                    selectedItem.inspectionDate ?: "Today"
                }", Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun setupObservers() {}
    override fun setupClickListeners() {}
    override fun loadInitialData() {}
}