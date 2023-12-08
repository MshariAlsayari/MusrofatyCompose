package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.sms.SmsContent
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics.StatisticsContent
import com.msharialsayari.musrofaty.ui_component.ListDetails

@Composable
fun DashboardExpanded(
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList: (senderId: Int) -> Unit
) {


    ListDetails(
        primaryRatio = 1f,
        secondaryRatio = 2f,
        primaryContent = {
            SmsContent(
                viewModel = viewModel,
                onSmsClicked = onSmsClicked,
                onNavigateToSenderSmsList = onNavigateToSenderSmsList
            )
        },
        secondaryContent ={
            StatisticsContent(viewModel = viewModel)
        }
    )
}