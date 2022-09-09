package com.msharialsayari.musrofaty.utils


object Constants {

    private const val EXPENSES_1 = "شراء"
    private const val EXPENSES_2 = "خصم"
    private const val EXPENSES_3 = "مدين"
    private const val EXPENSES_4 = "سداد"
    private const val EXPENSES_5 = "حوالة صادرة"
    private const val EXPENSES_6 = "سحب"

    private const val INCOME_1   = "حوالة واردة"
    private const val INCOME_2   = "دائن"
    private const val INCOME_3   = "عكس عملية"


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


    //Wallets
    const val STC_PAY_WALLET = "STCPAY"
    const val UR_PAY_BANK    = "urpay"


    //BILLS
    const val ALKAHRABA_COMPANY = "ALKAHRABA"
    const val WATER_COMPANY     = "NWC"


    //Currency
    const val CURRENCY_1 = "SAR"
    const val CURRENCY_2 = "ريال"
    const val CURRENCY_3 = "SR"
    const val CURRENCY_4 = "ر.س"


    var listIncomeWords  = listOf(
        INCOME_1,
                                  INCOME_2,
                                  INCOME_3
    )

    var listExpenseWords = listOf(
        EXPENSES_1,
                                  EXPENSES_2,
                                  EXPENSES_3,
                                  EXPENSES_4,
                                  EXPENSES_5,
                                  EXPENSES_6
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
                                  ALAHLI_WITH_SAMBA_BANK
    )


    var listCurrencyWords = listOf(
        CURRENCY_1,
                                   CURRENCY_2,
                                   CURRENCY_3,
                                   CURRENCY_4
    )


    // EXCEL
    const val EXCEL_FILE_NAME  = "sms.xls"
    const val EXCEL_SHEET_NAME = "My Sms"

    //PDF
    const val PDF_FILE_NAME = "sms.pdf"


    //Room
    const val TOTAL_NUMBER_OF_TASKS = 998
    const val MAXIMUM_SMS           = 1000


    //Notifications
    const val CHANNEL_ID      = "VERBOSE_NOTIFICATION"
    const val NOTIFICATION_ID = 1

    //language
    const val arabic_ar  ="ar"
    const val english_en ="en"


    //SMS Eliminator
    const val OTP_ar  ="كلمة المرور لمرة واحدة"
    const val OTP_en  ="One time password"

}