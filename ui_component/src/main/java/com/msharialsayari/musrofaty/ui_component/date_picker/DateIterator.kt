package com.msharialsayari.musrofaty.ui_component.date_picker

import java.time.LocalDate

internal class DateIterator(
    startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val step: DateRangeStep
) : Iterator<LocalDate> {

    private var currentDate = startDate

    override fun hasNext(): Boolean = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        val next = currentDate
        currentDate = getNextStep()
        return next
    }

    private fun getNextStep(): LocalDate {
        return when (step) {
            is DateRangeStep.Day -> currentDate.plusDays(step.value.toLong())
            is DateRangeStep.Month -> currentDate.plusMonths(step.value.toLong())
            is DateRangeStep.Year -> currentDate.plusYears(step.value.toLong())
        }
    }
}

internal class DateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val step: DateRangeStep = DateRangeStep.Day()
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive, step)

    infix fun step(step: DateRangeStep) = DateRange(start, endInclusive, step)
}

internal sealed class DateRangeStep(val value: Int) {
    class Day(value: Int = 1) : DateRangeStep(value)
    class Month(value: Int = 1) : DateRangeStep(value)
    class Year(value: Int = 1) : DateRangeStep(value)
}

internal operator fun LocalDate.rangeTo(other: LocalDate) = DateRange(this, other)