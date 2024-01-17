package com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
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


    if (uiState.showStartDatePicker) {
        ComposeDatePicker(
            title = stringResource(id = R.string.common_start_date),
            onDone = {
                viewModel.onStartDateSelected(DateUtils.toMilliSecond(it))
            },
            onDismiss = {

            }
        )
    }

    if (uiState.showEndDatePicker) {
        ComposeDatePicker(
            title = stringResource(id = R.string.common_end_date),
            onDone = {
                viewModel.onEndDateSelected(DateUtils.toMilliSecond(it))
                viewModel.updateSelectedFilterTimePeriods(SelectedItemModel(id = DateUtils.FilterOption.RANGE.id, value = ""))
                viewModel.dismissAllDatePicker()
            },
            onDismiss = {
                viewModel.dismissAllDatePicker()
            }
        )
    }

    TimePeriodsBottomSheet(
        selectedItem = selectedItem,
        startDate = startDate,
        endDate = endDate
    ) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
        }

        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.showStartDatePicker()
        } else {
            if (uiState.selectedTimePeriod?.id == it.id) {
                viewModel.updateSelectedFilterTimePeriods(null)
            } else {
                viewModel.updateSelectedFilterTimePeriods(it)
            }
            viewModel.dismissAllDatePicker()
        }
    }

}