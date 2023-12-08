package com.msharialsayari.musrofaty.ui.screens.senders_list_screen.senders

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SenderListTopBar
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.SendersListViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SendersListContent(
    modifier: Modifier = Modifier,
    screenType: ScreenType,
    viewModel: SendersListViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
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
        sheetState = sheetState,
        sheetContent = {
            BottomSheetComponent.TextFieldBottomSheetComponent(
                model = TextFieldBottomSheetModel(
                    title = R.string.common_sender_shortcut,
                    description = R.string.add_sender_description,
                    textFieldValue = "",
                    buttonText = R.string.common_add,
                    onActionButtonClicked = { value ->
                        viewModel.addSender(value)
                        coroutineScope.launch {
                            BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                        }
                    },
                    isSingleLine = true

                )
            )
        }
    ) {

        Scaffold(
            topBar = {
                SenderListTopBar(screenType = screenType) {
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            true
                        )
                    }
                }

            },
            floatingActionButton = {
                if(screenType == ScreenType.Compact)
                ButtonComponent.FloatingButton(
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
        ) { innerPadding ->
            SendersList(
                Modifier.padding(innerPadding),
                viewModel)
        }
    }
}