package com.msharialsayari.musrofaty

import android.app.Application
import android.content.Context
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        Lingver.getInstance().setFollowSystemLocale(this)

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SharedPreferenceManager.applyLanguage(newBase, SharedPreferenceManager.getLanguage(newBase)))
    }


}