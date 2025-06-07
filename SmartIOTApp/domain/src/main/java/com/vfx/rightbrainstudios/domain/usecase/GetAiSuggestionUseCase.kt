package com.vfx.rightbrainstudios.domain.usecase

import com.vfx.rightbrainstudios.domain.model.OpenAiData

fun interface GetAiSuggestionUseCase {
    suspend operator fun invoke(prompt: String): OpenAiData
}