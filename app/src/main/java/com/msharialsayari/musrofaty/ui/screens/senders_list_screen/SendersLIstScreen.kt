package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme


@Composable
fun SendersListScreen() {

    val viewModel: SendersListViewModel = hiltViewModel()

    SendersListPortraitScreen(viewModel = viewModel)


}



