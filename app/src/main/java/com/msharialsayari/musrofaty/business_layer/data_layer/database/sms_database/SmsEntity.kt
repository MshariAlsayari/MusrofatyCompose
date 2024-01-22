package com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.utils.enums.SmsType
import kotlinx.parcelize.Parcelize

@Parcelize

@Entity(tableName = "SmsEntity")
data class SmsEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String="",
    @ColumnInfo(name = "senderName")
    var senderName: String = "",
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0,
    @ColumnInfo(name = "body")
    var body: String = "",
    @ColumnInfo(name = "senderId")
    var senderId: Int,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean = false,
) : Parcelable


fun SmsEntity.toSmsModel(smsType: SmsType = SmsType.NOTHING) = SmsModel(
    id = id,
    senderName = senderName,
    timestamp = timestamp,
    body = body,
    senderId = senderId,
    isFavorite = isFavorite,
    isDeleted = isDeleted,
    smsType = smsType
)
