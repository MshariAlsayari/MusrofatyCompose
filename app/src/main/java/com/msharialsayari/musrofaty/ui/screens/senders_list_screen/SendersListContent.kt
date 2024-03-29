package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

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
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SendersListContent(
    modifier: Modifier = Modifier,
    onSenderClicked: (Int) -> Unit ={},
    viewModel: SendersListViewModel
) {

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val screenType = MusrofatyTheme.screenType
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
        modifier = modifier,
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
                SenderListTopBar {
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(
                            sheetState,
                            true
                        )
                    }
                }

            },
            floatingActionButton = {
                if (!screenType.isScreenWithDetails)
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
            SendersListCompose(
                Modifier.padding(innerPadding),
                onSenderClicked,
                viewModel
            )
        }
    }
}