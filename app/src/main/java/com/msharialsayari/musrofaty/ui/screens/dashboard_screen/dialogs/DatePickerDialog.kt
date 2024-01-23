package com.msharialsayari.musrofaty.ui.screens.dashboard_screen.dialogs

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.DashboardViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.RangeDatePickerCompose
import com.msharialsayari.musrofaty.ui_component.SelectedItemModel
import com.msharialsayari.musrofaty.utils.DateUtils

@Composable
fun DatePickerDialog(viewModel: DashboardViewModel){

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val startDate = uiState.startDate
    val endDate = uiState.endDate
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp


    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.height((screenHeight * 0.7).dp),
            elevation = 8.dp,
            shape = RoundedCornerShape(0.dp)
        ) {
            RangeDatePickerCompose(
                startDate = startDate,
                endDate = endDate
            ){ selectedStartDate , selectedEndDate ->
                viewModel.updateDialogType(null)
                viewModel.onDatePeriodsSelected(selectedStartDate,selectedEndDate)
                viewModel.onFilterTimeOptionSelected(SelectedItemModel(id = DateUtils.FilterOption.RANGE.id, value = DateUtils.formattedRangeDate(start =selectedStartDate , end = selectedEndDate), isSelected = true))
                viewModel.getData(context)
            }
        }


    }



}