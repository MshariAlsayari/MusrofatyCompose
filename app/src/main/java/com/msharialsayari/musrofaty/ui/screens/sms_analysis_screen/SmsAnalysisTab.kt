package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun SmsScreenTabRow(viewModel: SmsAnalysisViewModel, isSmsAnalysisScreen: Boolean){
    val uiState by viewModel.uiState.collectAsState()
    val tabTitles = viewModel.getTabList(isSmsAnalysisScreen)
    val isSmsAnalyticsScreen = uiState.isSmsAnalyticsScreen
    val tabIndex = uiState.selectedTab

    if(isSmsAnalyticsScreen){
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabIndex,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = MaterialTheme.colors.secondary,
                    height = TabRowDefaults.IndicatorHeight
                )
            }
        ) {
            tabTitles.forEachIndexed { index, stringResId ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    selected = tabIndex == index,
                    onClick = {
                        viewModel.updateSelectedTab(index)
                    },
                    text = {
                        TextComponent.ClickableText(
                            text = stringResource(id = stringResId),
                            color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                id = R.color.light_gray
                            )
                        )
                    })
            }
        }
    }else{
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabIndex,
            edgePadding = 0.dp,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = MaterialTheme.colors.secondary,
                    height = TabRowDefaults.IndicatorHeight
                )
            }
        ) {
            tabTitles.forEachIndexed { index, stringResId ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    selected = tabIndex == index,
                    onClick = {
                        viewModel.updateSelectedTab(index)
                    },
                    text = {
                        TextComponent.ClickableText(
                            text = stringResource(id = stringResId),
                            color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                id = R.color.light_gray
                            )
                        )
                    })
            }
        }

    }

}

@Composable
fun SmsAnalysisTab(
    viewModel: SmsAnalysisViewModel,
    onItemClicked: (WordDetectorEntity) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val index = uiState.selectedTab
    val filter = viewModel.getWordDetectorByIndex(index)
    val list = uiState.list?.collectAsState(initial = emptyList())?.value?.filter { it.type == filter.name } ?: emptyList()
    val description = when (viewModel.getWordDetectorByIndex(uiState.selectedTab)) {
        WordDetectorType.CURRENCY_WORDS -> stringResource(id = R.string.tab_currency_description)
        WordDetectorType.AMOUNT_WORDS -> stringResource(id = R.string.tab_amount_description)
        WordDetectorType.STORE_WORDS -> stringResource(id = R.string.tab_store_description)
        WordDetectorType.INCOME_WORDS -> stringResource(id = R.string.tab_income_description)
        WordDetectorType.EXPENSES_PURCHASES_WORDS -> stringResource(id = R.string.tab_expenses_description)
        WordDetectorType.EXPENSES_OUTGOING_TRANSFER_WORDS -> stringResource(id = R.string.tab_outgoing_transfer_description)
        WordDetectorType.EXPENSES_PAY_BILLS_WORDS -> stringResource(id = R.string.tab_pay_bill_description)
        WordDetectorType.WITHDRAWAL_ATM_WORDS -> stringResource(id = R.string.tab_withdrawal_atm_description)
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