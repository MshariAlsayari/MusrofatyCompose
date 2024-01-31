package com.msharialsayari.musrofaty.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import java.util.Locale


object SharedPreferenceManager {


    private const val PREF_LANGUAGE                                        = "key_preferredLang_ned"
    private const val PREF_THEME                                           = "PREF_THEME"
    private const val PREF_INIT_APP                                        = "PREF_INIT_APP"
    private const val PREF_FILTER_PERIOD = "PREF_FILTER_PERIOD"
    private const val PREF_INIT_TRANSFER_JOB = "PREF_INIT_TRANSFER_JOB"
    private const val PREF_NEW_WORD_DETECTOR = "PREF_NEW_WORD_DETECTOR"

    fun storeTheme(context: Context, theme: Theme) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(PREF_THEME,theme.id)
            .apply()

    }

    fun getTheme(context: Context):Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_THEME, Theme.DEFAULT.id)
    }



    fun storeLanguage(context: Context, language: Language) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putInt(PREF_LANGUAGE, language.id)
                         .apply()

    }


    fun getLanguage(context: Context): Language {
        val language = PreferenceManager.getDefaultSharedPreferences(context)
                                        .getInt(PREF_LANGUAGE, Language.DEFAULT.id)
        return Language.getLanguageById(language)
    }




    fun isArabic(context: Context): Boolean {
        val lang = getLanguage(context)
        return if(lang == Language.DEFAULT){
            Locale.getDefault().language.equals(Constants.arabic_ar, ignoreCase = true)
        }else{
            lang == Language.ARABIC
        }
    }


    fun sendersInitiated(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_INIT_APP, false)
    }

     fun setSendersInitiated(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_INIT_APP, true).apply()
    }

    fun transferJobInitiated(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            PREF_INIT_TRANSFER_JOB, false)
    }

    fun setTransferJobInitiated(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_INIT_TRANSFER_JOB, true).apply()
    }



    fun setFilterTimePeriod(context: Context, timePeriodId:Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_FILTER_PERIOD, timePeriodId).apply()
    }


    fun getFilterTimePeriod(context: Context):Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_FILTER_PERIOD, 5)
    }

    fun newWordDetectors(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            PREF_NEW_WORD_DETECTOR, false)
    }

    fun setNewWordDetectors(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_NEW_WORD_DETECTOR, true).apply()
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


