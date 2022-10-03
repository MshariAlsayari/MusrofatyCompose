package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "FilterAdvancedEntity")
class FilterAdvancedEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "words")
    var words: String = "",
    @ColumnInfo(name = "senderId")
    var senderId: Int,

) : Parcelable


fun FilterAdvancedEntity.toFilterAdvancedModel() = FilterAdvancedModel(
    id = id,
    title = title,
    words = words,
    senderId = senderId,)