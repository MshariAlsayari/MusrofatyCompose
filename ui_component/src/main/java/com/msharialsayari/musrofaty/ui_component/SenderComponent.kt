package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp


@Composable
fun SenderComponent(model: SenderComponentModel){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(id = R.dimen.default_margin16)
            ),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TextComponent.HeaderText(text = model.displayName)
            TextComponent.PlaceholderText(text = model.senderType)
        }



    }

}



data class SenderComponentModel(
    var senderName:String="",
    var displayName:String="",
    var senderType:String="",
)


