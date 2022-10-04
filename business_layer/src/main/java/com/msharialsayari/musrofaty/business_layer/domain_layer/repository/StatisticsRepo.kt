package com.msharialsayari.musrofaty.business_layer.domain_layer.repository

import android.content.Context
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.toSmsModel
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StatisticsRepo @Inject constructor(
    private val senderRepo: SenderRepo,
    @ApplicationContext val context: Context
){



     suspend fun getFinancialStatistics(list: List<SmsEntity>): Map<String, FinancialStatistics> {
        val map = mutableMapOf<String, FinancialStatistics>()
        list.forEach {
            var smsModel = it.toSmsModel()
            smsModel = senderRepo.fillSmsModel(smsModel)
            if (smsModel.smsType != SmsType.NOTHING) {


                if (SmsUtils.isSACurrency(smsModel.currency) || smsModel.currency.isEmpty() ) {
                    smsModel.currency = Constants.CURRENCY_1
                }


                if (smsModel.amount > 0) {
                    val financialSummary = map.getOrDefault(smsModel.currency, FinancialStatistics(smsModel.currency) )
                    map[smsModel.currency] = calculateSummary(financialSummary, smsModel.amount, smsModel.smsType)
                }


            }
        }

         return map

    }



    private fun calculateSummary(
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