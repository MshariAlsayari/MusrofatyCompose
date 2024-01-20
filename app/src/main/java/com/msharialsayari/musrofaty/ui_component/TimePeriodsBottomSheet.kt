package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable


@Composable
fun TimePeriodsBottomSheet(
    @StringRes title: Int? = null,
    selectedItem: SelectedItemModel? = null,
    startDate: Long = 0,
    endDate: Long = 0,
    onFilterSelected: (SelectedItemModel) -> Unit){

    BottomSheetComponent.TimeOptionsBottomSheet(
        title=title,
        selectedItem = selectedItem,
        startDate = startDate,
        endDate = endDate
    ) {
        onFilterSelected(it)
    }

}