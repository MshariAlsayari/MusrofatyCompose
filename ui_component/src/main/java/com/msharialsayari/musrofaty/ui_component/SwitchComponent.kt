package com.msharialsayari.musrofaty.ui_component


import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchColors
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable


@Composable
fun SwitchComponent(checked: Boolean = false, onChecked: (Boolean) -> Unit) {
    Switch(
        checked = checked,
        onCheckedChange ={onChecked(it)},
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.secondary,
            checkedTrackColor = MaterialTheme.colors.secondary,
        )
    )

}