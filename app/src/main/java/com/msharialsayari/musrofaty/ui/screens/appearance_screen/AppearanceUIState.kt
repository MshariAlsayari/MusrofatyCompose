package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel

data class AppearanceUIState(
    val appAppearance: Theme = Theme.DEFAULT,
    val appLanguage: Language = Language.DEFAULT
)