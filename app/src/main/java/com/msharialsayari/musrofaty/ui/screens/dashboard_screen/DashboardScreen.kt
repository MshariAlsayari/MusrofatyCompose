package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.DialogComponent
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.enums.ScreenType


@Composable
fun DashboardScreen(
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit) {

    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val screenType = MusrofatyTheme.screenType

    LaunchedEffect(Unit) {
        viewModel.getData(context)
    }



    if (uiState.showStartDatePicker) {
        ComposeDatePicker(
            title = stringResource(id = R.string.common_start_date),
            onDone = {
                viewModel.onStartDateSelected(DateUtils.toTimestamp(it))
            },
            onDismiss = {
                viewModel.dismissAllDatePicker()
            }
        )
    }

    if (uiState.showEndDatePicker) {
        ComposeDatePicker(
            title = stringResource(id = R.string.common_end_date),
            onDone = {
                viewModel.onEndDateSelected(DateUtils.toTimestamp(it))
                SharedPreferenceManager.setFilterTimePeriod(context,5)
                viewModel.onFilterTimeOptionSelected(SelectedItemModel(id = 5, value = ""))
                viewModel.dismissAllDatePicker()
                viewModel.getData(context)


            },
            onDismiss = {
                uiState.startDate = 0
                uiState.endDate = 0
                viewModel.dismissAllDatePicker()
            }
        )
    }

    if (uiState.showFilterTimeOptionDialog){
        DialogComponent.TimeOptionDialog(
            selectedItem = uiState.selectedFilterTimeOption,
            startDate = uiState.startDate,
            endDate = uiState.endDate
        ) {
            if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
                viewModel.showStartDatePicker()
            } else {
                uiState.selectedFilterTimeOption = it
                SharedPreferenceManager.setFilterTimePeriod(context,it.id)
                viewModel.getData(context)
                viewModel.dismissAllDatePicker()
            }
        }

    }

    Scaffold(
        topBar = { DashboardTopBar(viewModel) }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            if(screenType.isScreenWithDetails){
                DashboardExpanded(viewModel = viewModel, onSmsClicked = onSmsClicked, onNavigateToSenderSmsList = onNavigateToSenderSmsList)
            }else{
                DashboardCompact(viewModel = viewModel, onSmsClicked = onSmsClicked, onNavigateToSenderSmsList = onNavigateToSenderSmsList)
            }

        }
    }




}







