package com.simplemobiletools.calendar.domain.settings

import javax.inject.Inject

class UpdateAppLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appLanguage: Int) {
        return settingsRepository.setAppLanguage(appLanguage)
    }
}