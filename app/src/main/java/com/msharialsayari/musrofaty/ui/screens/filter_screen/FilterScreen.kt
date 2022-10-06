package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldComponent


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterScreen(senderId:Int , filterId:Int?, onDone:()->Unit){
    val viewModel:FilterViewModel = hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit){
        filterId?.let { viewModel.getFilter(it) }
        uiState.isCreateNewFilter = filterId == null
        uiState.senderId = senderId
    }


    Column(
        modifier = Modifier.fillMaxSize()) {
        FilterTitle(viewModel)
        FilterList(viewModel)
        BtnAction(viewModel, onDone)

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
fun FilterList(viewModel: FilterViewModel){

    val uiState                           by viewModel.uiState.collectAsState()


    val textState = remember { mutableStateOf(uiState.title) }
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = textState.value,
        label = R.string.filter_add_word,
        onValueChanged = {
            textState.value = it
            viewModel.onFilterWordChanged( textState.value)


        }
    )

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
fun BtnAction(viewModel: FilterViewModel, onDone:()->Unit){
    val uiState                           by viewModel.uiState.collectAsState()
   ButtonComponent.ActionButton(
       text = if (uiState.isCreateNewFilter) R.string.common_create else R.string.common_save,
       onClick = {
           if (uiState.isCreateNewFilter){
               viewModel.onCreateBtnClicked()
               onDone()
           }else{
               viewModel.onSaveBtnClicked()
               onDone()
           }
       }

   )

}



