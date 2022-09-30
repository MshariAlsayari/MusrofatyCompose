package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.yariksoffice.lingver.Lingver
import java.util.*


object SharedPreferenceManager {


    private const val PREF_LANGUAGE                               = "key_preferredLang"
    private const val PREF_LANGUAGE_CHANGED                       = "PREF_LANGUAGE_CHANGED"
    private const val PREF_First_Lunched                          = "PREF_First_Lunched"


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


    fun isFirstLunch(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_First_Lunched, true)
    }

     fun setFirstLunch(
        context: Context,
        isChanged: Boolean = false
    ) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_First_Lunched, isChanged).apply()
    }

}


