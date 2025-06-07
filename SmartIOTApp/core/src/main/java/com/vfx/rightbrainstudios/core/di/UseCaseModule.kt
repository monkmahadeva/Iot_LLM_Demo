package com.vfx.rightbrainstudios.core.di


import com.vfx.rightbrainstudios.data.usecaseimpl.GetAiSuggestionUseCaseImpl
import com.vfx.rightbrainstudios.data.usecaseimpl.SubscribeChartDataUseCaseImpl
import com.vfx.rightbrainstudios.domain.usecase.GetAiSuggestionUseCase
import com.vfx.rightbrainstudios.domain.usecase.SubscribeChartDataUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindAiSuggestionUseCase(
        impl: GetAiSuggestionUseCaseImpl
    ): GetAiSuggestionUseCase

    @Binds
    @Singleton
    abstract fun bindSubscribeChartDataUseCase(
        impl: SubscribeChartDataUseCaseImpl
    ): SubscribeChartDataUseCase
}