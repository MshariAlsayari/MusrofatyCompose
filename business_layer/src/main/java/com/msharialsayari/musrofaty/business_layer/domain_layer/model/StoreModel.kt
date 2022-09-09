package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreEntity
import kotlinx.parcelize.Parcelize


@Parcelize
data class StoreModel(
    var storeName: String,
    var categoryId: Int = 0,
) : Parcelable


fun StoreModel.toStoreEntity() = StoreEntity(storeName, categoryId)