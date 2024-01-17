package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.DateUtils.DEFAULT_DATE_PATTERN
import com.msharialsayari.musrofaty.utils.DateUtils.getToday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeDatePicker(
    modifier: Modifier = Modifier,
    startDate: Long? = null,
    endDate: Long? = null,
    showModeToggle:Boolean = false,
    onDone: (Long?, Long?) -> Unit
) {

    val context = LocalContext.current
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = if (startDate == null || startDate == 0L) DateUtils.toMilliSecond(
            getToday().toLocalDate()
        ) else startDate,
        initialSelectedEndDateMillis = if (endDate == null || endDate == 0L) DateUtils.toMilliSecond(
            getToday().toLocalDate()
        ) else endDate,
        initialDisplayedMonthMillis = if (startDate == null || startDate == 0L) DateUtils.toMilliSecond(
            getToday().toLocalDate()
        ) else startDate,
    )

    val selectedStartDate = rememberSaveable { mutableStateOf<String>("") }
    val selectedEndDate = rememberSaveable { mutableStateOf<String>("") }

    LaunchedEffect(key1 = state.selectedStartDateMillis) {
        state.selectedStartDateMillis?.let {
            selectedStartDate.value =
                DateUtils.getDateByTimestamp(it, pattern = DEFAULT_DATE_PATTERN)
                    ?: context.getString(R.string.common_start_date)
        } ?: kotlin.run { selectedStartDate.value = context.getString(R.string.common_start_date) }
    }

    LaunchedEffect(key1 = state.selectedEndDateMillis) {
        state.selectedEndDateMillis?.let {
            selectedEndDate.value = DateUtils.getDateByTimestamp(it, pattern = DEFAULT_DATE_PATTERN)
                ?: context.getString(R.string.common_end_date)
        } ?: kotlin.run { selectedEndDate.value = context.getString(R.string.common_end_date) }

    }
    DateRangePicker(
        state = state,
        modifier = modifier,
        dateFormatter = DatePickerFormatter(
            DEFAULT_DATE_PATTERN,
            DEFAULT_DATE_PATTERN,
            DEFAULT_DATE_PATTERN
        ),
        showModeToggle = showModeToggle,
        title = {

        },
        headline = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(text = selectedStartDate.value)
                Text(text = selectedEndDate.value)
                IconButton(onClick = {
                    onDone(state.selectedStartDateMillis, state.selectedEndDateMillis)
                }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)

                }


            }

        },
        colors = DatePickerDefaults.colors(
            titleContentColor = MusrofatyTheme.colors.activeColor,
            headlineContentColor = MusrofatyTheme.colors.onBackground,
            subheadContentColor = MusrofatyTheme.colors.onBackground,
            weekdayContentColor = MusrofatyTheme.colors.onBackground,
            yearContentColor = MusrofatyTheme.colors.onBackground,
            currentYearContentColor = MusrofatyTheme.colors.activeColor,
            todayDateBorderColor = MusrofatyTheme.colors.activeColor,
            todayContentColor = MusrofatyTheme.colors.onBackground,
            dayContentColor = MusrofatyTheme.colors.onBackground,
            dayInSelectionRangeContainerColor = MusrofatyTheme.colors.activeColor.copy(alpha = 0.6f),
            dayInSelectionRangeContentColor = Color.White,
            selectedDayContainerColor = MusrofatyTheme.colors.activeColor
        )

    )

}