package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.utils.notEmpty


@Composable
fun SenderComponent(modifier: Modifier = Modifier, model: SenderComponentModel, onAvatarClicked:(SenderComponentModel)->Unit = {}){

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {


        AvatarComponent(icon = model.senderIconPath, onClicked = {onAvatarClicked(model)} )

        Column(modifier = Modifier
            .weight(1f)
            .padding(start = dimensionResource(id = R.dimen.default_margin16)),
            verticalArrangement = Arrangement.Center
        ) {
            if (model.displayName.notEmpty())
                TextComponent.HeaderText(text = model.displayName)
            if (model.senderType.notEmpty())
                TextComponent.PlaceholderText(text = model.senderType)
        }



    }

}



data class SenderComponentModel(
    var senderId:Int,
    var senderName:String="",
    var displayName:String="",
    var senderType:String="",
    var senderIconPath:String="",
)


