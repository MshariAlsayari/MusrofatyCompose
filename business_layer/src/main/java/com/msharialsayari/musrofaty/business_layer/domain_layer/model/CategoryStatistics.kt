package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategoryStatistics(
    var categoryModel: CategoryModel,
    var visitingPercent: Double = 0.0,
    var payPercent: Double = 0.0,
    var occurrence: Int = 0,
    var total: Double = 0.0,
    var currency: String = "",
    val sms:MutableList<SmsModel> = mutableListOf()

) : Parcelable

fun CategoryStatistics.toCategoryStatisticsModel(context: Context, color: Int)= CategoryStatisticsModel(color =color , category = CategoryModel.getDisplayName(context,categoryModel), totalAmount = total, percent = payPercent, currency = currency, details = sms.map { it.toCategoryDetailsStatisticsModel() })