package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun RangeDatePickerCompose(
    modifier: Modifier = Modifier,
    startDate: Long? = null,
    endDate: Long? = null,
    onDone: (Long, Long) -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        RangeDatePicker(
            modifier = modifier,
            startDate = startDate,
            endDate = endDate
        ) { selectedStartDate, selectedEndDate ->
            if (selectedStartDate != null && selectedEndDate != null) {
                onDone(selectedStartDate, selectedEndDate)
            }
        }
    }


}