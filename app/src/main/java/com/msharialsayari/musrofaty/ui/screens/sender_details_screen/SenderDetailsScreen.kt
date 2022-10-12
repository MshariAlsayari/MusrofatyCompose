package com.msharialsayari.musrofaty.ui.screens.sender_details_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SenderDetailsScreen(senderId: Int, onDone:()->Unit) {
    val context = LocalContext.current
    val viewModel: SendersDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetType = remember { mutableStateOf<SenderDetailsBottomSheet?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    viewModel.getSenderModel(senderId)

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
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
            var model:TextFieldBottomSheetModel? =null

            when (bottomSheetType.value) {

                SenderDetailsBottomSheet.DISPLAY_NAME_AR ->
                    model = TextFieldBottomSheetModel(
                    title = R.string.sender_display_name_en,
                    textFieldValue = uiState.sender?.displayNameAr ?: "",
                    buttonText = R.string.common_save,
                    onActionButtonClicked = { value ->
                        viewModel.changeDisplayName(value, true)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)

                        }
                    },

                )

                SenderDetailsBottomSheet.DISPLAY_NAME_EN -> model = TextFieldBottomSheetModel(
                    title = R.string.sender_display_name_en,
                    textFieldValue = uiState.sender?.displayNameEn ?: "",
                    buttonText = R.string.common_save,
                    onActionButtonClicked = { value ->
                        viewModel.changeDisplayName(value, false)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)

                        }
                    },

                )

                SenderDetailsBottomSheet.CONTENT -> {}
                else -> {}
            }

            if (model!= null) {
                BottomSheetComponent.TextFieldBottomSheetComponent(model = model)
            }else{
                BottomSheetComponent.SelectedItemListBottomSheetComponent(
                    title = R.string.sender_category,
                    list = uiState.wrapContentModel(context),
                    onSelectItem = {
                        viewModel.updateSenderCategory(it)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(contentAlignment = Alignment.Center) {


            Column(modifier = Modifier.fillMaxSize()) {

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_display_name_ar)) },
                    trailing = {
                        TextComponent.ClickableText(
                            text = uiState.sender?.displayNameAr ?: "",
                            modifier = Modifier.clickable {
                                bottomSheetType.value = SenderDetailsBottomSheet.DISPLAY_NAME_AR
                                coroutineScope.launch {
                                    handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                                }
                            })
                    }
                )

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_display_name_en)) },
                    trailing = {
                        TextComponent.ClickableText(
                            text = uiState.sender?.displayNameEn ?: "",
                            modifier = Modifier.clickable {
                                bottomSheetType.value = SenderDetailsBottomSheet.DISPLAY_NAME_EN
                                coroutineScope.launch {
                                    handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                                }
                            })
                    }
                )

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_category)) },
                    trailing = {
                        TextComponent.ClickableText(
                            text = if (ContentModel.getDisplayName(
                                context = context,
                                uiState.sender?.content
                            ).isNotEmpty()) ContentModel.getDisplayName(
                                context = context,
                                uiState.sender?.content
                            ) else context.getString(androidx.compose.ui.R.string.not_selected) ,
                            modifier = Modifier.clickable {
                                bottomSheetType.value = SenderDetailsBottomSheet.CONTENT
                                coroutineScope.launch {
                                    handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                                }
                            })
                    }
                )

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_pin)) },
                    trailing = {
                        SwitchComponent(checked = uiState.isPin, onChecked = { check ->
                            viewModel.pinSender(check)
                        })
                    }
                )


                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_active)) },
                    trailing = {
                        SwitchComponent(checked = uiState.isActive, onChecked = { check ->
                            viewModel.activeSender(check)

                        })
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(modifier = Modifier.fillMaxWidth()) {

                    ButtonComponent.ActionButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        text = R.string.common_save,
                        onClick = {
                            onDone()
                        }
                    )

                    ButtonComponent.ActionButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        text = R.string.common_delete,
                        color= R.color.deletAction,
                        onClick = {
                            viewModel.deleteSender()
                            onDone()
                        }
                    )

                }


            }
        }


    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
suspend fun handleVisibilityOfBottomSheet(sheetState: ModalBottomSheetState, show: Boolean) {

    if (show) {
        sheetState.show()
    } else {
        sheetState.hide()
    }

}

enum class SenderDetailsBottomSheet {
    DISPLAY_NAME_AR, DISPLAY_NAME_EN, CONTENT
}