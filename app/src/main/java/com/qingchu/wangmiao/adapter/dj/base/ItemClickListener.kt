package com.qingchu.wangmiao.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}