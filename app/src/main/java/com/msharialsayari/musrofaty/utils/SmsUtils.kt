package com.msharialsayari.musrofaty.utils

import com.msharialsayari.musrofaty.utils.Constants.listSACurrency
import com.msharialsayari.musrofaty.utils.enums.SmsType
import com.msharialsayari.musrofaty.utils.enums.SmsType.Companion.isExpenses

object SmsUtils {

    private const val WHITESPACES_REGEX = "\\s"
    private const val LETTERS_REGEX = "\\p{L}"
    private const val UNWANTED_UNICODE_REGEX = "\u202C\u202A"

    //Amount Regex
    private const val AMOUNT_WORD_REGEX = "Amount(:|\\s).+|مبلغ(:|\\s).+|بمبلغ(:|\\s).+|بقيمة(:|\\s).+|القيمة(:|\\s).+|اضافة(:|\\s).+|القسط(:|\\s).+|المبلغ(:|\\s).+|تعبئة(:|\\s).+"
    private const val AMOUNT_REGEX = "([\\d]+[.][\\d]{2}|[\\d]+)"

    //Stores Regex
    private const val STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|محطة(:|\\s).+"
    private const val STC_PAY_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+|في(:|\\s).+|محطة(:|\\s).+"// for stcpay
    private const val ALINMA_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+|محطة(:|\\s).+" // for bank alinma
    private const val URPAY_STORE_NAME_REGEX = "At(:|\\s).+|لدى(:|\\s).+|من(:|\\s).+|محطة(:|\\s).+" // for urpay

    //Bills Regex
    private const val BILL_NAME_REGEX = "/الخدمة(:|\\s).+|الخدمة(:|\\s).+|الجهة(:|\\s).+"

    //Outgoing transfer Regex
    private const val RECEIVER_NAME_REGEX = "/اسم المستلم(:|\\s).+|إلى(:|\\s).+|الى(:|\\s).+|المستفيد(:|\\s).+|اسم المستلم(:|\\s).+"

    // Internal transfer rajhi
    private const val INTERNAL_TRANSFER_AS_OUTGOING_TRANSFER_REGEX = "هههههه(:|\\s).+|الى(:|\\s).[\\p{L}]+"



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

    fun isArabiBankSender(sender:String):Boolean{
        return  sender.equals(Constants.ALARABI_BANK, ignoreCase = true)
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


    fun getCurrency(smsBody: String?, currencyList:List<String>,amountList: List<String>): String {
        smsBody?.let {
            for (currency in currencyList) {
                if (isSmsContainsCurrency(currency, smsBody,amountList)) {
                    return if(listSACurrency.contains(currency) )
                        Constants.CURRENCY_1
                    else
                        currency
                }
            }

            val tempCurrency = getAmountWithCurrency(it,amountList)
             return if (tempCurrency.contains(LETTERS_REGEX.toRegex())){
                 ""
             }else{
                 Constants.CURRENCY_1
             }
        }


        return ""
    }


    fun getStoreName(sms: String?, smsType: SmsType, storesWord: List<String>): String {

        if(sms?.isNotEmpty() == true && smsType.isExpenses() ){
            return when (smsType) {
                SmsType.EXPENSES_PURCHASES -> {
                    getStoreNameFromExpensesPurchases(sms,storesWord)
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

    private fun getStoreNameFromExpensesPurchases(
        sms: String,
        storesWord: List<String>
    ): String{
        return try {
            val regex = getRegex(storesWord)
            var insteadLoopStopped = false
            var storeName = ""

            for (itemRegex in regex){
                val groupRegex = itemRegex.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues ?: emptyList()
                for (item in groupRegex){
                    if(item.contains(":")){
                        val tempStoreName = item.split(":").last()
                        if (canBeStoreName(tempStoreName)) {
                            storeName = tempStoreName.trim().replace("\\n".toRegex(), "")
                            insteadLoopStopped = true
                            break
                        }

                    }
                }

                if(insteadLoopStopped){
                    break
                }
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
                   senderName: String,
                   expensesPurchasesList:List<String>,
                   expensesOutGoingTransferList:List<String>,
                   expensesPayBillsList:List<String>,
                   expensesWithdrawalATMsList:List<String>,
                   incomesList:List<String>): SmsType {


        if (expensesPurchasesList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.EXPENSES_PURCHASES
        }

        if (expensesOutGoingTransferList.any {
                if(it == Constants.EXPENSES_OUTGOING_TRANSFER_6 && senderName == Constants.ALRAJHI_BANK)  {
                    sms.contains("رسوم:", ignoreCase = true)
                }else if(it == Constants.EXPENSES_OUTGOING_TRANSFER_7 && senderName == Constants.ALRAJHI_BANK){
                 !INTERNAL_TRANSFER_AS_OUTGOING_TRANSFER_REGEX.toRegex(option = RegexOption.IGNORE_CASE)
                    .find(sms)?.groupValues?.get(0).isNullOrEmpty()

                }else{
                    sms.contains(it, ignoreCase = true)
                }

        }) {
            return SmsType.OUTGOING_TRANSFER
        }

        if (expensesWithdrawalATMsList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.WITHDRAWAL_ATM
        }

        if (expensesPayBillsList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.PAY_BILLS
        }

        if (incomesList.any { sms.contains(it, ignoreCase = true) }) {
            return SmsType.INCOME
        }


        return SmsType.NOTHING
    }

    fun extractAmount(sms: String?, currencyList: List<String>,amountList: List<String>): Double {
        var amount = 0.0
        sms?.let {
            for (currency in currencyList) {
                if (isSmsContainsCurrency(currency, sms, amountList)) {
                    amount = getAmount(sms,amountList)
                    return amount
                }else if (getAmount(sms,amountList) > 0){
                    amount = getAmount(sms,amountList)
                    return amount
                }
            }
        }
        return amount
    }

    private fun getAmount(sms: String?,amountList: List<String>): Double {
        sms?.let {
            return try {
                val regex = getRegex(amountList)
                for (itemRegex in regex){
                    val groupRegex = itemRegex.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues ?: emptyList()
                    for (itemAmount in groupRegex){
                        if(itemAmount.contains(":")){
                            val tempAmount = itemAmount.split(":").last()
                            if (canBeAmount(tempAmount)) {
                                var amount = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(tempAmount)?.groupValues?.get(0) ?:"0"
                                amount = amount.replace(WHITESPACES_REGEX.toRegex(), "")
                                amount = amount.replace(LETTERS_REGEX.toRegex(), "")
                                return amount.toDouble()
                            }

                        }else{
                            val number  = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(itemAmount)?.groupValues?.get(0) ?:"0"
                            if (canBeAmount(number)){
                                var amount = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(number)?.groupValues?.get(0) ?:"0"
                                amount = amount.replace(WHITESPACES_REGEX.toRegex(), "")
                                amount = amount.replace(LETTERS_REGEX.toRegex(), "")
                                return amount.toDouble()
                            }
                        }

                    }

                }

                return  0.0
            } catch (e: Exception) {
                0.0
            }
        } ?: run { return 0.0 }

    }

    private fun getAmountWithCurrency(sms: String?,amountList: List<String>): String {
        sms?.let {
            return try {
                val regex = getRegex(amountList)
                for (itemRegex in regex){
                    val groupRegex = itemRegex.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues ?: emptyList()
                    for (itemAmount in groupRegex){
                        if(itemAmount.contains(":")){
                            val tempAmount = itemAmount.split(":").last()
                            if (canBeAmount(tempAmount)) {
                                return tempAmount
                            }

                        }else{
                            val number  = AMOUNT_REGEX.toRegex(option = RegexOption.IGNORE_CASE).find(itemAmount)?.groupValues?.get(0) ?:"0"
                            if (canBeAmount(number)){
                                return number
                            }
                        }
                    }

                }

                return  ""
            } catch (e: Exception) {
                ""
            }
        } ?: run { return "" }

    }



    private fun isSmsContainsCurrency(currency: String, sms: String?, amountList: List<String>): Boolean {
        sms?.let {
            val regex = getRegex(amountList)
            for (itemRegex in regex){
                val groupRegex = itemRegex.toRegex(option = RegexOption.IGNORE_CASE).find(sms)?.groupValues ?: emptyList()
                for (itemAmount in groupRegex){
                    if (itemAmount.contains(currency.toRegex(option = RegexOption.IGNORE_CASE))){
                        return true
                    }

                }
            }


            return false
        }?: run { return false }
    }

    private fun getRegex(words: List<String>):List<String>{
        val list = mutableListOf<String>()
        words.map { item->
            list.add("$item.+")
        }
        return list
    }

    private fun canBeStoreName(storeName: String):Boolean{
        val regex = "(X|x|\\*)+\\d+"
        return !storeName.contains(regex.toRegex())
    }

    private fun canBeAmount(amount: String):Boolean{
        return amount.contains(AMOUNT_REGEX.toRegex())
    }
}