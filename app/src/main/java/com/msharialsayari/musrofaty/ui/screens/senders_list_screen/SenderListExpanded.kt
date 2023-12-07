package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.msharialsayari.musrofaty.pdf.PdfCreatorViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListScreen
import com.msharialsayari.musrofaty.ui.screens.senders_list_screen.senders.SendersListContent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ListDetails
import com.msharialsayari.musrofaty.ui_component.PlaceHolder
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SendersListExpanded(
    viewModel: SendersListViewModel,
    onDetailsClicked: (Int) -> Unit,
    onNavigateToFilterScreen: (Int, Int?) -> Unit,
    onSmsClicked: (String) -> Unit,
    onExcelFileGenerated: () -> Unit,
    onNavigateToPDFCreatorActivity: (PdfCreatorViewModel.PdfBundle) -> Unit,
){

    val coroutineScope                    = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val senderId = remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.hide()
            }
        }

    } else {
        DisposableEffect(Unit) {
            onDispose {
                keyboardController?.show()
            }
        }

    }


    ListDetails(
        primaryRatio = 1f,
        secondaryRatio = 1f,
        primaryContent = {
            SendersListContent(
                screenType =  ScreenType.Expanded,
                viewModel = viewModel,
                onNavigateToSenderDetails = {id ->

                },
                onNavigateToSenderSmsList = {id ->
                    senderId.value      = id
                }
            )
        },
        secondaryContent ={
            LargeSideScreen(
                senderId = senderId.value,
                onDetailsClicked = onDetailsClicked ,
                onNavigateToFilterScreen = onNavigateToFilterScreen,
                onSmsClicked = onSmsClicked,
                onExcelFileGenerated = onExcelFileGenerated,
                onNavigateToPDFCreatorActivity = onNavigateToPDFCreatorActivity
            )
        }
    )

}

@Composable
fun LargeSideScreen(senderId: Int?=null,
                    onDetailsClicked: (Int) -> Unit,
                    onNavigateToFilterScreen: (Int, Int?) -> Unit,
                    onSmsClicked: (String) -> Unit,
                    onExcelFileGenerated: () -> Unit,
                    onNavigateToPDFCreatorActivity: (PdfCreatorViewModel.PdfBundle) -> Unit,
){

    Box(contentAlignment = Alignment.Center) {

        if(senderId != null){
            SenderSmsListScreen(
                screenType=ScreenType.Expanded,
                senderId = senderId,
                onDetailsClicked = onDetailsClicked ,
                onNavigateToFilterScreen = onNavigateToFilterScreen,
                onBack = {  },
                onSmsClicked = onSmsClicked,
                onExcelFileGenerated = onExcelFileGenerated,
                onNavigateToPDFCreatorActivity = onNavigateToPDFCreatorActivity
            )
        }else{
            PlaceHolder.ScreenPlaceHolder()
        }
    }

}


