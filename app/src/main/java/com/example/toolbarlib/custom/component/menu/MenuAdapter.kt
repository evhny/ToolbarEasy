package com.example.toolbarlib.custom.component.menu

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toolbarlib.R
import com.example.toolbarlib.custom.component.Component
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.inflate
import kotlinx.android.synthetic.main.li_custom.view.*
import kotlinx.android.synthetic.main.li_menu.view.*

class MenuAdapter(private val onItemClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mItems = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEXT -> ItemTextHolder(parent.inflate(R.layout.li_menu))
            else -> ItemViewHolder(parent.inflate(R.layout.li_custom))
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mItems[holder.adapterPosition]
        (holder as? ItemTextHolder)?.apply {
            val text = (item as? TextComponent)?.text
                ?: item.toString()
            tvText.text = text
        }

        (holder as? ItemViewHolder)?.apply {
            llContent.addView((item as Component).getView(llContent.context))
        }

        holder.itemView.setOnClickListener {
            if (item is Component && item.isInitCallback()) item.callback?.invoke()
            else onItemClick.invoke(holder.adapterPosition)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = mItems[position]
        return if (item is String || item is TextComponent) TYPE_TEXT
        else position
    }

    fun setList(list: MutableList<Any>) {
        mItems.clear()
        mItems.addAll(list)
        notifyDataSetChanged()
    }

    internal class ItemTextHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText = itemView.tvText as TextView
    }

    internal class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent = itemView.llContent as ViewGroup
    }

    companion object {
        const val TYPE_TEXT = 99999
        const val TYPE_CUSTOM = 1
    }
}