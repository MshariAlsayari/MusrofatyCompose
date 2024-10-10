package com.msharialsayari.musrofaty.ui.screens.sms_permission_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.ui_component.ButtonComponent
import com.msharialsayari.requestpermissionlib.component.RequestPermissions
import com.msharialsayari.requestpermissionlib.model.DialogParams


@Composable
fun SmsPermissionScreen() {

    var openSms by remember { mutableStateOf(false) }

    if (openSms){
        RequestPermissions(
            permissions = listOf(android.Manifest.permission.READ_SMS),
            deniedDialogParams = DialogParams(
                title= R.string.sms_permission_denied_dialog_title,
                message = R.string.sms_permission_denied_dialog_message,
                positiveButtonText = R.string.permission_dialog_positive_button
            ),
            isGranted = {
                openSms = false
            },
            onDone = {
                openSms = false
            }
        )
    }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center,
        ) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title()
                Message()
            }


            ActionBtn(modifier = Modifier.align(Alignment.BottomCenter), onClick = {
                openSms = true
            })


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
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun ActionBtn(modifier: Modifier = Modifier, onClick: () -> Unit) {

    ButtonComponent.ActionButton(
        modifier = modifier.fillMaxWidth(),
        text = R.string.common_enable,
        onClick = onClick
    )

}