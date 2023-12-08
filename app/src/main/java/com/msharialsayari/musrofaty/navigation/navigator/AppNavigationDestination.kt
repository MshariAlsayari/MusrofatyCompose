package com.msharialsayari.musrofaty.navigation.navigator

import android.os.Parcelable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

interface AppNavigationDestination {

    fun route(): String

    val arguments: List<NamedNavArgument>
        get() = emptyList()

    val deepLinks: List<NavDeepLink>
        get() = emptyList()

    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
}
