package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.LogicOperators
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.RowComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FilterWordSection(
    modifier: Modifier = Modifier,
    viewModel: FilterViewModel,
    onWordRowClicked: (FilterWordModel?) -> Unit) {


    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        SectionHeader(
            title = stringResource(id = R.string.filter_add_word),
            icon = Icons.Default.Add
        ) {
            viewModel.openBottomSheet(FilterBottomSheetType.WORD)
            onWordRowClicked(null)
        }
        FiltersList(viewModel) {
            viewModel.openBottomSheet(FilterBottomSheetType.WORD)
            onWordRowClicked(it)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
private fun FiltersList(
    viewModel: FilterViewModel,
    onWordRowClicked: (FilterWordModel) -> Unit
) {


    val uiState by viewModel.uiState.collectAsState()
    val filterWords = uiState.filterWords

    val deleteAction = Action<FilterWordModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete)) },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            viewModel.deleteFilter(item)
        })


    VerticalEasyList(
        modifier = Modifier,
        list = filterWords,
        view = { item ->
            RowComponent.FilterWordRow(
                title = item.word,
                value = if(viewModel.isLastItem(item)) null else item.logicOperator.name
            )
        },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->
            onWordRowClicked(item)
        },
        onItemDoubleClicked = {item, position ->
            if (item.logicOperator == LogicOperators.OR) {
                viewModel.changeLogicOperator(item, LogicOperators.AND)
            } else {
                viewModel.changeLogicOperator(item, LogicOperators.OR)
            }
        },
        startActions = listOf(deleteAction),
        emptyView = { EmptyCompose() },
    )

}