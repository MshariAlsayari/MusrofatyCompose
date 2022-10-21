package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.mirror
import com.squaredem.composecalendar.ComposeCalendar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope                    = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    LaunchedEffect(Unit){
        viewModel.getDate()
    }


    if (uiState.showStartDatePicker) {
        ComposeDatePicker(
            title = stringResource(id = R.string.common_start_date),
            startDate= LocalDate.of(
                DateUtils.getSalaryCalender().get(Calendar.YEAR),
                DateUtils.getSalaryCalender().get(Calendar.MONTH)+1 ,
                DateUtils.getSalaryCalender().get(Calendar.DAY_OF_MONTH) ),
            onDone = { it: LocalDate ->
                // Hide dialog
                viewModel.dismissAllDatePicker()
                // Do something with the date
            },
            onDismiss = {
                // Hide dialog
                viewModel.dismissAllDatePicker()
            }
        )
    }




    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = BottomNavItem.Dashboard.title,
                actions = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                                coroutineScope.launch {
                                    BottomSheetComponent.handleVisibilityOfBottomSheet(
                                        sheetState,
                                        true
                                    )
                                }

                            })
                },
                isParent = true
            )

        }
    ) { innerPadding ->

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                FilterTimeOptionsBottomSheet(viewModel =viewModel) {
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                    }
                    if (uiState.selectedFilterTimeOption?.id != 5) {
                        viewModel.getDate()
                        viewModel.dismissAllDatePicker()
                    } else {
                        viewModel.showStartDatePicker()
                    }

                }



            }) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
            ) {


                FinancialCompose(viewModel)
            }
        }

    }

}

@Composable
fun FinancialCompose(viewModel: DashboardViewModel){

    val uiState by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()

    if (uiState.isFinancialStatisticsSmsPageLoading){
        ItemLoading()
    }else

    LazyColumn(
        modifier = Modifier,
        state = listState,
    ) {

        items(items = uiState.financialStatistics.values.toList() , itemContent =  {
            FinancialStatistics(model = FinancialStatisticsModel(
                filterOption = viewModel.getFilterTimeOption(),
                currency = it.currency,
                total = it.expenses.plus(it.income),
                incomeTotal = it.income,
                expensesTotal = it.expenses,
            )
            )


        })




    }

}


@Composable
fun FilterTimeOptionsBottomSheet(viewModel: DashboardViewModel, onFilterSelected:()->Unit){
    val context                           = LocalContext.current
    val uiState                           by viewModel.uiState.collectAsState()
    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.common_filter_options,
        list = viewModel.getFilterTimeOptions(context, uiState.selectedFilterTimeOption),
        onSelectItem = {
            uiState.selectedFilterTimeOption = it
            onFilterSelected()
        }
    )
}


@Composable
fun ItemLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}
