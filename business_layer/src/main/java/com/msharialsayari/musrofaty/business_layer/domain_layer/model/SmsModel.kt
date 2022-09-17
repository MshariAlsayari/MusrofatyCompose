package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sms_database.SmsEntity
import com.msharialsayari.musrofaty.business_layer.data_layer.database.store_database.StoreAndCategoryModel
import com.msharialsayari.musrofaty.utils.SmsUtils
import com.msharialsayari.musrofaty.utils.enums.SmsType
import kotlinx.parcelize.Parcelize
import java.util.*

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
    var senderId: Int = 0,
    var displayName: String = "",
) : Parcelable{

    val storeName :String
        get() =
            SmsUtils.getStoreName(body)
}


fun SmsModel.toSmsEntity()=SmsEntity(id, senderName, timestamp, body,senderId)
