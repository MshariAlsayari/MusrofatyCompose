package com.msharialsayari.musrofaty.business_layer.data_layer.hilt

import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsSourceImpl
import com.simplemobiletools.calendar.domain.settings.SettingsRepository
import com.simplemobiletools.calendar.domain.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindSmsDataSource(smsSourceImpl: SmsSourceImpl): SmsDataSource

    @Binds
    abstract fun bindSettingsRemoteDataSource(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository


}