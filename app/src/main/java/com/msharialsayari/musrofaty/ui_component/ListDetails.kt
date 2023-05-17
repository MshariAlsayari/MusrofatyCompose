package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ListDetails(
    primaryRatio:Float,
    secondaryRatio:Float,
    primaryContent: @Composable () -> Unit,
    secondaryContent: (@Composable () -> Unit) = { PlaceHolder.ScreenPlaceHolder() },
){

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(primaryRatio),
            contentAlignment = Alignment.Center) {
            primaryContent()
        }

        DividerComponent.VerticalDividerComponent()

        Box(modifier = Modifier.weight(secondaryRatio),
            contentAlignment = Alignment.Center) {
            secondaryContent()
        }

    }

}