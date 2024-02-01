package com.msharialsayari.musrofaty.utils.models

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.parcelize.Parcelize


@Parcelize
class FinancialStatistics(
    val currency: String = "",
    var income: Double = 0.0,
    var expenses: Double = 0.0,
    val incomeSmsList: MutableList<SmsModel> = mutableListOf(),
    val expensesSmsList: MutableList<SmsModel> = mutableListOf()
) : Parcelable