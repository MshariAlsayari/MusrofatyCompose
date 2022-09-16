package com.msharialsayari.musrofaty.utils.enums

import com.msharialsayari.musrofaty.utils.R


enum class SmsType(val value: String, val valueString: Int) {
    INCOME("0", R.string.incomes_sms_type),
    EXPENSES("1", R.string.expenses_sms_type),
    NOTHING("2", R.string.expenses_sms_type);

    companion object {
        fun getValue(value: String): SmsType {
            return when (value) {
                "1" -> INCOME
                "2" -> EXPENSES
                else -> {
                    NOTHING
                }
            }
        }
    }

}