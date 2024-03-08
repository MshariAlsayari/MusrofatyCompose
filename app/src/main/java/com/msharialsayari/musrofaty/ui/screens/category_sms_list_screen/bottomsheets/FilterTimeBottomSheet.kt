package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui_component.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager

@Composable
fun FilterTimeBottomSheet(viewModel: CategorySmsListViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val timeOption = uiState.selectedFilterTimeOption
    val startDate = uiState.startDate
    val endDate = uiState.endDate

    TimePeriodsBottomSheet(selectedItem = timeOption, startDate = startDate , endDate = endDate) {
        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.updateBottomSheetType(CategorySmsListBottomSheetType.DATE_PICKER)
        } else {
            viewModel.updateBottomSheetType(null)
            if(timeOption?.id == it.id){
                viewModel.updateSelectedFilterTimePeriods(null)
            }else{
                viewModel.updateSelectedFilterTimePeriods(it)
            }

            SharedPreferenceManager.setFilterTimePeriod(context,it.id)
        }

    }

}