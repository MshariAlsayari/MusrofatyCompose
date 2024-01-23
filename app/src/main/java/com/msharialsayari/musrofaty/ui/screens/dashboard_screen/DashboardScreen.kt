package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs.DashboardDialogType
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs.DatePickerDialog
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs.FilterTimesOptionsDialog
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.DialogComponent
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager


@Composable
fun DashboardScreen() {

    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val dialogType = uiState.dashboardDialogType
    val screenType = MusrofatyTheme.screenType

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }

    when(dialogType){
        DashboardDialogType.TIMES_PERIODS -> FilterTimesOptionsDialog(viewModel)
        DashboardDialogType.DATE_PICKER -> DatePickerDialog(viewModel)
        null -> {}
    }



    Scaffold(
        topBar = { DashboardTopBar(viewModel) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
        ) {
            if(screenType.isScreenWithDetails){
                DashboardExpanded(viewModel = viewModel)
            }else{
                DashboardCompact(viewModel = viewModel)
            }

        }
    }




}







