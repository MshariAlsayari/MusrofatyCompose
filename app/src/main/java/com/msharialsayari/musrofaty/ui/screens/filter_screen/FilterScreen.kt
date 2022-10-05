package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListViewModel
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterScreen(senderId:Int , filterId:Int?){
    val viewModel:FilterViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState                           by viewModel.uiState.collectAsState()
    val textFieldBottomSheetValue = remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = Unit){
        filterId?.let { viewModel.getFilter(it) }
        uiState.isCreateNewFilter = filterId == null
        uiState.senderId = senderId
    }




    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
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
        sheetState = sheetState,
        sheetContent = {
            val model = TextFieldBottomSheetModel(
                title = R.string.filter_add_word,
                textFieldValue = textFieldBottomSheetValue.value,
                buttonText = R.string.common_add,
                onActionButtonClicked = { value ->
                    viewModel.addFilterWord(value)

                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)

                    }
                },

                )

            BottomSheetComponent.TextFieldBottomSheetComponent(model = model)

        },
        modifier = Modifier.fillMaxSize()
    ){

        Column(
            modifier = Modifier.fillMaxSize()) {
            FilterTitle(viewModel)
            FilterList(viewModel, onAddFilterClicked = {
                textFieldBottomSheetValue.value = it
                coroutineScope.launch {
                    handleVisibilityOfBottomSheet(sheetState, true)

                }
            })
            BtnAction(viewModel)

        }

    }





}

@Composable
fun FilterTitle(viewModel: FilterViewModel){

    val uiState                           by viewModel.uiState.collectAsState()
    val textState = remember { mutableStateOf(uiState.title) }
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = textState.value,
        label = R.string.filter_title,
        onValueChanged = {
            textState.value = it
            viewModel.onFilterTitleChanged( textState.value)


        }
    )

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterList(viewModel: FilterViewModel,onAddFilterClicked:(String)->Unit){

    val uiState                           by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()

        LazyColumn(
            modifier = Modifier,
            state = listState,
        ) {

            itemsIndexed(items = uiState.words) { index, item, ->
                TextComponent.BodyText(
                    modifier = Modifier
                        .padding(
                            dimensionResource(
                                id = R.dimen.default_margin16
                            )
                        )
                        .clickable {
                            onAddFilterClicked(item)
                        },
                    text = item )
                DividerComponent.HorizontalDividerComponent()
            }

            item {
                AddFilter(onAddFilterClicked = onAddFilterClicked)
            }
        }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddFilter(onAddFilterClicked:(String)->Unit){

    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onAddFilterClicked("")
        }) {
        DividerComponent.HorizontalDividerComponent()
        ListItem(
            text = { TextComponent.BodyText(text = stringResource(id = R.string.common_add)) },
            icon = {Icon( Icons.Default.Add, contentDescription = null)}
        )

    }

}

@Composable
fun BtnAction(viewModel: FilterViewModel){
    val uiState                           by viewModel.uiState.collectAsState()
   ButtonComponent.ActionButton(
       text = if (uiState.isCreateNewFilter) R.string.common_create else R.string.common_save,
       onClick = {
           if (uiState.isCreateNewFilter){
               viewModel.onCreateBtnClicked()
           }else{
               viewModel.onSaveBtnClicked()
           }
       }

   )

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
suspend fun handleVisibilityOfBottomSheet(sheetState: ModalBottomSheetState, show: Boolean) {

    if (show) {
        sheetState.show()
    } else {
        sheetState.hide()
    }

}


//val deleteAction = Action<String>(
//    { TextComponent.BodyText(text = stringResource(id = R.string.common_delete)) },
//    { ActionIcon(id = R.drawable.ic_delete) },
//    backgroundColor = colorResource(R.color.deletAction),
//    onClicked = { position, item ->
//        viewModel.removeWordFromFilter(item)
//
//    })
//
//Column {
//    VerticalEasyList(
//        modifier= Modifier.wrapContentSize(),
//        list = uiState.words,
//        view = {
//            TextComponent.BodyText(
//                modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)),
//                text = it
//            )
//
//        },
//        dividerView = { DividerComponent.HorizontalDividerComponent() },
//        onItemClicked = { item, position -> onAddFilterClicked(item) },
//        startActions = listOf(deleteAction),
//        loadingProgress = { ProgressBar.CircleProgressBar() },
//    )
//
//    AddFilter(onAddFilterClicked = onAddFilterClicked)
//
//}