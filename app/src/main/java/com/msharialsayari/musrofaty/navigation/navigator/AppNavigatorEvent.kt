package com.msharialsayari.musrofaty.navigation.navigator

import androidx.navigation.NavOptionsBuilder

sealed class AppNavigatorEvent {
    data class NavigateUp(val data: Map<String, Any>? = null) : AppNavigatorEvent()
    class Directions(val destination: String, val builder: NavOptionsBuilder.() -> Unit) :
        AppNavigatorEvent()
}
