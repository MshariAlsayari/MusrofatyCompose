package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.R


@Composable
fun CategoriesBottomSheet(
    categories: List<SelectedItemModel> = emptyList(),
    onCategorySelected: (SelectedItemModel) -> Unit,
    onCategoryLongPressed: (SelectedItemModel) -> Unit,
    onCreateCategoryClicked: () -> Unit,
) {

    BottomSheetComponent.SelectedItemListBottomSheetComponent(
        title = R.string.store_category,
        description = R.string.common_long_click_to_modify,
        list = categories,
        canUnSelect = true,
        trailIcon = {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.clickable {
                onCreateCategoryClicked()
            })
        },
        onSelectItem = {
            onCategorySelected(it)
        },
        onLongPress = {
            onCategoryLongPressed(it)
        }
    )
}