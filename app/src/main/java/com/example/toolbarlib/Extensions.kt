package com.example.toolbarlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes res: Int, attach: Boolean = false) : View{
    return LayoutInflater.from(context).inflate(res, this, attach)
}