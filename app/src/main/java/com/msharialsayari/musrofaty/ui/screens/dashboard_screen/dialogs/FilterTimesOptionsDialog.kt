package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext

import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.DialogComponent
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager

@Composable
fun FilterTimesOptionsDialog(viewModel: DashboardViewModel){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val selectedItem = uiState.selectedFilterTimeOption
    val startDate = uiState.startDate
    val endDate = uiState.endDate


    DialogComponent.TimeOptionDialog(
        selectedItem = selectedItem,
        startDate = startDate,
        endDate = endDate
    ) {
        viewModel.updateDialogType(null)
        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.updateDialogType(DashboardDialogType.DATE_PICKER)
        } else {
            viewModel.onFilterTimeOptionSelected(it)
            SharedPreferenceManager.setFilterTimePeriod(context,it.id)
            viewModel.getData(context)
        }
    }

}