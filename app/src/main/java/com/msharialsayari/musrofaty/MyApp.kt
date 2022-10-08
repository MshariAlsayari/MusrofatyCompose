package com.msharialsayari.musrofaty

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
        Lingver.getInstance().setLocale(this, Locale("ar"))
        SharedPreferenceManager.storeLanguage(this, Locale("ar"))

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SharedPreferenceManager.applyLanguage(newBase, SharedPreferenceManager.getLanguage(newBase)))
    }


}