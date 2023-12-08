package com.msharialsayari.musrofaty.ui.screens.settings_screen

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.msharialsayari.musrofaty.BuildConfig
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.AddSenderUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFlowSendersUserCase
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSendersUseCase: GetFlowSendersUserCase,
    private val navigator: AppNavigator,
): ViewModel() {


    fun navigateToAppearance(){
        navigator.navigate(Screen.AppearanceScreen.route)

    }

    fun navigateToStores(){
        navigator.navigate(Screen.StoresScreen.route)

    }

    fun navigateToAnalysis(){
        navigator.navigate(Screen.SmsAnalysisScreen.route)

    }

    fun navigateToCategoryScreen(id:Int){
        navigator.navigate(Screen.CategoryScreen.route + "/${id}")

    }

    fun navigateToStoreSmsListScreen(id:String){
        navigator.navigate(Screen.StoreSmsListScreen.route + "/${id}")
    }

    fun onClickOnUpdatePreference(activity: Activity){
        var packageName =  activity.packageName
        if (BuildConfig.DEBUG) {
            var pk =""
            packageName.split(".")
                .mapIndexed { index, s ->
                    when (index) {
                        0 -> pk += "$s."
                        1 -> pk += "$s."
                        2 -> pk += s
                    }
                }

            packageName = pk
        }

        try {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            activity. startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }

    }
}