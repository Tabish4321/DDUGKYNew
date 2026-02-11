package com.deendayalproject.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Rishi Porwal
 */
object KeyboardUtils {

    fun hide(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        val imm =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isOpen(activity: Activity): Boolean {
        val root = activity.window.decorView
        val rect = Rect()
        root.getWindowVisibleDisplayFrame(rect)
        return (root.height - rect.bottom) > root.height * 0.15
    }
}


