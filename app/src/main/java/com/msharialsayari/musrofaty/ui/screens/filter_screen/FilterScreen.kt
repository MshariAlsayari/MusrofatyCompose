package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterWordModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(){
    val viewModel:FilterViewModel = hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.hide()
            }
        }

    } else {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.show()
            }
        }

    }



    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = sheetState,
        sheetContent = {
            AddFilterBottomSheetCompose(onActionClicked = {
                    word -> viewModel.addFilter(word.trim())
                coroutineScope.launch {
                    BottomSheetComponent.handleVisibilityOfBottomSheet(
                        sheetState,
                        false
                    )
                }
            }
            )

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
                    .fillMaxSize()) {
                FilterTitle(viewModel)
                FiltersListHeader(onAddFilterIconClicked = {
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            !sheetState.isVisible
                        )
                    }
                })
                FiltersList(viewModel)
                Spacer(modifier = Modifier.weight(1f))
                BtnAction(viewModel)

            }
        }

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
fun FiltersListHeader(onAddFilterIconClicked:()->Unit) {


    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.default_margin16)), horizontalArrangement = Arrangement.SpaceBetween) {
                TextComponent.HeaderText(text = stringResource(id = R.string.filter_add_word))
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.clickable {
                    onAddFilterIconClicked()
                })
            }

        DividerComponent.HorizontalDividerComponent()


    }

}

@Composable
fun AddFilterBottomSheetCompose(onActionClicked:(String)->Unit){
    val model = TextFieldBottomSheetModel(
        title = R.string.filter_add_word,
        textFieldValue =  "",
        buttonText = R.string.common_add,
        onActionButtonClicked = { value ->
            onActionClicked(value)
        })

    BottomSheetComponent.TextFieldBottomSheetComponent(model = model)

}


@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun FiltersList(viewModel: FilterViewModel){


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
                    word = item.word,
                    logicOperators = if(viewModel.isLastItem(item)) null else item.logicOperator
                ) {
                    viewModel.changeLogicOperator(item, it)
                }
            },
            dividerView = { DividerComponent.HorizontalDividerComponent() },
            onItemClicked = { item, position ->

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



