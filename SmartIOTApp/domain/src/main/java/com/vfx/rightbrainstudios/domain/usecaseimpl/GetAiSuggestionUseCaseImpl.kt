package com.vfx.rightbrainstudios.domain.usecaseimpl


import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetAiSuggestionUseCaseImpl(
    private val repository: OpenAiRepository,
    private val ioDispatcher: CoroutineDispatcher
) : GetAiSuggestionUseCase {

    override suspend fun invoke(prompt: String): OpenAiData {
        return withContext(ioDispatcher) {
            repository.requestAiSuggestion(prompt)
        }
    }
}