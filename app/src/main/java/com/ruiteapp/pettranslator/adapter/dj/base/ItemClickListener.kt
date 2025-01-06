package com.ruiteapp.pettranslator.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}