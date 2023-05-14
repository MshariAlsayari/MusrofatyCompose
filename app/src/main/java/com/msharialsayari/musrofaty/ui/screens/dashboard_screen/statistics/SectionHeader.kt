package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.utils.DateUtils

@Composable
fun BottomSheetSectionHeader(viewModel: DashboardViewModel, @StringRes text:Int) {

    val uiState by viewModel.uiState.collectAsState()
    val filterTimeOption = uiState.selectedFilterTimeOption
    val filterText = if (filterTimeOption?.id == 5) DateUtils.formattedRangeDate(uiState.startDate,uiState.endDate) else stringResource(id = DateUtils.FilterOption.getFilterOption(filterTimeOption?.id).title)



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.default_margin20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextComponent.HeaderText(
            text = stringResource(id = text)
        )
        TextComponent.PlaceholderText(
            text = stringResource(id = R.string.common_filter_options) + ": " + filterText,
            alignment = TextAlign.End
        )
    }

}