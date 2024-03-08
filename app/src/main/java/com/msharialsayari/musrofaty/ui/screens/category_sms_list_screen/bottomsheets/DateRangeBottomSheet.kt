package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui_component.RangeDatePickerCompose
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils


@Composable
internal fun DateRangeBottomSheet(viewModel: CategorySmsListViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val startDate = uiState.startDate
    val endDate = uiState.endDate


    RangeDatePickerCompose(
        startDate = startDate,
        endDate = endDate
    ) { selectedStartDate, selectedEndDate ->
        viewModel.updateBottomSheetType(null)
        viewModel.onDatePeriodsSelected(selectedStartDate, selectedEndDate)
        viewModel.updateSelectedFilterTimePeriods(
            SelectedItemModel(
                id = DateUtils.FilterOption.RANGE.id,
                value = DateUtils.formattedRangeDate(
                    start = selectedStartDate,
                    end = selectedEndDate
                ),
                isSelected = true
            )
        )
    }
}