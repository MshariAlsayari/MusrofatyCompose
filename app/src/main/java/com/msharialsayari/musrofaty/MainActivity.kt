package com.msharialsayari.musrofaty


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.msharialsayari.musrofaty.utils.DialogsUtils
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


    private val openSettingRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (isPermissionGranted()) {
                runApp()
            }else{
                navigationToAppSetting(getSettingIntent())
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initJobs()
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

        when {
            isPermissionGranted() -> runApp()
            shouldShowRequestPermissionRationale() -> {
                DialogsUtils.showDialog(
                    this, DialogsUtils.Params(
                        title = R.string.sms_permission_rational_dialog_title,
                        message = R.string.sms_permission_rational_dialog_message,
                        positiveBtnText = android.R.string.ok,
                        positiveBtnListener = {
                            askPermission()
                        }
                    )
                )

            }

            else -> {
                DialogsUtils.showDialog(this, DialogsUtils.Params(
                    title = R.string.sms_permission_denied_dialog_title,
                    message = R.string.sms_permission_denied_dialog_message,
                    positiveBtnText = R.string.permission_dialog_positive_button,
                    positiveBtnListener = {
                        navigationToAppSetting(getSettingIntent())
                    }
                ))

            }
        }


    }


    private fun navigationToAppSetting(intent: Intent) {
        openSettingRequest.launch(intent)
    }

    private fun getSettingIntent(): Intent {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        return intent
    }

    private fun runApp() {
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
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
                    navigatorViewModel =navigatorViewModel,
                    screenType = screenType
                )
            }
        }
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun shouldShowRequestPermissionRationale(): Boolean {
        return shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this,
            listOf(Manifest.permission.READ_SMS).toTypedArray(),
            123
        )
    }

}





