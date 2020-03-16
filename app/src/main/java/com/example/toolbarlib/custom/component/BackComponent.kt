package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import com.example.toolbarlib.R

class BackComponent(
    @DrawableRes private var iconRes: Int = R.drawable.ic_arrow_back_black_24dp,
    var isEnabled: Boolean = false,
    onClick: (() -> Unit)? = null
) : Component(onClick) {

    override fun getView(context: Context): View {
        if (mView != null) return mView!!
        val image = AppCompatImageView(context)
        mView = image!!
        image.id = View.generateViewId()
        mView = image

        image.setOnClickListener { callback?.invoke() }
        image.setImageResource(iconRes)
        setHomeButtonEnabled(isEnabled)
        return image
    }

    fun setHomeButtonEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        if (mView == null) return
        mView!!.visibility = if (isEnabled) View.VISIBLE else View.GONE
    }

    fun setHomeButtonIcon(@DrawableRes iconRes: Int) {
        this.iconRes = iconRes
        if (mView == null) return
        (mView as ImageView).setImageResource(iconRes)
    }
}