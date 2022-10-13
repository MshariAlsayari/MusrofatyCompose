package com.msharialsayari.musrofaty.ui.screens.apperance_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.RowComponent
import org.intellij.lang.annotations.Language

@Composable
fun AppearanceScreen(){
    val viewModel:AppearanceViewModel = hiltViewModel()


    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
        dimensionResource(id = R.dimen.default_margin10))) {
        ThemeCompose(viewModel=viewModel, onClick = {})
        LanguageCompose(viewModel=viewModel, onClick = {})
    }

}

@Composable
fun ThemeCompose(viewModel: AppearanceViewModel, onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.theme),
        body = stringResource(id = R.string.pref_appearance_summary),
    )

}


@Composable
fun LanguageCompose(viewModel: AppearanceViewModel,onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.language),
        body = uiState.currentLanguageOption,
    )

}