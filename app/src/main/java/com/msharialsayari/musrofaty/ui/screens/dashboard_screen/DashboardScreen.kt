package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.msharialsayari.musrofaty.BottomNavigation
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.navigation.NavigationGraph
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.ui_component.TextComponent

@Composable
fun DashboardScreen() {
    Scaffold(
        topBar = {
            AppBarComponent.TopBarComponent(
                title = BottomNavItem.Dashboard.title,
                isParent = true
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        ) {


            TextComponent.HeaderText(text = "Dashboard")
        }

    }

}