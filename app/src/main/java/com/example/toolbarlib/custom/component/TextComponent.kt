package com.example.toolbarlib.custom.component

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import com.example.toolbarlib.R

class TextComponent(
    val text: String,
    @StyleRes private val style: Int = R.style.TextComponent,
    onTextClick: (() -> Unit)? = null
) : Component(onTextClick) {


    override fun getView(context: Context): View {
        if(mView != null) return mView!!
        val textView = TextView(context)
        textView.id = View.generateViewId()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(style)
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(context, style)
        }
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setOnClickListener {
            callback?.invoke()
        }
        mView = textView
        return textView
    }
}