package com.vfx.rightbrainstudios.data.usecaseimpl

import com.vfx.rightbrainstudios.data.di.IoDispatcher
import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAiSuggestionUseCaseImpl @Inject constructor(
    private val repository: OpenAiRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : GetAiSuggestionUseCase {

    override suspend fun invoke(prompt: String): OpenAiData {
        return withContext(ioDispatcher) {
            repository.requestAiSuggestion(prompt)
        }
    }
}