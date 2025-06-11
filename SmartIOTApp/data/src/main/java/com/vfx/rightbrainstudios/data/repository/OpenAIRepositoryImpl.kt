package com.vfx.rightbrainstudios.data.repository

import android.util.Log
import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.data.mapper.toDomain
import com.vfx.rightbrainstudios.data.remote.OpenAIService
import com.vfx.rightbrainstudios.data.remote.model.Message
import com.vfx.rightbrainstudios.data.remote.model.OpenAIRequest
import javax.inject.Inject

class OpenAIRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : OpenAiRepository {

    override suspend fun requestAiSuggestion(prompt: String): OpenAiData {
//        Log.d("OpenAiRepositoryImpl" , prompt)
        val response = openAIService.chatCompletion(
            OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    Message(
                        role = "system",
                        content = "You are an intelligent IoT assistant helping users control environmental conditions."
                    ),
                    Message("user", prompt)
                )
            )
        )

        return response.toDomain()
    }

}