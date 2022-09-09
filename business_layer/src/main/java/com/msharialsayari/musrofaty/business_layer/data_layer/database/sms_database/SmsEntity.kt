package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SmsEntity")
data class SmsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "senderName")
    var senderName: String? = null,
    @ColumnInfo(name = "dateTime")
    var dateTime: String? = null,
    @ColumnInfo(name = "body")
    var body: String? = null,
    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean? = false
) : Parcelable

fun SmsEntity.toSmsModel()= SmsEntity(id, senderName, dateTime, body, isDeleted)



