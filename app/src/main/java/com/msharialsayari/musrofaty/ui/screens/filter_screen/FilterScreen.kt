package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterScreen(senderId:Int , filterId:Int?, onDone:()->Unit,onBackPressed:()->Unit){
    val viewModel:FilterViewModel = hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit){
        filterId?.let {
            viewModel.getFilter(it)
        }
    }
    uiState.isCreateNewFilter = filterId == null
    uiState.filterId = filterId ?: 0
    uiState.senderId = senderId



    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.FilterScreen.title,
                onArrowBackClicked = onBackPressed
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            FilterTitle(viewModel)
            FilterWord(viewModel)
            Spacer(modifier = Modifier.weight(1f))
            BtnAction(viewModel, onDone)

        }
    }










}

@Composable
fun FilterTitle(viewModel: FilterViewModel){

    val uiState                           by viewModel.uiState.collectAsState()
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = uiState.title,
        label = R.string.filter_title,
        errorMsg = uiState.titleValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onFilterTitleChanged(it)


        }
    )

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun FilterWord(viewModel: FilterViewModel){

    val uiState                           by viewModel.uiState.collectAsState()
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = uiState.words,
        label = R.string.filter_add_word,
        errorMsg = uiState.wordValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onFilterWordChanged( it)


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

    Row {
        ButtonComponent.ActionButton(
            modifier =Modifier.weight(1f),
            text = if (uiState.isCreateNewFilter) R.string.common_create else R.string.common_save,
            onClick = {

                if (viewModel.validate()) {
                    if (uiState.isCreateNewFilter) {
                        viewModel.onCreateBtnClicked()
                        onDone()
                    } else {
                        viewModel.onSaveBtnClicked()
                        onDone()
                    }
                }
            }

        )

        if (!uiState.isCreateNewFilter)
        ButtonComponent.ActionButton(
            modifier =Modifier.weight(1f),
            color= R.color.delete_action_color,
            text =  R.string.common_delete,
            onClick = {
                viewModel.onDeleteBtnClicked()
                onDone()
            }

        )

    }


}



