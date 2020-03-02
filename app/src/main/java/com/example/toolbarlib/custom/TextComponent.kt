package com.example.toolbarlib.custom

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.toolbarlib.R

class TextComponent(private val text: String) : Component {
    override fun getView(context: Context): View {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        return textView
    }
}