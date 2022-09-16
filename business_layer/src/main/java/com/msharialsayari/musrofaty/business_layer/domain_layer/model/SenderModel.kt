package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class SenderModel(
    var id: Int = 0,
    var senderName: String,
    var displayNameAr: String = "",
    var displayNameEn: String = "",
    var type: String = "",
    var isPined: Boolean = false,
    var isActive: Boolean = true,
):Parcelable

fun SenderModel.toSenderEntity() = SenderEntity(id, senderName, displayNameAr, displayNameEn, type, isPined, isActive)

