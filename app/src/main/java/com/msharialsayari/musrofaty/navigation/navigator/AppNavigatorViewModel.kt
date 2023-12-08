package com.msharialsayari.musrofaty.navigation.navigator

import androidx.lifecycle.ViewModel
import com.msharialsayari.musrofaty.navigation.navigator.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigatorViewModel @Inject constructor(
    private val appNavigator: AppNavigator
) : ViewModel(), AppNavigator by appNavigator
