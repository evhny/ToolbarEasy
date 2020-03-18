package com.example.toolbarlib.custom.util

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.ToolbarAssembled
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.component.MenuComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.extensions.convertToPix

class CollectorComponents(private val container: ConstraintLayout) {

    private val context: Context = container.context
    private val mCalculateComponents = CalculateComponents(container)

    private val isNeedConnectLeftToCenter
        get() = mCalculateComponents.isNeedConnectLeftToCenter
    private val isNeedConnectRightToCenter
        get() = mCalculateComponents.isNeedConnectRightToCenter

    fun collectComponent(creator: ToolbarAssembled.Creator) {
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
        val triple =
            mCalculateComponents.calculateComponent(leftComponent, centerComponent, rightComponent)
        rightComponent = triple.third.toMutableList()
        leftComponent = triple.first.toMutableList()
        components.clear()
        components.addAll(leftComponent)
        components.addAll(centerComponent)
        components.addAll(rightComponent)
        components.forEachIndexed { index, component ->
            container.addView(
                component.getView(context),
                ConstraintLayout.LayoutParams(
                    if (component.gravity == GravityPosition.LEFT && isNeedConnectLeftToCenter
                        || component.gravity == GravityPosition.RIGHT && isNeedConnectRightToCenter
                    ) 0
                    else ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    0
                )
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
            val nextId =
                if (index == leftComponent.size - 1 && centerComponent.isNotEmpty() && isNeedConnectLeftToCenter) centerComponent.last().mViewId
                else leftComponent.getOrNull(index + 1)?.mViewId ?: -1
            constraintSetLeft(
                component,
                leftComponent.getOrNull(index - 1)?.mViewId ?: -1,
                nextId
            )
        }
        rightComponent.forEachIndexed { index, component ->
            constraintSetRight(
                component,
                rightComponent.getOrNull(index - 1)?.mViewId ?: -1,
                leftComponent.getOrNull(index + 1)?.mViewId ?: -1
            )
        }
        components.forEach {
            fixMargins(it)
        }
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

        if (isNeedConnectLeftToCenter)
            set.connect(
                component.getView(context).id,
                ConstraintSet.END,
                if (nextId > 0) nextId else ConstraintSet.PARENT_ID,
                if (nextId > 0) ConstraintSet.START else ConstraintSet.END
            )
        set.applyTo(container)
    }

    private fun constraintSetRight(component: Component, previewId: Int, nextId: Int) {
        val set = createConstraintSet(component)
        set.connect(
            component.mViewId,
            ConstraintSet.END,
            if (previewId > 0) previewId else ConstraintSet.PARENT_ID,
            if (previewId > 0) ConstraintSet.START else ConstraintSet.END
        )
        if (isNeedConnectRightToCenter)
            set.connect(
                component.getView(context).id,
                ConstraintSet.START,
                if (nextId > 0) nextId else ConstraintSet.PARENT_ID,
                if (nextId > 0) ConstraintSet.END else ConstraintSet.START
            )
        set.applyTo(container)
    }

    private fun fixMargins(component: Component) {
        val view = component.getView(context)
        val lParams = view.layoutParams as ConstraintLayout.LayoutParams
        if (lParams.bottomMargin + lParams.topMargin + view.height > container.height) {
            lParams.bottomMargin = 0
        }
        if (lParams.topMargin + view.height > container.height) {
            lParams.topMargin = 0
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
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(container.measuredHeight, View.MeasureSpec.EXACTLY)
            )
            val measuredWidth = view.measuredWidth + it.convertToPix(
                it.margin.marginEnd,
                context
            ) + it.convertToPix(it.margin.marginStart, context)
            sumWith += measuredWidth
            if (freeSpaceParties > sumWith)
                array = array.plusElement(measuredWidth)

        }
        return Pair(sumWith, array)
    }

    private fun collapsed(
        components: List<Component>,
        result: Pair<Int, Array<Int>>,
        freeSpaceParties: Int
    ): List<Component> {
        val newComponent = mutableListOf<Component>()
        if (result.first > freeSpaceParties && components.isNotEmpty() && result.second.size > 1) {
            newComponent.addAll(components.subList(0, result.second.size - 1))
            newComponent.add(
                collapsedComponentToMenu(
                    components.subList(
                        result.second.size,
                        components.size
                    )
                )
            )
        } else {
            newComponent.addAll(components)
        }
        return newComponent
    }


    private fun collapsedComponentToMenu(components: List<Component>): MenuComponent {
        var array = arrayOf<String>()
        components.forEach {
            if (it is TextComponent) {
                array = array.plus(it.text)
            } else if (it is MenuComponent) {
                array = array.plus(it.items)
            }
        }
        return MenuComponent(array, R.drawable.ic_more_vert_black_24dp) {

        }
    }

}