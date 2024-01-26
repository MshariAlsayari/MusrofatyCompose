package com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum

import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.enums.SmsType

enum class WordDetectorType(val id: Int, val value:Int) {
    EXPENSES_PURCHASES_WORDS(0, SmsType.EXPENSES_PURCHASES.valueString ),
    EXPENSES_OUTGOING_TRANSFER_WORDS(1,SmsType.OUTGOING_TRANSFER.valueString),
    EXPENSES_PAY_BILLS_WORDS(2,SmsType.PAY_BILLS.valueString),


    INCOME_WORDS(3,SmsType.INCOME.valueString),
    CURRENCY_WORDS(4, R.string.tab_currency);


    companion object {
        fun getById(id: Int): WordDetectorType {
            return when (id) {
                EXPENSES_PURCHASES_WORDS.id -> EXPENSES_PURCHASES_WORDS
                EXPENSES_OUTGOING_TRANSFER_WORDS.id -> EXPENSES_OUTGOING_TRANSFER_WORDS
                EXPENSES_PAY_BILLS_WORDS.id -> EXPENSES_PAY_BILLS_WORDS
                INCOME_WORDS.id -> INCOME_WORDS
                CURRENCY_WORDS.id -> CURRENCY_WORDS
                else -> throw Exception("The id is not existed")
            }
        }
    }

}