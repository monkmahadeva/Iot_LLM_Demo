package com.vfx.rightbrainstudios.smartiotapplication.model

import com.vfx.rightbrainstudios.domain.model.OpenAiData
import com.vfx.rightbrainstudios.smartiotapplication.presentation.state.State

data class OpenAISuggestion(val openAIData: OpenAiData, val state: State?)