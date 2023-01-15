package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.bottomSheet

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
fun BottomSheetLazyColumn(viewModel: DashboardViewModel){

    LazyColumn(
        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.default_margin40)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
        state = rememberLazyListState(),
    ) {

        item {
            BottomSheetFinancialCompose(viewModel)
        }

        item{
            BottomSheetCategoriesCompose(viewModel)
        }


    }

}