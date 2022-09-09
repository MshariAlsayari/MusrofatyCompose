package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryWithStoreModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategorySummary(
    var categoryModel: CategoryWithStoreModel,
    var visitingPercent: Double = 0.0,
    var payPercent: Double = 0.0,
    var occurrence: Int = 0,
    var total: Double = 0.0,
    var currency: String = "",

    ) : Parcelable