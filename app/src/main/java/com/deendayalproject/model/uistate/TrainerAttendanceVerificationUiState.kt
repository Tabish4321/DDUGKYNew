package com.deendayalproject.model.uistate


data class TrainerAttendanceVerificationUiState(

    val answers: MutableMap<Int, String?> = mutableMapOf(),
    val remarks: MutableMap<Int, String> = mutableMapOf(),

    val isLoading: Boolean = false,
    val showValidation: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

fun TrainerAttendanceVerificationUiState.getAnswer(id: Int): String? {
    return answers[id]
}

fun TrainerAttendanceVerificationUiState.getRemarks(id: Int): String {
    return remarks[id] ?: ""
}

fun TrainerAttendanceVerificationUiState.updateAnswer(id: Int, value: String): TrainerAttendanceVerificationUiState {
    val newAnswers = answers.toMutableMap()
    newAnswers[id] = value
    return copy(answers = newAnswers)
}

fun TrainerAttendanceVerificationUiState.updateRemarks(id: Int, value: String): TrainerAttendanceVerificationUiState {
    val newRemarks = remarks.toMutableMap()
    newRemarks[id] = value
    return copy(remarks = newRemarks)
}