package com.msharialsayari.musrofaty.navigation.navigator

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface AppNavigator {

    fun navigateUp(data: Map<String, Any>? = null): Boolean

    fun navigate(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = { }
    ): Boolean


    val destinations: Flow<AppNavigatorEvent>
}
