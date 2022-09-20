package com.msharialsayari.musrofaty.ui.screens.sender_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.ui_component.ProgressBar


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SenderDetailsScreen(navController: NavController, senderId: Int) {
    val viewModel: SendersDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    viewModel.getSenderModel(senderId)
    Box(contentAlignment = Alignment.Center) {

        if (uiState.isLoading) {
            ProgressBar.CircleProgressBar()
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_display_name_ar)) },
                    trailing = { Text(text = uiState.sender?.displayNameAr ?: "") }
                )

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_display_name_en)) },
                    trailing = { Text(text = uiState.sender?.displayNameEn ?: "") }
                )

                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_pin)) },
                    trailing = { Switch(checked = uiState.isPin  , onCheckedChange = {check->
                        viewModel.pinSender(check)
                    }) }
                )


                ListItem(
                    text = { Text(text = stringResource(id = R.string.sender_active)) },
                    trailing = { Switch(checked = uiState.isActive , onCheckedChange = {check->
                        viewModel.activeSender(check)

                    }) }
                )

            }
        }


    }
}