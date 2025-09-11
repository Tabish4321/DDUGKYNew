package com.deendayalproject.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.provider.Settings
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.deendayalproject.R
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.SecureRandom


object AppUtil {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val storagePermissions = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
    val legacyStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context) : String{

        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun showSessionExpiredDialog(navController: NavController, context: Context) {
        if (AppUtil.isSessionDialogShown) return // Prevent showing multiple dialogs

        AppUtil.isSessionDialogShown = true // Set flag to true when dialog is shown

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Session Expired")
        builder.setMessage("Your session has expired. Please log in again.")
        builder.setCancelable(false) // Prevent dismissing on outside touch or back press

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            logoutUser(navController, context)
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

     fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        } else {
            ContextCompat.checkSelfPermission(context, legacyStoragePermission) == PackageManager.PERMISSION_GRANTED
        }
    }
    fun logoutUser(navController: NavController, context: Context) {
        // Clear user session data
       AppUtil.saveLoginStatus(context, false)

        // Navigate to login and reset the flag after navigation
        navController.navigate(
            R.id.fragmentLogin,
            null,
            NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // Clear everything
                .build()
        )

        AppUtil.isSessionDialogShown = false // Reset flag after navigation
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
    private var isSessionDialogShown = false // Flag to prevent multiple dialogs

  /*  fun showSessionExpiredDialog(navController: NavController, context: Context) {
        if (isSessionDialogShown) return // Prevent showing multiple dialogs

        isSessionDialogShown = true // Set flag to true when dialog is shown

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Session Expired")
        builder.setMessage("Your session has expired. Please log in again.")
        builder.setCancelable(false) // Prevent dismissing on outside touch or back press

        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            logoutUser(navController, context)
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }*/

    /*fun logoutUser(navController: NavController, context: Context) {
        // Clear user session data
        AppUtil.saveLoginStatus(context, false)

        // Navigate to login and reset the flag after navigation
        navController.navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true) // Clear everything
                .build()
        )

        isSessionDialogShown = false // Reset flag after navigation
    }
*/
    fun saveTokenPreference(context: Context, tokenCode: String) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", tokenCode).apply()
    }

    fun getSavedTokenPreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "") ?: ""
    }


    /*  fun saveLoginIdPreference(context: Context, loginId: String) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("entity_code", entityCode)
        editor.apply()
    }*/

  fun saveLoginIdPreference(context: Context, loginId: String) {
      val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
      sharedPreferences.edit().putString("login_id", loginId).apply()
  }



    fun getSavedLoginIdPreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("login_id", "") ?: ""
    }

    fun clearPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }



/*
    fun getSavedLoginIdPreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("entity_code", "") ?: "" // Default to English
    }*/
/*    fun saveEapCanAgeLimitPreference(context: Context, tokenCode: Int) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("eap_canAge", tokenCode.toString())
        editor.apply()
    }

    fun getSavedEapCanAgeLimitPreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("eap_canAge", "") ?: "" // Default to English
    }*/


    // Add this function to your class
    fun convertUriToBase64(uri: Uri,context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun getTimeZone(): String {
        return TimeZone.getDefault().id
    }

    fun getTimeZoneOffset():Int{
        val offset: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.now().offset.totalSeconds/60
        } else {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            tz.getOffset(cal.timeInMillis)/1000*60
        }
        return offset
    }

    fun getAndroidDeviceInfo():String{
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
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

        // Retrieve the login status (false is the default value if not found)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }


    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        // Get the SharedPreferences instance
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

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


    /*fun formatScheduleTimeIntoDateTime(dateTimeString: String): Pair<String, String> {
        // Parse the input string into a ZonedDateTime object
        val zonedDateTime = ZonedDateTime.parse(dateTimeString)

        // Define the formatter for the date part
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())

        // Format the date part
        val formattedDate = zonedDateTime.format(dateFormatter)

        // Define the formatter for the time part
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH)

        // Format the time part
        val formattedTime = zonedDateTime.format(timeFormatter).lowercase()

        return Pair(formattedDate, formattedTime)
    }


    fun parseDateTime(dateString: String, timeString: String): String {
        // Define the formatter for parsing the input date
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        // Parse the date string into a LocalDateTime object
        val datePart = LocalDateTime.parse(dateString, dateFormatter)

        // Define the formatter for parsing the input time
        val timeFormatter = DateTimeFormatter.ofPattern("h : mm a", Locale.ENGLISH)
        // Parse the time string into a LocalDateTime object
        val timePart = LocalDateTime.parse(timeString, timeFormatter)

        // Combine the date and time parts
        val combinedDateTime = datePart.withHour(timePart.hour).withMinute(timePart.minute)

        // Format the combined date and time to ISO format with 'Z' to indicate UTC time
        val isoDateTime = combinedDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)

        return isoDateTime
    }


    fun formatUtcDateTimeIntoReminderDate(input: String): String {
        // Parse the input date-time string
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val parsedDateTime = LocalDateTime.parse(input, inputFormatter)

        // Define the output formatter
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mma", Locale.ENGLISH)

        // Format the parsed date-time
        val formattedDateTime = parsedDateTime.format(outputFormatter)

        return formattedDateTime
    }

    fun formatUtcDateTimeIntoReminderDateNew(input: String): String {
        // Parse the input date-time string without milliseconds and timezone
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH)
        val parsedDateTime = ZonedDateTime.parse(input, inputFormatter)

        // Define the output formatter (without timezone information)
        val outputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mma", Locale.ENGLISH)

        // Format the parsed date-time
        val formattedDateTime = parsedDateTime.format(outputFormatter)

        return formattedDateTime
    }


    fun formatReminderDateIntoUtcDateTime(input: String): String {

        val inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mma", Locale.ENGLISH)
        val parsedDateTime = LocalDateTime.parse(input, inputFormatter)

        // Convert the local date-time to a ZonedDateTime in the system default time zone
        val localZonedDateTime = parsedDateTime.atZone(ZoneId.systemDefault())

        // Convert the ZonedDateTime to UTC
        val utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))

        // Define the output formatter
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        // Format the parsed date-time
        val formattedDateTime = utcZonedDateTime.format(outputFormatter)

        return formattedDateTime
    }

    fun combineDateTime(date: String, time: String): String {
        // Parse the date and time strings
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mma")

        val parsedDate = LocalDate.parse(date, dateFormatter)
        val parsedTime = LocalTime.parse(time, timeFormatter)

        // Combine date and time
        val dateTime = parsedDate.atTime(parsedTime)

        // Define the output formatter
        ///val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        ///val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

        // Format the combined date-time
        val formattedDateTime = dateTime.format(outputFormatter)

        return formattedDateTime//.toLowerCase()  // Convert AM/PM to lowercase as per your requirement
    }

    fun formatTimeToLowercaseAMPM(inputTime: String): String {
        // Check if the input time ends with "AM" or "PM"
        return when {
            inputTime.endsWith("AM", ignoreCase = true) -> inputTime.replace("AM", "am")
            inputTime.endsWith("PM", ignoreCase = true) -> inputTime.replace("PM", "pm")
            else -> inputTime // Return the original string if it doesn't end with AM or PM
        }
    }

    fun convertUTCtoIST(utcFormat: String, istFormat: String, dateToFormat: String): String{
        val utcFormat: DateFormat = SimpleDateFormat(utcFormat)
        utcFormat.timeZone = TimeZone.getTimeZone("GMT")

        val indianFormat: DateFormat = SimpleDateFormat(istFormat)
        utcFormat.timeZone = TimeZone.getTimeZone("IST")

        val timestamp: Date = utcFormat.parse(dateToFormat)
        return indianFormat.format(timestamp)
    }


    fun getCurrentDateTime():String{
        val calendar = Calendar.getInstance();
        val dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.format(calendar.getTime());

    }

    fun getCurrentDate():String {
        val calendar = Calendar.getInstance();
        val dateFormat =  SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(calendar.getTime());

    }





    fun getCurrentYear(): Int {
        return LocalDate.now().year
    }

    fun convertMonthNumberToFullName(monthNumber: Int): String {
        val month = Month.of(monthNumber) // Convert MM to Month Enum
        return month.getDisplayName(TextStyle.FULL, Locale.ENGLISH) // Get full month name (MMMM)
    }

    fun extractYearFromDate(completionDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(completionDate, formatter)
        return date.year.toString()
    }

    fun extractMonthFromDate(completionDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(completionDate, formatter)
        return date.month.toString()
    }
*/
    fun saveLanguagePreference(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language_code", languageCode)
        editor.apply()
    }

    fun getSavedLanguagePreference(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language_code", "en") ?: "en" // Default to English
    }

    fun getSavedTcId(context: Context): String {
        return context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            .getString("TC_ID", "") ?: ""
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

/*
    fun saveFetchModuleToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("fetch_module_token", token).apply()
    }

    fun getFetchModuleToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("fetch_module_token", "") ?: ""
    }*/


    fun copyToClipboard(context: Context, text: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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
                            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeOn, null)
                        } else {
                            editText.inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeOff, null)
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

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
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
}