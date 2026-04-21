package com.catcsyun.liantadog.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}