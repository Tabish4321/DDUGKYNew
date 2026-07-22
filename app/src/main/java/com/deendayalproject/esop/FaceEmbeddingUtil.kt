package com.deendayalproject.esop


import kotlin.math.sqrt

/**
 * Wraps a MobileFaceNet-style TFLite model to turn a cropped face bitmap
 * into a fixed-length embedding vector.
 *
 * SETUP:
 * 1. Put your model file (e.g. "mobile_face_net.tflite") in app/src/main/assets/
 * 2. Match INPUT_SIZE / EMBEDDING_SIZE below to your model's actual input/output shape.
 */
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.math.sqrt

/**
 * Wraps a MobileFaceNet-style TFLite model to turn a cropped face bitmap
 * into a fixed-length embedding vector.
 *
 * SETUP:
 * 1. Put your model file (e.g. "mobile_face_net.tflite") in app/src/main/assets/
 * 2. Match INPUT_SIZE / EMBEDDING_SIZE below to your model's actual input/output shape.
 */
class FaceEmbeddingUtil(context: Context) {

    companion object {
        private const val MODEL_FILE = "mobile_face_net.tflite"
        private const val INPUT_SIZE = 112
        const val EMBEDDING_SIZE = 192
        // Cosine similarity threshold above which two faces are considered "same person".
        // Tune this using a few test captures; 0.75-0.85 is a common starting range.
        const val MATCH_THRESHOLD = 0.75f
    }

    private var interpreter: Interpreter? = null

    /** True only if the .tflite model was found in assets/ and loaded successfully. */
    var isReady: Boolean = false
        private set

    init {
        // IMPORTANT: this must never throw, or the whole app crashes on fragment
        // creation whenever the model file is missing/renamed/corrupted.
        try {
            val afd: AssetFileDescriptor = context.assets.openFd(MODEL_FILE)
            val inputStream = afd.createInputStream()
            val channel = inputStream.channel
            val modelBuffer = channel.map(
                FileChannel.MapMode.READ_ONLY,
                afd.startOffset,
                afd.declaredLength
            )
            interpreter = Interpreter(modelBuffer)
            isReady = true
        } catch (e: Exception) {
            // Model missing from app/src/main/assets/ (or wrong format). Log and
            // continue without crashing — callers must handle getEmbedding() == null.
            e.printStackTrace()
            interpreter = null
            isReady = false
        }
    }

    /** Returns null if the model failed to load — callers must handle this case. */
    fun getEmbedding(faceBitmap: Bitmap): FloatArray? {
        val model = interpreter ?: return null
        return try {
            val resized = Bitmap.createScaledBitmap(faceBitmap, INPUT_SIZE, INPUT_SIZE, true)
            val input = bitmapToByteBuffer(resized)
            val output = Array(1) { FloatArray(EMBEDDING_SIZE) }
            model.run(input, output)
            normalize(output[0])
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * INPUT_SIZE * INPUT_SIZE * 3)
        buffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
        bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)
        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 127.5f - 1f
            val g = (pixel shr 8 and 0xFF) / 127.5f - 1f
            val b = (pixel and 0xFF) / 127.5f - 1f
            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }
        return buffer
    }

    private fun normalize(vector: FloatArray): FloatArray {
        var norm = 0f
        for (v in vector) norm += v * v
        norm = sqrt(norm)
        return if (norm == 0f) vector else FloatArray(vector.size) { vector[it] / norm }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}

/** Both vectors must already be L2-normalized (getEmbedding() does this). */
fun cosineSimilarity(a: FloatArray, b: FloatArray): Float {
    var dot = 0f
    for (i in a.indices) dot += a[i] * b[i]
    return dot
}

