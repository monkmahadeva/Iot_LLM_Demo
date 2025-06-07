package com.vfx.rightbrainstudios.smartiotapplication.presentation.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.domain.model.SensorData
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import com.vfx.rightbrainstudios.smartiotapplication.presentation.state.State
import com.vfx.rightbrainstudios.smartiotapplication.util.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val subscribeChartDataUseCase: SubscribeChartDataUseCase,
    private val sensorRepository: SensorRepository,
    private var getAiSuggestionUseCase: GetAiSuggestionUseCase
) : ViewModel() {

    private val _liveChartData = MutableStateFlow<List<SensorData>>(emptyList())
    val liveChartData
        get() = _liveChartData

    private val liveBuffer = mutableListOf<SensorData>()

    private val _aiResponse = MutableStateFlow<OpenAISuggestion?>(null)
    val aiResponse: StateFlow<OpenAISuggestion?> = _aiResponse

    init {
        startMqttListener("deviceA")
    }

    @VisibleForTesting(otherwise = VisibleForTesting.Companion.PRIVATE)
    fun setTestData(data: List<SensorData>) {
        _liveChartData.value = data
    }

    fun setState(state: OpenAISuggestion) {
        _aiResponse.value = state
    }

    fun requestAiSuggestion(deviceId: String) {
        viewModelScope.launch {
            _aiResponse.value = null // Clear old response first
            val history = sensorRepository.getRecentSensorData(deviceId)

            if (history.isEmpty()) {
                setState(OpenAISuggestion(
                    openAIData = OpenAiData(
                        "No sensor history found."
                    ),
                    state = State.ERROR
                ))
                return@launch
            }
            try {
                val isBalanced = isBalanced(history)

                val prompt = buildPromptFrom(history)
                val response = getAiSuggestionUseCase.invoke(prompt)
                if (isBalanced){
                    setState(OpenAISuggestion(response, State.STABLE))
                }else{
                    setState(OpenAISuggestion(response, State.UNSTABLE))
                }

            } catch (e: Exception) {
                Log.e("SensorVieModel" , e.message.toString())
                _aiResponse.value = OpenAISuggestion(
                    openAIData = OpenAiData(e.message.toString()),
                    state = State.ERROR
                )
            }
        }
    }

    private fun startMqttListener(deviceId: String) {
        // Start listening to MQTT
        viewModelScope.launch {
            subscribeChartDataUseCase.invoke("iot/${deviceId}/temperature"
            ) { jsonMessage->
                Log.d("SensorViewModel", "Message received: $jsonMessage")
                try {
                    val sensor = parseSensorData(jsonMessage) //  private fun parseSensorData(json: String): SensorEntity -> should be converted to domain model SensorData

                    // Save in DB via Repository
                    viewModelScope.launch {
                        sensorRepository.saveSensorData(deviceId, sensor)
                    }

                    // Update chart buffer
                    liveBuffer.add(sensor)
                    if (liveBuffer.size > 10) liveBuffer.removeAt(0)
                    _liveChartData.value = liveBuffer.toList()

                } catch (e: Exception) {
                    Log.e("SensorViewModel", "Invalid JSON: $jsonMessage", e)
                }

            }
        }
    }

    @VisibleForTesting
    fun startTest() {
        startMqttListener("deviceA")
    }

    fun isBalanced(sensorList: List<SensorData>): Boolean { // TODO: Business logic to shift to domain and data#1
        return sensorList.all {
            it.temperature == 23.5 && it.humidity == 50
        }
    }

    fun buildPromptFrom(data: List<SensorData>): String {
        return buildString {
            append("Recent temperature and humidity readings:\n")
            data.forEach {
                append("• ${it.timestamp}: ${it.temperature}°C, ${it.humidity}%\n")
            }
            append("\nSuggest optimal controls based on this data.")
        }
    }

    private fun parseSensorData(json: String): SensorData { // TODO: Business logic to shift to domain and data#2
        val data = JsonParser.parseToMap(json)
        return SensorData(
            temperature = (data["temperature"] as? Double) ?: 0.0,
            humidity = ((data["humidity"] as? Double)?.toInt() ?: 0),
            timestamp = data["timestamp"] as? String ?: ""
        )
    }


    data class OpenAISuggestion(val openAIData: OpenAiData, val state: State?)
}