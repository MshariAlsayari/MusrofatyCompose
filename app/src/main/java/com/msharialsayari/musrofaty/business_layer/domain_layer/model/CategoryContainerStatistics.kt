package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.icu.text.CaseMap.Title
import android.os.Parcelable
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategoryContainerStatistics(
    val key: String,
    var data: MutableMap<Int, CategoryStatistics> = mutableMapOf(),
    var total: Double = 0.0,
) : Parcelable{
    fun getTitle() = "$key\n$total"
}

@Parcelize
data class CategoryStatistics(
    val categoryModel: CategoryModel,
    val key: String,
    var color:Int,
    var payPercent: Double = 0.0,
    var total: Double = 0.0,

    val sms: MutableList<SmsModel> = mutableListOf()

) : Parcelable

fun CategoryStatistics.toCategoryStatisticsModel(context: Context) =
    CategoryStatisticsModel(
        color = color,
        category = CategoryModel.getDisplayName(context, categoryModel),
        totalAmount = total,
        percent = payPercent,
        currency = key,
        details = sms.map { it.toCategoryDetailsStatisticsModel() })