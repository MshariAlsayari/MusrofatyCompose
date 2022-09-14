package com.msharialsayari.musrofaty.ui_component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

object DialogComponent {

    @Composable
    fun MusrofatyDialog(title:String="", message:String="", positiveBtnText:String?=null, negativeBtnText:String?=null, onClickPositiveBtn:()->Unit={}, onClickNegativeBtn:()->Unit={} ){

        Dialog(onDismissRequest = { }) {
            Card(modifier = Modifier
                .width(300.dp)
                .height(150.dp)) {
                Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_margin16))) {

                    TextComponent.HeaderText(
                        text = title
                    )

                    TextComponent.BodyText(
                        modifier= Modifier.weight(1f),
                        text = message
                    )

                    Row ( modifier= Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End){

                        if (negativeBtnText?.isNotEmpty()==true)
                        TextButton(onClick = onClickNegativeBtn) {
                            Text(text = negativeBtnText)
                        }

                        if (positiveBtnText?.isNotEmpty()==true)
                        TextButton(onClick = onClickPositiveBtn) {
                            Text(text = positiveBtnText)
                        }

                    }

                }
            }
        }

    }
}