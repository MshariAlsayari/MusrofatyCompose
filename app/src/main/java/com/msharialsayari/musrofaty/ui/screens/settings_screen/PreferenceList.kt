package com.msharialsayari.musrofaty.ui.screens.settings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.RowComponent

enum class PreferenceListEnum {
    Appearance,
    Stores,
    Analysis,
    Statistics,
    Update

}

@Composable
fun PreferenceList(modifier: Modifier =Modifier, onClick:(PreferenceListEnum)->Unit){
    Column (modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            RowComponent.PreferenceRow(
                iconId = R.drawable.ic_settings,
                header = stringResource(id = R.string.pref_appearance_title),
                body = stringResource(id = R.string.pref_appearance_summary),
                onClick = { onClick(PreferenceListEnum.Appearance) }
            )

            RowComponent.PreferenceRow(
                iconId = R.drawable.ic_stores,
                header = stringResource(id = R.string.pref_managment_stores_title),
                body = stringResource(id = R.string.pref_managment_stores_summary),
                onClick = { onClick(PreferenceListEnum.Stores) }
            )

            RowComponent.PreferenceRow(
                iconId = R.drawable.ic_analytics,
                header = stringResource(id = R.string.pref_managment_analysis_title),
                body = stringResource(id = R.string.pref_managment_analysis_summary),
                onClick = { onClick(PreferenceListEnum.Analysis) }
            )

        RowComponent.PreferenceRow(
            iconId = R.drawable.ic_update,
            header = stringResource(id = Screen.StatisticsScreen.title!!),
            body = stringResource(id = R.string.pref_managment_update_summary),
            onClick = { onClick(PreferenceListEnum.Statistics) }
        )

            RowComponent.PreferenceRow(
                iconId = R.drawable.ic_update,
                header = stringResource(id = R.string.pref_managment_update_title),
                body = stringResource(id = R.string.pref_managment_update_summary),
                onClick = { onClick(PreferenceListEnum.Update) }
            )
    }

}