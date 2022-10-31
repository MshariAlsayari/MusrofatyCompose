package com.msharialsayari.musrofaty.ui.screens.content_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldComponent

@Composable
fun ContentScreen(contentId:Int, onDone:()->Unit,onBackPressed:()->Unit){
    val viewModel: ContentViewModel = hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit){
       viewModel.getContent(contentId)
    }

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.ContentScreen.title,
                onArrowBackClicked = onBackPressed
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            ValueArTextField(viewModel)
            ValueEnTextField(viewModel)
            Spacer(modifier = Modifier.weight(1f))
            BtnAction(viewModel, onDone)

        }
    }




}


@Composable
fun ValueArTextField(viewModel: ContentViewModel){

    val uiState                           by viewModel.uiState.collectAsState()
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = uiState.valueAr,
        label = R.string.common_arabic,
        errorMsg = uiState.valueArValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onArabicValueChanged(it)
        }
    )

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ValueEnTextField(viewModel: ContentViewModel){

    val uiState                           by viewModel.uiState.collectAsState()
    TextFieldComponent.BoarderTextFieldComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_margin16)),
        textValue = uiState.valueEn,
        label = R.string.common_english,
        errorMsg = uiState.valueEnValidationModel.errorMsg,
        onValueChanged = {
            viewModel.onEnglishValueChanged( it)


        }
    )

}






@Composable
fun BtnAction(viewModel: ContentViewModel, onDone:()->Unit){
    val uiState                           by viewModel.uiState.collectAsState()

    Row {
        ButtonComponent.ActionButton(
            modifier = Modifier.weight(1f),
            text =  R.string.common_save,
            onClick = {

                if (viewModel.validate()) {
                        viewModel.updateContent()
                        onDone()

                }
            }

        )


            ButtonComponent.ActionButton(
                modifier = Modifier.weight(1f),
                color= R.color.delete_action_color,
                text =  R.string.common_delete,
                onClick = {
                    viewModel.deleteContent()
                    onDone()
                }

            )

    }


}