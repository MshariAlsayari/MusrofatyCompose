package com.msharialsayari.musrofaty.business_layer.data_layer.database.filter_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.FilterModel
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
    @ColumnInfo(name = "senderName")
    var senderName: String? = "",
    @ColumnInfo(name = "dateFrom")
    var dateFrom: String? = "",
    @ColumnInfo(name = "dateTo")
    var dateTo: String? = "",
) : Parcelable


fun FilterEntity.toFilterModel() = FilterModel(
    id = id,
    title = title,
    searchWord = searchWord,
    smsType = smsType,
    date = date,
    senderName = senderName,
    dateFrom = dateFrom,
    dateTo = dateTo)