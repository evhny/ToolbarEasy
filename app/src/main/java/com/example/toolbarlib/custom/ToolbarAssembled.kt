package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.extensions.convertToPix

class ToolbarAssembled @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val container = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
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
        components.forEach {
            container.addView(
                it.getView(context),
                createLayoutParams(it)
            )
        }
    }

    fun createToolbar(creatorBlock: Creator.() -> Unit) {
        val creator = Creator()
        creatorBlock.invoke(creator)
        collectComponent(creator)
    }

    private fun createLayoutParams(component: Component): ViewGroup.LayoutParams {
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.setMargins(
            component.convertToPix(component.margin.marginStart, context),
            component.convertToPix(component.margin.marginTop, context),
            component.convertToPix(component.margin.marginEnd, context),
            component.convertToPix(component.margin.marginBottom, context)
        )
        return params
    }

    inner class Creator(private val components: MutableList<Component> = mutableListOf()) {
        fun addComponent(component: Component) {
            this.components.add(component)
        }

        fun addComponent(component: Component, margin: Margin) {
            component.margin = margin
            this.components.add(component)
        }

        fun getComponents() = components.toList()
    }
}