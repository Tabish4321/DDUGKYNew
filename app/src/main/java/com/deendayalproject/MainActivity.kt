package com.deendayalproject
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.deendayalproject.util.FullScreenHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        FullScreenHelper.enableFullScreen(this)
        setContentView(R.layout.activity_main) // ✅ Load the correct activity layout

    }
}
