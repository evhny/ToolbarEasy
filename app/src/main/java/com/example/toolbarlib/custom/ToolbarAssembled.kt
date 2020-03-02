package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar

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
                LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
            )
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

        fun getComponents() = components.toList()
    }

}