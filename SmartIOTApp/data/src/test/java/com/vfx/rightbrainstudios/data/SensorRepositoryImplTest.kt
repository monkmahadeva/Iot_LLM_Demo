package com.vfx.rightbrainstudios.data

import com.vfx.rightbrainstudios.data.model.SensorDao
import com.vfx.rightbrainstudios.data.model.SensorEntity
import com.vfx.rightbrainstudios.data.repository.SensorRepositoryImpl
import com.vfx.rightbrainstudios.domain.model.SensorData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.UUID

@ExperimentalCoroutinesApi
class SensorRepositoryImplTest {

    @Mock
    private lateinit var mockSensorDao: SensorDao

    private lateinit var sensorRepository: SensorRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sensorRepository = SensorRepositoryImpl(mockSensorDao)
    }

    @Test
    fun `test saveSensorData calls insertSensor`() = runTest {
        val data = SensorData(23.5, 50, "2024-01-01T00:00:00Z")

        sensorRepository.saveSensorData("deviceA", data)

        verify(mockSensorDao).insert(any())
    }

    @Test
    fun `test getRecentSensorData returns last 10 mapped values`() = runTest {
        val dummyList = (6..15).map {
            SensorEntity(
                id = it,
                deviceId = "deviceA",
                temperature = 20.0 + it,
                humidity = 40 + it,
                timestamp = "2024-01-01T00:00:${it.toString().padStart(2, '0')}Z"
            )
        }.reversed() // Ensure DESC order if your logic assumes it

        whenever(mockSensorDao.getRecentSensorData("deviceA")).thenReturn(flowOf( dummyList))

        val result = sensorRepository.getRecentSensorData("deviceA")

        assertEquals(10, result.size)
        assertEquals("2024-01-01T00:00:15Z", result.first().timestamp)
    }
}