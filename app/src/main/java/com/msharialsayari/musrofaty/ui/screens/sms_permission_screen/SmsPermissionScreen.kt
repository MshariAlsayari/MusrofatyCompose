package com.msharialsayari.musrofaty.ui.screens.sms_permission_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.ButtonComponent


@Composable
fun SmsPermissionScreen(
    onActionBtnClick: () -> Unit
) {

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center,
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_sms),
                    tint = MusrofatyTheme.colors.iconBackgroundColor,
                    contentDescription = null
                )

                Title()
                Message()
            }


            ActionBtn(modifier = Modifier.align(Alignment.BottomCenter), onClick = onActionBtnClick)


        }
    }


}

@Composable
private fun Title(modifier: Modifier = Modifier) {


    Text(
        modifier = modifier,
        color = MusrofatyTheme.colors.onBackground,
        text = stringResource(id = R.string.sms_permission_title),
        fontWeight = FontWeight.Medium
    )


}

@Composable
private fun Message(modifier: Modifier = Modifier) {


    Text(
        modifier = modifier,
        color = MusrofatyTheme.colors.onBackground,
        text = stringResource(id = R.string.sms_permission_message),
        textAlign = TextAlign.Justify,
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun ActionBtn(modifier: Modifier = Modifier, onClick: () -> Unit) {

    ButtonComponent.ActionButton(
        modifier = modifier.fillMaxWidth(),
        text = R.string.permission_dialog_positive_button,
        onClick = onClick
    )

}