package com.deendayalproject.esop.exam

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import kotlin.math.sqrt
object FaceVerificationUtils {

    /** Simple data object forwarded to callers to avoid exposing ML Kit types to other modules */
    data class FaceData(
        val leftEyeOpenProbability: Float?,
        val rightEyeOpenProbability: Float?,
        val trackingId: Int?
    )

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onReady: (ImageCapture) -> Unit
    )




    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageCapture
                )

                onReady(imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Camera Start Failed", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun captureImage(
        imageCapture: ImageCapture?,
        cameraExecutor: ExecutorService,
        context: Context,
        onBitmapReady: (Bitmap) -> Unit
    ) {
        if (imageCapture == null) {
            Toast.makeText(context, "Camera Not Ready", Toast.LENGTH_SHORT).show()
            return
        }

        imageCapture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    try {
                        onBitmapReady(imageProxyToBitmap(image))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Toast.makeText(context, "Capture Failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun createEmbedding(bitmap: Bitmap): FloatArray {
        val resized = Bitmap.createScaledBitmap(bitmap, 32, 32, true)
        val embedding = FloatArray(32 * 32)

        var index = 0

        for (x in 0 until 32) {
            for (y in 0 until 32) {
                val pixel = resized.getPixel(x, y)

                val r = (pixel shr 16) and 0xff
                val g = (pixel shr 8) and 0xff
                val b = pixel and 0xff

                embedding[index++] = (r + g + b) / 3f
            }
        }

        return embedding
    }
    private fun compareEmbeddings(
        emb1: FloatArray,
        emb2: FloatArray
    ): Float {

        var sum = 0f

        for (i in emb1.indices) {

            val diff =
                emb1[i] - emb2[i]

            sum += diff * diff
        }

        return sqrt(sum)
    }
//    fun compareEmbeddings(
//    oldFace: FloatArray,
//    newFace: FloatArray?
//    ): Float {
//        var sum = 0f
//
//        for (i in oldFace.indices) {
//            val diff = oldFace[i] - newFace[i]
//            sum += diff * diff
//        }
//
//        return sqrt(sum)
//    }

    /**
     * Starts camera preview + image capture + image analysis (ML Kit face detection).
     * onReady returns the ImageCapture instance so callers can capture stills.
     * onFace is called for each detected Face (from ML Kit).
     */
    @OptIn(ExperimentalGetImage::class)
    fun startCameraWithAnalyzer(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onReady: (ImageCapture) -> Unit,
        onFace: (FaceData) -> Unit
    )
    {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .enableTracking()
            .build()

        val detector = FaceDetection.getClient(options)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    detector.process(image)
                        .addOnSuccessListener { faces ->
                            faces.forEach { face ->
                                try {
                                    // map ML Kit Face to our lightweight FaceData so callers don't need ML Kit types
                                    val fd = FaceData(
                                        leftEyeOpenProbability = face.leftEyeOpenProbability,
                                        rightEyeOpenProbability = face.rightEyeOpenProbability,
                                        trackingId = face.trackingId
                                    )
                                    onFace(fd)
                                } catch (e: Exception) {
                                    // swallow exceptions from caller analyzer
                                    e.printStackTrace()
                                }
                            }
                        }
                        .addOnFailureListener { }
                        .addOnCompleteListener { imageProxy.close() }
                } else {
                    imageProxy.close()
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageCapture,
                    imageAnalysis
                )

                onReady(imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Camera Start Failed", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(context))
    }
}

@Composable
fun VibrateWhileDialogVisible(
    visible: Boolean
) {
    val context = LocalContext.current

    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 500, 300),
                        0
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(
                    longArrayOf(0, 500, 300),
                    0
                )
            }
        } else {
            vibrator.cancel()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            vibrator.cancel()
        }
    }
}







//object FaceVerificationUtils {
//
//    /** Simple data object forwarded to callers to avoid exposing ML Kit types to other modules */
//    data class FaceData(
//        val leftEyeOpenProbability: Float?,
//        val rightEyeOpenProbability: Float?,
//        val trackingId: Int?
//    )
//
//    fun startCamera(
//        context: Context,
//        lifecycleOwner: LifecycleOwner,
//        previewView: PreviewView,
//        onReady: (ImageCapture) -> Unit
//    )
//
//
//
//
//    {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(previewView.surfaceProvider)
//            }
//
//            val imageCapture = ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build()
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    CameraSelector.DEFAULT_FRONT_CAMERA,
//                    preview,
//                    imageCapture
//                )
//
//                onReady(imageCapture)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "Camera Start Failed", Toast.LENGTH_SHORT).show()
//            }
//        }, ContextCompat.getMainExecutor(context))
//    }
//
//    fun captureImage(
//        imageCapture: ImageCapture?,
//        cameraExecutor: ExecutorService,
//        context: Context,
//        onBitmapReady: (Bitmap) -> Unit
//    ) {
//        if (imageCapture == null) {
//            Toast.makeText(context, "Camera Not Ready", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        imageCapture.takePicture(
//            cameraExecutor,
//            object : ImageCapture.OnImageCapturedCallback() {
//                override fun onCaptureSuccess(image: ImageProxy) {
//                    try {
//                        onBitmapReady(imageProxyToBitmap(image))
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    } finally {
//                        image.close()
//                    }
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    exception.printStackTrace()
//                    Toast.makeText(context, "Capture Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }
//
//    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
//        val buffer: ByteBuffer = image.planes[0].buffer
//        val bytes = ByteArray(buffer.remaining())
//        buffer.get(bytes)
//
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }
//
//    fun createEmbedding(bitmap: Bitmap): FloatArray {
//        val resized = Bitmap.createScaledBitmap(bitmap, 32, 32, true)
//        val embedding = FloatArray(32 * 32)
//
//        var index = 0
//
//        for (x in 0 until 32) {
//            for (y in 0 until 32) {
//                val pixel = resized.getPixel(x, y)
//
//                val r = (pixel shr 16) and 0xff
//                val g = (pixel shr 8) and 0xff
//                val b = pixel and 0xff
//
//                embedding[index++] = (r + g + b) / 3f
//            }
//        }
//
//        return embedding
//    }
//    private fun compareEmbeddings(
//        emb1: FloatArray,
//        emb2: FloatArray
//    ): Float {
//
//        var sum = 0f
//
//        for (i in emb1.indices) {
//
//            val diff =
//                emb1[i] - emb2[i]
//
//            sum += diff * diff
//        }
//
//        return sqrt(sum)
//    }
////    fun compareEmbeddings(
////        oldFace: FloatArray,
////        newFace: FloatArray
////    ): Float {
////        var sum = 0f
////
////        for (i in oldFace.indices) {
////            val diff = oldFace[i] - newFace[i]
////            sum += diff * diff
////        }
////
////        return sqrt(sum)
////    }
//
//    /**
//     * Starts camera preview + image capture + image analysis (ML Kit face detection).
//     * onReady returns the ImageCapture instance so callers can capture stills.
//     * onFace is called for each detected Face (from ML Kit).
//     */
//    fun startCameraWithAnalyzer(
//        context: Context,
//        lifecycleOwner: androidx.lifecycle.LifecycleOwner,
//        previewView: PreviewView,
//        onReady: (ImageCapture) -> Unit,
//        onFace: (FaceData) -> Unit
//    ) {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//
//        val options = FaceDetectorOptions.Builder()
//            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
//            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
//            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
//            .enableTracking()
//            .build()
//
//        val detector = FaceDetection.getClient(options)
//
//        cameraProviderFuture.addListener({
//            val cameraProvider = cameraProviderFuture.get()
//
//            val preview = Preview.Builder().build().also {
//                it.setSurfaceProvider(previewView.surfaceProvider)
//            }
//
//            val imageCapture = ImageCapture.Builder()
//                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .build()
//
//            val imageAnalysis = ImageAnalysis.Builder()
//                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                .build()
//
//            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
//                val mediaImage = imageProxy.image
//                if (mediaImage != null) {
//                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//                    detector.process(image)
//                        .addOnSuccessListener { faces ->
//                            faces.forEach { face ->
//                                try {
//                                    // map ML Kit Face to our lightweight FaceData so callers don't need ML Kit types
//                                    val fd = FaceData(
//                                        leftEyeOpenProbability = face.leftEyeOpenProbability,
//                                        rightEyeOpenProbability = face.rightEyeOpenProbability,
//                                        trackingId = face.trackingId
//                                    )
//                                    onFace(fd)
//                                } catch (e: Exception) {
//                                    // swallow exceptions from caller analyzer
//                                    e.printStackTrace()
//                                }
//                            }
//                        }
//                        .addOnFailureListener { }
//                        .addOnCompleteListener { imageProxy.close() }
//                } else {
//                    imageProxy.close()
//                }
//            }
//
//            try {
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    CameraSelector.DEFAULT_FRONT_CAMERA,
//                    preview,
//                    imageCapture,
//                    imageAnalysis
//                )
//
//                onReady(imageCapture)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "Camera Start Failed", Toast.LENGTH_SHORT).show()
//            }
//
//        }, ContextCompat.getMainExecutor(context))
//    }
//}
//
//@Composable
//fun VibrateWhileDialogVisible(
//    visible: Boolean
//) {
//    val context = LocalContext.current
//
//    val vibrator = remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val manager =
//                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//            manager.defaultVibrator
//        } else {
//            @Suppress("DEPRECATION")
//            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        }
//    }
//
//    LaunchedEffect(visible) {
//        if (visible) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                vibrator.vibrate(
//                    VibrationEffect.createWaveform(
//                        longArrayOf(0, 500, 300),
//                        0
//                    )
//                )
//            } else {
//                @Suppress("DEPRECATION")
//                vibrator.vibrate(
//                    longArrayOf(0, 500, 300),
//                    0
//                )
//            }
//        } else {
//            vibrator.cancel()
//        }
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            vibrator.cancel()
//        }
//    }
//}