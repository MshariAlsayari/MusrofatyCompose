package com.msharialsayari.musrofaty.ui.screens.sender_details_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.filter_screen.BtnAction
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterTitle
import com.msharialsayari.musrofaty.ui.screens.filter_screen.FilterWord
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch



@Composable
fun SenderDetailsScreen(senderId: Int, onNavigateToContent:(Int)->Unit,onDone:()->Unit) {
    val viewModel: SendersDetailsViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SenderDetailsScreen.title,
            )

        }
    ) { innerPadding ->
        PageCompose(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            senderId = senderId,
            onNavigateToContent = onNavigateToContent,
            onDone=onDone
        )
    }

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PageCompose(modifier: Modifier=Modifier,viewModel: SendersDetailsViewModel,senderId: Int, onNavigateToContent:(Int)->Unit, onDone:()->Unit){
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetType = remember { mutableStateOf<SenderDetailsBottomSheet?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val openDialog = remember { mutableStateOf(false) }
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

    if (openDialog.value){
        AddContentDialog(viewModel, onDismiss = {
            openDialog.value = false
        })
    }

    ModalBottomSheetLayout(
        modifier = modifier.fillMaxSize(),
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
                    description= R.string.common_long_click_to_modify,
                    trailIcon = {
                        Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                            openDialog.value = true
                        })
                    },
                    onLongPress = {
                        onNavigateToContent(it.id)

                    },
                    onSelectItem = {
                        viewModel.updateSenderCategory(it)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }
                    }
                )
            }
        },
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
                    text = { Text(text = stringResource(id = R.string.store_category)) },
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

@Composable
fun AddContentDialog(viewModel: SendersDetailsViewModel, onDismiss:()->Unit){

    Dialog(onDismissRequest = onDismiss) {

        DialogComponent.AddCategoryDialog(
            onClickPositiveBtn = {ar,en->
                viewModel.addContent(
                    ContentModel(
                    valueEn = en,
                    valueAr = ar,
                    contentKey = ContentKey.SENDERS.name
                )
                )

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}



enum class SenderDetailsBottomSheet {
    DISPLAY_NAME_AR, DISPLAY_NAME_EN, CONTENT
}