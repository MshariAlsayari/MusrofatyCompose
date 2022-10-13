package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import kotlinx.coroutines.launch

@Composable
fun StoresScreen(onNavigateToCategoryScreen:(Int)->Unit) {
    val viewModel: StoresViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.isLoading -> PageLoading()
        else -> PageCompose(viewModel, onCategoryLongPressed = {
            onNavigateToCategoryScreen(it)
        })
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageCompose(viewModel: StoresViewModel,onCategoryLongPressed:(Int)->Unit) {
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
        StoresList(viewModel, onItemClicked = {
            viewModel.onStoreSelected(it)
            coroutineScope.launch {
                handleVisibilityOfBottomSheet(sheetState, true)
            }
        })
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun StoresList(viewModel: StoresViewModel, onItemClicked:(StoreWithCategory)->Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val listState  = rememberLazyListState()
    val stores = uiState.stores?.collectAsState(initial = emptyList())?.value ?: emptyList()

    LazyColumn(
        state = listState,
    ) {
        items(stores) {  item ->
            StoreAndCategoryCompose(viewModel,item, onItemClicked={
                onItemClicked(it)
            })
            DividerComponent.HorizontalDividerComponent()

        }


    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreAndCategoryCompose(viewModel: StoresViewModel, item: StoreWithCategory, onItemClicked:(StoreWithCategory)->Unit) {


    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(item)
            }
            .padding(all = dimensionResource(id = R.dimen.default_margin16)),
        text = { Text(text = item.store.storeName) },
        trailing = {
            TextComponent.ClickableText(
                text =  viewModel.getCategoryDisplayName( item.store.categoryId, categories))
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
                    isDefault = false,
                ))

                onDismiss()

            },
            onClickNegativeBtn = onDismiss
        )

    }

}