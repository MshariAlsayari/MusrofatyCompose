package com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum

enum class WordDetectorType(val id: Int) {
    EXPENSES_PURCHASES_WORDS(0), INCOME_WORDS(1), CURRENCY_WORDS(2),
    EXPENSES_OUTGOING_TRANSFER_WORDS(3),EXPENSES_PAY_BILLS_WORDS(4),;

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