package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.base.LifecycleCallbacks
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.AddCategoryDialog
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.CategoriesBottomSheet
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import kotlinx.coroutines.launch

@Composable
fun CategorySmsListScreen() {
    val viewModel: CategorySmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val category = uiState.category
    val smsList = uiState.smsList

    LifecycleCallbacks(
        onStart = {
            viewModel.startObserveChat()
        },
        onStop = {
            viewModel.stopObserveChat()
        },
    )


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                titleString = CategoryModel.getDisplayName(context, category),
                onArrowBackClicked = { viewModel.navigateUp() }
            )

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when {
                smsList.isNotEmpty() -> PageCompose(Modifier, viewModel)
                else -> EmptyComponent.EmptyTextComponent()
            }

        }

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PageCompose(modifier: Modifier = Modifier, viewModel: CategorySmsListViewModel) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val sheetState = rememberBottomSheetScaffoldState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    BackHandler(sheetState.bottomSheetState.isExpanded) {
        coroutineScope.launch { BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false) }
    }

    if (openDialog.value) {
        AddCategoryDialog(onAdd = {
            viewModel.addCategory(it)
        }, onDismiss = {
            openDialog.value = false
        })
    }

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = sheetState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CategoriesBottomSheet(
                categories = viewModel.getCategoryItems(context, categories),
                onCategorySelected = {
                    viewModel.onCategorySelected(it)
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },
                onCreateCategoryClicked = {
                    openDialog.value = true
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                    }
                },
                onCategoryLongPressed = { category ->
                    viewModel.navigateToCategoryScreen(category.id)
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
                    }
                }
            )

        }) {

           CategorySmsListContent(
               modifier = Modifier.fillMaxSize(),
               viewModel = viewModel,
               sheetState=sheetState)
    }



}

@Composable
fun PageEmpty(modifier: Modifier = Modifier) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyComponent.EmptyTextComponent()
    }
}