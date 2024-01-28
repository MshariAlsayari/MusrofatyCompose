package com.msharialsayari.musrofaty.ui_component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.utils.DateUtils


@Composable
fun TimePeriodsBottomSheet(
    @StringRes title: Int? = null,
    selectedItem: SelectedItemModel? = null,
    ignoreFilterOption: DateUtils.FilterOption? = null,
    startDate: Long = 0,
    endDate: Long = 0,
    onFilterSelected: (SelectedItemModel) -> Unit){

    BottomSheetComponent.TimeOptionsBottomSheet(
        title=title,
        selectedItem = selectedItem,
        ignoreFilterOption=ignoreFilterOption,
        startDate = startDate,
        endDate = endDate
    ) {
        onFilterSelected(it)
    }

}