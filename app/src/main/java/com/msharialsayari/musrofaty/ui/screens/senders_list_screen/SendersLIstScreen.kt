package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.enums.ScreenType


@Composable
fun SendersListScreen(
    onNavigateToSenderDetails: (senderId: Int) -> Unit,
    onNavigateToSenderSmsList: (senderId: Int) -> Unit,
    onDetailsClicked: (Int) -> Unit,
    onNavigateToFilterScreen: (Int, Int?) -> Unit,
    onSmsClicked: (String) -> Unit,
    onExcelFileGenerated: () -> Unit,
    onNavigateToPDFCreatorActivity: (PdfCreatorViewModel.PdfBundle) -> Unit,
) {

    val viewModel: SendersListViewModel = hiltViewModel()
    val screenType = MusrofatyTheme.screenType

    if (screenType.isScreenWithDetails) {
        SendersListExpanded(
            viewModel = viewModel,
            onDetailsClicked = onDetailsClicked,
            onNavigateToFilterScreen = onNavigateToFilterScreen,
            onSmsClicked = onSmsClicked,
            onExcelFileGenerated = onExcelFileGenerated,
            onNavigateToPDFCreatorActivity = onNavigateToPDFCreatorActivity
        )
    } else {
        SendersListCompact(
            viewModel = viewModel,
            onNavigateToSenderDetails = onNavigateToSenderDetails,
            onNavigateToSenderSmsList = onNavigateToSenderSmsList
        )


    }


}



