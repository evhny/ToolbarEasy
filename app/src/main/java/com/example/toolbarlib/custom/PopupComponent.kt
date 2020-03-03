package com.example.toolbarlib.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.annotation.DrawableRes

/**
 * PopupComponent create popup menu that will show after click on icon
 * @param component Some Component
 * @param contentView Content view that will open as popup
 * @param iconRes drawable id icon. When click on it, window will open as popup
 */
class PopupComponent(
    private val component: Component? = null,
    private val contentView: View? = null,
    @DrawableRes private val iconRes: Int
) : Component {
    override fun getView(context: Context): View {
        val imageView = ImageView(context)
        imageView.setImageResource(iconRes)
        imageView.setOnClickListener { v: View -> createPopup(context, v) }
        return imageView
    }

    private fun createPopup(context: Context, view: View) {
        val popupWindow = PopupWindow(context)
        if (contentView == null) {
            popupWindow.contentView = component?.getView(context)
        } else {
            popupWindow.contentView = contentView
        }
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(view)
    }
}