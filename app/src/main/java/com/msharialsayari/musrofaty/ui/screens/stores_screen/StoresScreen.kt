package com.msharialsayari.musrofaty.ui.screens.stores_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreWithCategory
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.*
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent.handleVisibilityOfBottomSheet
import com.msharialsayari.musrofaty.utils.notEmpty
import kotlinx.coroutines.launch

@Composable
fun StoresScreen() {
    val viewModel: StoresViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    when {
        uiState.isLoading -> PageLoading()
        else -> PageCompose(viewModel)
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
fun PageCompose(viewModel: StoresViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope                    = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )


    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value){
       AddCategoryDialog(
            viewModel,
            onDismiss = {
                openDialog.value = false
            })
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CategoryBottomSheet(
                viewModel =viewModel,
                onCategorySelected = {
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
                }
            )

        }) {
        StoresList(viewModel, onItemClicked = {
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
    val stores = uiState.stores?.collectAsState(initial = emptyList())?.value ?: emptyList()

    VerticalEasyList(
        list = stores,
        view = { StoreAndCategoryCompose(it) },
        onItemClicked = { item, position ->
            onItemClicked(item)
        },
        dividerView = { DividerComponent.HorizontalDividerComponent() }
    )

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoreAndCategoryCompose(item: StoreWithCategory) {
    val context = LocalContext.current
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.default_margin16)),
        text = { Text(text = item.store.storeName) },
        trailing = {
            TextComponent.ClickableText(
                text = if (CategoryModel.getDisplayName(context, item.category)
                        .notEmpty()
                ) CategoryModel.getDisplayName(
                    context,
                    item.category
                ) else context.getString(R.string.common_no_category),

                )
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