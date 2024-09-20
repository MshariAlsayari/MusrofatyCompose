package com.msharialsayari.musrofaty.ui.screens.settings_screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.screens.appearance_screen.AppearanceContent
import com.msharialsayari.musrofaty.ui.screens.sms_analysis_screen.SmsAnalysisContent
import com.msharialsayari.musrofaty.ui.screens.sms_detector_tool_screen.SmsDetectorToolContent
import com.msharialsayari.musrofaty.ui.screens.sms_types_screen.SmsTypesContent
import com.msharialsayari.musrofaty.ui.screens.statistics_screen.StatisticsScreen
import com.msharialsayari.musrofaty.ui.screens.stores_screen.StoresScreen
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.ListDetails
import com.msharialsayari.musrofaty.ui_component.PlaceHolder
import com.msharialsayari.musrofaty.utils.findActivity

@Composable
fun SettingsScreen() {

    val screenType = MusrofatyTheme.screenType
    val viewModel: SettingsViewModel = hiltViewModel()
    val context = LocalContext.current
    val activity = context.findActivity()

    if (screenType.isScreenWithDetails) {
        SettingScreeLandscape(viewModel)
    } else {
        SettingScreenPortrait(
            onAppearanceClicked = { viewModel.navigateToAppearance() },
            onStoresClicked = { viewModel.navigateToStores() },
            onAnalysisClicked = { viewModel.navigateToAnalysis() },
            onStatisticsClicked = { viewModel.navigateToStatistics() },
            onUpdateClicked = { viewModel.onClickOnUpdatePreference(activity) },
            onSmsTypesClicked = { viewModel.navigateToSmsTypesScreen() },
            onSmsToolDetectorClicked = { viewModel.navigateToSmsToolDetectorScreen() }
        )
    }


}

@Composable
private fun SettingScreenPortrait(
    onAppearanceClicked: () -> Unit,
    onStoresClicked: () -> Unit,
    onAnalysisClicked: () -> Unit,
    onStatisticsClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
    onSmsTypesClicked: () -> Unit,
    onSmsToolDetectorClicked: () -> Unit,
) {

    Scaffold(topBar = {
        AppBarComponent.TopBarComponent(
            title = BottomNavItem.Setting.title,
            isParent = true
        )
    }) { innerPadding ->
        SettingsContent(
            modifier = Modifier.padding(innerPadding),
            onClick = {
                when (it) {
                    PreferenceListEnum.Appearance -> onAppearanceClicked()
                    PreferenceListEnum.Stores -> onStoresClicked()
                    PreferenceListEnum.Analysis -> onAnalysisClicked()
                    PreferenceListEnum.Update -> onUpdateClicked()
                    PreferenceListEnum.Statistics -> onStatisticsClicked()
                    PreferenceListEnum.SmsTypes -> onSmsTypesClicked()
                    PreferenceListEnum.SmsTool -> onSmsToolDetectorClicked()
                }
            }
        )

    }

}

@Composable
private fun SettingScreeLandscape(
    viewModel: SettingsViewModel
) {

    val context = LocalContext.current
    val activity = context.findActivity()

    val selectedPreference: MutableState<PreferenceListEnum?> =
        rememberSaveable { mutableStateOf(null) }


    ListDetails(
        primaryRatio = 1F,
        secondaryRatio = 2F,
        primaryContent = {
            SettingsContent(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    selectedPreference.value = it
                }
            )
        },
        secondaryContent = {
            when (selectedPreference.value) {
                PreferenceListEnum.Appearance -> AppearanceContent()
                PreferenceListEnum.Stores -> StoresScreen()
                PreferenceListEnum.Analysis -> SmsAnalysisContent(isSmsAnalysisScreen = true)
                PreferenceListEnum.SmsTypes -> SmsAnalysisContent(isSmsAnalysisScreen = false)
                PreferenceListEnum.Statistics -> StatisticsScreen()
                PreferenceListEnum.SmsTool -> SmsDetectorToolContent()
                PreferenceListEnum.Update -> viewModel.onClickOnUpdatePreference(activity)
                else -> PlaceHolder.ScreenPlaceHolder()
            }
        })

}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    onClick: (PreferenceListEnum) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.default_margin16)),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = rememberLazyListState(),
    ) {

        item {
            PreferenceList(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onClick(it)
                })
        }
        item {
            VersionInfo(
                Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
        }

    }
}