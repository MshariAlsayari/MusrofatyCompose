package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.ui_component.SmsComponentModel
import com.msharialsayari.musrofaty.utils.enums.SmsType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsModel(
    var id: String,
    var senderName: String = "",
    var timestamp: Long = 0,
    var body: String = "",
    var isSelected: Boolean = false,
    var storeAndCategoryModel: StoreAndCategoryModel? = null,
    var smsType: SmsType = SmsType.NOTHING,
    var currency: String = "",
    var storeName: String = "",
    var amount: Double = 0.0,
    var senderId: Int = 0,
    var senderModel: SenderModel? = null,
    var isFavorite: Boolean = false,
    var isDeleted: Boolean = false,
) : Parcelable


fun SmsModel.toSmsEntity() = SmsEntity(
    id = id,
    senderName = senderName,
    timestamp = timestamp,
    body = body,
    senderId = senderId,
    isFavorite = isFavorite,
    isDeleted = isDeleted
)

fun SmsModel.toSmsComponentModel(context: Context, storeName:String, category:String) = SmsComponentModel(
     id = id,
     senderId = senderId,
     timestamp=timestamp,
     body = body,
     smsType= smsType,
     currency= currency,
     senderDisplayName= SenderModel.getDisplayName(context, senderModel),
     senderCategory= ContentModel.getDisplayName(context, senderModel?.content),
     senderIcon= senderModel?.senderIconUri ?: "",
     isFavorite= isFavorite,
     isDeleted=isDeleted,
     storeName = storeName,
     storeCategory= category,
    amount = amount
)