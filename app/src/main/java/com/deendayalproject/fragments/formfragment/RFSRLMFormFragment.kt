package com.deendayalproject.fragments.formfragment

import android.view.View
import androidx.navigation.fragment.findNavController
import com.deendayalproject.BuildConfig
import com.deendayalproject.R
import com.deendayalproject.model.request.RFQteamVerificationRequest
import com.deendayalproject.util.AppUtil

class RFSRLMFormFragment : BaseFormSrmRF() {

    override fun tvtitle() {
        //super.tvtitle()
        binding.toolbar.tvTitle.text=getString(R.string.residential_facility_srlm)
        binding.toolbar.profilePic.visibility= View.GONE
    }

    override  fun submitFinalForm() {
        super.submitFinalForm()
        val requestTcInfraReq = RFQteamVerificationRequest(
            appVersion = BuildConfig.VERSION_NAME,
            loginId = AppUtil.getSavedLoginIdPreference(requireContext()),
            trainingCentre = centerId.toInt(),
            sanctionOrder = sanctionOrder,
            imeiNo = AppUtil.getAndroidId(requireContext()),
            basicInfoStatus = selectedRFBasicInformationApproval,
            basicInfoRemark = selectedRFBasicInformationRemarks,
            infraComplianceStatus = selectedInfrastctureDetailsComplainsApproval,
            infraComplianceRemark = selectedInfrastctureDetailsComplainsRemarks,
            livingAreaInfoStatus = selectedRFLevingAreaInformationApproval,
            livingAreaInfoRemark = selectedRFLevingAreaInformationRemarks,
            toiletStatus = selectedRFToiletApproval,
            toiletRemark = selectedRFToiletRemarks,
            nonLivingAreaStatus = selectedNonAreaInfoApproval,
            nonLivingAreaRemark = selectedRFNonLivingAreaRemarks,
            indoorGameStatus = selectedIndoorGameApproval,
            indoorGameRemark = selectedIndoorGameApprovalRemark,
            rfAvailableStatus = selectedResidintislFacilityApproval,
            rfAvailableRemark = selectedResidintislFacilityApprovalRemark,
            supportFacilityAvailableStatus = selectedResidintislSupportFacilityApproval,
            supportFacilityAvailableRemark = selectedResidintislSupportFacilityApprovalRemark,
            addToiletStatus = selectedRFToiletAdditionalSanctionApproval,
            addToiletRemark = selectedRFToiletAdditionalSanctionRemarks,
            facilityId = facilityId
        )
        viewModel.getFinalSubmitinsertRFinsertRFSrlmVerificationRequestData(requestTcInfraReq)
        collectFinalSubmitData()
        showProgress()
    }

    override fun collectFinalSubmitData() {
        super.collectFinalSubmitData()
        viewModel.insertRFSrlmVerification.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                hideProgress()
                when (it.responseCode) {
                    200 -> {
                        val isAllModified = listOf(
                            selectedRFBasicInformationApproval,
                            selectedInfrastctureDetailsComplainsApproval,
                            selectedRFLevingAreaInformationApproval,
                            selectedRFToiletApproval,
                            selectedNonAreaInfoApproval,
                            selectedIndoorGameApproval,
                            selectedResidintislFacilityApproval,
                            selectedResidintislSupportFacilityApproval
                        ).all { status -> status == "M" }

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
                toast("Failed: ${it.message}")
            }
        }
    }


}