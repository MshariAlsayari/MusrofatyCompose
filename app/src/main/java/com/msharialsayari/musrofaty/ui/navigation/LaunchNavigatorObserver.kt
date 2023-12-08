package com.msharialsayari.musrofaty.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LaunchNavigatorObserver(
    navigatorState: Flow<AppNavigatorEvent?>,
    navController: NavHostController
) {
    LaunchedEffect(true) {
        navigatorState
            .collectLatest { navigatorEvent ->
                try {
                    when (val event = navigatorEvent) {
                        null -> {}
                        is AppNavigatorEvent.NavigateUp -> {
                            for (datum in event.data ?: emptyMap()) {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    datum.key,
                                    datum.value
                                )
                            }
                            navController.navigateUp()
                        }

                        is AppNavigatorEvent.Directions -> {
                            Log.d(
                                "LaunchNavigatorObserver",
                                "navigator event destination is ${event.destination}"
                            )
                            navController.navigate(
                                event.destination
                            ) {
                                event.builder.invoke(this)
                            }
                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }
}