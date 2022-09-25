package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersListScreen(navController: NavHostController) {
    val viewModel: SendersListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val deleteAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_delete)) },
        { Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = null) },
        backgroundColor = colorResource(R.color.deletAction),
        onClicked = { position, item ->
            viewModel.disableSender(item.senderId)
        })

    val pinAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_pin)) },
        { Icon(painter = painterResource(id = R.drawable.ic_pin), contentDescription = null) },
        backgroundColor = colorResource(R.color.pinAction),
        onClicked = { position, item ->
            viewModel.pinSender(item.senderId)
        })


    val modifyAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = stringResource(id = R.string.common_change)) },
        { Icon(painter = painterResource(id = R.drawable.ic_modify), contentDescription = null) },
        backgroundColor = colorResource(R.color.modifyAction),
        onClicked = { position, item ->
            //Navigate to senderDetailScreen
            navController.navigate("sender_details/${item.senderId}")
        })


    VerticalEasyList(
        list = SendersListViewModel.SendersUiState.wrapSendersToSenderComponentModelList(
            uiState.senders,
            navController.context
        ),
        view = { SenderComponent(model = it) },
        dividerView = { DividerComponent.HorizontalDividerComponent() },
        onItemClicked = { item, position ->
            navController.navigate("sender_sms_list/${item.senderId}")


        },
        isLoading = uiState.isLoading,
        startActions = listOf(deleteAction),
        endActions = listOf(pinAction, modifyAction),
        loadingProgress = { ProgressBar.CircleProgressBar() },
        emptyView = { EmptyComponent.EmptyTextComponent() },
        onRefresh = { viewModel.getAllSenders() }
    )


}


