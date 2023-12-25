package com.msharialsayari.musrofaty.business_layer


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.msharialsayari.musrofaty.business_layer.AppPreferencesDataStore.PreferenceKeys.APP_APPEARANCE
import com.msharialsayari.musrofaty.business_layer.AppPreferencesDataStore.PreferenceKeys.APP_LANGUAGE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shared_preference")


    suspend fun setAppAppearance(appearance: Int) {
        context.dataStore.edit {
            it[APP_APPEARANCE] = appearance
        }

    }

    suspend fun setAppLanguage(language: Int) {
        context.dataStore.edit {
            it[APP_LANGUAGE] = language
        }
    }


    fun getAppAppearance(): Flow<Int> {
        return context.dataStore.data.map {
            it[APP_APPEARANCE] ?: 0
        }
    }

    fun getAppLanguage(): Flow<Int> {
        return context.dataStore.data.map {
            it[APP_LANGUAGE] ?: 0
        }
    }


    private object PreferenceKeys {
        val APP_APPEARANCE = intPreferencesKey("appAppearance")
        val APP_LANGUAGE = intPreferencesKey("appLanguage")
    }
}
