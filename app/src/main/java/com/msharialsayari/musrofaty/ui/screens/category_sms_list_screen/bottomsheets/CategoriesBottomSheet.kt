package com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.bottomsheets

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.ui.screens.category_sms_list_screen.CategorySmsListViewModel
import com.msharialsayari.musrofaty.ui.screens.sender_sms_list_screen.SenderSmsListViewModel
import com.msharialsayari.musrofaty.ui_component.AddCategoryDialog
import com.msharialsayari.musrofaty.ui_component.BottomSheetComponent
import com.msharialsayari.musrofaty.ui_component.CategoriesBottomSheet
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoriesBottomSheet(viewModel: CategorySmsListViewModel,
                          sheetState: ModalBottomSheetState) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val categories = uiState.categories?.collectAsState(initial = emptyList())
    val openDialog = rememberSaveable { mutableStateOf(false) }

    if (openDialog.value) {
        AddCategoryDialog(onAdd = {
            viewModel.addCategory(it)
        }, onDismiss = {
            openDialog.value = false
        })
    }

    CategoriesBottomSheet(
        categories = viewModel.getCategoryItems(context, categories?.value ?: emptyList()),
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

}