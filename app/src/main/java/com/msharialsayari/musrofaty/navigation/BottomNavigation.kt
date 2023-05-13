package com.msharialsayari.musrofaty.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.msharialsayari.musrofaty.ui.navigation.BottomNavItem
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme
import com.msharialsayari.musrofaty.utils.mirror


@Composable
fun BottomNavigation(navController: NavController, items:List<BottomNavItem>, bottomBarState:MutableState<Boolean>) {

    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        content = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colors.background,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(id = item.icon),
                                contentDescription = stringResource(item.title!!),
                                modifier = Modifier.mirror()
                            )
                        },
                        label = { Text(text = stringResource(item.title!!), fontSize = 9.sp) },
                        selectedContentColor = MusrofatyTheme.colors.selectedItemColor,
                        unselectedContentColor = MusrofatyTheme.colors.onBackground.copy(0.4f),
                        alwaysShowLabel = true,
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {

                                navController.graph.startDestinationRoute?.let { screen_route ->
                                    popUpTo(screen_route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    )
}