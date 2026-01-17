package com.deendayalproject.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deendayalproject.databinding.AttendanceCandidateListLayoutBinding
import com.deendayalproject.model.response.Candidate
import com.deendayalproject.util.AppUtil
import com.deendayalproject.R
import com.deendayalproject.fragments.AttendanceCandidateListFragmentDirections


class AttendanceCandidateAdapter(
    private val candidateList: List<Candidate>,
    private val candidateRegNo: String = ""
) : RecyclerView.Adapter<AttendanceCandidateAdapter.CandidateViewHolder>() {



    var candidateId=""
    var rollNo=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding =
            AttendanceCandidateListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CandidateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidateList[position]
        holder.bind(candidate)
    }

    override fun getItemCount(): Int = candidateList.size

    inner class CandidateViewHolder(private val binding: AttendanceCandidateListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(candidate: Candidate) {

            val context = binding.root.context

            // Handle Profile Picture
            val profilePic = candidate.candidateProfilePic ?: "NA"

            if (profilePic == "NA" || profilePic.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.person)
                    .into(binding.candidateImage)
            }  else {
                try {
                    val decodedString: ByteArray = Base64.decode(profilePic, Base64.DEFAULT)
                    val profileBitmap: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                    Glide.with(context)
                        .load(profileBitmap)
                        .into(binding.candidateImage)
                } catch (e: Exception) {
                    Glide.with(context)
                        .load(R.drawable.person) // Load default image if decoding fails
                        .into(binding.candidateImage)
                }
            }

            // Ensure non-null values for UI fields
            binding.tvCandidateName.text = candidate.candidateName ?: "Unknown"
            binding.tvRollNumberValue.text = candidate.rollNo.toString() ?: "N/A"
            binding.tvContactNumber.text = candidate.mobileNo ?: "N/A"
            binding.tvDate.text= AppUtil.getCurrentDateForAttendance()
            candidateId= candidate.candidateId
            rollNo= candidate.rollNo.toString()



            // Handle Click Navigation (Ensure safe `adapterPosition`)
            binding.btnMarkAttendance.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && position < candidateList.size) {
                    var dp = ""
                    if (candidate.candidateProfilePic != null){

                       dp = candidate.candidateProfilePic
                    }
                    else dp = ""


                    val action = AttendanceCandidateListFragmentDirections
                        .actionAttendanceCandidateListFragmentToCandidateAttendanceFragment(candidate.candidateId,candidate.candidateName,candidate.mobileNo,candidate.emailId
                            ,candidate.gender,candidate.dateOfBirth,dp,
                            candidate.batchId.toString(),candidateRegNo,candidate.rollNo.toString(),candidate.adhaarNo
                        )

                    binding.root.findNavController().navigate(action)
                }
            }

        }
    }
}