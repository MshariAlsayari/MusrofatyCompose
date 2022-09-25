package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


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
                Image(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .background(color = Color.White),
                    painter = painterResource(id = it), contentDescription = null)

            }
        }
    )

}

@Composable
private fun SmsComponent(body: String){
    TextComponent.BodyText(
        modifier=Modifier.padding(horizontal = dimensionResource(id = R.dimen.default_margin16)),
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
