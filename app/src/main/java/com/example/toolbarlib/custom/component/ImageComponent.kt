package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.consts.MarginSet.Companion.NONE

class ImageComponent(
    private val imageRes: Int,
    onImageClick: (() -> Unit)? = null
) : Component(onImageClick) {

    init {
        margin = Margin().apply {
            marginTop = NONE
            marginBottom = NONE
            marginStart = NONE
            marginEnd = NONE
        }
    }

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