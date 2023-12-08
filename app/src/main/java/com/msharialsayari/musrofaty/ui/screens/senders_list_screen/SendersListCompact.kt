package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.senders.SendersListContent

@Composable
fun SendersListCompact(viewModel: SendersListViewModel) {

    SendersListContent(viewModel = viewModel)
}

