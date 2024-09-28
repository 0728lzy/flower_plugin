package com.pet.translator.ext


fun String.isUrl(): Boolean {
    return this.matches(Regex("^(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?$"))
}