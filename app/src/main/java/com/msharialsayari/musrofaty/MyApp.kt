package com.msharialsayari.musrofaty

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()




    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(SharedPreferenceManager.applyLanguage(newBase, SharedPreferenceManager.getLanguage(newBase)))
    }


}