package com.msharialsayari.musrofaty.business_layer.data_layer.hilt

import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryDataSource
import com.msharialsayari.musrofaty.business_layer.data_layer.categories.CategoryDataSourceImpl
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsDataSource
import com.msharialsayari.musrofaty.business_layer.data_layer.sms.SmsSourceImpl
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.SettingsRepository
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindSmsDataSource(smsSourceImpl: SmsSourceImpl): SmsDataSource

    @Binds
    abstract fun bindSettingsRemoteDataSource(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindCategoryDataSource(categoryDataSourceImpl: CategoryDataSourceImpl): CategoryDataSource


}