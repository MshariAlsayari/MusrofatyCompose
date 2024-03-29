package com.msharialsayari.musrofaty.business_layer.domain_layer.settings

import kotlinx.coroutines.flow.Flow


interface SettingsRepository {
    suspend fun setAppAppearance(appAppearance: Int)
    suspend fun getAppAppearance(): Flow<Int>
    suspend fun setAppLanguage(appLanguage: Int)
    suspend fun getAppLanguage(): Flow<Int>
}
