package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.consts.MarginSet.Companion.NONE
/**
 * ImageComponent icon display component
 * @param imageRes - image resource link
 * @param onImageClick - block for event click
 */
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