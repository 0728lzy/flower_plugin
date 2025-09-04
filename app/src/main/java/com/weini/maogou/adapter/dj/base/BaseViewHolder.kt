package com.weini.maogou.adapter.dj.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(@NonNull context: Context, @NonNull itemView: View, viewGroup: ViewGroup) :
    RecyclerView.ViewHolder(itemView) {
    private val mSparseArray = SparseArray<View>()

    fun setImageResource(widgetId: Int, resId: Int): BaseViewHolder {
        getWidgetFromId<ImageView>(widgetId).setImageResource(resId)
        return this
    }

    fun setImageResource(widgetId: Int, drawable: Drawable): BaseViewHolder {
        getWidgetFromId<ImageView>(widgetId).setImageDrawable(drawable)
        return this
    }

    fun setTextResource(widgetId: Int, resId: Int): BaseViewHolder {
        getWidgetFromId<TextView>(widgetId).setText(resId)
        return this
    }

    fun setTextResource(widgetId: Int, str: String): BaseViewHolder {
        getWidgetFromId<TextView>(widgetId).text = str
        return this
    }

    fun <T : View> getWidgetFromId(id: Int): T {
        var view = mSparseArray.get(id)
        if (view != null) {
            return view as T
        }
        view = itemView.findViewById<T>(id)
        mSparseArray.put(id, view)
        return view
    }

    companion object {
        fun createViewHolder(
            @NonNull context: Context,
            @NonNull viewGroup: ViewGroup,
            @LayoutRes layoutId: Int
        ): BaseViewHolder {
            return BaseViewHolder(
                context,
                LayoutInflater.from(context).inflate(layoutId, viewGroup, false),
                viewGroup
            )
        }
    }
}