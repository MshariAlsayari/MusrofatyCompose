package com.msharialsayari.musrofaty


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.msharialsayari.musrofaty.Utils.getScreenSize
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorViewModel
import com.msharialsayari.musrofaty.ui.MainScreenView
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.utils.DialogsUtils
import com.msharialsayari.musrofaty.utils.getScreenTypeByWidth
import dagger.hilt.android.AndroidEntryPoint


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

                //SetStatusBarColor()
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





