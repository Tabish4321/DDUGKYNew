package com.deendayalproject.base.extFun


import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R

private const val TAG = "NavExt"

/**
 * Safe navigate - prevents double navigation by checking current destination
 */
fun Fragment.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null
) {
    try {
        val navController = findNavController()
        val currentDest = navController.currentDestination
        val action = currentDest?.getAction(resId)

        // If the current destination has the requested action or the destination exists in graph
        val destinationExists = navController.graph.findNode(resId) != null

        // Avoid duplicate navigations: only navigate if current destination is still attached & action exists (or target exists)
        if (currentDest != null && (action != null || destinationExists)) {
            navController.navigate(resId, args, navOptions)
        } else {
            Log.w(TAG, "navigateSafe: action/destination not available for id=$resId from ${currentDest?.id}")
        }
    } catch (e: IllegalArgumentException) {
        Log.w(TAG, "navigateSafe IllegalArgumentException: ${e.message}")
    } catch (e: Exception) {
        Log.w(TAG, "navigateSafe Exception: ${e.message}")
    }
}

/**
 * Pop safely. Returns true if pop succeeded.
 */
fun Fragment.popSafe(): Boolean {
    return try {
        findNavController().popBackStack()
    } catch (e: Exception) {
        Log.w(TAG, "popSafe failed: ${e.message}")
        false
    }
}

/**
 * Creates a NavOptions instance with simple enter/exit animations defined in res/animator
 * Usage: navigateSafe(R.id.dest, args, navOptions = defaultNavOptions())
 */
fun defaultNavOptions(
    @AnimatorRes enter: Int =R.anim.slide_in_right ,
    @AnimatorRes exit: Int = R.anim.slide_out_left,
    @AnimatorRes popEnter: Int = R.anim.slide_in_left,
    @AnimatorRes popExit: Int = R.anim.slide_out_right
): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(enter)
        .setExitAnim(exit)
        .setPopEnterAnim(popEnter)
        .setPopExitAnim(popExit)
        .build()
}
