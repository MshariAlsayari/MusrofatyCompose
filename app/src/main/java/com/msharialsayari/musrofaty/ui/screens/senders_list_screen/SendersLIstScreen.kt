package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
        { TextComponent.BodyText(text = "Delete") },
        { Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = "") },
        backgroundColor = colorResource(R.color.deletAction),
        onClicked = { position, item ->
            viewModel.disableSender(item.senderName)
        })

    val pinAction = Action<SenderComponentModel>(
        { TextComponent.BodyText(text = "Pin") },
        { Icon(painter = painterResource(id = R.drawable.ic_pin), contentDescription = "") },
        backgroundColor = colorResource(R.color.pinAction),
        onClicked = { position, item ->
            viewModel.pinSender(item.senderName)
        })


    VerticalEasyList(
        list = uiState.senders,
        views = { SenderComponent(model = it) },
        dividerView = { DividerComponent.HorizontalDividerComponent()},
        onItemClicked = {item, position ->  },
        isLoading = uiState.isLoading,
        startActions = listOf(deleteAction,pinAction),
        loadingProgress = {ProgressBar.CircleProgressBar()},
        emptyView = {EmptyComponent.EmptyTextComponent()},
        onRefresh = { viewModel.getAllSenders() }
    )
    

}


