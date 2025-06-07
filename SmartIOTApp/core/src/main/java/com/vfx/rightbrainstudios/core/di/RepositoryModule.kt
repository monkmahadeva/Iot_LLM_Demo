package com.vfx.rightbrainstudios.core.di

import com.vfx.rightbrainstudios.domain.repository.MqttRepository
import com.vfx.rightbrainstudios.domain.repository.OpenAiRepository
import com.vfx.rightbrainstudios.domain.repository.SensorRepository
import com.vfx.rightbrainstudios.data.repository.MqttRepositoryImpl
import com.vfx.rightbrainstudios.data.repository.OpenAIRepositoryImpl
import com.vfx.rightbrainstudios.data.repository.SensorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSensorRepository(
        impl: SensorRepositoryImpl
    ): SensorRepository

    @Binds
    @Singleton
    abstract fun bindMqttRepository(
        impl: MqttRepositoryImpl
    ): MqttRepository

    @Binds
    @Singleton
    abstract fun bindOpenAiRepository(
        impl: OpenAIRepositoryImpl
    ): OpenAiRepository

}