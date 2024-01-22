package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterWordsBottomSheet(viewModel:SenderSmsListViewModel, sheetState: ModalBottomSheetState){

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.common_filter,
        description = R.string.common_long_click_to_modify,
        list = viewModel.getFilterOptions(uiState.selectedFilter),
        trailIcon = {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.clickable {
                coroutineScope.launch {
                    BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                }
                viewModel.navigateToFilterScreen(uiState.sender.id,null)
            })
        },
        canUnSelect = true,
        onSelectItem = {
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
            if (it.isSelected) {
                viewModel.updateSelectedFilterWord(it)
            } else {
                viewModel.updateSelectedFilterWord(null)
            }
        },
        onLongPress = {
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
            viewModel.navigateToFilterScreen(uiState.sender.id,it.id)
        }


    )

}