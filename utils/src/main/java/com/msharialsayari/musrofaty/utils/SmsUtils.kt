package com.msharialsayari.musrofaty.utils

import android.content.Context

object SmsUtils {


    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"


    fun isValidSms(context: Context,sms:String?):Boolean{
        sms?.let {
            return containerNumber(it) && containsCurrency(context, it) && !isOTPSms(it)
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

    private fun containsCurrency(context: Context,sms:String?):Boolean{
        sms?.let {
            val currencyList = SharedPreferenceManager.getWordsList(context, WordsType.CURRENCY_WORDS)
            return currencyList.find { smsContainsCurrency(it, sms) } != null
        }?: kotlin.run { return false }
    }

    fun smsContainsCurrency(currency: String, sms: String): Boolean {
        return sms.contains(currency.toRegex(option = RegexOption.IGNORE_CASE))
    }

}