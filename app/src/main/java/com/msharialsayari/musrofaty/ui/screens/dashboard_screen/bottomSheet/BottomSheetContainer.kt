package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.bottomSheet

import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel


@Composable
fun BottomSheetContainer(viewModel: DashboardViewModel) {
    BottomSheetLazyColumn(viewModel)
}