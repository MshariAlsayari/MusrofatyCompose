package com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.RangeDatePickerCompose
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateRangeBottomSheet(
    viewModel: StatisticsViewModel,
    sheetState: ModalBottomSheetState,
){

    val uiState by viewModel.uiState.collectAsState()
    val startDate = uiState.startDate
    val endDate = uiState.endDate
    val coroutineScope = rememberCoroutineScope()

    RangeDatePickerCompose(
        startDate = startDate,
        endDate = endDate
    ){ selectedStartDate , selectedEndDate ->
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
        }
        viewModel.updateBottomSheetType(null)
        viewModel.onDatePeriodsSelected(selectedStartDate,selectedEndDate)
        viewModel.updateSelectedFilterTimePeriods(SelectedItemModel(id = DateUtils.FilterOption.RANGE.id, value = DateUtils.formattedRangeDate(start =selectedStartDate , end = selectedEndDate), isSelected = true))
    }
}