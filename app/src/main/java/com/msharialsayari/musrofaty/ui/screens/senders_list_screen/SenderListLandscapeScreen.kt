package com.msharialsayari.musrofaty.ui.screens.senders_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListScreen
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ListDetails
import com.msharialsayari.musrofaty.ui_component.PlaceHolder
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SendersListLandscapeScreen(viewModel: SendersListViewModel){

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
        secondaryRatio = 2f,
        primaryContent = {
            SendersListContent(viewModel = viewModel, onSenderClicked = {
                senderId.value = it
            })

        },
        secondaryContent ={
            SenderSmsListContent(senderId = senderId.value)
        }
    )

}

@Composable
fun SenderSmsListContent(senderId: Int?=null){

    Box(contentAlignment = Alignment.Center) {
        if(senderId != null){
            SenderSmsListScreen(senderId = senderId)
        }else{
            PlaceHolder.ScreenPlaceHolder()
        }
    }

}


