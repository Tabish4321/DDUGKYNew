package com.deendayalproject.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.deendayalproject.R
import com.deendayalproject.network.SecurePreferenceManager.getToken
import com.deendayalproject.network.SecurePreferenceManager.saveToken
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass


object AppUtil {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val storagePermissions = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
    val legacyStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {

        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @Volatile
    private var isSessionDialogShown = false

    fun showSessionExpiredDialog(
        navController: NavController,
        context: Context
    ) {
        if (isSessionDialogShown) return

        val activity = context as? Activity ?: return
        if (activity.isFinishing || activity.isDestroyed) return

        isSessionDialogShown = true

        AlertDialog.Builder(activity)
            .setTitle("Session Expired")
            .setMessage("Your session has expired. Please log in again.")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                isSessionDialogShown = false
                logoutUser(navController, activity)
            }
            .setOnDismissListener {
                isSessionDialogShown = false
            }.show()
    }



    fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            ContextCompat.checkSelfPermission(
                context,
                legacyStoragePermission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun logoutUser(navController: NavController, context: Context) {
        // Clear user session data
        saveLoginStatus(context, false)

        // Navigate to login and reset the flag after navigation
        navController.navigate(
            R.id.fragmentLogin,
            null,
            NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // Clear everything
                .build()
        )
        isSessionDialogShown = false // Reset flag after navigation
    }
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

//    Ajit Ranjan


    fun saveOJTBatchIDPreference(context: Context, batchId: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("batchId", batchId).apply()
    }

    fun getSavedOJTBatchIDPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("batchId", "") ?: ""
    }







    fun hideKeyboard(context: Context, view: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val focusedView = view ?: (context as? Activity)?.currentFocus ?: View(context)
        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }

    fun savetopValuePreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("topValue", tokenCode).apply()
    }

    fun gettopValuePreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("topValue", "") ?: ""
    }
    fun savesanctionOrderPreference(context: Context, sanctionOrder: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("sanctionOrder", sanctionOrder).apply()
    }
    fun savecenterIdPreference(context: Context, sanctionOrder: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("centerId", sanctionOrder).apply()
    }

    fun getcenterIdPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("centerId", "") ?: ""
    }
    fun getsanctionOrderPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("sanctionOrder", "") ?: ""
    }


    fun savesanctionOrderRFPreference(context: Context, sanctionOrder: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("sanctionOrderRF", sanctionOrder).apply()
    }
    fun savecenterIdRFPreference(context: Context, sanctionOrder: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("centerIdRF", sanctionOrder).apply()
    }

    fun getcenterIdRFPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("centerIdRF", "") ?: ""
    }
    fun getsanctionOrderRFPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("sanctionOrderRF", "") ?: ""
    }


    fun saveFacilityIdRFPreference(context: Context, sanctionOrder: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("facilityId", sanctionOrder).apply()
    }

    fun getFacilityIdRFPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("facilityId", "") ?: ""
    }

    fun sha512Hash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

        // Convert bytes to hex string
        return hashBytes.joinToString("") { "%02x".format(it) }
    }



    fun createFileName(userId: Int?): String {
        return "${userId}_${System.currentTimeMillis()}.jpg"
    }

    fun getCurrentDateForAttendance(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }



    fun bitmapToBase64(bitmap: Bitmap): String {
        val stream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }


    fun bitmapToCompressedBase64(bitmap: Bitmap): String {

        val stream = ByteArrayOutputStream()

        val resizedBitmap =
            Bitmap.createScaledBitmap(bitmap, 800, 800, true)

        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            70,
            stream
        )

        val byteArray = stream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun saveCenterTypePreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("CenterType", tokenCode).apply()
    }

    fun getSavedCenterTypePreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CenterType", "") ?: ""
    }


    fun saveSanctionOrderInsPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("SanctionOrderIns", tokenCode).apply()
    }

    fun getSavedSanctionOrderInsPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("SanctionOrderIns", "") ?: ""
    }


    fun showBase64Dialog(context: Context, base64: String) {

        val imageBytes = Base64.decode(base64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        val dialog = Dialog(context)
        val imageView = ImageView(context)

        imageView.setImageBitmap(bitmap)

        dialog.setContentView(imageView)
        dialog.show()
    }

    fun showNoImageDialog(context: Context) {

        val dialog = Dialog(context)

        val imageView = ImageView(context)

        imageView.setImageResource(R.drawable.no_image)

        dialog.setContentView(imageView)

        dialog.show()
    }

    fun validate(answer: String?, remark: String): Boolean {

        if (answer == null) return false

        if (answer == "No" && remark.isBlank()) return false


        return true
    }
    fun saveInspectionIdPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("InspectionId", tokenCode).apply()
    }

    fun getSavedInspectionIdPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("InspectionId", "") ?: ""
    }

    fun savePreviouseInspectionIdPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("previousInspectionId", tokenCode).apply()
    }

    fun getPreviouseSavedInspectionIdPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("previousInspectionId", "") ?: ""
    }


    fun saveTrainingCenterIdPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("TrainingCenterId", tokenCode).apply()
    }

    fun getSavedTrainingCenterIdPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("TrainingCenterId", "") ?: ""
    }






    fun saveTokenPreference(context: Context, tokenCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", tokenCode).apply()
    }

    fun getSavedTokenPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }


    fun saveLoginIdPreference(context: Context, loginId: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("login_id", loginId).apply()
    }


    fun getSavedLoginIdPreference(context: Context): String {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("login_id", "") ?: ""
    }

    fun clearPreferences(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    fun decodeBase64ToBitmap(base64Str: String?) =
        try {
            base64Str?.let {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            }
        } catch (e: Exception) {
            null
        }

    // Add this function to your class
    fun convertUriToBase64(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun getTimeZone(): String {
        return TimeZone.getDefault().id
    }

    fun getTimeZoneOffset(): Int {
        val offset: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.now().offset.totalSeconds / 60
        } else {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            tz.getOffset(cal.timeInMillis) / 1000 * 60
        }
        return offset
    }

    fun getAndroidDeviceInfo(): String {
        return "MODEL : ${Build.MODEL}, MANUFACTURER : ${Build.MANUFACTURER}, DEVICE : ${Build.DEVICE}"
    }


    fun changeAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode) // For example, "en" for English, "es" for Spanish, etc.
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale) // Set the locale for the app

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun getLoginStatus(context: Context): Boolean {
        // Get the SharedPreferences instance
        val sharedPreferences =
            context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Retrieve the login status (false is the default value if not found)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }


    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        // Get the SharedPreferences instance
        val sharedPreferences =
            context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Save the login status
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()  // Use apply() for asynchronous saving
    }


    fun showAlertDialog(context: Context, title: String, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
        return false
    }

    fun getProgressDialog(context: Context?): AlertDialog? {
        if (context == null) return null
        return MaterialAlertDialogBuilder(context)
            .setView(R.layout.layout_progress)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .setCancelable(false)
            .create()
    }

    fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language_code", languageCode)
        editor.apply()
    }

    fun getSavedLanguagePreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language_code", "en") ?: "en" // Default to English
    }

    fun saveTcId(context: Context, tcId: String) {
        val prefs = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("TC_ID", tcId).apply()
    }

    fun getSavedTcId(context: Context): String {
        return context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("TC_ID", "") ?: ""
    }

    fun saveSanctionOrder(context: Context, sanctionOrder: String) {
        val prefs = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("SANCTION_ORDER", sanctionOrder).apply()
    }
    fun getSavedSanctionOrder(context: Context): String {
        return context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("SANCTION_ORDER", "") ?: ""
    }


    inline fun <reified T> fromJson(json: String): T {
        val gson = Gson()
        return gson.fromJson(json, T::class.java)
    }

    fun <T> toJson(model: T): String {
        val gson = Gson()
        return gson.toJson(model)
    }

    fun generateOTP(): Int {
        val secureRandom = SecureRandom()
        return secureRandom.nextInt(9000) + 1000 // Ensures a 4-digit number (1000 - 9999)
    }

    fun isValidMobileNumber(mobileNumber: String): Boolean {
        val regex = "^[6789]\\d{9}$".toRegex()
        return mobileNumber.matches(regex)
    }

    fun decodeBase64(base64String: String): String? {
        return try {
            // Decode the Base64 string to a byte array
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

            // Convert the byte array to a string
            String(decodedBytes, Charsets.UTF_8)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null // Return null if decoding fails
        }
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clipData)

    }


    fun setupPasswordToggle(editText: EditText) {
        var isPasswordVisible = false

        val context = editText.context
        val eyeOn: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_passwordon)
        val eyeOff: Drawable? = ContextCompat.getDrawable(context, R.drawable.ic_passwordtoggle)

        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeOff, null)

        editText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = editText.compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchX = event.x.toInt()
                    val width = editText.width
                    val paddingEnd = editText.paddingEnd

                    if (touchX >= width - drawableWidth - paddingEnd) {
                        isPasswordVisible = !isPasswordVisible
                        if (isPasswordVisible) {
                            editText.inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                eyeOn,
                                null
                            )
                        } else {
                            editText.inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                eyeOff,
                                null
                            )
                        }
                        editText.setSelection(editText.text?.length ?: 0)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }


    fun imageViewToBase64(imageView: ImageView): String {
        val drawable = imageView.drawable ?: return ""
        val bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun compressBitmap(bitmap: Bitmap): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val bytes = stream.toByteArray()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }



    /*
        fun imageUriToBase64(context: Context, uri: Uri, maxSize: Int = 1024): String? {
            return try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true

                context.contentResolver.openInputStream(uri).use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)
                }

                options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
                options.inJustDecodeBounds = false

                val bitmap = context.contentResolver.openInputStream(uri).use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)
                } ?: return null

                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                val byteArray = outputStream.toByteArray()
                Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    */

//    fun imageUriToBase64(context: Context, uri: Uri, maxSize: Int = 800): String? {
//        return try {
//            val options = BitmapFactory.Options()
//            options.inJustDecodeBounds = true
//            context.contentResolver.openInputStream(uri).use { stream ->
//                BitmapFactory.decodeStream(stream, null, options)
//            }
//
//            options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
//            options.inJustDecodeBounds = false
//            options.inPreferredConfig = Bitmap.Config.RGB_565  // lower memory
//
//            val bitmap = context.contentResolver.openInputStream(uri).use { stream ->
//                BitmapFactory.decodeStream(stream, null, options)
//
//            } ?: return null
//
//            val outputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)  // smaller size
//
//            bitmap.recycle()
//
//            val byteArray = outputStream.toByteArray()
//            Base64.encodeToString(byteArray, Base64.NO_WRAP)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
fun imageUriToBase64(context: Context, uri: Uri, maxSize: Int = 800): String? {
    return try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        context.contentResolver.openInputStream(uri).use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        }

        // Resize the image using inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.RGB_565 // Low memory

        val bitmap = context.contentResolver.openInputStream(uri).use { stream ->
            BitmapFactory.decodeStream(stream, null, options)
        } ?: return null

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
        bitmap.recycle()

        val byteArray = outputStream.toByteArray()
        val base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP)

        if (!base64String.isNullOrEmpty()) {
            AppUtil.hideKeyboard(context, (context as Activity).window.decorView)
        }

        base64String
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height, width) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


    private fun setupSpinnerButtonVisibility(spinner: Spinner, button: Button) {
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

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: you can decide what to do here
            }
        }
    }

//rohit reset fields
        fun clearAllInputs(sectionRoot: ViewGroup) {
            for (i in 0 until sectionRoot.childCount) {
                val child = sectionRoot.getChildAt(i)

                when (child) {
                    is ViewGroup -> clearAllInputs(child)

                    is EditText -> child.text?.clear()
                    is Spinner -> child.setSelection(0)
                    is ImageView -> {
                        child.setImageDrawable(null)
                        child.visibility = View.GONE
                    }
                    is CheckBox -> child.isChecked = false
                    is RadioGroup -> child.clearCheck()
                    is Switch -> child.isChecked = false
                }
            }
        }

    fun getFileSize(base64: String): String {
        return try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val kb = bytes.size / 1024
            "$kb KB"
        } catch (e: Exception) {
            "Unknown size"
        }
    }

    fun downloadAndOpenPdf(context: Context, base64: String) {
        try {
            val pdfBytes = Base64.decode(base64, Base64.DEFAULT)

            val file = File(context.cacheDir, "revised_document.pdf")
            val fos = FileOutputStream(file)
            fos.write(pdfBytes)
            fos.flush()
            fos.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            context.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Unable to open PDF", Toast.LENGTH_SHORT).show()
        }
    }




    }


