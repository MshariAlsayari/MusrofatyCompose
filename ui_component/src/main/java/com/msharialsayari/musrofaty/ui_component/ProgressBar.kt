package com.msharialsayari.musrofaty.ui_component

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object ProgressBar {


    @Composable
    fun CircleProgressBar(){
        CircularProgressIndicator(color = MaterialTheme.colors.secondary)

    }
}