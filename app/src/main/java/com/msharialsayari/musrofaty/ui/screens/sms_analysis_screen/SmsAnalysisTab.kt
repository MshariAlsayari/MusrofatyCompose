package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun SmsAnalysisTab(viewModel: SmsAnalysisViewModel, word: WordDetectorType) {
    val uiState by viewModel.uiState.collectAsState()

    val list = when (word) {
        WordDetectorType.INCOME_WORDS -> uiState.incomesList?.collectAsState(initial = emptyList())?.value
            ?: emptyList()
        WordDetectorType.EXPENSES_PURCHASES_WORDS -> uiState.expensesPurchasesList?.collectAsState(initial = emptyList())?.value
            ?: emptyList()
        WordDetectorType.CURRENCY_WORDS -> uiState.currencyList?.collectAsState(initial = emptyList())?.value
            ?: emptyList()

        WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS -> uiState.expensesOutgoingTransferList?.collectAsState(initial = emptyList())?.value
            ?: emptyList()
        WordDetectorType.EXPENSES_PAY_BILLS_WORDS -> uiState.expensesPayBillsList?.collectAsState(initial = emptyList())?.value
            ?: emptyList()
    }

    val description = when (word) {
        WordDetectorType.INCOME_WORDS -> stringResource(id = R.string.tab_income_description)
        WordDetectorType.EXPENSES_PURCHASES_WORDS -> stringResource(id = R.string.tab_expenses_description)
        WordDetectorType.CURRENCY_WORDS -> stringResource(id = R.string.tab_currency_description)
        WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS -> stringResource(id = R.string.tab_outgoing_transfer_description)
        WordDetectorType.EXPENSES_PAY_BILLS_WORDS -> stringResource(id = R.string.tab_pay_bill_description)
    }

    Column(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.default_margin16))
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
    ) {

        TextComponent.PlaceholderText(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
            text = description
        )
        WordsDetectorListCompose(viewModel, list)
    }


}