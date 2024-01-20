package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoresCategoriesModel (
    val categoryId: Int,
    var keysSearch: List<String> = emptyList(),
) : Parcelable