package com.example.toolbarlib.custom

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class ImageComponent(
    private val imageRes: Int,
    private val onImageClick: (() -> Unit?)? = null
) : Component() {

    override fun getView(context: Context): View {
        val image = AppCompatImageView(context)
        image.layoutParams = createLayoutParams(context)
        image.setOnClickListener { onImageClick?.invoke() }
        image.setImageResource(imageRes)
        return image
    }
}