package com.msharialsayari.musrofaty

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.msharialsayari.musrofaty.jobs.InitAppJob
import com.msharialsayari.musrofaty.jobs.InitCategoriesFirebaseJob
import com.msharialsayari.musrofaty.jobs.InitStoresFirebaseJob
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    companion object {
        private val TAG = MyApp::class.java.simpleName
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        initAppCheckFirebase()
        initJobs()
        initUpdateAppWidgetJobWorker()

    }

    private fun initAppCheckFirebase() {
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }

    private fun initJobs() {
        if (!SharedPreferenceManager.sendersInitiated(this)) {
            initAppJob()
            SharedPreferenceManager.setSendersInitiated(this)
        }
        initCategoriesJob()
        initFirebaseStoresJob()
    }

    private fun initAppJob() {
        val initAppWorker = OneTimeWorkRequestBuilder<InitAppJob>().build()
        WorkManager.getInstance(this).enqueue(initAppWorker)
    }

    private fun initCategoriesJob() {
        val initCategoriesWorker = OneTimeWorkRequestBuilder<InitCategoriesFirebaseJob>().build()
        WorkManager.getInstance(this).enqueue(initCategoriesWorker)
    }

    private fun initFirebaseStoresJob() {
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresFirebaseJob>().build()
        WorkManager.getInstance(this).enqueue(initStoresWorker)
    }


    private fun initUpdateAppWidgetJobWorker(){
        val workRequest = PeriodicWorkRequest.Builder(UpdateAppWidgetJob::class.java, 1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)

    }

    private fun calculateInitialDelay(): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0) // Set the hour to 12:00 a.m.
        calendar.set(Calendar.MINUTE, 0) // Set the minute to 0
        calendar.set(Calendar.SECOND, 0) // Set the second to 0
        val currentTimeMillis = System.currentTimeMillis()
        val scheduledTimeMillis: Long = calendar.timeInMillis

        // Calculate the initial delay to reach 12:00 a.m.
        val initialDelayMillis = scheduledTimeMillis - currentTimeMillis


        Log.d(TAG, "calculateInitialDelay() initialDelayMillis: $initialDelayMillis")
        return initialDelayMillis
    }
}