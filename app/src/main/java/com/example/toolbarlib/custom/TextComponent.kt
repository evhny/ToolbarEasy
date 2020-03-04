package com.example.toolbarlib.custom

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import com.example.toolbarlib.R

class TextComponent(
    private val text: String,
    @StyleRes private val style: Int = R.style.TextComponent,
    private val onTextClick: (() -> Unit)? = null
) : Component() {

    override fun getView(context: Context): View {
        val textView = TextView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(style)
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(context, style)
        }
        textView.text = text
        textView.setOnClickListener {
            onTextClick?.invoke()
        }
        return textView
    }
}