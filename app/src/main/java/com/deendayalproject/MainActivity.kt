package com.deendayalproject
import android.graphics.Color
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.view.TextureView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import com.deendayalproject.base.BaseActivity
import com.deendayalproject.util.FullScreenHelper
import com.deendayalproject.util.validateDeviceSecurity
//import com.deendayalproject.util.validateDeviceSecurity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
//        disableScreenshots()
    }

    override fun onResume() {
        super.onResume()
        if (!validateDeviceSecurity(this)) {
            return
        }
    }

    private fun disableScreenshots() {
        this.window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}











//Testing


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var textureView: TextureView
//    private lateinit var btnRecord: Button
//    private lateinit var btnStop: Button
//    private lateinit var tvTimer: TextView
//    private lateinit var videoView: VideoView
//    private lateinit var controlLayout: View
//
//    private var mediaRecorder: MediaRecorder? = null
//    private var outputFile: String? = null
//    private var isRecording = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.video_activity_main)
//
//        textureView = findViewById(R.id.textureView)
//        btnRecord = findViewById(R.id.btnRecord)
//        btnStop = findViewById(R.id.btnStop)
//        tvTimer = findViewById(R.id.tvTimer)
//        videoView = findViewById(R.id.videoView)
//        controlLayout = findViewById(R.id.controlLayout)
//
//        btnRecord.setOnClickListener {
//            startCountdown()
//        }
//
//        btnStop.setOnClickListener {
//            stopRecording()
//        }
//    }
//    private fun startCountdown() {
//        tvTimer.visibility = View.VISIBLE
//
//        object : CountDownTimer(3000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                tvTimer.text = (millisUntilFinished / 1000).toString()
//            }
//
//            override fun onFinish() {
//                tvTimer.visibility = View.GONE
//                startRecording()
//            }
//        }.start()
//    }
//    private fun startRecording() {
//
//        val fileName = "camera_${System.currentTimeMillis()}.mp4"
//        outputFile = getExternalFilesDir(null)?.absolutePath + "/$fileName"
//
//        mediaRecorder = MediaRecorder().apply {
//            setAudioSource(MediaRecorder.AudioSource.MIC)
//            setVideoSource(MediaRecorder.VideoSource.SURFACE)
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            setOutputFile(outputFile)
//            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setVideoFrameRate(30)
//            setVideoSize(1280, 720)
//            prepare()
//            start()
//        }
//
//        isRecording = true
//
//        btnRecord.visibility = View.GONE
//        btnStop.visibility = View.VISIBLE
//    }
//    private fun stopRecording() {
//        try {
//            mediaRecorder?.stop()
//            mediaRecorder?.release()
//            mediaRecorder = null
//
//            isRecording = false
//
//            btnStop.visibility = View.GONE
//            btnRecord.visibility = View.VISIBLE
//
//            playVideo()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//    private fun playVideo() {
//
//        if (outputFile == null) return
//
//        controlLayout.visibility = View.GONE
//        textureView.visibility = View.GONE
//        videoView.visibility = View.VISIBLE
//
//        videoView.setVideoPath(outputFile)
//        videoView.start()
//
//        videoView.setOnCompletionListener {
//            // वापस camera UI
//            controlLayout.visibility = View.VISIBLE
//            textureView.visibility = View.VISIBLE
//            videoView.visibility = View.GONE
//        }
//    }
//
//}



