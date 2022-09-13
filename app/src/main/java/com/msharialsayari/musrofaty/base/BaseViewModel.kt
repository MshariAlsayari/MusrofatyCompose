package com.msharialsayari.musrofaty.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<EVENT> : ViewModel() {

    private val _event = mutableStateOf<EVENT?>(null)
    val event: State<EVENT?> = _event

    protected fun setEvent(newEvent: () -> EVENT) {
        _event.value = newEvent()
    }
}