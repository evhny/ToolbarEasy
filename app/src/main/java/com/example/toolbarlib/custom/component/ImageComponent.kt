package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class ImageComponent(
    private val imageRes: Int,
    onImageClick: (() -> Unit)? = null
) : Component(onImageClick) {

    override fun getView(context: Context): View {
        if(mView != null) return mView!!
        val image = AppCompatImageView(context)
        mView = image
        image.id = View.generateViewId()
        image.adjustViewBounds = true
        image.setOnClickListener { callback?.invoke() }
        image.setImageResource(imageRes)
        return image
    }
}