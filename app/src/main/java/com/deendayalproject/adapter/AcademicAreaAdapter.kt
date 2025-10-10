package com.deendayalproject.adapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.model.request.AcademicNonAcademicArea
import com.deendayalproject.model.response.wrappedList
import com.deendayalproject.util.AppUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
class AcademicAreaAdapter(
    private var centers: MutableList<wrappedList>,
) : RecyclerView.Adapter<AcademicAreaAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomtype: TextView = view.findViewById(R.id.roomtype)
        val roomno: TextView = view.findViewById(R.id.roomno)
        val roomlength: TextView = view.findViewById(R.id.roomlength)
        val roomarea: TextView = view.findViewById(R.id.roomarea)
        val delete: ImageView = view.findViewById(R.id.ImageViewDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_academic_area, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]
        holder.roomtype.text = center.roomType
        holder.roomno.text = center.roomNo
//        holder.roomlength.text = center.roomLength
        holder.roomarea.text = center.roomArea

        holder.delete.setOnClickListener {
            val context = holder.itemView.context
            val loginId = AppUtil.getSavedLoginIdPreference(context)
            val imeiNo = AppUtil.getAndroidId(context)
            val appVersion = BuildConfig.VERSION_NAME
            val tcId = AppUtil.getcenterIdPreference(context)
            val sanctionOrder = AppUtil.getsanctionOrderPreference(context)
            val token = AppUtil.getSavedTokenPreference(context)
            val roomNo = center.roomNo
            val roomType = center.roomType





            AlertDialog.Builder(context)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete RoomNo = $roomNo?")
                .setPositiveButton("Yes") { _, _ ->
                    deleteAcademicRoomApi(
                        loginId,
                        imeiNo,
                        appVersion,
                        tcId,
                        sanctionOrder,
                        roomNo,
                        roomType,
                        token,
                        context,
                        position
                    )
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<wrappedList>) {
        centers.clear()
        centers.addAll(newList)
        notifyDataSetChanged()
    }

    // 🔥 DELETE API FUNCTION INSIDE ADAPTER
    private fun deleteAcademicRoomApi(
        loginId: String,
        imeiNo: String,
        appVersion: String,
        tcId: String,
        sanctionOrder: String,
        roomNo: String,
        roomType: String,
        token: String,
        context: Context,
        position: Int
    ) {
        Thread {
            try {
                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()

                val jsonBody = """
                    {
                        "loginId": "$loginId",
                        "imeiNo": "$imeiNo",
                        "appVersion": "$appVersion",
                        "tcId": "$tcId",
                        "sanctionOrder": "$sanctionOrder",
                        "roomType": "$roomType",
                        "roomNo": ${roomNo.toIntOrNull() ?: 0}
                    }
                """.trimIndent()

                val body = jsonBody.toRequestBody(mediaType)
                val request = Request.Builder()
                    .url("https://kaushal.rural.gov.in/backend/ddugkyapp/deleteAcademicRoom")
                    .post(body)
                    .addHeader("ddugkyappauth", "Bearer $token")
                    .addHeader("Content-Type", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                val activity = context as? Activity

                if (response.isSuccessful) {
                    Log.d("DeleteAPI", "✅ Delete success: $responseBody")
                    activity?.runOnUiThread {
                        // ✅ Remove the deleted item immediately from list
                        if (position in centers.indices) {
                            centers.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, centers.size)
                            Toast.makeText(context, "Deleted Room $roomNo", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("DeleteAPI", "❌ Failed: ${response.code} | Body: $responseBody")
                    activity?.runOnUiThread {
                        Toast.makeText(context, "Delete failed (${response.code})", Toast.LENGTH_SHORT).show()
                    }
                }

                response.close()
            } catch (e: Exception) {
                Log.e("DeleteAPI", "❌ Exception: ${e.message}", e)
                val activity = context as? Activity
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}









//class AcademicAreaAdapter(
//    private var centers: List<wrappedList>,
//) : RecyclerView.Adapter<AcademicAreaAdapter.CenterViewHolder>() {
//    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val roomtype: TextView = view.findViewById(R.id.roomtype)
//        val roomno: TextView = view.findViewById(R.id.roomno)
//        val roomlength: TextView = view.findViewById(R.id.roomlength)
//        val roomarea: TextView = view.findViewById(R.id.roomarea)
//        val delete: ImageView = view.findViewById(R.id.ImageViewDelete)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_academic_area, parent, false)
//        return CenterViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
//        val center = centers[position]
//        holder.roomtype.text = center.roomType
//        holder.roomno.text = center.roomNo
//        holder.roomlength.text = center.roomLength
//        holder.roomarea.text = center.roomArea
//        holder.delete.setOnClickListener {
//            val request = AcademicNonAcademicArea(
//                loginId = AppUtil.getSavedLoginIdPreference(holder.itemView.context),
//                imeiNo = AppUtil.getAndroidId(holder.itemView.context),
//                appVersion = BuildConfig.VERSION_NAME,
//                tcId = AppUtil.getcenterIdPreference(holder.itemView.context),
//                sanctionOrder = AppUtil.getsanctionOrderPreference(holder.itemView.context),
//            )
//            val loginId = AppUtil.getSavedLoginIdPreference(holder.itemView.context)
//            val imeiNo = AppUtil.getAndroidId(holder.itemView.context)
//            val appVersion = BuildConfig.VERSION_NAME
//            val tcId = AppUtil.getcenterIdPreference(holder.itemView.context)
//            val sanctionOrder = AppUtil.getsanctionOrderPreference(holder.itemView.context)
//            val Token = AppUtil.getSavedTokenPreference(holder.itemView.context)
//            val roomNo = center.roomNo
//            val roomType = center.roomType
//            AlertDialog.Builder(holder.itemView.context)
//                .setTitle("Confirm Delete")
//                .setMessage("Are you sure you want to delete RoomNo = ${roomNo}?")
//                .setPositiveButton("Yes") { _, _ ->
//                    deleteAcademicRoomApi(loginId,imeiNo,appVersion,tcId,sanctionOrder,roomNo,roomType,Token,holder.itemView.context,position)
//                }
//                .setNegativeButton("No", null)
//                .show()
//        }
//
//    }
//    override fun getItemCount(): Int = centers.size
//    fun updateData(newList: List<wrappedList>) {
//        centers = newList
//        notifyDataSetChanged()
//    }
//    // 🔥 DELETE API FUNCTION INSIDE ADAPTER
//    private fun deleteAcademicRoomApi(
//        loginId: String,
//        imeiNo: String,
//        appVersion: String,
//        tcId: String,
//        sanctionOrder: String,
//        roomNo: String,
//        roomType: String,
//        Token: String,
//        contexts: Context,
//        postion:Int
//    ) {
//        val context = contexts
//        Thread {
//            try {
//                val client = OkHttpClient()
//                val mediaType = "application/json".toMediaType()
//
//                // ✅ Build clean JSON body — matches API exactly
//                val jsonBody = """
//                {
//                    "loginId": "${loginId}",
//                    "imeiNo": "${imeiNo}",
//                    "appVersion": "${appVersion}",
//                    "tcId": "${tcId}",
//                    "sanctionOrder": "${sanctionOrder}",
//                    "roomType": "${roomType}",
//                    "roomNo": ${roomNo.toIntOrNull() ?: 0}
//                }
//            """.trimIndent()
//
//                Log.d("DeleteAPI", "Request JSON: $jsonBody")
//
//                val body = jsonBody.toRequestBody(mediaType)
//
//                val request = Request.Builder()
//                    .url("https://kaushal.rural.gov.in/backend/ddugkyapp/deleteAcademicRoom")
//                    .post(body)
//                    .addHeader(
//                        "ddugkyappauth",
//                        "Bearer ${Token}"
//                    )
//                    .addHeader("Content-Type", "application/json")
//                    .build()
//
//                val response = client.newCall(request).execute()
//                val responseBody = response.body?.string()
//
//                val activity = context as? android.app.Activity
//                if (response.isSuccessful) {
//                    Log.d("DeleteAPI", "✅ Delete success: $responseBody")
//                    activity?.runOnUiThread {
//                        if (postion in centers.indices) {
//                            notifyDataSetChanged()
//                            Toast.makeText(context, "Deleted Room ${roomNo}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Log.e("DeleteAPI", "❌ Failed: ${response.code} | Body: $responseBody")
//
//                    activity?.runOnUiThread {
//                        Toast.makeText(context, "Delete failed (${response.code})", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                response.close()
//            } catch (e: Exception) {
//                Log.e("DeleteAPI", "❌ Exception: ${e.message}", e)
//                val activity = context as? android.app.Activity
//                activity?.runOnUiThread {
//                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }.start()
//    }
//}



