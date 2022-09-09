package com.msharialsayari.musrofaty.layer_data.database.sms_database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msharialsayari.musrofaty.layer_domain.model.SmsModel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "SmsEntity")
data class SmsEntity(
    @PrimaryKey
    @ColumnInfo(name = "smsId")
    var smsId: String,
    @ColumnInfo(name = "bankName")
    var bankName: String? = null,
    @ColumnInfo(name = "smsDateTime")
    var smsDateTime: String? = null,
    @ColumnInfo(name = "smsBody")
    var smsBody: String? = null,
    @ColumnInfo(name = "smsCategory")
    var smsCategory: Int? = null,
    @ColumnInfo(name = "isDeleted")
    var isDeleted: Boolean? = false
) : Parcelable {
    fun fillSmsEntity(model: SmsModel) {
        this.smsId = model.smsId
        this.bankName = model.bankName
        this.smsBody = model.smsBody
        this.smsDateTime = model.smsDateTime
        this.smsCategory = model.smsCategory
        this.isDeleted = model.isDeleted
    }
}