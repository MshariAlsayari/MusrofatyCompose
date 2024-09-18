package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import kotlinx.coroutines.flow.Flow

data class SmsAnalysisUIState(
    var selectedTab:Int = 0,
    var list: Flow<List<WordDetectorEntity>>? = null,
)