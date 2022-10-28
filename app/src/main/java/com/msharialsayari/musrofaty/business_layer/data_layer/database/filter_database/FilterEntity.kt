package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterAdvancedModel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "FilterEntity")
class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "searchWord")
    var searchWord: String = "",
    @ColumnInfo(name = "smsType")
    var smsType: String = "",
    @ColumnInfo(name = "date")
    var date: String = "",
    @ColumnInfo(name = "bankName")
    var bankName: String? = "",
    @ColumnInfo(name = "dateFrom")
    var dateFrom: String? = "",
    @ColumnInfo(name = "dateTo")
    var dateTo: String? = "",
) : Parcelable

fun FilterEntity.toFilterModel(senderId:Int) = FilterAdvancedModel(
    id = id,
    title = title,
    words = searchWord,
    senderId = senderId
)