package com.vfx.rightbrainstudios.domain

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.SaveSensorRecordUseCase
import com.vfx.rightbrainstudios.domain.usecaseimpl.SaveSensorRecordUseCaseImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveSensorRecordUseCaseImplTest {

    private val mockRepo = mockk<SensorRepository>()
    private lateinit var useCase: SaveSensorRecordUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = SaveSensorRecordUseCaseImpl(mockRepo, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invokes repository with correct sensor data`() = runTest(testDispatcher) {
        val deviceId = "deviceA"
        val data = SensorData(temperature = 24.5, humidity = 40, timestamp = "2024-06-09T12:00:00Z")

        coEvery { mockRepo.saveSensorData(deviceId, data) } just Runs

        useCase.invoke(deviceId, data)

        coVerify(exactly = 1) { mockRepo.saveSensorData(deviceId, data) }
    }
}