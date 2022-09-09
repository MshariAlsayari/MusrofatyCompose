package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class SmsModel(
    var id: String,
    var senderName: String? = null,
    var timestamp: LocalDateTime? = null,
    var body: String? = null,
    var isDeleted: Boolean? = false
) : Parcelable


fun SmsModel.toSmsEntity()=SmsEntity(id, senderName, timestamp, body, isDeleted)
