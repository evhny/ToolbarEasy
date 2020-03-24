package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.View
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
/**
 * Component base class
 */
abstract class Component {

    var callback: (() -> Unit)? = null

    /**
     * pointer to display position
     */
    var gravity: GravityPosition = GravityPosition.LEFT

    var margin: Margin = Margin()

    protected var mView: View? = null
    set(value) {
        if(value != null){
            value.id = View.generateViewId()
            field = value
        }
    }

    var mViewId = -1
    get() {
        return mView?.id ?: field
    }

    private var isCanBeCollapsed = true

    constructor()

    constructor(callback: (() -> Unit)?) {
        this.callback = callback
    }

    abstract fun getView(context: Context): View

    fun isInitCallback() = callback != null

    fun isCanBeCollapsed() = isCanBeCollapsed

    fun setCanBeCollapsed(canBeCollapsed: Boolean): Component {
        isCanBeCollapsed = canBeCollapsed
        return this
    }
}