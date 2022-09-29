package com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.compose.itemsIndexed
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ContentModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.ui.toolbar.CollapsingToolbar
import com.msharialsayari.musrofaty.ui.toolbar.ToolbarState
import com.msharialsayari.musrofaty.ui.toolbar.scrollflags.ScrollState
import com.msharialsayari.musrofaty.ui_component.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

private val MinToolbarHeight = 30.dp
private val MaxToolbarHeight = 80.dp




@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun SenderSmsListScreen(senderId: Int) {
    val viewModel: SenderSmsListViewModel =  hiltViewModel()
    val uiState                           by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit){ viewModel.getSenderWithAllSms(senderId) }

    when{
        uiState.isLoading -> PagerLoading()
        uiState.sender != null && uiState.smsFlow != null ->PageContainer(uiState.sender!!,  uiState.smsFlow!!, viewModel)
    }





}

@Composable
fun PageContainer(sender:SenderModel, sms: Flow<PagingData<SmsEntity>>, viewModel: SenderSmsListViewModel){
    val context                           = LocalContext.current
    val toolbarHeightRange                = with(LocalDensity.current) {MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx() }
    val toolbarState                      = rememberToolbarState(toolbarHeightRange)
    val nestedScrollConnection            = getNestedScrollConnection(toolbarState = toolbarState)
    Column(modifier = Modifier.nestedScroll(nestedScrollConnection)) {

        CollapsedToolbar(toolbarState = toolbarState, sender=sender)
        LazySenderSms(sms,viewModel)


    }

}

@Composable
fun CollapsedToolbar(toolbarState:ToolbarState, sender:SenderModel){
    val context                           = LocalContext.current
    CollapsingToolbar(
        progress = toolbarState.progress,
        title=SenderModel.getDisplayName(context,sender),
        background={ExpandedToolbarBackground(sender)},
        modifier = Modifier
            .fillMaxWidth()
            .height(toolbarState.height.dp)
            .graphicsLayer { translationY = toolbarState.offset }
    )
}


@Composable
fun PagerLoading(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}



@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun LazySenderSms(sms: Flow<PagingData<SmsEntity>>,
                  viewModel: SenderSmsListViewModel
) {
    val context    = LocalContext.current
    val smsItems   = sms.collectAsLazyPagingItems()
    val listState  = rememberLazyListState()

    LazyColumn(
        modifier              = Modifier,
        state                 = listState,
    ) {

        itemsIndexed( key ={_ , sms-> sms.id} ,items= smsItems ) { index,item, ->

            if (item != null){

                SmsComponent(model = viewModel.wrapSendersToSenderComponentModel(item,context), onActionClicked = { model, action ->
                    when (action) {
                        SmsActionType.FAVORITE -> viewModel.favoriteSms(model.id, model.isFavorite)
                        SmsActionType.SHARE -> {}
                    }
                })

                if (index != smsItems.itemSnapshotList.size-1)
                DividerComponent.HorizontalDividerComponent()




            }


        }




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


@Composable
fun ExpandedToolbarBackground(sender: SenderModel){
    val context = LocalContext.current
    val model = SenderComponentModel(
        senderId    = sender.id,
        senderName  = sender.senderName,
        displayName = SenderModel.getDisplayName(context, sender),
        senderType  = ContentModel.getDisplayName(context, sender.content),
    )

    SenderComponent(model)

}
