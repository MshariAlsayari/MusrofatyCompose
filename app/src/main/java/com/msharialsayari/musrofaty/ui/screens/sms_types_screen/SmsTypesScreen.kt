package com.msharialsayari.musrofaty.ui.screens.sms_types_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.WordDetectorType
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import kotlinx.coroutines.launch

@Composable
fun SmsTypesScreen(navigatorViewModel: AppNavigatorViewModel = hiltViewModel()){

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SmsTypesScreen.title,
                onArrowBackClicked = { navigatorViewModel.navigateUp() }
            )

        }
    ) { innerPadding ->
        SmsTypesContent(modifier = Modifier.padding(innerPadding))
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SmsTypesContent(modifier: Modifier = Modifier) {

    val viewModel:SmsTypesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val tabIndex = uiState.selectedTab


    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                false
            )
        }
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
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            BottomSheetComponent.TextFieldBottomSheetComponent(
                model = TextFieldBottomSheetModel(
                    title = R.string.sms_analysis_bottom_sheet_title,
                    textFieldValue = "",
                    buttonText = R.string.common_add,
                    onActionButtonClicked = { value ->
                        viewModel.addWordDetector(value)
                        coroutineScope.launch {
                            BottomSheetComponent.handleVisibilityOfBottomSheet(
                                sheetState,
                                false
                            )
                        }
                    },
                )
            )
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            val tabTitles = WordDetectorType.getSmsTypesScreenList().sortedBy { it.id }.map { it.value }
            Column(
                Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = tabIndex,
                    edgePadding = 0.dp,
                    indicator = {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                            color = MaterialTheme.colors.secondary,
                            height = TabRowDefaults.IndicatorHeight
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { index, stringResId ->
                        Tab(
                            modifier = Modifier.background(MaterialTheme.colors.background),
                            selected = tabIndex == index,
                            onClick = {
                                 viewModel.updateSelectedTab(index)
                            },
                            text = {
                                TextComponent.ClickableText(
                                    text = stringResource(id = stringResId),
                                    color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                        id = R.color.light_gray
                                    )
                                )
                            })
                    }
                }
                SmsTypesTab(viewModel){

                }
            }


            ButtonComponent.FloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                onClick = {
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            true
                        )
                    }
                }
            )


        }
    }

}