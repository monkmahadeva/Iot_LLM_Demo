package com.vfx.rightbrainstudios.domain

import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import com.vfx.rightbrainstudios.domain.usecaseimpl.GetAiSuggestionUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetAiSuggestionUseCaseImplTest {
    private val mockOpenAiRepository = mockk<OpenAiRepository>()

    private lateinit var useCase : GetAiSuggestionUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup(){
        useCase = GetAiSuggestionUseCaseImpl(mockOpenAiRepository,testDispatcher)
    }

    @Test
    fun `returns full AI suggestion with remedy and condition analysis`() = runTest {
        val prompt = "Humidity 30%, Temperature 16°C"
        val expectedSuggestion = """
        The condition is fluctuating. Temperature drop detected.
        Recommended: Check insulation and use humidifier.
    """.trimIndent()

        coEvery { mockOpenAiRepository.requestAiSuggestion(prompt) } returns OpenAiData(expectedSuggestion)

        val result = useCase(prompt)

        assertEquals(expectedSuggestion.trim(), result.content.trim())
        coVerify(exactly = 1) { mockOpenAiRepository.requestAiSuggestion(prompt) }
    }
}