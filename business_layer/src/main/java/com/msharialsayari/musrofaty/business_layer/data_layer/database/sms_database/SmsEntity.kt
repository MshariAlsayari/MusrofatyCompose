package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "SmsEntity")
data class SmsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "senderName")
    var senderName: String? = null,
    @ColumnInfo(name = "timestamp")
    var timestamp: LocalDateTime? = null,
    @ColumnInfo(name = "body")
    var body: String? = null,
    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean? = false
) : Parcelable

fun SmsEntity.toSmsModel()= SmsModel(id, senderName, timestamp, body, isDeleted)



