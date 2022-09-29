package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui.toolbar.CollapsingToolbar
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui.toolbar.scrollflags.ScrollState
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch


private val MinToolbarHeight = 60.dp
private val MaxToolbarHeight = 160.dp

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SenderSmsListScreen(senderId: Int) {
    val context = LocalContext.current
    val viewModel: SenderSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        viewModel.getSenderWithAllSms(senderId)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
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
                        ) { value, velocity ->
                            toolbarState.scrollTopLimitReached = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                            toolbarState.scrollOffset = toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Column(modifier = Modifier.nestedScroll(nestedScrollConnection)) {

        CollapsingToolbar(
            progress = toolbarState.progress,
            title=SenderModel.getDisplayName(context,uiState.sender),
            background={ExpandedToolbarBackground()},
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarState.height.dp)
                .graphicsLayer { translationY = toolbarState.offset }
        )

        LazySenderSms(viewModel, uiState,senderId)





    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun LazySenderSms(
    viewModel: SenderSmsListViewModel,
    uiState: SenderSmsListViewModel.SenderSmsListUiState,
    senderId:Int
) {
    val context = LocalContext.current
    val lazyMovieItems: LazyPagingItems<SmsEntity>? = uiState.smsFlow?.collectAsLazyPagingItems()
    val listState       = rememberLazyListState()

    if (lazyMovieItems != null)
    LazyColumn(
        modifier            = Modifier,
        state               = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding      = PaddingValues(
            horizontal          = 0.dp,
            vertical            = 0.dp
        )
    ) {


        items( key ={sms-> sms.id} , items= lazyMovieItems ) {  item ->

            if (item != null)
            SmsComponent(model = uiState.wrapSendersToSenderComponentModelList(item,context), onActionClicked = { model, action ->
                when (action) {
                    SmsActionType.FAVORITE -> viewModel.favoriteSms(model.id, model.isFavorite)
                    SmsActionType.SHARE -> {}
                }
            })




        }




    }




//    VerticalEasyList(
//        list = uiState.wrapSendersToSenderComponentModelList(lazyMovieItems?.itemSnapshotList?.items?: emptyList(), context) ,
//        view = { SmsComponent(model = it, onActionClicked = { model,action->
//            when(action){
//                SmsActionType.FAVORITE -> viewModel.favoriteSms(model.id, model.isFavorite)
//                SmsActionType.SHARE -> {}
//            }
//        }) },
//        onLoadingNextPage = {
//
//        },
//        dividerView = { HorizontalDividerComponent() },
//        onItemClicked = { item, position -> },
//        isLoading = uiState.isLoading,
//        loadingProgress = { ProgressBar.CircleProgressBar() },
//        emptyView = { EmptyComponent.EmptyTextComponent() },
//        onRefresh = { viewModel.getSenderWithAllSms(senderId)}
//    )

}


@Composable
fun ExpandedToolbarBackground(){
    Image(
        painter = painterResource(id = R.drawable.bg_expanded_toolbar),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxSize()
    )
}
