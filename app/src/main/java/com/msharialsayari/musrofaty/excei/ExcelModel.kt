package com.msharialsayari.musrofaty.excei

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FinancialSummary
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isExpenses
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isIncome
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExcelModel(
    var smsList:  List<SmsModel> = listOf(),
) : Parcelable{


    fun getStatistics(list:  List<SmsModel>): Map<String, FinancialSummary>{
        val map = mutableMapOf<String, FinancialSummary>()
        val grouped = list.groupBy { it.getExcelCurrency() }

        grouped.map {
            var expensesTotal = 0.0
            var incomesTotal = 0.0
            val finModel = FinancialSummary(currency = it.key)
            it.value.map {sms->
                if(sms.smsType.isExpenses()){
                    expensesTotal+= sms.amount
                }else if (sms.smsType.isIncome()){
                    incomesTotal+= sms.amount
                }
            }

            finModel.expenses = expensesTotal
            finModel.income = incomesTotal
            map.put(finModel.currency,finModel)
        }

        return map
    }
}