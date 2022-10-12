package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType


@Composable
fun SmsAnalysisTab(viewModel:SmsAnalysisViewModel, word:WordDetectorType){
    val uiState by viewModel.uiState.collectAsState()

    val list = when(word){
        WordDetectorType.INCOME_WORDS -> uiState.incomesList?.collectAsState(initial = emptyList())?.value ?: emptyList()
        WordDetectorType.EXPENSES_WORDS -> uiState.expensesList?.collectAsState(initial = emptyList())?.value ?: emptyList()
        WordDetectorType.CURRENCY_WORDS -> uiState.currencyList?.collectAsState(initial = emptyList())?.value ?: emptyList()
    }

    WordsDetectorListCompose(viewModel, list)

}