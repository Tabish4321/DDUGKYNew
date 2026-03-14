package com.deendayalproject.fragments.formfragment

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.model.request.RFQteamVerificationRequest
import com.deendayalproject.util.AppUtil

class RFQTeamFormFragment : BaseFormSrmRF() {

    override fun tvtitle() {
        super.tvtitle()
        binding.toolbar.tvTitle.text=getString(R.string.residential_facility_q_team)
    }

    override fun submitFinalForm() {
        super.submitFinalForm()
        val requestTcInfraReq = RFQteamVerificationRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            trainingCentre = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            basicInfoStatus = selectedRFBasicInformationApproval,
            basicInfoRemark = selectedRFBasicInformationRemarks.toString(),
            infraComplianceStatus = selectedInfrastctureDetailsComplainsApproval,
            infraComplianceRemark = selectedInfrastctureDetailsComplainsRemarks.toString(),
            livingAreaInfoStatus = selectedRFLevingAreaInformationApproval,
            livingAreaInfoRemark = selectedRFLevingAreaInformationRemarks.toString(),
            toiletStatus = selectedRFToiletApproval,
            toiletRemark = selectedRFToiletRemarks.toString(),
            nonLivingAreaStatus = selectedNonAreaInfoApproval,
            nonLivingAreaRemark = selectedRFNonLivingAreaRemarks.toString(),
            indoorGameStatus = selectedIndoorGameApproval,
            indoorGameRemark = selectedIndoorGameApprovalRemark.toString(),
            rfAvailableStatus = selectedResidintislFacilityApproval,
            rfAvailableRemark = selectedResidintislFacilityApprovalRemark.toString(),
            supportFacilityAvailableStatus = selectedResidintislSupportFacilityApproval,
            supportFacilityAvailableRemark = selectedResidintislSupportFacilityApprovalRemark.toString(),
            addToiletStatus = selectedRFToiletAdditionalSanctionApproval.toString(),
            addToiletRemark = selectedRFToiletAdditionalSanctionRemarks.toString(),
            facilityId = facilityId
        )
        viewModel.getFinalSubmitinsertRFQteamVerificationRequestData(requestTcInfraReq)
        collectFinalSubmitData()
        showProgress()
    }

    override fun collectFinalSubmitData() {
        super.collectFinalSubmitData()
        viewModel.insertRFQteamVerification.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgress()
                when (it.responseCode) {
                    200 -> {
                        val isAllModified = listOf(selectedRFBasicInformationApproval,selectedInfrastctureDetailsComplainsApproval,selectedRFLevingAreaInformationApproval,
                            selectedRFToiletApproval,selectedNonAreaInfoApproval,selectedIndoorGameApproval,selectedResidintislFacilityApproval,selectedResidintislSupportFacilityApproval).all { status -> status == "M" }
                        val message = if (isAllModified) {
                            "Send to Operation Team Successfully!!"
                        } else {
                            "Saved Successfully!!"
                        }
                        toast(message)
                        findNavController().navigateUp()
                    }

                    202 -> toast(it.responseDesc ?: "No data available.")
                    301 -> toast("Please upgrade your app.")
                    401 -> AppUtil.showSessionExpiredDialog(findNavController(), requireContext())
                }
            }
            result.onFailure {
                hideProgress()
                Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
