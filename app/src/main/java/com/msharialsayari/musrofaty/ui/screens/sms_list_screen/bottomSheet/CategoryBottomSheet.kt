package com.msharialsayari.musrofaty.ui.screens.sms_list_screen.bottomSheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.msharialsayari.musrofaty.ui.screens.sms_list_screen.SmsListViewModel
import com.msharialsayari.musrofaty.ui_component.AddCategoryDialog
import com.msharialsayari.musrofaty.ui_component.CategoriesBottomSheet


@Composable
fun CategoryBottomSheet(viewModel: SmsListViewModel){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val categories = uiState.categories?.collectAsState(initial = emptyList())?.value ?: emptyList()

    if (openDialog.value) {
        AddCategoryDialog(onAdd = {
            viewModel.addCategory(it)
        }, onDismiss = {
            openDialog.value = false
        })
    }

    CategoriesBottomSheet(
        categories = viewModel.getCategoryItems(context, categories),
        onCategorySelected = {
            viewModel.onCategorySelected(it)
            viewModel.updateSelectedBottomSheet(null)
        },
        onCreateCategoryClicked = {
            openDialog.value = true
        },
        onCategoryLongPressed = { category ->
            viewModel.navigateToCategoryScreen(category.id)
            viewModel.updateSelectedBottomSheet(null)

        }
    )

}