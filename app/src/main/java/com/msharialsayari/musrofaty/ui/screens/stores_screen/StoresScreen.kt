package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.utils.mirror
import kotlinx.coroutines.launch

@Composable
fun StoresScreen(onNavigateToCategoryScreen:(Int)->Unit,
                 onNavigateToStoreSmsListScreen:(String)->Unit,
                 onBackPressed:()->Unit) {
    val viewModel: StoresViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isSearchTopBar = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            AppBarComponent.SearchTopAppBar(
                title = Screen.StoresScreen.title,
                onArrowBackClicked = onBackPressed,
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
            uiState.isLoading -> PageLoading( modifier = Modifier.padding(innerPadding))
            else -> PageCompose(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onCategoryLongPressed = {
                onNavigateToCategoryScreen(it)
            },

                onStoreClicked = {
                    onNavigateToStoreSmsListScreen(it)
                }

                )
        }
    }

}

@Composable
fun PageLoading(modifier: Modifier=Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBar.CircleProgressBar()
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(modifier: Modifier=Modifier, viewModel: StoresViewModel,onCategoryLongPressed:(Int)->Unit, onStoreClicked:(String)->Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope                    = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetScaffoldState()


    BackHandler(sheetState.bottomSheetState.isExpanded) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value){
       AddCategoryDialog(
            viewModel,
            onDismiss = {
                openDialog.value = false
            })
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {


            CategoryBottomSheet(
                viewModel =viewModel,
                onCategorySelected = {
                    viewModel.onCategorySelected(it)
                    viewModel.changeStoreCategory()
                    coroutineScope.launch {
                        handleVisibilityOfBottomSheet(sheetState, false)
                    }

                },
                onCreateCategoryClicked = {
                    openDialog.value = true
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                },
                onCategoryLongPressed = {categoryId->
                    coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
                    onCategoryLongPressed(categoryId)

                }
            )

        }) {
        StoresList(viewModel, onCategoryClicked = {
            viewModel.onStoreSelected(it)
            coroutineScope.launch {
                handleVisibilityOfBottomSheet(sheetState, true)
            }
        },
            onStoreNameClicked = {
                onStoreClicked(it)
            }

            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoresList(viewModel: StoresViewModel, onCategoryClicked:(StoreWithCategory)->Unit, onStoreNameClicked:(String)->Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()
    val stores = uiState.stores?.collectAsState(initial = emptyList())?.value ?: emptyList()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()
    val groupedStores = stores.groupBy { it.store.category_id }.toSortedMap(compareBy<Int> { it }.thenBy { it }.reversed())

    LazyColumn(
        state = listState,
    ) {

        groupedStores.forEach { (categoryId, stores) ->

            stickyHeader {

                Row(modifier = Modifier
                    .background(MusrofatyTheme.colors.activeColor)
                    .padding(5.dp)
                    .fillMaxWidth(),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextComponent.HeaderText(
                        text =  viewModel.getCategoryDisplayName( categoryId, categories),
                        color = MaterialTheme.colors.onSecondary
                    )


                    TextComponent.BodyText(
                        text = stringResource(id = R.string.common_total) + ": " +  stores.size.toString(),
                        color = MaterialTheme.colors.onSecondary
                    )

                }

            }


            items(stores) {  item ->
                StoreAndCategoryCompose(viewModel,item, onCategoryClicked={
                    onCategoryClicked(it)
                },
                    onStoreNameClicked = {
                        onStoreNameClicked(it)
                    }
                )
                DividerComponent.HorizontalDividerComponent()

            }
        }



    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreAndCategoryCompose(viewModel: StoresViewModel, item: StoreWithCategory, onCategoryClicked:(StoreWithCategory)->Unit,onStoreNameClicked:(String)->Unit) {


    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onStoreNameClicked(item.store.name)
            }
            .padding(all = dimensionResource(id = R.dimen.default_margin16)),
        text = { Text(text = item.store.name) },
        trailing = {
            TextComponent.ClickableText(
                modifier = Modifier.clickable { onCategoryClicked(item) },
                text =  viewModel.getCategoryDisplayName( item.store.category_id, categories))
        }
    )

}

@Composable
fun CategoryBottomSheet(viewModel: StoresViewModel, onCategorySelected:(SelectedItemModel)->Unit, onCreateCategoryClicked: ()->Unit, onCategoryLongPressed:(Int)->Unit ){
    val context                           = LocalContext.current
    val uiState                           by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()
    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.store_category,
        description= R.string.common_long_click_to_modify,
        list = viewModel.getCategoryItems(context, categories),
        trailIcon = {
            Icon( Icons.Default.Add, contentDescription =null, modifier = Modifier.clickable {
                onCreateCategoryClicked()
            } )
        },
        onSelectItem = {
            onCategorySelected(it)
        },
        onLongPress = {
            onCategoryLongPressed(it.id)
        }


    )
}

@Composable
fun AddCategoryDialog(viewModel: StoresViewModel, onDismiss:()->Unit){

    Dialog(onDismissRequest = onDismiss) {

        DialogComponent.AddCategoryDialog(
            onClickPositiveBtn = {ar,en->
                viewModel.addCategory(CategoryModel(
                    valueEn = en,
                    valueAr = ar,
                ))

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}