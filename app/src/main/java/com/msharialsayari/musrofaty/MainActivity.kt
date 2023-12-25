package com.msharialsayari.musrofaty


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.msharialsayari.musrofaty.Utils.getScreenSize
import com.msharialsayari.musrofaty.jobs.InitAppJob
import com.msharialsayari.musrofaty.jobs.InitCategoriesJob
import com.msharialsayari.musrofaty.jobs.InitStoresJob
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.MainScreenView
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.DialogsUtils
import com.msharialsayari.musrofaty.utils.SetStatusBarColor
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.getScreenTypeByWidth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


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
        initAppCheckFirebase()
        initJobs()
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
        initStoresJob()
    }

    private fun initAppJob() {
        val initAppWorker = OneTimeWorkRequestBuilder<InitAppJob>().build()
        WorkManager.getInstance(this).enqueue(initAppWorker)
    }

    private fun initCategoriesJob() {
        val initCategoriesWorker = OneTimeWorkRequestBuilder<InitCategoriesJob>().build()
        WorkManager.getInstance(this).enqueue(initCategoriesWorker)
    }

    private fun initStoresJob() {
        val initStoresWorker = OneTimeWorkRequestBuilder<InitStoresJob>().build()
        WorkManager.getInstance(this).enqueue(initStoresWorker)
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





