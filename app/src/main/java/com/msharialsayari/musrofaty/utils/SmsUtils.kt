package com.msharialsayari.musrofaty.utils

import com.msharialsayari.musrofaty.utils.Constants.ALINMA_BANK
import com.msharialsayari.musrofaty.utils.enums.SmsType

object SmsUtils {




    private const val WHITESPACES_REGEX = "\\s"
    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"
    private const val AMOUNT_REGEX = "([\\d]+[.][\\d]{2}|[\\d]+)"
    private const val STORE_FROM_REGEX = "At(:|\\s).+|لدى(:|\\s).+"
    private const val ALINMA_STORE_FROM_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+" // for bank alinma
    private const val AMOUNT_WORD_REGEX = "Amount(:|\\s).+|مبلغ(:|\\s).+|بمبلغ(:|\\s).+|بقيمة(:|\\s).+|القيمة(:|\\s).+|اضافة(:|\\s).+|القسط(:|\\s).+"


    fun isValidSms(sms:String?):Boolean{
        sms?.let {
            return containerNumber(it)           &&
                   it.isNotEmpty()               &&
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
        Constants.eliminatorList.forEach {
            if (sms?.contains(it, ignoreCase = true) == true)
                return true
        }
        return false
    }


    fun getCurrency(smsBody: String?, currencyList:List<String>): String {
        smsBody?.let {
            for (currency in currencyList) {
                if (isSmsContainsCurrency(currency, smsBody)) {
                    return currency
                }
            }
        }
        return ""
    }


    fun getStoreName(sms: String?, isAlinmaBank:Boolean): String {
        sms?.let {
            return try {

                val groupRegex = if (isAlinmaBank)
                    ALINMA_STORE_FROM_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: ""
                else
                    STORE_FROM_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: ""


                var storeName = ""
                val list = groupRegex.split(":")
                if (list.size >= 2) {
                    storeName = list[1].trim()
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
        return words.any { smsBody?.contains(it, ignoreCase = true) == true }
    }

    private fun isIncomeSMS(smsBody: String?, words:List<String>): Boolean {
        return words.any { smsBody?.contains(it, ignoreCase = true) == true }
    }

    fun isSACurrency(currency: String) =   currency.equals(Constants.CURRENCY_1, ignoreCase = true) || currency.equals(
        Constants.CURRENCY_2,
        ignoreCase = true
    ) || currency.equals(
        Constants.CURRENCY_3,
        ignoreCase = true
    ) || currency.equals(
        Constants.CURRENCY_4,
        ignoreCase = true
    )

    fun extractAmount(sms: String?, currencyList: List<String>): Double {
        var amount = 0.0
        sms?.let {
            for (currency in currencyList) {
                if (isSmsContainsCurrency(currency, sms)) {
                    amount = getAmount(sms)
                    return amount
                }
            }
        }
        return amount
    }

    private fun getAmount(sms: String?): Double {
        sms?.let {
            return try {
                val amountLine = AMOUNT_WORD_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0) ?: "0"
                var amount = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(amountLine)?.groupValues?.get(0) ?: "0"
                amount = amount.replace(WHITESPACES_REGEX.toRegex(), "")
                amount.toDouble()
            } catch (e: Exception) {
                0.0
            }
        } ?: run { return 0.0 }

    }



    private fun isSmsContainsCurrency(currency: String, sms: String?): Boolean {
        return sms?.contains(currency.toRegex(option = RegexOption.IGNORE_CASE)) ?: false
    }

    fun isAlinmaBank(senderName:String)= ALINMA_BANK.equals(senderName,ignoreCase = true)

}