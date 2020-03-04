package com.example.toolbarlib.custom

import android.content.Context
import android.view.View
import com.example.toolbarlib.custom.property.Margin

abstract class Component {

    var margin: Margin = Margin()

    abstract fun getView(context: Context): View
}