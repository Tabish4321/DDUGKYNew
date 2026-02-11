package com.deendayalproject.base

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.LifecycleOwner
import com.deendayalproject.util.KeyboardUtils

/**
 * Created by Rishi Porwal
 */
class KeyboardBackPressHandler(
    private val activity: ComponentActivity
) {

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (KeyboardUtils.isOpen(activity)) {
                KeyboardUtils.hide(activity)
            } else {
                isEnabled = false
                activity.onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    fun register(owner: LifecycleOwner) {
        activity.onBackPressedDispatcher.addCallback(owner, callback)
    }
}
