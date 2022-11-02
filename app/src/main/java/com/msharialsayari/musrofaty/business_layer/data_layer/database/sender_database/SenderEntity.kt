package com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database

import android.os.Parcelable
import androidx.room.*
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SenderEntity",indices = [Index(value = ["senderName"], unique = true)])
data class SenderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "senderName")
    var senderName: String = "",
    @ColumnInfo(name = "displayNameAr")
    var displayNameAr: String = "",
    @ColumnInfo(name = "displayNameEn")
    var displayNameEn: String = "",
    @ColumnInfo(name = "contentId")
    var contentId: Int = 0,
    @ColumnInfo(name = "isPined")
    var isPined: Boolean = false,
    @ColumnInfo(name = "isActive")
    var isActive: Boolean = true,
) : Parcelable


@Parcelize
data class SenderWithRelations(
    @Embedded val sender: SenderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "senderId"
    )
    val sms: List<SmsEntity>,
):Parcelable


@Parcelize
data class SenderWithRelationsModel(
    var sender: SenderModel,
    var sms: List<SmsModel> = listOf(),
) : Parcelable


fun SenderEntity.toSenderModel() = SenderModel(id, senderName, displayNameAr, displayNameEn,contentId, isPined, isActive)
