package com.vfx.rightbrainstudios.smartiotapplication.util


import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object JsonParser {
    fun parseToMap(json: String): Map<String, Any> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            Any::class.java
        )
        val adapter = moshi.adapter<Map<String, Any>>(type)
        return adapter.fromJson(json) ?: throw IllegalArgumentException("Invalid JSON")
    }
}