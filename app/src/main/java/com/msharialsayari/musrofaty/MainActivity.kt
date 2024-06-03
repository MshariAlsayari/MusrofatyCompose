package com.msharialsayari.musrofaty


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Observer
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.Utils.getScreenSize
import com.msharialsayari.musrofaty.jobs.InitAppJob
import com.msharialsayari.musrofaty.jobs.InitCategoriesFirebaseJob
import com.msharialsayari.musrofaty.jobs.InitFiltersJob
import com.msharialsayari.musrofaty.jobs.InitNewWordDetectorJob
import com.msharialsayari.musrofaty.jobs.InitStoresFirebaseJob
import com.msharialsayari.musrofaty.jobs.InitTransferWordsJob
import com.msharialsayari.musrofaty.jobs.InitWithdrawalWordsJob
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.MainScreenView
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.utils.SetStatusBarColor
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.getScreenTypeByWidth
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


    private val viewModel: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initJobs()
        runApp()
    }

    private fun initJobs() {
        if (!SharedPreferenceManager.sendersInitiated(this)) {
            initAppJob()
        }

        if (!SharedPreferenceManager.transferJobInitiated(this)) {
            initTransferSmsJob()
        }

        if (!SharedPreferenceManager.newWordDetectors(this)) {
            initNewWordDetectorJob()
        }

        if (!SharedPreferenceManager.withdrawalATMWords(this)) {
            initWithdrawalATMWordsJob()
        }

        if (!SharedPreferenceManager.initFilters(this)) {
            initFiltersJob()
        }
        initCategoriesJob()
        initFirebaseStoresJob()
    }

    private fun initAppJob() {
        val worker = OneTimeWorkRequestBuilder<InitAppJob>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d(TAG, "initAppJob() state: SUCCEEDED")
                SharedPreferenceManager.setSendersInitiated(this)
            }
        })

    }

    private fun initTransferSmsJob() {
        val worker = OneTimeWorkRequestBuilder<InitTransferWordsJob>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d(TAG, "initTransferSmsJob() state: SUCCEEDED")
                SharedPreferenceManager.setTransferJobInitiated(this)
            }
        })
    }

    private fun initNewWordDetectorJob() {
        val worker = OneTimeWorkRequestBuilder<InitNewWordDetectorJob>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d(TAG, "initNewWordDetectorJob() state: SUCCEEDED")
                SharedPreferenceManager.setNewWordDetectors(this)
            }
        })
    }

    private fun initWithdrawalATMWordsJob() {
        val worker = OneTimeWorkRequestBuilder<InitWithdrawalWordsJob>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d(TAG, "initWithdrawalATMWordsJob() state: SUCCEEDED")
                SharedPreferenceManager.setWithdrawalATMWords(this)
            }
        })
    }

    private fun initFiltersJob() {
        val worker = OneTimeWorkRequestBuilder<InitFiltersJob>()
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d(TAG, "initFiltersJob() state: SUCCEEDED")
                SharedPreferenceManager.setInitFilters(this)
            }
        })
    }

    private fun initCategoriesJob() {
        val initCategoriesWorker = OneTimeWorkRequestBuilder<InitCategoriesFirebaseJob>().build()
        WorkManager.getInstance(this).enqueue(initCategoriesWorker)
    }

    private fun initFirebaseStoresJob() {
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresFirebaseJob>().build()
        WorkManager.getInstance(this).enqueue(initStoresWorker)
    }


    override fun onResume() {
        super.onResume()
        viewModel.observeSmsPermission()
    }


    private fun runApp() {
        setContent {
            val navigatorViewModel: AppNavigatorViewModel by viewModels()
            val uiState by viewModel.uiState.collectAsState()
            val windowSizeClass = getScreenSize(this)
            val screenType = windowSizeClass.getScreenTypeByWidth()
            MusrofatyComposeTheme(
                appTheme = uiState.appAppearance,
                appLanguage = uiState.appLanguage,
                screenType =screenType
            ) {

                SetStatusBarColor()
                MainScreenView(
                    mainViewModel = viewModel,
                    navigatorViewModel =navigatorViewModel,
                    screenType = screenType
                )
            }
        }
    }
}





