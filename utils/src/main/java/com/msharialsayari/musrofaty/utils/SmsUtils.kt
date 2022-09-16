package com.msharialsayari.musrofaty.utils

import android.content.Context
import com.msharialsayari.musrofaty.utils.enums.SmsType

object SmsUtils {


    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"
    private const val STORE_FROM_REGEX = "At:.+|لدى:.+"


    fun isValidSms(context: Context,sms:String?):Boolean{
        sms?.let {
            return containerNumber(it)           &&
                   clearSms(sms) != null         &&
                   !isOTPSms(it)
        }?: run { return false }

    }

    fun clearSms(sms:String?):String?{
        sms?.let {
            var clearedSms  =  it.replace(",", "")
                clearedSms  =  clearedSms.replace(UNWANTED_UNICODE_REGEX.toRegex(), "")
            return StringsUtils.formatArabicDigits(clearedSms)
        }?: run { return sms }
    }

    fun isAlahliSender(sender:String):Boolean{
        return  sender.equals(Constants.SAMBA_BANK, ignoreCase = true) ||
                sender.equals(Constants.ALAHLI_BANK, ignoreCase = true) ||
                sender.equals(Constants.ALAHLI_WITH_SAMBA_BANK, ignoreCase = true)
    }



    private fun containerNumber(sms:String?):Boolean{
        return sms?.contains(".*\\d+.*".toRegex())?:false
    }

    private fun isOTPSms(sms:String?):Boolean{
        return sms?.contains(Constants.OTP_ar, ignoreCase = true) == true || sms?.contains(Constants.OTP_en, ignoreCase = true)== true ||  sms?.contains(Constants.OTP_shortcut_en, ignoreCase = true)  ?:false
    }


    fun getCurrency(smsBody: String? , currency:List<String>): String {
        smsBody?.let {sms->
            return currency.find { it.contains(sms, ignoreCase = true) } ?: ""
        }?:run { return "" }
    }


    fun getStoreName(sms: String?): String {
        sms?.let {
            return try {
                val groupRegex = STORE_FROM_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: ""
                var storeName = ""
                val list = groupRegex.split(":")
                if (list.size >= 2) {
                    storeName = list[1]
                    storeName = storeName.replace("\\n".toRegex(), "")
                }
                storeName
            } catch (e: Exception) {
                ""
            }
        }?: kotlin.run { return "" }
    }



    fun getSmsType(sms: String, expensesList:List<String>,incomesList:List<String>): SmsType {
        if (isExpensesSMS(sms, expensesList)) {
            return SmsType.EXPENSES
        }

        if (isIncomeSMS(sms, incomesList)) {
            return SmsType.INCOME
        }
        return SmsType.NOTHING
    }

    private fun isExpensesSMS(smsBody: String?, words:List<String>): Boolean {
        smsBody?.let {sms->
            return words.find { it.contains(sms, ignoreCase = true) }     != null
        }?:run { return false }

    }

    private fun isIncomeSMS(smsBody: String?, words:List<String>): Boolean {
        smsBody?.let {sms->
            return words.find { it.contains(sms, ignoreCase = true) } != null
        }?:run { return false }

    }

}