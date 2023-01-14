package com.msharialsayari.musrofaty.business_layer.data_layer.database.store_firebase_database

import android.os.Parcelable
import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.category_database.CategoryEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.CategoryModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.StoreModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "StoreFirebaseEntity")
data class StoreFirebaseEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "category_id")
    var category_id: Int = 0,
) : Parcelable