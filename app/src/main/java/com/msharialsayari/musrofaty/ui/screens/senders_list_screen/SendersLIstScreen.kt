package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.musrofaty.ui_component.DividerComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SenderComponent
import com.msharialsayari.musrofaty.ui_component.SenderComponentModel
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.ui_component.TextFieldBottomSheetModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SendersListScreen(onNavigateToSenderDetails:(senderId:Int)->Unit, onNavigateToSenderSmsList:(senderId:Int)->Unit) {

    val viewModel: SendersListViewModel = hiltViewModel()
    val coroutineScope                    = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
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
        modifier = Modifier,
        sheetState = sheetState,

        sheetContent = {
            BottomSheetComponent.TextFieldBottomSheetComponent(model = TextFieldBottomSheetModel(
                title = R.string.common_sender_shortcut,
                description = R.string.add_sender_description,
                textFieldValue =  "",
                buttonText = R.string.common_add,
                onActionButtonClicked = { value ->
                    viewModel.addSender(value)
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },
                isSingleLine = true

                )
            )}
    ) {


        Box(modifier = Modifier.fillMaxSize()) {
            PageCompose(viewModel,onNavigateToSenderDetails,onNavigateToSenderSmsList)
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



@Composable
fun PageCompose(
    viewModel:SendersListViewModel,
    onNavigateToSenderDetails:(senderId:Int)->Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit){


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = BottomNavItem.SendersList.title,
                isParent = true
            )

        }
    ) { innerPadding ->
        SendersListCompose(
            Modifier.padding(innerPadding),
            viewModel,
            onNavigateToSenderDetails,
            onNavigateToSenderSmsList)

    }


}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersListCompose(
    modifier: Modifier=Modifier,
    viewModel:SendersListViewModel,
    onNavigateToSenderDetails:(senderId:Int)->Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val senderItems                          = uiState.senders?.collectAsState(emptyList())

    val pinAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_pin),color= Color.White, alignment = TextAlign.Center) },
        { ActionIcon(id = R.drawable.ic_pin) },
        backgroundColor = MusrofatyTheme.colors.pinActionColor,
        onClicked = { position, item ->
            viewModel.pinSender(item.senderId)
        })


    val modifyAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_change),color= Color.White,alignment = TextAlign.Center) },
        { ActionIcon(id = R.drawable.ic_modify) },
        backgroundColor = MusrofatyTheme.colors.modifyActionColor,
        onClicked = { position, item ->
            onNavigateToSenderDetails(item.senderId)
        })


    val deleteAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete )) },
        { ActionIcon(id = R.drawable.ic_delete ) },
        backgroundColor = MusrofatyTheme.colors.deleteActionColor,
        onClicked = { position, item ->
            viewModel.deleteSender(item.senderId)

        })


    VerticalEasyList(
        modifier = modifier,
        list = SendersListViewModel.SendersUiState.wrapSendersToSenderComponentModelList(senderItems?.value?: emptyList(), context),
        view = { sender -> SenderComponent( modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)), model = sender, onAvatarClicked = { onNavigateToSenderSmsList(sender.senderId)}) },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->
            onNavigateToSenderSmsList(item.senderId)
        },
        isLoading = uiState.isLoading,
        endActions = listOf(modifyAction,pinAction),
        startActions = listOf(deleteAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyCompose()},
        onRefresh = {
            viewModel.getAllSenders()
        }
    )


}

@Composable
fun EmptyCompose(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_senders))
    }

}

@Composable
fun ActionIcon(@DrawableRes id:Int){
    Icon(painter           = painterResource(id = id),
        contentDescription = null,
        tint               = Color.White
    )

}


