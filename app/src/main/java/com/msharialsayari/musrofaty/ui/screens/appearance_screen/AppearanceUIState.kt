package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import com.msharialsayari.musrofaty.ui_component.SelectedItemModel

data class AppearanceUIState(
    var currentLanguageOption:String= "",
    var selectedCurrentLanguage: SelectedItemModel? = null,
    var currentThemeOption:String= "",
    var selectedCurrentTheme: SelectedItemModel? = null,
)