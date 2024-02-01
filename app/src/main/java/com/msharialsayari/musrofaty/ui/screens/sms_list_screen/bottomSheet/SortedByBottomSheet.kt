package com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListViewModel

@Composable
fun SortedByBottomSheet(viewModel: SmsListViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val amount = uiState.selectedSortedByAmount
    val smsList = uiState.smsList

    com.msharialsayari.musrofaty.ui_component.SortedByBottomSheet(
        selectedSortedByAmount = amount,

        onSelectAmount = {
          viewModel.updateSelectedSortByAmount(it)
          viewModel.updateList(viewModel.getSortedList(smsList))
          viewModel.updateSelectedBottomSheet(null)
        },
    )
}
