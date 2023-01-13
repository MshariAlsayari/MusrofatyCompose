package com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database

import android.os.Parcelable
import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "CategoryEntity")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "valueAr")
    var valueAr: String? = null,
    @ColumnInfo(name = "valueEn")
    var valueEn: String? = null,
    @ColumnInfo(name = "sortOrder")
    var sortOrder: Int = 0,
) : Parcelable

data class CategoryWithStores(
    @Embedded val category: CategoryEntity?= null,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val stores: List<StoreEntity>,
)

@Parcelize
data class CategoryWithStoresModel(
    val category: CategoryModel? = null,
    val stores: List<StoreModel> = listOf(),
    var isSelected: Boolean = false
) : Parcelable


fun CategoryEntity.toCategoryModel() = CategoryModel(id, valueAr, valueEn)