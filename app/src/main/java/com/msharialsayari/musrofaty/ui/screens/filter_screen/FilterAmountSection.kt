package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAmountModel
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.RowComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterAmountSection(
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAddAmountFilter = uiState.filterAmountModel == null
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        FilterAmountHeader(viewModel)
        if (isAddAmountFilter) {
            FilterAmountEmptyRow()
        } else {
            FilterAmountRow(viewModel)
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun FilterAmountHeader(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val isAddAmountFilter = uiState.filterAmountModel == null

    SectionHeader(
        title = stringResource(id = R.string.filter_add_amount),
        icon = if (isAddAmountFilter) Icons.Default.Add else null
    ) {
        if (isAddAmountFilter) {
            viewModel.openBottomSheet(FilterBottomSheetType.AMOUNT)
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
    ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
private fun FilterAmountRow(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val amount = uiState.filterAmountModel


    val deleteAction = Action<FilterAmountModel?>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete)) },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            viewModel.deleteFilterAmount()
        })

    VerticalEasyList(
        modifier = Modifier,
        list = listOf(amount),
        view = { item ->
            RowComponent.FilterWordRow(
                title = item?.amount ?: "",
                value =  if (item != null) stringResource(id = item.amountOperator.valueString) else ""
            )
        },
        onItemClicked = { item, position ->
            viewModel.openBottomSheet(FilterBottomSheetType.AMOUNT)
        },
        onItemDoubleClicked = {item, position ->
            viewModel.changeAmountLogicOperator()
        },
        startActions = listOf(deleteAction),
        emptyView = { EmptyCompose() },
    )


}