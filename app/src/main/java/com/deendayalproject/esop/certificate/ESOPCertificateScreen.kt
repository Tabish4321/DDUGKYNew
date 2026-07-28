package com.deendayalproject.esop.certificate

import SharedViewModel
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import com.deendayalproject.R
import java.util.Locale





import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlin.math.cos
import kotlin.math.sin








import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontStyle


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin




import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deendayalproject.model.response.CertificateRes
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.util.AppUtil
import com.deendayalproject.util.ProgressDialogUtil.dismissProgressDialog
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import kotlin.getValue



//@SuppressLint("UnrememberedMutableState")
//@RequiresApi(Build.VERSION_CODES.Q)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ESOPCertificateScreen(
//    wrongAns: String,
//    numberofAttempt: String,
//    percentage: String,
//    correctAns: String,
//    result: String,
//    candidateName: String,
//    candidateMobileNo: String,
//    departmentCetegory: String,
//    certificationType: String,
//    totalQuestions: String,
//    resultText: String,
//    candidateLoginId: String,
//    id: Int,
//    onBackClick: () -> Unit
//) {
//    val viewModel: SharedViewModel = viewModel()
//    val context = LocalContext.current
//    val view = LocalView.current
//    val density = LocalDensity.current
//    var resultValue by remember { mutableStateOf(0) }
//    var CertificateList by mutableStateOf<List<CertificateRes.Certificate>>(emptyList())

//    var cardBounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }

//    val currentDate = remember {
//        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
//    }
//    var CertificateTimeValid by rememberSaveable { mutableStateOf("") }
//    var CertificateNumber by rememberSaveable { mutableStateOf("") }
//    var callCertificateApi by remember { mutableStateOf(false) }
//    val validityDate = remember {
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.DAY_OF_YEAR, 365)
//        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
//    }

//    val score = "$percentage / $totalQuestions"
//    val result = if (resultValue == 0) "FAIL" else "PASS"
//    val token = getToken(context)

//    // 👇 Screen states
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    var showEmptyDialog by remember { mutableStateOf(false) }
//    var ESOPResultList by remember { mutableStateOf(listOf<Any>()) } // apne actual type se replace karein

//    // 👇 Snackbar setup
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()

//    // ---------- 1) TOP-LEVEL AUTO CALL — sirf ek baar chalega ----------
//    LaunchedEffect(Unit) {
//        if (!departmentCetegory.isNullOrEmpty()) {
//            isLoading = true
//            val request = CertificateRequest(
//                loginId = candidateLoginId,
//                category = departmentCetegory,
//                certificateType = certificationType
//            )
//            viewModel.getcertificate(request, "Bearer $token")
//        }
//    }

//    // ---------- 2) OBSERVATION — ek hi jagah, dono call-sites (top-call + dropdown-click) yahin handle honge ----------
//    val certificateResponse by viewModel.certificate.observeAsState()

//    LaunchedEffect(certificateResponse) {
//        certificateResponse?.let { response ->
//            dismissProgressDialog()
//            isLoading = false

//            response.onSuccess { data ->
//                Log.d("GET_RESULT", "Response = $data")

//                if (data.responseCode != 200) {
//                    errorMessage = data.responseDesc
//                    scope.launch {
//                        snackbarHostState.showSnackbar(data.responseDesc ?: "Something went wrong")
//                    }
//                    return@onSuccess
//                }

//                CertificateList = data.wrappedList
//                showEmptyDialog = ESOPResultList.isEmpty()

//                // 👇 First item se CertificateNumber aur CertificateExpire nikaalna
//                CertificateList.firstOrNull()?.let { item ->

//                    CertificateNumber = item.CertificateNumber
//                    CertificateTimeValid = item.CertificateExpire.toString()
//                }

//                errorMessage = null
//                scope.launch {
//                    snackbarHostState.showSnackbar(data.responseDesc ?: "Success")
//                }
//            }

//            response.onFailure { error ->
//                isLoading = false
//                val msg = error.localizedMessage ?: "Something went wrong"
//                errorMessage = msg
//                scope.launch {
//                    snackbarHostState.showSnackbar(msg)
//                }
//            }
//        }
//    }

//    Scaffold(
//        containerColor = Color(0xFFF4F7FB),
//        snackbarHost = { SnackbarHost(snackbarHostState) },
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(text = "Certificate", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF111827))
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        },
//        bottomBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.White)
//                    .padding(12.dp),
//                horizontalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Button(
//                    onClick = {
//                        cardBounds?.let { bounds ->
//                            val fullBitmap = getBitmapFromView(view)
//                            val paddingPx = with(density) { 30.dp.toPx() }
//                            val croppedBitmap = cropBitmap(fullBitmap, bounds, paddingPx)
//                            savePdfToDownloads(context = context, bitmap = croppedBitmap)
//                        } ?: Toast.makeText(context, "Please wait, loading...", Toast.LENGTH_SHORT).show()
//                    },
//                    modifier = Modifier.weight(1f).height(46.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075CE8))
//                ) {
//                    Text("Download PDF", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }

//                OutlinedButton(
//                    onClick = {
//                        cardBounds?.let { bounds ->
//                            val fullBitmap = getBitmapFromView(view)
//                            val paddingPx = with(density) { 30.dp.toPx() }
//                            val croppedBitmap = cropBitmap(fullBitmap, bounds, paddingPx)
//                            val pdfFile = createPdf(context, croppedBitmap)
//                            sharePdf(context, pdfFile)
//                        } ?: Toast.makeText(context, "Please wait, loading...", Toast.LENGTH_SHORT).show()
//                    },
//                    modifier = Modifier.weight(1f).height(46.dp),
//                    shape = RoundedCornerShape(6.dp),
//                    border = BorderStroke(1.dp, Color(0xFFB7C7E8)),
//                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF075CE8))
//                ) {
//                    Text("Share Certificate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//            }
//        }
//    )
//    { paddingValues ->

//        var expanded by remember { mutableStateOf(false) }
//        var expandedCertificate by remember { mutableStateOf(false) }
//        var selectedDepartment by remember { mutableStateOf("Select") }
//        var selectedCertificate by remember { mutableStateOf("Select") }
//        val departments = listOf("-----Select-----", "Finance", "Operation", "PAA")
//        val certificateTypeOf = listOf("-----Select-----", "Master", "Professional")

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//        ) {

//            if (departmentCetegory.isNullOrEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                ) {
//                    // ---------- Department Dropdown ----------
//                    ExposedDropdownMenuBox(
//                        expanded = expanded,
//                        onExpandedChange = { expanded = !expanded }
//                    ) {
//                        OutlinedTextField(
//                            value = selectedDepartment,
//                            onValueChange = {},
//                            readOnly = true,
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedTextColor = Color.Black,
//                                unfocusedTextColor = Color.Black,
//                                focusedBorderColor = Color.Gray,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            label = { Text(text = "Select Department", color = Color.Black) },
//                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                            modifier = Modifier.fillMaxWidth().menuAnchor()
//                        )
//                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            departments.forEach { department ->
//                                DropdownMenuItem(
//                                    text = { Text(text = department, color = Color.Black) },
//                                    onClick = {
//                                        selectedDepartment = department
//                                        expanded = false
//                                    }
//                                )
//                            }
//                        }
//                    }

//                    Spacer(modifier = Modifier.height(10.dp))

//                    // ---------- Certificate Dropdown ----------
//                    ExposedDropdownMenuBox(
//                        expanded = expandedCertificate,
//                        onExpandedChange = { expandedCertificate = !expandedCertificate }
//                    )
//                    {
//                        OutlinedTextField(
//                            value = selectedCertificate,
//                            onValueChange = {},
//                            readOnly = true,
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedTextColor = Color.Black,
//                                unfocusedTextColor = Color.Black,
//                                focusedBorderColor = Color.Gray,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            label = { Text(text = "Select Certificate", color = Color.Black) },
//                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCertificate) },
//                            modifier = Modifier.fillMaxWidth().menuAnchor()
//                        )
//                        ExposedDropdownMenu(expanded = expandedCertificate, onDismissRequest = { expandedCertificate = false }) {
//                            certificateTypeOf.forEach { certificate ->
//                                DropdownMenuItem(
//                                    text = { Text(text = certificate, color = Color.Black) },
//                                    onClick = {
//                                        selectedCertificate = certificate
//                                        expandedCertificate = false

//                                        // 👇 Validation: dono dropdowns properly select hue ya nahi
//                                        if (selectedDepartment == "Select" || selectedCertificate == "Select") {
//                                            scope.launch {
//                                                snackbarHostState.showSnackbar("Please select both Department and Certificate")


//                                                val request = CertificateRequest(
//                                                    loginId = candidateLoginId,
//                                                    category = selectedDepartment,
//                                                    certificateType = selectedCertificate
//                                                )
//                                                viewModel.getcertificate(request, "Bearer $token")


//                                            }
//                                            return@DropdownMenuItem
//                                        }

//                                        callCertificateApi = true
//                                        isLoading = true


//                                    }
//                                )
//                            }
//                        }


////                        ExposedDropdownMenu(expanded = expandedCertificate, onDismissRequest = { expandedCertificate = false }) {
////                            certificateTypeOf.forEach { certificate ->
////                                DropdownMenuItem(
////                                    text = { Text(text = certificate, color = Color.Black) },
////                                    onClick = {
////                                        selectedCertificate = certificate
////                                        expandedCertificate = false
////                                        callCertificateApi = true
////
////                                        // ---------- 3) DROPDOWN-CLICK CALL — bas call karo, observation upar already set hai ----------
////                                        isLoading = true
////
////
////
////
////                                        val request = CertificateRequest(
////                                            loginId = candidateLoginId,
////                                            category = departmentCetegory,
////                                            certificateType = certificationType
////                                        )
////                                        viewModel.getcertificate(request, "Bearer $token")
////                                    }
////                                )
////                            }
////                        }
//                    }
//                }
//            }

//            // ---------- Certificate Card ----------
//            Box(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    modifier = Modifier.onGloballyPositioned { coordinates ->
//                        cardBounds = coordinates.boundsInWindow()
//                    }
//                ) {

//                    CertificateCard(
//                        candidateName = candidateName,
//                        score = score,
//                        result = resultText,
//                        date = currentDate,
//                        departmentType = departmentCetegory,
//                        certificationType = certificationType,
//                        certificateNumber = CertificateNumber,
//                        validityDate = CertificateTimeValid,

//                    )
////                    CertificateCard(
////                        candidateName = candidateName,
////                        score = score,
////                        result = resultText,
////                        date = currentDate,
////                        departmentType = departmentCetegory,
////                        certificationType = certificationType,
////                        certificateNumber = "${departmentCetegory.take(3)}/${certificationType.take(3)}/$id",
////                        validityDate = CertificateTimeValid,
////                        CertificateNumber = CertificateNumber
////                    )
//                }
//            }
//        }
//    }



@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ESOPCertificateScreen(
    wrongAns: String,
    numberofAttempt: String,
    percentage: String,
    correctAns: String,
    result: String,
    candidateName: String,
    candidateMobileNo: String,
    departmentCetegory: String,
    certificationType: String,
    totalQuestions: String,
    resultText: String,
    candidateLoginId: String,
    id: Int,
    onBackClick: () -> Unit
) {
    val viewModel: SharedViewModel = viewModel()
    val context = LocalContext.current
    val view = LocalView.current
    val density = LocalDensity.current
    var resultValue by remember { mutableStateOf(0) }
    var CertificateList by remember { mutableStateOf<List<CertificateRes.Certificate>>(emptyList()) }

    var cardBounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }

    val currentDate = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    }
    var CertificateTimeValid by rememberSaveable { mutableStateOf("") }
    var CertificateNumber by rememberSaveable { mutableStateOf("") }
    var callCertificateApi by remember { mutableStateOf(false) }
    val validityDate = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 365)
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
    }
    var showCertificate by remember { mutableStateOf(false) }
    val score = "$percentage / $totalQuestions"
    val result = if (resultValue == 0) "FAIL" else "PASS"
    val token = getToken(context)

    // 👇 Screen states
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showEmptyDialog by remember { mutableStateOf(false) }
    var ESOPResultList by remember { mutableStateOf(listOf<Any>()) } // apne actual type se replace karein

    // 👇 Snackbar setup
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ---------- 1) TOP-LEVEL AUTO CALL — sirf ek baar chalega ----------
    LaunchedEffect(Unit) {
        if (!departmentCetegory.isNullOrEmpty()) {
            isLoading = true
            val request = CertificateRequest(
                loginId = candidateLoginId,
                category = departmentCetegory,
                certificateType = certificationType
            )
            viewModel.getcertificate(request, "Bearer $token")
        }
    }

    // ---------- 2) OBSERVATION — ek hi jagah, dono call-sites (top-call + dropdown-click) yahin handle honge ----------
    val certificateResponse by viewModel.certificate.observeAsState()

    LaunchedEffect(certificateResponse) {
        certificateResponse?.let { response ->
            dismissProgressDialog()
            isLoading = false

            response.onSuccess { data ->
                Log.d("GET_RESULT", "Response = $data")

                if (data.responseCode != 200) {
                    showCertificate = false
                    errorMessage = data.responseDesc
                    scope.launch {
                        snackbarHostState.showSnackbar(data.responseDesc ?: "Something went wrong")
                    }
                    return@onSuccess
                }

                CertificateList = data.wrappedList
                showEmptyDialog = CertificateList.isEmpty()

                // 👇 First item se CertificateNumber aur CertificateExpire nikaalna
                CertificateList.firstOrNull()?.let { item ->
                    CertificateNumber = item.CertificateNumber
                    CertificateTimeValid = item.CertificateExpire.toString()
                }

                errorMessage = null

                if (data.responseDesc=="Certificate Fetched Successfully.") {
                    showCertificate = true
                }
                else{

                    showCertificate = false
                    scope.launch {
                        snackbarHostState.showSnackbar(data.responseDesc ?: "")
                    }
                }

            }

            response.onFailure { error ->
                showCertificate = false
                isLoading = false
                val msg = error.localizedMessage ?: "Something went wrong"
                errorMessage = msg
                scope.launch {
                    snackbarHostState.showSnackbar(msg)
                }
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4F7FB),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Certificate", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF111827))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        cardBounds?.let { bounds ->
                            val fullBitmap = getBitmapFromView(view)
                            val paddingPx = with(density) { 30.dp.toPx() }
                            val croppedBitmap = cropBitmap(fullBitmap, bounds, paddingPx)
                            savePdfToDownloads(context = context, bitmap = croppedBitmap)
                        } ?: Toast.makeText(context, "Please wait, loading...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075CE8))
                ) {
                    Text("Download PDF", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        cardBounds?.let { bounds ->
                            val fullBitmap = getBitmapFromView(view)
                            val paddingPx = with(density) { 30.dp.toPx() }
                            val croppedBitmap = cropBitmap(fullBitmap, bounds, paddingPx)
                            val pdfFile = createPdf(context, croppedBitmap)
                            sharePdf(context, pdfFile)
                        } ?: Toast.makeText(context, "Please wait, loading...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, Color(0xFFB7C7E8)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF075CE8))
                ) {
                    Text("Share Certificate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
    { paddingValues ->

        var expanded by remember { mutableStateOf(false) }
        var expandedCertificate by remember { mutableStateOf(false) }
        var selectedDepartment by remember { mutableStateOf("-----Select-----") }
        var selectedCertificate by remember { mutableStateOf("-----Select-----") }
        val departments = listOf("-----Select-----", "Finance", "Operation", "PAA")
        val certificateTypeOf = listOf("-----Select-----", "Master", "Professional")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            if (departmentCetegory.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    // ---------- Department Dropdown ----------
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDepartment,
                            onValueChange = {},
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            label = { Text(text = "Select Department", color = Color.Black) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            departments.forEach { department ->
                                DropdownMenuItem(
                                    text = { Text(text = department, color = Color.Black) },
                                    onClick = {
                                        selectedDepartment = department
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ---------- Certificate Dropdown ----------
                    ExposedDropdownMenuBox(
                        expanded = expandedCertificate,
                        onExpandedChange = { expandedCertificate = !expandedCertificate }
                    ) {
                        OutlinedTextField(
                            value = selectedCertificate,
                            onValueChange = {},
                            readOnly = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            label = { Text(text = "Select Certificate", color = Color.Black) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCertificate) },
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expandedCertificate, onDismissRequest = { expandedCertificate = false }) {
                            certificateTypeOf.forEach { certificate ->
                                DropdownMenuItem(
                                    text = { Text(text = certificate, color = Color.Black) },
                                    onClick = {
                                        selectedCertificate = certificate
                                        expandedCertificate = false

                                        // 👇 Validation: dono dropdowns properly select hue ya nahi
                                        if (selectedDepartment == "-----Select-----" || selectedCertificate == "-----Select-----") {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Please select both Department and Certificate")
                                            }
                                            return@DropdownMenuItem
                                        }

                                        // ---------- API CALL — sirf tab jab dono valid select ho chuke hon ----------
                                        callCertificateApi = true
                                        isLoading = true

                                        val request = CertificateRequest(
                                            loginId = candidateLoginId,
                                            category = selectedDepartment,
                                            certificateType = selectedCertificate
                                        )
                                        viewModel.getcertificate(request, "Bearer $token")
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ---------- Certificate Card — sirf success ke baad dikhega ----------
            if (showCertificate) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            // 👇 Yehi exact bounds hain jo PDF crop/share ke liye use honge — sirf card, baaki screen nahi
                            cardBounds = coordinates.boundsInWindow()
                        }
                    ) {
                        CertificateCard(
                            candidateName = candidateName,
                            score = score,
                            result = resultText,
                            date = currentDate,
                            departmentType = departmentCetegory,
                            certificationType = certificationType,
                            certificateNumber = CertificateNumber,
                            validityDate = CertificateTimeValid,
                        )
                    }
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFB91C1C),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
//    { paddingValues ->
//
//        var expanded by remember { mutableStateOf(false) }
//        var expandedCertificate by remember { mutableStateOf(false) }
//        var selectedDepartment by remember { mutableStateOf("-----Select-----") }
//        var selectedCertificate by remember { mutableStateOf("-----Select-----") }
//        var candidateId by remember { mutableStateOf("") }
//        val departments = listOf("-----Select-----", "Finance", "Operation", "PAA")
//        val certificateTypeOf = listOf("-----Select-----", "Master", "Professional")
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .verticalScroll(rememberScrollState())
//        ) {
//
//            if (departmentCetegory.isNullOrEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                ) {
//                    // ---------- Department Dropdown ----------
//                    ExposedDropdownMenuBox(
//                        expanded = expanded,
//                        onExpandedChange = { expanded = !expanded }
//                    ) {
//                        OutlinedTextField(
//                            value = selectedDepartment,
//                            onValueChange = {},
//                            readOnly = true,
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedTextColor = Color.Black,
//                                unfocusedTextColor = Color.Black,
//                                focusedBorderColor = Color.Gray,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            label = { Text(text = "Select Department", color = Color.Black) },
//                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                            modifier = Modifier.fillMaxWidth().menuAnchor()
//                        )
//                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            departments.forEach { department ->
//                                DropdownMenuItem(
//                                    text = { Text(text = department, color = Color.Black) },
//                                    onClick = {
//                                        selectedDepartment = department
//                                        expanded = false
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(10.dp))
//
//                    // ---------- Certificate Dropdown ----------
//                    ExposedDropdownMenuBox(
//                        expanded = expandedCertificate,
//                        onExpandedChange = { expandedCertificate = !expandedCertificate }
//                    ) {
//                        OutlinedTextField(
//                            value = selectedCertificate,
//                            onValueChange = {},
//                            readOnly = true,
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Color.White,
//                                unfocusedContainerColor = Color.White,
//                                focusedTextColor = Color.Black,
//                                unfocusedTextColor = Color.Black,
//                                focusedBorderColor = Color.Gray,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            label = { Text(text = "Select Certificate", color = Color.Black) },
//                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCertificate) },
//                            modifier = Modifier.fillMaxWidth().menuAnchor()
//                        )
//                        ExposedDropdownMenu(expanded = expandedCertificate, onDismissRequest = { expandedCertificate = false }) {
//                            certificateTypeOf.forEach { certificate ->
//                                DropdownMenuItem(
//                                    text = { Text(text = certificate, color = Color.Black) },
//                                    onClick = {
//                                        selectedCertificate = certificate
//                                        expandedCertificate = false
//
//                                        // 👇 Validation: dono dropdowns properly select hue ya nahi
//                                        if (selectedDepartment == "-----Select-----" || selectedCertificate == "-----Select-----") {
//                                            scope.launch {
//                                                snackbarHostState.showSnackbar("Please select both Department and Certificate")
//                                            }
//                                            return@DropdownMenuItem
//                                        }
//
//                                        // ---------- API CALL — sirf tab jab dono valid select ho chuke hon ----------
//                                        callCertificateApi = true
//                                        isLoading = true
//
//                                        val request = CertificateRequest(
//                                            loginId = AppUtil.getSavedLoginIdPreference(context),
//                                            category = selectedDepartment,
//                                            certificateType = selectedCertificate
//                                        )
//                                        viewModel.getcertificate(request, "Bearer $token")
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            // ---------- Certificate Card ----------
//            Box(
//                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    modifier = Modifier.onGloballyPositioned { coordinates ->
//                        cardBounds = coordinates.boundsInWindow()
//                    }
//                ) {
//                    CertificateCard(
//                        candidateName = candidateName,
//                        score = score,
//                        result = resultText,
//                        date = currentDate,
//                        departmentType = departmentCetegory,
//                        certificationType = certificationType,
//                        certificateNumber = CertificateNumber,
//                        validityDate = CertificateTimeValid,
//                    )
//                }
//            }
//        }
//    }





private fun getBitmapFromView(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(
        view.width,
        view.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

// 👇 Card ke exact bounds + upar 30dp aur neeche 30dp extra padding ke saath crop
private fun cropBitmap(
    bitmap: Bitmap,
    bounds: androidx.compose.ui.geometry.Rect,
    verticalPaddingPx: Float
): Bitmap {
    val x = bounds.left.toInt().coerceIn(0, bitmap.width - 1)
    val y = (bounds.top - verticalPaddingPx).toInt().coerceIn(0, bitmap.height - 1)
    val width = bounds.width.toInt().coerceIn(1, bitmap.width - x)
    val bottomY = (bounds.bottom + verticalPaddingPx).toInt().coerceIn(y + 1, bitmap.height)
    val height = (bottomY - y).coerceIn(1, bitmap.height - y)
    return Bitmap.createBitmap(bitmap, x, y, width, height)
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun savePdfToDownloads(context: Context, bitmap: Bitmap) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    page.canvas.drawBitmap(
        bitmap,
        null,
        RectF(30f, 20f, 565f, 812f),
        null
    )

    pdfDocument.finishPage(page)

    val fileName = "Certificate_${System.currentTimeMillis()}.pdf"
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val uri = context.contentResolver.insert(
        MediaStore.Downloads.EXTERNAL_CONTENT_URI,
        values
    )

    uri?.let {
        context.contentResolver.openOutputStream(it)?.use { output ->
            pdfDocument.writeTo(output)
        }
        Toast.makeText(context, "PDF Saved In Downloads Folder", Toast.LENGTH_LONG).show()
    }

    pdfDocument.close()
}

private fun createPdf(context: Context, bitmap: Bitmap): File {
    val pdfDocument = PdfDocument()
    val pageWidth = 595
    val pageHeight = 842

    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    val leftMargin = 30f
    val topMargin = 30f
    val bottomMargin = 30f

    val availableWidth = pageWidth - leftMargin
    val availableHeight = pageHeight - topMargin - bottomMargin

    val scale = minOf(
        availableWidth / bitmap.width.toFloat(),
        availableHeight / bitmap.height.toFloat()
    )

    val scaledWidth = bitmap.width * scale
    val scaledHeight = bitmap.height * scale

    canvas.drawBitmap(
        bitmap,
        null,
        RectF(leftMargin, topMargin, leftMargin + scaledWidth, topMargin + scaledHeight),
        null
    )

    pdfDocument.finishPage(page)

    val file = File(context.getExternalFilesDir(null), "Certificate.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return file
}

private fun sharePdf(context: Context, pdfFile: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        pdfFile
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Share Certificate"))
}

// ---------------------------------------------------------
// ------------------ CERTIFICATE CARD UI (UNCHANGED) --------------------
// ---------------------------------------------------------
@Composable
private fun CertificateCard(
    candidateName: String,
    score: String,
    result: String,
    date: String,
    departmentType: String,
    certificationType: String,
    certificateNumber: String,
    validityDate: String
) {
    val purple = Color(0xFF2E1065)
    val orange = Color(0xFFF5A623)
    val cream = Color(0xFFFFF8ED)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.65f)
            .clip(RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = purple, shape = RoundedCornerShape(8.dp))
    ) {

        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(cream, Color.White)))
        )

        // Faint watermark
        Image(
            painter = painterResource(id = R.drawable.ddugky),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.4f)
                .alpha(0.05f),
            contentScale = ContentScale.Fit
        )

        Row(modifier = Modifier.fillMaxSize()) {

            // ---------------- LEFT SIDE ----------------
            Column(
                modifier = Modifier
                    .weight(0.68f)
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.mord_image),
                        contentDescription = "Ministry of Rural Development",
                        modifier = Modifier.height(30.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ddugky_logo_transparent),
                        contentDescription = "DDU-GKY",
                        modifier = Modifier.height(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column {

                    Text(
                        text = "Certificate",
                        color = purple,
                        fontFamily = FontFamily.Serif,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 26.sp
                    )

                    Text(
                        text = "of Completion",
                        color = purple,
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "PROUDLY PRESENTED TO",
                        color = Color(0xFF6B7280),
                        letterSpacing = 1.5.sp,
                        fontSize = 8.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = candidateName.uppercase(),
                        color = Color.Black,
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .background(orange.copy(alpha = 0.22f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "has successfully completed the $certificationType Examination",
                            fontSize = 8.sp,
                            color = Color(0xFF374151)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(4.dp))
                            .dashedBorder(
                                color = purple,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "Department Category : $departmentType",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = purple
                        )
                        Text(
                            text = "Certificate Type : $certificationType",
                            fontSize = 7.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = purple
                        )
                    }
                }

                Text(
                    text = "Date: $date",
                    fontSize = 8.sp,
                    color = Color(0xFF374151)
                )
            }

            // ---------------- RIGHT SIDE (diagonal stripe) ----------------
            Box(
                modifier = Modifier
                    .weight(0.32f)
                    .fillMaxHeight()
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    val orangeStripe = Path().apply {
                        moveTo(w * 0.15f, 0f)
                        lineTo(w * 0.35f, 0f)
                        lineTo(w * 0f, h)
                        lineTo(-w * 0.2f, h)
                        close()
                    }
                    drawPath(orangeStripe, color = orange)

                    val purpleBlock = Path().apply {
                        moveTo(w * 0.35f, 0f)
                        lineTo(w, 0f)
                        lineTo(w, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(purpleBlock, color = purple)
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.85f)
                        .align(Alignment.Center)
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = certificateNumber,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.gold_logo),
                            contentDescription = "Certified Seal",
                            modifier = Modifier.size(45.dp)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "VALIDITY TILL",
                            color = orange,
                            fontSize = 7.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatDate(validityDate),
                            color = Color.White,
                            fontSize = 8.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Scan To Verify",
                            color = Color.White,
                            fontSize = 6.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color.White, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = "QR Code",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}




fun Modifier.dashedBorder(
    color: Color,
    shape: RoundedCornerShape,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 3.dp
) = this.drawBehind {
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f
        )
    )
    val cornerRadiusPx = shape.topStart.toPx(size, this)
    drawRoundRect(
        color = color,
        style = stroke,
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
}

fun formatDate(rawDate: String): String {

    if (rawDate.isBlank()) return ""

    val inputFormats = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )

    for (pattern in inputFormats) {
        try {
            val parser = SimpleDateFormat(pattern, Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")

            val date = parser.parse(rawDate) ?: continue

            // Sirf Date
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getDefault()

            return outputFormat.format(date)

        } catch (e: Exception) {
            // Try next format
        }
    }

    return rawDate
}