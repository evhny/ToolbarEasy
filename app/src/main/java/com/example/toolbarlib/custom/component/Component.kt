package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin

abstract class Component {


    var callback: (() -> Unit)? = null

    var gravity: GravityPosition = GravityPosition.LEFT

    var margin: Margin = Margin()

    protected var mView: View? = null
        set(value) {
            mViewId = value?.id ?: -1
            field = value
        }

    var mViewId = -1

    constructor()

    constructor(callback: (() -> Unit)?) {
        this.callback = callback
    }

    abstract fun getView(context: Context): View

    fun isInitCallback() = callback != null
}