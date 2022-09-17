package com.msharialsayari.musrofaty.ui.screens.settings_screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.RowComponent

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_settings,
            header = stringResource(id = R.string.pref_appearance_title),
            body = stringResource(id = R.string.pref_appearance_summary),
        )

        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_performance,
            header = stringResource(id = R.string.pref_sms_title),
            body = stringResource(id = R.string.pref_sms_summary),
        )
    }
}