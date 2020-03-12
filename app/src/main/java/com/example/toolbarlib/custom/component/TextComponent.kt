package com.example.toolbarlib.custom.component

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.toolbarlib.R

class TextComponent(
    val text: String,
    @StyleRes private val style: Int = R.style.TextComponent,
    @ColorRes private val textColor: Int? = null,
    @DimenRes private val textSize: Int? = null,
    @FontRes private val textFontRes: Int? = null,
    private val isSingleLine: Boolean? = null,
    private val maxLines: Int? = null,
    onTextClick: (() -> Unit)? = null
) : Component(onTextClick) {


    override fun getView(context: Context): View {
        if (mView != null) return mView!!
        val textView = TextView(context)
        textView.id = View.generateViewId()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(style)
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(context, style)
        }
        applyParams(textView, context)
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setOnClickListener {
            callback?.invoke()
        }
        mView = textView
        return textView
    }

    private fun applyParams(textView: TextView, context: Context) {
        if (textFontRes != null) {
            textView.typeface = ResourcesCompat.getFont(context, textFontRes)
        }
        if (textColor != null) {
            textView.setTextColor(textColor!!)
        }
        if (textSize != null) {
            textView.textSize = context.resources.getDimension(textSize)
        }
        if (isSingleLine != null) {
            textView.setSingleLine(isSingleLine)
        }
        if (maxLines != null) {
            textView.maxLines = maxLines
        }
    }
}