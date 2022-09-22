package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource


@Composable
fun SmsComponent(modifier: Modifier =Modifier, model: SmsComponentModel){
    Column(modifier = modifier) {
        SenderInfoComponent(model.senderDisplayName, model.senderCategory, model.senderIcon)
        SmsComponent(model.body)
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SenderInfoComponent(senderName:String,senderCategory:String,senderIcon:Int?=null ){
    ListItem(
        text = { Text(text = senderName) },
        secondaryText = { Text(text = senderCategory)},
        icon = {
            senderIcon?.let {
                Icon(painter = painterResource(id = senderIcon), contentDescription = null)
            }
        }
    )

}

@Composable
private fun SmsComponent(body: String){
    TextComponent.BodyText(
        text = body
    )

}

data class SmsComponentModel(
    var id: String,
    var timestamp: Long = 0,
    var body: String = "",
    var smsType: String ="",
    var currency: String="",
    var senderDisplayName: String = "",
    var senderCategory: String = "",
    var senderIcon: Int? = null,
)
