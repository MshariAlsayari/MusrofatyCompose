package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.Utils
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.ui_component.AddCategoryDialog
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.CategoriesBottomSheet
import com.msharialsayari.musrofaty.ui_component.EmptyComponent
import com.msharialsayari.musrofaty.ui_component.ProgressBar
import com.msharialsayari.musrofaty.ui_component.SmsActionType
import com.msharialsayari.musrofaty.ui_component.SmsComponent
import com.msharialsayari.musrofaty.ui_component.wrapSendersToSenderComponentModel
import kotlinx.coroutines.launch

@Composable
fun CategorySmsListScreen() {
    val viewModel: CategorySmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val category = uiState.category


    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                titleString = CategoryModel.getDisplayName(context, category),
                onArrowBackClicked = { viewModel.navigateUp() }
            )

        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> PageLoading(modifier = Modifier.padding(innerPadding))
            uiState.smsList.isNotEmpty() -> PageCompose(Modifier.padding(innerPadding), viewModel)
            else -> PageEmpty(
                Modifier.padding(
                    innerPadding
                )
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
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmptyComponent.EmptyTextComponent()
    }
}