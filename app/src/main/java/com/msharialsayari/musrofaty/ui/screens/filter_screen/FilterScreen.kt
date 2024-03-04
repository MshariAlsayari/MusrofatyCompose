package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.LogicOperators
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.AddFilterBottomSheetCompose
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.AmountFilterBottomSheetCompose
import com.msharialsayari.musrofaty.ui.screens.filter_screen.bottomsheets.FilterBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(){
    val viewModel:FilterViewModel = hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val selectedFilter = remember { mutableStateOf<FilterWordModel?>(null) }
    val bottomSheetType =  uiState.bottomSheetType

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
        viewModel.openBottomSheet(null)
    }


    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            when(bottomSheetType){
                FilterBottomSheetType.WORD ->   AddFilterBottomSheetCompose(item = selectedFilter.value) { item, newString ->
                    selectedFilter.value = null
                    viewModel.openBottomSheet(null)
                    viewModel.addFilter(item, newString.trim())
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            false
                        )
                    }
                }
                FilterBottomSheetType.AMOUNT -> AmountFilterBottomSheetCompose(item = uiState.filterAmountModel) { item, newString ->
                    selectedFilter.value = null
                    viewModel.openBottomSheet(null)
                    coroutineScope.launch {
                        viewModel.addAmountFilter(item, newString.trim())
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            false
                        )
                    }
                }
                null -> {}
            }

        },
    ){

        Scaffold(
            topBar = {
                AppBarComponent.TopBarComponent(
                    title = Screen.FilterScreen.title,
                    onArrowBackClicked = { viewModel.navigateUp() }
                )

            },
            scaffoldState = scaffoldState
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                ) {
                FilterTitle(viewModel)
                FiltersInstructions()
                FilterAmountSection(viewModel = viewModel, sheetState = sheetState)
                FilterWordSection (viewModel = viewModel, sheetState = sheetState)
                Spacer(modifier = Modifier.weight(1f))
                BtnAction(viewModel)
            }
        }

    }
}

@Composable
fun FiltersInstructions() {

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterVertically)
    ){
        TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_one_click))
        TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_double_click))
        TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_slide))
        TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_and))
        TextComponent.PlaceholderText(text = stringResource(id = R.string.filter_or))
    }

}

@Composable
fun FilterTitle(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val title = uiState.filterModel.title
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = title,
        label = R.string.filter_title,
        errorMsg = uiState.titleValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onFilterTitleChanged(it)
        }
    )

}




@ExperimentalComposeUiApi
@Composable
fun ListHeader(
    title:String,
    icon: ImageVector,
    onAddFilterIconClicked:()->Unit) {


    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin16)), horizontalArrangement = Arrangement.SpaceBetween) {
                TextComponent.HeaderText(text = title)
                Icon(icon, contentDescription = null, modifier = Modifier.clickable {
                    onAddFilterIconClicked()
                })
            }

        DividerComponent.HorizontalDividerComponent()


    }

}

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun FiltersList(
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



@Composable
fun BtnAction(viewModel: FilterViewModel){
    val uiState                           by viewModel.uiState.collectAsState()

    Row {
        ButtonComponent.ActionButton(
            modifier =Modifier.weight(1f),
            text = if (uiState.isCreateNewFilter) R.string.common_create else R.string.common_save,
            onClick = {
                if (viewModel.validate()) {
                   viewModel.onCreateBtnClicked()
                   viewModel.navigateUp()
                }
            }

        )

        if (!uiState.isCreateNewFilter)
            ButtonComponent.ActionButton(
                modifier = Modifier.weight(1f),
                color = MusrofatyTheme.colors.deleteActionColor,
                text = R.string.common_delete,
                onClick = {
                    viewModel.onDeleteBtnClicked()
                    viewModel.navigateUp()
                }

            )

    }


}

@Composable
fun EmptyCompose(){
    Box(contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent()
    }

}



