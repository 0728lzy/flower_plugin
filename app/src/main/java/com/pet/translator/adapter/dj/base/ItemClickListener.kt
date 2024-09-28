package com.pet.translator.adapter.dj.base

interface ItemClickListener<T> {
    fun click(position: Int, data: T)
}