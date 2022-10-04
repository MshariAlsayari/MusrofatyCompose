package com.msharialsayari.musrofaty.utils


object Constants {

    private const val EXPENSES_1 = "شراء"
    private const val EXPENSES_2 = "خصم"
    private const val EXPENSES_3 = "مدين"
    private const val EXPENSES_4 = "سداد"
    private const val EXPENSES_5 = "حوالة صادرة"
    private const val EXPENSES_6 = "سحب"
    const val EXPENSES_7 = "إصدار فاتورتك"

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
    const val MOI_MOROOR = "MOI-MOROOR"


    //Currency
    const val CURRENCY_1 = "SAR"
    const val CURRENCY_2 = "ريال"
    const val CURRENCY_3 = "SR"
    const val CURRENCY_4 = "ر.س"
    const val CURRENCY_5 = "USD"
    var listSACurrency  = listOf(
        CURRENCY_1,
        CURRENCY_2,
        CURRENCY_3,
        CURRENCY_4,
    )


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
                                  EXPENSES_6,
                                  EXPENSES_7
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
                                  MOI_MOROOR
    )


    var listCurrencyWords = listOf(
                                   CURRENCY_1,
                                   CURRENCY_2,
                                   CURRENCY_3,
                                   CURRENCY_4,
                                   CURRENCY_5
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
    var eliminatorList = listOf(OTP_ar, OTP2_ar,OTP_en,OTP_shortcut_en,dear_clients_ar, dear_clients2_ar,sign_in_en,log_in_en,sign_in_ar,sign_in2_ar,code_activate_ar,code_activate_en,code_detect_ar,code_detect_en)

    // mada
    const val mada_ar  ="مدى"
    const val mada_en  ="mada"
    var madaWordList = listOf(mada_ar, mada_en)

    //visa
    const val visa_ar  ="فيزا"
    const val visa_en  ="visa"
    const val visa_safar  ="سفر"
    const val visa_credit  ="ائتمانية"
    const val visa_urpay  ="urpay"
    var visaWordList = listOf(visa_ar, visa_en,visa_safar,visa_credit,visa_urpay)

    //visa
    const val mastercard_ar  ="ماستركارد"
    const val mastercard_en  ="mastercard"
    var mastercardWordList = listOf(mastercard_ar, mastercard_en)

}