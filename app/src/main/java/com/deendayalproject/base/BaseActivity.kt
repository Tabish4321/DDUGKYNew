package com.deendayalproject.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.deendayalproject.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // must match activity_main.xml
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.findNavController()

        // Optional: tie ActionBar with navController if you use a global toolbar in activity
        // setupActionBarWithNavController(navController)

        // Handle system back pressed centrally
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBack()
            }
        })
    }

    private fun handleBack() {
        val currentId = navController.currentDestination?.id
        if (currentId == R.id.homeFragment) {
            if (doubleBackToExit) {
                finish()
                return
            }

            doubleBackToExit = true
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, 2000)
            return
        }
        if (navController.popBackStack()) return
        finish()
    }

    // Optional: give fragments access to NavController if needed
    fun getNavController(): NavController = navController
}
