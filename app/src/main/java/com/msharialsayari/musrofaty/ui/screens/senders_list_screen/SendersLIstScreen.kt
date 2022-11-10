package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui_component.*


@Composable
fun SendersListScreen(onNavigateToSenderDetails:(senderId:Int)->Unit, onNavigateToSenderSmsList:(senderId:Int)->Unit) {

    val viewModel: SendersListViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = BottomNavItem.SendersList.title,
                isParent = true
            )

        }
    ) { innerPadding ->
        PageCompose(
            Modifier.padding(innerPadding),
            viewModel,
            onNavigateToSenderDetails,
            onNavigateToSenderSmsList)

    }



}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun PageCompose(
    modifier: Modifier=Modifier,
    viewModel:SendersListViewModel,
    onNavigateToSenderDetails:(senderId:Int)->Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val senderItems                          = uiState.senders?.collectAsState(emptyList())

    val deleteAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_disable)) },
        { ActionIcon(id = R.drawable.ic_visibility_off)},
        backgroundColor = colorResource(R.color.delete_action_color),
        onClicked = { position, item ->
            viewModel.disableSender(item.senderId)
        })

    val pinAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_pin)) },
        { ActionIcon(id = R.drawable.ic_pin) },
        backgroundColor = colorResource(R.color.pin_action_color),
        onClicked = { position, item ->
            viewModel.pinSender(item.senderId)
        })


    val modifyAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_change)) },
        { ActionIcon(id = R.drawable.ic_modify) },
        backgroundColor = colorResource(R.color.modify_action_color),
        onClicked = { position, item ->
            //Navigate to senderDetailScreen

            onNavigateToSenderDetails(item.senderId)
        })


    VerticalEasyList(
        modifier = modifier,
        list = SendersListViewModel.SendersUiState.wrapSendersToSenderComponentModelList(senderItems?.value?: emptyList(), context),
        view = { SenderComponent( modifier = Modifier.padding(
            dimensionResource(id = R.dimen.default_margin16)
        ), model = it) },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->

            onNavigateToSenderSmsList(item.senderId)


        },
        isLoading = uiState.isLoading,
        startActions = listOf(deleteAction),
        endActions = listOf(pinAction, modifyAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyCompose()},
        onRefresh = {
            viewModel.getAllSenders()
            viewModel.loadSms()
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


