package com.msharialsayari.musrofaty.ui_component.date_picker

import java.time.LocalDate

internal data class DateWrapper(
    val localDate: LocalDate,
    val isSelectedDay: Boolean,
    val isCurrentDay: Boolean,
    val isCurrentMonth: Boolean,
    val isInDateRange: Boolean,
    val showCurrentMonthOnly: Boolean
)
