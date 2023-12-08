package com.msharialsayari.musrofaty.ui.screens.settings_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msharialsayari.musrofaty.BuildConfig
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.TextComponent


@Composable
fun VersionInfo(modifier: Modifier = Modifier){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextComponent.PlaceholderText(
            text = stringResource(id = R.string.app_version),
            alignment = TextAlign.Start
        )
        TextComponent.PlaceholderText(
            text = BuildConfig.VERSION_NAME,
            alignment = TextAlign.Center
        )
    }

}