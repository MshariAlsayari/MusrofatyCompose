package com.msharialsayari.musrofaty.ui.screens.sms_types_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity

data class SmsTypesUiState(
    var selectedTab:Int = 0,
    var list: List<WordDetectorEntity> = emptyList()
)