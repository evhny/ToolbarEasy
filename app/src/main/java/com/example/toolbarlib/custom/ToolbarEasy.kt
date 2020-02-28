package com.example.toolbarlib.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar

class ToolbarEasy @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    fun applyBuilder(builder: Builder) {
        if (builder.homeButtonId != null) {
            setNavigationIcon(builder.homeButtonId!!)
        }
        if (builder.titleText != null) {
            title = builder.titleText
        }
        if (builder.titleTextColor != null) {
            setTitleTextColor(builder.titleTextColor!!)
        }
        if (builder.subTitleText != null) {
            subtitle = builder.subTitleText
        }
        if (builder.subtitleTextColor != null) {
            setSubtitleTextColor(builder.subtitleTextColor!!)
        }
        if (builder.navigationIconColor != null) {
            navigationIcon?.mutate()?.let {
                it.setTint(builder.navigationIconColor!!)
                this.navigationIcon = it
            }
        }
    }

    fun build(): Builder {
        return Builder()
    }

    inner class Builder(
        var homeButtonId: Int? = null,
        var titleText: String? = null,
        var titleTextColor: Int? = null,
        var subTitleText: String? = null,
        var subtitleTextColor: Int? = null,
        var navigationIconColor: Int? = null
    ) {

        fun homeButtonId(homeButtonId: Int) = apply { this.homeButtonId = homeButtonId }
        fun navigationIconColor(navigationIconColor: Int) =
            apply { this.navigationIconColor = navigationIconColor }

        fun titleText(titleText: String) = apply { this.titleText = titleText }
        fun titleTextColor(titleTextColor: Int) = apply { this.titleTextColor = titleTextColor }

        fun subTitleText(subTitleText: String) = apply { this.subTitleText = subTitleText }
        fun subtitleTextColor(subtitleTextColor: Int) =
            apply { this.subtitleTextColor = subtitleTextColor }

        fun build() = applyBuilder(this)
    }

}