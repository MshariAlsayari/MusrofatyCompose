package com.msharialsayari.musrofaty.utils.enums

import com.msharialsayari.musrofaty.R


enum class SmsType(val id: Int, val valueString: Int) {
    NOTHING(0, R.string.empty),
    INCOME(1, R.string.incomes_sms_type),
    EXPENSES_PURCHASES(2, R.string.expenses_sms_type),
    OUTGOING_TRANSFER(3, R.string.outgoing_transfer_sms_type),
    PAY_BILLS(4, R.string.pay_bill_sms_type),
    WITHDRAWAL_ATM(5, R.string.withdrawal_atm_sms_type);

    companion object {
        fun getValue(id: Int): SmsType {
            return when (id) {
                1 -> INCOME
                2 -> EXPENSES_PURCHASES
                3 -> OUTGOING_TRANSFER
                4 -> PAY_BILLS
                5 -> WITHDRAWAL_ATM
                else -> {
                    NOTHING
                }
            }
        }

        fun SmsType.isIncome() = this == INCOME

        fun SmsType.isExpenses() = this == EXPENSES_PURCHASES ||  this == OUTGOING_TRANSFER || this == PAY_BILLS ||this == WITHDRAWAL_ATM
    }

}