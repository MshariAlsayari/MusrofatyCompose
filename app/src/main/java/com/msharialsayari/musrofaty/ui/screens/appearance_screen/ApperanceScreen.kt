package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.ui_component.RowComponent
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppearanceScreen(onLanguageChanged:()->Unit,onThemeChanged:()->Unit){
    val viewModel:AppearanceViewModel = hiltViewModel()
    val isThemeBottomSheet = remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }



    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {

            if (isThemeBottomSheet.value) {
               ThemeBottomSheet(viewModel = viewModel, onSelect = {

                    viewModel.onThemeSelected(it)
                    onThemeChanged()
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }

                })
            } else {
                LanguageBottomSheet(viewModel = viewModel, onSelect = {

                    viewModel.onLanguageSelected(it)
                    onLanguageChanged()
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }

                })
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.default_margin10)
            )
        ) {
            ThemeCompose(viewModel = viewModel, onClick = {
                isThemeBottomSheet.value = true
                coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, true) }
            })
            LanguageCompose(viewModel = viewModel, onClick = {
                isThemeBottomSheet.value = false
                coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, true) }
            })
        }
    }

}

@Composable
fun ThemeCompose(viewModel: AppearanceViewModel, onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.theme),
        body = uiState.currentThemeOption,
        onClick = onClick
    )

}

@Composable
fun ThemeBottomSheet(viewModel: AppearanceViewModel, onSelect:(SelectedItemModel)->Unit){
    val uiState by viewModel.uiState.collectAsState()

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.theme,
        list = viewModel.getThemeOptions(uiState.selectedCurrentTheme),
        onSelectItem = {
            onSelect(it)
        },



        )

}


@Composable
fun LanguageCompose(viewModel: AppearanceViewModel,onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.language),
        body = uiState.currentLanguageOption,
        onClick = onClick
    )

}

@Composable
fun LanguageBottomSheet(viewModel: AppearanceViewModel, onSelect:(SelectedItemModel)->Unit){
    val uiState by viewModel.uiState.collectAsState()

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.language,
        list = viewModel.getLanguageOptions(uiState.selectedCurrentLanguage),
        onSelectItem = {
            onSelect(it)
        },



    )

}

