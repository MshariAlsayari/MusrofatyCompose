package com.msharialsayari.musrofaty.ui.screens.settings_screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msharialsayari.musrofaty.BuildConfig
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.RowComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent

@Composable
fun SettingsScreen(onAppearanceClicked:()->Unit,onSendersClicked:()->Unit, onStoresClicked:()->Unit,onAnalysisClicked:()->Unit,onUpdateClicked:()->Unit) {



    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = BottomNavItem.Setting.title,
                isParent = true
            )

        }
    ) { innerPadding ->

        Column (Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {


            Column {
                RowComponent.PreferenceRow(
                    iconId = R.drawable.ic_settings,
                    header = stringResource(id = R.string.pref_appearance_title),
                    body = stringResource(id = R.string.pref_appearance_summary),
                    onClick = onAppearanceClicked
                )

                RowComponent.PreferenceRow(
                    iconId = R.drawable.ic_stores,
                    header = stringResource(id = R.string.pref_managment_stores_title),
                    body = stringResource(id = R.string.pref_managment_stores_summary),
                    onClick = onStoresClicked
                )

                RowComponent.PreferenceRow(
                    iconId = R.drawable.ic_analytics,
                    header = stringResource(id = R.string.pref_managment_analysis_title),
                    body = stringResource(id = R.string.pref_managment_analysis_summary),
                    onClick = onAnalysisClicked
                )

                RowComponent.PreferenceRow(
                    iconId = R.drawable.ic_update,
                    header = stringResource(id = R.string.pref_managment_update_title),
                    body = stringResource(id = R.string.pref_managment_update_summary),
                    onClick = onUpdateClicked
                )

            }

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextComponent.PlaceholderText(text = stringResource(id = R.string.app_version), alignment = TextAlign.Start)
                    TextComponent.PlaceholderText(text = BuildConfig.VERSION_NAME, alignment = TextAlign.Center)
                }
                
            }
        }

    }

}