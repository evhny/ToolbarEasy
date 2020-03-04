package com.example.toolbarlib.custom.property.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.example.toolbarlib.custom.Component

fun Component.convertToPix(dip: Float, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        r.displayMetrics
    ).toInt()
}