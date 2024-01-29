package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.utils.mirror

@Composable
fun StoresScreen() {
    val viewModel: StoresViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isSearchTopBar = remember { mutableStateOf(false) }
    val screenType = MusrofatyTheme.screenType

    Scaffold(
        topBar = {
            AppBarComponent.SearchTopAppBar(
                title = Screen.StoresScreen.title,
                onArrowBackClicked = { viewModel.navigateUp() },
                isParent = screenType.isScreenWithDetails,
                isSearchMode = isSearchTopBar.value,
                onCloseSearchMode = {
                    isSearchTopBar.value = false
                },
                actions = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .mirror()
                            .clickable {
                                isSearchTopBar.value = !isSearchTopBar.value
                            })
                },
                onTextChange = {
                    viewModel.getStores(it)
                },
                onSearchClicked = {
                    viewModel.getStores(it)
                },


                )

        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> PageLoading(modifier = Modifier.padding(innerPadding))
            else -> PageCompose(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onStoreClicked = {
                    viewModel.navigateToStoreSmsListScreen(it)
                }
            )
        }
    }

}

@Composable
fun PageLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@Composable
fun PageCompose(
    modifier: Modifier = Modifier,
    viewModel: StoresViewModel,
    onStoreClicked: (String) -> Unit
) {

    StoresList(modifier,viewModel){
        onStoreClicked(it)
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoresList(
    modifier: Modifier,
    viewModel: StoresViewModel,
    onRowClicked: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val stores = uiState.stores?.collectAsState(initial = emptyList())?.value ?: emptyList()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()
    val groupedStores = stores.groupBy { it.store.category_id }
        .toSortedMap(compareBy<Int> { it }.thenBy { it }.reversed())

    LazyColumn(
        modifier=modifier,
        state = listState,
    ) {

        groupedStores.forEach { (categoryId, stores) ->

            stickyHeader {

                Row(
                    modifier = Modifier
                        .background(MusrofatyTheme.colors.activeColor)
                        .padding(5.dp)
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextComponent.HeaderText(
                        text = viewModel.getCategoryDisplayName(categoryId, categories),
                        color = MaterialTheme.colors.onSecondary
                    )


                    TextComponent.BodyText(
                        text = stringResource(id = R.string.common_total) + ": " + stores.size.toString(),
                        color = MaterialTheme.colors.onSecondary
                    )

                }

            }

            items(stores) { item ->
                StoreAndCategoryCompose(viewModel, item ){ onRowClicked(it) }
                DividerComponent.HorizontalDividerComponent()
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreAndCategoryCompose(
    viewModel: StoresViewModel,
    item: StoreWithCategory,
    onRowClicked: (String) -> Unit
) {


    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRowClicked(item.store.name)
            }
            .padding(all = dimensionResource(id = R.dimen.default_margin16)),
        text = { Text(text = item.store.name) },
        trailing = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextComponent.PlaceholderText(
                    text = viewModel.getCategoryDisplayName(item.store.category_id, categories)
                )

                Icon(Icons.Default.KeyboardArrowRight,
                    modifier = Modifier.mirror(),
                    contentDescription = null )
            }

        }
    )

}