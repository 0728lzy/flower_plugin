package com.cslianta.catdog.adapter.dj.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> constructor(
    @NonNull val context: Context,
    @LayoutRes val layoutId: Int,
    @NonNull var list: List<T>
) :
    RecyclerView.Adapter<BaseViewHolder>(), View.OnClickListener {

    private var mListener: ItemClickListener<T>? = null

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(this)
        bindView(holder, list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.createViewHolder(context, parent, layoutId)
    }

    fun updateAllData(list: List<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    abstract fun bindView(holder: BaseViewHolder, data: T)

    override fun onClick(view: View) {
        val tag = view.tag as Int
        mListener?.click(tag, list[tag])
    }

    fun setItemClickListener(listener: ItemClickListener<T>) {
        this.mListener = listener
    }
}