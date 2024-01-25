package com.msharialsayari.musrofaty.utils.enums

import com.msharialsayari.musrofaty.R


enum class SmsType(val id: Int, val valueString: Int) {
    NOTHING(0, R.string.empty),
    INCOME(1, R.string.incomes_sms_type),
    EXPENSES_PURCHASES(2, R.string.expenses_sms_type),
    OUTGOING_TRANSFER(3, R.string.outgoing_transfer_sms_type),
    INCOMING_TRANSFER(4, R.string.incoming_transfer_sms_type),
    PAY_BILLS(5, R.string.pay_bill_sms_type);

    companion object {
        fun getValue(id: Int): SmsType {
            return when (id) {
                1 -> INCOME
                2 -> EXPENSES_PURCHASES
                3 -> OUTGOING_TRANSFER
                4 -> INCOMING_TRANSFER
                5 -> PAY_BILLS
                else -> {
                    NOTHING
                }
            }
        }

        fun isIncome(type: SmsType) = type == INCOME ||  type == INCOMING_TRANSFER

        fun isExpenses(type: SmsType) = type == EXPENSES_PURCHASES ||  type == OUTGOING_TRANSFER || type ==PAY_BILLS
    }

}