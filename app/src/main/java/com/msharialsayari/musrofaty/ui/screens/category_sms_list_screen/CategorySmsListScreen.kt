package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.tabs.CategorySmsListTabs
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent
import com.msharialsayari.musrofaty.ui_component.wrapSendersToSenderComponentModel
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySmsListScreen() {

    val viewModel: CategorySmsListViewModel = hiltViewModel()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab = uiState.selectedTabIndex
    val isFilterDateSelected = uiState.selectedFilterTimeOption != null && uiState.selectedFilterTimeOption?.id != 0


    LaunchedEffect(
        key1 = uiState.selectedFilterTimeOption
    ) {
        viewModel.getData()
    }

    LaunchedEffect(
        key1 = uiState.isRefreshing,
    ) {
        if (uiState.isRefreshing)
            viewModel.getData()
    }


    if (uiState.isLoading) {
        PageLoading()
    } else {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MusrofatyTheme.colors.toolbarColor,
                        scrolledContainerColor = MusrofatyTheme.colors.toolbarColor,
                        navigationIconContentColor = MusrofatyTheme.colors.iconBackgroundColor,
                        actionIconContentColor = if (isFilterDateSelected) MusrofatyTheme.colors.secondary else MusrofatyTheme.colors.iconBackgroundColor,
                    ),
                    title = {
                        Text(
                            "Large Top App Bar",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.mirror()

                            )
                        }

                    },
                    actions = {
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Default.DateRange,
                                tint = if (isFilterDateSelected) MusrofatyTheme.colors.secondary else MusrofatyTheme.colors.iconBackgroundColor,
                                contentDescription = null,
                                modifier = Modifier
                                    .mirror()
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
        ) { innerPadding ->
            CategorySmsListContent(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CategorySmsListContent(
    modifier: Modifier = Modifier,
    viewModel: CategorySmsListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch {
            BottomSheetComponent.handleVisibilityOfBottomSheet(
                sheetState,
                false
            )
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = sheetState,
        sheetContent = {

        }) {

        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = rememberSwipeRefreshState(uiState.isRefreshing),
            onRefresh = { },
        ) {

            CategorySmsListTabs(viewModel = viewModel)
        }


    }


}

@Composable
fun SmsList(
    list: LazyPagingItems<SmsModel>,
    viewModel: CategorySmsListViewModel,
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
private fun PageLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}