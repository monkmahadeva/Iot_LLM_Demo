package com.vfx.rightbrainstudios.domain

import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import com.vfx.rightbrainstudios.domain.usecaseimpl.SubscribeChartDataUseCaseImpl
import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubscribeChartDataUseCaseImplTest {

    private val mockMqttRepository = mockk<MqttRepository>()
    private lateinit var useCase: SubscribeChartDataUseCase
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        useCase = SubscribeChartDataUseCaseImpl(mockMqttRepository , testDispatcher)
    }

    @Test
    fun `parses message and invokes callback with SensorData`() = runTest {
        val topic = "iot/deviceA/temperature"

        val jsonMessage = """{
            "temperature": 25.0,
            "humidity": 50,
            "timestamp": "2024-06-09T14:00:00Z"
        }""".trimIndent()

        val expected = SensorData(25.0, 50, "2024-06-09T14:00:00Z")
        val messageCallbackSlot = slot<(String) -> Unit>()
        var receivedSensorData: SensorData? = null

        every {
            mockMqttRepository.connectAndSubscribe(eq(topic), capture(messageCallbackSlot))
        } just Runs

        // Execute the use case
        useCase(topic) { receivedSensorData = parseSensorData(it) }

        // Simulate incoming MQTT message
        messageCallbackSlot.captured.invoke(jsonMessage)

        // Assert
        assertEquals(expected, receivedSensorData)
    }

    fun parseSensorData(json: String): SensorData {
        return SensorData(
            temperature = 25.0,
            humidity = 50,
            timestamp = "2024-06-09T14:00:00Z"
        )
    }
}