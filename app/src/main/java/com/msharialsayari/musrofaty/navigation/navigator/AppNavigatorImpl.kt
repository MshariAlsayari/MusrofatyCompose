package com.msharialsayari.musrofaty.navigation.navigator

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigatorImpl @Inject constructor() : AppNavigator {

    private val navigationEvents = Channel<AppNavigatorEvent>()

    override fun navigateUp(data: Map<String, Any>?): Boolean {
        return navigationEvents.trySend(AppNavigatorEvent.NavigateUp(data))
            .isSuccess
    }

    override fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit): Boolean {
        return AppNavigatorEvent.Directions(
            destination = route,
            builder = builder
        ).run {
            navigationEvents.trySend(this)
                .isSuccess
        }
    }

    override val destinations: Flow<AppNavigatorEvent>
        get() = navigationEvents.receiveAsFlow()
}
