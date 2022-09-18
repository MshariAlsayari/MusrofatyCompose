package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object DividerComponent {

    @Composable
    fun HorizontalDividerComponent(){
        Divider(modifier = Modifier.fillMaxWidth().width(1.dp))
    }
}