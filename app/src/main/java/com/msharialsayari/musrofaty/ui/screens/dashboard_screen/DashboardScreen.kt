package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)

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
                viewModel.onFilterTimeOptionSelected( SelectedItemModel(id = 5, value = "") )
                viewModel.dismissAllDatePicker()
                viewModel.getDate()


            },
            onDismiss = {
                uiState.startDate = 0
                uiState.endDate = 0
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
                    if (DateUtils.FilterOption.isRangeDateSelected(it.id )) {
                        viewModel.showStartDatePicker()
                    } else {
                        uiState.selectedFilterTimeOption = it
                        viewModel.getDate()
                        viewModel.dismissAllDatePicker()
                    }

                }



            }) {

            SwipeRefresh(
                state = rememberSwipeRefreshState(uiState.isRefreshing),
                onRefresh = { viewModel.loadSms() },
            ) {

                FinancialCompose(Modifier.padding(innerPadding), viewModel)
            }
        }

    }

}

@Composable
fun FinancialCompose(modifier: Modifier=Modifier,viewModel: DashboardViewModel){

    val uiState by viewModel.uiState.collectAsState()


    when {
        uiState.isFinancialStatisticsSmsPageLoading -> ItemLoading()
        uiState.financialStatistics.isEmpty()       -> EmptyCompose()
        else                                        -> LazyFinancialCompose(modifier, viewModel)
    }


}

@Composable
fun LazyFinancialCompose(modifier: Modifier=Modifier,viewModel: DashboardViewModel){
    val uiState by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {

        items(items = uiState.financialStatistics.values.toList(), itemContent = {
            FinancialStatistics(
                model = FinancialStatisticsModel(
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
fun FilterTimeOptionsBottomSheet(viewModel: DashboardViewModel, onFilterSelected:(SelectedItemModel)->Unit){
    val uiState                           by viewModel.uiState.collectAsState()
    BottomSheetComponent.TimeOptionsBottomSheet(selectedItem = uiState.selectedFilterTimeOption, startDate = uiState.startDate, endDate = uiState.endDate){
        onFilterSelected(it)
    }
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

@Composable
fun EmptyCompose(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
    }

}
