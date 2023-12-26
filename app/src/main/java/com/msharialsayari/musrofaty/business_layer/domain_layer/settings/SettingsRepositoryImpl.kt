package com.simplemobiletools.calendar.domain.settings


import com.msharialsayari.musrofaty.business_layer.AppPreferencesDataStore
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appPreferencesDataStore: AppPreferencesDataStore,
) : SettingsRepository {

    companion object {
        const val TAG = "SettingsRemoteDataSource"
    }


    override suspend fun setAppAppearance(appAppearance: Int) {
        appPreferencesDataStore.setAppAppearance(appAppearance)
    }

    override suspend fun getAppAppearance(): Flow<Int> {
        return appPreferencesDataStore.getAppAppearance()
    }

    override suspend fun setAppLanguage(appLanguage: Int) {
        appPreferencesDataStore.setAppLanguage(appLanguage)
    }

    override suspend fun getAppLanguage(): Flow<Int> {
        return appPreferencesDataStore.getAppLanguage()
    }


}
