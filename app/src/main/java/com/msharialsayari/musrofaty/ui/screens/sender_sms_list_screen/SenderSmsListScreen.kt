package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui.screens.sender_details_screen.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.AllSmsTab
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.FavoriteSmsTab
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.tabs.StatisticsTab
import com.msharialsayari.musrofaty.ui.toolbar.CollapsingToolbar
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui.toolbar.scrollflags.ScrollState
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

private val MinToolbarHeight = 40.dp
private val MaxToolbarHeight = 85.dp




@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SenderSmsListScreen(senderId: Int,
                        onDetailsClicked: (Int)->Unit,
                        onNavigateToFilterScreen: (Int,Int?)->Unit,
                        onBack: ()->Unit
) {
    val viewModel: SenderSmsListViewModel =  hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit){ viewModel.getSender(senderId) }


    when{
        uiState.isLoading -> PageLoading()
        uiState.sender != null ->
            PageContainer(
                viewModel,
                onDetailsClicked,
                onNavigateToFilterScreen,
                onBack)
    }





}

@Composable
fun FilterTimeOptionsBottomSheet(viewModel: SenderSmsListViewModel, onFilterSelected:()->Unit){
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
fun FilterBottomSheet(viewModel: SenderSmsListViewModel, onFilterSelected:()->Unit,   onCreateFilterClicked: ()->Unit, onFilterLongPressed:(Int)->Unit ){
    val context                           = LocalContext.current
    val uiState                           by viewModel.uiState.collectAsState()
    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.common_filter,
        description= R.string.on_long_press_on_filter,
        list = viewModel.getFilterOptions(uiState.selectedFilter),
        trailIcon = {
                    Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                        onCreateFilterClicked()
                    } )
        },
        canUnSelect = true,
        onSelectItem = {
            if (it.isSelected) {
                uiState.selectedFilter = it
            }else{
                uiState.selectedFilter = null
            }
            onFilterSelected()
        },
        onLongPress = {
            onFilterLongPressed(it.id)
        }


    )
}



@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PageContainer(
                  viewModel: SenderSmsListViewModel,
                  onDetailsClicked: (Int)->Unit,
                  onNavigateToFilterScreen: (Int,Int?)->Unit,
                  onBack: ()->Unit){
    val context                           = LocalContext.current
    val toolbarHeightRange                = with(LocalDensity.current) {MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx() }
    val toolbarState                      = rememberToolbarState(toolbarHeightRange)
    val nestedScrollConnection            = getNestedScrollConnection(toolbarState = toolbarState)
    val uiState                           by viewModel.uiState.collectAsState()
    val isFilterTimeOptionBottomSheet     = remember { mutableStateOf(false) }
    val coroutineScope                    = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    LaunchedEffect(key1 = Unit){
        viewModel.getAllSmsBySenderId(uiState.sender?.id?:0)
    }


    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {

            if (isFilterTimeOptionBottomSheet.value)
                FilterTimeOptionsBottomSheet(viewModel =viewModel, onFilterSelected= {
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                    viewModel.onFilterChanged()

                })


            else
                FilterBottomSheet(
                    viewModel =viewModel,
                    onFilterSelected = {
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, !sheetState.isVisible)
                    }
                    viewModel.onFilterChanged()

                },
                    onCreateFilterClicked = {
                        uiState.sender?.let {
                            onNavigateToFilterScreen(it.id, null)
                        }
                    },
                    onFilterLongPressed = {filterId->
                        uiState.sender?.let {senderModel->
                            onNavigateToFilterScreen(senderModel.id, filterId)
                        }
                    }
                )






        }){

        Column(modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            CollapsedToolbar(
                toolbarState             = toolbarState,
                viewModel                = viewModel,
                onDetailsClicked         = onDetailsClicked,
                onBack                   = onBack,
                onCreateFilterClicked    = { uiState.sender?.let {
                    onNavigateToFilterScreen(it.id, null)
                } },
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
            Tabs(uiState.sender?.id?:0)
        }

    }


}

@Composable
fun Tabs(senderId: Int){
    Column {
        var tabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf(R.string.tab_all_sms, R.string.tab_favorite_sms,R.string.tab_statistics)
        Column {
            TabRow(
                selectedTabIndex = tabIndex,
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
                        onClick = { tabIndex = index },
                        text = { Text(text = stringResource(id = stringResId), color = MaterialTheme.colors.onBackground) })
                }
            }
            when (tabIndex) {
                0 ->  AllSmsTab(senderId = senderId)
                1 ->  FavoriteSmsTab(senderId = senderId)
                2 ->  StatisticsTab(senderId = senderId)
            }
        }

    }
}

@Composable
fun CollapsedToolbar(toolbarState: ToolbarState,
                     viewModel: SenderSmsListViewModel,
                     onDetailsClicked: (Int)->Unit,
                     onCreateFilterClicked: ()->Unit,
                     onBack: ()->Unit,
                     onFilterTimeIconClicked: ()->Unit,
                     onFilterIconClicked: ()->Unit){
    CollapsingToolbar(
        progress   = toolbarState.progress,
        actions    = {ToolbarActionsComposable(viewModel,onBack, onFilterTimeIconClicked,onFilterIconClicked)},
        collapsedComposable      = { CollapsedToolbarComposable(viewModel)},
        expandedComposable = { ExpandedToolbarComposable(viewModel,onDetailsClicked, onCreateFilterClicked)},
        modifier   = Modifier
            .fillMaxWidth()
            .height(toolbarState.height.dp + 5.dp)

    )
}


@Composable
fun PageLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}



@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun LazySenderSms(
    list: LazyPagingItems<SmsEntity>,
    viewModel: SenderSmsListViewModel
) {
    val context    = LocalContext.current
    val listState  = rememberLazyListState()

    if (list.itemSnapshotList.isNotEmpty()) {

        LazyColumn(
            modifier = Modifier,
            state = listState,
        ) {

            itemsIndexed(key = { _, sms -> sms.id }, items = list) { index, item, ->

                if (item != null) {

                    SmsComponent(
                        model = viewModel.wrapSendersToSenderComponentModel(item, context),
                        onActionClicked = { model, action ->
                            when (action) {
                                SmsActionType.FAVORITE -> viewModel.favoriteSms(
                                    model.id,
                                    model.isFavorite
                                )
                                SmsActionType.SHARE -> {}
                            }
                        })

                    if (index != list.itemSnapshotList.size - 1)
                        DividerComponent.HorizontalDividerComponent()


                }


            }


        }
    }else{

        EmptySmsCompose()
    }






}

@Composable
fun EmptySmsCompose(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        EmptyComponent.EmptyTextComponent()
    }
}


@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}

@Composable
fun getNestedScrollConnection(listState   : LazyListState  = rememberLazyListState(),
                              scope       : CoroutineScope = rememberCoroutineScope(),
                              toolbarState: ToolbarState
) = remember {

    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            toolbarState.scrollTopLimitReached = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
            toolbarState.scrollOffset          = toolbarState.scrollOffset - available.y
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExpandedToolbarComposable(
    viewModel: SenderSmsListViewModel,
    onDetailsClicked: (Int)->Unit,
    onCreateFilterClicked: ()->Unit,
){

    Column( modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        UpperPartExpandedToolbar(viewModel, onDetailsClicked)
        DividerComponent.HorizontalDividerComponent()
        LowerPartExpandedToolbar(viewModel,onCreateFilterClicked)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UpperPartExpandedToolbar(viewModel: SenderSmsListViewModel,onDetailsClicked: (Int)->Unit){
    val context = LocalContext.current
    val uiState  by viewModel.uiState.collectAsState()
    val sender = uiState.sender
    val smsCount = uiState.allSmsFlow?.collectAsState(initial = emptyList())?.value?.size ?: 0
    val model = SenderComponentModel(
        senderId    = sender?.id ?:0,
        senderName  = sender?.senderName ?: "",
        displayName = SenderModel.getDisplayName(context, sender),
        senderType  = ContentModel.getDisplayName(context, sender?.content),
    )
    SenderComponent(
        modifier = Modifier.padding(horizontal = 16.dp ),
        model =model)

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Column {
            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_sender_shortcut) + ": " + sender?.senderName
            )

            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_sms_total)+ ": "+ smsCount.toString() + " " + pluralStringResource(id = R.plurals.common_sms, count = smsCount)
            )

        }

        ButtonComponent.OutlineButton(
            text = R.string.common_details,
            onClick = {
                onDetailsClicked(sender?.id?:0)
            }
        )

    }

}

@Composable
fun LowerPartExpandedToolbar(viewModel: SenderSmsListViewModel, onCreateFilterClicked: ()->Unit,){

    val uiState  by viewModel.uiState.collectAsState()
    val filters = uiState.filters
    val selectedTimeFilter = uiState.selectedFilterTimeOption
    val selectedFilter = uiState.selectedFilter

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {


        TextComponent.PlaceholderText(
            text = stringResource(id = R.string.common_filter_options) + ": " + (selectedTimeFilter?.value ?: stringArrayResource(id = R.array.filter_options)[0])
        )


        if (filters.isEmpty()){

            Row(horizontalArrangement = Arrangement.SpaceAround) {
                TextComponent.PlaceholderText(
                    text = stringResource(id = R.string.common_filter)+ ": "
                )

                TextComponent.ClickableText(
                    modifier= Modifier.clickable {
                        onCreateFilterClicked()
                    },
                    text = stringResource(id = R.string.create_filter)
                )
            }

        }else{
            TextComponent.PlaceholderText(
                text = stringResource(id = R.string.common_filter)+ ": "+ (selectedFilter?.value ?: stringResource(id = R.string.common_no_selected))
            )
        }




    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CollapsedToolbarComposable(viewModel:SenderSmsListViewModel){
    val context  = LocalContext.current
    val uiState  by viewModel.uiState.collectAsState()
    val smsCount = uiState.allSmsFlow?.collectAsState(initial = emptyList())?.value?.size ?: 0

    Column(modifier = Modifier.fillMaxWidth()) {
        TextComponent.HeaderText(
            text = SenderModel.getDisplayName(context, uiState.sender)
        )

        TextComponent.BodyText(
            text = smsCount.toString() + " " + pluralStringResource(id = R.plurals.common_sms, count = smsCount )
        )

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToolbarActionsComposable(viewModel: SenderSmsListViewModel, onBack:()->Unit, onFilterTimeIconClicked:()->Unit, onFilterIconClicked:()->Unit) {
    val uiState  by viewModel.uiState.collectAsState()
    val filters = uiState.filters

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon( Icons.Default.ArrowBack,

            contentDescription = null,
            modifier = Modifier
                .mirror()
                .clickable {
                    onBack()

                })


        Row( horizontalArrangement = Arrangement.spacedBy(16.dp)) {


            if (filters.isNotEmpty())
            Icon(painter = painterResource(id = R.drawable.ic_filter),

                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable {
                        onFilterIconClicked()

                    })

            Icon(Icons.Default.DateRange,

                contentDescription = null,
                modifier = Modifier
                    .mirror()
                    .clickable {
                        onFilterTimeIconClicked()

                    })
        }




    }

}
