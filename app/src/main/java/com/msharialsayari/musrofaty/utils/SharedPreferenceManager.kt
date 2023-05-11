package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import java.util.*


object SharedPreferenceManager {


    private const val PREF_LANGUAGE                                        = "key_preferredLang"
    private const val PREF_THEME                                           = "PREF_THEME"
    private const val PREF_INCOME_WORDS                                    = "PREF_INCOME_WORDS"
    private const val PREF_INIT_APP                                        = "PREF_INIT_APP"
    
    
    private const val PREF_BANKS = "PREF_BANKS"
    private const val PREF_CURRENCY = "PREF_CURRENCY"
    private const val PREF_EXPENSES_WORDS = "PREF_EXPENSES_WORDS"
    private const val PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED"
    private const val PREF_FILTER_PERIOD = "PREF_FILTER_PERIOD"

    fun storeTheme(context: Context, theme: AppTheme) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(PREF_THEME,theme.id)
            .apply()

    }

    fun getTheme(context: Context):Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_THEME, AppTheme.System.id)


    }



    fun storeLanguage(context: Context, locale: Locale) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putString(PREF_LANGUAGE, Gson().toJson(locale))
                         .apply()

    }


    fun getLanguage(context: Context): Locale {
        val defaultLocale = Locale(Constants.arabic_ar)
        val language = PreferenceManager.getDefaultSharedPreferences(context)
                                        .getString(PREF_LANGUAGE, Gson().toJson(defaultLocale))
        return Gson().fromJson(language, Locale::class.java)
    }


    fun applyLanguage(context: Context, locale: Locale): Context {
        val newContext: Context
        val res = context.resources
        val config = Configuration(res.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        newContext = context.createConfigurationContext(config)
        return newContext
    }

    fun isArabic(context: Context): Boolean {
        return getLanguage(context).language.equals(Constants.arabic_ar, ignoreCase = true)
    }


    fun sendersInitiated(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_INIT_APP, false)
    }

     fun setSendersInitiated(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_INIT_APP, true).apply()
    }



    fun setFilterTimePeriod(context: Context, timePeriodId:Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_FILTER_PERIOD, timePeriodId).apply()
    }


    fun getFilterTimePeriod(context: Context):Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_FILTER_PERIOD, 5)
    }






}

enum class WordsType {
    INCOME_WORDS, EXPENSES_WORDS, BANKS_WORDS, CURRENCY_WORDS
}

enum class AppTheme(val id:Int){
    Light(1),Dark(2), System(0);
    companion object{
        fun getThemById(id:Int): AppTheme {
            return when(id){
                1-> Light
                2-> Dark
                0-> System
                else -> System
            }
        }
    }
}


