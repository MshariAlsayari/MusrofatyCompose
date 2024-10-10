package com.msharialsayari.musrofaty.ui.screens.filter_screen

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.msharialsayari.musrofaty.ui.navigation.Screen
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.utils.mirror

@Composable
fun FilterScreenTopBar(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    AppBarComponent.TopBarComponent(
        title = Screen.FilterScreen.title,
        actions = {
            TopBarAction(viewModel)
        },
        onArrowBackClicked = { viewModel.navigateUp() }
    )

}

@Composable
private fun TopBarAction(viewModel: FilterViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    if (!uiState.isCreateNewFilter)
        Icon(
            Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .mirror()
                .clickable {
                    viewModel.updateConfirmationDialogStatus(true)
                })

}