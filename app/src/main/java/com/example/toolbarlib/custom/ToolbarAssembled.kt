package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin

class ToolbarAssembled @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val container = ConstraintLayout(context).apply {
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
        val leftComponent = mutableListOf<Component>()
        val centerComponent = mutableListOf<Component>()
        val rightComponent = mutableListOf<Component>()
        components.forEach {
            when (it.gravity) {
                GravityPosition.CENTER -> centerComponent.add(it)
                GravityPosition.LEFT -> leftComponent.add(it)
                GravityPosition.RIGHT -> rightComponent.add(it)
            }
        }
        components.forEachIndexed { index, component ->
            container.addView(
                component.getView(context)
            )
        }
        centerComponent.forEachIndexed { index, component ->
            constraintSetCenter(
                component,
                centerComponent.getOrNull(index - 1)?.mViewId ?: -1,
                centerComponent.getOrNull(index + 1)?.mViewId ?: -1
            )
        }
        leftComponent.forEachIndexed { index, component ->
            constraintSetLeft(
                component,
                leftComponent.getOrNull(index - 1)?.mViewId ?: -1,
                leftComponent.getOrNull(index + 1)?.mViewId ?: -1
            )
        }
        rightComponent.forEachIndexed { index, component ->
            constraintSetRight(
                component,
                rightComponent.getOrNull(index - 1)?.mViewId ?: -1,
                rightComponent.getOrNull(index + 1)?.mViewId ?: -1
            )
        }
    }

    fun createToolbar(creatorBlock: Creator.() -> Unit) {
        val creator = Creator()
        creatorBlock.invoke(creator)
        collectComponent(creator)
    }

    private fun createConstraintSet(viewId: Int): ConstraintSet {
        val set = ConstraintSet()
        set.clone(container)
        set.connect(
            viewId,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        set.connect(
            viewId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )
        return set
    }

    private fun constraintSetCenter(component: Component, previewId: Int, nextId: Int) {
        val set = createConstraintSet(component.mViewId)
        set.connect(
            component.getView(context).id,
            ConstraintSet.START,
            if (previewId > 0) previewId else ConstraintSet.PARENT_ID,
            if (previewId > 0) ConstraintSet.END else ConstraintSet.START
        )
        set.connect(
            component.getView(context).id,
            ConstraintSet.END,
            if (nextId > 0) nextId else ConstraintSet.PARENT_ID,
            if (nextId > 0) ConstraintSet.START else ConstraintSet.END
        )
        set.setHorizontalChainStyle(component.getView(context).id, ConstraintSet.CHAIN_PACKED)
        set.applyTo(container)
    }

    private fun constraintSetLeft(component: Component, previewId: Int, nextId: Int) {
        val set = createConstraintSet(component.mViewId)
        set.connect(
            component.mViewId,
            ConstraintSet.START,
            if (previewId > 0) previewId else ConstraintSet.PARENT_ID,
            if (previewId > 0) ConstraintSet.END else ConstraintSet.START
        )
        if (nextId > 0)
            set.connect(
                component.getView(context).id,
                ConstraintSet.END,
                nextId,
                ConstraintSet.START
            )
        set.setHorizontalChainStyle(component.mViewId, ConstraintSet.CHAIN_PACKED)
        set.applyTo(container)
    }

    private fun constraintSetRight(component: Component, previewId: Int, nextId: Int) {
        val set = createConstraintSet(component.mViewId)
        set.connect(
            component.mViewId,
            ConstraintSet.END,
            if (previewId > 0) previewId else ConstraintSet.PARENT_ID,
            if (previewId > 0) ConstraintSet.END else ConstraintSet.START
         )
        if (nextId > 0)
            set.connect(
                component.getView(context).id,
                ConstraintSet.END,
                nextId,
                ConstraintSet.START
            )
        set.setHorizontalChainStyle(component.mViewId, ConstraintSet.CHAIN_PACKED)
        set.applyTo(container)
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