package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin

abstract class Component {

    var gravity: GravityPosition = GravityPosition.CENTER

    var margin: Margin = Margin()

    protected var mView: View? = null

    var mViewId = -1
        get() {
            return mView?.id ?: -1
        }

    abstract fun getView(context: Context): View

}