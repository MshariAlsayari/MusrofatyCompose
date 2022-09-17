package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.msharialsayari.musrofaty.business_layer.data_layer.database.sender_database.SenderEntity
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import kotlinx.parcelize.Parcelize

@Parcelize
data class SenderModel(
    var id: Int = 0,
    var senderName: String,
    var displayNameAr: String = "",
    var displayNameEn: String = "",
    var contentId: Int = 0,
    var isPined: Boolean = false,
    var isActive: Boolean = true,
):Parcelable{
    companion object{
        fun getDefaultSendersModel():List<SenderModel>{
            val list = mutableListOf<SenderModel>()
            list.add(SenderModel(senderName = Constants.SAIB_BANK    , displayNameAr = "البنك السعودي للاستثمار", displayNameEn = "The Saudi Investment Bank"))
            list.add(SenderModel(senderName = Constants.ALINMA_BANK, displayNameAr = "مصرف الإنماء", displayNameEn = "Alinma Bank"))
            list.add(SenderModel(senderName = Constants.ALRAJHI_BANK, displayNameAr = "مصرف الراجحي", displayNameEn = "Alrajhi Bank"))
            list.add(SenderModel(senderName = Constants.SAMBA_BANK, displayNameAr = "بنك الأهلي", displayNameEn = "Alahli Bank"))
            list.add(SenderModel(senderName = Constants.RIYAD_BANK, displayNameAr = "بنك الرياض", displayNameEn = "Riyad Bank"))
            list.add(SenderModel(senderName = Constants.ALBILAD_BANK, displayNameAr = "بنك البلاد", displayNameEn = "Albilad Bank"))
            list.add(SenderModel(senderName = Constants.ALARABI_BANK, displayNameAr = "البنك العربي", displayNameEn = "Arab National Bank"))
            list.add(SenderModel(senderName = Constants.ALAHLI_BANK, displayNameAr = "بنك الأهلي", displayNameEn = "Alahli Bank"))
            list.add(SenderModel(senderName = Constants.ALAHLI_WITH_SAMBA_BANK, displayNameAr = "بنك الأهلي", displayNameEn = "Alahli Bank"))
            list.add(SenderModel(senderName = Constants.STC_PAY_WALLET, displayNameAr = "UrPay محفظة", displayNameEn = "UrPay wallet"))
            list.add(SenderModel(senderName = Constants.UR_PAY_BANK, displayNameAr = "STCPay محفظة", displayNameEn = "The Saudi Investment Bank"))
            list.add(SenderModel(senderName = Constants.ALKAHRABA_COMPANY, displayNameAr = "الشركة السعودية للكهرباء", displayNameEn = "Saudi Electricity Company"))
            list.add(SenderModel(senderName = Constants.WATER_COMPANY, displayNameAr = "شركة المياه الوطنية", displayNameEn = "National Water Company"))
            list.add(SenderModel(senderName = Constants.MOI_MOROOR, displayNameAr = "الأمن العام", displayNameEn = "Police Security"))
            return list

        }

        fun getDisplayName(context: Context, model:SenderModel?):String {
            return if (SharedPreferenceManager.isArabic(context)){
                model?.displayNameAr?: ""
            }else{
                model?.displayNameEn ?: ""
            }
        }
    }
}

fun SenderModel.toSenderEntity() = SenderEntity(id, senderName, displayNameAr, displayNameEn, contentId, isPined, isActive)



