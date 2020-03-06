package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.component.Component

class ImageComponent(
    private val imageRes: Int,
    private val onImageClick: () -> Unit
) : Component() {

    override fun getView(context: Context): View {
        if(mView != null) return mView!!
        val image = AppCompatImageView(context)
        mView = image
        image.id = View.generateViewId()
        image.setOnClickListener { onImageClick.invoke() }
        image.setImageResource(imageRes)
        return image
    }
}