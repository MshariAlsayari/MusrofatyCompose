package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui_component.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersListScreen(navController: NavHostController) {
    val viewModel: SendersListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()


    VerticalEasyList(
        list = uiState.senders,
        views = { SenderComponent(model = it) },
        dividerView = {DividerComponent.HorizontalDividerComponent()},
        onItemClicked = {item, position ->  },
        isLoading = uiState.isLoading,
        loadingProgress = {ProgressBar.CircleProgressBar()},
        emptyView = {EmptyComponent.EmptyTextComponent()},
        onRefresh = { viewModel.getAllSenders() }
    )
    

}


