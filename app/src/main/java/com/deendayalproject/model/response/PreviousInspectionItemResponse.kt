

data class PreviousInspectionItemResponse(
    val id: Int,
    val date: String,
    val conductedBy: String,
    val observations: String,
    val actionTaken: String,
    val remarks: String,
    val complianceStatus: String
)
