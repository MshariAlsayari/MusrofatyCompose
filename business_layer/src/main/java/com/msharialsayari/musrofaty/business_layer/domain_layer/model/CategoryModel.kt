package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class CategoryModel(
    var id: Int=0,
    var valueAr: String?=null,
    var valueEn: String?=null,
    var isDefault: Boolean =false,
    var isSelected: Boolean = false,
) : Parcelable



fun CategoryModel.toCategoryEntity() = CategoryEntity(id, valueAr, valueEn, isDefault)