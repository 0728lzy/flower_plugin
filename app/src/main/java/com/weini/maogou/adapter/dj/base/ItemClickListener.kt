package com.weini.maogou.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}