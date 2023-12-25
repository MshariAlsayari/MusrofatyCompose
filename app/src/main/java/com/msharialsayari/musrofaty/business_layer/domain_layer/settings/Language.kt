package com.msharialsayari.musrofaty.business_layer.domain_layer.settings


enum class Language(val id: Int, val shortcut: String) {
    DEFAULT(0, ""),
    ARABIC(1, "ar"),
    ENGLISH(2, "en");


    companion object {
        fun getLanguageById(id: Int): Language {
            return when (id) {
                1 -> ARABIC
                2 -> ENGLISH
                else -> DEFAULT
            }
        }
    }

}