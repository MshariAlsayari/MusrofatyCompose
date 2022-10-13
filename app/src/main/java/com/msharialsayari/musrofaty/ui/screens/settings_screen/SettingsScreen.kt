package com.msharialsayari.musrofaty.ui.screens.settings_screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.RowComponent

@Composable
fun SettingsScreen(onAppearanceClicked:()->Unit,onSendersClicked:()->Unit, onStoresClicked:()->Unit,onAnalysisClicked:()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_settings,
            header = stringResource(id = R.string.pref_appearance_title),
            body = stringResource(id = R.string.pref_appearance_summary),
            onClick = onAppearanceClicked
        )

        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_performance,
            header = stringResource(id = R.string.pref_managment_sender_title),
            body = stringResource(id = R.string.pref_managment_sender_summary),
            onClick = onSendersClicked
        )


        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_performance,
            header = stringResource(id = R.string.pref_managment_stores_title),
            body = stringResource(id = R.string.pref_managment_stores_summary),
            onClick = onStoresClicked
        )

        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_performance,
            header = stringResource(id = R.string.pref_managment_analysis_title),
            body = stringResource(id = R.string.pref_managment_analysis_summary),
            onClick = onAnalysisClicked
        )
    }
}