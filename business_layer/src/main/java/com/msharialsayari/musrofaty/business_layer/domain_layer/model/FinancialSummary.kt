package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FinancialSummary(
    val currency: String = "",
    var income: Double = 0.0,
    var expenses: Double = 0.0
) : Parcelable