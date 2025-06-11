package com.vfx.rightbrainstudios.core.di


import com.vfx.rightbrainstudios.data.di.IoDispatcher
import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.domain.usecaseimpl.GetAiSuggestionUseCaseImpl
import com.vfx.rightbrainstudios.domain.usecaseimpl.GetRecentChartDataUseCaseImpl
import com.vfx.rightbrainstudios.domain.usecaseimpl.SaveSensorRecordUseCaseImpl
import com.vfx.rightbrainstudios.domain.usecaseimpl.SubscribeChartDataUseCaseImpl
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import com.vfx.rightbrainstudios.domain.usecase.GetHistoryDataUseCase
import com.vfx.rightbrainstudios.domain.usecase.GetRecentChartDataUseCase
import com.vfx.rightbrainstudios.domain.usecase.SaveSensorRecordUseCase
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import com.vfx.rightbrainstudios.domain.usecaseimpl.GetHistoryDataUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSaveSensorRecordUseCase(
        repository: SensorRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): SaveSensorRecordUseCase {
        return SaveSensorRecordUseCaseImpl(repository, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGetRecentChartDataUseCase(
        repository: SensorRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): GetRecentChartDataUseCase {
        return GetRecentChartDataUseCaseImpl(repository, dispatcher)
    }

    @Provides
    @Singleton
    fun provideSubscribeChartDataUseCase(
        repository: MqttRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): SubscribeChartDataUseCase {
        return SubscribeChartDataUseCaseImpl(repository, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGetAiSuggestionUseCase(
        openAiRepository: OpenAiRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher

    ): GetAiSuggestionUseCase {
        return GetAiSuggestionUseCaseImpl(openAiRepository, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGetHistoryDataUseCase(
        sensorRepository: SensorRepository,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): GetHistoryDataUseCase {
        return GetHistoryDataUseCaseImpl(sensorRepository , dispatcher)
    }
}