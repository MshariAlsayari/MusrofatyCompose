package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.github.mikephil.charting.utils.ColorTemplate
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoriesChartModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryContainerStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class StatisticsRepo @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
) {
    suspend fun getFinancialStatistics(list: List<SmsEntity>): Map<String, FinancialStatistics> {
        val map = mutableMapOf<String, FinancialStatistics>()
        list.forEach {
            var smsModel = it.toSmsModel()
            smsModel = smsRepo.fillSmsModel(smsModel)
            if (smsModel.smsType != SmsType.NOTHING) {
                if (SmsUtils.isSACurrency(smsModel.currency) || smsModel.currency.isEmpty()) {
                    smsModel.currency = Constants.CURRENCY_1
                }
                if (smsModel.amount > 0) {
                    val financialSummary =
                        map.getOrDefault(smsModel.currency, FinancialStatistics(smsModel.currency))
                    map[smsModel.currency] = calculateFinancialSummary(
                        financialSummary,
                        smsModel.amount,
                        smsModel.smsType
                    )
                }
            }
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
        val expensesSmsList = list.filter { it.smsType == SmsType.EXPENSES && it.amount > 0 }

        expensesSmsList.map {
            amountTotal += it.amount
            val categoryId = it.storeAndCategoryModel?.category?.id ?: 0
            val categoryModel =
                if (categoryId == 0 || it.storeAndCategoryModel?.category == null) CategoryModel.getNoSelectedCategory() else it.storeAndCategoryModel?.category!!
            val categorySummary = returnedValue.data.getOrDefault(
                categoryId,
                CategoryStatistics(
                    categoryModel = categoryModel,
                    key=key,
                    color = colors[Random.nextInt(0, colors.size)],
                )
            )
            categorySummary.total += it.amount
            categorySummary.sms.add(it)
            returnedValue.data[categoryId] = categorySummary
        }

        returnedValue.data.map {
            it.value.payPercent = MathUtils.calculatePercentage(it.value.total, amountTotal)
            it.value.color = if (it.key < returnedValue.data.size) colors[it.key] else colors[Random.nextInt(0, colors.size)]
        }

        returnedValue.data = returnedValue.data.toList().sortedByDescending { (_, value) -> value.payPercent }.toMap().toMutableMap()
        return returnedValue

    }

    fun getCategoriesStatisticsChartData(key: String, list: List<SmsModel>): CategoriesChartModel {

        //declaring variables
        val model = CategoriesChartModel(key = key)
        val data = mutableMapOf<LocalDate, Float>()
        val entries = mutableListOf<FloatEntry>()
        var total = 0.0f
        var average = 0.0f

        //get expenses sms
        val expensesSmsList = list.filter { it.smsType == SmsType.EXPENSES }

        //get map of sms Map<LocalData,List<SmsModel>>
        val groupedByLocalDate = expensesSmsList.groupBy { item ->
            DateUtils.toLocalDate(item.timestamp)
        }

        groupedByLocalDate.forEach { (localDate, items) ->
            val key = localDate
            var subTotal = 0.0f
            items.map {
                subTotal += it.amount.toFloat()
            }
            data[key] = subTotal
            entries.add(FloatEntry(x = key.toEpochDay().toFloat(), y = subTotal))
            total += subTotal
        }
        average = if (expensesSmsList.isEmpty()) {
            0f
        } else {
            total / expensesSmsList.size
        }

        val xValuesToDates = data.keys.associateBy { it.toEpochDay().toFloat() }
        val dateTimeFormatter: DateTimeFormatter = if (data.keys.size <= 6) {
            DateTimeFormatter.ofPattern("d MMM")
        } else {
            DateTimeFormatter.ofPattern("d")
        }

        val horizontalAxisValueFormatter =
            AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValue ->
                (xValuesToDates[value] ?: LocalDate.ofEpochDay(value.toLong())).format(
                    dateTimeFormatter
                )
            }

        model.data = data
        model.entries = entries
        model.total = total
        model.average = average
        model.xValueFormatter = horizontalAxisValueFormatter
        model.yTitle = key
        return model
    }


    private fun calculateFinancialSummary(
        financialSummary: FinancialStatistics,
        amount: Double,
        smsType: SmsType
    ): FinancialStatistics {
        when (smsType) {
            SmsType.INCOME -> financialSummary.income += amount
            SmsType.EXPENSES -> financialSummary.expenses += amount
            else -> {}
        }
        return financialSummary
    }
}