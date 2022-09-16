package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


object SharedPreferenceManager {

    private const val PREF_THEME                                  = "PREF_THEME"
    private const val PREF_LANGUAGE                               = "key_preferredLang"
    private const val PREF_SHOW_TOOLTIPS                          = "PREF_SHOW_TOOLTIPS"
    private const val PREF_ACTIVITY_TRANSITION                    = "PREF_ACTIVITY_TRANSITION"
    private const val PREF_LANGUAGE_CHANGED                       = "PREF_LANGUAGE_CHANGED"
    private const val PREF_INSERT_DEFAULT_WORDS                   = "PREF_INSERT_DEFAULT_WORDS"
    private const val PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED   = "PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED    = "PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_CATEGORY_INSERTED      = "PREF_IS_DEFAULT_LIST_CATEGORY_INSERTED"

    fun storeLanguage(context: Context, locale: Locale) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putString(PREF_LANGUAGE, Gson().toJson(locale))
                         .apply()
        setLanguageChanged(context)
    }


    fun isLanguageChanged(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(PREF_LANGUAGE_CHANGED, false)

    }

    private fun setLanguageChanged(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putBoolean(PREF_LANGUAGE_CHANGED, true)
                         .apply()
    }

    fun getLanguage(context: Context): Locale {
        val defaultLocale: Locale = Resources.getSystem().configuration.locales[0]
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


    fun isDefaultSmsWordsListChanged(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(PREF_INSERT_DEFAULT_WORDS, false)
    }

     fun setDefaultSmsWordsChanged(
        context: Context,
        isChanged: Boolean = true
    ) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_INSERT_DEFAULT_WORDS, isChanged).apply()
    }

    fun getShouldShowTooltips(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(PREF_SHOW_TOOLTIPS, true)
    }

    fun setShouldShowTooltips(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putBoolean(PREF_SHOW_TOOLTIPS, false)
                         .apply()
    }

    fun shouldUseDifferentActivityTransition(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(PREF_ACTIVITY_TRANSITION, false)
    }

    fun setShouldUseDifferentActivityTransition(context: Context, should: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putBoolean(PREF_ACTIVITY_TRANSITION, should)
                         .apply()
    }



    fun setTheme(context: Context, theme: Int) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putInt(PREF_THEME, theme)
                         .apply()
    }

    fun getCurrentTheme(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getInt(PREF_THEME, -99)
    }

    fun isDarkThem(context: Context): Boolean {
        return true
    }
}


