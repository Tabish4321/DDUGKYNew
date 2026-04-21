package com.deendayalproject.fragments.composeui.previous_inspection

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.model.request.GetDDSaveDataReq
import com.deendayalproject.model.request.SavePreDDQueReq
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel

@Composable
fun DueDiligenceEditScreen(
    viewModel: InspectionViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val savedRes by viewModel.getSavedPreviousDueDiligenceQue.collectAsState()
    val saveRes by viewModel.savePreviousDueDiligenceQues.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    val inspectionId = AppUtil.getSavedInspectionIdPreference(context)
    val trainingCenterId = AppUtil.getSavedTrainingCenterIdPreference(context)

    val observationList = listOf(
        PreviousObservationRes(
            questionId = 1,
            title = "Is the training center infrastructure available as per the approved due diligence?",
            conductedBy = "",
            remarks = "",
            preAnswer = savedRes?.wrappedList?.firstOrNull()?.inspectorAnswer,
            preRemark = savedRes?.wrappedList?.firstOrNull()?.inspectorRemark
        )
    )

    PreviousInspectionDueAllObserver(
        observationItems = observationList,
        onBackClick = { navController.popBackStack() },
        isLoading = isLoading,
        expandedIndex = expandedIndex,
        onExpandChange = { expandedIndex = it },

        onExpand = { questionId ->

            viewModel.getSavedPreviousDueDiligenceQue(
                GetDDSaveDataReq(
                    inspectionId = inspectionId,
                    questionId = questionId.toString(),
                    appVersion = BuildConfig.VERSION_NAME,
                    trainingCenterId = trainingCenterId
                ),
                AppUtil.getSavedTokenPreference(context)
            )
        },

        onSubmit = { ui ->
            val finalAnswer = ui.selectionYesNo ?: ""

            val finalRemark = if (finalAnswer == "No") {
                if (ui.inputRemarks.isBlank()) {
                    Toast.makeText(context, "Remark is mandatory for No", Toast.LENGTH_SHORT).show()
                    return@PreviousInspectionDueAllObserver
                }
                ui.inputRemarks
            } else {
                ""
            }

            viewModel.savePreviousDueDiligenceQues(
                SavePreDDQueReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    inspectionId = inspectionId.toInt(),
                    questionId = ui.questionId,
                    answer = finalAnswer,
                    trainingCenterId = trainingCenterId.toInt(),
                    remark = finalRemark
                ),
                AppUtil.getSavedTokenPreference(context)
            )

        }
    )

    LaunchedEffect(saveRes) {
        saveRes?.let {
            Toast.makeText(context, it.responseDesc, Toast.LENGTH_SHORT).show()
        }
        viewModel.getSavedPreviousDueDiligenceQue(
            GetDDSaveDataReq(
                inspectionId = inspectionId,
                questionId = "1",
                appVersion = BuildConfig.VERSION_NAME,
                trainingCenterId = trainingCenterId
            ),
            AppUtil.getSavedTokenPreference(context)
        )
    }


}