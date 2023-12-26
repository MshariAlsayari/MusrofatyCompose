package com.msharialsayari.musrofaty.business_layer.domain_layer.settings

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAppAppearanceUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(): Flow<Int> {
        return settingsRepository.getAppAppearance()
    }
}
