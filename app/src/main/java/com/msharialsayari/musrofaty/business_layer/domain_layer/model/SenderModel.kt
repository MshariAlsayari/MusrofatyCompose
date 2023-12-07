package com.msharialsayari.musrofaty.business_layer.domain_layer.model

import android.content.Context
import android.os.Parcelable
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
    var content: ContentModel? = null,
    var senderIconUri: String ="",
) : Parcelable {

    companion object {
        fun getDefaultSendersModel(
            bankContentId: Int,
            servicesContentId: Int,
            digitalWalletContentId: Int,
            tdawelSenderId: Int,
        ): List<SenderModel> {
            val list = mutableListOf<SenderModel>()
            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.SAIB_BANK,
                    displayNameAr = "البنك السعودي للاستثمار",
                    displayNameEn = "The Saudi Investment Bank"
                )
            )
            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.ALINMA_BANK,
                    displayNameAr = "مصرف الإنماء",
                    displayNameEn = "Alinma Bank"
                )
            )
            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.ALRAJHI_BANK,
                    displayNameAr = "مصرف الراجحي",
                    displayNameEn = "Alrajhi Bank"
                )
            )

            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.RIYAD_BANK,
                    displayNameAr = "بنك الرياض",
                    displayNameEn = "Riyad Bank"
                )
            )
            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.ALBILAD_BANK,
                    displayNameAr = "بنك البلاد",
                    displayNameEn = "Albilad Bank"
                )
            )
            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.ALARABI_BANK,
                    displayNameAr = "البنك العربي",
                    displayNameEn = "Arab National Bank"
                )
            )

            list.add(
                SenderModel(
                    contentId = bankContentId,
                    senderName = Constants.ALAHLI_WITH_SAMBA_BANK,
                    displayNameAr = "بنك الأهلي",
                    displayNameEn = "Alahli Bank"
                )
            )
            list.add(
                SenderModel(
                    contentId = digitalWalletContentId,
                    senderName = Constants.UR_PAY_BANK,
                    displayNameAr = "UrPay محفظة",
                    displayNameEn = "UrPay wallet"
                )
            )
            list.add(
                SenderModel(
                    contentId = digitalWalletContentId,
                    senderName = Constants.STC_PAY_WALLET,
                    displayNameAr = "STCPay محفظة",
                    displayNameEn = "STCPay wallet"
                )
            )
            list.add(
                SenderModel(
                    contentId = servicesContentId,
                    senderName = Constants.ALKAHRABA_COMPANY,
                    displayNameAr = "الشركة السعودية للكهرباء",
                    displayNameEn = "Saudi Electricity Company"
                )
            )
            list.add(
                SenderModel(
                    contentId = servicesContentId,
                    senderName = Constants.WATER_COMPANY,
                    displayNameAr = "شركة المياه الوطنية",
                    displayNameEn = "National Water Company"
                )
            )
            list.add(
                SenderModel(
                    contentId = servicesContentId,
                    senderName = Constants.MOI_MOROOR,
                    displayNameAr = "الأمن العام",
                    displayNameEn = "Police Security"
                )
            )


            list.add(
                SenderModel(
                    contentId = tdawelSenderId,
                    senderName = Constants.DERAYAH_SMS,
                    displayNameAr = "منصة دراية",
                    displayNameEn = "Derayah"
                )
            )
            return list

        }

        fun getDisplayName(context: Context, model: SenderModel?): String {
            return if (SharedPreferenceManager.isArabic(context)) {
                model?.displayNameAr ?: ""
            } else {
                model?.displayNameEn ?: ""
            }
        }

        fun getDisplayName(context: Context, model: SenderEntity?): String {
            return if (SharedPreferenceManager.isArabic(context)) {
                model?.displayNameAr ?: ""
            } else {
                model?.displayNameEn ?: ""
            }
        }
    }
}

fun SenderModel.toSenderEntity() =
    SenderEntity(id, senderName, displayNameAr, displayNameEn, contentId, isPined, senderIconUri)



