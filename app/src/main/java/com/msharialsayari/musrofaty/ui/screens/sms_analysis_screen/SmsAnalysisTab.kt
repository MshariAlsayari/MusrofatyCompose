package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

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
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun SmsAnalysisTab(
    viewModel: SmsAnalysisViewModel,
    onItemClicked: (WordDetectorEntity) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val index = uiState.selectedTab
    val filter = viewModel.getWordDetectorByIndex(index)
    val list =
        uiState.list?.collectAsState(initial = emptyList())?.value?.filter { it.type == filter.name }
            ?: emptyList()
    val description = when (viewModel.getWordDetectorByIndex(uiState.selectedTab)) {
        WordDetectorType.CURRENCY_WORDS -> stringResource(id = R.string.tab_currency_description)
        WordDetectorType.AMOUNT_WORDS -> stringResource(id = R.string.tab_amount_description)
        WordDetectorType.STORE_WORDS -> stringResource(id = R.string.tab_store_description)
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
            onItemClicked = {
                onItemClicked(it)
            },
            onDelete = { id -> viewModel.deleteWordDetector(id) })
    }


}