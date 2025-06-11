package com.vfx.rightbrainstudios.data

import com.vfx.rightbrainstudios.data.remote.OpenAIService
import com.vfx.rightbrainstudios.data.remote.model.Choice
import com.vfx.rightbrainstudios.data.remote.model.Message
import com.vfx.rightbrainstudios.data.remote.model.OpenAIResponse
import com.vfx.rightbrainstudios.data.repository.OpenAIRepositoryImpl
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OpenAIRepositoryImplTest {
    private val mockService = mockk<OpenAIService>()

    private lateinit var repository: OpenAiRepository

    @Before
    fun setup(){
        repository = OpenAIRepositoryImpl(mockService)
    }

    @Test
    fun `calls openAIService and maps response to OpenAiData`() = runTest {
        val prompt = "How to control temperature?"

        println("Before mocking...")

        val message = Message(role = "assistant", content = "Environment is stable.")
        val choice = Choice(1,message,"Done..")
        val mockResponse = OpenAIResponse(choices = listOf(choice))

        coEvery { mockService.chatCompletion(any()) } returns mockResponse

        println("Mock ready, calling function")

        val result = repository.requestAiSuggestion(prompt)

        println("Response: ${result.content}")
        assertEquals("Environment is stable.", result.content)
    }
}