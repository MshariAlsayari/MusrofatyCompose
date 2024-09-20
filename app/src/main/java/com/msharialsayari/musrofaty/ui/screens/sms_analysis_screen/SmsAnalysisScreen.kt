package com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.word_detector_database.WordDetectorEntity
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import kotlinx.coroutines.launch

@Composable
fun SmsAnalysisScreen(
    navigatorViewModel: AppNavigatorViewModel = hiltViewModel(),
    isSmsAnalysisScreen: Boolean
) {

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = if (isSmsAnalysisScreen) Screen.SmsAnalysisScreen.title else Screen.SmsTypesScreen.title,
                onArrowBackClicked = { navigatorViewModel.navigateUp() }
            )
        }
    ) { innerPadding ->
        SmsAnalysisContent(
            modifier = Modifier.padding(innerPadding),
            isSmsAnalysisScreen = isSmsAnalysisScreen
        )
    }


}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SmsAnalysisContent(
    modifier: Modifier = Modifier,
    isSmsAnalysisScreen: Boolean
) {

    val viewModel: SmsAnalysisViewModel = hiltViewModel()
    LaunchedEffect(key1 = isSmsAnalysisScreen) {
        viewModel.updateScreenType(isSmsAnalysisScreen)
    }


    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedItem by rememberSaveable { mutableStateOf<WordDetectorEntity?>(null) }
    var textValue by rememberSaveable { mutableStateOf<String>(selectedItem?.word ?: "") }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val showSheet: (Boolean) -> Unit = { show ->
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                show
            )
        }

        if (!show) {
            selectedItem = null
        }
    }


    BackHandler(sheetState.isVisible) {
        showSheet(false)
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

    LaunchedEffect(selectedItem) {
        textValue = selectedItem?.word ?: ""
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            BottomSheetComponent.TextFieldBottomSheetComponent(
                model = TextFieldBottomSheetModel(
                    title = if (selectedItem != null) R.string.common_change else R.string.common_add,
                    label = R.string.common_word,
                    textFieldValue = textValue,
                    buttonText = if (selectedItem != null) R.string.common_change else R.string.common_add,
                    onActionButtonClicked = { value ->
                        viewModel.onActionClicked(
                            value = value,
                            selectedItem = selectedItem
                        )
                        showSheet(false)
                    },
                )
            )
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                SmsScreenTabRow(
                    viewModel = viewModel,
                    isSmsAnalysisScreen = isSmsAnalysisScreen
                )
                SmsAnalysisTab(viewModel) {
                    selectedItem = it
                    showSheet(true)
                }
            }

            ButtonComponent.FloatingButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                onClick = {
                    selectedItem = null
                    showSheet(true)
                }
            )
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun WordsDetectorListCompose(
    list: List<WordDetectorEntity>,
    onItemClicked: (WordDetectorEntity) -> Unit,
    onDelete: (Int) -> Unit,
) {
    val deleteAction = Action<WordDetectorEntity>(
        {
            TextComponent.BodyText(
                text = stringResource(id = R.string.common_delete),
                color = Color.White,
                alignment = TextAlign.Center
            )
        },
        { ActionIcon(id = R.drawable.ic_delete) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            onDelete(item.id)

        })

    VerticalEasyList(
        list = list,
        view = {
            TextComponent.BodyText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.default_margin16)), text = it.word
            )
        },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->
            onItemClicked(item)
        },
        onItemDoubleClicked = { item, position ->

        },
        startActions = listOf(deleteAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyCompose() },
    )
}

@Composable
fun EmptyCompose() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent()
    }

}