package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.ui_component.CategoryDetailsStatisticsModel
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SmsModel(
    var id: String,
    var senderName: String = "",
    var timestamp: Long = 0,
    var body: String = "",
    var isSelected:Boolean = false,
    var storeAndCategoryModel : StoreAndCategoryModel? =null,
    var smsType: SmsType  = SmsType.NOTHING,
    var currency: String="",
    var amount: Double=0.0,
    var senderId: Int = 0,
    var senderModel: SenderModel?=null,
    var isFavorite: Boolean = false,
) : Parcelable{

    val storeName :String
        get() =
            SmsUtils.getStoreName(body, SmsUtils.isAlinmaBank(senderName))
}


fun SmsModel.toSmsEntity()=SmsEntity(id=id, senderName = senderName, timestamp = timestamp, body = body, senderId = senderId, isFavorite = isFavorite)


fun SmsModel.toCategoryDetailsStatisticsModel()= CategoryDetailsStatisticsModel(  smsId = id , storeName =storeName, amount =amount, currency=currency,timestamp=timestamp)