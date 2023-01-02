package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.toCategoryStatisticsModel
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
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope                    = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        viewModel.getDate()
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 150.dp,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ButtonComponent.FloatingButton(firstIcon = Icons.Filled.KeyboardArrowUp, secondIcon = Icons.Filled.KeyboardArrowDown, isFirstPosition = sheetState.bottomSheetState.isCollapsed , onClick = {
                if (sheetState.bottomSheetState.isExpanded){
                    coroutineScope.launch {
                        sheetState.bottomSheetState.collapse()
                    }
                }else{
                    coroutineScope.launch {
                        sheetState.bottomSheetState.expand()
                    }
                }






            })

        },
        sheetContent = {
            BottomSheetContainer(viewModel)

        }) {
        DashboardContainer(viewModel)
    }


}

@Composable
fun BottomSheetContainer(viewModel: DashboardViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.default_margin40)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16))
    ) {
        BottomSheetHeader(viewModel)
        CategorySummaryComposable(viewModel)
    }

}

@Composable
fun BottomSheetHeader(viewModel: DashboardViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val filterTimeOption = uiState.selectedFilterTimeOption
    val filterText = if (filterTimeOption?.id == 5) DateUtils.formattedRangeDate(uiState.startDate,uiState.endDate) else stringResource(id = DateUtils.FilterOption.getFilterOption(filterTimeOption?.id).title)



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.default_margin20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextComponent.HeaderText(
            text = stringResource(id = R.string.tab_categories_statistics)
        )
        TextComponent.PlaceholderText(
            text = stringResource(id = R.string.common_filter_options) + ": " + filterText
        )
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardContainer(viewModel: DashboardViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                false
            )
        }
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
                viewModel.onFilterTimeOptionSelected(SelectedItemModel(id = 5, value = ""))
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
                viewModel.getDate()
                viewModel.dismissAllDatePicker()
            }
        }

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
                                viewModel.onDateRangeClicked()
                            })

                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                                viewModel.loadSms()

                            })
                },
                isParent = true
            )

        }
    ) { innerPadding ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = { viewModel.loadSms() },
        ) {

            FinancialCompose(Modifier.padding(innerPadding), viewModel)
        }

    }

}

@Composable
fun FinancialCompose(modifier: Modifier = Modifier, viewModel: DashboardViewModel) {

    val uiState by viewModel.uiState.collectAsState()


    when {
        uiState.isFinancialStatisticsSmsPageLoading -> ItemLoading()
        uiState.financialStatistics.isEmpty() -> EmptyCompose()
        else -> LazyFinancialCompose(modifier, viewModel)
    }


}

@Composable
fun LazyFinancialCompose(modifier: Modifier = Modifier, viewModel: DashboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

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
fun CategorySummaryComposable(viewModel: DashboardViewModel) {


    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val colors: ArrayList<Int> = ArrayList()
    colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toList())
    colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
    colors.addAll(ColorTemplate.COLORFUL_COLORS.toList())
    colors.addAll(ColorTemplate.LIBERTY_COLORS.toList())
    colors.addAll(ColorTemplate.PASTEL_COLORS.toList())
    colors.add(ColorTemplate.getHoloBlue())
    val categories = uiState.categoriesStatistics.values.mapIndexed { index, it ->
        it.toCategoryStatisticsModel(
            context,
            colors[index]
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isCategoriesStatisticsSmsPageLoading -> ProgressBar.CircleProgressBar()
            uiState.categoriesStatistics.isEmpty()-> EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
            else -> {
                CategoriesStatistics(categories = categories, onSmsClicked = {})

            }
        }


    }
}





@Composable
fun ItemLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
fun EmptyCompose() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyComponent.EmptyTextComponent(text = stringResource(id = R.string.empty_financial_statistics))
    }

}
