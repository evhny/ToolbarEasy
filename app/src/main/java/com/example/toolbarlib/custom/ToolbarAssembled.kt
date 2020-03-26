package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isEmpty
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.component.BackComponent
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.component.MenuComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.consts.MarginSet
import com.example.toolbarlib.custom.property.extensions.convertToPix
import com.example.toolbarlib.custom.util.CollectorComponents

class ToolbarAssembled @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val container = ConstraintLayout(context)
    private var creator: Creator? = null

    private var backComponent: BackComponent? = null
    private val mComponentCollector = CollectorComponents(container)

    init {
        super.addView(
            container,
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        backComponent = BackComponent(/*onClick = { setOnHomeButtonClick() }*/)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        //ignore add menu xml
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight > 0 && container.isEmpty()) {
            creator?.let { mComponentCollector.collectComponent(it) }
        }
    }

    fun createToolbar(creatorBlock: Creator.() -> Unit) {
        creator = Creator()
        creatorBlock.invoke(creator!!)
    }

    fun setOnHomeButtonClick(onHomeButtonClick: (() -> Unit)? = null) {
        backComponent?.callback = onHomeButtonClick
    }

    fun setHomeButtonIcon(@DrawableRes iconRes: Int) {
        backComponent?.setHomeButtonIcon(iconRes)
    }

    fun setHomeButtonEnabled(isEnabled: Boolean) {
        backComponent?.setHomeButtonEnabled(isEnabled)
        if (measuredHeight > 0) {
            container.removeAllViews()
            creator?.let { mComponentCollector.collectComponent(it) }
        }
    }

    fun isHomeButtonEnabled() = backComponent?.isEnabled


    inner class Creator(private val components: MutableList<Component> = mutableListOf()) {

        init {
            addComponent(
                backComponent!!,
                Margin().apply {
                    marginStart = MarginSet.MEDIUM
                    marginEnd = MarginSet.MEDIUM
                },
                GravityPosition.LEFT
            )
        }

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