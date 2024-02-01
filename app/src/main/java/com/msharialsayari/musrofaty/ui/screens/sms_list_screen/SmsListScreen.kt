package com.msharialsayari.musrofaty.ui.screens.sms_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.base.LifecycleCallbacks
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet.CategoryBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet.SmsListBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet.SortedByBottomSheet
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch

@Composable
fun SmsListScreen() {
    val viewModel: SmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsList

    LifecycleCallbacks(
        onStart = {
            viewModel.startObserveChat()
        },
        onStop = {
            viewModel.stopObserveChat()
        },
    )


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                titleString = viewModel.screenTitle,
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort_by),
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                               viewModel.updateSelectedBottomSheet(SmsListBottomSheetType.SORTEDBY)
                            })
                },
                onArrowBackClicked = { viewModel.navigateUp() }
            )

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when {
                smsList.isNotEmpty() -> PageCompose(Modifier, viewModel)
                else -> EmptyComponent.EmptyTextComponent()
            }

        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PageCompose(modifier: Modifier = Modifier, viewModel: SmsListViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetType = uiState.bottomSheetType
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { true },
        skipHalfExpanded = true
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    LaunchedEffect(key1 = bottomSheetType) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                bottomSheetType != null
            )
        }
    }

    LaunchedEffect(sheetState.isVisible){
        if(!sheetState.isVisible){
            viewModel.updateSelectedBottomSheet(null)
        }

    }


    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {
            when (bottomSheetType) {
                SmsListBottomSheetType.CATEGORIES -> CategoryBottomSheet(viewModel)
                SmsListBottomSheetType.SORTEDBY -> SortedByBottomSheet(viewModel)
                null -> {}
            }

        }) {

        SmsListContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
        )
    }


}