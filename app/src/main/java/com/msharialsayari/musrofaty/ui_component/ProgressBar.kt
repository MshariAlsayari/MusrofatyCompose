package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.msharialsayari.musrofaty.R

object ProgressBar {

    @Composable
    fun CircleProgressBar(modifier: Modifier=Modifier){
         CircularProgressIndicator(
             modifier = modifier,
             color = colorResource(id = R.color.active_color)
         )
    }
}