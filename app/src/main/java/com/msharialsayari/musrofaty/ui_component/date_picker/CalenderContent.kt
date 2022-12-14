package com.msharialsayari.musrofaty.ui_component.date_picker

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState





import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun CalendarContent(
    startDate: LocalDate,
    minDate: LocalDate,
    maxDate: LocalDate,
    title:String="",
    onSelected: (LocalDate) -> Unit,
) {


    val dateRange = getDateRange(minDate, maxDate)
    val dateRangeByYear = dateRange.step(DateRangeStep.Year(1))
    val totalPageCount = dateRange.count()
    val initialPage = getStartPage(startDate, dateRange, totalPageCount)

    val isPickingYear = remember { mutableStateOf(false) }

    // for display only, used in CalendarMonthYearSelector
    val currentPagerDate = remember { mutableStateOf(startDate.withDayOfMonth(1)) }

    val selectedDate = remember { mutableStateOf(startDate) }

    val pagerState = rememberPagerState(initialPage)
    val coroutineScope = rememberCoroutineScope()
    val gridState = with(dateRangeByYear.indexOfFirst { it.year == selectedDate.value.year }) {
        rememberLazyGridState(initialFirstVisibleItemIndex = this)
    }

    val setSelectedDate: (LocalDate) -> Unit = {
        onSelected(it)
        selectedDate.value = it
    }

    if (!LocalInspectionMode.current) {
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                val currentDate = getDateFromCurrentPage(page, dateRange)
                currentPagerDate.value = currentDate
            }
        }
    }

    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CalendarTopBar(selectedDate.value, title)

        CalendarMonthYearSelector(
            currentPagerDate.value,
            onChipClicked = { isPickingYear.value = !isPickingYear.value },
            onNextMonth = {
                coroutineScope.launch {
                    try {
                        val newPage = pagerState.currentPage + 1
                        pagerState.animateScrollToPage(newPage)
                    } catch (e: Exception) {
                        // avoid IndexOutOfBounds and animation crashes
                    }
                }
            },
            onPreviousMonth = {
                coroutineScope.launch {
                    try {
                        val newPage = pagerState.currentPage - 1
                        pagerState.animateScrollToPage(newPage)
                    } catch (e: Exception) {
                        // avoid IndexOutOfBounds and animation crashes
                    }
                }
            }
        )

        if (!isPickingYear.value) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                DayOfWeek.values().forEach {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = it.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalPager(
                count = totalPageCount,
                state = pagerState
            ) { page ->
                val currentDate = getDateFromCurrentPage(page, dateRange)
                currentDate?.let {
                    // grid
                    CalendarGrid(
                        it.withDayOfMonth(1),
                        dateRange,
                        selectedDate.value,
                        setSelectedDate,
                        true
                    )
                }
            }

        } else {

            CalendarYearGrid(
                gridState = gridState,
                dateRangeByYear = dateRangeByYear,
                selectedYear = selectedDate.value.year,
                currentYear = startDate.year,
                onYearSelected = { year ->
                    coroutineScope.launch {
                        val newPage = dateRange.indexOfFirst {
                            it.year == year && it.month == selectedDate.value.month
                        }
                        pagerState.scrollToPage(newPage)
                    }
                    currentPagerDate.value = currentPagerDate.value.withYear(year)
                    isPickingYear.value = false
                }
            )

        }
    }
}

private fun getStartPage(
    startDate: LocalDate,
    dateRange: DateRange,
    pageCount: Int
): Int {
    if (startDate <= dateRange.start) {
        return 0
    }
    if (startDate >= dateRange.endInclusive) {
        return pageCount
    }
    val indexOfRange = dateRange.indexOfFirst {
        it.year == startDate.year && it.monthValue == startDate.monthValue
    }
    return if (indexOfRange != -1) indexOfRange else pageCount / 2
}

private fun getDateRange(min: LocalDate, max: LocalDate): DateRange {
    val lowerBound = with(min) {
        val year = with(LocalDate.now().minusYears(100).year) {
            100.0 * (floor(abs(this / 100.0)))
        }
        coerceAtLeast(
            LocalDate.now().withYear(year.toInt()).withDayOfYear(1)
        )
    }
    val upperBound = with(max) {
        val year = with(LocalDate.now().year) {
            100.0 * (ceil(abs(this / 100.0)))
        }
        coerceAtMost(LocalDate.now().withYear(year.toInt())).apply {
            withDayOfYear(this.lengthOfYear())
        }
    }
    return lowerBound.rangeTo(upperBound) step DateRangeStep.Month()
}

private fun getDateFromCurrentPage(
    currentPage: Int,
    dateRange: DateRange,
): LocalDate? {
    return try {
        dateRange.elementAt(currentPage)
    } catch (e: Exception) {
        null
    }
}


