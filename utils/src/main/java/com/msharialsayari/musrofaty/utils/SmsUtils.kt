package com.msharialsayari.musrofaty.utils

object SmsUtils {


    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"


    fun isValidSms(sms:String?):Boolean{
        sms?.let {
            return containerNumber(it) && !isOTPSms(it)
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
        return sms?.contains(Constants.OTP_ar, ignoreCase = true) == true || sms?.contains(Constants.OTP_en, ignoreCase = true) ?:false
    }

}