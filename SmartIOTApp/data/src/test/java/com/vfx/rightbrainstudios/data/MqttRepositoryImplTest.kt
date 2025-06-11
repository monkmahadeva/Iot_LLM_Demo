package com.vfx.rightbrainstudios.data

import com.vfx.rightbrainstudios.data.remote.MqttManager
import com.vfx.rightbrainstudios.data.repository.MqttRepositoryImpl
import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class MqttRepositoryImplTest {

    private val mockManager = mockk<MqttManager>(relaxed = true)
    private lateinit var repository: MqttRepository

    @Before
    fun setup() {
        repository = MqttRepositoryImpl(mockManager)
    }

    @Test
    fun `delegates connect to MqttManager`() {
        val topic = "iot/deviceA/temp"
        val callback = mockk<(String) -> Unit>()

        repository.connectAndSubscribe(topic, callback)

        coVerify(exactly = 1) {
            mockManager.connect(topic, callback)
        }
    }

    @Test
    fun `delegates disconnect to MqttManager`() {
        repository.disconnect()
        coVerify(exactly = 1) { mockManager.disconnect() }
    }
}
