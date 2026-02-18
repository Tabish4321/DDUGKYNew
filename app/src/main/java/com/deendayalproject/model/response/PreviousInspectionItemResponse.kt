

data class PreviousInspectionItemResponse(
    val id: Int,
    val date: String,
    val conductedBy: String,
    val type: String    // inspection or due delegence
)
