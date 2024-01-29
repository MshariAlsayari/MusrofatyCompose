package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.github.mikephil.charting.utils.ColorTemplate
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.ChartEntry
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isExpenses
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isIncome
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class StatisticsRepo @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getFinancialStatistics(list: List<SmsModel>): Map<String, FinancialStatistics> {
        val map = mutableMapOf<String, FinancialStatistics>()
        val expensesPURCHASESSmsList = list.filter { !it.isDeleted && (it.smsType.isExpenses() || it.smsType.isIncome()) && it.amount > 0 }
        expensesPURCHASESSmsList.forEach { smsModel->
                val financialSummary = map.getOrDefault(smsModel.currency, FinancialStatistics(smsModel.currency))
                map[smsModel.currency] = calculateFinancialSummary(
                    financialSummary,
                    smsModel.amount,
                    smsModel.smsType
                )
        }
        return map
    }

    fun getCategorySummaryOfSmsList(
        key: String,
        list: List<SmsModel>
    ): CategoryContainerStatistics {
        val returnedValue = CategoryContainerStatistics(key)
        val colors: ArrayList<Int> = ArrayList()
        colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toList())
        colors.addAll(ColorTemplate.JOYFUL_COLORS.toList())
        colors.addAll(ColorTemplate.COLORFUL_COLORS.toList())
        colors.addAll(ColorTemplate.LIBERTY_COLORS.toList())
        colors.addAll(ColorTemplate.PASTEL_COLORS.toList())
        colors.add(ColorTemplate.getHoloBlue())
        var amountTotal = 0.0

        //get expenses sms
        val filteredList = list.filter { !it.isDeleted && it.smsType.isExpenses() && it.amount > 0 }

        filteredList.map {
            amountTotal += it.amount
            val category = when (it.smsType) {
                SmsType.PAY_BILLS ->CategoryModel.getCategory(id = -2, valueAr = "سداد فاتورة", valueEn = "Pay Bill")
                SmsType.OUTGOING_TRANSFER -> CategoryModel.getCategory(id =-3,valueAr = "حوالات صادرة", valueEn = "Outgoing transfers")
                else -> it.storeAndCategoryModel?.category ?: CategoryModel.getCategory(-1)
            }



            val storeModel = when (it.smsType) {
                SmsType.EXPENSES_PURCHASES -> it.storeAndCategoryModel?.store!!
                else -> StoreModel(name = "" ,categoryId= category.id)
            }



            val storeAndCategory = StoreAndCategoryModel(store = storeModel ,category = category )
            val categorySummary = returnedValue.data.getOrDefault(
                category.id,
                CategoryStatistics(
                    storeAndCategory = storeAndCategory,
                    key=key,
                    color = colors[Random.nextInt(0, colors.size)],
                )
            )
            categorySummary.total += it.amount
            categorySummary.sms.add(it)
            returnedValue.data[category.id] = categorySummary
        }

        returnedValue.data.map {
            it.value.payPercent = MathUtils.calculatePercentage(it.value.total, amountTotal)
            it.value.color = if (it.key > 0 && it.key < returnedValue.data.size) colors[it.key] else colors[Random.nextInt(0, colors.size)]
        }
        returnedValue.total = amountTotal
        returnedValue.data = returnedValue.data.toList().sortedByDescending { (_, value) -> value.payPercent }.toMap().toMutableMap()
        return returnedValue

    }

    fun getCategoriesStatisticsChartData(
        key: String,
        filterOption: DateUtils.FilterOption,
        list: List<SmsModel>
    ): CategoriesChartModel {

        //declaring variables
        val model = CategoriesChartModel(key = key)
        val chartEntryList = mutableListOf<ChartEntry>()
        val data = mutableMapOf<LocalDate, Float>()
        val entries = mutableListOf<FloatEntry>()
        var total = 0.0f
        var average = 0.0f
        val filteredList = list.filter { !it.isDeleted && it.smsType.isExpenses() && it.amount > 0}
        if(filteredList.isEmpty())
            return model


        val sortedList = filteredList.sortedByDescending { it.timestamp }
        val endDate =  sortedList.first().timestamp
        val startDate = sortedList.last().timestamp
        val finalFilterOption = if(filterOption == DateUtils.FilterOption.RANGE || filterOption == DateUtils.FilterOption.YEAR){
            val start = DateUtils.ofMilliSecond(startDate)
            val end = DateUtils.ofMilliSecond(endDate)
            val monthsBetween = ChronoUnit.MONTHS.between(start, end).toInt()
            val weeksBetween = ChronoUnit.WEEKS.between(start, end).toInt()

            if(weeksBetween <= 7){
                DateUtils.FilterOption.MONTH
            }else if(monthsBetween <=12){
                DateUtils.FilterOption.YEAR
            }else{
                DateUtils.FilterOption.WEEK
            }
        }else {
            filterOption
        }


        val groupedDate = sortedList.groupBy {
            val smsDate = DateUtils.toLocalDate(it.timestamp)
            when (finalFilterOption) {
                DateUtils.FilterOption.MONTH -> {
                    val weekOfMonth = smsDate.get(WeekFields.of(Locale.ENGLISH).weekOfMonth())
                    val keyDate = DateUtils.getDateByWeekOfMonth(weekOfMonth.toLong(), smsDate)
                    keyDate
                }
                DateUtils.FilterOption.YEAR -> {
                    smsDate.withDayOfMonth(1)
                }
                else -> {
                    smsDate
                }
            }

        }


        groupedDate.entries.map {entry->
            var amount = 0f
            entry.value.map { amount += it.amount.toFloat() }
            val chartEntry = ChartEntry(amount=amount, date = entry.key )
            chartEntryList.add(chartEntry)
        }


        chartEntryList.mapIndexed { index,entry ->
            data[entry.date] = entry.amount
            entries.add(FloatEntry(x = entry.date.toEpochDay().toFloat(), y = entry.amount))
            total += entry.amount
        }
        average = if (chartEntryList.isEmpty()) {
            0f
        } else {
            total / chartEntryList.size
        }



        val xValuesToDates = chartEntryList.associateBy { it.date.toEpochDay().toFloat() }
        val dateTimeFormatter: DateTimeFormatter =
           if (entries.size <= 6) {
                DateTimeFormatter.ofPattern("d MMM")
            } else {
                DateTimeFormatter.ofPattern("d")
            }

        val horizontalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValue ->
                when (finalFilterOption) {
                    DateUtils.FilterOption.MONTH -> {
                       val weekFields = WeekFields.of(Locale.getDefault())
                       val numberOfWeek =  xValuesToDates[value]?.date?.get(weekFields.weekOfWeekBasedYear())
                        "${context.getString(R.string.common_week)} $numberOfWeek"
                    }

                    DateUtils.FilterOption.YEAR -> {
                        xValuesToDates[value]?.date?.month?.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())?: ""
                    }
                    else -> {
                        (xValuesToDates[value])?.date?.format(dateTimeFormatter) ?:""
                    }
                }


            }

        model.data = data
        model.entries = entries
        model.total = total
        model.average = average
        model.xValueFormatter = horizontalAxisValueFormatter
        model.yTitle = key.ifEmpty { context.getString(R.string.no_currency) }
        model.xTitle =   when (finalFilterOption) {
            DateUtils.FilterOption.MONTH -> {
                context.getString(R.string.common_weeks)
            }
            DateUtils.FilterOption.YEAR -> {
                context.getString(R.string.common_months)
            }
            else -> {
                context.getString(R.string.common_days)
            }
        }
        return model
    }


    private fun calculateFinancialSummary(
        financialSummary: FinancialStatistics,
        amount: Double,
        smsType: SmsType
    ): FinancialStatistics {
        when (smsType) {
            SmsType.INCOME -> financialSummary.income += amount
            SmsType.EXPENSES_PURCHASES -> financialSummary.expenses += amount
            else -> {}
        }
        return financialSummary
    }
}