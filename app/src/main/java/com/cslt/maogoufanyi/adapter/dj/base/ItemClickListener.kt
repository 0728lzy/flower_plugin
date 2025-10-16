package com.cslt.maogoufanyi.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}