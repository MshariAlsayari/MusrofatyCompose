package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.senders.SendersListContent
import com.msharialsayari.musrofaty.utils.enums.ScreenType

@Composable
fun SendersListCompact(viewModel: SendersListViewModel) {

    SendersListContent(screenType = ScreenType.Compact, viewModel = viewModel,)
}

