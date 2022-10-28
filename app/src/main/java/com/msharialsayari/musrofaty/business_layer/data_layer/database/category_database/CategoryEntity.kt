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
    @ColumnInfo(name = "value_ar")
    var valueAr: String? = null,
    @ColumnInfo(name = "value_en")
    var valueEn: String? = null,
    @ColumnInfo(name = "isDefault")
    var isDefault: Boolean = false,
) : Parcelable

data class CategoryWithStore(
    @Embedded val category: CategoryEntity?= null,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val stores: List<StoreEntity>,
)

@Parcelize
data class CategoryWithStoreModel(
    val category: CategoryModel? = null,
    val stores: List<StoreModel> = listOf(),
    var isSelected: Boolean = false
) : Parcelable


fun CategoryEntity.toCategoryModel() = CategoryModel(id, valueAr, valueEn, isDefault)