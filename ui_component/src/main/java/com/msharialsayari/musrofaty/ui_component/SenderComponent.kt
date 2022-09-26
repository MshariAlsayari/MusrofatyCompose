package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource


@Composable
fun SenderComponent(model: SenderComponentModel){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                dimensionResource(id = R.dimen.default_margin16)
            ),
    ) {

        model.senderIcon?.let {
            AvatarComponent(it)
        }
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


