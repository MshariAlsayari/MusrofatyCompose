package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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

        if (model.senderIcon != null)
        Image(modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .background(color = Color.White),
            painter = painterResource(id = model.senderIcon!!),
            contentDescription = null)
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = dimensionResource(id = R.dimen.default_margin16))) {
            TextComponent.HeaderText(text = model.displayName)
            TextComponent.PlaceholderText(text = model.senderType)
        }



    }

}



data class SenderComponentModel(
    var senderId:Int,
    var senderName:String="",
    var displayName:String="",
    var senderType:String="",
    var senderIcon:Int?,
)


