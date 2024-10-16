package com.msharialsayari.musrofaty.ui.screens.sms_types_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen.WordsDetectorListCompose
import com.msharialsayari.musrofaty.ui_component.TextComponent

@Composable
fun SmsTypesTab(
    viewModel: SmsTypesViewModel,
    onItemClicked: (WordDetectorEntity) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val list = viewModel.initialPage.collectAsState(initial = emptyList()).value
    val description = when (viewModel.getWordDetectorByIndex(uiState.selectedTab)) {
        WordDetectorType.INCOME_WORDS -> stringResource(id = R.string.tab_income_description)
        WordDetectorType.EXPENSES_PURCHASES_WORDS -> stringResource(id = R.string.tab_expenses_description)
        WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS -> stringResource(id = R.string.tab_outgoing_transfer_description)
        WordDetectorType.EXPENSES_PAY_BILLS_WORDS -> stringResource(id = R.string.tab_pay_bill_description)
        WordDetectorType.WITHDRAWAL_ATM_WORDS -> stringResource(id = R.string.tab_withdrawal_atm_description)
        else -> ""
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
        WordsDetectorListCompose(
            list = list,
            onItemClicked = onItemClicked,
            onDelete = { id -> viewModel.deleteWordDetector(id) })
    }


}