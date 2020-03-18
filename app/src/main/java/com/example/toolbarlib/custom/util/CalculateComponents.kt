package com.example.toolbarlib.custom.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.component.MenuComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.component.menu.RemasteredMenuComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.extensions.convertToPix

class CalculateComponents(private val container: ConstraintLayout) {

    private val context: Context = container.context

    var isNeedConnectLeftToCenter = false
    var isNeedConnectRightToCenter = false

    fun calculateComponent(
        components: List<Component>
    ): Triple<List<Component>, List<Component>, List<Component>> {

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

        val metrics = DisplayMetrics()

        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(metrics)

        val displayWidth = metrics.widthPixels
        val centerWidth = calculateWidth(centerComponent, displayWidth)
        var freeSpaceParties =
            if (centerComponent.isEmpty()) displayWidth else (displayWidth - centerWidth.first) / 2

        val newLeftComponent = recreateComponent(leftComponent, freeSpaceParties)
        val leftResult = calculateWidth(newLeftComponent, freeSpaceParties)

        isNeedConnectLeftToCenter = leftResult.second.sum() > freeSpaceParties

        freeSpaceParties = if (centerComponent.isEmpty() && leftResult.first > 0) {
            displayWidth - leftResult.first
        } else freeSpaceParties

        val newRightComponent= recreateComponent(rightComponent, freeSpaceParties)
        val rightResult = calculateWidth(newRightComponent, freeSpaceParties)

        isNeedConnectRightToCenter = rightResult.second.sum() > freeSpaceParties

        return Triple(newLeftComponent, centerComponent, newRightComponent.reversed())
    }

    private fun recreateComponent(
        components: List<Component>,
        space: Int
    ): List<Component> {
        val listCantCollapsed = mutableListOf<Component>()
        val listCanCollapsed = mutableListOf<Component>()

        components.forEach {
            if (it.isCanBeCollapsed()) listCanCollapsed.add(it)
            else listCantCollapsed.add(it)
        }
        val resultCanCollapsed = calculateWidth(listCanCollapsed, space)
        val newComponent = collapsed(listCanCollapsed, resultCanCollapsed, space)
        listCantCollapsed.addAll(newComponent)
        return listCantCollapsed
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
        if (result.first > freeSpaceParties && components.isNotEmpty() && result.second.isNotEmpty()) {
            newComponent.addAll(components.subList(0, result.second.size))
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


    private fun collapsedComponentToMenu(components: List<Component>): RemasteredMenuComponent {
        var array = arrayOf<String>()
        components.forEach {
            if (it is TextComponent) {
                array = array.plus(it.text)
            } else if (it is MenuComponent) {
                array = array.plus(it.items)
            }
        }
        return RemasteredMenuComponent(R.drawable.ic_more_vert_black_24dp, {
            addAll(components)
        }) {

        }
    }

}