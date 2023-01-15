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
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch



@Composable
fun SenderDetailsScreen(senderId: Int, onNavigateToContent:(Int)->Unit,onDone:()->Unit,onBackPressed:()->Unit) {
    val viewModel: SendersDetailsViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SenderDetailsScreen.title,
                onArrowBackClicked = onBackPressed
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
    val bottomSheetType = remember { mutableStateOf(SenderDetailsBottomSheet.DISPLAY_NAME_AR) }
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

            when (bottomSheetType.value) {

                SenderDetailsBottomSheet.DISPLAY_NAME_AR ->{
                    ArabicNameBottomSheetCompose(textFieldValue = uiState.sender?.displayNameAr, onActionClicked = {
                        viewModel.changeDisplayName(it, true)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)

                        }
                    })
                }


                SenderDetailsBottomSheet.DISPLAY_NAME_EN ->{
                    EnglishNameBottomSheetCompose(textFieldValue = uiState.sender?.displayNameEn, onActionClicked = {
                        viewModel.changeDisplayName(it, false)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)

                        }
                    })
                }



                SenderDetailsBottomSheet.CONTENT -> {
                    SenderCategoryBottomSheetCompose(
                    categories = uiState.wrapContentModel(context),
                    onAddCategoryClicked = { openDialog.value = true },
                    onCategorySelected = {
                        viewModel.updateSenderCategory(it)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }
                    },
                    onCategoryLongClicked = {
                        onNavigateToContent(it.id)
                    })
                }
            }

        },
    ) {
        Box(contentAlignment = Alignment.Center) {


            Column(modifier = Modifier.fillMaxSize()) {

                ClickableTextListItemCompose(title = stringResource(id = R.string.sender_display_name_ar) ,value =  uiState.sender?.displayNameAr, onClick = {
                    bottomSheetType.value = SenderDetailsBottomSheet.DISPLAY_NAME_AR
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                })

                ClickableTextListItemCompose(title = stringResource(id = R.string.sender_display_name_en) ,value =  uiState.sender?.displayNameEn, onClick = {
                    bottomSheetType.value = SenderDetailsBottomSheet.DISPLAY_NAME_EN
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                })

                ClickableTextListItemCompose(title = stringResource(id = R.string.sender_category) ,value =  viewModel.getSenderContentDisplayName(context = context), onClick = {
                    bottomSheetType.value = SenderDetailsBottomSheet.CONTENT
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                })


                SwitchListItemCompose(title = stringResource(id = R.string.sender_pin), isChecked = uiState.isPin, onCheck = {
                    viewModel.pinSender(it)
                })




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
                        color= MusrofatyTheme.colors.deleteActionColor,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClickableTextListItemCompose(title:String?, value:String?, onClick:()->Unit){

    ListItem(
        text = { TextComponent.BodyText(text = title?:"") },
        trailing = {
            TextComponent.ClickableText(
                text = value?:"",
                modifier = Modifier.clickable {
                    onClick()
                })
        }
    )

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwitchListItemCompose(title:String?, isChecked:Boolean, onCheck:(Boolean)->Unit){

    ListItem(
        text = { TextComponent.BodyText(text = title?:"") },
        trailing = {
            SwitchComponent(checked = isChecked, onChecked = { check ->
                onCheck(check)

            })
        }
    )

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

@Composable
fun ArabicNameBottomSheetCompose(textFieldValue:String?,onActionClicked:(String)->Unit){
    val model = TextFieldBottomSheetModel(
        title = R.string.sender_display_name_ar,
        textFieldValue = textFieldValue ?: "",
        buttonText = R.string.common_save,
        onActionButtonClicked = { value ->
            onActionClicked(value)
        })

    BottomSheetComponent.TextFieldBottomSheetComponent(model = model)

}

@Composable
fun EnglishNameBottomSheetCompose(textFieldValue:String?,onActionClicked:(String)->Unit){
    val model = TextFieldBottomSheetModel(
        title = R.string.sender_display_name_en,
        textFieldValue = textFieldValue ?: "",
        buttonText = R.string.common_save,
        onActionButtonClicked = { value ->
            onActionClicked(value)
        })

    BottomSheetComponent.TextFieldBottomSheetComponent(model = model)

}


@Composable
fun SenderCategoryBottomSheetCompose(categories:List<SelectedItemModel>,
                                     onAddCategoryClicked:()->Unit,
                                     onCategorySelected:(SelectedItemModel)->Unit,
                                     onCategoryLongClicked:(SelectedItemModel)->Unit){




    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.sender_category,
        list = categories,
        description= R.string.common_long_click_to_modify,
        trailIcon = {
            Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                onAddCategoryClicked()
            })
        },
        onLongPress = {
            onCategoryLongClicked(it)
        },
        onSelectItem = {
            onCategorySelected(it)
        }
    )

}



enum class SenderDetailsBottomSheet {
    DISPLAY_NAME_AR, DISPLAY_NAME_EN, CONTENT
}