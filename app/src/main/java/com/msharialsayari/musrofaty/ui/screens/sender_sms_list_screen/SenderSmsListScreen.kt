package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.appbar.SenderSmsListCollapsedBar
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.FilterTimePeriodsBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.FilterWordsBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.*
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui.toolbar.scrollflags.ScrollState
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
import com.msharialsayari.musrofaty.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


private val MinToolbarHeight = 40.dp
private val MaxToolbarHeight = 85.dp


@Composable
fun SenderSmsListScreen(senderId: Int) {
    val viewModel: SenderSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        key1 = uiState.selectedFilter,
        key2 = uiState.selectedFilterTimeOption
    ) {
        viewModel.getDate()
    }

    LaunchedEffect(
        key1 = uiState.isRefreshing,
    ) {
        if(uiState.isRefreshing)
        viewModel.getDate()
    }

    when {
        uiState.isLoading -> PageLoading()
        else -> SenderSmsListContent(viewModel)
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SenderSmsListContent(viewModel: SenderSmsListViewModel) {

    val toolbarHeightRange = with(LocalDensity.current) { MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx() }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val nestedScrollConnection = getNestedScrollConnection(toolbarState = toolbarState)
    val uiState by viewModel.uiState.collectAsState()
    val isFilterTimeOptionBottomSheet = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
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
            },
            onDismiss = {
                uiState.startDate = 0
                uiState.endDate = 0
                viewModel.dismissAllDatePicker()
            }
        )
    }

    if (uiState.showGeneratingExcelFileDialog) {
        DialogComponent.LoadingDialog(message = R.string.notification_generate_excel_file_starting_message,)
    }




    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            if (isFilterTimeOptionBottomSheet.value)
                FilterTimePeriodsBottomSheet(viewModel = viewModel) {
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)
                    }
                    if (DateUtils.FilterOption.isRangeDateSelected(it.id)) {
                        viewModel.showStartDatePicker()
                    } else {
                        if( uiState.selectedFilterTimeOption?.id == it.id){
                           viewModel.updateSelectedFilterTimePeriods(null)
                        }else{
                            viewModel.updateSelectedFilterTimePeriods(it)
                        }
                        viewModel.dismissAllDatePicker()
                    }

                }
            else{
                FilterWordsBottomSheet(
                    viewModel = viewModel,
                    onDismiss = {
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, false)
                        }
                    }
                )

            }
        }) {

        SwipeRefresh(
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = { viewModel.refreshSms() },
        ) {

            Column(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                SenderSmsListCollapsedBar(
                    toolbarState = toolbarState,
                    viewModel = viewModel,
                    onFilterIconClicked = {
                        coroutineScope.launch {
                            isFilterTimeOptionBottomSheet.value = false
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }

                    },
                    onFilterTimeIconClicked = {
                        coroutineScope.launch {
                            isFilterTimeOptionBottomSheet.value = true
                            handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                        }


                    }

                )
                Tabs(viewModel = viewModel)
            }
        }


    }


}

@Composable
fun Tabs(viewModel: SenderSmsListViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val tabIndex = uiState.selectedTabIndex
    val tabTitles = listOf(
        R.string.tab_all_sms,
        R.string.tab_favorite_sms,
        R.string.tab_financial_statistics,
        R.string.tab_categories_statistics,
        R.string.tab_deleted_sms,
    )

    Column(Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.background,
            selectedTabIndex = tabIndex,
            edgePadding = 0.dp,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
                    color = MaterialTheme.colors.secondary,
                    height = TabRowDefaults.IndicatorHeight
                )
            }
        ) {
            tabTitles.forEachIndexed { index, stringResId ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    selected = tabIndex == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = {
                        TextComponent.ClickableText(
                            text = stringResource(id = stringResId),
                            color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                id = R.color.light_gray
                            )
                        )
                    })
            }
        }
        when (tabIndex) {
            0 -> AllSmsTab(viewModel)
            1 -> FavoriteSmsTab(viewModel)
            2 -> FinancialStatisticsTab(viewModel)
            3 -> CategoriesStatisticsTab(viewModel)
            4 -> SoftDeletedTab(viewModel)
        }
    }


}


@Composable
fun PageLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}


@Composable
fun LazySenderSms(
    list: LazyPagingItems<SmsEntity>,
    viewModel: SenderSmsListViewModel
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier,
        state = rememberLazyListState(),
    ) {

        itemsIndexed(key = { _, sms -> sms.id }, items = list) { index, item ->

            if (item != null) {
                SmsComponent(
                    onSmsClicked ={
                        viewModel.navigateToSmsDetails(item.id)
                    },
                    model = viewModel.wrapSendersToSenderComponentModel(item, context),
                    onActionClicked = { model, action ->
                        when (action) {
                            SmsActionType.FAVORITE -> viewModel.favoriteSms(
                                model.id,
                                model.isFavorite
                            )

                            SmsActionType.COPY -> {
                                Utils.copyToClipboard(item.body, context)
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.common_copied),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            SmsActionType.SHARE -> {
                                Utils.shareText(item.body, context)
                            }

                            SmsActionType.DELETE -> viewModel.softDelete(
                                model.id,
                                model.isDeleted
                            )
                        }
                    })

            }


        }


    }


}

@Composable
fun EmptySmsCompose() {
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


@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}

@Composable
fun getNestedScrollConnection(
    listState: LazyListState = rememberLazyListState(),
    scope: CoroutineScope = rememberCoroutineScope(),
    toolbarState: ToolbarState
) = remember {

    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            toolbarState.scrollTopLimitReached =
                listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
            toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
            return Offset(0f, toolbarState.consumed)
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            if (available.y > 0) {
                scope.launch {
                    animateDecay(
                        initialValue = toolbarState.height + toolbarState.offset,
                        initialVelocity = available.y,
                        animationSpec = FloatExponentialDecaySpec()
                    ) { value, _ ->
                        toolbarState.scrollTopLimitReached =
                            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                        toolbarState.scrollOffset =
                            toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                        if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                    }
                }
            }

            return super.onPostFling(consumed, available)
        }
    }
}
