package com.vfx.rightbrainstudios.domain.usecase

fun interface SubscribeChartDataUseCase {
    suspend operator fun invoke(topic: String, onData: (String) -> Unit)
}