package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.sms.SmsContent
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics.StatisticsContent
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardCompact(
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList: (senderId: Int) -> Unit
) {

    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 80.dp,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ButtonComponent.FloatingButton(
                firstIcon = Icons.Filled.KeyboardArrowUp,
                secondIcon = Icons.Filled.KeyboardArrowDown,
                isFirstPosition = sheetState.bottomSheetState.isCollapsed,
                onClick = {
                    if (sheetState.bottomSheetState.isExpanded) {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.collapse()
                        }
                    } else {
                        coroutineScope.launch {
                            sheetState.bottomSheetState.expand()
                        }
                    }

                })

        },
        sheetContent = {
            StatisticsContent(viewModel = viewModel)

        }) {

        Scaffold(
            topBar = { DashboardTopBar(viewModel) }
        ) { innerPadding ->
            SmsContent(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onSmsClicked = onSmsClicked,
                onNavigateToSenderSmsList = onNavigateToSenderSmsList
            )

        }

    }

}