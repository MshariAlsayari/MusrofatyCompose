package com.msharialsayari.musrofaty.utils.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class FinancialStatistics(
    val currency: String = "",
    var income: Double = 0.0,
    var expenses: Double = 0.0
) : Parcelable