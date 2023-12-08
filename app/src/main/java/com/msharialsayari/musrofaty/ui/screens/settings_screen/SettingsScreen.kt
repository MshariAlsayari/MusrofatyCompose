package com.msharialsayari.musrofaty.ui.screens.settings_screen


import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.BuildConfig
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.screens.appearance_screen.AppearanceContent
import com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen.SmsAnalysisContent
import com.msharialsayari.musrofaty.ui.screens.stores_screen.StoresScreen
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.ListDetails
import com.msharialsayari.musrofaty.ui_component.PlaceHolder
import com.msharialsayari.musrofaty.ui_component.TextComponent
import com.msharialsayari.musrofaty.utils.findActivity

@Composable
fun SettingsScreen(
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit
) {

    val screenType = MusrofatyTheme.screenType
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val activity = context.findActivity()

    if (screenType.isScreenWithDetails) {
        SettingScreeLandscape(
            viewModel,
            onLanguageChanged,
            onThemeChanged
        )
    } else {
        SettingScreePortrait(
            onAppearanceClicked = { viewModel.navigateToAppearance() },
            onStoresClicked = { viewModel.navigateToStores() },
            onAnalysisClicked = { viewModel.navigateToAnalysis() },
            onUpdateClicked = { viewModel.onClickOnUpdatePreference(activity) }
        )
    }


}

@Composable
private fun SettingScreePortrait(
    onAppearanceClicked: () -> Unit,
    onStoresClicked: () -> Unit,
    onAnalysisClicked: () -> Unit,
    onUpdateClicked: () -> Unit
) {

    Scaffold(topBar = {
        AppBarComponent.TopBarComponent(
            title = BottomNavItem.Setting.title,
            isParent = true
        )
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PreferenceList(onClick = {
                when (it) {
                    PreferenceListEnum.Appearance -> onAppearanceClicked()
                    PreferenceListEnum.Stores -> onStoresClicked()
                    PreferenceListEnum.Analysis -> onAnalysisClicked()
                    PreferenceListEnum.Update -> onUpdateClicked()
                }
            })

            VersionInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }

}

@Composable
private fun SettingScreeLandscape(
    viewModel: SettingsViewModel,
    onLanguageChanged: () -> Unit,
    onThemeChanged: () -> Unit,
) {

    val context = LocalContext.current
    val activity = context.findActivity()

    val selectedPreference: MutableState<PreferenceListEnum?> =
        rememberSaveable { mutableStateOf(null) }


    ListDetails(primaryRatio = 1F, secondaryRatio = 2F, primaryContent = {
        PreferenceList(onClick = {
            selectedPreference.value = it
        })
    }, secondaryContent = {
        when (selectedPreference.value) {
            PreferenceListEnum.Appearance -> AppearanceContent(
                onLanguageChanged = onLanguageChanged,
                onThemeChanged = onThemeChanged
            )

            PreferenceListEnum.Stores -> StoresScreen()
            PreferenceListEnum.Analysis -> SmsAnalysisContent()
            PreferenceListEnum.Update -> {
                viewModel.onClickOnUpdatePreference(activity)
            }

            else -> PlaceHolder.ScreenPlaceHolder()
        }
    })

}