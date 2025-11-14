package com.cslianta.catdog.ext


fun String.isUrl(): Boolean {
    return this.matches(Regex("^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$"))
}