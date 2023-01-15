package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.screens.dashboard_screen.bottomSheet.BottomSheetContainer
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.date_picker.ComposeDatePicker
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardScreen(onSmsClicked: (String) -> Unit,onNavigateToSenderSmsList:(senderId:Int)->Unit) {
    val context = LocalContext.current
    val viewModel: DashboardViewModel = hiltViewModel()
    val sheetState = rememberBottomSheetScaffoldState()
    val coroutineScope                    = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        viewModel.getData()
        //viewModel.initJobs(context)
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 80.dp,
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
        DashboardContainer(viewModel,onSmsClicked,onNavigateToSenderSmsList)
    }


}





@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DashboardContainer(viewModel: DashboardViewModel,onSmsClicked: (String) -> Unit,onNavigateToSenderSmsList:(senderId:Int)->Unit) {

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
                viewModel.getData()


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
                viewModel.getData()
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
            modifier = Modifier.padding(innerPadding),
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = { viewModel.loadSms() },
        ) {
            SmsListCompose(viewModel,onSmsClicked,  onNavigateToSenderSmsList)

        }

    }

}

@Composable
fun SmsListCompose(viewModel: DashboardViewModel  , onSmsClicked: (String) -> Unit,onNavigateToSenderSmsList:(senderId:Int)->Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsFlow?.collectAsLazyPagingItems()

    when{
        uiState.isSmsPageLoading                        -> ItemLoading()
        smsList?.itemSnapshotList?.isNotEmpty() == true -> LazySenderSms(viewModel = viewModel, list = smsList, onSmsClicked = onSmsClicked , onNavigateToSenderSmsList = onNavigateToSenderSmsList)
        else                                            -> EmptyCompose()
    }





}

@Composable
fun LazySenderSms(
    list: LazyPagingItems<SmsEntity>,
    viewModel: DashboardViewModel,
    onSmsClicked: (String) -> Unit,
    onNavigateToSenderSmsList:(senderId:Int)->Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier,
        state = rememberLazyListState(),
    ) {

        itemsIndexed(key = { _, sms -> sms.id }, items = list) { index, item ->

            if (item != null) {

                SmsComponent(
                    modifier = Modifier.clickable {
                        onSmsClicked(item.id)
                    },
                    model = viewModel.wrapSendersToSenderComponentModel(item, context),
                    onSenderIconClicked = {
                     onNavigateToSenderSmsList(it)
                    },
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

                            SmsActionType.ShARE -> {
                                Utils.shareText(item.body, context)
                            }
                        }
                    })


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
