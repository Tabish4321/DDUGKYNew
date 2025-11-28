package com.deendayalproject
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.deendayalproject.base.BaseActivity
import com.deendayalproject.util.FullScreenHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        FullScreenHelper.enableFullScreen(this)
    }
}
