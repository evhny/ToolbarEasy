package com.example.toolbarlib.custom.component

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.Component
import com.example.toolbarlib.custom.property.extensions.convertSpToPix
import com.example.toolbarlib.custom.property.extensions.convertToPix
import com.example.toolbarlib.custom.util.CountDrawable


class BadgeComponent(
    private val component: Component,
    private val badgeTextSize: Float = 10f,
    private val badgeTextColor: Int = Color.WHITE,
    private val badgeTextTypeface: Typeface = Typeface.DEFAULT,
    private val badgeSize: Float = 10f,
    private val badgeColor: Int = Color.BLACK,
    private val count: Int = 0,
    private val position: Position = Position.BOTTOM_LEFT,
    private val onClick: () -> Unit
) : Component() {

    override fun getView(context: Context): View {
        val contentView = component.getView(context)
        contentView.setBackgroundColor(Color.CYAN)

        val container = FrameLayout(context)
        container.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        container.setOnClickListener { onClick.invoke() }
        container.id = View.generateViewId()

        container.addView(contentView)

        val imageBadge = AppCompatImageView(context)
        val layoutParams = FrameLayout.LayoutParams(
            convertToPix(badgeSize, context),
            convertToPix(badgeSize, context)
        )

        layoutParams.gravity = getGravityByPosition()

        val badge = CountDrawable(convertSpToPix(badgeTextSize, context).toFloat())
        badge.setTextColor(badgeTextColor)
        badge.setBadgeColor(badgeColor)
        badge.setCount(count.toString())
        badge.setTextTypeface(badgeTextTypeface)
        imageBadge.setImageDrawable(badge)

        container.addView(imageBadge, layoutParams)
        return container
    }

    private fun getGravityByPosition(): Int {
        return when (position) {
            Position.BOTTOM_LEFT -> Gravity.BOTTOM or Gravity.START
            Position.CENTER -> Gravity.CENTER
            Position.BOTTOM_RIGHT -> Gravity.BOTTOM or Gravity.END
            Position.TOP_LEFT -> Gravity.TOP or Gravity.START
            Position.TOP_RIGHT -> Gravity.TOP or Gravity.END
        }
    }

    enum class Position() {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }
}