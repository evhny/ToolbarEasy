package com.example.toolbarlib.custom.util

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.toolbarlib.custom.ToolbarAssembled
import com.example.toolbarlib.custom.component.Component
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

        val (leftComponent,centerComponent, rightComponent)
                = mCalculateComponents.calculateComponent(components)
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
                if (index == leftComponent.size - 1 && centerComponent.isNotEmpty() && isNeedConnectLeftToCenter) centerComponent.first().mViewId
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
            component.margin.marginTop.convertToPix(context)
        )
        set.connect(
            viewId,
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM,
            component.margin.marginBottom.convertToPix(context)
        )
        set.setMargin(
            viewId,
            ConstraintSet.START,
            component.margin.marginStart.convertToPix(context)
        )
        set.setMargin(
            viewId,
            ConstraintSet.END,
            component.margin.marginEnd.convertToPix(context)
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
}