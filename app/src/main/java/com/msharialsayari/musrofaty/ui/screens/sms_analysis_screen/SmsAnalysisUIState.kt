package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import kotlinx.coroutines.flow.Flow

data class SmsAnalysisUIState(
    var currencyList: Flow<List<WordDetectorEntity>>? = null,
    var expensesList: Flow<List<WordDetectorEntity>>? = null,
    var incomesList: Flow<List<WordDetectorEntity>>? = null,
)