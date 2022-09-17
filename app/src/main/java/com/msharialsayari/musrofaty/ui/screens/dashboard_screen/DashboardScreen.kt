package com.msharialsayari.musrofaty.ui.screens.dashboard_screen


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.ui_component.RowComponent

@Composable
fun DashboardScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {


        RowComponent.SenderRow("Bank Alsajhi", 50) {
            Log.i(
                "MshariTest",
                "Clicked sldflskjflk jsdflkjsd lfjsd lfjsdflk sdflsjd fljsdflslfl"
            )
        }
    }
}