package com.appcatdog.translations.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}