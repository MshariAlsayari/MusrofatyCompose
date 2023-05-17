package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object DividerComponent {

    @Composable
    fun HorizontalDividerComponent(modifier: Modifier=Modifier){
        Divider(modifier = modifier.fillMaxWidth().width(1.dp))
    }

    @Composable
    fun VerticalDividerComponent(modifier: Modifier=Modifier){
        Divider(modifier = modifier.fillMaxHeight().width(1.dp))
    }
}