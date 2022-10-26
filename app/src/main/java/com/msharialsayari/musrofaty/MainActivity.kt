package com.msharialsayari.musrofaty


import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.msharialsayari.musrofaty.jobs.InitAppJob

import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph
import com.msharialsayari.musrofaty.ui.theme.isLightTheme
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint

import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppJob(this)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            uiState.currentLocale?.let { SetLanguage(activity = this, locale = it) }
            SetStatusAndNavigationBarColor(this, uiState.currentTheme)
            MusrofatyComposeTheme(appTheme = uiState.currentTheme) {
                MainScreenView(
                    this,
                    onLanguageChanged = {
                        viewModel.updateLanguage()
                    },
                    onThemeChanged = {
                        viewModel.updateTheme()

                    }
                )
            }
        }


    }

}

private fun initAppJob(context: Context){
    if (SharedPreferenceManager.isFirstLunch(context)) {
        val workManager = WorkManager.getInstance(context)
        val initAppJob: WorkRequest =
            OneTimeWorkRequestBuilder<InitAppJob>()
                .build()
        workManager.enqueue(initAppJob)
        SharedPreferenceManager.setFirstLunch(context)
    }
}

@Composable
private fun SetStatusAndNavigationBarColor(activity: MainActivity, theme : AppTheme){


    activity.window.statusBarColor = if (isLightTheme(appTheme = theme)) {
        ContextCompat.getColor(activity, R.color.toolbar_background_color)

    } else {
        ContextCompat.getColor(activity, R.color.black_onyx)
    }



    if (isLightTheme(appTheme = theme)) {
        val view: View = activity.window.decorView
        view.systemUiVisibility = view.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        val view: View = activity.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }

    activity.window.navigationBarColor = if (isLightTheme(appTheme = theme)) {
        ContextCompat.getColor(activity, R.color.white)
    } else {
        ContextCompat.getColor(activity, R.color.black)
    }



}



@Composable
private fun SetLanguage(activity: MainActivity, locale: Locale) {
    val configuration = LocalConfiguration.current
    Locale.setDefault(locale)
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.configuration.setLayoutDirection(locale)
    activity.window.decorView.layoutDirection = resources.configuration.layoutDirection
    resources.updateConfiguration(configuration, resources.displayMetrics)
}


@Composable
fun MainScreenView(
    activity: MainActivity,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit
) {
    val context = LocalContext.current
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navController = rememberNavController()
    val bottomNavigationItems = if (!SharedPreferenceManager.isArabic(context)) {
        listOf(
            BottomNavItem.Dashboard,
            BottomNavItem.SendersList,
            BottomNavItem.Setting
        )

    }else{
        listOf(
            BottomNavItem.Setting,
            BottomNavItem.SendersList,
            BottomNavItem.Dashboard,
        )
    }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        BottomNavItem.Dashboard.route -> {
            bottomBarState.value   = true
        }
        BottomNavItem.SendersList.route -> {
            bottomBarState.value   = true
        }
        BottomNavItem.Setting.route -> {
            bottomBarState.value = true
        }

        else -> {
            bottomBarState.value = false

        }

    }


    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController,
                items = bottomNavigationItems,
                bottomBarState = bottomBarState
            )
        },

    ) { innerPadding ->
        NavigationGraph(
            activity = activity,
            navController = navController,
            innerPadding = innerPadding,
            onLanguageChanged = onLanguageChanged,
            onThemeChanged = onThemeChanged
        )
    }
}


