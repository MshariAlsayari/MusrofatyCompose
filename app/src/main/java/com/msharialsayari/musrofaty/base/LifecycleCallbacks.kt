package com.msharialsayari.musrofaty.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifecycleCallbacks(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onDispose: (() -> Unit)? = null,
    onAny: (() -> Unit)? = null
) {
    // Safely update the current lambdas when a new one is provided
    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDestroy by rememberUpdatedState(onDestroy)
    val currentOnDispose by rememberUpdatedState(onDispose)
    val currentOnAny by rememberUpdatedState(onAny)

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    currentOnCreate?.invoke()
                }
                Lifecycle.Event.ON_START -> {
                    currentOnStart?.invoke()
                }
                Lifecycle.Event.ON_RESUME -> {
                    currentOnResume?.invoke()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    currentOnPause?.invoke()
                }
                Lifecycle.Event.ON_STOP -> {
                    currentOnStop?.invoke()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    currentOnDestroy?.invoke()
                }
                Lifecycle.Event.ON_ANY -> {
                    currentOnAny?.invoke()
                }
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            currentOnDispose?.invoke()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}