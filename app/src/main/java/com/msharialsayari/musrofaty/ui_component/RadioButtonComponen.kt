package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonComponent (modifier: Modifier=Modifier, text:String, isSelected:Boolean = false,onSelected:(String)->Unit){
    Row(
        modifier
            .fillMaxWidth()
            .selectable(selected = (isSelected), onClick = {
                onSelected(text)
            })
            .padding(horizontal = 16.dp)
    ) {
        RadioButton(
            selected = (isSelected),
            onClick = {         onSelected(text) }
        )
        TextComponent.BodyText(
            text = text,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}