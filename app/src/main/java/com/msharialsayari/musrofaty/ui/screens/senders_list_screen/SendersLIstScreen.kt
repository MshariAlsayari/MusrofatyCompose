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
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.RowComponent

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SendersListScreen(navController: NavHostController) {
    val viewModel: SendersListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()


    VerticalEasyList(
        list = uiState.senders.keys.toList(),
        views = { RowComponent.SenderRow(displayName = SenderModel.getDisplayName(navController.context,it), totalSms = uiState.senders[it]?.size?:0) },
        onItemClicked = {item, position ->  },
        isLoading = uiState.isLoading,
        loadingProgress = {ProgressBar.CircleProgressBar()},
        emptyView = {EmptyComponent.EmptyTextComponent()},
        onRefresh = { viewModel.getAllSenders() }
    )
    

}


