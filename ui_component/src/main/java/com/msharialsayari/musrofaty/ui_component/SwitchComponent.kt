package com.msharialsayari.musrofaty.ui_component


import androidx.compose.material.Switch
import androidx.compose.runtime.Composable


@Composable
fun SwitchComponent(checked: Boolean = false, onChecked: (Boolean) -> Unit) {
    Switch(checked = checked, onCheckedChange ={onChecked(it)})

}