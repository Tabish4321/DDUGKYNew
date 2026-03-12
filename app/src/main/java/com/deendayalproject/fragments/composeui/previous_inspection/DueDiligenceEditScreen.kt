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
import com.deendayalproject.model.request.GetPrevDueQueList
import com.deendayalproject.model.request.SavePreDDQueReq
import com.deendayalproject.model.response.PreviousObservationRes
import com.deendayalproject.util.AppUtil
import com.deendayalproject.viewmodel.InspectionViewModel
import kotlinx.coroutines.delay

@Composable
fun DueDiligenceEditScreen(
    viewModel: InspectionViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val questionRes by viewModel.getPreviousDueDiligenceQuestion.collectAsState()
    val savedRes by viewModel.getSavedPreviousDueDiligenceQue.collectAsState()
    val saveRes by viewModel.savePreviousDueDiligenceQues.collectAsState()

    var isLoading by remember { mutableStateOf(true) }

    val inspectionId = AppUtil.getSavedInspectionIdPreference(context)
    val trainingCenterId = AppUtil.getSavedTrainingCenterIdPreference(context)

    LaunchedEffect(Unit) {

        viewModel.getPreviousDueDiligenceQuestion(
            GetPrevDueQueList(
                trainingCenterId = trainingCenterId,
                appVersion = BuildConfig.VERSION_NAME
            ),
            AppUtil.getSavedTokenPreference(context)
        )

        delay(500)
        isLoading = false
    }

    val observationList = questionRes?.wrappedList?.map { q ->

        val saved = savedRes?.wrappedList?.find {
            it.questionId == q.questionId
        }

        PreviousObservationRes(
            questionId = q.questionId,
            title = "Due Diligence Question ${q.questionId}",
            conductedBy = q.dueDiligenceBy,
            remarks = q.dueDiligenceRemark,
            preAnswer = saved?.inspectorAnswer,
            preRemark = saved?.inspectorRemark
        )
    } ?: emptyList()

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

            viewModel.savePreviousDueDiligenceQues(
                SavePreDDQueReq(
                    appVersion = BuildConfig.VERSION_NAME,
                    inspectionId = inspectionId.toInt(),
                    questionId = ui.questionId,
                    answer = ui.selectionYesNo ?: "",
                    trainingCenterId = trainingCenterId.toInt(),
                    remark = ui.inputRemarks
                ),
                AppUtil.getSavedTokenPreference(context)
            )
        }
    )

    LaunchedEffect(saveRes) {

        saveRes?.let {

            Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()

            expandedIndex = null

            viewModel.getSavedPreviousDueDiligenceQue(
                GetDDSaveDataReq(
                    inspectionId = inspectionId,
                    questionId = "0",
                    appVersion = BuildConfig.VERSION_NAME,
                    trainingCenterId = trainingCenterId
                ),
                AppUtil.getSavedTokenPreference(context)
            )
        }
    }
}