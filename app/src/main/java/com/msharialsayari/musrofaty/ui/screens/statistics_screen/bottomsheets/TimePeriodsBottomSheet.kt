package com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimePeriodsBottomSheet(viewModel: StatisticsViewModel, sheetState: ModalBottomSheetState){

    val uiState by viewModel.uiState.collectAsState()
    val selectedItem = uiState.selectedTimePeriod
    val startDate = uiState.startDate
    val endDate = uiState.endDate
    val coroutineScope = rememberCoroutineScope()

    TimePeriodsBottomSheet(
        title = R.string.common_period_time,
        selectedItem = selectedItem,
        ignoreFilterOption = DateUtils.FilterOption.ALL,
        startDate = startDate,
        endDate = endDate
    ) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
        }
        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.updateBottomSheetType(BottomSheetType.DATE_PICKER)
        } else {
            if (uiState.selectedTimePeriod?.id == it.id) {
                viewModel.updateSelectedFilterTimePeriods(null)
            } else {
                viewModel.updateSelectedFilterTimePeriods(it)
            }

            viewModel.onDatePeriodsSelected(0,0)
        }
    }

}