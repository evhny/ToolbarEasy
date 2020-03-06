package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin

class ToolbarFlexible @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val container = RelativeLayout(context).apply {
        // orientation = LinearLayout.HORIZONTAL
        setBackgroundResource(android.R.color.white)
    }

    init {
        super.addView(
            container,
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        //ignore add menu xml
    }

    private fun collectComponent(creator: Creator) {
        val components = creator.getComponents()
        var viewOld: View? = null
        components.forEachIndexed { index, it ->
            val view = it.getView(context)
            view.id = index + 1
            if (view.layoutParams != null) {
                val params = RelativeLayout.LayoutParams(view.layoutParams as MarginLayoutParams)
                when (it.gravity) {
                    GravityPosition.LEFT -> params.addRule(
                        RelativeLayout.ALIGN_PARENT_START,
                        RelativeLayout.TRUE
                    )
                    GravityPosition.RIGHT -> params.addRule(
                        RelativeLayout.ALIGN_PARENT_END,
                        RelativeLayout.TRUE
                    )
                    GravityPosition.CENTER -> params.addRule(
                        RelativeLayout.CENTER_IN_PARENT,
                        RelativeLayout.TRUE
                    )
                    else -> {}
                }

                if (viewOld != null && it.gravity == GravityPosition.NONE) {
                    params.addRule(RelativeLayout.RIGHT_OF, viewOld!!.id)
                }

                view.layoutParams = params
                viewOld = view

            }
            container.addView(view)
        }
    }

    fun createToolbar(creatorBlock: Creator.() -> Unit) {
        val creator = Creator()
        creatorBlock.invoke(creator)
        collectComponent(creator)
    }

    inner class Creator(private val components: MutableList<Component> = mutableListOf()) {
        fun addComponent(component: Component) {
            this.components.add(component)
        }

        fun addComponent(component: Component, margin: Margin) {
            component.margin = margin
            this.addComponent(component)
        }

        fun addComponent(component: Component, margin: Margin, gravity: GravityPosition) {
            component.gravity = gravity
            this.addComponent(component, margin)
        }

        fun addComponent(component: Component, gravity: GravityPosition) {
            component.gravity = gravity
            this.addComponent(component)
        }

        fun getComponents() = components.toList()
    }
}