package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "StoreEntity")
data class StoreEntity(
    @PrimaryKey
    @ColumnInfo(name = "storeName")
    var storeName: String,
    @ColumnInfo(name = "categoryId")
    var categoryId: Int = 0,
) : Parcelable


@Parcelize
data class StoreAndCategoryModel(
    var store: StoreModel,
    var category: CategoryModel? = null
) : Parcelable


fun StoreEntity.toStoreModel() = StoreModel(storeName, categoryId)
