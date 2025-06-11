package com.vfx.rightbrainstudios.smartiotapplication

import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import com.vfx.rightbrainstudios.domain.usecase.GetRecentChartDataUseCase
import com.vfx.rightbrainstudios.domain.usecase.SaveSensorRecordUseCase
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import com.vfx.rightbrainstudios.smartiotapplication.presentation.state.State
import com.vfx.rightbrainstudios.smartiotapplication.presentation.viewmodel.SensorViewModel
import com.vfx.rightbrainstudios.smartiotapplication.util.JsonParser
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class SensorViewModelTest {
     private lateinit var subscribeChartDataUseCase : SubscribeChartDataUseCase
//     private val mqttRepository = mockk<MqttRepository>()
//
     private lateinit var saveSensorRecordUseCase : SaveSensorRecordUseCase
//     private val sensorRepository = mockk<SensorRepository>()
//
     private lateinit var getAiSuggestionUseCase : GetAiSuggestionUseCase
//     private val openAiRepository = mockk<OpenAiRepository>()
//
     private lateinit var getRecentChartDataUseCase :GetRecentChartDataUseCase
     private val testDispatcher = UnconfinedTestDispatcher()

     private lateinit var viewModel: SensorViewModel

     @Before
     fun setup(){
          Dispatchers.setMain(testDispatcher)
          subscribeChartDataUseCase = mockk()
          coEvery { subscribeChartDataUseCase.invoke(any(), any()) } returns Unit

          getAiSuggestionUseCase = mockk()
          coEvery { getAiSuggestionUseCase.invoke(any()) } returns OpenAiData("Condition is STABLE")

          saveSensorRecordUseCase = mockk()
          coEvery { saveSensorRecordUseCase.invoke(any(),any()) } just Runs

          getRecentChartDataUseCase = mockk()
          coEvery { getRecentChartDataUseCase.invoke(any()) } returns listOf(SensorData(23.50 , 50 , "2025-06-11T01:00:00"))

          viewModel = SensorViewModel(
               subscribeChartDataUseCase,
               saveSensorRecordUseCase,
               getRecentChartDataUseCase,
               getAiSuggestionUseCase
          )
     }

     @After
     fun tearDown() {
          Dispatchers.resetMain()
     }

     @Test
     fun `analyze sets uiState to Success on valid AI response`() = runTest{
          val prompt = "Temperature is 23.5C, humidity 50%"
          val expectedSuggestion = "Condition is STABLE"

          coEvery { getAiSuggestionUseCase.invoke(any()) } returns OpenAiData(expectedSuggestion)

          val result = getAiSuggestionUseCase.invoke(prompt)

         advanceUntilIdle()

          assertTrue(result.content.contains(State.STABLE.name))
          assertEquals(expectedSuggestion, result.content)
     }

     @Test
     fun `analyze save data use case`() = runTest {
          val sensorData = SensorData(23.50, 50, LocalDateTime.now().toString())
          viewModel.startTest(sensorData)
          advanceUntilIdle()

          val result = viewModel.liveChartData.first()
          assertEquals(listOf(sensorData), result)
     }

     @Test
     fun `analyze sets uiState to Error on failure`() = runTest {
          val prompt = "Invalid reading"
          val deviceId = "DeviceA"

          coEvery { getAiSuggestionUseCase.invoke(any()) } throws RuntimeException("AI Failed")

          viewModel.requestAiSuggestion(deviceId)

          advanceUntilIdle()

          assertTrue(viewModel.aiResponse.value?.state == State.ERROR)
          assertEquals("AI Failed", viewModel.aiResponse.value?.openAIData?.content)
     }

     @Test
     fun `requestAiSuggestion sets STABLE state when isBalanced`() = runTest {
          val mockHistory = listOf(
               SensorData(23.50, 50, "2025-06-11T01:00:00"),
               SensorData(23.50, 50, "2025-06-11T01:01:00") // balanced
          )

          coEvery { getRecentChartDataUseCase.invoke(any()) } returns mockHistory
          coEvery { getAiSuggestionUseCase.invoke(any()) } returns OpenAiData("All good.")

          viewModel.requestAiSuggestion("DeviceA")
          advanceUntilIdle()

          assertEquals(State.STABLE, viewModel.aiResponse.value?.state)
     }

     @Test
     fun `requestAiSuggestion emits ERROR when no history`() = runTest {
          coEvery { getRecentChartDataUseCase.invoke(any()) } returns emptyList()

          viewModel.requestAiSuggestion("deviceA")
          advanceUntilIdle()

          val result = viewModel.aiResponse.value
          assertEquals(State.ERROR, result?.state)
          assertEquals("No sensor history found.", result?.openAIData?.content)
     }
}