package com.msharialsayari.musrofaty


import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msharialsayari.musrofaty.ui.navigation.BaseScreen

import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui.theme.IsLightTheme
import com.msharialsayari.musrofaty.ui.theme.MusrofatyComposeTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.utils.AppTheme
import dagger.hilt.android.AndroidEntryPoint

import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

@Composable
private fun SetStatusAndNavigationBarColor(activity: MainActivity, theme : AppTheme){


    activity.window.statusBarColor = if (IsLightTheme(appTheme = theme)) {
        ContextCompat.getColor(activity, R.color.white)

    } else {
        ContextCompat.getColor(activity, R.color.blackOnyx)
    }



    if (IsLightTheme(appTheme = theme)) {
        val view: View = activity.window.decorView
        view.systemUiVisibility = view.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        val view: View = activity.window.decorView
        view.systemUiVisibility = view.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }

    activity.window.navigationBarColor = if (IsLightTheme(appTheme = theme)) {
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
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val topBarState = rememberSaveable { (mutableStateOf(true)) }
    val screenTitleState = rememberSaveable { (mutableStateOf("")) }
    val navController = rememberNavController()
    val bottomNavigationItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.SendersList,
        BottomNavItem.Setting
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        BottomNavItem.Dashboard.route -> {
            bottomBarState.value   = true
            topBarState.value      = true
            screenTitleState.value = stringResource(id = BottomNavItem.Dashboard.title!!)
        }
        BottomNavItem.SendersList.route -> {
            bottomBarState.value   = true
            topBarState.value      = true
            screenTitleState.value = stringResource(id = BottomNavItem.SendersList.title!!)


        }
        BottomNavItem.Setting.route -> {
            bottomBarState.value = true
            topBarState.value = true
            screenTitleState.value = stringResource(id = BottomNavItem.Setting.title!!)
        }

        else -> {
            bottomBarState.value = false
            val route = navBackStackEntry?.destination?.route?.split("/")?.get(0) ?: ""
            if (BaseScreen.getScreenByRoute(route).title != null){
                topBarState.value = true
                screenTitleState.value = stringResource(id = BaseScreen.getScreenByRoute(route).title!!)
            }else{
                topBarState.value = false
                screenTitleState.value = ""
            }

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

        topBar = {
            AppBarComponent.TopBarComponent(
                title = screenTitleState.value,
                topBarState = topBarState
            )

        }
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


