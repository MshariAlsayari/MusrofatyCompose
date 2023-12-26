package com.simplemobiletools.calendar.domain.settings

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.SettingsRepository
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UpdateAppAppearanceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    @ApplicationContext val context: Context
) {

    suspend operator fun invoke(appAppearance: Int) {
        val theme = Theme.getThemeById(appAppearance)
         settingsRepository.setAppAppearance(appAppearance)
        SharedPreferenceManager.storeTheme(context,theme)
    }
}
