package com.msharialsayari.musrofaty.business_layer.domain_layer.settings


import com.msharialsayari.musrofaty.business_layer.AppPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val appPreferencesDataStore: AppPreferencesDataStore,
) : SettingsRepository {

    companion object {
        private val TAG = SettingsRepositoryImpl::class.java.simpleName
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
