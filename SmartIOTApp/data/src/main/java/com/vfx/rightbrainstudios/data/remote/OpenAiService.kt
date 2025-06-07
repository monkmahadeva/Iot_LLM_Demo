package com.vfx.rightbrainstudios.data.remote

import com.vfx.rightbrainstudios.data.remote.model.OpenAIRequest
import com.vfx.rightbrainstudios.data.remote.model.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenAIService {
    @POST("chat/completions")
    suspend fun chatCompletion(@Body request: OpenAIRequest): OpenAIResponse
}