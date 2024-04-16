package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.TimePeriodsBottomSheet
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterTimeBottomSheet(viewModel:CategorySmsListViewModel, sheetState: ModalBottomSheetState){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val timeOption = uiState.selectedFilterTimeOption
    val startDate = uiState.startDate
    val endDate = uiState.endDate

    TimePeriodsBottomSheet(selectedItem = timeOption, startDate = startDate , endDate = endDate) {
        if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
            viewModel.updateBottomSheetType(CategorySmsListBottomSheetType.DATE_PICKER)
        } else {
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
            if(timeOption?.id == it.id){
                viewModel.updateSelectedFilterTimePeriods(null)
            }else{
                viewModel.updateSelectedFilterTimePeriods(it)
            }

            SharedPreferenceManager.setFilterTimePeriod(context,it.id)
        }

    }

}