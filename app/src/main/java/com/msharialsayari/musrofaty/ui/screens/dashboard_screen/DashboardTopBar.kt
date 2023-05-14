package com.msharialsayari.musrofaty.ui.screens.dashboard_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui_component.AppBarComponent
import com.msharialsayari.musrofaty.utils.mirror

@Composable
fun DashboardTopBar(viewModel: DashboardViewModel,){

    val context = LocalContext.current
    val isSearchTopBar = remember { mutableStateOf(false) }

    AppBarComponent.SearchTopAppBar(
        title = BottomNavItem.Dashboard.title,
        isParent = true,
        isSearchMode = isSearchTopBar.value,
        onCloseSearchMode = {
            isSearchTopBar.value = false
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .mirror()
                        .clickable {
                            isSearchTopBar.value = !isSearchTopBar.value
                        })

                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .mirror()
                        .clickable {
                            viewModel.onDateRangeClicked()
                        })

                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier
                        .mirror()
                        .clickable {
                            viewModel.loadSms(context)

                        })
            }
        },
        onTextChange = {
            viewModel.onQueryChanged(it)
            viewModel.getData(context)
        },
        onSearchClicked = {
            viewModel.onQueryChanged(it)
            viewModel.getData(context)
        },


        )

}