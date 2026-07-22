package com.deendayalproject.esop

/**
 * Wraps a MobileFaceNet-style TFLite model to turn a cropped face bitmap
 * into a fixed-length embedding vector.
 *
 * SETUP:
 * 1. Put your model file (e.g. "mobile_face_net.tflite") in app/src/main/assets/
 * 2. Match INPUT_SIZE / EMBEDDING_SIZE below to your model's actual input/output shape.
 */


import android.graphics.Bitmap
import android.graphics.Rect
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

/**
 * Runs the FRONT camera continuously.
 *
 * @param previewVisible true = show a visible preview (use during enrollment),
 *                        false = run camera invisibly in the background (use during exam monitoring).
 * @param onFaceFrame     called (throttled by ML Kit's own processing speed) whenever at least
 *                        one face is found, with a cropped bitmap of the FIRST face, its
 *                        left/right eye-open probabilities, and the TOTAL number of faces
 *                        found in that frame (use this to detect "someone else appeared").
 * @param onNoFaceDetected called when no face is found in a frame.
 * @param checkIntervalMs  minimum time between two processed frames. Running full face
 *                         detection (and, in the caller, TFLite embedding extraction) on
 *                         EVERY camera frame (15-30 fps) is expensive and can make the whole
 *                         screen janky/unresponsive. Default is 2000ms — a "check every
 *                         2 seconds" cadence, which is enough for continuous monitoring
 *                         through the whole exam without overloading the device.
 */
@Composable
fun FaceCameraAnalyzer(
    previewVisible: Boolean,
    modifier: Modifier = Modifier,
    checkIntervalMs: Long = 2000L,
    onFaceFrame: (Bitmap, leftEyeOpenProb: Float?, rightEyeOpenProb: Float?, faceCount: Int) -> Unit,
    onNoFaceDetected: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    val detector = remember {
        FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        )
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()

                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    var lastProcessedAt = 0L

                    analysis.setAnalyzer(executor) { imageProxy ->
                        val now = System.currentTimeMillis()
                        if (now - lastProcessedAt < checkIntervalMs) {
                            // Not time for the next check yet — must still close the
                            // frame immediately or the camera pipeline stalls.
                            imageProxy.close()
                        } else {
                            lastProcessedAt = now
                            processFrame(imageProxy, detector, onFaceFrame, onNoFaceDetected)
                        }
                    }

                    cameraProvider.unbindAll()

                    if (previewVisible) {
                        // Visible mode: bind Preview (with its surface) + analysis.
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview,
                            analysis
                        )
                    } else {
                        // Background monitoring mode: bind ONLY analysis, no Preview
                        // use-case at all (an unattached Preview surface can crash
                        // or freeze frame delivery on some devices).
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            analysis
                        )
                    }
                } catch (e: Exception) {
                    // Camera may be busy/unavailable (e.g. permission revoked, or
                    // already bound elsewhere) — don't crash the screen for it.
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

@OptIn(ExperimentalGetImage::class)
private fun processFrame(
    imageProxy: ImageProxy,
    detector: FaceDetector,
    onFaceFrame: (Bitmap, Float?, Float?, Int) -> Unit,
    onNoFaceDetected: () -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage == null) {
        imageProxy.close()
        return
    }

    val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    detector.process(inputImage)
        .addOnSuccessListener { faces ->
            if (faces.isEmpty()) {
                onNoFaceDetected()
            } else {
                val face = faces[0]
                imageProxy.toBitmapCropped(face.boundingBox)?.let { bmp ->
                    onFaceFrame(bmp, face.leftEyeOpenProbability, face.rightEyeOpenProbability, faces.size)
                }
            }
        }
        .addOnFailureListener { it.printStackTrace() }
        .addOnCompleteListener { imageProxy.close() }
}

// Minimal YUV_420_888 -> JPEG -> Bitmap conversion + crop to the detected face box.
// For a production app, swap this for a well-tested YUV->RGB converter (this is
// intentionally kept self-contained/dependency-free).
private fun ImageProxy.toBitmapCropped(box: Rect): Bitmap? {
    return try {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = android.graphics.YuvImage(
            nv21, android.graphics.ImageFormat.NV21, width, height, null
        )
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 90, out)
        val bytes = out.toByteArray()
        val full = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

        val safeBox = Rect(
            box.left.coerceIn(0, full.width - 1),
            box.top.coerceIn(0, full.height - 1),
            box.right.coerceIn(1, full.width),
            box.bottom.coerceIn(1, full.height)
        )

        Bitmap.createBitmap(
            full,
            safeBox.left,
            safeBox.top,
            safeBox.width().coerceAtLeast(1),
            safeBox.height().coerceAtLeast(1)
        )
    } catch (e: Exception) {
        null
    }
}