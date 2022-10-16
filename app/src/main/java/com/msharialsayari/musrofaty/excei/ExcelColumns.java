package com.msharialsayari.musrofaty.excei;

import com.msharialsayari.musrofaty.R;

enum ExcelColumns{
    SENDER_NAME_COLUMN(0, R.string.excel_sender_name_header),
    SMS_BODY(1, R.string.excel_sms_body_header),
    STORE_NAME(2, R.string.excel_store_name_header),
    STORE_CATEGORY(3, R.string.excel_store_category_header),
    EXPENSES_AMOUNT_COLUMN(4, R.string.excel_expenses_amount_header),
    INCOME_AMOUNT_COLUMN(5, R.string.excel_income_amount_header),
    AMOUNT_CURRENCY_COLUMN(6, R.string.excel_amount_currency_header),
    SMS_TYPE_COLUMN(7, R.string.excel_sms_type_header),
    SMS_DATE(8, R.string.excel_sms_date_header);

    private int index;
    private int title;
    ExcelColumns(int index, int title){
        this.index = index;
        this.title  = title;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}