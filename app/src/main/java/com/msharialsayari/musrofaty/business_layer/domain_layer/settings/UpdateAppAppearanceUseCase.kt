package com.simplemobiletools.calendar.domain.settings

import javax.inject.Inject

class UpdateAppAppearanceUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(appAppearance: Int) {
        return settingsRepository.setAppAppearance(appAppearance)
    }
}
