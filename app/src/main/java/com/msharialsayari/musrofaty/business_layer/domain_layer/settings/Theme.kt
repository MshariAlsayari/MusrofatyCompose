package com.msharialsayari.musrofaty.business_layer.domain_layer.settings

enum class Theme(val id: Int) {
    DEFAULT(0),
    LIGHT(1),
    DARK(2);

    companion object {
        fun getThemeById(id: Int): Theme {
            return when (id) {
                1 -> LIGHT
                2 -> DARK
                else -> DEFAULT
            }
        }
    }

}