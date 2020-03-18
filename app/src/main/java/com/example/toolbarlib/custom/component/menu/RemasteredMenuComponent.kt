package com.example.toolbarlib.custom.component.menu

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.component.Component
import kotlinx.android.synthetic.main.list.view.*

class RemasteredMenuComponent(
    @DrawableRes private val iconRes: Int,
    private val creatorBlock: Creator.() -> Unit,
    private val onMenuClick: (position: Int) -> Unit
) : Component() {

    private val adapter = MenuAdapter{
        onMenuClick.invoke(it)
        popupWindow?.dismiss()

    }
    var popupWindow: PopupWindow? = null
    private lateinit var contentView: RecyclerView

    override fun getView(context: Context): View {
        if (mView != null) return mView!!
        val imageView = ImageView(context)
        imageView.id = View.generateViewId()
        mView = imageView
        imageView.setImageResource(iconRes)
        imageView.setOnClickListener { v: View -> createPopup(context, v) }

        initRecyclerView(context)

        return imageView
    }

    private fun initRecyclerView(context: Context){
        val creator = Creator()
        creatorBlock.invoke(creator)
        adapter.setList(creator.components)
        contentView = RecyclerView(context)
        contentView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        contentView.layoutManager = LinearLayoutManager(context)
        contentView.adapter = adapter

    }

    private fun createPopup(context: Context, view: View) {
        popupWindow = PopupWindow(context)
        popupWindow!!.contentView = contentView
        popupWindow!!.isFocusable = true
        popupWindow!!.showAsDropDown(view)
    }

    inner class Creator(val components: MutableList<Any> = mutableListOf()) {
        fun add(component: Component) {
            components.add(component)
        }

        fun add(vararg items: String) {
            components.addAll(items)
        }

        fun add(creator: Creator) {
            components.addAll(creator.components)
        }

        fun addAll(components: List<Component>) {
            this.components.addAll(components)
        }
    }
}