package com.example.toolbarlib.custom.component

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.example.toolbarlib.R


/**
 * MenuComponent create popup menu that will show after click on menu icon
 * @param items is a string array with name of item menu
 * @param iconRes drawable id icon. When click on it, menu will open as popup
 * @param style Style resource id
 * @param onMenuClick Listener when menu item click
 */
class MenuComponent(
    val items: Array<String>,
    @DrawableRes private val iconRes: Int,
    @StyleRes private val style: Int = R.style.MenuComponent,
    private val onMenuClick: (menuItem: MenuItem) -> Unit
) : Component() {
    override fun getView(context: Context): View {
        if(mView != null) return mView!!
        val imageView = ImageView(context)
        imageView.id = View.generateViewId()
        mView = imageView
        imageView.setImageResource(iconRes)
        imageView.setOnClickListener { v: View -> createPopup(context, v) }
        return imageView
    }

    private fun createPopup(context: Context, view: View) {
        val wrapper = ContextThemeWrapper(context, style)
        val popupMenu = PopupMenu(wrapper, view)
        items.forEach { popupMenu.menu.add(it) }
        popupMenu.setOnMenuItemClickListener {
            onMenuClick.invoke(it)
            true
        }
        popupMenu.show()
    }
}