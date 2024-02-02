package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import kotlinx.coroutines.flow.Flow

data class SmsAnalysisUIState(
    var expensesPurchasesList: Flow<List<WordDetectorEntity>>? = null,
    var expensesOutgoingTransferList: Flow<List<WordDetectorEntity>>? = null,
    var expensesPayBillsList: Flow<List<WordDetectorEntity>>? = null,
    var expensesWithdrawalATMList: Flow<List<WordDetectorEntity>>? = null,
    var incomesList: Flow<List<WordDetectorEntity>>? = null,
    var amountsList: Flow<List<WordDetectorEntity>>? = null,
    var storesList: Flow<List<WordDetectorEntity>>? = null,
    var currencyList: Flow<List<WordDetectorEntity>>? = null,
)