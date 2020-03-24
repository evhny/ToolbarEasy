package com.example.toolbarlib.custom.component

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.property.extensions.convertSpToPix
import com.example.toolbarlib.custom.property.extensions.convertToPix
import com.example.toolbarlib.custom.util.CountDrawable

/**
 * BadgeComponent displays on top of the specified component counter
 * @param component - base component
 * @param badgeTextSize - size text badge
 * @param badgeTextColor - color text badge
 * @param badgeTextTypeface - font text badge
 * @param badgeSize - size badge
 * @param badgeColor - color background badge
 * @param count - The number needed to display
 * @param position - position badge
 * @param width - width component
 * @param height - height component
 * @param onClick - block for event click
 */
class BadgeComponent(
    private val component: Component,
    private val badgeTextSize: Float = 10f,
    private val badgeTextColor: Int = Color.WHITE,
    private val badgeTextTypeface: Typeface = Typeface.DEFAULT,
    private val badgeSize: Float = 10f,
    private val badgeColor: Int = Color.BLACK,
    private val count: Int = 0,
    private val position: Position = Position.BOTTOM_LEFT,
    private val width: Float? = null,
    private val height: Float? = null,
    private val onClick: () -> Unit
) : Component(onClick) {

    override fun getView(context: Context): View {
        if (mView != null) return mView!!
        val contentView = component.getView(context)

        component.callback = onClick

        val container = RelativeLayout(context)
        container.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        container.id = View.generateViewId()
        mView = container

        container.addView(contentView, RelativeLayout.LayoutParams(
            if (width != null)
                convertToPix(width, context) else ViewGroup.LayoutParams.WRAP_CONTENT,
            if (height != null)
                convertToPix(height, context) else ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { addRule(RelativeLayout.CENTER_IN_PARENT) })

        val imageBadge = AppCompatImageView(context)
        val layoutParams = createBadgeParams(contentView)

        val badge = CountDrawable(convertSpToPix(badgeTextSize, context).toFloat())
        badge.setTextColor(badgeTextColor)
        badge.setBadgeColor(badgeColor)
        badge.setCount(count.toString())
        badge.setTextTypeface(badgeTextTypeface)
        imageBadge.setImageDrawable(badge)

        container.addView(imageBadge, layoutParams)

        return container
    }

    private fun createBadgeParams(view: View): RelativeLayout.LayoutParams {
        val params = RelativeLayout.LayoutParams(
            convertToPix(badgeSize, view.context),
            convertToPix(badgeSize, view.context)
        )

        when (position) {
            Position.BOTTOM_LEFT -> {
                params.addRule(RelativeLayout.ALIGN_START, view.id)
                params.addRule(RelativeLayout.ALIGN_BOTTOM, view.id)
            }
            Position.CENTER -> {
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
            }
            Position.BOTTOM_RIGHT -> {
                params.addRule(RelativeLayout.ALIGN_END, view.id)
                params.addRule(RelativeLayout.ALIGN_BOTTOM, view.id)
            }
            Position.TOP_LEFT -> {
                params.addRule(RelativeLayout.ALIGN_START, view.id)
                params.addRule(RelativeLayout.ALIGN_TOP, view.id)
            }
            Position.TOP_RIGHT -> {
                params.addRule(RelativeLayout.ALIGN_END, view.id)
                params.addRule(RelativeLayout.ALIGN_TOP, view.id)
            }
        }

        return params
    }

    enum class Position() {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }
}