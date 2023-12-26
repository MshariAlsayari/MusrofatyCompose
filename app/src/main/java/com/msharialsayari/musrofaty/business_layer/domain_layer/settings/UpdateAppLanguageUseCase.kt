package com.msharialsayari.musrofaty.business_layer.domain_layer.settings

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.SettingsRepository
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateAppLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext val context: Context

) {

    suspend operator fun invoke(appLanguage: Int) {
        val language = Language.getLanguageById(appLanguage)
         settingsRepository.setAppLanguage(appLanguage)
         SharedPreferenceManager.storeLanguage(context, language)

    }
}