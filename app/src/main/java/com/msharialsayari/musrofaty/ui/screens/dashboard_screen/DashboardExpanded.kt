package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.sms.SmsContent
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.statistics.StatisticsContent

@Composable
fun DashboardExpanded(
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
){
    Row(modifier = Modifier.fillMaxSize()) {
        SmsContent(modifier = Modifier.weight(1f), viewModel = viewModel, onSmsClicked = onSmsClicked, onNavigateToSenderSmsList = onNavigateToSenderSmsList)
        StatisticsContent(modifier = Modifier.weight(1f),viewModel = viewModel)
    }
}