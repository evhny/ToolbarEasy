package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isEmpty
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.component.MenuComponent
import com.example.toolbarlib.custom.component.PopupComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.extensions.convertToPix

class ToolbarAssembled @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private val container = ConstraintLayout(context)
    private var creator: Creator? =null

    init {
        super.addView(
            container,
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )

    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        //ignore add menu xml
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(measuredHeight > 0 && container.isEmpty()){
            creator?.let { collectComponent(it) }
        }
        f(0,",", creator, container, context)
    }

    fun f(vararg arg: Any?){}

    fun createToolbar(creatorBlock: Creator.() -> Unit) {
        creator = Creator()
        creatorBlock.invoke(creator!!)
    }

    private fun collectComponent(creator: Creator) {
        val components = creator.getComponents().toMutableList()
        var leftComponent = mutableListOf<Component>()
        val centerComponent = mutableListOf<Component>()
        var rightComponent = mutableListOf<Component>()
        components.forEach {
            when (it.gravity) {
                GravityPosition.CENTER -> centerComponent.add(it)
                GravityPosition.LEFT -> leftComponent.add(it)
                GravityPosition.RIGHT -> rightComponent.add(it)
            }
        }
        val triple = calculateComponent(leftComponent, centerComponent, rightComponent)
        rightComponent = triple.third.toMutableList()
        leftComponent = triple.first.toMutableList()
        components.clear()
        components.addAll(leftComponent)
        components.addAll(centerComponent)
        components.addAll(rightComponent)
        components.forEachIndexed { index, component ->
            container.addView(
                component.getView(context),
                ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, 0)
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
                rightComponent.getOrNull(index - 1)?.mViewId ?: -1
            )
        }
    }

    private fun calculateComponent(
        leftComponent: List<Component>,
        centerComponent: List<Component>,
        rightComponent: List<Component>
    ): Triple<List<Component>, List<Component>, List<Component>> {
        val metrics = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(metrics)

        val displayWidth = metrics.widthPixels
        val centerWidth = calculateWidth(centerComponent, displayWidth)
        val freeSpaceParties = (displayWidth - centerWidth.first) / 2

        val rightResultSum = calculateWidth(rightComponent, freeSpaceParties)
        val leftWidth = calculateWidth(leftComponent, freeSpaceParties)

        val newRightComponent = mutableListOf<Component>()
        val newLeftComponent = mutableListOf<Component>()

        if ((centerWidth.first + rightResultSum.first + leftWidth.first) > displayWidth) {

            if(rightComponent.isNotEmpty() && rightResultSum.second.size  > 1){
                newRightComponent.addAll(rightComponent.subList(0, rightResultSum.second.size - 1))
                newRightComponent.add(collapsedComponentToMenu(rightComponent.subList(rightResultSum.second.size, rightComponent.size)))
            } else if(rightComponent.isNotEmpty()){
                newRightComponent.add(collapsedComponentToMenu(rightComponent))
            }

            if(leftComponent.isNotEmpty() && leftWidth.second.size > 1){
                newLeftComponent.addAll(leftComponent.subList(0, leftWidth.second.size - 1))
                newLeftComponent.add(collapsedComponentToMenu(leftComponent.subList(leftWidth.second.size, leftWidth.second.size)))
            } else if(leftComponent.isNotEmpty()){
                newLeftComponent.add(collapsedComponentToMenu(leftComponent))
            }

            Log.d(
                "Calculate",
                "freeSpace: $freeSpaceParties"
            )
        }

        Log.d(
            "Calculate",
            "displayWidth: $displayWidth centerWidth: ${centerWidth.second.size} ${centerWidth.first} rightWidth: ${rightResultSum.second.size} ${rightResultSum.first} leftWidth: ${leftWidth.second.size} ${leftWidth.first}"
        )

        return Triple(newLeftComponent, centerComponent, newRightComponent)
    }

    private fun collapsedComponentToMenu(components: List<Component>): MenuComponent{
        var array = arrayOf<String>()
        components.forEach {
            if(it is TextComponent){
                array = array.plus(it.text)
            } else if(it is MenuComponent){
                array = array.plus(it.items)
            }
        }
        return MenuComponent(array, R.drawable.ic_more_vert_black_24dp) {

        }
    }

    private fun calculateWidth(
        components: List<Component>,
        freeSpaceParties: Int
    ): Pair<Int, Array<Int>> {
        var sumWith = 0
        var array = arrayOf<Int>()
        components.forEach {
            val view = it.getView(context)
            view.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(container.measuredHeight, MeasureSpec.EXACTLY)
            )
            val measuredWidth = view.measuredWidth
            sumWith += measuredWidth
            if (freeSpaceParties > sumWith)
              array = array.plusElement(measuredWidth)

        }
        return Pair(sumWith, array)
    }

    private fun createConstraintSet(component: Component): ConstraintSet {
        val viewId = component.mViewId
        val set = ConstraintSet()
        set.clone(container)
        set.constrainDefaultHeight(viewId, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.connect(
            viewId,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            component.convertToPix(component.margin.marginTop, context)
        )
        set.connect(
            viewId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            component.convertToPix(component.margin.marginBottom, context)
        )
        set.setMargin(
            viewId,
            ConstraintSet.START,
            component.convertToPix(component.margin.marginStart, context)
        )
        set.setMargin(
            viewId,
            ConstraintSet.END,
            component.convertToPix(component.margin.marginEnd, context)
        )
        return set
    }

    private fun constraintSetCenter(component: Component, previewId: Int, nextId: Int) {
        val set = createConstraintSet(component)
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
        val set = createConstraintSet(component)
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
        set.applyTo(container)
    }

    private fun constraintSetRight(component: Component, previewId: Int) {
        val set = createConstraintSet(component)
        set.connect(
            component.mViewId,
            ConstraintSet.END,
            if (previewId > 0) previewId else ConstraintSet.PARENT_ID,
            if (previewId > 0) ConstraintSet.START else ConstraintSet.END
        )
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