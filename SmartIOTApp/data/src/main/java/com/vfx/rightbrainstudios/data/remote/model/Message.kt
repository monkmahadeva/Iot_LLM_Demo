package com.vfx.rightbrainstudios.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message (
    val role: String,   // "user" or "system"
    val content: String
)