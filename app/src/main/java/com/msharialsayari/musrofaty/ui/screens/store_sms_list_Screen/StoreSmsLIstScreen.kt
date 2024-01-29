package com.msharialsayari.musrofaty.ui.screens.store_sms_list_Screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
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
fun StoreSmsListScreen(){

    val viewModel: StoreSmsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsList
    val isLoading = uiState.isLoading

    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = viewModel.storeName,
                onArrowBackClicked = {viewModel.navigateUp()}
            )

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            when{
                isLoading               -> PageLoading(Modifier)
                smsList.isNotEmpty()    -> PageCompose(Modifier, viewModel)
                else                    -> PageEmpty(Modifier)
            }

        }



    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PageCompose(modifier: Modifier=Modifier, viewModel: StoreSmsListViewModel){
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

        StoreListContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            sheetState=sheetState)
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun StoreListContent(modifier: Modifier=Modifier, viewModel: StoreSmsListViewModel, sheetState: BottomSheetScaffoldState){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val smsList = uiState.smsList
    val listState  = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        items(smsList) { item ->
            SmsComponent(
                model = wrapSendersToSenderComponentModel(item, context),
                onCategoryClicked = {
                    viewModel.onSmsCategoryClicked(item)
                    coroutineScope.launch {
                        BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, true)
                    }
                },
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



@Composable
private fun PageEmpty(modifier: Modifier=Modifier){
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyComponent.EmptyTextComponent()
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