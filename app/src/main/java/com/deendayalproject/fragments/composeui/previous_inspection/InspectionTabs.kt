package com.deendayalproject.fragments.composeui.previous_inspection


enum class InspectionTab(
    val title: String,
    val sectionKey: String
) {

    TRAINING("Training Quality", "TrainingQuality"),
    TRAINER("Trainer Attendance", "ValidateTrainerAttendance"),
    PREVIOUS("Previous Batch", "PreviousBatchDataVerification"),
    ONGOING("Ongoing Batch", "OngoingBatchCandidateVerification"),
    DOCUMENTS("Standard Forms", "DocumentsStandardFormsAvailability"),
}