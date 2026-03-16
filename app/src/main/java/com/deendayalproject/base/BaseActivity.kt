package com.deendayalproject.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import com.deendayalproject.util.KeyboardUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.deendayalproject.network.SessionManager
import com.deendayalproject.util.AppUtil.showSessionExpiredDialog
import kotlinx.coroutines.launch


open class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var doubleBackToExit = false
    private val TAG = "BaseActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: BaseActivity started")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.findNavController()
        observeSessionExpired()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Back button pressed")
                    if (KeyboardUtils.isOpen(this@BaseActivity)) {
                        KeyboardUtils.hide(this@BaseActivity)
                        return
                    }
                    handleBack()
                }
            }
        )

        val rootView = findViewById<View>(android.R.id.content)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                imeInsets.bottom
            )
            insets
        }
    }


    private fun observeSessionExpired() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SessionManager.sessionExpired.collect {
                    showSessionExpiredDialog(
                        navController = navController,
                        context = this@BaseActivity
                    )
                }
            }
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            KeyboardUtils.hide(this)
        }else if (ev.flags and MotionEvent.FLAG_WINDOW_IS_OBSCURED != 0) {
                return true
            }

        return super.dispatchTouchEvent(ev)
    }

    private fun handleBack() {
        val currentId = navController.currentDestination?.id

        Log.d(TAG, "handleBack: currentDestinationId = $currentId")

        if (currentId == R.id.homeFragment) {
            if (doubleBackToExit) {
                finish()
                return
            }

            doubleBackToExit = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExit = false
            }, 2000)
            return
        }

        if (navController.popBackStack()) return

        finish()
    }

    fun getNavController(): NavController = navController
}
