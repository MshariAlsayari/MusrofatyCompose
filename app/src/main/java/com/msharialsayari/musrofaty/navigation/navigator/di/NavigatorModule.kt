package com.simplemobiletools.calendar.navigation.navigator.di

import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Binds
    abstract fun navigator(navigator: AppNavigatorImpl): AppNavigator
}
