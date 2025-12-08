package com.deendayalproject.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var doubleBackToExit = false
    private val TAG = "BaseActivityLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: BaseActivity started")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: layout set -> activity_main")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.findNavController()

        Log.d(TAG, "NavController initialized, currentDestination = ${navController.currentDestination?.label}")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "Back button pressed")
                handleBack()
            }
        })
    }

    private fun handleBack() {
        val currentId = navController.currentDestination?.id

        Log.d(TAG, "handleBack: currentDestinationId = $currentId")

        if (currentId == R.id.homeFragment) {
            if (doubleBackToExit) {
                Log.d(TAG, "Exiting app (double back confirmed)")
                finish()
                return
            }
            Log.d(TAG, "user: Press back again to exit")
            doubleBackToExit = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExit = false
                Log.d(TAG, "Reset doubleBackToExit after timeout")
            }, 2000)
            return
        }

        if (navController.popBackStack()) {
            Log.d(TAG, "Navigated back using navController.popBackStack()")
            return
        }

        Log.d(TAG, "No more fragments, finishing activity")
        finish()
    }

    fun getNavController(): NavController = navController
}
