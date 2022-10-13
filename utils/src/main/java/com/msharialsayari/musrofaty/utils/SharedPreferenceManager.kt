package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Configuration
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import java.util.*


object SharedPreferenceManager {


    private const val PREF_LANGUAGE                                        = "key_preferredLang"
    private const val PREF_THEME                                           = "PREF_THEME"
    private const val PREF_First_Lunched                                   = "PREF_First_Lunched"
    private const val PREF_SHOULD_MIGRATE_FOR_FILTERS                      = "PREF_SHOULD_MIGRATE_FOR_FILTERS"


    private const val PREF_First_Lunched_Category                          = "PREF_First_Lunched_Category"

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
        val defaultLocale = Locale("ar")
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
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_First_Lunched_Category, isChanged).apply()
    }



}

enum class AppTheme(val id:Int){
    Light(1),Dark(2), System(0);
    companion object{
        fun getThemById(id:Int):AppTheme{
            return when(id){
                1->Light
                2->Dark
                0->System
                else -> throw Exception("The theme is not supported")
            }
        }
    }
}


