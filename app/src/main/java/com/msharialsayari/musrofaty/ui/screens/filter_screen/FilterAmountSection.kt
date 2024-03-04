package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.RowComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterAmountSection(
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel,
    sheetState: ModalBottomSheetState
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAddAmountFilter = uiState.filterAmountModel == null
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterAmountHeader(viewModel, sheetState)
        if (isAddAmountFilter) {
            FilterAmountEmptyRow()
        } else {
            FilterAmountRow(viewModel, sheetState)
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
private fun FilterAmountHeader(
    viewModel: FilterViewModel,
    sheetState: ModalBottomSheetState
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isAddAmountFilter = uiState.filterAmountModel == null


    ListHeader(
        title = stringResource(id = R.string.filter_add_amount),
        icon = if (isAddAmountFilter) Icons.Default.Add else Icons.Default.Clear
    ) {
        keyboardController?.hide()
        if (isAddAmountFilter) {
            viewModel.openBottomSheet(FilterBottomSheetType.AMOUNT)
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(
                    sheetState,
                    true
                )
            }
        } else {
            viewModel.deleteFilterAmount()
            viewModel.openBottomSheet(null)
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(
                    sheetState,
                    false
                )
            }
        }

    }
}

@Composable
private fun FilterAmountEmptyRow() {
    TextComponent.PlaceholderText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        alignment = TextAlign.Center,
        text = stringResource(id = R.string.no_result))
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun FilterAmountRow(
    viewModel: FilterViewModel,
    sheetState: ModalBottomSheetState
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedFilter = remember { mutableStateOf<FilterAmountModel?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val amount = uiState.filterAmountModel?.amount ?: ""
    val amountOperator = if (uiState.filterAmountModel != null) stringResource(id = uiState.filterAmountModel!!.amountOperator.valueString) else ""
    RowComponent.FilterWordRow(
        modifier = Modifier.combinedClickable(
            onClick = {
                selectedFilter.value = uiState.filterAmountModel
                viewModel.openBottomSheet(FilterBottomSheetType.AMOUNT)
                keyboardController?.hide()
                coroutineScope.launch {
                    BottomSheetComponent.handleVisibilityOfBottomSheet(
                        sheetState,
                        true
                    )
                }

            },
            onDoubleClick = {
                viewModel.changeAmountLogicOperator()
            }
        ),
        title = amount,
        value = amountOperator
    )

}