package com.example.toolbarlib.custom.property.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.example.toolbarlib.custom.component.Component

fun Component.convertToPix(dip: Float, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip,
        r.displayMetrics
    ).toInt()
}

fun Component.convertSpToPix(sp: Float, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        r.displayMetrics
    ).toInt()
}

fun Float.convertToPix(context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        r.displayMetrics
    ).toInt()
}

fun Float.convertSpToPix(context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        r.displayMetrics
    ).toInt()
}