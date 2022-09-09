package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class StoreSummary(
    var storeName: String = "",
    var categoryModel: CategoryModel? = null,
    var visitingPercent: Double = 0.0,
    var payPercent: Double = 0.0,
    var occurrence: Int = 0,
    var total: Double = 0.0,
    var currency: String = "",
) : Parcelable
