package com.example.toolbarlib.custom

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.annotation.DrawableRes

/**
 * MenuComponent create popup menu that will show after click on menu icon
 * @param items is a string array with name of item menu
 * @param iconRes drawable id icon. When click on it, menu will open as popup
 */
class MenuComponent(
    private val items: Array<String>,
    @DrawableRes private val iconRes: Int
) : Component {
    override fun getView(context: Context): View {
        val imageView = ImageView(context)
        imageView.setImageResource(iconRes)
        imageView.setOnClickListener { v: View -> createPopup(context, v) }
        return imageView
    }

    private fun createPopup(context: Context, view: View) {
        val popupMenu = PopupMenu(context, view)
        items.forEach { popupMenu.menu.add(it) }
        popupMenu.show()
    }
}