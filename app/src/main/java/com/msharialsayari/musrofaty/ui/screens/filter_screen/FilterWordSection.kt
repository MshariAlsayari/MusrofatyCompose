package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun FilterWordSection(
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel,
    sheetState: ModalBottomSheetState
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val selectedFilter = remember { mutableStateOf<FilterWordModel?>(null) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ListHeader(
            title = stringResource(id = R.string.filter_add_word),
            icon = Icons.Default.Add
        ) {
            selectedFilter.value = null
            viewModel.openBottomSheet(FilterBottomSheetType.WORD)
            keyboardController?.hide()
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(
                    sheetState,
                    true
                )
            }
        }
        FiltersList(viewModel) {
            selectedFilter.value = it
            viewModel.openBottomSheet(FilterBottomSheetType.WORD)
            keyboardController?.hide()
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(
                    sheetState,
                    true
                )
            }
        }
    }

}