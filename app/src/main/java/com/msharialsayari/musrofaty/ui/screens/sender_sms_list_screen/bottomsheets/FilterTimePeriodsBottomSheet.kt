package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel


@Composable
fun FilterTimePeriodsBottomSheet(viewModel: SenderSmsListViewModel,
                                 onFilterSelected: (SelectedItemModel) -> Unit){

    val uiState by viewModel.uiState.collectAsState()
    BottomSheetComponent.TimeOptionsBottomSheet(
        selectedItem = uiState.selectedFilterTimeOption,
        startDate = uiState.startDate,
        endDate = uiState.endDate
    ) {
        onFilterSelected(it)

    }

}