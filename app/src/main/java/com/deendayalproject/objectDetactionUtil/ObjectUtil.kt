package com.deendayalproject.objectDetactionUtil

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext

 fun showCountBadge(iv: ImageView, count: Int,context: Context) {
    val frame = iv.parent as FrameLayout
    frame.findViewWithTag<View>("BADGE")?.let {
        frame.removeView(it)
    }

    val badge = TextView(context).apply {
        tag = "BADGE"
        text = if (count > 99) "99+" else count.toString()
        setTextColor(Color.WHITE)
        textSize = 12f
        setTypeface(null, Typeface.BOLD)
        setPadding(12, 4, 12, 4)

        background = GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setColor(Color.RED)
        }

        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.END or Gravity.TOP
        ).apply {
            setMargins(0, 8, 8, 0)
        }
    }
    frame.addView(badge)
}

 fun showLoaderBadge(iv: ImageView,context: Context) {
    val parent = iv.parent as ViewGroup
    val frame = if (parent is FrameLayout) {
        parent
    } else {
        val index = parent.indexOfChild(iv)
        parent.removeView(iv)

        val f = FrameLayout(context)
        f.layoutParams = iv.layoutParams
        f.addView(iv)

        parent.addView(f, index)
        f
    }
    // Remove old badge/loader
    frame.findViewWithTag<View>("BADGE")?.let {
        frame.removeView(it)
    }

    val progress = ProgressBar(context).apply {
        tag = "BADGE"

        layoutParams = FrameLayout.LayoutParams(
            60, 60,
            Gravity.END or Gravity.TOP
        ).apply {
            setMargins(0, 8, 8, 0)
        }
    }

    progress.background = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setColor(Color.WHITE)
    }

    frame.addView(progress)
}


enum class ObjectType {
    LIGHT,
    FAN,
}