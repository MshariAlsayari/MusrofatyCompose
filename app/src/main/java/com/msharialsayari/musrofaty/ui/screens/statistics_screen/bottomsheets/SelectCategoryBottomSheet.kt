package com.msharialsayari.musrofaty.ui.screens.statistics_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsViewModel
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.CategoriesBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectCategoryBottomSheet(viewModel: StatisticsViewModel, sheetState: ModalBottomSheetState) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val categories = viewModel.observingCategories().collectAsState(initial = emptyList())

    CategoriesBottomSheet(
        categories = viewModel.getCategoryItems(context, categories.value),
        onCategorySelected = {
            viewModel.updateSelectedCategory(it)
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
        },
        onCreateCategoryClicked = {
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
        },
        onCategoryLongPressed = { category ->
            coroutineScope.launch {
                BottomSheetComponent.handleVisibilityOfBottomSheet(sheetState, false)
            }
        }
    )

}