package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.tabs.ActiveSendersTab
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch

@Composable
fun SendersManagementScreen(onNavigateToSenderDetails:(senderId:Int)->Unit,onBackPressed:()->Unit){
    val viewModel:SendersManagementViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = Screen.SendersManagementScreen.title,
                onArrowBackClicked = onBackPressed
            )

        }
    ) { innerPadding ->
        when{
            uiState.isLoading -> LoadingPageCompose(modifier = Modifier.padding(innerPadding))
            else -> PageCompose(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onNavigateToSenderDetails=onNavigateToSenderDetails)
        }
    }


}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PageCompose(modifier: Modifier=Modifier,viewModel: SendersManagementViewModel, onNavigateToSenderDetails:(senderId:Int)->Unit){

    val tabIndex by remember { mutableStateOf(0) }

    val coroutineScope                    = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


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
        modifier=modifier,
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
                      handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },

                )
            )


        }) {

        Box(modifier = Modifier.fillMaxSize()) {

            ActiveSendersTab(
                viewModel = viewModel,
                onNavigateToSenderDetails = onNavigateToSenderDetails
            )


            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensionResource(id = R.dimen.default_margin16)),
                visible = tabIndex == 0,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                content = {
                    ButtonComponent.FloatingButton(

                        onClick = {
                            coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, true) }
                        }
                    )
                }
            )


        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersListCompose(viewModel: SendersManagementViewModel, list: List<SenderComponentModel>, onNavigateToSenderDetails:(senderId:Int)->Unit){
    val uiState by viewModel.uiState.collectAsState()


    val modifyAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_change), color = Color.White,alignment = TextAlign.Center) },
        { ActionIcon(id = R.drawable.ic_modify) },
        backgroundColor = MusrofatyTheme.colors.modifyActionColor,
        onClicked = { position, item ->
            onNavigateToSenderDetails(item.senderId)

        })


    VerticalEasyList(
        list            = list,
        view            = { sender -> SenderComponent( modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)), model = sender, onAvatarClicked = {  onNavigateToSenderDetails(sender.senderId)}) },
        dividerView     = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked   = { item, position ->
            onNavigateToSenderDetails(item.senderId)
        },
        isLoading       = uiState.isLoading,
        endActions      = listOf(modifyAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView       = { EmptyCompose() },
    )
}

@Composable
fun EmptyCompose(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent()
    }

}


@Composable
fun LoadingPageCompose(modifier: Modifier=Modifier){
    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
            ){
        ProgressBar.CircleProgressBar()

    }

}