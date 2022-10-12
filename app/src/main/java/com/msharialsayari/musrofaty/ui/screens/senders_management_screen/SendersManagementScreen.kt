package com.msharialsayari.musrofaty.ui.screens.senders_management_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.ActionIcon
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.tabs.ActiveSendersTab
import com.msharialsayari.musrofaty.ui.screens.senders_management_screen.tabs.UnActiveSendersTab
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch

@Composable
fun SendersManagementScreen(onNavigateToSenderDetails:(senderId:Int)->Unit){
    val viewModel:SendersManagementViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    when{
        uiState.isLoading -> LoadingPageCompose()
        else -> PageCompose(viewModel = viewModel,onNavigateToSenderDetails=onNavigateToSenderDetails)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PageCompose(viewModel: SendersManagementViewModel,onNavigateToSenderDetails:(senderId:Int)->Unit){

    var tabIndex by remember { mutableStateOf(0) }

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
        sheetState = sheetState,
        sheetContent = {

            BottomSheetComponent.TextFieldBottomSheetComponent(model = TextFieldBottomSheetModel(
                title = R.string.common_sender_shortcut,
                textFieldValue =  "",
                buttonText = R.string.common_add,
                onActionButtonClicked = { value ->
                    viewModel.addSender(value)
                    viewModel.loadSms()
                    coroutineScope.launch {
                      handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },

                ))


        }) {

        Box(modifier = Modifier.fillMaxSize()) {

            val tabTitles = listOf(R.string.tab_active_senders, R.string.tab_unactive_senders,)
            Column {
                TabRow(
                    selectedTabIndex = tabIndex,
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
                            onClick = { tabIndex = index },
                            text = {
                                Text(
                                    text = stringResource(id = stringResId),
                                    color = MaterialTheme.colors.onBackground
                                )
                            })
                    }
                }
                when (tabIndex) {
                    0 -> ActiveSendersTab(
                        viewModel = viewModel,
                        onNavigateToSenderDetails = onNavigateToSenderDetails
                    )
                    1 -> UnActiveSendersTab(
                        viewModel = viewModel,
                        onNavigateToSenderDetails = onNavigateToSenderDetails
                    )
                }
            }


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
fun SendersListCompose(viewModel: SendersManagementViewModel,list: List<SenderComponentModel>,onNavigateToSenderDetails:(senderId:Int)->Unit, isActiveTab:Boolean){
    val uiState by viewModel.uiState.collectAsState()
    val deleteAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = if (isActiveTab) R.string.common_disable else  R.string.common_enable)) },
        { ActionIcon(id = if (isActiveTab) R.drawable.ic_visibility_off else R.drawable.ic_visibility) },
        backgroundColor = colorResource(if (isActiveTab)  R.color.deletAction else R.color.pinAction ),
        onClicked = { position, item ->
            viewModel.updateSenderVisibility(item.senderId, !isActiveTab)

        })




    val modifyAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_change)) },
        { ActionIcon(id = R.drawable.ic_modify) },
        backgroundColor = colorResource(R.color.modifyAction),
        onClicked = { position, item ->
            onNavigateToSenderDetails(item.senderId)

        })


    VerticalEasyList(
        list            = list,
        view            = { SenderComponent( modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16)), model = it) },
        dividerView     = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked   = { item, position ->
            onNavigateToSenderDetails(item.senderId)
        },
        isLoading       = uiState.isLoading,
        startActions    = listOf(deleteAction),
        endActions      = listOf(modifyAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView       = { EmptyComponent.EmptyTextComponent() },
    )
}


@Composable
fun LoadingPageCompose(){
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
            ){
        ProgressBar.CircleProgressBar()

    }

}