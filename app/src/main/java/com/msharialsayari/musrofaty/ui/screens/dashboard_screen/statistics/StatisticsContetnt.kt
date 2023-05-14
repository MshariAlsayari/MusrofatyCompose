package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel


@Composable
fun StatisticsContent(modifier: Modifier=Modifier, viewModel: DashboardViewModel) {
    LazyColumn(
        modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.default_margin40)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
        state = rememberLazyListState(),
    ) {

        item {
            FinancialContent(viewModel)
        }

        item{
            CategoriesContent(viewModel)
        }


    }
}