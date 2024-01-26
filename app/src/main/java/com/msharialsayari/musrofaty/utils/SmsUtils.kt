package com.msharialsayari.musrofaty.utils

import com.msharialsayari.musrofaty.utils.Constants.ALINMA_BANK
import com.msharialsayari.musrofaty.utils.Constants.STC_PAY_WALLET
import com.msharialsayari.musrofaty.utils.Constants.UR_PAY_BANK
import com.msharialsayari.musrofaty.utils.Constants.listSACurrency
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isExpenses

object SmsUtils {

    private const val WHITESPACES_REGEX = "\\s"
    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"

    //Amount Regex
    private const val AMOUNT_WORD_REGEX = "Amount(:|\\s).+|مبلغ(:|\\s).+|بمبلغ(:|\\s).+|بقيمة(:|\\s).+|القيمة(:|\\s).+|اضافة(:|\\s).+|القسط(:|\\s).+|المبلغ(:|\\s).+"
    private const val AMOUNT_REGEX = "([\\d]+[.][\\d]{2}|[\\d]+)"

    //Stores Regex
    private const val STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+"
    private const val STC_PAY_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+|في(:|\\s).+"// for stcpay
    private const val ALINMA_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+" // for bank alinma
    private const val URPAY_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+" // for urpay

    //Bills Regex
    private const val BILL_NAME_REGEX = "/الخدمة(:|\\s).+|الخدمة(:|\\s).+|الجهة(:|\\s).+"

    //Outgoing transfer Regex
    private const val RECEIVER_NAME_REGEX = "/اسم المستلم(:|\\s).+|إلى(:|\\s).+|المستفيد(:|\\s).+|اسم المستلم(:|\\s).+"



    fun isValidSms(sms: String?): Boolean {
        sms?.let {
            return containsNumber(it) && it.isNotEmpty() && !isIgnoredSms(it)
        } ?: run { return false }
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



    private fun containsNumber(sms:String?):Boolean{
        return sms?.contains(".*\\d+.*".toRegex())?:false
    }

    private fun isIgnoredSms(sms:String?):Boolean{
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
                    return if(listSACurrency.contains(currency) )
                        Constants.CURRENCY_1
                    else
                        currency
                }
            }
        }
        return ""
    }


    fun getStoreName(sms: String?, senderName: String, smsType: SmsType): String {

        if(sms?.isNotEmpty() == true && smsType.isExpenses() ){
            return when (smsType) {
                SmsType.EXPENSES_PURCHASES -> {
                    getStoreNameFromExpensesPurchases(sms,senderName)
                }
                SmsType.PAY_BILLS -> {
                    getBillNameFromExpensesPayBill(sms)
                }
                SmsType.OUTGOING_TRANSFER -> {
                    getReceiverNameFromExpensesOutgoingTransfer(sms)
                }
                else -> ""
            }

        }else{
           return  ""
        }
    }

    private fun getStoreNameFromExpensesPurchases(sms: String, senderName: String): String{
        return try {
            val groupRegex = when {
                ALINMA_BANK.equals(
                    senderName,
                    ignoreCase = true
                ) -> ALINMA_STORE_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0) ?: ""

                UR_PAY_BANK.equals(
                    senderName,
                    ignoreCase = true
                ) -> URPAY_STORE_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0) ?: ""

                STC_PAY_WALLET.equals(
                    senderName,
                    ignoreCase = true
                ) -> STC_PAY_STORE_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0) ?: ""

                else -> STORE_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0) ?: ""
            }

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

    }

    private fun getBillNameFromExpensesPayBill(sms: String): String{
        return try {
            val groupRegex = BILL_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: ""
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

    }

    private fun getReceiverNameFromExpensesOutgoingTransfer(sms: String): String{
        return try {
            val groupRegex = RECEIVER_NAME_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: ""
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

    }





    fun getSmsType(sms: String,
                   expensesPurchasesList:List<String>,
                   expensesOutGoingTransferList:List<String>,
                   expensesPayBillsList:List<String>,
                   incomesList:List<String>): SmsType {


        if (expensesPurchasesList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.EXPENSES_PURCHASES
        }

        if (expensesOutGoingTransferList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.OUTGOING_TRANSFER
        }

        if (expensesPayBillsList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.PAY_BILLS
        }

        if (incomesList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.INCOME
        }


        return SmsType.NOTHING
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
                }else if (getAmount(sms) > 0){
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
                val amountLine = AMOUNT_WORD_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: "0"
                var amount = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(amountLine)?.groupValues?.get(0) ?: "0"
                amount = amount.replace(WHITESPACES_REGEX.toRegex(), "")
                amount.toDouble()
            } catch (e: Exception) {
                0.0
            }
        } ?: run { return 0.0 }

    }



    private fun isSmsContainsCurrency(currency: String, sms: String?): Boolean {
        sms?.let {
            val amountLine = AMOUNT_WORD_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues?.get(0) ?: "0"
            return amountLine.contains(currency.toRegex(option = RegexOption.IGNORE_CASE))
        }?: run { return false }
    }
}