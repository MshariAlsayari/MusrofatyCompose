package com.msharialsayari.musrofaty.ui_component

import androidx.compose.runtime.Composable


@Composable
fun TimePeriodsBottomSheet(selectedItem: SelectedItemModel? = null,
                           startDate: Long = 0,
                           endDate: Long = 0,
                           onFilterSelected: (SelectedItemModel) -> Unit){

    BottomSheetComponent.TimeOptionsBottomSheet(
        selectedItem = selectedItem,
        startDate = startDate,
        endDate = endDate
    ) {
        onFilterSelected(it)
    }

}