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
    private const val PREF_INCOME_WORDS                           = "PREF_INCOME_WORDS"
    private const val PREF_BANKS                                  = "PREF_BANKS"
    private const val PREF_CURRENCY                               = "PREF_CURRENCY"
    private const val PREF_EXPENSES_WORDS                         = "PREF_EXPENSES_WORDS"
    private const val PREF_SHOW_TOOLTIPS                          = "PREF_SHOW_TOOLTIPS"
    private const val PREF_ACTIVITY_TRANSITION                    = "PREF_ACTIVITY_TRANSITION"
    private const val PREF_LANGUAGE_CHANGED                       = "PREF_LANGUAGE_CHANGED"
    private const val PREF_FIRST_TIME_OPEN_ADD_WORD_FRAGMENT      = "PREF_FIRST_TIME_OPEN_ADD_WORD_FRAGMENT"
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

    fun isDefaultCategoryListInserted(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(PREF_IS_DEFAULT_LIST_CATEGORY_INSERTED, false)
    }

    fun setDefaultCategoryListInserted(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putBoolean(PREF_IS_DEFAULT_LIST_CATEGORY_INSERTED, true)
                         .apply()
    }

    fun getWordsList(context: Context, wordsType: WordsType): MutableList<String> {
        val wType = when (wordsType) {
            WordsType.INCOME_WORDS -> PREF_INCOME_WORDS
            WordsType.EXPENSES_WORDS -> PREF_EXPENSES_WORDS
            WordsType.BANKS_WORDS -> PREF_BANKS
            WordsType.CURRENCY_WORDS -> PREF_CURRENCY
        }
        val gson = Gson()
        val wordsList: List<String>
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val jsonPreferences = sharedPref.getString(wType, "")
        val type: Type = object : TypeToken<List<String>>() {}.type
        wordsList = gson.fromJson(jsonPreferences, type) ?: mutableListOf()
        return wordsList.toMutableList()

    }

    fun saveArrayList(context: Context, list: List<String>, wordsType: WordsType) {
        val wType = when (wordsType) {
            WordsType.INCOME_WORDS -> PREF_INCOME_WORDS
            WordsType.EXPENSES_WORDS -> PREF_EXPENSES_WORDS
            WordsType.BANKS_WORDS -> PREF_BANKS
            WordsType.CURRENCY_WORDS -> PREF_CURRENCY
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list.toSet().toList())
        editor.putString(wType, json)
        editor.apply()

        setDefaultSmsWordsChanged(context, wordsType)
    }

    fun isDefaultSmsWordsListChanged(context: Context, wordsType: WordsType): Boolean {
        val listType = when (wordsType) {
            WordsType.INCOME_WORDS -> PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED
            WordsType.EXPENSES_WORDS -> PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED
            WordsType.BANKS_WORDS -> PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED
            WordsType.CURRENCY_WORDS -> PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED
        }
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(listType, false)
    }

    private fun setDefaultSmsWordsChanged(
        context: Context,
        wordsType: WordsType,
        isChanged: Boolean = true
    ) {

        val key = when (wordsType) {
            WordsType.INCOME_WORDS -> PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED
            WordsType.EXPENSES_WORDS -> PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED
            WordsType.BANKS_WORDS -> PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED
            WordsType.CURRENCY_WORDS -> PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, isChanged)
            .apply()
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

    fun getFirstTimeOPenAddWordFragment(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                                .getBoolean(PREF_FIRST_TIME_OPEN_ADD_WORD_FRAGMENT, true)
    }

    fun setFirstTimeOPenAddWordFragment(context: Context, should: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
                         .edit()
                         .putBoolean(PREF_FIRST_TIME_OPEN_ADD_WORD_FRAGMENT, should)
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


enum class WordsType {
    INCOME_WORDS, EXPENSES_WORDS, BANKS_WORDS, CURRENCY_WORDS
}