package com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum

import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.enums.SmsType

enum class WordDetectorType(val id: Int, val value:Int) {
    EXPENSES_PURCHASES_WORDS(0, SmsType.EXPENSES_PURCHASES.valueString ),
    EXPENSES_OUTGOING_TRANSFER_WORDS(1,SmsType.OUTGOING_TRANSFER.valueString),
    WITHDRAWAL_ATM_WORDS(2, SmsType.WITHDRAWAL_ATM.valueString),
    EXPENSES_PAY_BILLS_WORDS(3,SmsType.PAY_BILLS.valueString),
    INCOME_WORDS(4,SmsType.INCOME.valueString),
    CURRENCY_WORDS(5, R.string.tab_currency),
    AMOUNT_WORDS(6, R.string.tab_amount),
    STORE_WORDS(7, R.string.tab_store);



    companion object {
        fun getById(id: Int): WordDetectorType {
            return when (id) {
                EXPENSES_PURCHASES_WORDS.id -> EXPENSES_PURCHASES_WORDS
                EXPENSES_OUTGOING_TRANSFER_WORDS.id -> EXPENSES_OUTGOING_TRANSFER_WORDS
                EXPENSES_PAY_BILLS_WORDS.id -> EXPENSES_PAY_BILLS_WORDS
                INCOME_WORDS.id -> INCOME_WORDS
                CURRENCY_WORDS.id -> CURRENCY_WORDS
                AMOUNT_WORDS.id -> AMOUNT_WORDS
                STORE_WORDS.id -> STORE_WORDS
                WITHDRAWAL_ATM_WORDS.id -> WITHDRAWAL_ATM_WORDS
                else -> throw Exception("The id is not existed")
            }
        }

        fun getAnalyticsScreenList()= listOf(
            CURRENCY_WORDS,
            AMOUNT_WORDS,
            STORE_WORDS
        )

        fun getSmsTypesScreenList()= listOf(
            EXPENSES_PURCHASES_WORDS,
            EXPENSES_OUTGOING_TRANSFER_WORDS,
            WITHDRAWAL_ATM_WORDS,
            EXPENSES_PAY_BILLS_WORDS,
            INCOME_WORDS
        )

        fun getTypeByIndexForAnalyticsScreen(index:Int):WordDetectorType{
            return when(index){
                0 -> CURRENCY_WORDS
                1 -> AMOUNT_WORDS
                2 -> STORE_WORDS
                else -> CURRENCY_WORDS
            }

        }

        fun getTypeByIndexForSmsTypesScreen(index:Int):WordDetectorType{
            return when(index){
                0 -> EXPENSES_PURCHASES_WORDS
                1 -> EXPENSES_OUTGOING_TRANSFER_WORDS
                2 -> WITHDRAWAL_ATM_WORDS
                3 -> EXPENSES_PAY_BILLS_WORDS
                4 -> INCOME_WORDS
                else -> EXPENSES_PURCHASES_WORDS
            }
        }


    }

}