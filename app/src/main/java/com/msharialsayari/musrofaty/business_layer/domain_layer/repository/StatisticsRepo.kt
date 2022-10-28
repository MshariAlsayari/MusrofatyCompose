package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryStatistics
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.MathUtils
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatisticsRepo @Inject constructor(
    private val smsRepo: SmsRepo,
    @ApplicationContext val context: Context
){



     suspend fun getFinancialStatistics(list: List<SmsEntity>): Map<String, FinancialStatistics> {
        val map = mutableMapOf<String, FinancialStatistics>()
        list.forEach {
            var smsModel = it.toSmsModel()
            smsModel = smsRepo.fillSmsModel(smsModel)
            if (smsModel.smsType != SmsType.NOTHING) {


                if (SmsUtils.isSACurrency(smsModel.currency) || smsModel.currency.isEmpty() ) {
                    smsModel.currency = Constants.CURRENCY_1
                }


                if (smsModel.amount > 0) {
                    val financialSummary = map.getOrDefault(smsModel.currency, FinancialStatistics(smsModel.currency) )
                    map[smsModel.currency] = calculateFinancialSummary(financialSummary, smsModel.amount, smsModel.smsType)
                }


            }
        }

         return map

    }

    suspend fun getCategorySummaryOfSmsList(list: List<SmsEntity>): Map<Int, CategoryStatistics> {
        val map = mutableMapOf<Int, CategoryStatistics>()
        var amountTotal = 0.0
        var visitTotal = 0

        list.map {
            var smsModel = it.toSmsModel()
            smsModel = smsRepo.fillSmsModel(smsModel)
            return@map smsModel
        }.filter {
            it.smsType ==  SmsType.EXPENSES && it.storeAndCategoryModel?.category != null
        }.map {
            val categoryId =  it.storeAndCategoryModel?.category?.id
            val categorySummary = map.getOrPut(categoryId!!) { CategoryStatistics(it.storeAndCategoryModel?.category!!) }
            amountTotal += it.amount
            visitTotal++
            categorySummary.total += it.amount
            categorySummary.occurrence++
            categorySummary.currency = it.currency
            categorySummary.sms.add(it)
            map[categoryId] = categorySummary


        }



        map.forEach {
            it.value.visitingPercent = MathUtils.calculatePercentage(it.value.occurrence.toDouble(), visitTotal.toDouble())
            it.value.payPercent = MathUtils.calculatePercentage(it.value.total, amountTotal)
        }


        return map.toList().sortedByDescending { (_, value) -> value.payPercent }.toMap()

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