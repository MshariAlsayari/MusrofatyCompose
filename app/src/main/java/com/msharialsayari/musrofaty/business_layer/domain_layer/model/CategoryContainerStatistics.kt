package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.ui_component.CategoryStatisticsModel
import com.msharialsayari.musrofaty.utils.StringsUtils
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategoryContainerStatistics(
    val key: String,
    var data: MutableMap<Int, CategoryStatistics> = mutableMapOf(),
    var total: Double = 0.0,
) : Parcelable{
    fun getTitle() :String{
        val prettyTotal = StringsUtils.prettyCount(total.toLong())
        val formattedNumber = StringsUtils.formatArabicDigits(prettyTotal)
        return "$key\n$formattedNumber"
    }
}

@Parcelize
data class CategoryStatistics(
    val storeAndCategory: StoreAndCategoryModel,
    val key: String,
    var color:Int,
    var payPercent: Double = 0.0,
    var total: Double = 0.0,
    val sms: MutableList<SmsModel> = mutableListOf()

) : Parcelable

fun CategoryStatistics.toCategoryStatisticsModel(context: Context) =
    CategoryStatisticsModel(
        color = color,
        storeAndCategoryModel = storeAndCategory ,
        category = CategoryModel.getDisplayName(context, storeAndCategory.category),
        totalAmount = total,
        percent = payPercent,
        currency = key,
        smsList = sms
    )