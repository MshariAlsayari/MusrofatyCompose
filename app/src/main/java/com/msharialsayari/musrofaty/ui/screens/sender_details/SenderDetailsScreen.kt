package com.msharialsayari.musrofaty.ui.screens.sender_details

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun SenderDetailsScreen(navController: NavController, senderId: Int){
    val viewModel: SendersDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    viewModel.getSenderModel(senderId)
    Box(contentAlignment = Alignment.Center) {
        TextComponent.HeaderText(
            text = uiState.sender?.senderName ?: ""
        )

    }
}