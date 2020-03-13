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
        mView = AppCompatImageView(context)
            .apply {
                setOnClickListener { callback?.invoke() }
                setImageResource(imageRes)
            }
        return mView!!
    }
}