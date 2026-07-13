package com.deendayalproject.base

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.deendayalproject.objectDetactionUtil.ObjectType

object AiBadgeHelper {

    private const val TAG_BADGE = "ai_badge_overlay"
    private const val TAG_CONTAINER = "ai_badge_container"

    
    private fun ensureContainer(imageView: ImageView): FrameLayout {
        val current = imageView.parent
        if (current is FrameLayout && current.tag == TAG_CONTAINER) return current

        val originalParent = current as? ViewGroup ?: return FrameLayout(imageView.context)
        val index = originalParent.indexOfChild(imageView)
        val originalParams = imageView.layoutParams

        originalParent.removeView(imageView)

        val container = FrameLayout(imageView.context).apply {
            tag = TAG_CONTAINER
            layoutParams = originalParams
        }
        container.addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        originalParent.addView(container, index)
        return container
    }

    private fun dp(context: Context, v: Int) = (v * context.resources.displayMetrics.density).toInt()

    private fun pill(context: Context, accent: Int): LinearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(context, 10), dp(context, 5), dp(context, 10), dp(context, 5))
        elevation = dp(context, 3).toFloat()
        background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(context, 20).toFloat()
            setColor(Color.parseColor("#E6212121"))
            setStroke(dp(context, 1), accent)
        }
    }

    private fun place(container: FrameLayout, view: View) {
        container.findViewWithTag<View>(TAG_BADGE)?.let { container.removeView(it) }
        view.tag = TAG_BADGE
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.TOP or Gravity.END
            topMargin = dp(container.context, 6)
            marginEnd = dp(container.context, 6)
        }
        view.scaleX = 0.7f; view.scaleY = 0.7f; view.alpha = 0f
        container.addView(view, params)
        view.animate().scaleX(1f).scaleY(1f).alpha(1f).setDuration(180).start()
    }

    fun clear(imageView: ImageView) {
        (imageView.parent as? FrameLayout)?.findViewWithTag<View>(TAG_BADGE)?.let {
            (imageView.parent as FrameLayout).removeView(it)
        }
    }

    fun showLoading(imageView: ImageView, context: Context) {
        val container = ensureContainer(imageView)
        val accent = Color.parseColor("#9E9E9E")
        val view = pill(context, accent).apply {
            addView(ProgressBar(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp(context, 14), dp(context, 14))
                indeterminateTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            })
        }
        place(container, view)
    }

    fun showCount(imageView: ImageView, count: Int, type: String, context: Context) {
        val container = ensureContainer(imageView)
        val isFan = type.equals(ObjectType.FAN.name, true)
        val accent = if (isFan) Color.parseColor("#29B6F6") else Color.parseColor("#FFC107")
        val label = if (isFan) "Fan" else "Light"

        val view = pill(context, accent).apply {
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp(context, 8), dp(context, 8)).apply { marginEnd = dp(context, 6) }
                background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(accent) }
            })
            addView(TextView(context).apply {
                text = "$label· $count"
                setTextColor(Color.WHITE)
                textSize = 12f
                setTypeface(typeface, Typeface.BOLD)
            })
        }
        place(container, view)
    }
}