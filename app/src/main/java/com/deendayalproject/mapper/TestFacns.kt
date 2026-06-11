package com.deendayalproject.mapper

//data class FansCountReq(
//    val appVersion: String="1.1.3",
//    val fansAttachment: String,
//    val detObject:String)

//enum class ObjectType {
//    LIGHT,
//    FAN,
//    TABLE,
//    BIOMETRIC_DEVICES,
//    CHAIR,
//    HEADS
//}

//fun resizeBitmap(bitmap: Bitmap, maxSize: Int = 1080): Bitmap {
//    val ratio = minOf(
//        maxSize.toFloat() / bitmap.width,
//        maxSize.toFloat() / bitmap.height
//    )
//    return Bitmap.createScaledBitmap(
//        bitmap,
//        (bitmap.width * ratio).toInt(),
//        (bitmap.height * ratio).toInt(),
//        true
//    )
//}


//@HiltViewModel
//class AIObjectDetectionViewModel @Inject constructor(
//    private val repository: FansRepository
//) : ViewModel() {
//
//    private val _fansState =
//        MutableStateFlow<Resource<FansCountRes>>(Resource.loading(null))
//    val fansState: StateFlow<Resource<FansCountRes>> = _fansState
//
//    fun getFansCount(request: FansCountReq) {
//        // if (_fansState.value.status == Status.LOADING) return
//
//        viewModelScope.launch {
//
//            Log.d("AI_API", "Calling getFansCount API")
//
//            _fansState.value = Resource.loading(null)
//
//            val response = repository.getFansCount(request)
//
//            _fansState.value = response
//        }
//    }
//
//    fun resetFansState() {
//        _fansState.value = Resource.success(null)
//    }
//}


//@POST("getFansCount")
//suspend fun getFansCountAPI(
//    @Body fansCountReq: FansCountReq
//): Response<FansCountRes>
