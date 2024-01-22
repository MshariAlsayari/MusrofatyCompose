package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterTimeBottomSheet(viewModel:SenderSmsListViewModel, sheetState: ModalBottomSheetState){

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val timeOption = uiState.selectedFilterTimeOption
    val startDate = uiState.startDate
    val endDate = uiState.endDate

    TimePeriodsBottomSheet(selectedItem = timeOption, startDate = startDate , endDate = endDate) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
        }
        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.showStartDatePicker()
        } else {
            if(timeOption?.id == it.id){
                viewModel.updateSelectedFilterTimePeriods(null)
            }else{
                viewModel.updateSelectedFilterTimePeriods(it)
            }
            viewModel.dismissAllDatePicker()
        }

    }

}