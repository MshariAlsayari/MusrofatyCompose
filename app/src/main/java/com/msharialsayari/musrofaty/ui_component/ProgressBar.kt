package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme

object ProgressBar {

    @Composable
    fun CircleProgressBar(modifier: Modifier=Modifier){
         CircularProgressIndicator(
             modifier = modifier,
             color = MusrofatyTheme.colors.activeColor
         )
    }
}