package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


object SharedPreferenceManager {


    private const val PREF_LANGUAGE                                        = "key_preferredLang"
    private const val PREF_THEME                                           = "PREF_THEME"
    private const val PREF_First_Lunched                                   = "PREF_First_Lunched"
    private const val PREF_First_Lunched_Category                          = "PREF_First_Lunched_Category"
    private const val PREF_INCOME_WORDS = "PREF_INCOME_WORDS"
    
    
    private const val PREF_BANKS = "PREF_BANKS"
    private const val PREF_CURRENCY = "PREF_CURRENCY"
    private const val PREF_EXPENSES_WORDS = "PREF_EXPENSES_WORDS"
    private const val PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_INCOME_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_EXPENSES_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_BANKS_WORDS_CHANGED"
    private const val PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED = "PREF_IS_DEFAULT_LIST_CURRENCY_WORDS_CHANGED"

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


    fun isFirstLunch(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_First_Lunched, true)
    }

     fun setFirstLunch(
        context: Context,
        isChanged: Boolean = false
    ) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_First_Lunched, isChanged).apply()
    }




     fun setFirstLunchCategory(
        context: Context,
        isChanged: Boolean = false
    ) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(
            PREF_First_Lunched_Category, isChanged).apply()
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

    fun addWordToList(context: Context, word: String, wordsType: WordsType) {
        val wType = when (wordsType) {
            WordsType.INCOME_WORDS -> PREF_INCOME_WORDS
            WordsType.EXPENSES_WORDS -> PREF_EXPENSES_WORDS
            WordsType.BANKS_WORDS -> PREF_BANKS
            WordsType.CURRENCY_WORDS -> PREF_CURRENCY
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val currentList = getWordsList(context,wordsType)
        currentList.add(word)
        val gson = Gson()
        val json = gson.toJson(currentList.toSet().toList())
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


