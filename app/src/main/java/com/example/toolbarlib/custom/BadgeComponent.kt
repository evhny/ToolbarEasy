package com.example.toolbarlib.custom.component

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.Component
import com.example.toolbarlib.custom.property.extensions.convertToPix
import com.example.toolbarlib.custom.util.CountDrawable


class BadgeComponent(
    private val onImageClick: () -> Unit
) : Component() {

    override fun getView(context: Context): View {

        val image = AppCompatImageView(context)
        image.layoutParams = ViewGroup.LayoutParams(convertToPix(24f, context), convertToPix(24f, context))
        image.id = View.generateViewId()
        image.setOnClickListener { onImageClick.invoke() }

        val back = ShapeDrawable()
        back.paint.color = Color.RED

        val badgeBack = ShapeDrawable()
        badgeBack.paint.color = Color.GRAY
        val layers = arrayOf<Drawable>(back, badgeBack)
        val layerDrawable = LayerDrawable(layers)
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);
        layerDrawable.setLayerInset(1, 0, 0, 0, 0);
        layerDrawable.mutate()

        val badge = CountDrawable(context)
        badge.setCount("7")
        layerDrawable.setDrawableByLayerId(1, badge)
        image.setImageDrawable(badge)
        return image
    }
}