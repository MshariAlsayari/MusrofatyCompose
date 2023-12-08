package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme


@Composable
fun SendersListScreen() {

    val viewModel: SendersListViewModel = hiltViewModel()
    val screenType = MusrofatyTheme.screenType

    if (screenType.isScreenWithDetails) {
        SendersListExpanded(viewModel = viewModel)
    } else {
        SendersListCompact(viewModel = viewModel)
    }


}



