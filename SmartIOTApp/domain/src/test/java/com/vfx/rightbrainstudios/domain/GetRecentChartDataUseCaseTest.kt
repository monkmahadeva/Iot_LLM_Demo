package com.vfx.rightbrainstudios.domain

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.GetRecentChartDataUseCase
import com.vfx.rightbrainstudios.domain.usecaseimpl.GetRecentChartDataUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetRecentChartDataUseCaseTest {

    private val mockRepo = mockk<SensorRepository>()
    private lateinit var useCase: GetRecentChartDataUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        useCase = GetRecentChartDataUseCaseImpl(mockRepo , testDispatcher) // ✅ class, not interface
    }

    @Test
    fun `returns recent chart data correctly`() = runTest {
        val mockData = listOf(SensorData(25.0, 40, "2024-06-09T00:00:00Z"))
        coEvery { mockRepo.getRecentSensorData("deviceA") } returns mockData

        val result = useCase.invoke("deviceA")

        assertEquals(mockData, result)
        coVerify { mockRepo.getRecentSensorData("deviceA") }
    }
}