package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import com.msharialsayari.musrofaty.business_layer.data_layer.database.content_database.ContentEntity
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.ContentKey
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum.SendersKey
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import kotlinx.parcelize.Parcelize


@Parcelize
data class ContentModel (
    var id: Int = 0,
    var contentKey: String = "",
    var valueAr: String? = null,
    var valueEn: String? = null,
):Parcelable{
    companion object{
        fun getDefaultContent():List<ContentModel>{
            val list = mutableListOf<ContentModel>()
            list.addAll(getDefaultSendersContent())
            return list
        }

        fun getDisplayName(context: Context, model:ContentModel?):String {
            return if (SharedPreferenceManager.isArabic(context)){
                model?.valueAr?: ""
            }else{
                model?.valueEn ?: ""
            }
        }


        private fun getDefaultSendersContent():List<ContentModel>{
            val list = mutableListOf<ContentModel>()
            list.add(ContentModel(contentKey = ContentKey.SENDERS.name, valueAr = SendersKey.BANKS.valueAr, valueEn = SendersKey.BANKS.valueEn))
            list.add(ContentModel(contentKey = ContentKey.SENDERS.name, valueAr = SendersKey.SERVICES.valueAr, valueEn = SendersKey.SERVICES.valueEn))
            list.add(ContentModel(contentKey = ContentKey.SENDERS.name, valueAr = SendersKey.DIGITALWALLET.valueAr, valueEn = SendersKey.DIGITALWALLET.valueEn))
            return list

        }

    }
}

fun ContentModel.toContentEntity() = ContentEntity(id,contentKey, valueAr, valueEn)

