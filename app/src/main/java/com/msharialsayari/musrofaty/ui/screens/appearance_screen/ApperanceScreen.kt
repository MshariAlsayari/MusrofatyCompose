package com.msharialsayari.musrofaty.ui.screens.appearance_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch


@Composable
fun AppearanceScreen(navigatorViewModel: AppNavigatorViewModel = hiltViewModel()){

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.AppearanceScreen.title,
                onArrowBackClicked = {navigatorViewModel.navigateUp()}
            )

        }
    ) { innerPadding ->
        AppearanceContent(modifier = Modifier.padding(innerPadding))
    }

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppearanceContent(modifier: Modifier=Modifier){

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
                    viewModel.updateAppAppearance(Theme.getThemeById(it.id))
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                })
            } else {
                LanguageBottomSheet(viewModel = viewModel, onSelect = {
                    viewModel.updateAppLanguage(Language.getLanguageById(it.id))
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                })
            }
        },
        modifier = modifier.fillMaxSize()
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

    val body = stringArrayResource(id = R.array.theme_options )[uiState.appAppearance.id]

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.theme),
        body = body,
        onClick = onClick
    )

}

@Composable
fun ThemeBottomSheet(viewModel: AppearanceViewModel, onSelect:(SelectedItemModel)->Unit){
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.theme,
        list = viewModel.getThemeOptions(context),
        onSelectItem = {
            onSelect(it)
        },



        )

}


@Composable
fun LanguageCompose(viewModel: AppearanceViewModel,onClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()

    val body = stringArrayResource(id = R.array.language_options )[uiState.appLanguage.id]

    RowComponent.PreferenceRow(
        header = stringResource(id = R.string.language),
        body = body,
        onClick = onClick
    )

}

@Composable
fun LanguageBottomSheet(viewModel: AppearanceViewModel, onSelect:(SelectedItemModel)->Unit){
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.language,
        list = viewModel.getLanguageOptions(context),
        onSelectItem = {
            onSelect(it)
        },



    )

}

