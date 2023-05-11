package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.bottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.FinancialStatistics
import com.msharialsayari.musrofaty.ui_component.FinancialStatisticsModel
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics


@Composable
fun BottomSheetFinancialCompose(viewModel: DashboardViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val financialList = uiState.financialStatistics.values.toList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),

    ) {
        BottomSheetSectionHeader(viewModel, R.string.tab_financial_statistics)
        when {
            uiState.isFinancialStatisticsSmsPageLoading ->  ProgressBar.CircleProgressBar()
            financialList.isEmpty()                     ->  EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
            else                                        ->  FinancialCompose(viewModel,financialList)
        }

    }




}


@Composable
private fun FinancialCompose(viewModel: DashboardViewModel, financialList: List<FinancialStatistics>) {
    val context = LocalContext.current
    Column {
        financialList.forEach {
            FinancialStatistics(
                model = FinancialStatisticsModel(
                    filterOption = viewModel.getFilterTimeOption(context),
                    currency = it.currency,
                    total = it.expenses.plus(it.income),
                    incomeTotal = it.income,
                    expensesTotal = it.expenses,
                )
            )
        }

    }

}