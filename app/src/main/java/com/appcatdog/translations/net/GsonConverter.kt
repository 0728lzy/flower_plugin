package com.appcatdog.translations.net

import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.lang.reflect.Type


class GsonConverter : MyJSONConvert() {
    companion object {
        private val gson = GsonBuilder().serializeNulls().create()
    }

    override fun <R> String.parseBody(succeed: Type): R? {
        val string = try {
            JSONObject(this).getString("data")
        } catch (e: Exception) {
            this
        }
        return gson.fromJson<R>(string, succeed)
    }
}