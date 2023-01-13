package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database

import android.os.Parcelable
import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "StoreEntity")
data class StoreEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "category_id")
    var category_id: Int = 0,
) : Parcelable


@Parcelize
data class StoreWithCategory(
    @Embedded val store: StoreEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "id"
    )
    val category: CategoryEntity?=null,
):Parcelable


@Parcelize
data class StoreAndCategoryModel(
    var store: StoreModel,
    var category: CategoryModel? = null
) : Parcelable


fun StoreEntity.toStoreModel() = StoreModel(name, category_id)
