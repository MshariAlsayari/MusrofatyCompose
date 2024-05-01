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
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.appbar.SenderSmsListCollapsedBar
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.DateRangeBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.FilterTimeBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.FilterWordsBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.bottomsheets.SenderSmsListBottomSheetType
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.*
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui.toolbar.scrollflags.ScrollState
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


private val MinToolbarHeight = 40.dp
private val MaxToolbarHeight = 85.dp


@Composable
fun SenderSmsListScreen() {
    val viewModel: SenderSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab = uiState.selectedTabIndex

    LaunchedEffect(
        key1 = uiState.selectedFilter,
        key2 = uiState.selectedFilterTimeOption
    ) {
        viewModel.getData()
    }

    LaunchedEffect(
        key1 = uiState.isRefreshing,
    ) {
        if (uiState.isRefreshing)
            viewModel.getData()
    }

    LaunchedEffect(key1 = selectedTab){
        when(SenderSmsListScreenTabs.getTabByIndex(selectedTab)){
            SenderSmsListScreenTabs.FINANCIAL -> viewModel.getFinancialStatistics()
            SenderSmsListScreenTabs.CATEGORIES -> viewModel.getCategoriesStatistics()
            else -> {}
        }

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
    val bottomSheetType = uiState.bottomSheetType
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }




    if (uiState.showGeneratingExcelFileDialog) {
        DialogComponent.LoadingDialog(message = R.string.notification_generate_excel_file_starting_message,)
    }


    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            when(bottomSheetType){
                SenderSmsListBottomSheetType.TIME_PERIODS ->  FilterTimeBottomSheet(viewModel,sheetState)
                SenderSmsListBottomSheetType.FILTER ->  FilterWordsBottomSheet(viewModel, sheetState)
                SenderSmsListBottomSheetType.DATE_PICKER ->DateRangeBottomSheet(viewModel, sheetState)
                null -> {}
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
                        viewModel.updateBottomSheetType(SenderSmsListBottomSheetType.FILTER)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, true)
                        }

                    },
                    onFilterTimeIconClicked = {
                        viewModel.updateBottomSheetType(SenderSmsListBottomSheetType.TIME_PERIODS)
                        coroutineScope.launch {
                            handleVisibilityOfBottomSheet(sheetState, true)
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
            SenderSmsListScreenTabs.values().forEachIndexed { index, tab ->
                Tab(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    selected = tabIndex == index,
                    onClick = { viewModel.onTabSelected(index) },
                    text = {
                        TextComponent.ClickableText(
                            text = stringResource(id = tab.title),
                            color = if (tabIndex == index) MaterialTheme.colors.secondary else colorResource(
                                id = R.color.light_gray
                            )
                        )
                    })
            }
        }
        when (SenderSmsListScreenTabs.getTabByIndex(tabIndex)) {
            SenderSmsListScreenTabs.ALL -> AllSmsTab(viewModel)
            SenderSmsListScreenTabs.FAVORITE -> FavoriteSmsTab(viewModel)
            SenderSmsListScreenTabs.DELETED -> SoftDeletedTab(viewModel)
            SenderSmsListScreenTabs.FINANCIAL -> FinancialStatisticsTab(viewModel)
            SenderSmsListScreenTabs.CATEGORIES -> CategoriesStatisticsTab(viewModel)
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
    list: LazyPagingItems<SmsModel>,
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
                    model = wrapSendersToSenderComponentModel(item , context),
                    onSmsClicked = {
                        viewModel.navigateToSmsDetails(it)
                    },
                    forceHideStoreAndCategory = true,
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
