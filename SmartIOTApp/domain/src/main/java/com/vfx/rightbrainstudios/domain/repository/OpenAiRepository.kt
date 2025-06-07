package com.vfx.rightbrainstudios.domain.repository

import com.vfx.rightbrainstudios.domain.model.OpenAiData

interface OpenAiRepository {
    suspend fun requestAiSuggestion(prompt: String): OpenAiData
}