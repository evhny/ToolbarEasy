package com.example.toolbarlib.custom

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.extensions.convertToPix

abstract class Component {

    var gravity: GravityPosition = GravityPosition.CENTER

    var margin: Margin = Margin()

    abstract fun getView(context: Context): View

    protected  fun createLayoutParams(context: Context): ViewGroup.LayoutParams {
        val params = Toolbar.LayoutParams(
            Toolbar.LayoutParams.WRAP_CONTENT,
            Toolbar.LayoutParams.WRAP_CONTENT
        )
        params.gravity =
            when (gravity) {
                GravityPosition.LEFT -> Gravity.START or Gravity.CENTER_VERTICAL
                GravityPosition.RIGHT -> Gravity.END or Gravity.CENTER_VERTICAL
                else -> Gravity.CENTER
            }
        params.setMargins(
            convertToPix(margin.marginStart, context),
            convertToPix(margin.marginTop, context),
            convertToPix(margin.marginEnd, context),
            convertToPix(margin.marginBottom, context)
        )
        return params
    }
}