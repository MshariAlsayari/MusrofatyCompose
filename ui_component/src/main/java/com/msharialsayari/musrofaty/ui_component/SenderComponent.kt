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
            .height(120.dp)
            .padding(
                dimensionResource(id = R.dimen.default_margin16)
            ),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            TextComponent.HeaderText(text = model.displayName)
            TextComponent.PlaceholderText(text = model.senderType)
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextComponent.HeaderText(text = model.smsTotal.toString())
            TextComponent.HeaderText(text = "SMS")
        }





    }

}



data class SenderComponentModel(
    var senderName:String="",
    var displayName:String="",
    var senderType:String="",
    var smsTotal:Int=0,
)


