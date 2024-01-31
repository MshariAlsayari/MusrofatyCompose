package com.msharialsayari.musrofaty.utils


object Constants {

    //Incomes
    private const val INCOME_1   = "حوالة واردة"
    var listIncomeWords  = listOf(INCOME_1)

    //Expenses purchases
    private const val EXPENSES_1 = "شراء"
    private const val EXPENSES_2 = "مشتريات"

    var listExpenseWords = listOf(
        EXPENSES_1,
        EXPENSES_2,
    )

    //Expenses pay bills
    private const val EXPENSES_PAY_BILL_1 = "سداد فاتورة"
    private const val EXPENSES_PAY_BILL_2 = "مدفوعات سداد"
    var listExpensePayBillsWords = listOf(
        EXPENSES_PAY_BILL_1,
        EXPENSES_PAY_BILL_2
    )

    //Expenses outgoing transfer
    private const val EXPENSES_OUTGOING_TRANSFER_1 = "حوالة صادرة"
    private const val EXPENSES_OUTGOING_TRANSFER_2 = "حوالة صادرة داخلية"
    private const val EXPENSES_OUTGOING_TRANSFER_3 = "حوالة فورية محلية صادرة"
    private const val EXPENSES_OUTGOING_TRANSFER_4 = "خصم عبر حوالة محفظة"
    private const val EXPENSES_OUTGOING_TRANSFER_5 = "حوالة داخلية صادرة"
    const val EXPENSES_OUTGOING_TRANSFER_6 = "حوالة محلية"
    const val EXPENSES_OUTGOING_TRANSFER_7 = "حوالة داخلية"
    var listExpenseOutgoingTransferWords = listOf(
        EXPENSES_OUTGOING_TRANSFER_1,
        EXPENSES_OUTGOING_TRANSFER_2,
        EXPENSES_OUTGOING_TRANSFER_3,
        EXPENSES_OUTGOING_TRANSFER_4,
        EXPENSES_OUTGOING_TRANSFER_5,
        EXPENSES_OUTGOING_TRANSFER_6,
        EXPENSES_OUTGOING_TRANSFER_7
    )


    //Senders
    const val SAIB_BANK              = "SAIB"
    const val ALINMA_BANK            = "alinmabank"
    const val ALRAJHI_BANK           = "AlRajhiBank"
    const val SAMBA_BANK             = "Samba."
    const val RIYAD_BANK             = "RiyadBank"
    const val ALBILAD_BANK           = "BankAlbilad"
    const val ALARABI_BANK           = "ANB"
    const val ALAHLI_BANK            = "SNBAlAhli"
    const val ALAHLI_WITH_SAMBA_BANK = "SNB-AlAhli"
    const val DERAYAH_SMS            = "DerayahSMS"

    //Wallets
    const val STC_PAY_WALLET = "STCPAY"
    const val UR_PAY_BANK    = "urpay"


    //BILLS
    const val ALKAHRABA_COMPANY = "ALKAHRABA"
    const val WATER_COMPANY     = "NWC"
    const val MOI_MOROOR = "MOI-MOROOR"


    //Currency
    const val CURRENCY_1 = "SAR"
    const val CURRENCY_2 = "ريال"
    const val CURRENCY_3 = "SR"
    const val CURRENCY_4 = "ر.س"
    const val CURRENCY_5 = "USD"
    const val CURRENCY_6 = "AED"
    var listSACurrency  = listOf(
        CURRENCY_1,
        CURRENCY_2,
        CURRENCY_3,
        CURRENCY_4,
    )



    var listOfSenders    = listOf(
                                  SAIB_BANK,
                                  ALINMA_BANK,
                                  ALRAJHI_BANK,
                                  SAMBA_BANK,
                                  RIYAD_BANK,
                                  ALBILAD_BANK,
                                  ALARABI_BANK,
                                  STC_PAY_WALLET,
                                  ALAHLI_BANK,
                                  ALKAHRABA_COMPANY,
                                  WATER_COMPANY,
                                  UR_PAY_BANK,
                                  ALAHLI_WITH_SAMBA_BANK,
                                  MOI_MOROOR,
                                  DERAYAH_SMS
    )


    var listCurrencyWords = listOf(
                                   CURRENCY_1,
                                   CURRENCY_2,
                                   CURRENCY_3,
                                   CURRENCY_4,
                                   CURRENCY_5,
                                   CURRENCY_6
    )


    // EXCEL
    const val EXCEL_FILE_NAME  = "sms.xls"

    //Notifications
    const val CHANNEL_ID      = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1

    //language
    const val arabic_ar  ="ar"
    const val english_en ="en"



    //SMS Eliminator
    const val OTP_ar = "كلمة المرور"
    const val OTP2_ar = "كلمة مرور"
    const val OTP_en = "password"
    const val OTP_shortcut_en = "OTP"
    const val dear_clients_ar = "عزيزنا"
    const val dear_clients2_ar = "عميلنا"
    const val sign_in_en = "sign in"
    const val log_in_en = "log in"
    const val sign_in_ar = "تسجيل"
    const val sign_in2_ar = "دخول"
    const val code_activate_ar = "رمز"
    const val code_activate_en = "code"
    const val code_detect_ar = "رمز التحقق"
    const val code_detect_en = "detect code"
    const val credit_limit = "رصيد غير كافي"
    const val credit_limit_en = "Declined due to Insufficient balance"
    var eliminatorList = listOf(
        OTP_ar, OTP2_ar,
        OTP_en,
        OTP_shortcut_en,
        dear_clients_ar, dear_clients2_ar,
        sign_in_en,
        log_in_en,
        sign_in_ar,
        sign_in2_ar,
        code_activate_ar,
        code_activate_en,
        code_detect_ar,
        code_detect_en,
        credit_limit,
        credit_limit_en
    )

    //Amount words
    private const val Amount_1 = "Amount"
    private const val Amount_2 = "مبلغ"
    private const val Amount_3 = "بمبلغ"
    private const val Amount_4 = "بقيمة"
    private const val Amount_5 = "القيمة"
    private const val Amount_6 = "اضافة"
    private const val Amount_7 = "القسط"
    private const val Amount_8 = "المبلغ"
    private const val Amount_9 = "تعبئة"


    var listAmountWords = listOf(
        Amount_1,
        Amount_2,
        Amount_3,
        Amount_4,
        Amount_5,
        Amount_6,
        Amount_7,
        Amount_8,
        Amount_9,
    )


    //Store words
    private const val Store_1 = "At"
    private const val Store_2 = "لدى"
    private const val Store_3 = "من"
    private const val Store_4 = "في"
    private const val Store_5 = "محطة"


    var listStoreWords = listOf(
        Store_1,
        Store_2,
        Store_3,
        Store_4,
        Store_5,
    )




}