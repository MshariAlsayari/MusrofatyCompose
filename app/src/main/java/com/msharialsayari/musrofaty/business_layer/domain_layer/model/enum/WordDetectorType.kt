package com.msharialsayari.musrofaty.business_layer.domain_layer.model.enum

enum class WordDetectorType(val id: Int) {
    EXPENSES_WORDS(0), INCOME_WORDS(1), CURRENCY_WORDS(2);

    companion object {
        fun getById(id: Int): WordDetectorType {
            return when (id) {
                EXPENSES_WORDS.id -> EXPENSES_WORDS
                INCOME_WORDS.id -> INCOME_WORDS
                CURRENCY_WORDS.id -> CURRENCY_WORDS
                else -> throw Exception("The id is not existed")
            }
        }
    }

}